/****************************************************************************
 * 
 *					Copyright (c), $Date: 1/17/14 5:08p $
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 * **************************************************************************
 * File Name: TripAlertRequest.java 
 * 
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/scheduletask/TripAlertRequest.java $
 * Oct 5, 2015 Y Yin
 * PF-16808. Renamed fare to totalAmount. Added meterFare and otherExpenses.
 * 
 * PF-16399, 08/26/15, DChen, port it to 4.30 version.
 * 
 * Jun 12, 2015, Yutian Yin
 * PF-16549. Getting redispatch job ID for redispatch event.
 * 
 * PF-16093 added FARE_EVENT for driver initiated payment
 * 
 * PF-16074 added TAXILIMO_MOBILE_ORIGN
 * 
 * Added OSP REJECT_OFFER and TRIP_OFFER
 * 
 * 4     1/17/14 5:08p Ezhang
 * PF-15841 changeAPI to String type.
 * PF-15839 Add REJECT FOR STREET HIRE event for MB
 * 
 * 3     11/20/13 11:24a Sfoladian
 * PF-15597- Late Trip Notification Support
 * 
 * 2     11/12/13 2:49p Ezhang
 * PF-15594 add new event for message to rider
 * 
 * 1     3/08/13 3:31p Dchen
 * Separate rider charge service from callbooker package.
 * 
 * 5     11/02/12 9:58a Ezhang
 * PF-14845 added arrived event for Mobile booking
 * 
 * 4     4/03/12 2:30p Yyin
 * C36786 - Fixed the XML_TAG_TIME_ZONE
 * 
 * 3     3/05/12 10:02a Yyin
 * C36678 - Fixed the typo in Create.
 * 
 * 2     2/16/12 3:51p Yyin
 * C36568 - Fixed the problem that an exception is thrown when there is no
 * gps for a car.
 * 
 * 1     12/28/11 5:27p Yzhan
 * Added for C36130.
 * 
 * C36130 GoFastCab Trip Status Alert request
 */
package com.dds.pathfinder.itaxiinterface.scheduletask;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.dds.pathfinder.itaxiinterface.util.Debug2;

/**
 * @author ezhang
 *
 */
public class TripAlertRequest {
	public final static String XML_TAG_OPEN = "<";
	public final static String XML_TAG_OPEN_END = "</";
	public final static String XML_TAG_CLOSE = ">";
	public final static String XML_TAG_REQUEST = "StatusAlertRequest";
	//public final static String XML_TAG_PROVIDER_RIDE_ID = "provider_ride_id";
	public final static String XML_TAG_TAXI_API_KEY = "taxi_api_key";
	public final static String XML_TAG_TAXI_RIDE_ID = "taxi_ride_id";
	public final static String XML_TAG_EVENT = "event";
	public final static String XML_TAG_TIME = "time";
	public final static String XML_TAG_LAT = "latitude";
	public final static String XML_TAG_LONG = "longitude";
	public final static String XML_TAG_VEHICLE = "vehicle_number";
	public final static String XML_TAG_DRIVER = "driver_name";
	public final static String XML_TAG_TIME_ZONE = "time_zone";
	public final static String XML_TAG_MSG_TO_RIDER = "msg_to_rider";
	public final static String XML_TAG_AMT = "amt";
	public final static String XML_TAG_REDISP_TAXI_RIDE_ID = "redispatch_taxi_ride_id";
	public final static String XML_TAG_METER_FARE = "meter_fare";
	public final static String XML_TAG_OTHER_EXPENSES = "other_expenses";
	
	
	public final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static DecimalFormat LATLONG_FORMAT = new DecimalFormat("#.######"); //We display lat/long up to 6 decimal places.
	public final static DecimalFormat FARE_FORMAT = new DecimalFormat("#.##"); //In db, max fare is Number(15, 6). We display it up to 2 decimal places.

