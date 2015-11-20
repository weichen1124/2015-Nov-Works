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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/scheduletask/MBTripUpdateStatusTask.java $
 * Oct 5, 2015 Y Yin
 * PF-16808. Renamed fare to totalAmount. Added meterFare and otherExpenses.
 * 
 * Aug 13, 2015 YYin
 * PF-16707. Fixed infinite loops while having problem to get alerts.
 * Y Yin May 29, 2015
 * PF-16093. Changed driver intiated payment to use get_mb_drv_init_pay_msg and implemented GPS.
 * 
 * PF-16093 Add driver initiated payment event
 * 
 * PF-16074 Send updates based on job origin either MG2.0 or TaxiLim MG
 * 
 * 13    2/04/14 5:11p Ezhang
 * PF-15814 fixed NullPointerException in status update callback.
 * added API key to sendRiderMsgRequest()
 * 
 * 12    1/29/14 3:59p Ezhang
 * PF-15809 Pass pf parameters load bean to individual task
 * 
 * 11    1/17/14 5:08p Ezhang
 * PF-15814 fixed a typo
 * 
 * 10    1/13/14 2:39p Ezhang
 * PF-15814 change http client to CloseableHttpAsyncClient.
 * 
 * 9     12/13/13 10:50a Ezhang
 * remove MSG_TO_RIDER_HEADER. MG will add. call sendRiderMsgAck() only after successful http post.
 * 
 * 8     12/09/13 4:23p Ezhang
 * fixed httpEntity content Type after upgrade of http client.
 * 
 * 7     11/28/13 6:40p Dchen
 * upgrade http client to 4.3
 * 
 * 6     11/28/13 6:27p Dchen
 * upgrade http client to 4.3
 * 
 * 5     11/26/13 2:28p Ezhang
 * PF-15594 remove parameter S_MB_ENABL_MSG_TO_MB using canned message flag instead.
 * 
 * 4     11/20/13 11:24a Sfoladian
 * PF-15597- Late Trip Notification Support
 * 
 * 3     11/12/13 5:00p Ezhang
 * PF-15594 added sendRiderMsgAck() to send ack text msg to MDT.
 * 
 * 2     11/12/13 2:49p Ezhang
 * PF-15594 added runMsgToRiderTask(), sendRiderMsgRequest() for msg to rider event.
 * 
 * 1     3/08/13 3:31p Dchen
 * Separate rider charge service from callbooker package.
 * 
 * 2     11/02/12 12:58p Ezhang
 * PF-14845 code clean up
 * 
 * 1     11/02/12 9:53a Ezhang
 * 
 * PF-14845  OSP status update to support Mobile booking status update
 * 
 */
package com.dds.pathfinder.itaxiinterface.scheduletask;


import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import javax.sql.DataSource;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.util.Debug2;


/**
 * This class will send Mobile Booking server the job status update.
 * delete the event in timers table when it's done
 * @author ezhang
 *
 */
public class MBTripUpdateStatusTask implements UpdateStatusTaskInterface{
	
	private static Debug2 log = Debug2.getLogger(MBTripUpdateStatusTask.class);
	//boolean scanMobileBookingGpsUpdate = false;

	String mobileBooking_URL = null;
	String taxiLimo_URL = null;
	public final static int HTTP_CONNECTION_TIMEOUT = 90000; // 90 seconds
	public final static String MSG_PRIORITY_URGENT = "U";
	
	private int maxHttpConnection = MBStatusService.DEFAULT_HTTP_MAX_CONNECTION;
	
	private int httpConnectionTimeout = MBStatusService.DEFAULT_HTTP_CONNECTION_TIMEOUT;

	
	private DataSource pfDataSource;
	
	private LoadDispatchParametersLocal cachedParam;
	
	public MBTripUpdateStatusTask(String toMGURL, String toTaxiLimoURL, DataSource dataSource, LoadDispatchParametersLocal cachedParam){
		//scanMobileBookingGpsUpdate = gpsScan; //mb does not have gps update
		mobileBooking_URL = toMGURL;
		taxiLimo_URL = toTaxiLimoURL;
		pfDataSource = dataSource;
		this.cachedParam = cachedParam;
	}
	

