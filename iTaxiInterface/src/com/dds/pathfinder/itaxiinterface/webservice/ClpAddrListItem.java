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
 * File Name: ClpAddrListItem.java
 *
 * $Log $
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClpAddrListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "passengerName", "streetNr", "streetName", "unit", "landmark", "district", "remarks", "attributeList" })
public class ClpAddrListItem {
	
	@XmlElement(name="passengerName", required = true)
	private String passengerName;	
	
	@XmlElement(name="pickupStreetNumber", required = true)
	private String streetNr;
	
	@XmlElement(name="pickupStreetName", required = true)
	private String streetName;
	
	@XmlElement(name="pickUnitNumber", required = true)
	private String unit;
	
	@XmlElement(name="pickupLandmark", required = true)
	private String landmark;
	
	@XmlElement(name="pickupRegion", required = true)
	private String district;
	
	@XmlElement(name="remarks", required = true)
	private String remarks;

	@XmlElement(name="attributeList", required = true)
	private Attribute[] attributeList;

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}
	
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

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public Attribute[] getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(Attribute[] attributeList) {
		this.attributeList = attributeList;
	}
	
}
