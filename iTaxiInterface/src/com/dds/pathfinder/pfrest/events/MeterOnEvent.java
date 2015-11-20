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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/MeterOnEvent.java $
* 
* PF-16653, 07/23/15, DChen, remove properties from pf events.
* 
* PF-16653, 07/17/15, DChen, add event type for TG.
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import com.fasterxml.jackson.annotation.JsonProperty;


public class MeterOnEvent extends PFRestEvent {

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
	private String meterOnDTM;
	
	@JsonProperty(value = "passenger")
	private String passengerName;
	
	@JsonProperty(value = "stop-point")
	private int stopPointIndex;
	
	
	
	public MeterOnEvent(PFJobEvent jobEvent){
		super();
		setDataType(PFRestEventType.PFEvent_Type_Meteron.getEventType()); 
		if(jobEvent != null){
			this.jobID = jobEvent.getJobID();
			this.externalJobID = jobEvent.getExternalJobID();
			this.taxiCompanyID = jobEvent.getTaxiCompanyID();
			this.vehicleCallsign = jobEvent.getVehicleCallsign();
			this.vehicleLatitude = jobEvent.getVehicleLatitude();
			this.vehicleLongitude = jobEvent.getVehicleLongitude();
			this.meterOnDTM = jobEvent.getEventDTM();
			this.passengerName = jobEvent.getPassengerName();
			this.stopPointIndex = jobEvent.getStopEnumeration();
		}
		// setProperties(new MeterOnProperties(jobEvent));
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

	public String getMeterOnDTM() {
		return meterOnDTM;
	}

	public void setMeterOnDTM(String meterOnDTM) {
		this.meterOnDTM = meterOnDTM;
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
