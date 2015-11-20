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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/DriverMessageDestinationListItem.java $
 * 
 * 1     1/12/10 2:29p Yyin
 * Added
 * 
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DriverMessageDestinationListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "destinationTypeID"})
public class DriverMessageDestinationListItem {
		
	@XmlElement(name="destinationTypeID", required = true, defaultValue="")
	private String destinationTypeID;


	public String getDestinationTypeID() {
		return destinationTypeID;
	}

	public void setDestinationTypeID(String destinationTypeID) {
		this.destinationTypeID = destinationTypeID;
	}
}