	/** OSP status update event ID (for more event ID, see rtl_type.hdr) */
	public final static int ACCEPT_EVENT = 0;	 //OSP_JOB_ACCEPT
	public final static int METER_ON_EVENT = 1;	 //OSP_METER_ON
	public final static int METER_OFF_EVENT = 2; //OSP_METER_OFF
	public final static int CANCELLED_EVENT = 3; //OSP_COMPLETE_BY_TRIP_CANCEL
	public final static int NO_SHOW_EVENT = 4;  //OSP_COMPLETE_BY_NO_SHOW
	public final static int FORCED_COMPLETE_EVENT = 5;  //OSP_COMPLETE_FORCE
	public final static int ARRIVED_EVENT = 6;  //OSP_ARRIVED
	public final static int LATE_TRIP_EVENT = 7;  //OSP_LATE_TRIP
	public final static int REJECT_FOR_STREET_HIRE = 8; // OSP_REJECT_TRIP
	public final static int REJECT_OFFER = 9; // OSP_REJECT_OFFER
	public final static int TRIP_OFFER = 10; // OSP_JOB_OFFER
	public final static int TRIP_REDISPATCH = 11; // OSP JOB REDISPATCJED. Mobile booker an Zoro job only.
	public final static int FARE_EVENT = 97; //driver request fare amount
	public final static int MSG_TO_RIDER = 98;	//Canned message to rider
	public final static int GPS_UPDATE_EVENT = 99;  //OSP_GPS_UPDATE
	
	private static Debug2 logger = Debug2.getLogger(TripAlertRequest.class);
	public static final String MOBILEBOOKING_ORIGN = "MB";
	public static final String GOFASTCAB_ORIGN = "GF";
	public static final String TAXILIMO_MOBILE_ORIGN = "ZA";
	
	private String taxi_api_key;
	private Integer taxi_ride_id;
	private Integer event;
	private Date time;
	private Double latitude; 	//optional
	private Double longitude;   //optional
	private String vehicle_number;
	private String driver_name;
	private String time_zone;
	private String msgToRider;
	private String totalAmount;
	private Integer redisp_taxi_ride_id;
	private String meterFare;
	private String otherExpenses;
	public TripAlertRequest() {
		taxi_api_key = null;
		taxi_ride_id = null;
		event = null;
		time = null;
		redisp_taxi_ride_id = null;
	}
	

