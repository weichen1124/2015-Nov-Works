/****************************************************************************
 *
 *		   		    Copyright (c), 2014
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GetUDICalloutResponse.java $
 * 
 * 1     1/16/14 3:44p Dchen
 * PF-15847, UDI callout extensions.
 * 
 * 
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetUDICalloutResponseType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "numberOfEvents","listOfCalloutEvents"})
public class GetUDICalloutResponse extends BaseRes {
	
	@XmlElement(name="number_of_events",   required = false)
	private Integer numberOfEvents;
	
	@XmlElementWrapper(name = "listOfCalloutEvents", required = false)
	// @XmlElement(name="listOfCalloutEvents",   required = false)
	@XmlElement(name="callout_event",   required = false)
	private UDICalloutListItem[] listOfCalloutEvents;

	public Integer getNumberOfEvents() {
		return numberOfEvents;
	}

	public void setNumberOfEvents(Integer numberOfEvents) {
		this.numberOfEvents = numberOfEvents;
	}

	public UDICalloutListItem[] getListOfCalloutEvents() {
		return listOfCalloutEvents;
	}

	public void setListOfCalloutEvents(UDICalloutListItem[] listOfCalloutEvents) {
		this.listOfCalloutEvents = listOfCalloutEvents;
	}
	

}
