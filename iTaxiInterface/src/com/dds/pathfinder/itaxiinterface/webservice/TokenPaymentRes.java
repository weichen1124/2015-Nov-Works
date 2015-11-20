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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/TokenPaymentRes.java $
 * 
 * 2     10/30/13 5:09p Ezhang
 * PF-15582 added email and emailSent to the response
 * 
 * 1     10/23/13 3:03p Ezhang
 * 
 * PF-15582 Add Credit card Payment by Token Support for Mobile
 * Booker V2.0
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
@XmlType(name = "TokenPaymentResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "seqNo", "rspCode", "rspMsg", "authCode", "refNo","email","emailSend","errorCode"})
public class TokenPaymentRes extends BaseRes {
	
	@XmlElement(name="seqNo", required = true)
	private String seqNo;
	
	@XmlElement(name="rspCode", required = true)
	private Integer rspCode;
	
	@XmlElement(name="rspMsg", required = false)
	private String rspMsg;
	
	@XmlElement(name="authCode", required = false)
	private String authCode;
	
	@XmlElement(name="refNo", required = false)
	private String refNo;
	
	@XmlElement(name="email", required = false)
	private String email;
	
	@XmlElement(name="emailSent", required = false)
	private boolean emailSend;
	
	@XmlElement(name="errorCode", required = false)
	private Integer errorCode;

	

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public Integer getRspCode() {
		return rspCode;
	}

	public void setRspCode(Integer rspCode) {
		this.rspCode = rspCode;
	}

	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmailSend() {
		return emailSend;
	}

	public void setEmailSend(boolean emailSend) {
		this.emailSend = emailSend;
	}

}
