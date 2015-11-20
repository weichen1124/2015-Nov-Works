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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/seconddevices/SecondDevice.java $
* 
* PF-16605, 06/18/15, DChen, created for 2nd device.
* 
* **************************************/
package com.dds.pathfinder.pfrest.seconddevices;

import com.dds.pathfinder.pfrest.resources.PFResourceData;
import com.dds.pathfinder.pfrest.resources.PFResourceData.PFDataType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SecondDevice extends PFResourceData {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "driver-badge-nr")
	private String badgeNumber;
	
	
	@JsonProperty(value = "vehicle-call-sign")
	private String callSign;
	
	@JsonProperty(value = "driver-pin")
	private String driverPin;

	

	public SecondDevice() {
		super();
		setDataType(PFDataType.PFData_Type_2ndDevice.getDataType());
	}


	public SecondDevice(String badgeNumber, String callSign, String driverPin) {
		super();
		this.badgeNumber = badgeNumber;
		this.callSign = callSign;
		this.driverPin = driverPin;
	}


	public String getBadgeNumber() {
		return badgeNumber;
	}


	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}


	public String getCallSign() {
		return callSign;
	}


	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}


	public String getDriverPin() {
		return driverPin;
	}


	public void setDriverPin(String driverPin) {
		this.driverPin = driverPin;
	}

}