	public void runUpdateStatusTask() {
	
		int jobId = -1;
		int taxi_co_Id = -1;
		Date time = null;
		int event = -1;
		String jobRef = ""; //PF-16074
		Connection con = null;
	    CallableStatement cs = null;
	    boolean getMbEvent = true;
	  
		ArrayList<HttpPost> statusUpdatePosts = new ArrayList<HttpPost>();
		while(getMbEvent){
			try {
				
				//PF-15814 change to CloseableHttpAsyncClient
				con = pfDataSource.getConnection ();
				if(con != null) {
					
					cs = con.prepareCall ("{call jobpf.get_mb_event (?,?,?,?,?)}");
					
					cs.registerOutParameter (1, java.sql.Types.INTEGER); //event_id
					cs.registerOutParameter (2, java.sql.Types.INTEGER); //job_id
					cs.registerOutParameter (3, java.sql.Types.INTEGER); //taxi_co_id
					cs.registerOutParameter (4, java.sql.Types.TIMESTAMP); //event dtm
					cs.registerOutParameter (5, java.sql.Types.VARCHAR); //job_ref
					
					cs.execute ();
					event = cs.getInt(1);
					jobId = cs.getInt(2);
					taxi_co_Id = cs.getInt(3);
					time = cs.getTimestamp(4);
					jobRef = cs.getString(5);
					
					log.debug("event " + event);
					log.debug("jobId " + jobId);
					log.debug("taxiCoId " + taxi_co_Id);
					log.debug("time " + time);
					log.debug("jobRef " + jobRef);
					
					if(jobId > 0) {
						HttpPost post = null;
						post = sendTripUpdateRequest(jobId, event, taxi_co_Id, time, jobRef);
						
						if(post != null) {
							statusUpdatePosts.add(post);
						}
					}else {
						getMbEvent = false;
						log.debug("no mb status update");
					}
				}else {
					getMbEvent = false;
					log.debug("no pf db connection");
				}

			}catch(SQLException se){
				getMbEvent = false;
	      	    log.error ("runUpdateStatusTask() failed with exception", se);
	      	   
	        }
		    finally
		    {
	            if (cs != null) try {cs.close ();} catch (SQLException ignore) {};
	    	    if (con != null) try {con.close ();} catch (SQLException ignore) {};
				
	        }
		}
		
		if(statusUpdatePosts != null && statusUpdatePosts.size() > 0) {
			sendHttpPosts(statusUpdatePosts);
		}
	}
	
	
	public void runMSgToRiderTask() {
		String carNum = null;
		int mdtId = -1;
		String msg = null;
		int jobId = -1;
		int taxi_co_id = -1;
		Date time = null;
		Connection con = null;
	    CallableStatement cs = null;
	    boolean getRiderMsg = true;
	    String jobRef = null;
	    
		ArrayList<HttpPost> riderMsgPosts = new ArrayList<HttpPost>();
	
		
		while(getRiderMsg) {
			try {
				
					//check pf_messages table to get all the messages for 'MBUSR'
				
					con = pfDataSource.getConnection ();
					cs = con.prepareCall ("{call aqueues.get_mb_user_msg (?,?,?,?,?,?)}");
					
					cs.registerOutParameter (1, java.sql.Types.INTEGER); //mdt_id
					cs.registerOutParameter (2, java.sql.Types.VARCHAR); //mdt_msg
					cs.registerOutParameter (3, java.sql.Types.INTEGER); //taxi_ride_id
					cs.registerOutParameter (4, java.sql.Types.INTEGER); //taxi_co_id
					cs.registerOutParameter (5, java.sql.Types.TIMESTAMP); //event dtm
					cs.registerOutParameter(6, java.sql.Types.VARCHAR); //job_ref pf-16074
					
					cs.execute ();
					
					mdtId = cs.getInt(1);
					msg = cs.getString(2);
					jobId = cs.getInt(3);
					taxi_co_id = cs.getInt(4);
					time = cs.getTimestamp(5);
					jobRef = cs.getString(6);
					
					/*
					log.info("car Num " + mdtId);
					log.info("msg " + msg);
					log.info("jobId " + jobId);
					log.info("time " + time);
					log.info("taxi_co_id " + taxi_co_id);
					*/
					if(jobId > 0 ) {
						HttpPost post = null;
						post = sendRiderMsgRequest(jobId, TripAlertRequest.MSG_TO_RIDER, time, msg, taxi_co_id, carNum, jobRef);
						//add this header so sendRiderMsgAck() can use only
						post.setHeader("mdt", ""+mdtId);
						post.setHeader("job", ""+jobId);
						post.setHeader("event", ""+ TripAlertRequest.MSG_TO_RIDER);
						
						if(post != null) {
							riderMsgPosts.add(post);
							
						}
						
						
					}else {
						getRiderMsg = false;
					}
				
			
			} catch(SQLException se)
		    {
				getRiderMsg = false;
	      	    log.error ("runMSgToRiderTask() failed with exception", se);
	      	   
	        }
		    finally
		    {
	            if (cs != null) try {cs.close ();} catch (SQLException ignore) {};
	    	    if (con != null) try {con.close ();} catch (SQLException ignore) {};
				
	        }
	    }
		if(riderMsgPosts != null && riderMsgPosts.size() > 0) {
			sendHttpPosts(riderMsgPosts);
		}
	}

