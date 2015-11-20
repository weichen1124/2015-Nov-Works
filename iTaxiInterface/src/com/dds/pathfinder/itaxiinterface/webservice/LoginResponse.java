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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/LoginResponse.java $
 * 
 * 4     12/08/09 12:46p Yyin
 * Changed type of authCompList.
 * 
 * 3     11/30/09 9:46a Yyin
 */
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LoginResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "authCompList","sessionID"})
public class LoginResponse extends BaseRes{
	
	
	@XmlElement(name="authCompList",   required = false)
	private CompanyListItem[] authCompList;
	
	@XmlElement(name="sessionID",   required = false)
	private String sessionID;


	public CompanyListItem[] getAuthCompList() {
		return authCompList;
	}

	public void setAuthCompList(CompanyListItem[] authCompList) {
		this.authCompList = authCompList;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}