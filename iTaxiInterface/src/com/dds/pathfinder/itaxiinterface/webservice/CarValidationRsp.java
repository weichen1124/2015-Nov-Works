/****************************************************************************
 *
 *		   		    Copyright (c), 2010
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/CarValidationRsp.java $
 * 
 * 1     9/20/10 11:33a Ezhang
 * OSP 2.0
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CarValidationRspType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "errorCode", "carStatus"})
public class CarValidationRsp extends BaseRes{
	@XmlElement(name="errorCode", required = true)
	private Integer errorCode;
	
	@XmlElement(name="carStatus", required = false)
	private Integer carStatus;
	
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
	public Integer getCarStatus() {
		return carStatus;
	}

	public void setCarStatus(Integer carStatus) {
		this.carStatus = carStatus;
	}
}
