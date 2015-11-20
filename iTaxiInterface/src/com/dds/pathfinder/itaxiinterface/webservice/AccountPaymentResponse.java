/****************************************************************************
 *
 *                            Copyright (c), 2013
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/AccountPaymentResponse.java $
 * 
 * 1     10/28/13 12:50p Sfoladian
 * 
 * PF-15587 OSP Account Payment Support for Mobile Booker V2.0
 * 
 */
package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author sfoladian
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountPaymentResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "errorCode"})
public class AccountPaymentResponse extends BaseRes{
	@XmlElement(name="errorCode", required = true)
	private Integer errorCode;

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

}
