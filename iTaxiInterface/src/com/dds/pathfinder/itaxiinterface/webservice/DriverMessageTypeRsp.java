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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/DriverMessageTypeRsp.java $
 * 
 * 2     1/12/10 5:50p Yyin
 * Added supportScheduledDelivery and supportScheduledPriority.
 * 
 * 1     1/12/10 2:29p Yyin
 * Added
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DriverMessageTypeResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "supportScheduledDelivery", "supportProiority", "messageDestinationList"})
public class DriverMessageTypeRsp extends BaseRes{
	
	@XmlElement(name="supportScheduledDelivery", required = false, defaultValue="Y")
	private String supportScheduledDelivery;
	@XmlElement(name="supportProiority", required = false, defaultValue="Y")
	private String supportProiority;
	@XmlElement(name="messageDestinationList",   required = false)
	private DriverMessageDestinationListItem[] messageDestinationList;

	public String getSupportScheduledDelivery() {
		return supportScheduledDelivery;
	}

	public void setSupportScheduledDelivery(String supportScheduledDelivery) {
		this.supportScheduledDelivery = supportScheduledDelivery;
	}
	
	public String getSupportProiority() {
		return supportProiority;
	}

	public void setSupportProiority(String supportProiority) {
		this.supportProiority = supportProiority;
	}
	
	public DriverMessageDestinationListItem[] getMessageDestinationList() {
		return messageDestinationList;
	}

	public void setMessageDestinationList(DriverMessageDestinationListItem[] messageDestinationList) {
		this.messageDestinationList = messageDestinationList;
	}

}
