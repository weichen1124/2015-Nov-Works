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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/AdviseArrivalResponse.java $
 * 
 * 1     11/17/09 5:32p Yyin
 * Added
 * 
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdviseArrivalResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "numberOfRec","listOfAdviseArrival"})
public class AdviseArrivalResponse extends BaseRes{
	
	
	@XmlElement(name="number_of_records",   required = false)
	private Integer numberOfRec;
	
	@XmlElement(name="listOfAdviseArrival",   required = false)
	private AdviseArrivalListItem[] listOfAdviseArrival;


	public Integer getNumberOfRec() {
		return numberOfRec;
	}

	public void setNumberOfRec(Integer numberOfRec) {
		this.numberOfRec = numberOfRec;
	}

	public AdviseArrivalListItem[] getRegionList() {
		return listOfAdviseArrival;
	}

	public void setAdviseArrivalList(AdviseArrivalListItem[] listOfAdviseArrival) {
		this.listOfAdviseArrival = listOfAdviseArrival;
	}
	
	
}
