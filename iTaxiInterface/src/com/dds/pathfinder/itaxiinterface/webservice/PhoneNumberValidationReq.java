/****************************************************************************
 *
 *                            Copyright (c), 2010
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/PhoneNumberValidationReq.java $
 * 
 * 1     9/20/10 11:25a Ezhang
 * OSP 2.0
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PhoneNumberValidationReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", "phoneNum" })
public class PhoneNumberValidationReq extends BaseReq{
	@XmlElement(name="sessionID", required = false)
	private String sessionID;

	@XmlElement(name="phonenum", required = true)
	private String phoneNum;

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phonenum) {
		this.phoneNum = phoneNum;
	}
}