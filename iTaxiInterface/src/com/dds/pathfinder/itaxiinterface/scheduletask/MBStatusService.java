/****************************************************************************
 * 
 *					Copyright (c), $Date: 2/04/14 5:11p $
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 * **************************************************************************
 * File Name: MBStatusService.java
 * C36130 Add GoFastCab Trip Status update.
 * 
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/scheduletask/MBStatusService.java $
 * 
 * PF-16333, 12/11/14, DChen, some null exceptions in OSP.
 * 
 * PF-16093 Added new task for driver initiated payment.
 * 
 * PF-16074 Send updates based on job origin either MG2.0 or TaxiLim MG. 
 * 
 * 8     2/04/14 5:11p Ezhang
 * PF-15814 increase the httpsockettimeout to 30,000 miliseconds
 * 
 * 7     1/29/14 3:58p Ezhang
 * 
 * 6     1/13/14 2:39p Ezhang
 * PF-15814 change http client to CloseableHttpAsyncClient.
 * Added DEFAULT_HTTP_MAX_CONNECTION, remove scanMobileBookingGpsUpdate since mb doesn't need GPS update
 * 
 * 5     11/26/13 2:28p Ezhang
 * PF-15594 remove parameter S_MB_ENABL_MSG_TO_MB using canned message flag instead.
 * 
 * 4     11/12/13 2:48p Ezhang
 * PF-15594 added support for message to rider feature.
 * 
 * 3     4/03/13 10:23a Dchen
 * use defined scan frequency.
 * 
 * 2     3/12/13 1:03p Dchen
 * Added pf parameters load bean.
 * 
 * 1     3/08/13 4:09p Dchen
 * Separate rider charge service from callbooker package.
 * 
 * 
 */
package com.dds.pathfinder.itaxiinterface.scheduletask;



import java.sql.Connection;
import java.sql.SQLException;
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

import com.dds.pathfinder.itaxiinterface.util.Debug2;
//import org.jboss.ejb3.annotation.LocalBinding;
//import org.jboss.ejb3.annotation.RemoteBinding;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;



/**
 * @author ezhang
 * 
 */
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Startup
@Singleton
// @DependsOn(com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
@Remote(MBStatusServiceRemote.class)
// @RemoteBinding(jndiBinding = MBStatusServiceRemote.JNDI_BINDING)
@Local(MBStatusServiceLocal.class)
// @LocalBinding(jndiBinding = MBStatusServiceLocal.JNDI_BINDING)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Lock(LockType.READ)
public class MBStatusService implements MBStatusServiceRemote, MBStatusServiceLocal{
	
	
	@Resource(mappedName ="java:jboss/datasources/PathfinderDS")
	private DataSource pfDataSource;
	
	@Resource
	private TimerService timerService;
	
	@EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
	private LoadDispatchParametersLocal cachedParam;
	
	public static final int CHECK_CONFIG_RATE = 10;	    //10 minutes
	
	public static long scanMobileBookingTimerFreq = 0; // scan frequency in milli
	private static String mobileBooking_URL = ""; // the MobileBooking MG server URL
	private static String taxiLimo_URL = ""; // the TaxiLimo MG server URL
	//private static boolean scanMobileBookingGpsUpdate = false; // enable MobileBooking

	
	
	public final static int TASK_START_DELAY = 15;      // wait 15 secs before
														// starting another task
	
	public final static String OSPMB_TRIP_STATUS_TIMER = "OSPMB_TripStatusServiceTimer";
	public final static String OSPMB_CHECK_CONFIG_TIMER = "OSPMB_CheckConfigRate";
	
	public final static int DEFAULT_HTTP_MAX_CONNECTION = 100;
	
	public final static int DEFAULT_HTTP_CONNECTION_TIMEOUT = 30000; //increase from 10 to 30 secs

	
	private static boolean otherOSPMBTaskRunning = false;  //only one updating should be running
	
