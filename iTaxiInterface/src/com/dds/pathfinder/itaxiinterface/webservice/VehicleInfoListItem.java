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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/VehicleInfoListItem.java $
 * 
 * 1     11/27/12 1:11p Dchen
 * PF-14930, Veolia osp changes.
 * 
 * 
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VehicleInfoListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "vehicleNumber","vehLatitude","vehLongitude","vehicleStatus","taxiRideID"})
public class VehicleInfoListItem {
	@XmlElement(name="vehicle_number", required = false)
	private String vehicleNumber;
	
	@XmlElement(name="latitude", required = false)
	private Double vehLatitude;
	
	@XmlElement(name="longitude", required = false)
	private Double vehLongitude;
	
	@XmlElement(name="vehicle_status", required = false)
	private String vehicleStatus;
	
	@XmlElement(name="taxi_ride_id", required = false)
	private Long taxiRideID;

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public Double getVehLatitude() {
		return vehLatitude;
	}

	public void setVehLatitude(Double vehLatitude) {
		this.vehLatitude = vehLatitude;
	}

	public Double getVehLongitude() {
		return vehLongitude;
	}

	public void setVehLongitude(Double vehLongitude) {
		this.vehLongitude = vehLongitude;
	}

	public String getVehicleStatus() {
		return vehicleStatus;
	}

	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}
	
	

}
