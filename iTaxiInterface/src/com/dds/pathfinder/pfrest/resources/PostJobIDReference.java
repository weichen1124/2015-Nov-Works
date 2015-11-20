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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/PostJobIDReference.java $
* 
* 
* PF-16428, 03/13/15, DChen, based on json-api format
* 
* **************************************/

package com.dds.pathfinder.pfrest.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostJobIDReference implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "job-id")	
	private Long taxiRideID;
	
	@JsonProperty(value = "reference-nb")
	private String referenceNb;
	
	

	public PostJobIDReference() {
		super();
	}

	public PostJobIDReference(Long taxiRideID, String referenceNb) {
		super();
		this.taxiRideID = taxiRideID;
		this.referenceNb = referenceNb;
	}

	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}

	public String getReferenceNb() {
		return referenceNb;
	}

	public void setReferenceNb(String referenceNb) {
		this.referenceNb = referenceNb;
	}

}
