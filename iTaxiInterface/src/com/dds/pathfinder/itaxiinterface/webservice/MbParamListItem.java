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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/MbParamListItem.java $
 * 
 * 1     10/28/13 12:19p Sfoladian
 * 
 * PF-15595 - Mobile Booker Parameter
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
@XmlType(name = "MbParamListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "parameterName","parameterValue"})
public class MbParamListItem {
	
	@XmlElement(name="parameterName", required = false)
	private String parameterName;
	
	@XmlElement(name="parameterValue", required = false)
	private String parameterValue;

	public MbParamListItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MbParamListItem(String parameterName, String parameterValue) {
		super();
		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
	}
	
	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

}