	/**
	 * Send the trip update alert request and don't need response.
	 * 
	 * @param job_id
	 *            Pathfinder Job ID
	 * @param eventID
	 *            event ID of the event that triggered the request
	 * @param time
	 *            the time the event took place
	 *
	 * @return SEND_STATUS
	 */

	//private SEND_STATUS sendTripUpdateRequest(int job_id, int eventID,
	private HttpPost sendTripUpdateRequest(int job_id, int eventID, int taxi_co_Id,
			Date time, String jobRef) {
		HttpPost post = null;

		try {
			//PF-16074
			if(TripAlertRequest.MOBILEBOOKING_ORIGN.equalsIgnoreCase(jobRef)){
				post = new HttpPost(mobileBooking_URL);// eg.
												// http://192.168.50.78:92/statusAlert.svc/xml/notify
			//post = new HttpPost("http://localhost:8080/");
			}
			else if (TripAlertRequest.TAXILIMO_MOBILE_ORIGN.equalsIgnoreCase(jobRef)){
				post = new HttpPost(taxiLimo_URL);
			}
			TripAlertRequest tripUpdateReq = new TripAlertRequest();
			tripUpdateReq.setTaxi_ride_id(job_id);
			tripUpdateReq.setEvent(eventID);
			tripUpdateReq.setTime(time);


			JobInfo jobInfo = new JobInfo(job_id, eventID, jobRef, pfDataSource, cachedParam);// Get job info
					 
			jobInfo.updateJobInfo(tripUpdateReq); // update request with job
													// info

			String content = tripUpdateReq.create();
			if (content == null) // failed to create the request
			{
				return null;
				
			}

		
			StringEntity entity = new StringEntity(content, ContentType.create("text/xml", "UTF-8"));
			entity.setContentEncoding(CharEncoding.UTF_8);
			
			post.setEntity(entity);

			log.debug("Request Params: " + content);
			
			return post;
			

		} catch (Exception e) {
			log.error("TripService HTTPRequest Exception", e);
			return null;
	
		} 
	}
	

	
	private HttpPost sendRiderMsgRequest(int job_id, int eventID,
			Date time, String msg, int taxi_co_id,  String carNum, String jobRef) {
		HttpPost post = null;
		try {
			//post = new HttpPost(mobileBooking_URL);// eg.
													// http://192.168.50.78:92/statusAlert.svc/xml/notify
			//PF-16074
			if(TripAlertRequest.MOBILEBOOKING_ORIGN.equalsIgnoreCase(jobRef)){
				post = new HttpPost(mobileBooking_URL);// eg.
												// http://192.168.50.78:92/statusAlert.svc/xml/notify
			//post = new HttpPost("http://localhost:8080/");
			}
			else if (TripAlertRequest.TAXILIMO_MOBILE_ORIGN.equalsIgnoreCase(jobRef)){
				post = new HttpPost(taxiLimo_URL);
			}
			
			String apiKey = cachedParam.getCompanyParameterValue(taxi_co_id, CompanyDefaultValues.COMP_PARAMETER_C_MB_API_KEY);
			
			TripAlertRequest tripUpdateReq = new TripAlertRequest();
			tripUpdateReq.setTaxi_ride_id(job_id);
			tripUpdateReq.setEvent(eventID);
			tripUpdateReq.setTime(time);
			tripUpdateReq.setMsgToRider(msg);
			tripUpdateReq.setVehicle_number(carNum);
			tripUpdateReq.setTaxi_api_key(apiKey);

			String content = tripUpdateReq.create();
			
			if (content == null) // failed to create the request
			{
				return null;
			}

			
			StringEntity entity = new StringEntity(content, ContentType.create("text/xml", "UTF-8"));
			entity.setContentEncoding(CharEncoding.UTF_8);
			post.setEntity(entity);
			
			

			log.debug("Request Params: " + content);
			
			return post;
			

		} catch (Exception e) {
			log.error("sendRiderMsgRequest HTTPRequest Exception", e);
			return null; 
		} 
	}
	
