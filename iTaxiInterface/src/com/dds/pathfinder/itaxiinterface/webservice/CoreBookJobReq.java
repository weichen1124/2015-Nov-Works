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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/CoreBookJobReq.java $
 * 
 * 9     3/07/14 2:04p Ezhang
 * PF-15917 added default values to passengerNr fields.
 * 
 * 8     4/23/12 4:36p Ajiang
 * PF-14393 - added default values to optional request fields
 * 
 * 7     10/05/10 12:20p Ezhang
 * change ride_id from required to optional
 * 
 * 6     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 5     9/17/09 3:40p Dchen
 * pathfinder iTaxi interface.
 * 
 * 4     8/14/09 4:21p Dchen
 * pathfinder iTaxi interface.
 * 
 * 3     8/14/09 2:28p Dchen
 * Renamed BookJobReq.java to CoreBookJobReq.java
 * 
 * 2     8/13/09 5:47p Dchen
 * pathfinder iTaxi interface.
 *com.dds.pathfinder.itaxiinterface.webserviceder iTaxi interface.
 * 
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CoreBookJobReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "requestType", 
						  "rideID",
						  "taxiCoID",
						  "taxiRideID",
						  "telephoneNr",
						  "telephoneExt",
						  "pickupStreetNr",
						  "pickupStreetName",
						  "pickupRegion",
						  "pickupUnit",
						  "pickupLandmark",
						  "pickupLon",
						  "pickupLat",
						  "dropoffStreetNr",
						  "dropoffStreetName",
						  "dropoffRegion",
						  "dropoffUnit",
						  "dropoffLandmark",
						  "dropoffLon",
						  "dropoffLat",
						  "passangerName",
						  "passangerNr",
						  "remarks",
						  "pickupTime"})
public class CoreBookJobReq extends BaseReq{
	
	@XmlElement(name="request_type", required = true)
	private Integer requestType;
	
	@XmlElement(name="taxi_company_id", required = true )
	private Integer taxiCoID;
	
	@XmlElement(name="taxi_ride_id", required = false, defaultValue = "0")
	private Long taxiRideID;
	
	@XmlElement(name="ride_id",  required = false)
	private String rideID;
	
	@XmlElement(name="phonenum",   required = false)
	private String telephoneNr;

	@XmlElement(name="phoneext",   required = false)
	private String telephoneExt;

	@XmlElement(name="pickup_house_number",   required = false)
	private String pickupStreetNr;
	
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
	
	@XmlElement(name="dropoff_house_number",   required = false)
	private String dropoffStreetNr;
	
	@XmlElement(name="dropoff_street_name",   required = false)
	private String dropoffStreetName;
	
	@XmlElement(name="dropoff_district",   required = false)
	private String dropoffRegion;
	
	@XmlElement(name="dropoff_landmark",   required = false)
	private String dropoffLandmark;
	
	@XmlElement(name="dropoff_unit",   required = false)
	private String dropoffUnit;
	
	@XmlElement(name="dropoff_longitude",  required = false, defaultValue = "182")
	private Double dropoffLon;
	
	@XmlElement(name="dropoff_latitude",   required = false, defaultValue = "182")
	private Double dropoffLat;
	
	@XmlElement(name="passenger_name",   required = false)
	private String passangerName;
	
	@XmlElement(name="number_of_passenger",   required = false, defaultValue = "1")
	private Integer passangerNr;
	
	@XmlElement(name="remarks",   required = false)
	private String remarks;
	
	@XmlElement(name="pickup_time",   required = false)
	private String pickupTime;
		

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}


	public Integer getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(Integer taxiCoID) {
		this.taxiCoID = taxiCoID;
	}

	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}

	public String getRideID() {
		return rideID;
	}

	public void setRideID(String rideID) {
		this.rideID = rideID;
	}

	public String getTelephoneNr() {
		return telephoneNr;
	}

	public void setTelephoneNr(String telephoneNr) {
		this.telephoneNr = telephoneNr;
	}

	public String getTelephoneExt() {
		return telephoneExt;
	}

	public void setTelephoneExt(String telephoneExt) {
		this.telephoneExt = telephoneExt;
	}

	public String getPickupStreetNr() {
		return pickupStreetNr;
	}

	public void setPickupStreetNr(String pickupStreetNr) {
		this.pickupStreetNr = pickupStreetNr;
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

	public String getDropoffStreetNr() {
		return dropoffStreetNr;
	}

	public void setDropoffStreetNr(String dropoffStreetNr) {
		this.dropoffStreetNr = dropoffStreetNr;
	}

	public String getDropoffStreetName() {
		return dropoffStreetName;
	}

	public void setDropoffStreetName(String dropoffStreetName) {
		this.dropoffStreetName = dropoffStreetName;
	}

	public String getDropoffRegion() {
		return dropoffRegion;
	}

	public void setDropoffRegion(String dropoffRegion) {
		this.dropoffRegion = dropoffRegion;
	}

	public String getDropoffLandmark() {
		return dropoffLandmark;
	}

	public void setDropoffLandmark(String dropoffLandmark) {
		this.dropoffLandmark = dropoffLandmark;
	}

	public String getDropoffUnit() {
		return dropoffUnit;
	}

	public void setDropoffUnit(String dropoffUnit) {
		this.dropoffUnit = dropoffUnit;
	}

	public Double getDropoffLon() {
		return dropoffLon;
	}

	public void setDropoffLon(Double dropoffLon) {
		this.dropoffLon = dropoffLon;
	}

	public Double getDropoffLat() {
		return dropoffLat;
	}

	public void setDropoffLat(Double dropoffLat) {
		this.dropoffLat = dropoffLat;
	}

	public String getPassangerName() {
		return passangerName;
	}

	public void setPassangerName(String passangerName) {
		this.passangerName = passangerName;
	}

	public Integer getPassangerNr() {
		if(passangerNr == null ) {
			passangerNr = 1;
		}
		return passangerNr;
	}

	public void setPassangerNr(Integer passangerNr) {
		this.passangerNr = passangerNr;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}
	
	
	
}
