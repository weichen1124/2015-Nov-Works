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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/DriverNotesData.java $
* 
* 
*  PF-16819, 10/05/15, DChen, added address driver notes resource.
* 
* **************************************/
package com.dds.pathfinder.pfrest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;


public class DriverNotesData extends PFResourceData {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "address-drv-notes")
	private String addrDriverNotes;
	
	@JsonProperty(value = "landmark-drv-notes")
	private String landMarkDriverNotes;
	
	@JsonProperty(value = "blockface-drv-notes")
	private String blockFcDriverNotes;
	
	@JsonProperty(value = "area-drv-notes")
	private String areaDriverNotes;

	
	
	
	public DriverNotesData() {
		super();
		setDataType(PFDataType.PFData_Type_Drivernotes.getDataType());
	}
	
	

	public DriverNotesData(String addrDriverNotes, String landMarkDriverNotes,
			String blockFcDriverNotes, String areaDriverNotes) {
		super();
		setDataType(PFDataType.PFData_Type_Drivernotes.getDataType());
		this.addrDriverNotes = addrDriverNotes;
		this.landMarkDriverNotes = landMarkDriverNotes;
		this.blockFcDriverNotes = blockFcDriverNotes;
		this.areaDriverNotes = areaDriverNotes;
	}



	public String getAddrDriverNotes() {
		return addrDriverNotes;
	}

	public void setAddrDriverNotes(String addrDriverNotes) {
		this.addrDriverNotes = addrDriverNotes;
	}

	public String getLandMarkDriverNotes() {
		return landMarkDriverNotes;
	}

	public void setLandMarkDriverNotes(String landMarkDriverNotes) {
		this.landMarkDriverNotes = landMarkDriverNotes;
	}

	public String getBlockFcDriverNotes() {
		return blockFcDriverNotes;
	}

	public void setBlockFcDriverNotes(String blockFcDriverNotes) {
		this.blockFcDriverNotes = blockFcDriverNotes;
	}

	public String getAreaDriverNotes() {
		return areaDriverNotes;
	}

	public void setAreaDriverNotes(String areaDriverNotes) {
		this.areaDriverNotes = areaDriverNotes;
	}
	
}
