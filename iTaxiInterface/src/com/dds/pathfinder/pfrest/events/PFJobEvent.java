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
* PF-16785, 09/18/15, DChen, add ftj data event for samplan.
* 
* PF-16653, 07/23/15, DChen, remove properties from pf events.
* 
* PF-16653, 07/17/15, DChen, add event type for TG.
* 
* PF-16572, 05/19/15, DChen, review notification message json format.
* 
* PF-16428, 03/25/15, DChen, pfrest job events service.
* 
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import java.io.Serializable;
import java.util.Date;

import com.dds.pathfinder.itaxiinterface.util.Utilities;

public class PFJobEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// @JsonProperty(value = "pf-job-id")
	private Long jobID;
	
	// @JsonProperty(value = "external-job-id")
	private String externalJobID;
	
	// @JsonProperty(value = "taxi-company-id")
	private int taxiCompanyID;
	
	// @JsonProperty(value = "driver-badge")
	private String driverBadge;
	
	// @JsonProperty(value = "vehicle-callsign")
	private String vehicleCallsign;
	
	// @JsonProperty(value = "vehicle-latitude")
	private double vehicleLatitude;
	
	
	// @JsonProperty(value = "vehicle-longitude")
	private double vehicleLongitude;
	
	// @JsonProperty(value = "gps-time")
	private String gpsUpdateDTM;
	
	// @JsonProperty(value = "vehicle-status")
	private String vehicleStatus;
	
	private int nbSeats;
	
	private String drvAttributes;
	
	private String vehAttributes;
	
	private String passengerName;
	
	private String eventDTM;
	
	private int stopEnumeration;
	 
	private String meterOnDist;
    private String meterOnTime; 
    private float cashPayment; 
    private float accountPayment; 

	
	public PFJobEvent(){
		super();
	}
	

	public PFJobEvent(Long jobID, String externalJobID, int taxiCompanyID,
			String driverBadge, String vehicleCallsign, double vehicleLatitude,
			double vehicleLongitude, String gpsUpdateDTM, String vehicleStatus,
			int nbSeats, String drvAttributes, String vehAttributes,
			String passengerName, String eventDTM, int stopEnumeration) {
		super();
		this.jobID = jobID;
		this.externalJobID = externalJobID;
		this.taxiCompanyID = taxiCompanyID;
		this.driverBadge = driverBadge;
		this.vehicleCallsign = vehicleCallsign;
		this.vehicleLatitude = vehicleLatitude;
		this.vehicleLongitude = vehicleLongitude;
		this.gpsUpdateDTM = gpsUpdateDTM;
		this.vehicleStatus = vehicleStatus;
		this.nbSeats = nbSeats;
		this.drvAttributes = drvAttributes;
		this.vehAttributes = vehAttributes;
		this.passengerName = passengerName;
		this.eventDTM = eventDTM;
		this.stopEnumeration = stopEnumeration;
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

	public String getDriverBadge() {
		return driverBadge;
	}

	public void setDriverBadge(String driverBadge) {
		this.driverBadge = driverBadge;
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

	public String getGpsUpdateDTM() {
		return gpsUpdateDTM;
	}

	public void setGpsUpdateDTM(String gpsUpdateDTM) {
		this.gpsUpdateDTM = gpsUpdateDTM;
	}
	
	public void setGpsUpdateDTM(Date gpsUpdateDTM) {
		this.gpsUpdateDTM = Utilities.formatOSPDefaultDate(gpsUpdateDTM);
	}

	public String getVehicleStatus() {
		return vehicleStatus;
	}

	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

	public int getNbSeats() {
		return nbSeats;
	}

	public void setNbSeats(int nbSeats) {
		this.nbSeats = nbSeats;
	}

	public String getDrvAttributes() {
		return drvAttributes;
	}

	public void setDrvAttributes(String drvAttributes) {
		this.drvAttributes = drvAttributes;
	}

	public String getVehAttributes() {
		return vehAttributes;
	}

	public void setVehAttributes(String vehAttributes) {
		this.vehAttributes = vehAttributes;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}


	public String getEventDTM() {
		return eventDTM;
	}


	public void setEventDTM(String eventDTM) {
		this.eventDTM = eventDTM;
	}


	public int getStopEnumeration() {
		return stopEnumeration;
	}


	public void setStopEnumeration(int stopEnumeration) {
		this.stopEnumeration = stopEnumeration;
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
