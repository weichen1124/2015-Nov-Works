/****************************************************************************
 * 
 *					Copyright (c), 2014
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 * **************************************************************************
 * File Name: TssAvmService.java
 * 
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/scheduletask/TssAvmService.java $
 * 
 * 9/15/14, DChen, PF-16183, to create avm request.
 * 
 * 9/11/14, DChen, PF-16183, TSS shared rider AVM part.
 *
 * 
 */
package com.dds.pathfinder.itaxiinterface.scheduletask;



import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.DependsOn;
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

//import org.jboss.ejb3.annotation.LocalBinding;
//import org.jboss.ejb3.annotation.RemoteBinding;

import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.util.Debug2;


@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Startup
@Singleton
// @DependsOn(com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
@Remote(TssAvmServiceRemote.class)
// @RemoteBinding(jndiBinding = TssAvmServiceRemote.JNDI_BINDING)
@Local(TssAvmServiceLocal.class)
// @LocalBinding(jndiBinding = TssAvmServiceLocal.JNDI_BINDING)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Lock(LockType.READ)
public class TssAvmService implements TssAvmServiceRemote, TssAvmServiceLocal{
	
	
	@Resource(mappedName ="java:jboss/datasources/PathfinderDS")
	private DataSource pfDataSource;
	
	@Resource
	private TimerService timerService;
	
	@EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
	private LoadDispatchParametersLocal cachedParam;
	
	public static final int CHECK_CONFIG_RATE = 10;	    //10 minutes
	
	public static long scanTssAvmTimerFreq = 0; // scan frequency in milli seconds
	private static String tssServerURL = ""; // the TSS server URL														
	
	public final static int TASK_START_DELAY = 15;      // wait 15 secs before
														// starting another task
	
	public final static String TSS_AVM_REQUEST_TIMER = "TSSAvmRequestTimer";
	public final static String TSS_CHECK_CONFIG_TIMER = "TSSCheckConfigRate";
	
	private static boolean otherOSPTaskRunning = false;  //only one updating should be running
	
	private static Debug2 log = Debug2.getLogger(TssAvmService.class);




	/**
	 * Trip Alert Request status.
	 */
	protected enum SEND_STATUS {
		SEND_REQ_INVALID, // Request was invalid
		SEND_HTTP_FAILED, // Send failed, HTTP status not OK
		SEND_EXCEPTION, // Exception occurred during sending of request
		SEND_OK, RESP_FAILED_PROCESS
		// Failed to process response
	};

	
	@Schedule(minute="*/" + CHECK_CONFIG_RATE, hour="*", persistent=false, info=TSS_CHECK_CONFIG_TIMER)
	public void checkConfigTask() {
		
			/* Get GoFastCab Parameters*/
			String latestTSSServerURL = cachedParam.getSystemParameterValue("S_TSS_SERVER_URL");    
			log.debug("latest S_TSS_SERVER_URL " + latestTSSServerURL);

			// get the latest scan frequency
			int latestTssScanFreq = cachedParam.getSystemParameterIntValue("S_TSS_AVM_SCAN_FREQ");    
			log.debug("latest S_TSS_AVM_SCAN_FREQ " + latestTssScanFreq + " sec");
			
			if(latestTSSServerURL  == null || latestTSSServerURL.trim().length() == 0 || latestTssScanFreq <= 0){
				stopTimerServices(TSS_AVM_REQUEST_TIMER);
				scanTssAvmTimerFreq = 0;
				tssServerURL = "";
			}else{
				if (latestTssScanFreq != scanTssAvmTimerFreq
						|| !latestTSSServerURL.equals(tssServerURL) ){
					stopTimerServices(TSS_AVM_REQUEST_TIMER);
					tssServerURL = latestTSSServerURL;
					scanTssAvmTimerFreq = latestTssScanFreq;
					setTSSAvmRequestTimer(TASK_START_DELAY);
				}else{
				}
				
			}
			
	}



	@PostConstruct
	public void startService()  {
		log.info("start OSP TssAvmService");
		try {
			checkConfigTask(); 
		} catch (Exception e) {
			log.error("OSP TssAvmService: startService exception:", e);
		}
	}

	@PreDestroy
	public void stopAllService() {
		stopTimerServices(TSS_AVM_REQUEST_TIMER);
		stopTimerServices(TSS_CHECK_CONFIG_TIMER);
		setOtherOSPTaskRunning(false);
	}
	
	
	@Lock(LockType.WRITE)
	public void stopTimerServices(String timerInfo){
		if(timerInfo == null || timerInfo.isEmpty()) return;
		
		Collection<Timer> timers = timerService.getTimers();
		if(timers != null && timers.size() > 0){
			// log.warn("rider_debug: stopUpdateStatusService called: " + timers.size());
			for(Timer timer : timers){
				// log.warn("rider_debug: check timer: ......." + timer.getInfo());
				if(timer != null && timerInfo.equals(timer.getInfo())) timer.cancel();
			}
		}
	}
	
	
	public void setTSSAvmRequestTimer(int initialDuration){
		if(scanTssAvmTimerFreq <= 0) return;
		
		TimerConfig timerConfig = new TimerConfig();
		timerConfig.setPersistent(false);
		timerConfig.setInfo(TSS_AVM_REQUEST_TIMER);
		int init = (initialDuration <= 0) ? TASK_START_DELAY : initialDuration;
		timerService.createIntervalTimer((init * 1000), (scanTssAvmTimerFreq * 1000 ), timerConfig);
	}
	
	
	@Timeout
	public void runTssAvmRequestTask(Timer timer){
		
		if(tssServerURL == null || tssServerURL.trim().length() == 0 || isOtherOSPTaskRunning()) return;
		else{
			setOtherOSPTaskRunning(true);
			try{
				TssAvmRequestTask task = new TssAvmRequestTask(tssServerURL, pfDataSource);
				task.runUpdateStatusTask();
			}catch(Exception e){
				log.error("runTssAvmRequestTask failed with exception : " , e);
			}finally{
				setOtherOSPTaskRunning(false);
			}
		}
	}
	
	

	public static boolean isOtherOSPTaskRunning() {
		return otherOSPTaskRunning;
	}


	@Lock(LockType.WRITE)
	public static void setOtherOSPTaskRunning(boolean otherOSPTaskRunning) {
		TssAvmService.otherOSPTaskRunning = otherOSPTaskRunning;
	}

	

}
