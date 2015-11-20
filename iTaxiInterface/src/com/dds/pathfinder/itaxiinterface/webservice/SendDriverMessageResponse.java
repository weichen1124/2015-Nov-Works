/****************************************************************************
 *
 *		   		    Copyright (c), 2009
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/SendDriverMessageResponse.java $
 * 
 * 2     11/05/13 1:17p Ezhang
 * PF-15612 create response added errorCode
 * 
 * 1     1/29/10 10:55a Jwong
 * 
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SendDriverMessageResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = {"errorCode"})
public class SendDriverMessageResponse extends BaseRes{
	@XmlElement(name="errorCode", required = false)
	private Integer errorCode;
	
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
