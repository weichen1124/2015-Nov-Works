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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/DriverNotesResource.java $
* 
* 
*  PF-16819, 10/05/15, DChen, added address driver notes resource.
* 
* **************************************/
package com.dds.pathfinder.pfrest.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DriverNotesResource extends PFResource {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "data")
	private DriverNotesData dataResource;
	
	@JsonIgnore
	private int httpStatus;
	
	public DriverNotesResource(){
		super();
		this.dataResource = new DriverNotesData();
	}
	
	@Override
	public PFResourceData getDataResource() {
		return dataResource;
	}
	
	public void setDataResource(DriverNotesData dataResource) {
		this.dataResource = dataResource;
	}

	@JsonIgnore
	public int getHttpStatus() {
		return httpStatus;
	}

	@JsonIgnore
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	
}
