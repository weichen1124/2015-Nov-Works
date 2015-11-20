/****************************************************************************
 *
 *                            Copyright (c), 2009
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/TripUpdateRequest.java $
 * 
 * 3     4/23/12 4:34p Ajiang
 * PF-14393 - added default values to optional request fields
 * 
 * 2     9/27/10 2:41p Ajiang
 * Fixed a type in requestType
 * 
 * 1     9/27/10 11:16a Ajiang
 * Created
 * 
 *
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TripUpdateReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "requestType", 
						  "taxiRideID", 
						  "rideID",
						  "longitude",
						  "latitude",
						  "eventTime"})
public class TripUpdateRequest {
	
	@XmlElement(name="request_type", required = true)
	private String requestType;
	
	@XmlElement(name="provider_ride_id", required = true )
	private Long taxiRideID;
	
	@XmlElement(name="ride_id", required = false, defaultValue = "0")
	private Long rideID;
	
	@XmlElement(name="longitude",  required = false, defaultValue = "182")
	private Double longitude;
	
	@XmlElement(name="latitude",   required = false, defaultValue = "182")
	private Double latitude;
	
	@XmlElement(name="event_time", required = false)
	private String eventTime;
	
	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
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
	
	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
}
