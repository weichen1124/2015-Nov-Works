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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/MeterOffEvent.java $
* 
* PF-16653, 07/23/15, DChen, remove properties from pf events.
* 
* PF-16653, 07/17/15, DChen, add event type for TG.
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import java.util.ArrayList;

import com.dds.pathfinder.pfrest.resources.PaymentListItem;
import com.fasterxml.jackson.annotation.JsonProperty;


public class MeterOffEvent extends PFRestEvent {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "job-id")
	private Long jobID;
	
	@JsonProperty(value = "external-job-id")
	private String externalJobID;
	
	@JsonProperty(value = "taxi-company-id")
	private int taxiCompanyID;
	
	@JsonProperty(value = "vehicle")
	private String vehicleCallsign;
	
	@JsonProperty(value = "meter-start-distance")
	private String meterStartDistance;
	
	@JsonProperty(value = "meter-start-time")
	private String meterStartTime;
	
	@JsonProperty(value = "payments")
	private ArrayList<PaymentListItem> paymentList;
	
	public MeterOffEvent(PFJobEvent jobEvent){
		super();
		setDataType(PFRestEventType.PFEvent_Type_Meteroff.getEventType());
		if(jobEvent != null){
			this.jobID = jobEvent.getJobID();
			this.externalJobID = jobEvent.getExternalJobID();
			this.taxiCompanyID = jobEvent.getTaxiCompanyID();
			this.vehicleCallsign = jobEvent.getVehicleCallsign();
		}
		// setProperties(new MeterOffProperties(jobEvent));
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

	public String getMeterStartDistance() {
		return meterStartDistance;
	}

	public void setMeterStartDistance(String meterStartDistance) {
		this.meterStartDistance = meterStartDistance;
	}

	public String getMeterStartTime() {
		return meterStartTime;
	}

	public void setMeterStartTime(String meterStartTime) {
		this.meterStartTime = meterStartTime;
	}

	public ArrayList<PaymentListItem> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(ArrayList<PaymentListItem> paymentList) {
		this.paymentList = paymentList;
	}

}
