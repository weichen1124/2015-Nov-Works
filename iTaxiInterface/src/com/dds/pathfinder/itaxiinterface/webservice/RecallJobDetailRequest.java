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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/RecallJobDetailRequest.java $
 * 
 * 7     9/27/10 11:02a Ajiang
 * Set default osp version to 2.0
 * 
 * 6     9/20/10 1:57p Ezhang
 * OSP 2.0 added ospVersion.
 * 
 * 5     10/28/09 2:22p Yyin
 * C32549. Rename GetJobs and GetJobDetail to RecallJob and
 * RecallJobDetail.
 * 
 * 4     10/15/09 6:10p Yyin
 * 
 * 3     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 2     10/09/09 2:27p Yyin
 * 
 * 1     10/01/09 1:47p Yyin
 * Added
 * 
 * 1     8/13/09 5:47p Y Yin
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
@XmlType(name = "RecallJobDetailReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", "taxiCoID", "rideID", "taxiRideID", "ospVersion" })
public class RecallJobDetailRequest extends BaseReq{
	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;
	
	@XmlElement(name="taxi_company_id", required = true )
	private Integer taxiCoID;
	
	@XmlElement(name="ride_id",  required = true)
	private String rideID;
	
	@XmlElement(name="taxi_ride_id", required = true)
	private Long taxiRideID;
	
	@XmlElement(name="ospVersion", required = false, defaultValue="2.0")
	private String ospVersion;
	
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
	
	public String getRideID() {
		return rideID;
	}

	public void setRideID(String rideID) {
		this.rideID = rideID;
	}

	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}
	
	public String getOspVersion() {
		return ospVersion;
	}

	public void setOspVersion(String ospVersion) {
		this.ospVersion = ospVersion;
	}
}
