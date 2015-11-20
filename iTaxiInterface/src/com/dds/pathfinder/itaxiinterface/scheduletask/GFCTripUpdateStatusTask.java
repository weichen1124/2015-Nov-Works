/****************************************************************************
 * 
 *					Copyright (c), $Date: 1/29/14 3:58p $
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 * **************************************************************************
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/scheduletask/GFCTripUpdateStatusTask.java $
 * 
 * 5     1/29/14 3:58p Ezhang
 * PF-15809 Pass pf parameters load bean to individual task
 * 
 * 4     1/13/14 2:39p Ezhang
 * added delete for arrived event after httppost
 * 
 * 3     12/09/13 4:23p Ezhang
 * fixed httpEntity content Type after upgrade of http client.
 * 
 * 2     11/28/13 6:27p Dchen
 * upgrade http client to 4.3
 * 
 * 1     3/08/13 3:31p Dchen
 * Separate rider charge service from callbooker package.
 * 
 * 1     11/02/12 9:53a Ezhang
 * 
 * PF-14845  OSP status update to support GoFastCab Trip status update
 * 
 */
package com.dds.pathfinder.itaxiinterface.scheduletask;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.scheduletask.TripStatusService.SEND_STATUS;
import com.dds.pathfinder.itaxiinterface.util.Debug2;

/**
 * This class will send GoFastCab server the job status update.
 * delete the event in timers table when it's done
 * @author ezhang
 *
 */
public class GFCTripUpdateStatusTask implements UpdateStatusTaskInterface{
	
	
	private static Debug2 log = Debug2.getLogger(GFCTripUpdateStatusTask.class);
	boolean scanGoFastCabGpsUpdate = false;
	String goFastCab_URL = null;
	public final static int HTTP_CONNECTION_TIMEOUT = 90000; // 90 seconds
	CloseableHttpClient httpclient = null;
	private  DataSource pfDataSource;
	private LoadDispatchParametersLocal cachedParam;
	
	public GFCTripUpdateStatusTask(boolean gpsScan, String toURL,  DataSource dataSource, LoadDispatchParametersLocal cachedParam){
		scanGoFastCabGpsUpdate = gpsScan;
		goFastCab_URL = toURL;
		pfDataSource = dataSource;
		this.cachedParam = cachedParam;
	}
	

