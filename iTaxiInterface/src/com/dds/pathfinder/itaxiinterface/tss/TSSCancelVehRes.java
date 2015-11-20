/****************************************************************************
 *
 *		   		    Copyright (c), 2014
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/tss/TSSCancelVehRes.java $
 * 
 * PF-16183, 08/29/14, DChen,added TSS require service.
 * 
 * 08/25/14, DChen, create TSS project.
 * 
 */

package com.dds.pathfinder.itaxiinterface.tss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.dds.pathfinder.itaxiinterface.tss.impl.TSSBaseImplement.TSSErrorCode;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSSCancelVehResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "errorCode","errorText"})
public class TSSCancelVehRes {

	@XmlElement(name="error_Code",   required = true)
	private String errorCode;
	
	@XmlElement(name="err_text", required = true)
	private String errorText;
	
	
	public TSSCancelVehRes(){
		super();
	}
	
	public TSSCancelVehRes(TSSErrorCode errCode){
		this();
		setErrorCode(errCode);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	
	public void setErrorCode(TSSErrorCode errCode){
		this.errorCode = errCode.getErrorCode();
		this.errorText = errCode.getErrorText();
	}
	
}
