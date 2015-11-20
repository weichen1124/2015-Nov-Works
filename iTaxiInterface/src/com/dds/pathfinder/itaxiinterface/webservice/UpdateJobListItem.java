/****************************************************************************
 *
 *		   		    Copyright (c), 2010
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/UpdateJobListItem.java $
 * 
 * 2     11/10/10 9:44a Ezhang
 * rename the method from setTripCancelStatus() to setTripUpdateStatus()
 * 
 * 1     9/20/10 11:17a Ezhang
 *
 * for OSP 2.0
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateJobListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "taxiRideID","tripUpdateStatus"})
public class UpdateJobListItem {
	@XmlElement(name="taxi_ride_id",   required = true)
	private Long taxiRideID;
	
	@XmlElement(name="tripUpdateStatus", required = true)
	private Integer tripUpdateStatus;
	
	public UpdateJobListItem(){
		super();
	}
	public UpdateJobListItem(Long taxiRideID, Integer tripUpdateStatus){
		super();
		this.taxiRideID= taxiRideID;
		this.tripUpdateStatus = tripUpdateStatus;
	}
	
	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}

	public Integer getTripUpdateStatus() {
		return tripUpdateStatus;
	}

	public void setTripUpdateStatus(Integer tripUpdateStatus) {
		this.tripUpdateStatus = tripUpdateStatus;
	}
	
	
}
