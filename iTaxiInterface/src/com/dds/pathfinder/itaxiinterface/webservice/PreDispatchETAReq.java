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
 * Aug 19, 2015 Yutian Yin
 * PF-16698. Reuse preDispatchETA for zoro. Added 'useStatistics' for zoro. When it's 
 * missing or set to 'N', the PreDispatchETA would work exactly the same way
 * as before.
 *
 * PF-16496, 05/27/15, DChen, add pre dispatch eta.
 * 
 */
package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PreDispatchETAReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", "taxiCoID", "latitude", "longitude", "radius", "useStatistics"})
public class PreDispatchETAReq extends BaseReq{
	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;
	
	@XmlElement(name="taxi_company_id", required = true)
	private int taxiCoID;
	
	@XmlElement(name="latitude", required = true)
	private double latitude;
	
	@XmlElement(name="longitude", required = true)
	private double longitude;
	
	@XmlElement(name="radius_in_meter", required = false, defaultValue="2000")
	private int radius;
	
	@XmlElement(name="useStatistics", required = false, defaultValue="N")
	private String useStatistics;
	

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public int getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(int taxiCoID) {
		this.taxiCoID = taxiCoID;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public String getUseStatistics() {
		return useStatistics;
	}

	public void setUseStatistics(String useStatistics) {
		this.useStatistics = useStatistics;
	}

}
