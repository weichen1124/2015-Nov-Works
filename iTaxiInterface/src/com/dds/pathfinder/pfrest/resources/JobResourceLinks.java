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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/JobResourceLinks.java $
* 
* 
* PF-16428, 03/13/15, DChen, based on json-api format
* 
* **************************************/
package com.dds.pathfinder.pfrest.resources;

import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.pfrest.resources.PFResourceData.PFDataType;

public class JobResourceLinks extends PFResourceLink {

	private static final long serialVersionUID = 1L;

	public JobResourceLinks() {
		super(Utilities.PFREST_CONTEXT_ROOT + PFDataType.PFData_Type_Jobs.getLinkPath());
	}

	public JobResourceLinks(String selfLink) {
		super(selfLink);
	}
	
	

}
