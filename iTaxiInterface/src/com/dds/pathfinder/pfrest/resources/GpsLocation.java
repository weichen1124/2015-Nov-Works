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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/GpsLocation.java $
 *
 * PF-16385, 10/08/15, DZhou, add vehicle location request
 * 
 * **************************************/
package com.dds.pathfinder.pfrest.resources;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GpsLocation implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "longitude")
	private Double longitude;
	
	@JsonProperty(value = "latitude")
	private Double latitude;
	
	//	time gps was retrieved, if it has it
	@JsonProperty(value = "dateTime")
	private String dateTime;
	
	public GpsLocation(){
		super();					
	}	

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
