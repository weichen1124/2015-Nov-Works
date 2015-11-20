/****************************************************************************
 *
 *		   		    Copyright (c), 2014
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/DrvVehAttributes.java $
 * 
 * 08/29/14,DChen, PF-16183,added TSS require service.
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;

public class DrvVehAttributes {
	private String drvAttrBinary;
	private String vehAttrBinary;
	
	
	
	public DrvVehAttributes(String drvAttrBinary, String vehAttrBinary) {
		super();
		this.drvAttrBinary = drvAttrBinary;
		this.vehAttrBinary = vehAttrBinary;
	}
	
	public String getDrvAttrBinary() {
		return drvAttrBinary;
	}
	public void setDrvAttrBinary(String drvAttrBinary) {
		this.drvAttrBinary = drvAttrBinary;
	}
	public String getVehAttrBinary() {
		return vehAttrBinary;
	}
	public void setVehAttrBinary(String vehAttrBinary) {
		this.vehAttrBinary = vehAttrBinary;
	}
	
	

}
