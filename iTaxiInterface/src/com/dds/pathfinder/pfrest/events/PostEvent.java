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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/PostEvent.java $
* 
* PF-16572, 05/19/15, DChen, review notification message json format.
* 
* PF-16428, 03/25/15, DChen, pfrest job events service.
* 
* PF-16428, 03/06/15, DChen, pfrest notification service.
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import java.io.Serializable;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@JsonProperty(value = "data")
	private PFRestEvent dataResource;
	
	@GET
	@Produces("application/json")
	public PostEvent get(){
		return this;
	}
	
	

	public PostEvent() {
		super();
	}
	
	public PostEvent(PFRestEvent dataResource) {
		super();
		this.dataResource = dataResource;
	}



	public PFRestEvent getDataResource() {
		return dataResource;
	}

	public void setDataResource(PFRestEvent dataResource) {
		this.dataResource = dataResource;
	}
	

	
}
