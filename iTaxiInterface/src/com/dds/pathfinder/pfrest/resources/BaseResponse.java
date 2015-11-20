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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResponse extends PFResourceData {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "response-code")
	private Integer responseCode;
		
	@JsonProperty(value = "message")	
	private String errorMessage;
	
	@JsonIgnore
	private int httpStatus;

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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
