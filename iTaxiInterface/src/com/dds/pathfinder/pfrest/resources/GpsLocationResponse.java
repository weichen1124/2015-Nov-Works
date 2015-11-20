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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/GpsLocationResponse.java $
* 
* 
* PF-16385, 10/08/15, DZhou, add vehicle location request
* 
* **************************************/

package com.dds.pathfinder.pfrest.resources;

import java.util.ArrayList;
import java.util.List;

import com.dds.pathfinder.itaxiinterface.common.impl.CommonImplement.BookJobErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GpsLocationResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	
	public GpsLocationResponse(){
		super();
		setDataType(PFDataType.PFData_Type_GpsLocation.getDataType());
		setDataID(null);
		setResponseCode(BookJobErrorCode.NO_ERROR.getCode());
		setErrorMessage(ResponseResource.ERROR_CODE_SUCCESS);
		gpsLocations = new ArrayList<GpsLocation>();
		gpsLocations.add(new GpsLocation());
	}
	
	@JsonProperty(value = "resources")
	private List<GpsLocation> gpsLocations;

	public List<GpsLocation> getGpsLocations() {
		return gpsLocations;
	}

	public void setGpsLocations(List<GpsLocation> gpsLocations) {
		this.gpsLocations = gpsLocations;
	}

}
