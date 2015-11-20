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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/PredefinedMessageListItem.java $
 * 
 * 1     1/12/10 2:27p Yyin
 * Added methods driverMessageType() and predefinedMessageList().
 * 
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PredefinedMessageListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "predefinedMessageID"})
public class PredefinedMessageListItem {
		
	@XmlElement(name="predefinedMessageID", required = true, defaultValue="")
	private String predefinedMessageID;


	public String getPredefinedMesssageID() {
		return predefinedMessageID;
	}

	public void setPredefinedMesssageID(String predefinedMessageID) {
		this.predefinedMessageID = predefinedMessageID;
	}
}
