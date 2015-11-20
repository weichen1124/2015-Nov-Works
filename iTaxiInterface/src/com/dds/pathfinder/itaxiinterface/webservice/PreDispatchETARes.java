/****************************************************************************
 *
 *                            Copyright (c), 2015
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 * 
 * PF-16496, 05/27/15, DChen, add pre dispatch eta.
 * 
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
@XmlType(name = "PreDispatchETAResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "etaInSecond","errorCode"})
public class PreDispatchETARes extends BaseRes {
	@XmlElement(name="eta_in_second", required = true )
	private int etaInSecond;
	
	@XmlElement(name="errorCode",   required = false)
	private Integer errorCode;
	
	
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public int getEtaInSecond() {
		return etaInSecond;
	}

	public void setEtaInSecond(int etaInSecond) {
		this.etaInSecond = etaInSecond;
	}

	
}
