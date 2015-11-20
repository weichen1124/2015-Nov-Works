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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/PaymentListItem.java $
* 
* PF-16653, 07/23/15, DChen, remove properties from pf events.
* 
* **************************************/

package com.dds.pathfinder.pfrest.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentListItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public PaymentListItem(String paymentType, String amount) {
		super();
		this.paymentType = paymentType;
		this.amount = amount;
	}

	@JsonProperty(value = "payment-type")
	private String paymentType;
	
	@JsonProperty(value = "amount")
	private String amount;

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	

}
