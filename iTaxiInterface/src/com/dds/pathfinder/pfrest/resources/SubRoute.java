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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/SubRoute.java $
* 
* PF-16522, 05/19/15, DChen, if not booked by TG, send not found response.
* 
* PF-16568, 05/15/15, DChen, add node cancellation in PF side.
* 
* PF-16385, 02/13/15, DChen, create pfrest project.
* 
* **************************************/
package com.dds.pathfinder.pfrest.resources;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubRoute implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "stops")
	private ArrayList<StopPointResource> stopPoints;
	
	@JsonProperty(value = "account")
	private Account	account;
	
	public SubRoute(){
		stopPoints = new ArrayList<StopPointResource>();
		account = new Account();
	}

//	@GET
//	@Produces("application/json")
//	public SubRoute get(){
//		return this;
//	}

	public ArrayList<StopPointResource> getStopPoints() {
		return stopPoints;
	}

	public void setStopPoints(ArrayList<StopPointResource> stopPoints) {
		this.stopPoints = stopPoints;
	}


	@Produces("application/json")
	@Path("/account")
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	

}
