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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/seconddevices/RegisterDeviceResponse.java $
* 
* PF-16605, 06/18/15, DChen, created for 2nd device.
* 
* **************************************/
package com.dds.pathfinder.pfrest.seconddevices;

import com.dds.pathfinder.pfrest.resources.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SecondDeviceResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;

	public SecondDeviceResponse() {
		super();
		setDataType(PFDataType.PFData_Type_2ndDevice.getDataType());
		setDataID(null);
	}
	
	

	public SecondDeviceResponse(String driverName, String companyName) {
		this();
		this.driverName = driverName;
		this.companyName = companyName;
	}



	@JsonProperty(value = "driver-name")
	private String driverName;
	
	@JsonProperty(value = "company-name")
	private String companyName;

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
}
