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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/AccountPaymentRequest.java $
 * 
 * 2     11/28/13 9:51a Sfoladian
 * PF-15748- Mobile Booker Account Payment Support Part I
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
@XmlType(name = "AccountPaymentReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID" , "taxiCoID", "deviceID", "taxiRideID", "accountCode", "amount", "tip"})
public class AccountPaymentRequest extends BaseReq{

	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;

	@XmlElement(name="account_code", required = true)
	private String accountCode;
	
	/*@XmlElement(name="account_password", required = true)
	private String accountPassword;*/
	
	@XmlElement(name="taxi_company_id", required = false, defaultValue = "0")
	private Integer taxiCoID;
	
	@XmlElement(name="taxi_ride_id", required = true)
	private String taxiRideID;
	
	@XmlElement(name="deviceID", required = true)
	private String deviceID;
	
	@XmlElement(name = "amount", required = true)
	protected String amount;
	
	@XmlElement(name = "tip", required = false)
	protected String tip;

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	/*public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}*/

	public Integer getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(Integer taxiCoID) {
		this.taxiCoID = taxiCoID;
	}

	public String getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(String taxiRideID) {
		this.taxiRideID = taxiRideID;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

}
