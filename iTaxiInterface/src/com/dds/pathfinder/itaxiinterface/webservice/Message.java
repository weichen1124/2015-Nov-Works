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
 * File Name: Message.java
 *
 * $Log $
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "callTakerMessage" })
public class Message {
	@XmlElement(name="callTakerMessage", required = false)
	String callTakerMessage;
 
	
	
	public Message() {
		super();
	}
	
	

	public Message(String callTakerMessage) {
		super();
		this.callTakerMessage = callTakerMessage;
	}



	public String getCallTakerMessage() {
		return callTakerMessage;
	}

	public void setCallTakerMessage(String callTakerMessage) {
		this.callTakerMessage = callTakerMessage;
	}
}
