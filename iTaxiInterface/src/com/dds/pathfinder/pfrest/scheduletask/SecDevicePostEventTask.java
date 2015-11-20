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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/scheduletask/SecDevicePostEventTask.java $
* 
* TSAB-729, 10/08/15, DChen, jdbc driver nulls in string array.
* 
* PF-16774, 09/30/15, DChen, modify to mask pickup address.
* 
* PF-16606, 09/17/15, DChen, add company parameter to mask pickup.
* 
* PF-16606, 09/15/15, DChen, use job offer pickup address.
* 
* PF-16763, 09/14/15, DChen, add 2nd device MDT signoff event.
* 
* PF-16760, 09/10/15, DChen, reject for street hire.
* 
* PF-16606, 09/08/15, DChen, translate job type.
* 
* PF-16606, 09/03/15, DChen, add some timeout in job offer.
* 
* PF-16607, 09/01/15, DChen, 2nd device integration test bug fix.
* 
* PF-16607, 08/25/15, DChen, 2nd device bug fix.
* 
* PF-16607, 08/21/15, DChen, add 2nd device in vehicle event.
* 
* PF-16607, 08/19/15, DChen, accept reject from 2nd device.
* 
* PF-16606, 08/17/15, DChen, job offer event.
* 
* PF-16605, 08/07/15, DChen, temp off event notification
* 
* PF-16653, 07/23/15, DChen, remove properties from pf events.
* 
* PF-16653, 07/17/15, DChen, add event type for TG.
* 
* PF-16605, 06/18/15, DChen, created for 2nd device.
* 
* **************************************/
package com.dds.pathfinder.pfrest.scheduletask;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import javax.sql.DataSource;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartModel;
import com.dds.pathfinder.callbooker.server.cart.model.StopPoint;
import com.dds.pathfinder.callbooker.server.controller.event.CartEvent;
import com.dds.pathfinder.callbooker.server.jobsearch.ejb.JobSearch;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.pfrest.events.PFRestEvent;
import com.dds.pathfinder.pfrest.events.PFRestEvent.PFRestEventType;
import com.dds.pathfinder.pfrest.events.PostEvent;
import com.dds.pathfinder.pfrest.events.SecDevAddrFormatEvent;
import com.dds.pathfinder.pfrest.events.SecDevJobOfferEvent;
import com.dds.pathfinder.pfrest.events.SecDeviceEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
//import oracle.sql.ARRAY;
//import oracle.sql.ArrayDescriptor;
import org.apache.commons.codec.CharEncoding;

public class SecDevicePostEventTask {
	
	private Logger logger = Logger.getLogger(SecDevicePostEventTask.class);
	
	String secDeviceJobURL = null;
	
	String secDeviceVehicleURL = null;
	
	private final static int maxHttpConnection = 100;
	public final static int HTTP_CONNECTION_TIMEOUT = 10000; // 90 seconds
	CloseableHttpClient httpclient = null;
	
	private  DataSource pfDataSource;
	
	private JobSearch jobSearch;
	
	private ObjectMapper jsonMap = null;
	
	private String[] recipients;
	
	private LoadDispatchParametersLocal cachedParam;
	
	private SecDevJobOfferEvent jobLabels;
	
	public final static String C_SMS_ACCEPT_TIME = "C_SMS_ACCEPT_TIME";
	
	public final static String C_DVDEV_ACCEPT_TIME = "C_DVDEV_ACCEPT_TIME";
	
	public final static String C_DVDEV_STRHIRE_BTN = "C_DVDEV_STRHIRE_BTN";
	
	public final static String C_DVDEV_MASK_PICKUP = "C_DVDEV_MSK_PKP_ADR";
	
	
	public SecDevicePostEventTask(String secDeviceGWURL) {
		super();
		this.secDeviceJobURL = secDeviceGWURL;
	}
	
	public SecDevicePostEventTask(String secDeviceJobURL, String secDeviceVehicleURL, DataSource pfDataSource, String[] recipients, LoadDispatchParametersLocal cachedParam, JobSearch jobSearch, SecDevJobOfferEvent jobLabel) {
		super();
		this.secDeviceJobURL = secDeviceJobURL;
		this.secDeviceVehicleURL = secDeviceVehicleURL;
		this.pfDataSource = pfDataSource;
		this.recipients = recipients;
		this.jobSearch = jobSearch;
		this.cachedParam = cachedParam;
		this.jobLabels = jobLabel;
	}
	
	
	public void runSendSDEventsTask() {
		if(recipients != null && recipients.length > 0){
			for(String recipient : recipients){
				runSendSDEventsTask(recipient);
			}
		}
	}
	
