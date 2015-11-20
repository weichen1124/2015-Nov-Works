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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/JobResource.java $
* 
* PF-16522, 05/19/15, DChen, if not booked by TG, send not found response.
* 
* PF-16428, 03/13/15, DChen, based on json-api format
* 
* PF-16385, 02/13/15, DChen, create pfrest project.
* 
* **************************************/
package com.dds.pathfinder.pfrest.resources;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobResource extends PFResource {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "links")
	private JobResourceLinks resourceLink;
	
	@JsonProperty(value = "data")
	private Job dataResource;
	
	@JsonIgnore
	private int httpStatus;
	
	
	public JobResource(){
		super();
		this.resourceLink = new JobResourceLinks();
		this.dataResource = new Job();
	}
	
//	@GET
//	@Produces("application/json")
//	public JobResource get(){
//		return this;
//	}

	public void setJobLinks(JobResourceLinks resourceLink) {
		this.resourceLink = resourceLink;
	}

	public void setDataResource(Job dataResource) {
		this.dataResource = dataResource;
	}

	public JobResourceLinks getResourceLink() {
		return resourceLink;
	}

	@Override
	public Job getDataResource() {
		return dataResource;
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
