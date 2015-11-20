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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/PostJobResponse.java $
* 
* 
* PF-16428, 03/13/15, DChen, based on json-api format
* 
* **************************************/
package com.dds.pathfinder.pfrest.resources;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostJobResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	
	public PostJobResponse(){
		super();
		setDataType(PFDataType.PFData_Type_Jobs.getDataType());
		setDataID(null);
	}
	
	@JsonProperty(value = "resources")
	private List<PostJobIDReference> jobIDs;

	public List<PostJobIDReference> getJobIDs() {
		return jobIDs;
	}

	public void setJobIDs(List<PostJobIDReference> jobIDs) {
		this.jobIDs = jobIDs;
	}
}
