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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/BaseReq.java $
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
@XmlType(name = "BaseReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "systemID", "systemPassword"})
public class BaseReq {
	@XmlElement(name="systemID", required = true, defaultValue="0")
	private Integer systemID;
	
	@XmlElement(name="systemPassword", required = true, defaultValue="")
	private String systemPassword;

	
	public Integer getSystemID() {
		return systemID;
	}

	public void setSystemID(Integer systemID) {
		this.systemID = systemID;
	}

	public String getSystemPassword() {
		return systemPassword;
	}

	public void setSystemPassword(String systemPassword) {
		this.systemPassword = systemPassword;
	}
}
