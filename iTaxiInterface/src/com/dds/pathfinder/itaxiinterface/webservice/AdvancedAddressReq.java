/****************************************************************************
 *
 *		   		    Copyright (c), 2010
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/AdvancedAddressReq.java $
 * 
 * 3     4/23/12 4:35p Ajiang
 * PF-14393 - added default values to optional request fields
 * 
 * 2     9/24/10 1:31p Ezhang
 * added district field
 * 
 * 1     9/20/10 11:29a Ezhang
 * 
 * OSP 2.0
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdvancedAddressReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", "taxiCoID", "streetNr","streetName", "unit", "district",
							"business","state","country"})
public class AdvancedAddressReq extends BaseReq{
	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;
	
	@XmlElement(name="taxi_co_id", required = false, defaultValue = "0")
	private Integer taxiCoID;
	
	@XmlElement(name="house_number", required = false)
	private String streetNr;
	
	@XmlElement(name="street_name", required = true)
	private String streetName;
	
	@XmlElement(name="unit", required = false)
	private String unit;
	
	@XmlElement(name="district", required = false)
	private String district;
	
	@XmlElement(name="business", required = true)
	private String business;
	
		
	@XmlElement(name="state", required = false)
	private String state;
	
	@XmlElement(name="country", required = false)
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

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
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

	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}
	
	
}
