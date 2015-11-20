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
 * File Name: TripInfoResponse.java
 *
 * $Log $
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TripInfoResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "pickupZoneNumber",
					  "dropoffZoneNumber",
					  "tripPosition",
					  "nofTripsInBackupZones",
					  "nofCarsInPrimaryZones",
					  "nofCarsInBackupZones",
					  "minPickupTime",
					  "maxPickupTime",
					  "nofCarsAvailableBookin",
					  "nofCarsSoonToClear",
					  "nofCarsTempOff",
					  "svcAvgTime",
					  "svcAvgType",
					  "rateQuote"
})
public class TripInfoResponse extends BaseRes{
	
	@XmlElement(name="pickupZoneNumber",   required = false)
	private String pickupZoneNumber;

	@XmlElement(name="dropoffZoneNumber",   required = false)
	private String dropoffZoneNumber;
	
	@XmlElement(name="tripPosition",   required = false)
	private Integer tripPosition;
	
	@XmlElement(name="nofTripsInBackupZones",   required = false)
	private Integer nofTripsInBackupZones;
	
	@XmlElement(name="nofCarsInPrimaryZones",   required = false)
	private Integer nofCarsInPrimaryZones;
	
	@XmlElement(name="nofCarsInBackupZones",   required = false)
	private Integer nofCarsInBackupZones;
	
	@XmlElement(name="minPickupTime",   required = false)
	private Integer minPickupTime;
	
	@XmlElement(name="maxPickupTime",   required = false)
	private Integer maxPickupTime;
	
	@XmlElement(name="nofCarsAvailableBookin",   required = false)
	private Integer nofCarsAvailableBookin;
	
	@XmlElement(name="nofCarsSoonToClear",   required = false)
	private Integer nofCarsSoonToClear;
	
	@XmlElement(name="nofCarsTempOff",   required = false)
	private Integer nofCarsTempOff;
	
	@XmlElement(name="svcAvgTime",   required = false)
	private Integer svcAvgTime;
	
	@XmlElement(name="svcAvgType",   required = false)
	private Integer svcAvgType;
	
	@XmlElement(name="rateQuote",   required = false)
	private String rateQuote;

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
	
	public Integer getTripPosition() {
		return tripPosition;
	}
	
	public void setTripPosition(Integer tripPosition) {
		this.tripPosition = tripPosition;
	}
	
	public Integer getNofTripsInBackupZones() {
		return nofTripsInBackupZones;
	}
	
	public void setNofTripsInBackupZones(Integer nofTripsInBackupZones) {
		this.nofTripsInBackupZones = nofTripsInBackupZones;
	}
	
	public Integer getNofCarsInPrimaryZones() {
		return nofCarsInPrimaryZones;
	}
	
	public void setNofCarsInPrimaryZones(Integer nofCarsInPrimaryZones) {
		this.nofCarsInPrimaryZones = nofCarsInPrimaryZones;
	}
	
	public Integer getNofCarsInBackupZones() {
		return nofCarsInBackupZones;
	}
	
	public void setNofCarsInBackupZones(Integer nofCarsInBackupZones) {
		this.nofCarsInBackupZones = nofCarsInBackupZones;
	}
	
	public Integer getMinPickupTime() {
		return minPickupTime;
	}
	
	public void setMinPickupTime(Integer minPickupTime) {
		this.minPickupTime = minPickupTime;
	}
	
	public Integer getMaxPickupTime() {
		return maxPickupTime;
	}
	
	public void setMaxPickupTime(Integer maxPickupTime) {
		this.maxPickupTime = maxPickupTime;
	}
	
	public Integer getNofCarsAvailableBookin() {
		return nofCarsAvailableBookin;
	}
	
	public void setNofCarsAvailableBookin(Integer nofCarsAvailableBookin) {
		this.nofCarsAvailableBookin = nofCarsAvailableBookin;
	}

	public Integer getNofCarsSoonToClear() {
		return nofCarsSoonToClear;
	}
	
	public void setNofCarsSoonToClear(Integer nofCarsSoonToClear) {
		this.nofCarsSoonToClear = nofCarsSoonToClear;
	}

	public Integer getNofCarsTempOff() {
		return nofCarsTempOff;
	}
	
	public void setNofCarsTempOff(Integer nofCarsTempOff) {
		this.nofCarsTempOff = nofCarsTempOff;
	}
	
	public Integer getSvcAvgTime() {
		return svcAvgTime;
	}
	
	public void setSvcAvgTime(Integer svcAvgTime) {
		this.svcAvgTime = svcAvgTime;
	}
	
	public Integer getSvcAvgType() {
		return svcAvgType;
	}
	
	public void setSvcAvgType(Integer svcAvgType) {
		this.svcAvgType = svcAvgType;
	}
	
	public String getRateQuote() {
		return rateQuote;
	}
	
	public void setRateQuote(String rateQuote) {
		this.rateQuote = rateQuote;
	}
}
