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
 * File Name: ClientProfileResponse.java
 *
 * $Log $
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetClientProfileResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "numberOfRec","addressList"})
public class ClientProfileResponse extends BaseRes{
	
	@XmlElement(name="number_of_records",   required = true)
	private Integer numberOfRec;
	
	@XmlElement(name="listOfClientAddress",   required = false)
	private ClpAddrListItem[] addressList;


	public Integer getNumberOfRec() {
		return numberOfRec;
	}

	public void setNumberOfRec(Integer numberOfRec) {
		this.numberOfRec = numberOfRec;
	}

	public ClpAddrListItem[] getRegionList() {
		return addressList;
	}

	public void setRegionList(ClpAddrListItem[] addressList) {
		this.addressList = addressList;
	}
	
	
}
