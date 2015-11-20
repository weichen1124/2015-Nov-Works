/****************************************************************************
 *
 *                            Copyright (c), 2009
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 * File Name: GetAccountRequest.java
 *
 * $Log $
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAccountDetailReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", "taxiCoID", "accountCode", "accountName" })
public class GetAccountDetailRequest extends BaseReq{
	@XmlElement(name="sessionID", required = false)
	private String sessionID;

	@XmlElement(name="taxi_company_id", required = true)
	private Integer taxiCoID;
	
	@XmlElement(name="accountCode")
	private String accountCode;
	
	@XmlElement(name="accountName")
	private String accountName;
	
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
	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
}