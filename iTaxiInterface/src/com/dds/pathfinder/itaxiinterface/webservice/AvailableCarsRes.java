/****************************************************************************
 *
 *                            Copyright (c), 2015
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 * 
 * Created for Mobile Application vehicle on the map
 * 
 */
package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ezhang
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AvailableCarsResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "nofCars", "carGPSList", "errorCode"})
public class AvailableCarsRes extends BaseRes {
	@XmlElement(name="nofCars", required = true )
	private Integer nofCars;
	
	@XmlElement(name="carGPSList", required = true )
	private CarGPSListItem[] carGPSList;
	
	public Integer getNofCars() {
		return nofCars;
	}

	public void setNofCars(Integer nofCars) {
		this.nofCars = nofCars;
	}

	public CarGPSListItem[] getCarList() {
		return carGPSList;
	}

	public void setCarList(CarGPSListItem[] carGPSList) {
		this.carGPSList = carGPSList;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	@XmlElement(name="errorCode",   required = false)
	private Integer errorCode;

}
