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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/SendDriverMessageRequest.java $
 * 
 * 4     11/05/13 1:17p Ezhang
 * PF-15621 remove the deviceID.
 * 
 * 3     10/30/13 4:23p Sfoladian
 * PF-15621: added deviceId to the SendDriverMessage Request
 * 
 * 2     2/09/10 2:35p Jwong
 * 
 * 1     1/29/10 10:55a Jwong
 * 
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SendDriverMessageReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = {  "sessionID", "message", "deliveryTime", "priority", "destinationTypeID", "destination" })
public class SendDriverMessageRequest extends BaseReq{
	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;
	
	@XmlElement(name="message", required = true )
	private String message;
	
	@XmlElement(name="deliveryTime", required = false )
	private String deliveryTime;
	
	@XmlElement(name="priority", required = true )
	private String priority;
	
	@XmlElement(name="destinationTypeID", required = true )
	private String destinationTypeID;
	
	@XmlElement(name="destination", required = false )
	private String destination;

	
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getDeliveryTime() {
		return deliveryTime;
	}
	
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	
	public String getPriority() {
		return priority;
	}
	
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public String getDestinationTypeID() {
		return destinationTypeID;
	}
	
	public void setDestinationTypeID(String destinationTypeID) {
		this.destinationTypeID = destinationTypeID;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public void setDestination(String destination) {
		this.destination = destination;
	}

	
}
