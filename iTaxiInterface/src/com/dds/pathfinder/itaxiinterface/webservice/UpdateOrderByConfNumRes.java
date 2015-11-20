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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/UpdateOrderByConfNumRes.java $
 * 
 * 2     9/24/10 1:34p Ezhang
 * corrected a setter method
 * 
 * 1     9/20/10 11:15a Ezhang
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateOrderByConfNumResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "updateJobList","errorCode"})
public class UpdateOrderByConfNumRes extends BaseRes{
	@XmlElement(name="updateJobList",   required = false)
	private UpdateJobListItem[] updateJobList;

	@XmlElement(name="errorCode", required = true)
	private Integer errorCode;
	
	public UpdateJobListItem[] getCancelJobList() {
		return updateJobList;
	}

	public void setUpdateJobList(UpdateJobListItem[] updateJobList) {
		this.updateJobList = updateJobList;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
}
