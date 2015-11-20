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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/AcctAddrListItem.java $
 * 
 * 3     1/22/10 4:52p Dchen
 * OSP interface.
 * 
 * 2     11/16/09 11:13a Jwong
 * 
 * 1     11/04/09 3:26p Jwong
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
@XmlType(name = "AcctAddrListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { 
		  "pickupStreetNumber",
		  "pickupStreetName",
		  "pickupRegion",
		  "pickupUnitNumber",
		  "dropoffStreetNumber",
		  "dropoffStreetName",
		  "dropoffRegion",
		  "dropoffUnitNumber"})
public class AcctAddrListItem {

	@XmlElement(name="pickup_house_number", required = false)
	private String pickupStreetNumber;

	@XmlElement(name="pickup_street_name", required = false)
	private String pickupStreetName;
	
	@XmlElement(name="pickup_district", required = false)
	private String pickupRegion;
	
	@XmlElement(name="pickup_unit", required = false)
	private String pickupUnitNumber;		
	
	@XmlElement(name="dropoff_house_number", required = false)
	private String dropoffStreetNumber;

	@XmlElement(name="dropoff_street_name", required = false)
	private String dropoffStreetName;
	
	@XmlElement(name="dropoff_district", required = false)
	private String dropoffRegion;
	
	@XmlElement(name="dropoff_unit", required = false)
	private String dropoffUnitNumber;	

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
	
	public String getPickupRegion() {
		return pickupRegion;
	}

	public void setPickupRegion(String pickupRegion) {
		this.pickupRegion = pickupRegion;
	}
	
	public String getPickupUnitNumber() {
		return pickupUnitNumber;
	}

	public void setPickupUnitNumber(String pickupUnitNumber) {
		this.pickupUnitNumber = pickupUnitNumber;
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
	
	public String getDropoffRegion() {
		return dropoffRegion;
	}

	public void setDropoffRegion(String dropoffRegion) {
		this.dropoffRegion = dropoffRegion;
	}
	
	public String getDropoffUnitNumber() {
		return dropoffUnitNumber;
	}

	public void setDropoffUnitNumber(String dropoffUnitNumber) {
		this.dropoffUnitNumber = dropoffUnitNumber;
	}
}
