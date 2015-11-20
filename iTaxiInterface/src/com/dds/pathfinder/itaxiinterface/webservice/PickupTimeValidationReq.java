/****************************************************************************
 *
 *                            Copyright (c), 2010
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/PickupTimeValidationReq.java $
 * 
 * 2     9/20/10 11:24a Ezhang
 * 
 * OSP 2.0
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PickupTimeValidationReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", "taxiCoID", "pickupTime"})
public class PickupTimeValidationReq extends BaseReq{
	@XmlElement(name="sessionID", required = false)
	private String sessionID;

	@XmlElement(name="taxi_company_id", required = true )
	private Integer taxiCoID;
	
	@XmlElement(name="pickup_time",   required = false)
	private String pickupTime;

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}
	
	public Integer getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(Integer taxiCoID) {
		this.taxiCoID = taxiCoID;
	}
}