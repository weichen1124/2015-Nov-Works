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
 * Created for Mobile Application Customer to Rate Trip Experience.
 * 
 */
package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RateTripReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", "taxiRideID" , "generalRate", "carRate", "driverRate", "appRate", "comments"})

public class RateTripReq extends BaseReq {
	
	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;
	
	@XmlElement(name="taxi_ride_id", required = true)
	private Long taxiRideID;
	
	@XmlElement(name="generalRate", required = false)
	private int generalRate;
	
	@XmlElement(name="carRate", required = false)
	private int carRate;
	
	@XmlElement(name="driverRate", required = false)
	private int driverRate;
	
	@XmlElement(name="appRate", required = false)
	private int appRate;
	
	@XmlElement(name="comments", required = false)
	private String comments;

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}

	public int getGeneralRate() {
		return generalRate;
	}

	public void setGeneralRate(int generalRate) {
		this.generalRate = generalRate;
	}

	public int getCarRate() {
		return carRate;
	}

	public void setCarRate(int carRate) {
		this.carRate = carRate;
	}

	public int getDriverRate() {
		return driverRate;
	}

	public void setDriverRate(int driverRate) {
		this.driverRate = driverRate;
	}

	public int getAppRate() {
		return appRate;
	}

	public void setAppRate(int appRate) {
		this.appRate = appRate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	

}
