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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/CancelJobListItem.java $
 * 
 * 1     9/20/10 11:28a Ezhang
 * OSP 2.0
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CancelJobListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "taxiRideID","tripCancelStatus"})
public class CancelJobListItem {
	@XmlElement(name="taxi_ride_id",   required = true)
	private Long taxiRideID;
	
	@XmlElement(name="tripCancelStatus", required = true)
	private Integer tripCancelStatus;
	
	public CancelJobListItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CancelJobListItem(Long taxiRideID, Integer tripCancelStatus){
		super();
		this.taxiRideID= taxiRideID;
		this.tripCancelStatus = tripCancelStatus;
	}
	
	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}

	public Integer getTripCancelStatus() {
		return tripCancelStatus;
	}

	public void setTripCancelStatus(Integer tripCancelStatus) {
		this.tripCancelStatus = tripCancelStatus;
	}
	
	
}
