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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/UpdateUDICalloutReq.java $
 * 
 * 1     1/16/14 3:44p Dchen
 * PF-15847, UDI callout extensions.
 * 
 * 
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateUDICalloutReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "taxiCoID", "taxiRideID", "calloutID", "callNumber", "numberOfAttempt", "completedFlag", "disposition"})
public class UpdateUDICalloutReq extends BaseReq {
	
	@XmlElement(name="taxi_company_id", required = true )
	private Integer taxiCoID;
	
	@XmlElement(name="taxi_ride_ID", required = true)
	private Long taxiRideID;
	
	@XmlElement(name="udi_callout_ID", required = true)
	private Long calloutID;
	
	@XmlElement(name="call_number", required = false)
	private String callNumber;
	
	@XmlElement(name="num_of_attempt", required = false)
	private Integer numberOfAttempt;
	
	@XmlElement(name="complete_flag", required = false)
	private String completedFlag;
	
	@XmlElement(name="disposition", required = false)
	private String disposition;

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

	public Long getCalloutID() {
		return calloutID;
	}

	public void setCalloutID(Long calloutID) {
		this.calloutID = calloutID;
	}

	public String getCallNumber() {
		return callNumber;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	public Integer getNumberOfAttempt() {
		return numberOfAttempt;
	}

	public void setNumberOfAttempt(Integer numberOfAttempt) {
		this.numberOfAttempt = numberOfAttempt;
	}

	public String getCompletedFlag() {
		return completedFlag;
	}

	public void setCompletedFlag(String completedFlag) {
		this.completedFlag = completedFlag;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}
	
	
}
