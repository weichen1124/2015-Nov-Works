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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/MatchAccountListItem.java $
 * 
 * 2     1/13/10 6:19p Dchen
 * OSP interface.
 * 
 * 1     10/19/09 5:00p Yyin
 * Added GetAccountList method
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchAccountListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "accountCode","accountName","accountPassword"})
public class MatchAccountListItem {
		
	@XmlElement(name="accountCode", required = true)
	private String accountCode;
	
	@XmlElement(name="accountName", required = true)
	private String accountName;
	
	@XmlElement(name="accountPassword", required = false)
	private String accountPassword;


	public MatchAccountListItem() {
		super();
	}

	public MatchAccountListItem(String accountCode, String accountName,
			String accountPassword) {
		super();
		this.accountCode = accountCode;
		this.accountName = accountName;
		this.accountPassword = accountPassword;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

}
