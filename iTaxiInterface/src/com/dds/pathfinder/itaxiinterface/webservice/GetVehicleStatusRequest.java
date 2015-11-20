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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GetVehicleStatusRequest.java $
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
@XmlType(name = "GetVehicleStatusRequestType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", "taxiCoID", "vehicleNumber"})
public class GetVehicleStatusRequest extends BaseReq {
	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;
	
	@XmlElement(name="taxi_company_id", required = true )
	private Integer taxiCoID;
	
	@XmlElement(name="vehicle_number",  required = true)
	private String vehicleNumber;

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

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	
	

}
