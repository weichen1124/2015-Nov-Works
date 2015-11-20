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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/AddressReq.java $
 * 
 * 5     4/23/12 4:34p Ajiang
 * PF-14393 - added default values to optional request fields
 * 
 * 4     11/18/09 3:01p Yyin
 * Added sessionID
 * 
 * 3     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 2     8/14/09 4:20p Dchen
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
@XmlType(name = "AddressReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", "taxiCoID", "streetNr","streetName","district","unit",
							"landmark","postCode","state","country"})
public class AddressReq extends BaseReq{
	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;
	
	@XmlElement(name="taxi_co_id", required = false, defaultValue = "0")
	private Integer taxiCoID;
	
	@XmlElement(name="house_number", required = false)
	private String streetNr;
	
	@XmlElement(name="street_name", required = false)
	private String streetName;
	
	@XmlElement(name="district", required = true)
	private String district;
	
	@XmlElement(name="unit", required = false)
	private String unit;
	
	@XmlElement(name="landmark", required = false)
	private String landmark;
	
	@XmlElement(name="post_code", required = false)
	private String postCode;
	
	@XmlElement(name="state", required = true)
	private String state;
	
	@XmlElement(name="country", required = true)
	private String country;
	
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	public Integer getTaxiCoID() {
		return taxiCoID;
	}
	public void setTaxiCoID(Integer taxiCoID) {
		this.taxiCoID = taxiCoID;
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
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	
}
