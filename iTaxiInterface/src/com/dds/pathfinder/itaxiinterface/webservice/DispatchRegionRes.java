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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/DispatchRegionRes.java $
 * 
 * 3     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 2     8/14/09 4:21p Dchen
 * pathfinder iTaxi interface.
 * 
 * 1     8/13/09 5:47p Dchen
 * pathfinder iTaxi interface.
 * 
 * 
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DispatchRegionResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "numberOfRec","regionList"})
public class DispatchRegionRes extends BaseRes{
	
	
	@XmlElement(name="number_of_records",   required = false)
	private Integer numberOfRec;
	
	@XmlElement(name="region_list",   required = false)
	private RegionListItem[] regionList;


	public Integer getNumberOfRec() {
		return numberOfRec;
	}

	public void setNumberOfRec(Integer numberOfRec) {
		this.numberOfRec = numberOfRec;
	}

	public RegionListItem[] getRegionList() {
		return regionList;
	}

	public void setRegionList(RegionListItem[] regionList) {
		this.regionList = regionList;
	}
	
	
}