	/**
	 * Create trip update request.
	 * 	<StatusAlertRequest> 
 * 			<taxi_api_key>1</taxi_api_key>
	 * 		<taxi_ride_id>5067</taxi_ride_id>
	 *		<ride_id>3452</ride_id>
	 *		<event>0</event>
	 *		<time>2011-11-29 10:44:40</time> 
	 *		<amt>45.00</amt>					  //optional
	 *      <latitude>49.2636</latitude> 		  //optional
	 *      <longtitude>-123.1386</longtitude>    //optional
	 *	    <vehicle_number>2</vehicle_number>	  //optional if event is cancel ( trip can be canceled before dispatching)
	 *      <driver_name>1</driver_name>		  //optional if event is cancel
	 *      <time_zone>PST</time_zone>
	 *	</StatusAlertRequest>
	 *	@return the String request if successful, null otherwise.
	 */
	public String create()
	{
		if (!checkReqFields())
		{
			logger.debug("Some required fields are missing for JobID " + taxi_ride_id + " event " + event);
			return null;
		}
			
		StringBuffer requestBuf = new StringBuffer();
		
		requestBuf.append(XML_TAG_OPEN).append(XML_TAG_REQUEST).append(XML_TAG_CLOSE);
		
		//Company ID
		
		requestBuf.append(XML_TAG_OPEN).append(XML_TAG_TAXI_API_KEY).append(XML_TAG_CLOSE);
		requestBuf.append(taxi_api_key);
		requestBuf.append(XML_TAG_OPEN_END).append(XML_TAG_TAXI_API_KEY).append(XML_TAG_CLOSE);
		
		
		//PathFinder Job ID
		requestBuf.append(XML_TAG_OPEN).append(XML_TAG_TAXI_RIDE_ID).append(XML_TAG_CLOSE);
		requestBuf.append(taxi_ride_id);
		requestBuf.append(XML_TAG_OPEN_END).append(XML_TAG_TAXI_RIDE_ID).append(XML_TAG_CLOSE);
		
		
		/** OSP event ID (see rtl_type.hdr)
		    OSP_JOB_ACCEPT                  CONSTANT NUMBER(1) := 0;     -- "Accept" on Mobile
		    OSP_METER_ON                    CONSTANT NUMBER(1) := 1;     -- "Load" on Mobile
		    OSP_METER_OFF                   CONSTANT NUMBER(1) := 2;     -- "Unload" and print out receipt on Mobile
		    OSP_COMPLETE_BY_TRIP_CANCEL     CONSTANT NUMBER(2) := 3;    -- Operator cancels job
		    OSP_COMPLETE_BY_NO_SHOW         CONSTANT NUMBER(2) := 4;    -- "No Show" on Mobile and Operator cancels job
		    OSP_COMPLETE_FORCE              CONSTANT NUMBER(2) := 5;    -- Operator initializes force complete on a job
		    OSP_ARRIVED              		CONSTANT NUMBER(2) := 6;    -- "Arrived" on Mobile
		    OSP_MSG_TO_RIDER				CONSTANT NUMBER(2) :=98;	--  Canned Msg from Mobile
		    OSP_GPS_UPDATE         			CONSTANT NUMBER(2) := 99;    -- "No Show" on Mobile and Operator cancels job
		 */
		requestBuf.append(XML_TAG_OPEN).append(XML_TAG_EVENT).append(XML_TAG_CLOSE);
		requestBuf.append(event);
		requestBuf.append(XML_TAG_OPEN_END).append(XML_TAG_EVENT).append(XML_TAG_CLOSE);
		
		//Time the event took place (JBOSS local time), "YYYY-MM-DD HH:MM:SS" 
		requestBuf.append(XML_TAG_OPEN).append(XML_TAG_TIME).append(XML_TAG_CLOSE);
		requestBuf.append(new SimpleDateFormat(TIME_FORMAT).format(time.getTime()));
		requestBuf.append(XML_TAG_OPEN_END).append(XML_TAG_TIME).append(XML_TAG_CLOSE);
		
		if (latitude != null)
		{
			requestBuf.append(XML_TAG_OPEN).append(XML_TAG_LAT).append(XML_TAG_CLOSE);
			requestBuf.append(LATLONG_FORMAT.format(latitude));
			requestBuf.append(XML_TAG_OPEN_END).append(XML_TAG_LAT).append(XML_TAG_CLOSE);
		}
		if (longitude != null)
		{
			requestBuf.append(XML_TAG_OPEN).append(XML_TAG_LONG).append(XML_TAG_CLOSE);
			requestBuf.append(LATLONG_FORMAT.format(longitude));
			requestBuf.append(XML_TAG_OPEN_END).append(XML_TAG_LONG).append(XML_TAG_CLOSE);
		}
		if(vehicle_number != null)
		{
			requestBuf.append(XML_TAG_OPEN).append(XML_TAG_VEHICLE).append(XML_TAG_CLOSE);
			requestBuf.append(vehicle_number);
			requestBuf.append(XML_TAG_OPEN_END).append(XML_TAG_VEHICLE).append(XML_TAG_CLOSE);
		}
		if(driver_name != null)
		{
			requestBuf.append(XML_TAG_OPEN).append(XML_TAG_DRIVER).append(XML_TAG_CLOSE);
			requestBuf.append(driver_name);
			requestBuf.append(XML_TAG_OPEN_END).append(XML_TAG_DRIVER).append(XML_TAG_CLOSE);
		}
		if(time_zone != null)
		{
			requestBuf.append(XML_TAG_OPEN).append(XML_TAG_TIME_ZONE).append(XML_TAG_CLOSE);
			requestBuf.append(time_zone);
			requestBuf.append(XML_TAG_OPEN_END).append(XML_TAG_TIME_ZONE).append(XML_TAG_CLOSE);
		}
		
		//PF-15594
		if(getMsgToRider() != null) {
			requestBuf.append(XML_TAG_OPEN).append(XML_TAG_MSG_TO_RIDER).append(XML_TAG_CLOSE);
			requestBuf.append(getMsgToRider());
			requestBuf.append(XML_TAG_OPEN_END).append(XML_TAG_MSG_TO_RIDER).append(XML_TAG_CLOSE);
		}
		
		//PF-16093
		if(getTotalAmount() != null) {
			requestBuf.append(XML_TAG_OPEN).append(XML_TAG_AMT).append(XML_TAG_CLOSE);
			requestBuf.append(getTotalAmount());
			requestBuf.append(XML_TAG_OPEN_END).append(XML_TAG_AMT).append(XML_TAG_CLOSE);
		}
				
			
		//PF-16549
		if(getRedispatchTaxiRideId() != null) {
			requestBuf.append(XML_TAG_OPEN).append( XML_TAG_REDISP_TAXI_RIDE_ID).append(XML_TAG_CLOSE);
			requestBuf.append(getRedispatchTaxiRideId());
			requestBuf.append(XML_TAG_OPEN_END).append( XML_TAG_REDISP_TAXI_RIDE_ID).append(XML_TAG_CLOSE);
		}
		
		if(getMeterFare() != null) {
			requestBuf.append(XML_TAG_OPEN).append( XML_TAG_METER_FARE).append(XML_TAG_CLOSE);
			requestBuf.append(getMeterFare());
			requestBuf.append(XML_TAG_OPEN_END).append( XML_TAG_METER_FARE).append(XML_TAG_CLOSE);
		}
		
		if(getOtherExpenses() != null) {
			requestBuf.append(XML_TAG_OPEN).append( XML_TAG_OTHER_EXPENSES).append(XML_TAG_CLOSE);
			requestBuf.append(getOtherExpenses());
			requestBuf.append(XML_TAG_OPEN_END).append( XML_TAG_OTHER_EXPENSES).append(XML_TAG_CLOSE);
		}
		requestBuf.append(XML_TAG_OPEN_END).append(XML_TAG_REQUEST).append(XML_TAG_CLOSE);
		logger.debug("created request:\n" + requestBuf.toString());
		return requestBuf.toString();
	}
	
