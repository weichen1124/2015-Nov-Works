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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/GetAliveEventResponse.java $
* 
* 
* PF-16385, 02/13/15, DChen, create pfrest project.
* 
* **************************************/

package com.dds.pathfinder.pfrest.resources;

import java.util.ArrayList;
import java.util.List;

import com.dds.pathfinder.itaxiinterface.common.impl.CommonImplement.BookJobErrorCode;
import com.dds.pathfinder.pfrest.events.AliveEvent;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAliveEventResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	
	public GetAliveEventResponse(){
		super();
		setDataType(PFDataType.PFData_Type_Events.getDataType());
		setDataID(null);
		setResponseCode(BookJobErrorCode.NO_ERROR.getCode());		//always succeed
		setErrorMessage(ResponseResource.ERROR_CODE_SUCCESS);
		aliveEvents = new ArrayList<AliveEvent>();
		aliveEvents.add(new AliveEvent());
	}
	
	@JsonProperty(value = "resources")
	private List<AliveEvent> aliveEvents;

	public List<AliveEvent> getAliveEvents() {
		return aliveEvents;
	}

	public void setAliveEvents(List<AliveEvent> aliveEvents) {
		this.aliveEvents = aliveEvents;
	}

}