	private static Debug2 log = Debug2.getLogger(MBStatusService.class);




	

	
	@Schedule(minute="*/" + CHECK_CONFIG_RATE, hour="*", persistent=false, info=OSPMB_CHECK_CONFIG_TIMER)
	public void checkConfigTask() {
		
			/* Get MobileBooking Parameters */
			// String latest_MobileBooking_URL = cachedParam.getSystemParameterValue("S_MB_SERVER_URL"); 
			// String latest_TaxiLimo_URL = cachedParam.getSystemParameterValue("S_TL_SERVER_URL");
			String latest_MobileBooking_URL = cachedParam.getSystemParameterValue("S_MB_SERVER_URL",""); 
			String latest_TaxiLimo_URL = cachedParam.getSystemParameterValue("S_TL_SERVER_URL","");
			//String latest_MobileBooking_URL = "http://localhost:2905/StatusAlertService/statusAlert.svc/xml/notify";
			//String latest_MobileBooking_URL = "HTTP://192.168.50.160:90/STATUSALERT.SVC/XML/NOTIFY";
			log.debug("latest S_MB_SERVER_URL " + latest_MobileBooking_URL);
			log.debug("latest S_TL_SERVER_URL " + latest_TaxiLimo_URL);

			// get the latest scan frequency
			int latest_mb_scan_freq = cachedParam.getSystemParameterIntValue("S_MB_STAT_SCAN_FREQ");   //use seconds
			//int latest_mb_scan_freq = 5;
			log.debug("latest S_MB_STAT_SCAN_FREQ " + latest_mb_scan_freq );
			
			
			
			if(latest_mb_scan_freq <= 0 ){
				stopTimerServices(OSPMB_TRIP_STATUS_TIMER);
			}else{
				
				
				if (latest_mb_scan_freq != scanMobileBookingTimerFreq
						|| !latest_MobileBooking_URL.equalsIgnoreCase(mobileBooking_URL)
						|| !latest_TaxiLimo_URL.equalsIgnoreCase(taxiLimo_URL)){
					stopTimerServices(OSPMB_TRIP_STATUS_TIMER);
					mobileBooking_URL = latest_MobileBooking_URL;
					scanMobileBookingTimerFreq = latest_mb_scan_freq;
					taxiLimo_URL = latest_TaxiLimo_URL;
				
					setMBStatusTimer(TASK_START_DELAY);
				}else{
				}	
			}
	}



	@PostConstruct
	public void startService()  {
		log.info("start MB TripStatusService");
		try {
			checkConfigTask(); 
		} catch (Exception e) {
			log.error("MB TripStatusService: startService exception:", e);
		}
	}

	@PreDestroy
	public void stopAllService() {
		stopTimerServices(OSPMB_TRIP_STATUS_TIMER);
		stopTimerServices(OSPMB_CHECK_CONFIG_TIMER);
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
	
	
	public void setMBStatusTimer(int initialDuration){
		if(scanMobileBookingTimerFreq <= 0) return;
		
		TimerConfig timerConfig = new TimerConfig();
		timerConfig.setPersistent(false);
		timerConfig.setInfo(OSPMB_TRIP_STATUS_TIMER);
		int init = (initialDuration <= 0) ? TASK_START_DELAY : initialDuration;
		timerService.createIntervalTimer((init * 1000), (scanMobileBookingTimerFreq * 1000 ), timerConfig);
	}
	
	@Timeout
	public void runUpdateStatusTask(Timer timer){
		
		if(!isDataBaserunning(pfDataSource) || 
				isMGURLempty() || isOtherOSPMBTaskRunning() ) {
			return;
		}
		else{
			setOtherOSPMBTaskRunning(true);
			try{
				// log.warn("rider_debug: update status task started..............");
				MBTripUpdateStatusTask task = new MBTripUpdateStatusTask( mobileBooking_URL, taxiLimo_URL, pfDataSource, cachedParam);
				task.runUpdateStatusTask();
				task.runFareToRiderTask(); //PF-16093
				task.runMSgToRiderTask();
				// log.warn("rider_debug: update status task stopped..............");
			}catch(Exception e){
				log.error("runUpdateStatusTask failed with exception : " , e);
			}finally{
				setOtherOSPMBTaskRunning(false);
			}
		}
	}
	
	


	public static boolean isOtherOSPMBTaskRunning() {
		return otherOSPMBTaskRunning;
	}


	@Lock(LockType.WRITE)
	public static void setOtherOSPMBTaskRunning(boolean otherOSPMBTaskRunning) {
		MBStatusService.otherOSPMBTaskRunning = otherOSPMBTaskRunning;
	}
	
	//PF-16070 check database collection before running the status update task
	public boolean isDataBaserunning(DataSource pfDataSource){
		
		
		Connection con = null;
		boolean dbResult = false;
		try{
			con = pfDataSource.getConnection ();
			if(con != null){
				dbResult = true;
			}else{
				log.error("isDataBaserunning() Database connection is null");
			}
		}catch(Exception e){
			log.error("isDataBaserunning() Database connection exception error", e);
		}finally{
			if (con != null) try {con.close ();} catch (SQLException ignore) {};
		}
		//log.info("isDataBaserunning() " + dbResult);
		return dbResult;
		
		
	}
	
	//PF-16-74 check URL before running the status update task
	public boolean isMGURLempty(){
		boolean result = false;
		if((mobileBooking_URL == null || mobileBooking_URL.trim().length() == 0) &&
				(taxiLimo_URL == null ||taxiLimo_URL.trim().length() == 0)){
			result = true;
		}
		
		return result;
	}
	


	

}
