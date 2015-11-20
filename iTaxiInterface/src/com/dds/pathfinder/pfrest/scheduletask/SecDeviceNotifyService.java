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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/scheduletask/SecDeviceNotifyService.java $
* 
* PF-16607, 08/25/15, DChen, 2nd device bug fix.
* 
* PF-16607, 08/21/15, DChen, add event id and use diff url to notify
* 
* PF-16606, 08/17/15, DChen, job offer event.
* 
* PF-16605, 08/07/15, DChen, temp off event notification
* 
* PF-16605, 06/18/15, DChen, created for 2nd device.
* 
* **************************************/
package com.dds.pathfinder.pfrest.scheduletask;

import java.io.File;
import java.io.IOException;
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
import com.dds.pathfinder.pfrest.events.SecDevJobOfferEvent;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Startup
@Singleton
@Remote(SecDeviceNotifyServiceRemote.class)
@Local(SecDeviceNotifyServiceLocal.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Lock(LockType.READ)
public class SecDeviceNotifyService implements SecDeviceNotifyServiceLocal, SecDeviceNotifyServiceRemote {
	
	private Logger logger = Logger.getLogger(SecDeviceNotifyService.class);
	
	@Resource(lookup ="java:jboss/datasources/PathfinderDS")
	private DataSource pfDataSource;
	
	@Resource
	private TimerService timerService;
	
	@EJB(lookup = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
	private LoadDispatchParametersLocal cachedParam;
	
	@EJB(mappedName= com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + JobSearchLocal.JNDI_BINDING)
    private JobSearchLocal jobSearchLocal;
	
	public static final int CHECK_CONFIG_RATE = 10;	    //10 minutes
	public final static int TASK_START_DELAY = 15;      // wait 15 secs before
					
	public static long scanSDEventTimerFreq = 0; // scan frequency in milli seconds
	private static String eventSDJobServerURL = ""; // the mb for job event server URL
	private static String eventSDVehServerURL = ""; // the mb for vehicle event server URL
	
	public final static String SD_CHECK_CONFIG_TIMER = "PFSDCheckConfigRate";
	public final static String SD_SEND_EVENTS_TIMER = "PFSDSendEventsTimer";
	
	public final static String PFREST_SD_NOTIFY_RECIPIENTS = "2nd-dev-notify-recipients";
	public final static String SEC_DEVICE_SERVER_NAME = "SECONDARY_DEVICE_NOTIFY_SERVER";
	public final static String PFREST_DEFAULT_SEC_DEV = "SD";
	
	
	// public final static String ATTR_NAME_SD_NOTIFY_HOST_URL="sd-notify-gateway-host-url";
	public final static String ATTR_NAME_SD_SCAN_EVENT_FREQ_SEC="sd-scan-event-in-sec";
	
	public final static String ATTR_NAME_SD_NOTIFY_JOBOFFER_URL="sd-notify-joboffer-host-url";
	public final static String ATTR_NAME_SD_NOTIFY_OUTVEH_URL="sd-notify-outvehicle-host-url";
	public final static String ATTR_NAME_SD_JOBOFFER_JSONFILE_NAME="sd-joboffer-json-file";
	
	
	private static boolean otherSDEventTaskRunning = false;  //only one post events should be running
	
	public static SecDevJobOfferEvent JobOfferEvent = null; 
	
	
	@Schedule(minute="*/" + CHECK_CONFIG_RATE, hour="*", persistent=false, info=SD_CHECK_CONFIG_TIMER)
	public void checkConfigTask() {
		
			String latestSDJobServerURL = getSDEventHostURL(ATTR_NAME_SD_NOTIFY_JOBOFFER_URL);
			String latestSDVehServerURL = getSDEventHostURL(ATTR_NAME_SD_NOTIFY_OUTVEH_URL);
			
			logger.info("latest sd-notify-gateway-host-url " + latestSDJobServerURL + ", " + latestSDVehServerURL);

			// get the latest scan frequency
			int latestSDEventScanFreq = getPFRestServiceAttributeAsInt(ATTR_NAME_SD_SCAN_EVENT_FREQ_SEC, 0);
			logger.info("latest sd-scan-event-in-sec " + latestSDEventScanFreq + " sec");
			
			if((latestSDJobServerURL  == null && latestSDVehServerURL == null)|| (latestSDJobServerURL.trim().length() == 0 && latestSDVehServerURL.trim().length() == 0) || latestSDEventScanFreq <= 0){
				stopTimerServices(SD_SEND_EVENTS_TIMER);
				scanSDEventTimerFreq = 0;
				eventSDJobServerURL = "";
				eventSDVehServerURL = "";
			}else{
				if (latestSDEventScanFreq != scanSDEventTimerFreq
						|| !eventSDJobServerURL.equals(latestSDJobServerURL)
						|| !eventSDVehServerURL.equals(latestSDVehServerURL)){
					stopTimerServices(SD_SEND_EVENTS_TIMER);
					eventSDJobServerURL = latestSDJobServerURL == null ? "" : latestSDJobServerURL;
					eventSDVehServerURL = latestSDVehServerURL == null ? "" : latestSDVehServerURL;
					scanSDEventTimerFreq = latestSDEventScanFreq;
					setPostSDEventTimer(TASK_START_DELAY);
				}else{
				}
				
			}
			
	}
	
	
	
	public void setPostSDEventTimer(int initialDuration){
		if(scanSDEventTimerFreq <= 0) return;
		
		setJobOfferEvent();
		
		TimerConfig timerConfig = new TimerConfig();
		timerConfig.setPersistent(false);
		timerConfig.setInfo(SD_SEND_EVENTS_TIMER);
		int init = (initialDuration <= 0) ? TASK_START_DELAY : initialDuration;
		timerService.createIntervalTimer((init * 1000), (scanSDEventTimerFreq * 1000 ), timerConfig);
	}
	
	
	@PostConstruct
	public void startService()  {
		logger.info("start 2nd device notify service");
		try {
			checkConfigTask(); 
		} catch (Exception e) {
			logger.error("start 2nd devide notify service:", e);
		}
	}
	
	@PreDestroy
	public void stopAllService() {
		stopTimerServices(SD_SEND_EVENTS_TIMER);
		stopTimerServices(SD_CHECK_CONFIG_TIMER);
		setOtherSDEventTaskRunning(false);
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
	public void runPostSDEventsTask(Timer timer){
		
		if((eventSDJobServerURL == null && eventSDVehServerURL == null) || (eventSDJobServerURL.trim().length() == 0 && eventSDVehServerURL.trim().length() == 0) 
				|| isOtherSDEventTaskRunning() || !SD_SEND_EVENTS_TIMER.equals(timer.getInfo())) return;
		else{
			setOtherSDEventTaskRunning(true);
			try{
				SecDevicePostEventTask postTask = new SecDevicePostEventTask(eventSDJobServerURL, eventSDVehServerURL, pfDataSource, getSDNotifyRecipients(), cachedParam, jobSearchLocal, JobOfferEvent);
				postTask.runSendSDEventsTask();
			}catch(Exception e){
				logger.error("runPostSDEventsTask failed with exception : " , e);
			}finally{
				setOtherSDEventTaskRunning(false);
			}
		}
		
	}
	
	
	public String getSDEventHostURL(String attrURL){
		if(cachedParam != null && cachedParam.getPFRestConfigAttributes() != null){
			return cachedParam.getPFRestConfigAttributes().get(attrURL);
		}else{
			return null;
		}
	}
	
	@Lock(LockType.WRITE)
	public void setJobOfferEvent(){
		if(cachedParam != null && cachedParam.getPFRestConfigAttributes() != null){
			String fileName = cachedParam.getPFRestConfigAttributes().get(ATTR_NAME_SD_JOBOFFER_JSONFILE_NAME);
			if(fileName != null && fileName.trim().length() > 0){
				File jsonFile = new File(System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator")+"wsdl" + System.getProperty("file.separator") + fileName);
				if(jsonFile.exists()){
					ObjectMapper mapper = new ObjectMapper();
					try {
						JobOfferEvent = mapper.readValue(jsonFile, SecDevJobOfferEvent.class);
						
					} catch (JsonParseException e) {
						logger.error("read json file failed, JsonParseException :", e);
					} catch (JsonMappingException e) {
						logger.error("read json file failed, JsonMappingException :", e);
					} catch (IOException e) {
						logger.error("read json file failed, IOException :", e);
					}
				}else{
					logger.error("json file not exist: " + jsonFile.getAbsolutePath());
				}
				
			}
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
	
	public String[] getSDNotifyRecipients(){
//		String recips = getPFRestServiceAttribute(PFREST_SD_NOTIFY_RECIPIENTS);
//		String[] recipients = new String[]{PFREST_DEFAULT_SEC_DEV};
//		if(recips != null && recips.trim().length() > 0){
//			recipients = recips.split(",");
//		}
//		
//		for(int i =0 ; i< recipients.length ; i++){
//			recipients[i] = (SEC_DEVICE__SERVER_NAME + recipients[i]);
//		}	
		String[] recipients = new String[]{SEC_DEVICE_SERVER_NAME};
		return recipients;
	}


	public static boolean isOtherSDEventTaskRunning() {
		return otherSDEventTaskRunning;
	}
	
	


	public static SecDevJobOfferEvent getJobOfferEvent() {
		return JobOfferEvent;
	}



	public static void setJobOfferEvent(SecDevJobOfferEvent jobOfferEvent) {
		JobOfferEvent = jobOfferEvent;
	}



	@Lock(LockType.WRITE)
	public static void setOtherSDEventTaskRunning(boolean otherSDEventTaskRunning) {
		SecDeviceNotifyService.otherSDEventTaskRunning = otherSDEventTaskRunning;
	}
	
	

}
