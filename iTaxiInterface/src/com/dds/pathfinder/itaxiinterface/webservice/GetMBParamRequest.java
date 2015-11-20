/****************************************************************************
 *
 *                            Copyright (c), 2013
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GetMBParamRequest.java $
 * 
 * 1     10/28/13 11:24a Sfoladian
 * 
 * PF-15595 - Mobile Booker Parameter for Mobile Booker V2.0
 * 
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * @author sfoladian
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetMBParamReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", "taxiCoID"})
public class GetMBParamRequest extends BaseReq{
	
	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;
	
	@XmlElement(name="taxi_company_id", required = false, defaultValue = "0")
	private Integer taxiCoID;

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
	
	
}
