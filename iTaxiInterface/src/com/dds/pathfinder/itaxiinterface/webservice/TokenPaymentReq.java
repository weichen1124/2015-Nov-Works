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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/TokenPaymentReq.java $
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
@XmlType(name = "TokenPaymentReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "taxiCoID", "deviceID", "seqNo", "transDTM","reqType", "token", "amount", "tip", "info1", "info2", "info3", "info4", "info5", "cardNr","cardBrand" })
public class TokenPaymentReq extends BaseReq {
	
	@XmlElement(name="taxi_co_id", required = false, defaultValue = "0")
	private Integer taxiCoID;
	
	@XmlElement(name="deviceID", required = true)
	private String deviceID;
	
	@XmlElement(name="seqNo", required = true)
	private String seqNo;
	
	@XmlElement(name = "transDTM", required = false)
	protected String transDTM;
	
	@XmlElement(name = "reqType", required = true)
	protected String reqType;
	
	@XmlElement(name = "token", required = true)
	protected String token;
	
	@XmlElement(name = "amount", required = true)
	protected String amount;
	
	@XmlElement(name = "tip", required = false)
	protected String tip;
	
	/* Driver ID */
	@XmlElement(name="info_1", required = false)
	private String info1;
	
	/* Vehicle ID */
	@XmlElement(name="info_2", required = false)
	private String info2;
	
	/* Job ID */
	@XmlElement(name="info_3", required = true)
	private String info3;
	
	/* Pickup Address */
	@XmlElement(name="info_4", required = false)
	private String info4;
	
	/* Drop off Address */
	@XmlElement(name="info_5", required = false)
	private String info5;
	
	//adding following two for MDT receipt requirement
	/* card number  */
	@XmlElement(name="cardNr", required = true)
	private String cardNr;
	
	/* card Brand */
	@XmlElement(name="cardBrand", required = true)
	private String cardBrand;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getInfo1() {
		return info1;
	}

	public void setInfo1(String info1) {
		this.info1 = info1;
	}

	public String getInfo2() {
		return info2;
	}

	public void setInfo2(String info2) {
		this.info2 = info2;
	}

	public String getInfo3() {
		return info3;
	}

	public void setInfo3(String info3) {
		this.info3 = info3;
	}

	public String getInfo4() {
		return info4;
	}

	public void setInfo4(String info4) {
		this.info4 = info4;
	}

	public String getInfo5() {
		return info5;
	}

	public void setInfo5(String info5) {
		this.info5 = info5;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public Integer getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(Integer taxiCoID) {
		this.taxiCoID = taxiCoID;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	/**
	 * @return the reqType
	 */
	public String getReqType() {
		return reqType;
	}

	/**
	 * @param reqType the reqType to set
	 */
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return the transDTM
	 */
	public String getTransDTM() {
		return transDTM;
	}

	/**
	 * @param transDTM the transDTM to set
	 */
	public void setTransDTM(String transDTM) {
		this.transDTM = transDTM;
	}

	public String getCardNr() {
		return cardNr;
	}

	public void setCardNr(String cardNr) {
		this.cardNr = cardNr;
	}

	public String getCardBrand() {
		return cardBrand;
	}

	public void setCardBrand(String cardBrand) {
		this.cardBrand = cardBrand;
	}

	/**
	 * @return the tip
	 */
	public String getTip() {
		return tip;
	}

	/**
	 * @param tip the tip to set
	 */
	public void setTip(String tip) {
		this.tip = tip;
	}

}
