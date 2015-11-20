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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/PFJobEvent.java $
* 
* PF-16785, 09/18/15, DChen, change payment to float.
* 
* PF-16785, 09/18/15, DChen, json format changes.
* 
* PF-16785, 09/18/15, DChen, add ftj data event for samplan.
* 
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import com.fasterxml.jackson.annotation.JsonProperty;


public class FtjDataEvent extends PFRestEvent {

	
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "job-id")
	private Long jobID;
	
	@JsonProperty(value = "external-job-id")
	private String externalJobID;
	
	
	@JsonProperty(value = "vehicle")
	private String vehicleCallsign;
	
	@JsonProperty(value = "meter-start-dist")
	private String meterOnDist;
    
	@JsonProperty(value = "meter-start-time")
	private String meterOnTime;
    
	@JsonProperty(value = "cash-pay")
	private float cashPayment;
    
	@JsonProperty(value = "account-pay")
	private float accountPayment;

	
	
	
	public FtjDataEvent() {
		super();
	}

	
	public FtjDataEvent(PFJobEvent jobEvent){
		super();
		setDataType(PFRestEventType.PFEvent_Type_Ftj_Data.getEventType());
		if(jobEvent != null){
			this.jobID = jobEvent.getJobID();
			this.externalJobID = jobEvent.getExternalJobID();
			this.vehicleCallsign = jobEvent.getVehicleCallsign();
			this.meterOnDist = jobEvent.getMeterOnDist();
			this.meterOnTime = jobEvent.getMeterOnTime();
			this.cashPayment = jobEvent.getCashPayment();
			this.accountPayment = jobEvent.getAccountPayment();
		}

	}

	public FtjDataEvent(Long jobID, String externalJobID, 
			String vehicleCallsign, String meterOnDist, String meterOnTime,
			float cashPayment, float accountPayment) {
		super();
		this.jobID = jobID;
		this.externalJobID = externalJobID;
		this.vehicleCallsign = vehicleCallsign;
		this.meterOnDist = meterOnDist;
		this.meterOnTime = meterOnTime;
		this.cashPayment = cashPayment;
		this.accountPayment = accountPayment;
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


	public String getVehicleCallsign() {
		return vehicleCallsign;
	}

	public void setVehicleCallsign(String vehicleCallsign) {
		this.vehicleCallsign = vehicleCallsign;
	}

	public String getMeterOnDist() {
		return meterOnDist;
	}

	public void setMeterOnDist(String meterOnDist) {
		this.meterOnDist = meterOnDist;
	}

	public String getMeterOnTime() {
		return meterOnTime;
	}

	public void setMeterOnTime(String meterOnTime) {
		this.meterOnTime = meterOnTime;
	}

	public float getCashPayment() {
		return cashPayment;
	}

	public void setCashPayment(float cashPayment) {
		this.cashPayment = cashPayment;
	}

	public float getAccountPayment() {
		return accountPayment;
	}

	public void setAccountPayment(float accountPayment) {
		this.accountPayment = accountPayment;
	}
	
	
	
	
	
}
