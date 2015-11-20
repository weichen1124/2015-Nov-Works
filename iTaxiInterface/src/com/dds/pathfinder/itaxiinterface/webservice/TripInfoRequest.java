/****************************************************************************
 *
 *                            Copyright (c), 2009
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 * File Name: TripInfoRequest.java
 *
 * $Log $
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TripInfoReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID",
						  "taxiCoID", 
						  "phoneNumber",
						  "passengerName",
						  "pickupStreetNumber",
						  "pickupStreetName",
						  "pickupUnitNumber",
						  "pickupRegion",
						  "pickupLandmark",
						  "accountCode",
						  "dropoffStreetNumber",
						  "dropoffStreetName",
						  "dropoffUnitNumber",
						  "dropoffRegion",
						  "dropoffLandmark",
						  "taxiRideID"
						})
public class TripInfoRequest extends BaseReq{
	@XmlElement(name="sessionID", required = false)
	private String sessionID;

	@XmlElement(name="taxi_company_id", required = true)
	private Integer taxiCoID;

	@XmlElement(name="phoneNumber", required = false)
	private String phoneNumber;

	@XmlElement(name="passengerName", required = false)
	private String passengerName;
	
	@XmlElement(name="pickupStreetNumber", required = false)
	private String pickupStreetNumber;
	
	@XmlElement(name="pickupStreetName", required = false)
	private String pickupStreetName;
	
	@XmlElement(name="pickupUnitNumber", required = false)
	private String pickupUnitNumber;
	
	@XmlElement(name="pickupRegion",  required = false)
	private String pickupRegion;
	
	@XmlElement(name="pickupLandmark", required = false)
	private String pickupLandmark;
	
	@XmlElement(name="accountCode", required = false)
	private String accountCode;
	
	@XmlElement(name="dropoffStreetNumber", required = false)
	private String dropoffStreetNumber;
	
	@XmlElement(name="dropoffStreetName", required = false)
	private String dropoffStreetName;
	
	@XmlElement(name="dropoffUnitNumber", required = false)
	private String dropoffUnitNumber;
	
	@XmlElement(name="dropoffRegion",  required = false)
	private String dropoffRegion;
	
	@XmlElement(name="dropoffLandmark", required = false)
	private String dropoffLandmark;
	
	@XmlElement(name="taxi_ride_id", required = false, defaultValue = "0")
	private Long taxiRideID;
	
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}
	
	public String getPickupStreetNumber() {
		return pickupStreetNumber;
	}

	public void setPickupStreetNumber(String pickupStreetNumber) {
		this.pickupStreetNumber = pickupStreetNumber;
	}
	
	public String getPickupStreetName() {
		return pickupStreetName;
	}

	public void setPickupStreetName(String pickupStreetName) {
		this.pickupStreetName = pickupStreetName;
	}
	
	public String getPickupUnitNumber() {
		return pickupUnitNumber;
	}

	public void setPickupUnitNumber(String pickupUnitNumber) {
		this.pickupUnitNumber = pickupUnitNumber;
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
	
	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	
	public String getDropoffStreetNumber() {
		return dropoffStreetNumber;
	}

	public void setDropoffStreetNumber(String dropoffStreetNumber) {
		this.dropoffStreetNumber = dropoffStreetNumber;
	}
	
	public String getDropoffStreetName() {
		return dropoffStreetName;
	}

	public void setDropoffStreetName(String dropoffStreetName) {
		this.dropoffStreetName = dropoffStreetName;
	}
	
	public String getDropoffUnitNumber() {
		return dropoffUnitNumber;
	}

	public void setDropoffUnitNumber(String dropoffUnitNumber) {
		this.dropoffUnitNumber = dropoffUnitNumber;
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
	
	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}

}