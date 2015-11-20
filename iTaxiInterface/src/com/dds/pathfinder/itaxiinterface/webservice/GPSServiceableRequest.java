/****************************************************************************
 *
 *		   		    Copyright (c), 2015
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GPSServiceableRequest.java $
 * 
 * PF-16398, 04/06/15, DChen, add isAddressServiceable service
 * 
 * 
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GPSServiceableRequestType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "taxiCoID", "latitude", "longitude"})
public class GPSServiceableRequest extends BaseReq {
	
	@XmlElement(name="taxi_company_id", required = true )
	private int taxiCoID;
	
	@XmlElement(name="longitude", required = true)
	private Double longitude;
	
	@XmlElement(name="latitude",  required = true)
	private Double latitude;

	public int getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(int taxiCoID) {
		this.taxiCoID = taxiCoID;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	
	
}
