/****************************************************************************
 *
 *		   		    Copyright (c), 2009
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/AddressListItem.java $
 * 
 * 4     12/07/10 4:09p Mkan
 * added organization for C34593
 * 
 * 3     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 2     9/25/09 5:43p Dchen
 * pathfinder iTaxi interface.
 * 
 * 1     8/13/09 5:46p Dchen
 * pathfinder iTaxi interface.
 * 
 * 
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "streetNr","streetName","district","unit",
							"landmark", "organization", "postCode","longitude","latitude"})
public class AddressListItem {
	@XmlElement(name="house_number", required = true)
	private String streetNr;
	
	@XmlElement(name="street_name", required = true)
	private String streetName;
	
	@XmlElement(name="district", required = true)
	private String district;
	
	@XmlElement(name="unit", required = true)
	private String unit;
	
	@XmlElement(name="landmark", required = true)
	private String landmark;
	
	@XmlElement(name="organization", required = false)
	private String organization;
	
	@XmlElement(name="post_code", required = true)
	private String postCode;
	
	@XmlElement(name="longitude", required = true)
	private Double longitude;
	
	@XmlElement(name="latitude", required = true)
	private Double latitude;

	public String getStreetNr() {
		return streetNr;
	}

	public void setStreetNr(String streetNr) {
		this.streetNr = streetNr;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
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
	
	
}