	/**
	 * Check if all required fields are present.
	 * @return true if all required fields are present
	 */
	public boolean checkReqFields()
	{
		if (taxi_ride_id == null 
				|| taxi_ride_id == -1
				|| event == null
				|| event == -1
				|| time == null)
		{
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Setters and getters for all request fields
	 */

	public String getTaxi_api_key() {
		return taxi_api_key;
	}
	
	public void setTaxi_api_key(String taxi_api_key) {
		this.taxi_api_key = taxi_api_key;
	}
	
	public Integer getTaxi_ride_id() {
		return taxi_ride_id;
	}

	public void setTaxi_ride_id(Integer taxi_ride_id) {
		this.taxi_ride_id = taxi_ride_id;
	}


	public Integer getEvent() {
		return event;
	}

	public void setEvent(Integer event) {
		this.event = event;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getVehicle_number() {
		return vehicle_number;
	}

	public void setVehicle_number(String vehicle_number) {
		this.vehicle_number = vehicle_number;
	}

	public String getDriver_name() {
		return driver_name;
	}
	
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}
	
	public String getTime_zone() {
		return time_zone;
	}
	
	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}


	public String getMsgToRider() {
		return msgToRider;
	}


	public void setMsgToRider(String msgToRider) {
		this.msgToRider = msgToRider;
	}


	public String getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getRedispatchTaxiRideId() {
		return redisp_taxi_ride_id;
	}


	public void setRedispatchTaxiRideId(Integer redisp_taxi_ride_id) {
		this.redisp_taxi_ride_id = redisp_taxi_ride_id;
	}

	public String getMeterFare() {
		return meterFare;
	}


	public void setMeterFare(String meterFare) {
		this.meterFare = meterFare;
	}
	public String getOtherExpenses() {
		return otherExpenses;
	}

	public void setOtherExpenses(String otherExpenses) {
		this.otherExpenses = otherExpenses;
	}
}
