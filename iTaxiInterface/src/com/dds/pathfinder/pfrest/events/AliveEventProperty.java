/****************************************************************************
*
*		   		    Copyright (c), 2015
*					All rights reserved
*
*					DIGITAL DISPATCH SYSTEMS, INC
*					RICHMOND, BRITISH COLUMBIA
*					CANADA
*
****************************************************************************
*
*
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/AliveEventProperty.java $
* 
* PF-16572, 05/19/15, DChen, review notification message json format.
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import java.util.Date;

import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AliveEventProperty extends EventProperties {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "time")
	private String eventDTM;

	
	
	public AliveEventProperty() {
		super();
		eventDTM = Utilities.formatOSPDefaultDate(new Date());
	}

	public String getEventDTM() {
		return eventDTM;
	}

	public void setEventDTM(String eventDTM) {
		this.eventDTM = eventDTM;
	}
	
	public void setEventDTM(Date eventDTM) {
		this.eventDTM = Utilities.formatOSPDefaultDate(eventDTM);
	}
	
	
	
}