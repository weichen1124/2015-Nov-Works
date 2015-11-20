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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GetAttrListResponse.java $
 * 
 * 3     9/20/10 12:11p Ezhang
 * 
 * 2     1/13/10 6:19p Dchen
 * OSP interface.
 * 
 * 1     10/21/09 2:30p Yyin
 * Added GetAttributeList method
 * 
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAttrListResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "nofAttributes","listOfAttributes", "errorCode"})
public class GetAttrListResponse extends BaseRes{
	
	
	@XmlElement(name="nofAttributes",   required = false)
	private Integer nofAttributes;
	
	@XmlElement(name="listOfAttributes",   required = false)
	private PFAttrListItem[] listOfAttributes;

	@XmlElement(name="errorCode",   required = false)
	private Integer errorCode;
	
	public Integer getNofAttributes() {
		return nofAttributes;
	}

	public void setNofAttributes(Integer nofAttributes) {
		this.nofAttributes = nofAttributes;
	}

	public PFAttrListItem[] getListOfAttributes() {
		return listOfAttributes;
	}

	public void setListOfAttributes(PFAttrListItem[] listOfAttributes) {
		this.listOfAttributes = listOfAttributes;
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	

}
