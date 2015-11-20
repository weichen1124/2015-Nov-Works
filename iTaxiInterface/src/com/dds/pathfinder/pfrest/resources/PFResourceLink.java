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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/PFResourceLink.java $
* 
* 
* PF-16428, 03/13/15, DChen, based on json-api format
* 
* **************************************/

package com.dds.pathfinder.pfrest.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class PFResourceLink implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public PFResourceLink(){
		super();
	}
	
	public PFResourceLink(String selfLink) {
		super();
		this.selfLink = selfLink;
	}



	@JsonProperty(value = "self")
	private String selfLink;

	public String getSelfLink() {
		return selfLink;
	}

	public void setSelfLink(String selfLink) {
		this.selfLink = selfLink;
	}
	
	
}
