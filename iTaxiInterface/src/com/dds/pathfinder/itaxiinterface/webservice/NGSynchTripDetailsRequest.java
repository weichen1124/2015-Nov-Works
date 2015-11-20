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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/NGSynchTripDetailsRequest.java $
 * 
 * 1     9/14/10 2:41p Ajiang
 * C34078 - Texas Taxi Enhancements
 *
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NGSynchTripDetailsReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", 
						  "taxiCoID", 
						  "QueryIntervalStartDateTime", 
						  "QueryIntervalEndDateTime" 
						  })
public class NGSynchTripDetailsRequest extends BaseReq{
	
	@XmlElement(name="sessionID", required = false)
	private String sessionID;
	
	@XmlElement(name="taxiCoID", required = true )
	private Integer taxiCoID;
	
	@XmlElement(name="QueryIntervalStartDateTime", required = false)
	private String QueryIntervalStartDateTime;
	
	@XmlElement(name="QueryIntervalEndDateTime", required = false)
	private String QueryIntervalEndDateTime;
	
	
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

	public String getStartDateTime() {
		return QueryIntervalStartDateTime;
	}

	public void setStartDateTime(String QueryIntervalEndDateTime) {
		this.QueryIntervalStartDateTime = QueryIntervalEndDateTime;
	}

	public String getEndDateTime() {
		return QueryIntervalEndDateTime;
	}

	public void setEndDateTime(String QueryIntervalEndDateTime) {
		this.QueryIntervalEndDateTime = QueryIntervalEndDateTime;
	}	
}
