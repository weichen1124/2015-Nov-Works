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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/PassengerEvent.java $
* 
* PF-16809, 09/28/15, DChen, created for passenger events.
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PassengerEvent extends PFRestEvent {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "job-id")
	private Long jobID;
	
	@JsonProperty(value = "external-job-id")
	private String externalJobID;
	
	@JsonProperty(value = "taxi-company-id")
	private int taxiCompanyID;
	
	@JsonProperty(value = "vehicle")
	private String vehicleCallsign;
	
	@JsonProperty(value = "latitude")
	private double vehicleLatitude;
		
	@JsonProperty(value = "longitude")
	private double vehicleLongitude;
		
	@JsonProperty(value = "time")
	private String passengerEventTime;
	
	@JsonProperty(value = "passenger")
	private String passengerName;
	
	@JsonProperty(value = "stop-point")
	private int stopPointIndex;
	
	
	public PassengerEvent() {
		super();
	}

	public PassengerEvent(PFJobEvent jobEvent, String eventType){
		super();
		setDataType(eventType); 
		if(jobEvent != null){
			this.jobID = jobEvent.getJobID();
			this.externalJobID = jobEvent.getExternalJobID();
			this.taxiCompanyID = jobEvent.getTaxiCompanyID();
			this.vehicleCallsign = jobEvent.getVehicleCallsign();
			this.vehicleLatitude = jobEvent.getVehicleLatitude();
			this.vehicleLongitude = jobEvent.getVehicleLongitude();
			this.passengerEventTime = jobEvent.getEventDTM();
			this.passengerName = jobEvent.getPassengerName();
			this.stopPointIndex = jobEvent.getStopEnumeration();
		}
	}

	public Long getJobID() {
		return jobID;
	}

	public void setJobID(Long jobID) {
		this.jobID = jobID;
	}

	public String getExternalJobID() {
		return externalJobID;
	}

	public void setExternalJobID(String externalJobID) {
		this.externalJobID = externalJobID;
	}

	public int getTaxiCompanyID() {
		return taxiCompanyID;
	}

	public void setTaxiCompanyID(int taxiCompanyID) {
		this.taxiCompanyID = taxiCompanyID;
	}

	public String getVehicleCallsign() {
		return vehicleCallsign;
	}

	public void setVehicleCallsign(String vehicleCallsign) {
		this.vehicleCallsign = vehicleCallsign;
	}

	public double getVehicleLatitude() {
		return vehicleLatitude;
	}

	public void setVehicleLatitude(double vehicleLatitude) {
		this.vehicleLatitude = vehicleLatitude;
	}

	public double getVehicleLongitude() {
		return vehicleLongitude;
	}

	public void setVehicleLongitude(double vehicleLongitude) {
		this.vehicleLongitude = vehicleLongitude;
	}

	public String getPassengerEventTime() {
		return passengerEventTime;
	}

	public void setPassengerEventTime(String passengerEventTime) {
		this.passengerEventTime = passengerEventTime;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public int getStopPointIndex() {
		return stopPointIndex;
	}

	public void setStopPointIndex(int stopPointIndex) {
		this.stopPointIndex = stopPointIndex;
	}

}
