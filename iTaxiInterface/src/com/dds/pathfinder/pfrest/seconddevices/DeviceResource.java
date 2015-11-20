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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/seconddevices/DeviceResource.java $
* 
* PF-16605, 06/18/15, DChen, created for 2nd device.
* 
* **************************************/
package com.dds.pathfinder.pfrest.seconddevices;


import com.dds.pathfinder.pfrest.resources.PFResource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceResource extends PFResource {

	private static final long serialVersionUID = 1L;
	
	
	@JsonProperty(value = "data")
	private SecondDevice dataResource;
	
	@JsonIgnore
	private int httpStatus;

	
	
	
	public DeviceResource() {
		super();
		dataResource = new SecondDevice();
	}

	@Override
	public SecondDevice getDataResource() {
		return dataResource;
	}
	
	public void setDataResource(SecondDevice dataResource) {
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
