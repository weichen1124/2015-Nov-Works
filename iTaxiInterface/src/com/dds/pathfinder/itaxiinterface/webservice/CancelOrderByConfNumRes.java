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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/CancelOrderByConfNumRes.java $
 * 
 * 1     9/20/10 11:26a Ezhang
 * OSP 2.0
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CancelJobByConfNumResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "cancelJobList","errorCode"})
public class CancelOrderByConfNumRes extends BaseRes{
	@XmlElement(name="cancelJobIist",   required = false)
	private CancelJobListItem[] cancelJobList;

	@XmlElement(name="errorCode", required = true)
	private Integer errorCode;
	
	public CancelJobListItem[] getCancelJobList() {
		return cancelJobList;
	}

	public void setCancelJobList(CancelJobListItem[] cancelJobList) {
		this.cancelJobList = cancelJobList;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
}
