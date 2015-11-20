/****************************************************************************
*
*		   		    Copyright (c), 2015
*					All rights reserved
*
*					DIGITAL DISPATCH SYSTEMS, INC
*					RICHMOND, BRITISH COLUMBIA
*					CANADA
*
****************************************************************************
*
*
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/Account.java $
* 
* PF-16568, 05/15/15, DChen, add node cancellation in PF side.
* 
* PF-16386, 02/13/15, DChen, create pfrest project.
* 
* **************************************/
package com.dds.pathfinder.pfrest.resources;

import java.io.Serializable;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "account-code")
	private String accountCode;	
	
	@JsonProperty(value = "account-name")
	private String accountName;
	
	@GET
	@Produces("application/json")
	public Account get(){
		return this;
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
	
	

}
