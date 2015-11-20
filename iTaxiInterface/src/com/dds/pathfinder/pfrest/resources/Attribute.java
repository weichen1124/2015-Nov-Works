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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/Attribute.java $
 * 
 * PF-16385, 02/13/15, DChen, create pfrest project.
 * 
 * 
 *****/

package com.dds.pathfinder.pfrest.resources;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Attribute implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "attr-short-name")
	String attrShortName;

	
	
	public Attribute() {
		super();
	}
	
	

	public Attribute(String attrShortName) {
		super();
		this.attrShortName = attrShortName;
	}



	public String getAttrShortName() {
		return attrShortName;
	}

	public void setAttrShortName(String attrShortName) {
		this.attrShortName = attrShortName;
	}
}
