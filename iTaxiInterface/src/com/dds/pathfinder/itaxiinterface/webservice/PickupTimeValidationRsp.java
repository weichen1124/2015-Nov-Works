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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/PickupTimeValidationRsp.java $
 * 
 * 2     9/20/10 11:23a Ezhang
 * 
 * OSP 2.0
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PickupTimeValidationRspType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "errorCode", "nofTaxisAllowed"})
public class PickupTimeValidationRsp extends BaseRes{
	@XmlElement(name="errorCode", required = true)
	private Integer errorCode;
	
	@XmlElement(name="nofTaxisAllowed", required = false)
	private Integer nofTaxisAllowed;
	
	
	public Integer getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public void setNofTaxisAllowed(Integer nofTaxisAllowed) {
		this.nofTaxisAllowed = nofTaxisAllowed;
	}
	
	public Integer getNofTaxisAllowed() {
		return nofTaxisAllowed;
	}

	
}