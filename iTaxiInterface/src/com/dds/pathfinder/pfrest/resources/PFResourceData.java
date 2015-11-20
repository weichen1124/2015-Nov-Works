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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/PFResourceData.java $
* 
* PF-16819, 10/05/15, DChen, added address driver notes resource.
* 
* PF-16385, 10/08/15, DZhou, add vehicle location request
* 
* PF-16605, 06/18/15, DChen, for 2nd device.
* 
* PF-16428, 03/13/15, DChen, based on json-api format
* 
* **************************************/

package com.dds.pathfinder.pfrest.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class PFResourceData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum PFDataType {
		PFData_Type_Jobs("jobs", "/jobs"),
		PFData_Type_Vehicles("vehicles", "/vehicles"),		
		PFData_Type_Drivernotes("driver-notes", "/addresses"),
		PFData_Type_GpsLocation("gpsLocation", null),
		PFData_Type_2ndDevice("2nd-device","/2nd-device"),
		PFData_Type_Events("events", "/events");
		
		private String dataType;
		private String linkPath;
		
		private PFDataType(String dataType, String linkPath) {
			   this.dataType = dataType;
			   this.linkPath = linkPath;
		}

		public String getDataType(){
			return dataType;
		}
		
		public String getLinkPath(){
			return linkPath;
		}
		
	};
	

	@JsonProperty(value = "type")
	private String dataType;
	
	@JsonProperty(value = "id")
	private String dataID;

	public PFResourceData(){
		super();
	}
	
	public PFResourceData(String dataType, String dataID) {
		super();
		this.dataType = dataType;
		this.dataID = dataID;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataID() {
		return dataID;
	}

	public void setDataID(String dataID) {
		this.dataID = dataID;
	}
	
	

}
