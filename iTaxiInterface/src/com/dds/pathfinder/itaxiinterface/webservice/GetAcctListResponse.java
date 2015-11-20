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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GetAcctListResponse.java $
 * 
 * 3     9/20/10 12:12p Ezhang
 * OSP 2.0 interface added errorCode.
 * 
 * 2     1/13/10 6:19p Dchen
 * OSP interface.
 * 
 * 1     10/19/09 5:00p Yyin
 * Added GetAccountList method
 * 
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAcctListResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "nofAccounts","listOfAccounts","errorCode"})
public class GetAcctListResponse extends BaseRes{
	
	
	@XmlElement(name="nofAccounts",   required = false)
	private Integer nofAccounts;
	
	@XmlElement(name="listOfAccounts",   required = false)
	private MatchAccountListItem[] listOfAccounts;

	@XmlElement(name="errorCode",   required = false)
	private Integer errorCode;
	
	public Integer getNofAccounts() {
		return nofAccounts;
	}

	public void setNofAccounts(Integer nofAccounts) {
		this.nofAccounts = nofAccounts;
	}

	public MatchAccountListItem[] getListOfAccounts() {
		return listOfAccounts;
	}

	public void setListOfAccounts(MatchAccountListItem[] listOfAccounts) {
		this.listOfAccounts = listOfAccounts;
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

}
