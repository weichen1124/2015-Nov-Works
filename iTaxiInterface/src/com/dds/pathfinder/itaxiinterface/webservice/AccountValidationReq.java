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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/AccountValidationReq.java $
 * 
 * 1     10/28/13 12:04p Sfoladian
 * 
 * PF-15613 - OSP Account Validation for Mobile Booker V2.0
 * 
 */


package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountValidationReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", "accountCode", "accountPassword", "taxiCoID"})

public class AccountValidationReq extends BaseReq{
	@XmlElement(name="sessionID", required = false)
	private String sessionID;

	@XmlElement(name="account_code", required = true)
	private String accountCode;
	
	@XmlElement(name="account_password", required = true)
	private String accountPassword;
	
	@XmlElement(name="taxi_co_id", required = false, defaultValue = "0")
	private Integer taxiCoID;
	
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	public Integer getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(Integer taxiCoID) {
		this.taxiCoID = taxiCoID;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

}