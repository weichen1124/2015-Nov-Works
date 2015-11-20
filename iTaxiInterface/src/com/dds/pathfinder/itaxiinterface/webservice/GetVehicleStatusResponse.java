/****************************************************************************
 *
 *		   		    Copyright (c), 2012
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GetVehicleStatusResponse.java $
 * 
 * 1     11/27/12 1:13p Dchen
 * PF-14930, Veolia osp changes.
 * 
 * 
 * 
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetVehicleStatusResponseType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "vehicleInfo"})
public class GetVehicleStatusResponse extends BaseRes {
	
	@XmlElement(name="vehicle_info",   required = false)
	private VehicleInfoListItem vehicleInfo;

	public VehicleInfoListItem getVehicleInfo() {
		return vehicleInfo;
	}

	public void setVehicleInfo(VehicleInfoListItem vehicleInfo) {
		this.vehicleInfo = vehicleInfo;
	}
	
	
}
