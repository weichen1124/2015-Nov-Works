/****************************************************************************
 *
 *		   		    Copyright (c), 2014
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/tss/TSSRequireVehReq.java $
 * 
 * 08/25/14, DChen, create TSS project.
 * 
 */

package com.dds.pathfinder.itaxiinterface.tss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSSRequireVehReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = {
						  "taxiCoID",
						  "tssID",
						  "pickupTime",
						  "pickupStreetNr",
						  "pickupStreetName",
						  "pickupRegion",
						  "pickupUnit",
						  "pickupPostCode",
						  "pickupLandmark",
						  "pickupLon",
						  "pickupLat",
						  "attributes",
						  "driverNotes"})
public class TSSRequireVehReq {
	
	@XmlElement(name="company_id", required = true )
	private Integer taxiCoID;
	
	@XmlElement(name="tss_id", required = true)
	private Integer tssID;
	
	@XmlElement(name="pickup_house_number",   required = false)
	private String pickupStreetNr;
	
	@XmlElement(name="pickup_time",   required = false)
	private String pickupTime;
	
	@XmlElement(name="pickup_street_name",   required = false)
	private String pickupStreetName;
	
	@XmlElement(name="pickup_unit",   required = false)
	private String pickupUnit;
	
	@XmlElement(name="pickup_district",  required = false)
	private String pickupRegion;
	
	@XmlElement(name="pickup_landmark",  required = false)
	private String pickupLandmark;
	
	@XmlElement(name="pickup_longitude", required = true)
	private Double pickupLon;
	
	@XmlElement(name="pickup_latitude",  required = true)
	private Double pickupLat;
	
	@XmlElement(name="attributes",  required = false)
	private String attributes;
	
	@XmlElement(name="driver_notes",   required = false)
	private String driverNotes;
	
	@XmlElement(name="pickup_postcode",   required = false)
	private String pickupPostCode;

	public Integer getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(Integer taxiCoID) {
		this.taxiCoID = taxiCoID;
	}

	public Integer getTssID() {
		return tssID;
	}

	public void setTssID(Integer tssID) {
		this.tssID = tssID;
	}

	public String getPickupStreetNr() {
		return pickupStreetNr;
	}

	public void setPickupStreetNr(String pickupStreetNr) {
		this.pickupStreetNr = pickupStreetNr;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getPickupStreetName() {
		return pickupStreetName;
	}

	public void setPickupStreetName(String pickupStreetName) {
		this.pickupStreetName = pickupStreetName;
	}

	public String getPickupUnit() {
		return pickupUnit;
	}

	public void setPickupUnit(String pickupUnit) {
		this.pickupUnit = pickupUnit;
	}

	public String getPickupRegion() {
		return pickupRegion;
	}

	public void setPickupRegion(String pickupRegion) {
		this.pickupRegion = pickupRegion;
	}

	public String getPickupLandmark() {
		return pickupLandmark;
	}

	public void setPickupLandmark(String pickupLandmark) {
		this.pickupLandmark = pickupLandmark;
	}

	public Double getPickupLon() {
		return pickupLon;
	}

	public void setPickupLon(Double pickupLon) {
		this.pickupLon = pickupLon;
	}

	public Double getPickupLat() {
		return pickupLat;
	}

	public void setPickupLat(Double pickupLat) {
		this.pickupLat = pickupLat;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getDriverNotes() {
		return driverNotes;
	}

	public void setDriverNotes(String driverNotes) {
		this.driverNotes = driverNotes;
	}

	public String getPickupPostCode() {
		return pickupPostCode;
	}

	public void setPickupPostCode(String pickupPostCode) {
		this.pickupPostCode = pickupPostCode;
	}
	
}
