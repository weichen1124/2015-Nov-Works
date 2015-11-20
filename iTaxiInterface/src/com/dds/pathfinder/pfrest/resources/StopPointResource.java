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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/StopPointResource.java $
* 
* PF-16615, 06/23/15, DChen, add operator notes in stop point.
* 
* PF-16614, 06/19/15, DChen, add suti particular stop points resource.
* 
* PF-16580, 06/03/15, DChen, add customer number in stop points.
* 
* PF-16568, 05/15/15, DChen, add node cancellation in PF side.
* 
* PF-16428, 03/06/15, DChen, added notes in job resource.
* 
* PF-16385, 02/13/15, DChen, create pfrest project.
* 
* **************************************/
package com.dds.pathfinder.pfrest.resources;

import java.io.Serializable;
import java.util.Date;

import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StopPointResource implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value="id")
	int stopPointID;

	@JsonProperty(value="pickup-address")
	Address pickupAddress;
	
	@JsonProperty(value="dropoff-address")
	Address dropoffAddress;
	
	@JsonProperty(value="passenger")
	Passenger passenger;
	
	@JsonProperty(value="driver-notes")
	String driverNotes;
	
	@JsonProperty(value="operator-notes")
	String operatorNotes;
	
	@JsonProperty(value="customer-number")
	String customerNumber;
	
	@JsonProperty(value="required-dtm")
	String requiredDTM;
	
	@JsonProperty(value="is-cancelled")
	String cancelledFlag;
	
	 
	
	
//	@GET
//	@Produces("application/json")
//	public StopPointResource get(){
//		return this;
//	}
	
	public StopPointResource(){
		pickupAddress = new Address();
		dropoffAddress = new Address();
		passenger = new Passenger();
		cancelledFlag = "N";
	}

	
	
	public int getStopPointID() {
		return stopPointID;
	}

	public void setStopPointID(int stopPointID) {
		this.stopPointID = stopPointID;
	}

	public Address getPickupAddress() {
		return pickupAddress;
	}

	public void setPickupAddress(Address pickupAddress) {
		this.pickupAddress = pickupAddress;
	}

	public Address getDropoffAddress() {
		return dropoffAddress;
	}

	public void setDropoffAddress(Address dropoffAddress) {
		this.dropoffAddress = dropoffAddress;
	}

	public Passenger getPassenger() {
		return passenger;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}



	public String getDriverNotes() {
		return driverNotes;
	}

	public void setDriverNotes(String driverNotes) {
		this.driverNotes = driverNotes;
	}


	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getCancelledFlag() {
		return cancelledFlag;
	}

	public void setCancelledFlag(String cancelledFlag) {
		this.cancelledFlag = cancelledFlag;
	}


	public String getOperatorNotes() {
		return operatorNotes;
	}

	public void setOperatorNotes(String operatorNotes) {
		this.operatorNotes = operatorNotes;
	}

	public String getRequiredDTM() {
		return requiredDTM;
	}

	public void setRequiredDTM(String requiredDTM) {
		this.requiredDTM = requiredDTM;
	}
	
	public void setRequiredDTMinDate(Date requiredDTM) {
		this.requiredDTM = Utilities.formatOSPDefaultDate(requiredDTM);
	}

}