	public void runUpdateStatusTask() {
		StringBuffer query = new StringBuffer();
		// Scan timers table for timers inserted for GoFastCab trip
		// (use priority_upd_dtm as the event time, it is more persistent.
		// due in timers table gets set to null when job is canceled/scrub)
		query.append("select timers.job_id, timers.subtype event, priority_upd_dtm time, jobs.taxi_co_id from timers, jobs, job_references ");
		query.append("where timer_type = 'F' and timers.job_id = jobs.job_id and job_references.job_id = jobs.job_id and job_references.reference_type = 'GF' ");
		query.append("order by priority_upd_dtm"); // process the oldest
													// timer first
		PreparedStatement stmt = null, del_stmt = null;
		ResultSet rset = null;
		Connection dbConnection = null;
		try {
			if ((dbConnection = pfDataSource.getConnection()) == null) {
				log.error("Failed to get db connection.....");
				return;
			}

			stmt = dbConnection.prepareStatement(query.toString());
			rset = stmt.executeQuery();

			int job_id = -1;
			int event = -1;
			Date time = null;
			
			SEND_STATUS send_status = SEND_STATUS.SEND_REQ_INVALID;
			
			RequestConfig requestConfig = RequestConfig.custom()
		            .setSocketTimeout(HTTP_CONNECTION_TIMEOUT)
		            .setConnectTimeout(HTTP_CONNECTION_TIMEOUT).build();
				
			httpclient = HttpClients.custom()
			            .setDefaultRequestConfig(requestConfig)
			            .build();
			
			// Process the timers that we got from the query above
			while (rset.next()) {
				job_id = rset.getInt("job_id");
				event = rset.getInt("event");
				time = rset.getTimestamp("time");
				
				//send update if it's not GPS update or it's GPS update and GPS update is turned on
				if(event != TripAlertRequest.GPS_UPDATE_EVENT || 
						(event == TripAlertRequest.GPS_UPDATE_EVENT && scanGoFastCabGpsUpdate == true)){
					send_status = sendTripUpdateRequest(job_id, event, time); // Send request
															// according to
															// timer info
				}

				try {
					// Delete the processed timer, GoFastCab timer type is
					// 'F' (see GOFASTCAB_TIMER_TYPE in rtl_type.hdr)
					// (there should only be one event/subtype per job)
					//PF-14473
					// if the event type is accept or meter on then just delete the event itself
					if (event == TripAlertRequest.ACCEPT_EVENT || 
							event == TripAlertRequest.ARRIVED_EVENT ||  //ezhang arrived event added for delete
							event == TripAlertRequest.METER_ON_EVENT) {
						del_stmt = dbConnection
								.prepareStatement("DELETE FROM timers WHERE timers.job_id="
										+ job_id
										+ " and timer_type='F' and subtype="
										+ event);
						int numOfRowsDel = del_stmt.executeUpdate();
						if (numOfRowsDel == 0) {
							log.error("Failed to delete processed GFC Timer "
									+ "(job_id:"
									+ job_id
									+ ",event:"
									+ event + ". Message will get resent.");
						} else {
							log.debug(numOfRowsDel
									+ " GFC Timer deleted for job_id "
									+ job_id + " event " + event);
						}
					}
					//if the event type is meter off, no show, cancel or force complete appeared 
					// then delete the event type and gps update timer 
					else if(event == TripAlertRequest.CANCELLED_EVENT  ||
							event == TripAlertRequest.NO_SHOW_EVENT ||
							event == TripAlertRequest.FORCED_COMPLETE_EVENT ||
							event == TripAlertRequest.METER_OFF_EVENT) 
					{
						del_stmt = dbConnection
								.prepareStatement("DELETE FROM timers WHERE timers.job_id="
										+ job_id
										+ " and timer_type='F' and ( subtype="
										+ event
										+ "or subtype="
										+ TripAlertRequest.GPS_UPDATE_EVENT 
										+ ")");
						int numOfRowsDel = del_stmt.executeUpdate();
						if (numOfRowsDel == 0) {
							// this should not be error since it may just
							// don't have event anymore
							log.debug("Failed to delete processed GFC Timer "
									+ "(job_id:"
									+ job_id
									+ ",event:"
									+ event);
						} else {
							log.debug(numOfRowsDel
									+ " GFC Timer deleted for job_id "
									+ job_id + " event " + event);
						}
					}
				} catch (SQLException e) {
					log.error(
							"SQLException in deleting GFC Timer deleted for job_id "
									+ job_id + " event " + event, e);
				} finally {
					try {
						if (del_stmt != null)
							del_stmt.close();
					} catch (SQLException e) {
						log.error("Failed to close del_stmt", e);
					}
				}

				if (send_status == SEND_STATUS.SEND_EXCEPTION
						|| send_status == SEND_STATUS.SEND_HTTP_FAILED) {
					return; // don't process the rest of the timers till
							// connection clears up
				}
				log.debug("Send trip update request, SEND_STATUS "
						+ send_status.name());

			}
		} catch (SQLException e) {
			log.error("Exception in TripUpdateStatusTask run()", e);
		} finally {
			try {if (rset != null)rset.close();} catch (SQLException ignore) {};
			try {if (stmt != null)stmt.close();} catch (SQLException ignore) {};
			try {if (dbConnection != null)dbConnection.close();} catch (SQLException ignore) {};
			try{if(httpclient !=null) httpclient.close();}catch(IOException ignore){};
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

	private SEND_STATUS sendTripUpdateRequest(int job_id, int eventID,
			Date time ) {
		HttpPost post = null;

		try {
			post = new HttpPost(goFastCab_URL);// eg.
													// http://gfc.axmor.com/notifications/DispatchNotificationService.svc/ddsosp/notify
			

			TripAlertRequest tripUpdateReq = new TripAlertRequest();
			tripUpdateReq.setTaxi_ride_id(job_id);
			tripUpdateReq.setEvent(eventID);
			tripUpdateReq.setTime(time);


			JobInfo jobInfo = new JobInfo(job_id, eventID, TripAlertRequest.GOFASTCAB_ORIGN, pfDataSource, cachedParam);// Get job info
					 
			jobInfo.updateJobInfo(tripUpdateReq); // update request with job
													// info

			String content = tripUpdateReq.create();
			if (content == null) // failed to create the request
			{
				return SEND_STATUS.SEND_REQ_INVALID;
			}

			//StringEntity entity = new StringEntity(content, ContentType.TEXT_XML);
			//entity.setContentEncoding(CharEncoding.UTF_8);
			StringEntity entity = new StringEntity(content, ContentType.create("text/xml", "UTF-8"));
			entity.setContentEncoding(CharEncoding.UTF_8);
			post.setEntity(entity);
			
			log.debug("sendTripUpdateRequest Request Params: " + content);

			int returnCode = -1;
			CloseableHttpResponse response = null;
			try {
				response = httpclient.execute(post);
				if(response != null) returnCode = response.getStatusLine().getStatusCode();
			} catch (Exception e) {
				log.error(" HTTP send failed:", e);
				return SEND_STATUS.SEND_EXCEPTION;
			}finally{
				if(response != null) try{response.close();}catch(IOException ignore){};
			}
			// GoFastCab sends back 201 instead of 200.
			if (returnCode == HttpStatus.SC_OK
					|| returnCode == HttpStatus.SC_CREATED) {

				log.debug(". Process response for jobID:" + job_id
						+ ",event:" + eventID);
				return SEND_STATUS.SEND_OK; // GoFastCab got it we don't
											// process the acutal response.
				
			} else {
				log.error("TripService HTTPRequest failed. HTTPStatus not ok, status:"
						+ returnCode);
				return SEND_STATUS.SEND_HTTP_FAILED;

			}

		} catch (Exception e) {
			log.error("TripService HTTPRequest Exception", e);
			return SEND_STATUS.SEND_EXCEPTION;
		} 
	}
	
}