	public void runSendSDEventsTask(String recipient) {
		if(pfDataSource == null) return;
		
		ArrayList<HttpPost> devEvents = new ArrayList<HttpPost>();
		
		String query = "select job_id, event, event_time, taxi_co_id, driver_id, snd_device_id, badge_nr, callsign, geo_lat, geo_long, vehicle_status, gpstime, req_pickup_dtm, offer_address_mask, area_id_for_ofrmask "
				+ " from TABLE(aqueues.get_2nd_device_event(?))";
		
		logger.info("get  2nd dev query: " + query);
		
		PreparedStatement stmt = null;
		ResultSet rset = null;
		Connection dbConnection = null;
		try 
		{
			if ((dbConnection = pfDataSource.getConnection()) == null){
				logger.error("Failed to get db connection.....");
				return;
			}
			//log.info(query.toString());
			
//			Connection physicalCon = ((WrappedConnection)dbConnection).getUnderlyingConnection();		
//			ArrayDescriptor recpArrayType = ArrayDescriptor.createDescriptor("PF.T_PFREST_RECIPIENT_TAB", physicalCon); 
//			ARRAY recpArr = new ARRAY(recpArrayType, physicalCon, recipients);
			
			stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, recipient);		
			
			rset = stmt.executeQuery();
			// SEND_STATUS send_status = SEND_STATUS.RESP_RC_OK;
			
			
			while (rset.next()) {
				
				long jobID = rset.getLong("job_id");
				String event = rset.getString("event");
				String eventTime = rset.getString("event_time");
				int companyID = rset.getInt("taxi_co_id");
				int driverID = rset.getInt("driver_id");
				String secondDeviceID = rset.getString("snd_device_id");
			    String badgeNr = rset.getString("badge_nr");
			    String callsign = rset.getString("callsign");
			     
			    double geoLatitude = rset.getDouble("geo_lat");
			    double geoLongitude = rset.getDouble("geo_long");
			    String vehicleStatus = rset.getString("vehicle_status");
			    String lastGPSDtm = rset.getString("gpstime");
			    
			    String reqPickupDTM = rset.getString("req_pickup_dtm");
			    String addressMask = rset.getString("offer_address_mask");
			    int areaIDOfrMask = rset.getInt("area_id_for_ofrmask");
			    
			    
			    
			    logger.info("runSendSDEventsTask: get 2nd device event with job ID: " + jobID + ", 2nd device = " + secondDeviceID +", callsign = "+  callsign + ", lastGPSDTM=" + lastGPSDtm + ", driver ID=" + driverID +", event=" + event);
			      
			    SecDeviceEvent deviceEvent = new SecDeviceEvent(jobID, companyID, driverID, badgeNr, secondDeviceID, callsign);
			    int eventID = PFRestEventType.PFEvent_Type_Not_Known.getEventID();
			    try{
			    	eventID = Integer.parseInt(event);
			    }catch(NumberFormatException ne){
			    	logger.error("runSendSDEventsTask get wrong event: " + event);
			    }
			    deviceEvent.setEventID(eventID);
			    
			    HttpPost post = getSDEventHttpPost(createRestEvent(getJobEventType(eventID), deviceEvent, new SecDevAddrFormatEvent(reqPickupDTM, addressMask, areaIDOfrMask)));
			    if(post != null) devEvents.add(post);
			}

		}catch(SQLException se){
			logger.error("Exception in runSendTGEventsTask " ,se);
		}finally{
			try {if (rset != null) rset.close();} catch (SQLException ignore) {};
			try {if (stmt != null) stmt.close();} catch (SQLException ignore) {};
			try {if (dbConnection != null) dbConnection.close();} catch (SQLException ignore) {};
		}
		if(devEvents != null && devEvents.size() > 0) sendHttpSDEvents(devEvents);
		
	}
	
	
	private PFRestEvent createRestEvent(String eventType, SecDeviceEvent devEvent, SecDevAddrFormatEvent addrFormat){
		PFRestEvent pfRestEvent = new PFRestEvent();
		if(PFRestEventType.PFEvent_Type_2nd_Out_Vehicle.getEventType().equals(eventType)){
			devEvent.setDataType(PFRestEventType.PFEvent_Type_2nd_Out_Vehicle.getEventType());
			return devEvent;
		}else if(PFRestEventType.PFEvent_Type_2nd_In_Vehicle.getEventType().equals(eventType)){
			devEvent.setDataType(PFRestEventType.PFEvent_Type_2nd_In_Vehicle.getEventType());
			return devEvent;
		}else if(PFRestEventType.PFEvent_Type_2nd_Job_Offer.getEventType().equals(eventType)){
			devEvent.setDataType(PFRestEventType.PFEvent_Type_2nd_Job_Offer.getEventType());
			return createJobOfferEvent(devEvent, addrFormat);
		}else if(PFRestEventType.PFEvent_Type_2nd_Job_Cancel.getEventType().equals(eventType)){
			devEvent.setDataType(PFRestEventType.PFEvent_Type_2nd_Job_Cancel.getEventType());
			return devEvent;
		}else if(PFRestEventType.PFEvent_Type_2nd_MDT_SIGNOFF.getEventType().equals(eventType)){
			devEvent.setDataType(PFRestEventType.PFEvent_Type_2nd_MDT_SIGNOFF.getEventType());
			return devEvent;
		}else {
			return pfRestEvent;
		}
	}
	
	
	private SecDevJobOfferEvent createJobOfferEvent(SecDeviceEvent devEvent, SecDevAddrFormatEvent addrFormat){
		SecDevJobOfferEvent jobOfferEvent = new SecDevJobOfferEvent(devEvent);
		try{
			CartModel jobs =  jobSearch.getModifyJobDetails_EntityEJB("", "", null, "" + devEvent.getJobID(), CartEvent.ACTIONTYPE_JOB_RETRIEVAL);
			if(jobs != null && jobs.getSize() > 0){
				CartItem cart = jobs.getItemAt(0);
				assignJobOfferEvent(jobOfferEvent, cart, addrFormat);
				assignJobOfferLabels(jobOfferEvent);
			}
		}catch(Exception e){
			logger.error("createJobOfferEvent failed: ", e);
		}
		return jobOfferEvent;
	}
	
	private void assignJobOfferLabels(SecDevJobOfferEvent jobOfferEvent){
		if(jobLabels != null){
			jobOfferEvent.setJobTypeLabel(jobLabels.getJobTypeLabel());
			jobOfferEvent.setPickupZoneLabel(jobLabels.getPickupZoneLabel());
			jobOfferEvent.setPickupAreaLabel(jobLabels.getPickupAreaLabel());
			jobOfferEvent.setNbPassengerLabel(jobLabels.getNbPassengerLabel());
			jobOfferEvent.setNbStopLabel(jobLabels.getNbStopLabel());
			
			jobOfferEvent.setPickupAddressLabel(jobLabels.getPickupAddressLabel());
			jobOfferEvent.setDropoffAddressLabel(jobLabels.getDropoffAddressLabel());
			jobOfferEvent.setPaymentTypeLabel(jobLabels.getPaymentTypeLabel());
			
			jobOfferEvent.setLayoutAmountLabel(jobLabels.getLayoutAmountLabel());
			jobOfferEvent.setFixedPriceLabel(jobLabels.getFixedPriceLabel());
		}
	}
	
	
	private void assignJobOfferEvent(SecDevJobOfferEvent jobOfferEvent, CartItem cart, SecDevAddrFormatEvent addrFormat){
		if(cart == null) return;
		jobOfferEvent.setJobType(translateJobType(cart.getJOB_TYPE()));
		try{
			int nbPassenger = cart.getPassengetCount() != null ? Integer.parseInt(cart.getPassengetCount()) : 0;
			jobOfferEvent.setNbPassenger(nbPassenger);
		}catch(NumberFormatException ne){
			logger.error("assignJobOfferEvent with number format exception: " + cart.getPassengetCount());
		}
		Vector<StopPoint> stops = cart.getStopPoints(); 
		if(stops != null && stops.size() > 0){
			jobOfferEvent.setNbStop(stops.size());
			StopPoint stopPoint = stops.get(0);
			if(stopPoint.getContactInfo() != null) jobOfferEvent.setPaymentType(stopPoint.getContactInfo().getAccount());
			AddressItem pickupAddress = stopPoint.getPickupAddress();
			if(pickupAddress != null){
				jobOfferEvent.setPickupArea(pickupAddress.getAreaName());
				jobOfferEvent.setPickupZone(pickupAddress.getRegion());
				if(!isPickupAddressMasked(jobOfferEvent.getTaxiCompanyID())){
					jobOfferEvent.setPickupAddress(getJobOfferAddress(jobOfferEvent.getJobID(), addrFormat, jobOfferEvent.getTaxiCompanyID()));
				}else jobOfferEvent.setPickupAddress("");		//mask pickup address
				//jobOfferEvent.setPickupAddress(pickupAddress.getStrNum() + " " + pickupAddress.getStrName() + ", " + pickupAddress.getRegion());
			}
			AddressItem dropAddress = stopPoint.getSetdownAddress();
			if(dropAddress != null){
				String formatDropOffAddress = (dropAddress.getStrNum() != null && dropAddress.getStrNum().trim().length() > 0) ? (dropAddress.getStrNum() + " " ) : "";
				formatDropOffAddress += dropAddress.getStrName();
				formatDropOffAddress += (dropAddress.getRegion() != null && dropAddress.getRegion().trim().length() > 0) ? (", " + dropAddress.getRegion()) : "";
				jobOfferEvent.setDropoffAddress(formatDropOffAddress);
			}
		}
		if(cachedParam != null){
			jobOfferEvent.setAutoRejectTimeout(cachedParam.getCompanyParameterIntValue(jobOfferEvent.getTaxiCompanyID(), C_SMS_ACCEPT_TIME));
			jobOfferEvent.setBackInVehicleTimeout(cachedParam.getCompanyParameterIntValue(jobOfferEvent.getTaxiCompanyID(), C_DVDEV_ACCEPT_TIME));
			jobOfferEvent.setStreetHireButton("Y".equalsIgnoreCase(cachedParam.getCompanyParameterValue(jobOfferEvent.getTaxiCompanyID(),  C_DVDEV_STRHIRE_BTN) ) );
		}
		// jobOfferEvent.setJobAttributes(getJobAttributesFromCart(cart, jobOfferEvent.getTaxiCompanyID()));  not used any longer
		
	}
	
	
	private boolean isPickupAddressMasked(int companyID){
		if(cachedParam == null || companyID <= 0) return false;
		else return "Y".equalsIgnoreCase(cachedParam.getCompanyParameterValue(companyID, C_DVDEV_MASK_PICKUP));
	}
	
	private String getJobOfferAddress(long jobID, SecDevAddrFormatEvent addrFormat, int companyID){
		if(jobID <= 0 ) return null;
		String address = "";
		
			
		String query = "select decode(s.forced_address, NULL, spvrpages.expand_addr_offer(s.address_id, s.forced_unit_nr, 'N', ?, ?, ?, ?), spvrpages.format_forced_address_offer(s.forced_address, s.forced_unit_nr, ?, ?, ?, ?)) as job_offer_address \n" +
						" from stop_points s, jobs j, orders o \n" +  
						" where j.job_id = ? \n" +
						" and o.order_id = j.order_id \n" +
						" and s.order_id = o.order_id \n" +
						" and s.stop_enumeration = 1 \n";
		
		PreparedStatement stmt = null;
		ResultSet rset = null;
		Connection dbConnection = null;
		
		try 
		{
			if ((dbConnection = pfDataSource.getConnection()) == null){
				logger.error("Failed to get db connection.....");
				return null;
			}
			logger.debug("getJobOfferAddress: " + jobID +", pickupDTM =" + addrFormat.getReqPickupDTM() +", areaID =" + addrFormat.getAreaIDOfrMask() +", addrMask =" + addrFormat.getAddressMask());
			logger.debug(query);
			
			stmt = dbConnection.prepareStatement(query);
			stmt.setTimestamp(1, new Timestamp(Utilities.convertOSPDefaultDate(addrFormat.getReqPickupDTM()).getTime()));
			stmt.setInt(2, companyID);
			stmt.setInt(3, addrFormat.getAreaIDOfrMask());
			stmt.setString(4, addrFormat.getAddressMask());
			
			stmt.setTimestamp(5, new Timestamp(Utilities.convertOSPDefaultDate(addrFormat.getReqPickupDTM()).getTime()));
			stmt.setInt(6, companyID);
			stmt.setInt(7, addrFormat.getAreaIDOfrMask());
			stmt.setString(8, addrFormat.getAddressMask());
			
			stmt.setLong(9, jobID);		
			
			rset = stmt.executeQuery();
			
			
			if (rset.next()) {
				address = rset.getString(1);
				logger.debug("get job offer address string: " + address);
			}

		}catch(SQLException se){
			logger.error("Exception in getJobOfferAddress " ,se);
		}finally{
			try {if (rset != null) rset.close();} catch (SQLException ignore) {};
			try {if (stmt != null) stmt.close();} catch (SQLException ignore) {};
			try {if (dbConnection != null) dbConnection.close();} catch (SQLException ignore) {};
		}
		return address;
	}
	
	
	private String translateJobType(String jobType)  {
		if(jobType == null || jobType.trim().length() != 1) return jobType;
		
		String type = "";
        if (jobType.equalsIgnoreCase("I"))
        	type = "ASAP";
        else if(jobType.equalsIgnoreCase("P"))
        	type = "PBOK";
        else if(jobType.equalsIgnoreCase("T"))
        	type = "RTC";
        else if(jobType.equalsIgnoreCase("Q"))
        	type = "QUICKBOOKER";
        else if (jobType.equalsIgnoreCase("E"))
            type = "HELD TEMPLATE";
        else if(jobType.equalsIgnoreCase("S"))
        	type = "STREET HIRE";
        
        return type;
    }
	
	private String[] getJobAttributesFromCart(CartItem cart, int companyID){
		if(cachedParam == null || cart == null ){
			logger.error("getJobAttributesFromJobEvent failed with cached param null......");
			return null;
		}
		
		
		ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>();
		
		HashMap<String, SysAttrListItem> drvAttrMap = cachedParam.getDrvAttributeMap().get(companyID);
		HashMap<String, SysAttrListItem> vehAttrMap = cachedParam.getVehAttributeMap().get(companyID);
		// CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
		attributes.addAll(Utilities.getAttributesItem(drvAttrMap, Utilities.ToBinaryAttributesString(cart.getDriverAttributeBinary())));
		attributes.addAll(Utilities.getAttributesItem(vehAttrMap, Utilities.ToBinaryAttributesString(cart.getVehicleAttributeBinary())));
		
		if(attributes.size() > 0){
			String[] attrArray = new String[attributes.size()];
			for(int i=0; i<attributes.size(); i++){
				attrArray[i] = attributes.get(i).getAttrLongName();
			}
			
			return attrArray;
		}else return null;
	}
	
	private String getJobEventType(int eventID){
		String dataType = PFRestEventType.PFEvent_Type_Not_Known.getEventType();
		
		for(PFRestEventType e : PFRestEventType.values()){
			if(eventID == e.getEventID()) return e.getEventType();
		}
		
		return dataType;
	}
	
	private void sendHttpSDEvents(ArrayList<HttpPost> posts){
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
	
	
	private HttpPost getSDEventHttpPost(PFRestEvent jobEvent){
		
		if((secDeviceJobURL == null && secDeviceVehicleURL == null) || jobEvent == null || getJsonMap() == null) return null;
		
		HttpPost post = createHttpPostByEventType(jobEvent);
		if(post == null) return null;
		
		StringEntity entity;
		try {
			String eventContent = jsonMap.writeValueAsString(new PostEvent(jobEvent));
			logger.debug("PFRest is to send DDevice event: " + eventContent);
			entity = new StringEntity(eventContent, ContentType.create("application/json", "UTF-8"));
			entity.setContentEncoding(CharEncoding.UTF_8);
			post.setEntity(entity);
			return post;
		} catch (UnsupportedCharsetException e) {
			logger.error("getSDEventHttpPost failed with UnsupportedCharsetException: ", e);
			return null;
		} catch (JsonProcessingException e) {
			logger.error("getSDEventHttpPost failed with JsonProcessingException: ", e);
			return null;
		}
		
		
	}
	
	
	private HttpPost createHttpPostByEventType(PFRestEvent jobEvent){
		if(PFRestEventType.PFEvent_Type_2nd_Out_Vehicle.getEventType().equals(jobEvent.getDataType()) ||
				PFRestEventType.PFEvent_Type_2nd_In_Vehicle.getEventType().equals(jobEvent.getDataType() ) ||
				PFRestEventType.PFEvent_Type_2nd_MDT_SIGNOFF.getEventType().equals(jobEvent.getDataType() )){
			if(secDeviceVehicleURL != null && secDeviceVehicleURL.trim().length() > 0) return new HttpPost(secDeviceVehicleURL);
			else return null;
		}else if(PFRestEventType.PFEvent_Type_2nd_Job_Offer.getEventType().equals(jobEvent.getDataType()) ||
				PFRestEventType.PFEvent_Type_2nd_Job_Cancel.getEventType().equals(jobEvent.getDataType() )){
			if(secDeviceJobURL != null && secDeviceJobURL.trim().length() > 0) return new HttpPost(secDeviceJobURL);
			else return null;
		}else return null;
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
