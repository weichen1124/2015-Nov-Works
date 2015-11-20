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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/Address.java $
* 
* 
* PF-16385, 02/13/15, DChen, create pfrest project.
* 
* **************************************/
package com.dds.pathfinder.pfrest.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "street-nr")
	private String streetNumber;

	@JsonProperty(value = "street-name")
	private String streetName;
	
	@JsonProperty(value = "region")
	private String region;
	
	@JsonProperty(value = "unit-nr")
	private String unitNumber;	
	
	@JsonProperty(value = "location")
	private String landmark;	
	
	@JsonProperty(value = "organization")
	private String organization;
	
	@JsonProperty(value = "zone-number")
	private String areaName;
	
	@JsonProperty(value = "longitude")
	private Double longitude;
	
	@JsonProperty(value = "latitude")
	private Double latitude;
	
	@JsonProperty(value = "forced-address-flag")
	private String forcedAddrFlag;

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
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

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getForcedAddrFlag() {
		return forcedAddrFlag;
	}

	public void setForcedAddrFlag(String forcedAddrFlag) {
		this.forcedAddrFlag = forcedAddrFlag;
	}

}
