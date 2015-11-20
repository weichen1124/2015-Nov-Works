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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/PFRestEvent.java $
* 
* PF-16809, 09/28/15, DChen, created for passenger events.
* 
* PF-16785, 09/18/15, DChen, add ftj data event for samplan.
* 
* PF-16763, 09/14/15, DChen, add 2nd device MDT signoff event.
* 
* PF-16653, 07/23/15, DChen, remove properties from pf events.
* 
* PF-16653, 07/17/15, DChen, add event type for TG.
* 
* PF-16605, 06/18/15, DChen, for 2nd device.
* 
* PF-16572, 05/19/15, DChen, review notification message json format.
* 
* PF-16428, 03/25/15, DChen, pfrest job events service.
* 
* PF-16428, 03/06/15, DChen, pfrest notification service.
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PFRestEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum PFRestEventType {
		PFEvent_Type_Accept("dispatch", 0),					//accept in TG
		PFEvent_Type_Meteron("meter-on", 1),				
		PFEvent_Type_Meteroff("clear-job", 2),				//clear job in TG
		PFEvent_Type_Cancelled("cancelled-event", 3),
		PFEvent_Type_Noshow("noshow-event", 4),
		PFEvent_Type_Force_Complete("force-complete-event", 5),
		PFEvent_Type_Arrived("arrived-event", 6),
		PFEvent_Type_Latetrip("latetrip-event", 7),
		PFEvent_Type_Reject_SH("reject-streethire-event", 8),
		PFEvent_Type_Reject_Offer("reject-offer-event", 9),
		PFEvent_Type_Job_Offer("job-offer-event", 10),
		PFEvent_Type_Ftj_Data("ftj-data",12),
		PFEvent_Type_Passenger_Pickup("passenger-pickup", 13),
		PFEvent_Type_Passenger_Dropoff("passenger-dropoff", 14),
		PFEvent_Type_Passenger_Noshow("passenger-noshow", 15),
		PFEvent_Type_2nd_Out_Vehicle("2nd-device-out-vehicle-event", 20),
		PFEvent_Type_2nd_Job_Offer("2nd-device-job-offer-event", 21),
		PFEvent_Type_2nd_Job_Cancel("2nd-device-job-cancel-event", 22),
		PFEvent_Type_2nd_In_Vehicle("2nd-device-in-vehicle-event", 23),
		PFEvent_Type_2nd_MDT_SIGNOFF("2nd-device-mdt-signoff-event", 24),
		PFEvent_Type_Fare("fare-event", 97),
		PFEvent_Type_Msg_Rider("msg-to-rider-event", 98),
		PFEvent_Type_GPS_Update("gps-update-event", 99),
		PFEvent_Type_Not_Known("not-known", 999);
		
		private String eventType;
		private int eventID;
		
		private PFRestEventType(String eventType, int eventID) {
			   this.eventType = eventType;
			   this.eventID = eventID;
		}

		public String getEventType(){
			return eventType;
		}
		
		public int getEventID(){
			return eventID;
		}
		
	};
	
	public final static String EVENT_TYPE_PFREST_ALIVE = "alive";
	
	@JsonProperty(value = "type")
	private String dataType;
	
//	@JsonProperty(value = "properties")
//	private EventProperties properties;
	
//	@JsonIgnore
//	private String dataID;
	
	
	@JsonIgnore
	private Date eventDTM;
	
	public PFRestEvent(){
		super();
		this.dataType = PFRestEventType.PFEvent_Type_Not_Known.getEventType();
		eventDTM = new Date();
	}
	

//	public PFRestEvent(String dataType, EventProperties properties) {
//		super();
//		this.dataType = dataType;
//		this.properties = properties;
//	}
	
	public PFRestEvent(String dataType) {
		super();
		this.dataType = dataType;
	}




	@JsonIgnore
	public Date getEventDTM() {
		return eventDTM;
	}

	@JsonIgnore
	public void setEventDTM(Date eventDTM) {
		this.eventDTM = eventDTM;
	}


	public String getDataType() {
		return dataType;
	}


	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


//	public EventProperties getProperties() {
//		return properties;
//	}
//
//
//	public void setProperties(EventProperties properties) {
//		this.properties = properties;
//	}
	 
}
