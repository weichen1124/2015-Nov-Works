/****************************************************************************
*
*		   		    Copyright (c), 2015
*					All rights reserved
*
*					DIGITAL DISPATCH SYSTEMS, INC
*					RICHMOND, BRITISH COLUMBIA
*					CANADA
*
****************************************************************************
*
*
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/scheduletask/PfrestNotifyService.java $
* 
* PF-16653, 07/17/15, DChen, add event type for TG.
* 
* PF-16597, 06/09/15, DChen, add system id in pfrest.
* 
* PF-16523, 04/30/15, DChen, using pfwebservices.xml to configure.
* 
* PF-16523, 04/29/15, DChen, should be 16523 not 16522.
* 
* PF-16522, 04/29/15, DChen, re organize keep alive service.
* 
* PF-16428, 03/25/15, DChen, pfrest job events service.
* 
* PF-16428, 03/13/15, DChen, based on json-api format
* 
* PF-16428, 03/06/15, DChen, pfrest notification service.
* 
* PF-16428, 03/04/15, DChen, pfrest notification service.
* 
* **************************************/
package com.dds.pathfinder.pfrest.scheduletask;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.sql.DataSource;

import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.jobsearch.ejb.JobSearchLocal;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.callbooker.server.util.PFServiceConfig;


@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Startup
@Singleton
@Remote(PfrestNotifyServiceRemote.class)
@Local(PfrestNotifyServiceLocal.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Lock(LockType.READ)
public class PfrestNotifyService implements PfrestNotifyServiceLocal, PfrestNotifyServiceRemote {
	
	private Logger logger = Logger.getLogger(PfrestNotifyService.class);
	
	@Resource(lookup ="java:jboss/datasources/PathfinderDS")
	private DataSource pfDataSource;
	
	@Resource
	private TimerService timerService;
	
	@EJB(lookup = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
	private LoadDispatchParametersLocal cachedParam;
	
	@EJB(mappedName= com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + JobSearchLocal.JNDI_BINDING)
	private JobSearchLocal jobSearchLocal;
	
	public static final int SEND_ALIVE_RATE = 1;	    //1 minute
	public static final int CHECK_CONFIG_RATE = 10;	    //10 minutes
	public final static int TASK_START_DELAY = 15;      // wait 15 secs before
	
	public static long scanTGEventTimerFreq = 0; // scan frequency in milli seconds
	private static String eventTGServerURL = ""; // the TSS server URL				
	
	public final static String PFREST_CHECK_CONFIG_TIMER = "PFRestCheckConfigRate";
	public final static String PFREST_SEND_EVENTS_TIMER = "PFRestSendEventsTimer";
	
	public final static String PFREST_NOTIFY_RECIPIENTS = "pfnotify-recipients";
	public final static String PFREST_TRANS_GW_SERVER_NAME = "TRANS_GATEWAY_NOTIFY_SERVER";
	public final static String PFREST_DEFAULT_TG_RECIPIENT = "TG";
	
	
	private static boolean otherPFEventTaskRunning = false;  //only one post events should be running
	
	
	@Schedule(minute="*/" + CHECK_CONFIG_RATE, hour="*", persistent=false, info=PFREST_CHECK_CONFIG_TIMER)
	public void checkConfigTask() {
		
			/* Get GoFastCab Parameters*/
			// String latestTGServerURL = cachedParam.getSystemParameterValue("S_TG_SERVER_URL");    
			String latestTGServerURL = getPFRestServiceAttribute(PfrestKeepAliveService.ATTR_NAME_TRANSIT_GW_HOST_URL);
			logger.debug("latest transit-gateway-host-url " + latestTGServerURL);

			// get the latest scan frequency
			//int latestTGEventScanFreq = cachedParam.getSystemParameterIntValue("S_TG_EVENT_SCAN_FREQ");
			int latestTGEventScanFreq = getPFRestServiceAttributeAsInt(PfrestKeepAliveService.ATTR_NAME_SCAN_EVENT_FREQ_SEC, 0);
			logger.debug("latest S_TG_EVENT_SCAN_FREQ " + latestTGEventScanFreq + " sec");
			
			if(latestTGServerURL  == null || latestTGServerURL.trim().length() == 0 || latestTGEventScanFreq <= 0){
				stopTimerServices(PFREST_SEND_EVENTS_TIMER);
				scanTGEventTimerFreq = 0;
				eventTGServerURL = "";
			}else{
				if (latestTGEventScanFreq != scanTGEventTimerFreq
						|| !latestTGServerURL.equals(eventTGServerURL) ){
					stopTimerServices(PFREST_SEND_EVENTS_TIMER);
					eventTGServerURL = latestTGServerURL;
					scanTGEventTimerFreq = latestTGEventScanFreq;
					setPostTGEventTimer(TASK_START_DELAY);
				}else{
				}
				
			}
			
	}
	
	
	
	public void setPostTGEventTimer(int initialDuration){
		if(scanTGEventTimerFreq <= 0) return;
		
		TimerConfig timerConfig = new TimerConfig();
		timerConfig.setPersistent(false);
		timerConfig.setInfo(PFREST_SEND_EVENTS_TIMER);
		int init = (initialDuration <= 0) ? TASK_START_DELAY : initialDuration;
		timerService.createIntervalTimer((init * 1000), (scanTGEventTimerFreq * 1000 ), timerConfig);
	}
	
	
	@PostConstruct
	public void startService()  {
		logger.info("start Pfrest notify service");
		try {
			checkConfigTask(); 
		} catch (Exception e) {
			logger.error("start Pfrest notify service:", e);
		}
	}
	
	@PreDestroy
	public void stopAllService() {
		stopTimerServices(PFREST_SEND_EVENTS_TIMER);
		stopTimerServices(PFREST_CHECK_CONFIG_TIMER);
		setOtherPFEventTaskRunning(false);
	}
	
	@Lock(LockType.WRITE)
	public void stopTimerServices(String timerInfo){
		if(timerInfo == null || timerInfo.isEmpty()) return;
		
		Collection<Timer> timers = timerService.getTimers();
		if(timers != null && timers.size() > 0){
			for(Timer timer : timers){
				if(timer != null && timerInfo.equals(timer.getInfo())) timer.cancel();
			}
		}
	}


	@Timeout
	public void runPostTGEventsTask(Timer timer){
		
		if(eventTGServerURL == null || eventTGServerURL.trim().length() == 0 || isOtherPFEventTaskRunning() || !PFREST_SEND_EVENTS_TIMER.equals(timer.getInfo())) return;
		else{
			setOtherPFEventTaskRunning(true);
			try{
				PfrestPostEventTask postTask = new PfrestPostEventTask(eventTGServerURL, pfDataSource, getPFNotifyRecipients(), cachedParam, jobSearchLocal);
				postTask.runSendTGEventsTask();
			}catch(Exception e){
				logger.error("runPostTGEventsTask failed with exception : " , e);
			}finally{
				setOtherPFEventTaskRunning(false);
			}
		}
		
	}
	
	
	public String getTransitGWHostURL(){
		if(cachedParam != null && cachedParam.getPFRestConfigAttributes() != null){
			return cachedParam.getPFRestConfigAttributes().get(PFServiceConfig.ATTR_NAME_TRANSIT_GW_HOST_URL);
		}else{
			return null;
		}
	}
	
	public String getPFRestServiceAttribute(String attrName){
		if(attrName == null || attrName.trim().length() == 0) return attrName;
		if(cachedParam != null && cachedParam.getPFRestConfigAttributes() != null){
			return cachedParam.getPFRestConfigAttributes().get(attrName);
		}else{
			return null;
		}
	}
	
	public int getPFRestServiceAttributeAsInt(String attrName, int defaultValue){
		if(attrName == null || attrName.trim().length() == 0) return defaultValue;
		if(cachedParam != null && cachedParam.getPFRestConfigAttributes() != null){
			String strValue = cachedParam.getPFRestConfigAttributes().get(attrName);
			if(strValue == null || strValue.trim().length() == 0) return defaultValue;
			try{
				return Integer.parseInt(strValue);
			}catch(NumberFormatException e){
				return defaultValue;
			}
		}else{
			return defaultValue;
		}
	}
	
	public String[] getPFNotifyRecipients(){
		String recips = getPFRestServiceAttribute(PFREST_NOTIFY_RECIPIENTS);
		String[] recipients = new String[]{PFREST_DEFAULT_TG_RECIPIENT};
		if(recips != null && recips.trim().length() > 0){
			recipients = recips.split(",");
		}
		
		for(int i =0 ; i< recipients.length ; i++){
			recipients[i] = (PFREST_TRANS_GW_SERVER_NAME + recipients[i]);
		}	
		return recipients;
	}


	public static boolean isOtherPFEventTaskRunning() {
		return otherPFEventTaskRunning;
	}


	@Lock(LockType.WRITE)
	public static void setOtherPFEventTaskRunning(boolean otherPFEventTaskRunning) {
		PfrestNotifyService.otherPFEventTaskRunning = otherPFEventTaskRunning;
	}
	
	

}
