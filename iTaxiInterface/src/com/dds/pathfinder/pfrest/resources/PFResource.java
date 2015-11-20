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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/PFResource.java $
* 
* 
* PF-16428, 03/13/15, DChen, based on json-api format
* 
* **************************************/
package com.dds.pathfinder.pfrest.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class PFResource implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// public abstract PFResourceLink getResourceLink();
		
	public abstract PFResourceData getDataResource();

}
