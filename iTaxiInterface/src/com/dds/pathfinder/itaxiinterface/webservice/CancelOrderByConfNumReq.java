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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/CancelOrderByConfNumReq.java $
 * 
 * 1     9/20/10 11:27a Ezhang
 * OSP 2.0
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CancelJobByConfNumReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "requestType", "confirmationNum", "sessionID"})
public class CancelOrderByConfNumReq extends BaseReq{
	@XmlElement(name="request_type", required = true)
	private Integer requestType;
		
	@XmlElement(name="confirmationNum", required = true)
	private String confirmationNum;
	
	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;
	
	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	public String getConfirmationNum() {
		return confirmationNum;
	}

	public void setconfirmationNum(String confirmationNum) {
		this.confirmationNum = confirmationNum;
	}
	
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}
