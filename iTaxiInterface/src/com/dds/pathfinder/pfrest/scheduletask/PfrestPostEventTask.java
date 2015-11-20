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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/scheduletask/PfrestPostEventTask.java $
* 
* TSAB-729, 10/08/15, DChen, jdbc driver nulls in string array.
* 
* PF-16809, 09/28/15, DChen, created for passenger events.
* 
* PF-16785, 09/18/15, DChen, change payment to float.
* 
* PF-16785, 09/18/15, DChen, add ftj data event for samplan.
* 
* PF-16680, 07/30/15, DChen, switch payment type amount.
* 
* PF-16653, 07/23/15, DChen, remove properties from pf events.
* 
* PF-16653, 07/17/15, DChen, add event type for TG.
* 
* PF-16597, 06/09/15, DChen, add system id in pfrest.
* 
* PF-16572, 05/19/15, DChen, review notification message json format.
* 
* PF-16523, 04/29/15, DChen, should be 16523 not 16522.
* 
* PF-16522, 04/29/15, DChen, re organize keep alive service.
* 
* PF-16428, 03/25/15, DChen, pfrest job events service.
* 
* PF-16428, 03/06/15, DChen, pfrest notification service.
* 
* 
* **************************************/
package com.dds.pathfinder.pfrest.scheduletask;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import javax.sql.DataSource;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.cart.model.PricingItem;
import com.dds.pathfinder.callbooker.server.jobsearch.ejb.JobSearchLocal;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;
import com.dds.pathfinder.callbooker.server.payment.model.PaymentItem;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.pfrest.events.AcceptEvent;
import com.dds.pathfinder.pfrest.events.FtjDataEvent;
import com.dds.pathfinder.pfrest.events.MeterOffEvent;
import com.dds.pathfinder.pfrest.events.PFJobEvent;
import com.dds.pathfinder.pfrest.events.PFRestEvent;
import com.dds.pathfinder.pfrest.events.PFRestEvent.PFRestEventType;
import com.dds.pathfinder.pfrest.events.PassengerEvent;
import com.dds.pathfinder.pfrest.events.PostEvent;
import com.dds.pathfinder.pfrest.resources.PaymentListItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class PfrestPostEventTask {
	
	private Logger logger = Logger.getLogger(PfrestPostEventTask.class);
	
	String transitGWURL = null;
	
	private final static int maxHttpConnection = 100;
	public final static int HTTP_CONNECTION_TIMEOUT = 10000; // 90 seconds
	CloseableHttpClient httpclient = null;
	
	private  DataSource pfDataSource;
	
	private ObjectMapper jsonMap = null;
	
	private String[] recipients;
	
	private LoadDispatchParametersLocal cachedParam;
	
	private JobSearchLocal jobSearchLocal;
	
	public PfrestPostEventTask(String transitGWURL) {
		super();
		this.transitGWURL = transitGWURL;
	}
	
	public PfrestPostEventTask(String transitGWURL, DataSource pfDataSource, String[] recipients, LoadDispatchParametersLocal cachedParam, JobSearchLocal jobSearchLocal) {
		super();
		this.transitGWURL = transitGWURL;
		this.pfDataSource = pfDataSource;
		this.recipients = recipients;
		this.cachedParam = cachedParam;
		this.jobSearchLocal = jobSearchLocal;
	}
	
	public void postTGAliveEvent(PostEvent aliveEvent){
		RequestConfig requestConfig = RequestConfig.custom()
	            .setSocketTimeout(HTTP_CONNECTION_TIMEOUT)
	            .setConnectTimeout(HTTP_CONNECTION_TIMEOUT).build();
			
		httpclient = HttpClients.custom()
		            .setDefaultRequestConfig(requestConfig)
		            .build();

		HttpPost post = new HttpPost(transitGWURL);
		CloseableHttpResponse response = null;
		
		try{
			try{
				jsonMap = getJsonMap();
				if(jsonMap != null){
					StringEntity entity = new StringEntity(jsonMap.writeValueAsString(aliveEvent), ContentType.create("application/json", "UTF-8"));
					entity.setContentEncoding(CharEncoding.UTF_8);
					post.setEntity(entity);
					response = httpclient.execute(post);
					if(response != null){
						int statusCode = response.getStatusLine().getStatusCode();
						logger.info("post alive event response: " + statusCode);
					}
				}else{
					logger.error("postTGAliveEvent failed as json object map null");
				}
			}catch(Exception e){
				logger.error("http post failed", e);
			}
			
		}finally{
			if(response != null) try{response.close();}catch(IOException ignore){};
			if(post != null) post.releaseConnection();
			if(httpclient != null) try{httpclient.close();}catch(IOException ignore){};
		}

		
	}
	
	public void runSendTGEventsTask() {
		if(recipients != null && recipients.length > 0){
			for(String recipient : recipients){
				runSendTGEventsTask(recipient);
			}
		}
	}
	
	public void runSendTGEventsTask(String recipient) {
		logger.debug("start run runSendTGEventsTask: pf datasource is null: " + (pfDataSource == null));
		if(pfDataSource == null) return;
		
		ArrayList<HttpPost> pfJobEvents = new ArrayList<HttpPost>();
		
		
		String query = "select job_id, external_job_id, event, event_time, taxi_co_id, driver_id, badge_nr, callsign, geo_lat, geo_long, vehicle_status, gpstime, nb_seats, driver_att, vehicle_att, stop_enum, client_name, meter_on_dist, meter_on_time, cash_pay, account_pay "
				+ " from TABLE(aqueues.get_pfrest_job_event(?))";
		
		PreparedStatement stmt = null;
		ResultSet rset = null;
		Connection dbConnection = null;
		try 
		{
			if ((dbConnection = pfDataSource.getConnection()) == null){
				logger.error("Failed to get db connection.....");
				return;
			}
			// logger.debug(query.toString());
			
//			Connection physicalCon = ((WrappedConnection)dbConnection).getUnderlyingConnection();		
//			ArrayDescriptor recpArrayType = ArrayDescriptor.createDescriptor("PF.T_PFREST_RECIPIENT_TAB", physicalCon); 
//			ARRAY recpArr = new ARRAY(recpArrayType, physicalCon, recipients);
//			
//			logger.debug("with reciptients: " + Arrays.toString(recipients));
			
			stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, recipient);		
			
			rset = stmt.executeQuery();
			// SEND_STATUS send_status = SEND_STATUS.RESP_RC_OK;
			
			
			while (rset.next()) {
				
				long jobID = rset.getLong("job_id");
				String externalJobID = rset.getString("external_job_id");
				String event = rset.getString("event");
				String eventTime = rset.getString("event_time");
				int companyID = rset.getInt("taxi_co_id");
				int driverID = rset.getInt("driver_id");
			    String badgeNr = rset.getString("badge_nr");
			    String callsign = rset.getString("callsign");
			     
			    double geoLatitude = rset.getDouble("geo_lat");
			    double geoLongitude = rset.getDouble("geo_long");
			    String vehicleStatus = rset.getString("vehicle_status");
			    String lastGPSDtm = rset.getString("gpstime");
			    int nbSeats = rset.getInt("nb_seats");
			    String drvAttributes = rset.getString("driver_att");
			    String vehAttributes = rset.getString("vehicle_att");
			    int stopEnum = rset.getInt("stop_enum");
			    String passengerName = rset.getString("client_name");
			    
			    String meterOnDist = rset.getString("meter_on_dist");
			    String meterOnTime = rset.getString("meter_on_time");
			    float cashPayment = rset.getFloat("cash_pay");
			    float accountPayment = rset.getFloat("account_pay");   

			    
			    logger.info("runSendTGEventsTask: get pf job event with jobid: " + jobID +", callsign = "+  callsign + ", lastGPSDTM=" + lastGPSDtm + ", external job ID=" + externalJobID +", event=" + event);
			    
			    PFJobEvent jobEvent = new PFJobEvent(jobID, externalJobID, companyID, badgeNr, callsign, geoLatitude,
			    						geoLongitude, lastGPSDtm, vehicleStatus, nbSeats, drvAttributes, vehAttributes, passengerName, eventTime, stopEnum);
			    
			    //add ftj data
			    jobEvent.setMeterOnDist(meterOnDist);	
			    jobEvent.setMeterOnTime(meterOnTime);
			    jobEvent.setCashPayment(cashPayment);
			    jobEvent.setAccountPayment(accountPayment);
			    
			    
			    // jobEvent.setDataType(getJobEventType(event));
			    // jobEvent.setDataID("" + PFRestEventType.PFEvent_Type_Accept.getEventID());
			    // jobEvent.setEventDTM(Utilities.convertOSPDefaultDate(eventTime));
			    HttpPost post = getJobEventHttpPost(createRestEvent(getJobEventType(event), jobEvent));
			    if(post != null) pfJobEvents.add(post);
			}

		}catch(SQLException se){
			logger.error("Exception in runSendTGEventsTask " ,se);
		}finally{
			try {if (rset != null) rset.close();} catch (SQLException ignore) {};
			try {if (stmt != null) stmt.close();} catch (SQLException ignore) {};
			try {if (dbConnection != null) dbConnection.close();} catch (SQLException ignore) {};
		}
		if(pfJobEvents != null && pfJobEvents.size() > 0) sendHttpJobEvents(pfJobEvents);
		
	}
	
	private PFRestEvent createRestEvent(String eventType, PFJobEvent jobEvent){
		PFRestEvent pfRestEvent = new PFRestEvent();
		if(PFRestEventType.PFEvent_Type_Accept.getEventType().equals(eventType)){
			return getAcceptEvent(jobEvent);
		}
//		else if(PFRestEventType.PFEvent_Type_Meteron.getEventType().equals(eventType)){
//			return new MeterOnEvent(jobEvent);
//		}
		else if(PFRestEventType.PFEvent_Type_Meteroff.getEventType().equals(eventType)){
			return getMeterOffEvent(jobEvent);
		}else if(PFRestEventType.PFEvent_Type_Ftj_Data.getEventType().equals(eventType)){
			return getFtjDataEvent(jobEvent);
		}else if(isPassengerEvent(eventType)){
			return new PassengerEvent(jobEvent, eventType);
		}else{
			return pfRestEvent;
		}
	}
	
	
	private boolean isPassengerEvent(String eventType){
		return (PFRestEventType.PFEvent_Type_Passenger_Pickup.getEventType().equals(eventType) ||
				PFRestEventType.PFEvent_Type_Passenger_Dropoff.getEventType().equals(eventType) ||
				PFRestEventType.PFEvent_Type_Passenger_Noshow.getEventType().equals(eventType)
				 );
	}
	
	@SuppressWarnings("unchecked")
	private MeterOffEvent getMeterOffEvent(PFJobEvent jobEvent){
		MeterOffEvent mOffEvent = new MeterOffEvent(jobEvent);
		// MeterOffProperties mOffProperties = (MeterOffProperties) mOffEvent.getProperties();
		if(jobSearchLocal != null){
			PricingItem priceItem = jobSearchLocal.getJobPricingItem("", "", "" + jobEvent.getJobID());
			if(priceItem != null){
				mOffEvent.setMeterStartTime(Utilities.formatOSPDefaultDate(priceItem.meterOn));
				Vector<PaymentItem> payments = priceItem.getPayments();
				
				if(payments != null && payments.size() > 0){
					ArrayList<PaymentListItem> paymentList = new ArrayList<PaymentListItem>();
					for(PaymentItem item : payments){
						if(item != null) paymentList.add(new PaymentListItem(item.getPaymentTypeDescription(), item.getPaymentAmount()));
					}
					mOffEvent.setPaymentList(paymentList);
				}
			}
		}
		return mOffEvent;
	}
	
	private FtjDataEvent getFtjDataEvent(PFJobEvent jobEvent){
		FtjDataEvent ftjDataEvent = new FtjDataEvent(jobEvent);
		return ftjDataEvent;
	}
	
	private AcceptEvent getAcceptEvent(PFJobEvent jobEvent){
		AcceptEvent acceptEvent = new AcceptEvent(jobEvent);
		// AcceptProperties acceptProperties = (AcceptProperties) acceptEvent.getProperties();
		acceptEvent.setJobAttributes(getJobAttributesFromJobEvent(jobEvent));
		return acceptEvent;
	}
	
	private String[] getJobAttributesFromJobEvent(PFJobEvent jobEvent){
		if(cachedParam == null || jobEvent == null || jobEvent.getTaxiCompanyID() <= 0 ){
			logger.error("getJobAttributesFromJobEvent failed with cached param null......");
			return null;
		}
		
		ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>();
		
		HashMap<String, SysAttrListItem> drvAttrMap = cachedParam.getDrvAttributeMap().get(jobEvent.getTaxiCompanyID());
		HashMap<String, SysAttrListItem> vehAttrMap = cachedParam.getVehAttributeMap().get(jobEvent.getTaxiCompanyID());
		// CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
		attributes.addAll(Utilities.getAttributesItem(drvAttrMap, Utilities.ToBinaryAttributesString(jobEvent.getDrvAttributes())));
		attributes.addAll(Utilities.getAttributesItem(vehAttrMap, Utilities.ToBinaryAttributesString(jobEvent.getVehAttributes())));
		
		if(attributes.size() > 0){
			String[] attrArray = new String[attributes.size()];
			for(int i=0; i<attributes.size(); i++){
				attrArray[i] = attributes.get(i).getAttrShortName();
			}
			
			return attrArray;
		}else return null;
	}
	
	
	private String getJobEventType(String event){
		String dataType = PFRestEventType.PFEvent_Type_Not_Known.getEventType();
		if(event != null && event.trim().length() > 0){
			try{
				int eventType = Integer.parseInt(event);
				for(PFRestEventType e : PFRestEventType.values()){
					if(eventType == e.getEventID()) return e.getEventType();
				}
			}catch(NumberFormatException ne){
				logger.error("getJobEventType event not an integer: " + event);
			}
		}
		return dataType;
		
	}
	
	private void sendHttpJobEvents(ArrayList<HttpPost> posts){
		logger.info("start to sending posts : " + posts.size());
		long currentTime = System.currentTimeMillis();
		CloseableHttpAsyncClient httpClient = initHttpClient(posts.size());
		logger.info("sending posts init http client used .......=" + (System.currentTimeMillis() - currentTime) + " mis");
		if(httpClient == null){
			logger.error("sendHttpPosts failed with http client is null..............");
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
	                        // log.debug("get response completed -> " + post.getRequestLine() + "->" + response.getStatusLine());
	                    }

	                    public void failed(final Exception ex) {
	                        latch.countDown();
	                        logger.info("get response failed -> " + post.getRequestLine() + "->" + ex);
	                    }

	                    public void cancelled() {
	                        latch.countDown();
	                        logger.info("get response cancelled ->" + post.getRequestLine() + " cancelled");
	                    }

	                });
			}
			logger.info("sending posts finished posting.............=" + (System.currentTimeMillis() - currentTime) + " milsec.");
			try{
				latch.await();
				logger.info("sending posts after response spent ...=" + (System.currentTimeMillis() - currentTime) +  " milsec.");
			}catch(InterruptedException ie){
				logger.error("sendHttpPosts latch await failed: ", ie);
				return;
			}
			
		}finally{
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("http Client close failed.....", e);
			}
		}
	}
	
	private CloseableHttpAsyncClient initHttpClient(int nbPosts){
		 
		IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
	             .setIoThreadCount(Runtime.getRuntime().availableProcessors())
	             .setConnectTimeout(HTTP_CONNECTION_TIMEOUT)
	             .setSoTimeout(HTTP_CONNECTION_TIMEOUT)
	             .build();
		
		RequestConfig requestConfig = RequestConfig.custom()
				.setExpectContinueEnabled(false)
				.setStaleConnectionCheckEnabled(true)
	            .setSocketTimeout(HTTP_CONNECTION_TIMEOUT)
	            .setConnectTimeout(HTTP_CONNECTION_TIMEOUT).build();
		
		ConnectingIOReactor ioReactor = null;
		try {
			ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
		} catch (IOReactorException e) {
			logger.error("initHttpClient failed on connecting IO reactor", e);
		}
		
		PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
		
		int connections = (nbPosts >= maxHttpConnection) ? maxHttpConnection : nbPosts;
		
		logger.debug("create http client with conn: " + connections);
		connManager.setMaxTotal(connections);
		connManager.setDefaultMaxPerRoute(20);
		
		
		CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
				.setConnectionManager(connManager)
	            .setDefaultRequestConfig(requestConfig)
	            .build();
		
		return httpClient;
	}
	
	
	private HttpPost getJobEventHttpPost(PFRestEvent jobEvent){
		if(transitGWURL == null || jobEvent == null || getJsonMap() == null) return null;
		HttpPost post = new HttpPost(transitGWURL);
		StringEntity entity;
		try {
			entity = new StringEntity(jsonMap.writeValueAsString(new PostEvent(jobEvent)), ContentType.create("application/json", "UTF-8"));
			entity.setContentEncoding(CharEncoding.UTF_8);
			post.setEntity(entity);
			return post;
		} catch (UnsupportedCharsetException e) {
			logger.error("getJobEventHttpPost failed with UnsupportedCharsetException: ", e);
			return null;
		} catch (JsonProcessingException e) {
			logger.error("getJobEventHttpPost failed with JsonProcessingException: ", e);
			return null;
		}
		
		
	}
	
	public ObjectMapper getJsonMap() {
		if(jsonMap != null) return jsonMap;
		jsonMap = new ObjectMapper();  
		jsonMap.configure(SerializationFeature.INDENT_OUTPUT, true);
		SimpleDateFormat ospDateFormat = new SimpleDateFormat(Utilities.OSP_DEFAULT_DATE_FORMAT);
		jsonMap.setDateFormat(ospDateFormat);
		return jsonMap;
	}


	public void setJsonMap(ObjectMapper jsonMap) {
		this.jsonMap = jsonMap;
	}

	public String[] getRecipients() {
		return recipients;
	}

	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}
	

}
