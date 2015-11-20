/****************************************************************************
 *
 *		   		    Copyright (c), 2012
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GetVehicleInventoryRequest.java $
 * 
 * 1     11/27/12 1:13p Dchen
 * PF-14930, Veolia osp changes.
 * 
 * 
 * 
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetVehicleInventoryReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", "taxiCoID", "searchType", "pickupZoneNumber", "dropoffZoneNumber",
						"regionLTopLatitude","regionLTopLongitude","regionRBotLatitude","regionRBotLongitude"})
public class GetVehicleInventoryRequest extends BaseReq {
	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;
	
	@XmlElement(name="taxi_company_id", required = true )
	private Integer taxiCoID;
	
	@XmlElement(name="search_type",  required = true)
	private String searchType;
	
	@XmlElement(name="pickup_zone_number", required = false)
	private String pickupZoneNumber;
	
	@XmlElement(name="dropoff_zone_number", required = false)
	private String dropoffZoneNumber;
	
	@XmlElement(name="left_top_latitude", required = false, defaultValue = "182")
	private Double regionLTopLatitude;
	
	@XmlElement(name="left_top_longitude", required = false, defaultValue = "182")
	private Double regionLTopLongitude;
	
	@XmlElement(name="right_bot_latitude", required = false, defaultValue = "182")
	private Double regionRBotLatitude;
	
	@XmlElement(name="right_bot_longitude", required = false, defaultValue = "182")
	private Double regionRBotLongitude;

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

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getPickupZoneNumber() {
		return pickupZoneNumber;
	}

	public void setPickupZoneNumber(String pickupZoneNumber) {
		this.pickupZoneNumber = pickupZoneNumber;
	}

	public String getDropoffZoneNumber() {
		return dropoffZoneNumber;
	}

	public void setDropoffZoneNumber(String dropoffZoneNumber) {
		this.dropoffZoneNumber = dropoffZoneNumber;
	}

	public Double getRegionLTopLatitude() {
		return regionLTopLatitude;
	}

	public void setRegionLTopLatitude(Double regionLTopLatitude) {
		this.regionLTopLatitude = regionLTopLatitude;
	}

	public Double getRegionLTopLongitude() {
		return regionLTopLongitude;
	}

	public void setRegionLTopLongitude(Double regionLTopLongitude) {
		this.regionLTopLongitude = regionLTopLongitude;
	}

	public Double getRegionRBotLatitude() {
		return regionRBotLatitude;
	}

	public void setRegionRBotLatitude(Double regionRBotLatitude) {
		this.regionRBotLatitude = regionRBotLatitude;
	}

	public Double getRegionRBotLongitude() {
		return regionRBotLongitude;
	}

	public void setRegionRBotLongitude(Double regionRBotLongitude) {
		this.regionRBotLongitude = regionRBotLongitude;
	}

}
