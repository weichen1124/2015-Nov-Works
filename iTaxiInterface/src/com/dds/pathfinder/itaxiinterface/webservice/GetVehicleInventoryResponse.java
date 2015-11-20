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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GetVehicleInventoryResponse.java $
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
@XmlType(name = "GetVehicleInventoryResponseType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "nbOfVehicles","vehicleInfoList"})
public class GetVehicleInventoryResponse extends BaseRes {
	
	@XmlElement(name="nb_of_vehicles",   required = false)
	private Integer nbOfVehicles;
	
	@XmlElement(name="list_of_vehicles",   required = false)
	private VehicleInfoListItem[] vehicleInfoList;

	public Integer getNbOfVehicles() {
		return nbOfVehicles;
	}

	public void setNbOfVehicles(Integer nbOfVehicles) {
		this.nbOfVehicles = nbOfVehicles;
	}

	public VehicleInfoListItem[] getVehicleInfoList() {
		return vehicleInfoList;
	}

	public void setVehicleInfoList(VehicleInfoListItem[] vehicleInfoList) {
		this.vehicleInfoList = vehicleInfoList;
	}
	
	


}
