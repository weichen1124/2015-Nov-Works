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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/MeterOnProperties.java $
* 
* PF-16653, 07/17/15, DChen, add event type for TG.
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeterOnProperties extends EventProperties {

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
	private String eventDTM;
	
	@JsonProperty(value = "passenger")
	private String passengerName;

	public MeterOnProperties(PFJobEvent jobEvent){
		super();
		if(jobEvent != null){
			this.jobID = jobEvent.getJobID();
			this.externalJobID = jobEvent.getExternalJobID();
			this.taxiCompanyID = jobEvent.getTaxiCompanyID();
			this.vehicleCallsign = jobEvent.getVehicleCallsign();
			this.vehicleLatitude = jobEvent.getVehicleLatitude();
			this.vehicleLongitude = jobEvent.getVehicleLongitude();
			this.eventDTM = jobEvent.getEventDTM();
			this.passengerName = jobEvent.getPassengerName();
		}
	}
	
	
	
	public MeterOnProperties(Long jobID, String externalJobID,
			int taxiCompanyID, String vehicleCallsign, double vehicleLatitude,
			double vehicleLongitude, String eventDTM, String passengerName) {
		super();
		this.jobID = jobID;
		this.externalJobID = externalJobID;
		this.taxiCompanyID = taxiCompanyID;
		this.vehicleCallsign = vehicleCallsign;
		this.vehicleLatitude = vehicleLatitude;
		this.vehicleLongitude = vehicleLongitude;
		this.eventDTM = eventDTM;
		this.passengerName = passengerName;
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

	public String getEventDTM() {
		return eventDTM;
	}

	public void setEventDTM(String eventDTM) {
		this.eventDTM = eventDTM;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

}
