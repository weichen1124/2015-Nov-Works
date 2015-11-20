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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/scheduletask/PfrestKeepAliveService.java $
* 
* PF-16523, 04/29/15, DChen, should be 16523 not 16522.
* 
* PF-16522, 04/29/15, DChen, re organize keep alive service.
* 
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

import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.pfrest.events.AliveEvent;
import com.dds.pathfinder.pfrest.events.PostEvent;


@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Startup
@Singleton
@Remote(PfrestKeepAliveServiceRemote.class)
@Local(PfrestKeepAliveServiceLocal.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Lock(LockType.READ)
public class PfrestKeepAliveService implements PfrestKeepAliveServiceLocal, PfrestKeepAliveServiceRemote{

	private Logger logger = Logger.getLogger(PfrestKeepAliveService.class);
	
	public final static String PFREST_SEND_ALIVE_TIMER = "PFRestSendAliveTimer";
	
	public final static String ATTR_NAME_TRANSIT_GW_HOST_URL="transit-gateway-host-url";
	public final static String ATTR_NAME_SCAN_EVENT_FREQ_SEC="transit-scan-event-in-sec";
	public final static String ATTR_NAME_POST_ALIVE_FREQ_SEC="post-alive-frequency-in-sec";
	
	@Resource
	private TimerService timerService;
	
	@EJB(lookup = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
	private LoadDispatchParametersLocal cachedParam;
	
	private static boolean otherPFEventTaskRunning = false;  //only one post events should be running
	public static int postAliveFreqInSec = 0; 
	private static String postAliveServerURL = ""; 		
	
	
	public void sendAliveTask() {
		if(postAliveServerURL == null || postAliveServerURL.trim().length() == 0) return;
		
		PostEvent aliveEvent = new PostEvent();
		aliveEvent.setDataResource(new AliveEvent());
		// String hostURL = getTransitGWHostURL();
		String hostURL = postAliveServerURL;
		
		if(hostURL != null){
			PfrestPostEventTask postTask = new PfrestPostEventTask(hostURL);
			postTask.postTGAliveEvent(aliveEvent);
		}else{
			logger.error("failed to create json with host URL null : " + hostURL);
		}
			
	}
	
	@PostConstruct
	public void startService()  {
		logger.info("start Pfrest keep alive service");
		try {
			setPostAliveEventTimer(); 
		} catch (Exception e) {
			logger.error("start Pfrest keep alive service:", e);
		}
	}
	
	@PreDestroy
	public void stopAllService() {
		stopTimerServices(PFREST_SEND_ALIVE_TIMER);
		setOtherPFEventTaskRunning(false);
	}
	
	public void setPostAliveEventTimer(){
		stopTimerServices(PFREST_SEND_ALIVE_TIMER);
		String tgServerURL = getPFRestServiceAttribute(ATTR_NAME_TRANSIT_GW_HOST_URL);
		int postAliveFreq =  getPFRestServiceAttributeAsInt(ATTR_NAME_POST_ALIVE_FREQ_SEC, 0);
		
		postAliveFreqInSec = postAliveFreq;
		postAliveServerURL = tgServerURL;
		
		if(tgServerURL == null || tgServerURL.trim().length() == 0 || postAliveFreq == 0) return;
		TimerConfig timerConfig = new TimerConfig();
		timerConfig.setPersistent(false);
		timerConfig.setInfo(PFREST_SEND_ALIVE_TIMER);
		timerService.createIntervalTimer((10 * 1000), (postAliveFreq * 1000 ), timerConfig);
	}
	
	@Timeout
	public void runKeepAliveTask(Timer timer){
		if(PFREST_SEND_ALIVE_TIMER.equals(timer.getInfo())){
			setOtherPFEventTaskRunning(true);
			try{
				sendAliveTask();
			}catch(Exception e){
				logger.error("sendAliveTask failed with exception: ", e);
			}finally{
				setOtherPFEventTaskRunning(false);
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
	
	public static boolean isOtherPFEventTaskRunning() {
		return otherPFEventTaskRunning;
	}

	@Lock(LockType.WRITE)
	public static void setOtherPFEventTaskRunning(boolean otherPFEventTaskRunning) {
		PfrestKeepAliveService.otherPFEventTaskRunning = otherPFEventTaskRunning;
	}
}
