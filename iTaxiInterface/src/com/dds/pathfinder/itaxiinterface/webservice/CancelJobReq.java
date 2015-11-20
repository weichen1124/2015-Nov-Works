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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/CancelJobReq.java $
 * 
 * 5     12/09/09 5:33p Yyin
 * Added sessionID
 * 
 * 4     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 3     9/25/09 5:43p Dchen
 * pathfinder iTaxi interface.
 * 
 * 2     8/14/09 4:21p Dchen
 * pathfinder iTaxi interface.
 * 
 * 1     8/13/09 5:47p Dchen
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
@XmlType(name = "CancelJobReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "requestType", "taxiRideID", "rideID", "sessionID"})
public class CancelJobReq extends BaseReq{
	@XmlElement(name="request_type", required = true)
	private Integer requestType;
		
	@XmlElement(name="taxi_ride_id", required = true)
	private Long taxiRideID;
	
	@XmlElement(name="ride_id", required = true)
	private Long rideID;

	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;
	
	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}

	public Long getRideID() {
		return rideID;
	}

	public void setRideID(Long rideID) {
		this.rideID = rideID;
	}
	
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}
