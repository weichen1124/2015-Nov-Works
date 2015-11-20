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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/AdviseArrivalListItem.java $
 * 
 * 2     1/26/10 5:44p Dchen
 * OSP interface.
 * 
 * 1     11/17/09 5:31p Yyin
 * Added
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdviseArrivalListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "adviseArrival"})
public class AdviseArrivalListItem {
		
	@XmlElement(name="adviseArrival", required = false, defaultValue="")
	private String adviseArrival;

	
	
	public AdviseArrivalListItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public AdviseArrivalListItem(String adviseArrival) {
		super();
		this.adviseArrival = adviseArrival;
	}



	public String getAdviseArrival() {
		return adviseArrival;
	}

	public void setAdviseArrival(String adviseArrival) {
		this.adviseArrival = adviseArrival;
	}
}
