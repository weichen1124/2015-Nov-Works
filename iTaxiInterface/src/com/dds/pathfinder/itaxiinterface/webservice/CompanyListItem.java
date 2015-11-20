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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/CompanyListItem.java $
 * 
 * 2     1/19/10 4:50p Dchen
 * OSP interface.
 * 
 * 1     12/08/09 12:46p Yyin
 * Changed type of authCompList.
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { 
		    "taxiCoID" })
public class CompanyListItem {
	
	@XmlElement(name="taxi_company_id", required = true)
	private Integer taxiCoID;

	
	
	
	public CompanyListItem() {
		super();
	}
	
	

	public CompanyListItem(Integer taxiCoID) {
		super();
		this.taxiCoID = taxiCoID;
	}



	public Integer getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(Integer taxiCoID) {
		this.taxiCoID = taxiCoID;
	}
	
}
