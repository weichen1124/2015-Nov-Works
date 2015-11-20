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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/BaseRes.java $
 * 
 * 3     1/13/10 6:18p Dchen
 * OSP interface.
 * 
 * 2     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 1     8/14/09 4:21p Dchen
 * pathfinder iTaxi interface.
 * 
 * 
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseResType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "requestStatus", "errorMessage"})
public class BaseRes {
	@XmlElement(name="request_status", required = true)
	private Integer requestStatus;
	
	@XmlElement(name="error_string", required = true)
	private String errorMessage;

	
	
	public BaseRes() {
		super();
		this.requestStatus = GenErrMsgRes.STATUS_FAILED;
		this.errorMessage = GenErrMsgRes.ERROR_CODE_FAILED;
	}

	public Integer getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(Integer requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