	void sendRiderMsgAck(int vehicleNumber, int jobId ) {
		// Send request to DB for processing
	    Connection con = null;
	    CallableStatement cs = null;
	  
	    //Ack msg to rider is done.
	    String messageBody = "Job " + jobId + " MSG to MB rider sent";
	    String priority = MSG_PRIORITY_URGENT;
	    
	    try
	    {
	        con = pfDataSource.getConnection ();
	        cs = con.prepareCall ("{?=call mail.mb_send_driver_message (?,?,?,?)}");
	        /*
	        log.info ("message body: " + messageBody);
	        log.info ("message priority: " + priority);
	        //log.info ("delivery dtm: " + request.getDeliveryTime());
	        log.info ("mdt id: " + vehicleNumber);
	        */
	   
	        cs.registerOutParameter (1, java.sql.Types.INTEGER);
		    cs.setString (2, messageBody);
		    cs.setString (3, priority);
		    cs.setString (4, "");
		    cs.setInt(5, vehicleNumber);
		    
		    
		    cs.execute ();
		    log.debug ("sendRiderMsgAck Result: " + cs.getInt (1));
		    
 	    }
	    catch(SQLException se)
	    {
      	    log.error ("sendRiderMsgAck failed with exception", se);
      	    
        }
	    finally
	    {
            if (cs != null) try {cs.close ();} catch (SQLException ignore) {};
    	    if (con != null) try {con.close ();} catch (SQLException ignore) {};
        }
    	
    	
	}
	
	
	static final int INVALID_GPS_X=32767;
	static final int INVALID_GPS_Y=32767;
	//PF-16093
	public void runFareToRiderTask() {
			String carNum = null;
			int mdtId = -1;
			String totalAmount = null, meterFare=null, otherExpenses=null;
			int jobId = -1;
			int taxi_co_id = -1;
			Date time = null;
			Connection con = null;
		    CallableStatement cs = null;
		    boolean getFare = true;
		    String jobRef = null;
		    int gps_x=INVALID_GPS_X;
		    int gps_y=INVALID_GPS_Y;
		    
			ArrayList<HttpPost> riderFarePosts = new ArrayList<HttpPost>();
		
			
			while(getFare) {
				try {
					
						//check pf_messages table to get all the messages for 'MBUSR'
					
						con = pfDataSource.getConnection ();
						cs = con.prepareCall ("{call aqueues.get_mb_drv_init_pay_msg (?,?,?,?,?,?,?,?,?,?,?)}");
						//cs = con.prepareCall ("{call aqueues.get_mb_fare_msg (?,?,?,?,?,?)}");
						
						cs.registerOutParameter (1, java.sql.Types.INTEGER); //callsign
						cs.registerOutParameter (2, java.sql.Types.INTEGER); //mdt_id
						cs.registerOutParameter (3, java.sql.Types.VARCHAR); //total amount, not including tips
						cs.registerOutParameter (4, java.sql.Types.INTEGER); //taxi_ride_id
						cs.registerOutParameter (5, java.sql.Types.INTEGER); //taxi_co_id
						cs.registerOutParameter (6, java.sql.Types.TIMESTAMP); //event dtm
						cs.registerOutParameter(7, java.sql.Types.VARCHAR); //job_ref
						cs.registerOutParameter(8, java.sql.Types.INTEGER); //GPS X
						cs.registerOutParameter(9, java.sql.Types.INTEGER); //GPS Y
						cs.registerOutParameter (10, java.sql.Types.VARCHAR); //meter fare
						cs.registerOutParameter (11, java.sql.Types.VARCHAR); //other expenses
						
						cs.execute ();
						
						mdtId = cs.getInt(2);
						totalAmount = cs.getString(3);
						jobId = cs.getInt(4);
						taxi_co_id = cs.getInt(5);
						time = cs.getTimestamp(6);
						jobRef = cs.getString(7);
						gps_x = cs.getInt(8);
						gps_y = cs.getInt(9);
						meterFare = cs.getString(10);
						otherExpenses = cs.getString(11);
						
						//eventType = cs.getInt(7);
						
						/*
						log.info("car Num " + mdtId);
						log.info("msg " + msg);
						log.info("jobId " + jobId);
						log.info("time " + time);
						log.info("taxi_co_id " + taxi_co_id);
						*/
						if(jobId > 0 ) {
							HttpPost post = null;
							double[] latitude = new double[1];
							double[] longitude = new double[1];
							if( gps_x!= INVALID_GPS_X && gps_y != INVALID_GPS_Y)
							{
								log.info("gps_X " + gps_x);
								log.info("gps_Y " + gps_y);
								JobInfo.getLatLong(con, gps_x, gps_y, latitude, longitude);
							}
							post = sendFareRequest(jobId, TripAlertRequest.FARE_EVENT, time, totalAmount, taxi_co_id, carNum, jobRef, latitude[0], longitude[0], meterFare, otherExpenses);
							log.info("car Num " + carNum);
							log.info("jobId " + jobId);
							log.info("time " + time);
							log.info("taxi_co_id " + taxi_co_id);
							log.info("total amount " + totalAmount);
							log.info("gps lat " + latitude[0]);
							log.info("gps_lon " + longitude[0]);
							log.info("meter fare " + meterFare);
							log.info("other expenses " + otherExpenses);
							//add this header so sendRiderMsgAck() can use only
							//post.setHeader("mdt", ""+mdtId);
							//post.setHeader("job", ""+jobId);
							//post.setHeader("event", ""+ TripAlertRequest.MSG_TO_RIDER);
							
							if(post != null) {
								riderFarePosts.add(post);
								
							}
							
							
						}else {
							getFare = false;
						}
					
				
				} catch(SQLException se)
			    {
					getFare = false;
		      	    log.error ("runFareToRiderTask() failed with exception", se);
		      	   
		        }
			    finally
			    {
		            if (cs != null) try {cs.close ();} catch (SQLException ignore) {};
		    	    if (con != null) try {con.close ();} catch (SQLException ignore) {};
					
		        }
		    }
			if(riderFarePosts != null && riderFarePosts.size() > 0) {
				sendHttpPosts(riderFarePosts);
			}
		}
		//PF-16093
		private HttpPost sendFareRequest(int job_id, int eventID,
				Date time, String totalAmount, int taxi_co_id,  String carNum, String jobRef, double latitude, double longitude,
				String meterFare, String otherExpenses) {
			HttpPost post = null;
			try {
			
				//PF-16074
				if(TripAlertRequest.MOBILEBOOKING_ORIGN.equalsIgnoreCase(jobRef)){
					post = new HttpPost(mobileBooking_URL);// eg.
													// http://192.168.50.78:92/statusAlert.svc/xml/notify
				//post = new HttpPost("http://localhost:8080/");
				}
				else if (TripAlertRequest.TAXILIMO_MOBILE_ORIGN.equalsIgnoreCase(jobRef)){
					post = new HttpPost(taxiLimo_URL);
				}
				
				String apiKey = cachedParam.getCompanyParameterValue(taxi_co_id, CompanyDefaultValues.COMP_PARAMETER_C_MB_API_KEY);
				
				TripAlertRequest tripUpdateReq = new TripAlertRequest();
				tripUpdateReq.setTaxi_ride_id(job_id);
				tripUpdateReq.setEvent(eventID);
				tripUpdateReq.setTime(time);
				tripUpdateReq.setTotalAmount(totalAmount);
				tripUpdateReq.setVehicle_number(carNum);
				tripUpdateReq.setTaxi_api_key(apiKey);
				tripUpdateReq.setLatitude(latitude);
				tripUpdateReq.setLongitude(longitude);
				tripUpdateReq.setMeterFare(meterFare);
				tripUpdateReq.setOtherExpenses(otherExpenses);
				

				String content = tripUpdateReq.create();
				
				if (content == null) // failed to create the request
				{
					return null;
				}

				
				StringEntity entity = new StringEntity(content, ContentType.create("text/xml", "UTF-8"));
				entity.setContentEncoding(CharEncoding.UTF_8);
				post.setEntity(entity);
				
				

				log.debug("Request Params: " + content);
				
				return post;
				

			} catch (Exception e) {
				log.error("sendFareRequest HTTPRequest Exception", e);
				return null; 
			} 
		}
	
