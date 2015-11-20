/****************************************************************************
 *
 *                            Copyright (c), 2015
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 *
 * $Log:  $
 * 
 * Created for Mobile Application Customer to Rate Trip Experience (PF-16547).
 * 
 */
package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author ezhang
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RateTripResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "taxiRideID", "errorCode"})
public class RateTripRes extends BaseRes {
	
	@XmlElement(name="taxi_ride_id",   required = true)
	private Long taxiRideID;
	
	@XmlElement(name="errorCode",   required = false)
	private Integer errorCode;
	
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}
}
