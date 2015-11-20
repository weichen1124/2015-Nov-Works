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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GetUDICalloutRequest.java $
 * 
 * 2     1/27/14 5:14p Dchen
 * attempts count typo.
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
@XmlType(name = "GetUDICalloutReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "taxiCoID", "calloutType", "staleTripPassedMin"})
public class GetUDICalloutRequest extends BaseReq {
	
	@XmlElement(name="taxi_company_id", required = true )
	private Integer taxiCoID;
	
	@XmlElement(name="callout_type", required = true)
	private Integer calloutType;
	
	@XmlElement(name="stale_trip_minutes", required = false, defaultValue = "30")
	private Integer staleTripPassedMin;

	public Integer getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(Integer taxiCoID) {
		this.taxiCoID = taxiCoID;
	}

	public Integer getCalloutType() {
		return calloutType;
	}

	public void setCalloutType(Integer calloutType) {
		this.calloutType = calloutType;
	}

	public Integer getStaleTripPassedMin() {
		return staleTripPassedMin;
	}

	public void setStaleTripPassedMin(Integer staleTripPassedMin) {
		this.staleTripPassedMin = staleTripPassedMin;
	}

}
