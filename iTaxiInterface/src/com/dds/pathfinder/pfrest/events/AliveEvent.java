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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/AliveEvent.java $
* 
* PF-16653, 07/23/15, DChen, remove properties from pf events.
* 
* PF-16572, 05/19/15, DChen, review notification message json format.
* 
* PF-16428, 03/25/15, DChen, pfrest job events service.
* 
* PF-16428, 03/06/15, DChen, pfrest notification service.
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import java.util.Date;

import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.fasterxml.jackson.annotation.JsonProperty;


public class AliveEvent extends PFRestEvent  {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "time")
	private String aliveEventDTM;
	
	
//	@JsonProperty(value = "pfrest-version")
//	private String pfrestVersion = Utilities.PFREST_VERSION_NUMBER;
	
	public AliveEvent(){
		super();
		setDataType(PFRestEvent.EVENT_TYPE_PFREST_ALIVE);
		aliveEventDTM = Utilities.formatOSPDefaultDate(new Date());
		// setProperties(new AliveEventProperty());
		//setDataID(null);
	}


	public String getAliveEventDTM() {
		return aliveEventDTM;
	}


	public void setAliveEventDTM(String aliveEventDTM) {
		this.aliveEventDTM = aliveEventDTM;
	}
	
	public void setAliveEventDTM(Date eventDTM) {
		this.aliveEventDTM = Utilities.formatOSPDefaultDate(eventDTM);
	}
	

}
