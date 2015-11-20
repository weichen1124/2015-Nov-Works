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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/deviceevents/OutVehicleProperties.java $
* 
* PF-16605, 06/18/15, DChen, created for 2nd device.
* 
* **************************************/

package com.dds.pathfinder.pfrest.deviceevents;

import com.dds.pathfinder.pfrest.events.EventProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OutVehicleProperties extends EventProperties {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "driver-badge-nr")
	private String badgeNumber;
	
	@JsonProperty(value = "2nd-device-id")
	private String deviceID;

	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	
	

}
