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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/UDICalloutListItem.java $
 * 
 * 1     1/16/14 3:44p Dchen
 * PF-15847, UDI callout extensions.
 * 
 * 
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;

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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/UDICalloutListItem.java $
 * 
 * 1     1/16/14 3:44p Dchen
 * PF-15847, UDI callout extensions.
* 
* 
*****/
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UDICalloutListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "calloutType","taxiRideID","calloutID","phoneNumber",
							"emailAddress", "contactType"})
public class UDICalloutListItem {
	
	@XmlElement(name="callout_type", required = true)
	private Integer calloutType;
	
	@XmlElement(name="taxi_ride_ID", required = true)
	private Long taxiRideID;
	
	@XmlElement(name="udi_callout_ID", required = false)
	private Long calloutID;
	
	@XmlElement(name="phone_number", required = false)
	private String phoneNumber;
	
	@XmlElement(name="email", required = false)
	private String emailAddress;
	
	@XmlElement(name="contact_type", required = false)
	private String contactType;

	 
	
	public UDICalloutListItem() {
		super();
	}

	public UDICalloutListItem(Long taxiRideID, Integer calloutType ) {
		super();
		this.taxiRideID = taxiRideID;
		this.calloutType = calloutType;
	}

	public Integer getCalloutType() {
		return calloutType;
	}

	public void setCalloutType(Integer calloutType) {
		this.calloutType = calloutType;
	}

	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}

	public Long getCalloutID() {
		return calloutID;
	}

	public void setCalloutID(Long calloutID) {
		this.calloutID = calloutID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	

}
