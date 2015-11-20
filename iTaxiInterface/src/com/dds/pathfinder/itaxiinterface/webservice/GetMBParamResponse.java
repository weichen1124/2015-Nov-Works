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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GetMBParamResponse.java $
 * 
 * 1     10/28/13 12:17p Sfoladian
 * 
 * PF-15595 - Mobile Booker Parameter for Mobile Booker V2.0
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
@XmlType(name = "GetMBParamResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "nrOfParameters","listOfParameters","errorCode"})
public class GetMBParamResponse extends BaseRes{

	@XmlElement(name="nrOfParameters",   required = false)
	private Integer nrOfParameters;
	
	@XmlElement(name="listOfParameters",   required = false)
	private MbParamListItem[] listOfParameters;

	@XmlElement(name="errorCode",   required = false)
	private Integer errorCode;

	public MbParamListItem[] getListOfParameters() {
		return listOfParameters;
	}

	public void setListOfParameters(MbParamListItem[] listOfParameters) {
		this.listOfParameters = listOfParameters;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public Integer getNrOfParameters() {
		return nrOfParameters;
	}

	public void setNrOfParameters(Integer nrOfParameters) {
		this.nrOfParameters = nrOfParameters;
	}
}
