/****************************************************************************
 * 
 *					Copyright (c), $Date: 1/29/14 3:59p $
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 * **************************************************************************
 * File Name: TripStatusService.java
 * C36130 Add GoFastCab Trip Status update.
 * 
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/scheduletask/TripStatusService.java $
 * 
 * 5     1/29/14 3:59p Ezhang
 * PF-15809 Pass pf parameters load bean to individual task
 * 
 * 4     4/03/13 10:23a Dchen
 * use defined scan frequency.
 * 
 * 3     3/12/13 1:04p Dchen
 * Added pf parameters load bean.
 * 
 * 2     3/08/13 4:09p Dchen
 * Separate rider charge service from callbooker package.
 * 
 * 1     3/08/13 3:31p Dchen
 * Separate rider charge service from callbooker package.
 * 
 * 9     11/02/12 9:58a Ezhang
 * PF-14845 added MobileBooking status update logic, move JobInfo to different class, create two more new classes 
 * GFCTripUpdateStatusTask.java and MBTripUpdateStatusTask.java
 * 
 * 8     6/12/12 4:49p Ezhang
 * PF-14478 fixed one typo.
 * 
 * 7     6/05/12 2:44p Ezhang
 * code clean up and change FPF to PF JIRA number
 * 
 * 6     6/05/12 1:56p Ezhang
 * PF-14478 updated getJobInfo() to get correct driver name for gps update event.
 * 
 * PF-14473 update updateJobInfo() to avoid null pointer exception.
 * Updated run() to delete GPS update timer event.
 * 
 * 5     3/05/12 10:04a Yyin
 * C36678 - Fixed the problem in the query to get the job so that the job
 * can be retrieved when the job is not dispatched. Fixed the code to get
 * api_key.
 * 
 * 4     2/16/12 3:53p Yyin
 * C36569 - Fixed the typo in Run().
 * 
 * 3     1/27/12 5:57p Dchen
 * modify for trip status alert service.
 * 
 * 2     1/26/12 11:38a Yzhan
 * cleanup code
 * 
 * 1     12/28/11 5:27p Yzhan
 * Added for C36130.
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



/**
 * @author ezhang
 * 
 */
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Startup
@Singleton
// @DependsOn(com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
@Remote(TripStatusServiceRemote.class)
// @RemoteBinding(jndiBinding = TripStatusServiceRemote.JNDI_BINDING)
@Local(TripStatusServiceLocal.class)
// @LocalBinding(jndiBinding = TripStatusServiceLocal.JNDI_BINDING)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Lock(LockType.READ)
public class TripStatusService implements TripStatusServiceRemote, TripStatusServiceLocal{
	
	
	@Resource(mappedName =com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_PF_DATA_SOURCE)
	private DataSource pfDataSource;
	
	@Resource
	private TimerService timerService;
	
	@EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
	private LoadDispatchParametersLocal cachedParam;
	
	public static final int CHECK_CONFIG_RATE = 10;	    //10 minutes
	
	public static long scanGoFastCabTimerFreq = 0; // scan frequency in milli
													// seconds
	private static String goFastCab_URL = ""; // the GoFastCab server URL
	private static boolean scanGoFastCabGpsUpdate = false; // enable GoFastCab
															// GPS update
	
	public final static int TASK_START_DELAY = 15;      // wait 15 secs before
														// starting another task
	
	public final static String OSP_TRIP_STATUS_TIMER = "OSPTripStatusServiceTimer";
	public final static String OSP_CHECK_CONFIG_TIMER = "OSPCheckConfigRate";
	
	private static boolean otherOSPTaskRunning = false;  //only one updating should be running
	
	private static Debug2 log = Debug2.getLogger(TripStatusService.class);




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

	
	@Schedule(minute="*/" + CHECK_CONFIG_RATE, hour="*", persistent=false, info=OSP_CHECK_CONFIG_TIMER)
	public void checkConfigTask() {
		
			/* Get GoFastCab Parameters*/
			String latest_GoFastCab_URL = cachedParam.getSystemParameterValue("S_GFC_SERVER_URL");    //getStrSysParameter("S_GFC_SERVER_URL");
			log.debug("latest S_GFC_SERVER_URL " + latest_GoFastCab_URL);

			// get the latest scan frequency
			int latest_gfc_scan_freq = cachedParam.getSystemParameterIntValue("S_GFC_EVENT_TMR_FREQ");    //getIntSysParameter("S_GFC_EVENT_TMR_FREQ"); //use mili seconds
			log.debug("latest S_GFC_EVENT_TMR_FREQ " + latest_gfc_scan_freq + " sec");
			
			boolean latest_gfc_gps_scan = "Y".equalsIgnoreCase(cachedParam.getSystemParameterValue("S_GFC_GPS_SCAN_ENABL")); //getStrSysParameter("S_GFC_GPS_SCAN_ENABL"));
			log.debug("latest S_GFC_GPS_SCAN_ENABL " + latest_gfc_gps_scan);
			
			if(latest_GoFastCab_URL  == null || latest_GoFastCab_URL.trim().length() == 0 || latest_gfc_scan_freq <= 0){
				stopTimerServices(OSP_TRIP_STATUS_TIMER);
			}else{
				if(latest_gfc_scan_freq < TASK_START_DELAY) latest_gfc_scan_freq = TASK_START_DELAY;
				
				if (latest_gfc_scan_freq != scanGoFastCabTimerFreq
						|| !latest_GoFastCab_URL.equals(goFastCab_URL) ){
					stopTimerServices(OSP_TRIP_STATUS_TIMER);
					goFastCab_URL = latest_GoFastCab_URL;
					scanGoFastCabTimerFreq = latest_gfc_scan_freq;
					scanGoFastCabGpsUpdate = latest_gfc_gps_scan;
					setTripStatusTimer(TASK_START_DELAY);
				}else{
				}
				
			}
			
	}



	@PostConstruct
	public void startService()  {
		log.info("start OSP TripStatusService");
		try {
			checkConfigTask(); 
		} catch (Exception e) {
			log.error("OSP TripStatusService: startService exception:", e);
		}
	}

	@PreDestroy
	public void stopAllService() {
		stopTimerServices(OSP_TRIP_STATUS_TIMER);
		stopTimerServices(OSP_CHECK_CONFIG_TIMER);
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
	
	
	public void setTripStatusTimer(int initialDuration){
		if(scanGoFastCabTimerFreq <= 0) return;
		
		TimerConfig timerConfig = new TimerConfig();
		timerConfig.setPersistent(false);
		timerConfig.setInfo(OSP_TRIP_STATUS_TIMER);
		int init = (initialDuration <= 0) ? TASK_START_DELAY : initialDuration;
		timerService.createIntervalTimer((init * 1000), (scanGoFastCabTimerFreq * 1000 ), timerConfig);
	}
	
	
	@Timeout
	public void runUpdateStatusTask(Timer timer){
		
		if(goFastCab_URL == null || goFastCab_URL.trim().length() == 0 || isOtherOSPTaskRunning()) return;
		else{
			setOtherOSPTaskRunning(true);
			try{
				GFCTripUpdateStatusTask task = new GFCTripUpdateStatusTask(scanGoFastCabGpsUpdate, goFastCab_URL, pfDataSource, cachedParam);
				task.runUpdateStatusTask();
			}catch(Exception e){
				log.error("runUpdateStatusTask failed with exception : " , e);
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
		TripStatusService.otherOSPTaskRunning = otherOSPTaskRunning;
	}

	

}
