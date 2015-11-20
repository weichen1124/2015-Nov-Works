/****************************************************************************
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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GetPredefinedMsgReq.java $
 * 
 * 1     1/18/10 1:34p Yyin
 * Added.
 * 
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetPredefinedMsgReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID","predefinedMessageID"})
public class GetPredefinedMsgReq extends BaseReq{
	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;
	
	@XmlElement(name="predefinedMessageID", required = true, defaultValue="")
	private String predefinedMessageID;
	
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	public String getPredefinedMessageID() {
		return predefinedMessageID;
	}

	public void setPredefinedMessageID(String predefinedMessageID) {
		this.predefinedMessageID = predefinedMessageID;
	}
}