	private CloseableHttpAsyncClient initHttpClient(int nbPosts){
		 
		IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
	             .setIoThreadCount(Runtime.getRuntime().availableProcessors())
	             .setConnectTimeout(httpConnectionTimeout)
	             .setSoTimeout(httpConnectionTimeout)
	             .build();
		
		RequestConfig requestConfig = RequestConfig.custom()
				.setExpectContinueEnabled(false)
				.setStaleConnectionCheckEnabled(true)
	            .setSocketTimeout(httpConnectionTimeout)
	            .setConnectTimeout(httpConnectionTimeout).build();
		
		ConnectingIOReactor ioReactor = null;
		try {
			ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
		} catch (IOReactorException e) {
			log.error("initHttpClient failed on connecting IO reactor", e);
		}
		
		PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
		
		int connections = (nbPosts >= maxHttpConnection) ? maxHttpConnection : nbPosts;

		log.debug("create http client with conn: " + connections);
		connManager.setMaxTotal(connections);
		connManager.setDefaultMaxPerRoute(20);
		
		
		CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
				.setConnectionManager(connManager)
	            .setDefaultRequestConfig(requestConfig)
	            .build();
		
		return httpClient;
	}
	
	private void sendHttpPosts(ArrayList<HttpPost> posts){
		log.debug("start to sending posts : " + posts.size());
		long currentTime = System.currentTimeMillis();
		CloseableHttpAsyncClient httpClient = initHttpClient(posts.size());
		log.debug("sending posts init http client used ......." + (System.currentTimeMillis() - currentTime) + " mis");
		if(httpClient == null){
			log.error("sendHttpPosts failed with http client is null..............");
			return;
		}
		try {
			currentTime = System.currentTimeMillis();
			httpClient.start(); 
			final CountDownLatch latch = new CountDownLatch(posts.size());
			for(final HttpPost post : posts){
				httpClient.execute(post, new FutureCallback<HttpResponse>() {

	                    public void completed(final HttpResponse response) {
	                        latch.countDown();
	                        
	                        log.debug("get response completed -> " + post.getRequestLine() + "->" + response.getStatusLine().getStatusCode());
	   
	                        //MsgToRider event need to send ack back to MDT
	                        if(post.getLastHeader("event") != null) {
		                        String eventStr = post.getLastHeader("event").getValue();
		                        if(eventStr != null && eventStr.length() > 0){
			                        try {
				                        
				                        if(Integer.parseInt(eventStr) == TripAlertRequest.MSG_TO_RIDER){
											
					                        int returnCode = response.getStatusLine().getStatusCode();
					                        
					                        if (returnCode == HttpStatus.SC_OK
					            					|| returnCode == HttpStatus.SC_CREATED) {
					                        	
					                        	//send text msg to MDT to ack for successful request sent
					                        	int jobId = Integer.parseInt(post.getLastHeader("job").getValue());
					                        	int mdtId = Integer.parseInt(post.getLastHeader("mdt").getValue());
												sendRiderMsgAck(mdtId, jobId);
					                        	
					            				log.debug(" Process response for jobID:" + jobId
					            						+ ",mdt:" + mdtId);
					            			
					            				
					            			} 
				                        }
			                        }catch(NumberFormatException ex) {
			                        	log.error("FutureCallback completed failed: ", ex);
			                        }
		                        }
	                        }
	                    }

	                    public void failed(final Exception ex) {
	                        latch.countDown();
	                        log.debug("get response failed -> " + post.getRequestLine() + "->" + ex);
	                        //ex.printStackTrace(); //need to know what caused failure.
	                    }

	                    public void cancelled() {
	                        latch.countDown();
	                        log.debug("get response cancelled ->" + post.getRequestLine() + " cancelled");
	                    }

	                });
			}
			log.debug("sending posts finished posting........" + (System.currentTimeMillis() - currentTime)/ 1000.0 + " sec.");
			try{
				latch.await();
				log.debug("sending posts after response spent ..." + (System.currentTimeMillis() - currentTime)/ 1000.0 + " sec.");
			}catch(InterruptedException ie){
				log.error("sendHttpPosts latch await failed: ", ie);
				return;
			}
			
		}finally{
			try {
				httpClient.close();
			} catch (IOException e) {
				log.error("http Client close failed.....", e);
			}
		}
	}
}
