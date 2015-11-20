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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/NGReSynchListItem.java $
 * 
 * 1     9/14/10 2:41p Ajiang
 * C34078 - Texas Taxi Enhancements
 * 
 * 
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NGReSynchListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "taxiRideID",
						  "nGReferenceID"
						})
						
public class NGReSynchListItem {
		
	@XmlElement(name="taxiRideID", required = true)
	private Integer taxiRideID;
	
	@XmlElement(name="nGReferenceID", required = true)
	private Integer nGReferenceID;
	
	
	public Integer getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Integer taxiRideID) {
		this.taxiRideID = taxiRideID;
	}

	public Integer getNGReferenceID() {
		return nGReferenceID;
	}

	public void setNGReferenceID(Integer nGReferenceID) {
		this.nGReferenceID = nGReferenceID;
	}
}