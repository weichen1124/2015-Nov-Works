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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/SecDeviceEvent.java $
* 
* PF-16605, 08/28/15, DChen, some changes required from MG.
* 
* PF-16607, 08/21/15, DChen, add event id and use diff url to notify
* 
* PF-16606, 08/17/15, DChen, job offer event.
* 
* PF-16605, 08/07/15, DChen, add 2nd device event.
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SecDeviceEvent extends PFRestEvent {

	
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "jobId")
	private Long jobID;
	
	@JsonProperty(value = "companyId")
	private int taxiCompanyID;
	
	@JsonProperty(value = "event")
	private int eventID;
	
	@JsonProperty(value = "driver-id")
	private int driverID; 
	
	@JsonProperty(value = "driver-badge-nr")
	private String badgeNumber;
	
	@JsonProperty(value = "sec-device-id")
	private String secDeviceID;
	
	@JsonProperty(value = "vehicle-call-sign")
	private String vehicleCallsign;
	
	
	public SecDeviceEvent(){
		super();
	}
	
	public SecDeviceEvent(Long jobID, int taxiCompanyID, int driverID,
			String badgeNumber, String secDeviceID, String vehicleCallsign) {
		super();
		setDataType(PFRestEventType.PFEvent_Type_Not_Known.getEventType());
		this.jobID = jobID;
		this.taxiCompanyID = taxiCompanyID;
		this.driverID = driverID;
		this.badgeNumber = badgeNumber;
		this.secDeviceID = secDeviceID;
		this.vehicleCallsign = vehicleCallsign;
	}

	public Long getJobID() {
		return jobID;
	}

	public void setJobID(Long jobID) {
		this.jobID = jobID;
	}

	public int getTaxiCompanyID() {
		return taxiCompanyID;
	}

	public void setTaxiCompanyID(int taxiCompanyID) {
		this.taxiCompanyID = taxiCompanyID;
	}

	public int getDriverID() {
		return driverID;
	}

	public void setDriverID(int driverID) {
		this.driverID = driverID;
	}

	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public String getSecDeviceID() {
		return secDeviceID;
	}

	public void setSecDeviceID(String secDeviceID) {
		this.secDeviceID = secDeviceID;
	}

	public String getVehicleCallsign() {
		return vehicleCallsign;
	}

	public void setVehicleCallsign(String vehicleCallsign) {
		this.vehicleCallsign = vehicleCallsign;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	
}
