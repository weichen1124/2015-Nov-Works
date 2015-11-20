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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/SysAttrListItem.java $
 * 
 * PF-15816, 15/07/14 DChen, added taxi company ID in attributes list.
 * 
 * 2     1/13/10 6:19p Dchen
 * OSP interface.
 * 
 * 1     10/21/09 2:31p Yyin
 * Added GetAttributeList method
 * 
 * 
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SysAttrItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "attrShortName","attrLongName","attrWebBooker","taxiCompanyID"})
public class PFAttrListItem {
	@XmlElement(name="attrShortName", required = false)
	String attrShortName;
	@XmlElement(name="attrLongName", required = false)
	String attrLongName;
	@XmlElement(name="attrWebBooker", required = false)
	String attrWebBooker;
	@XmlElement(name="taxi_company_id", required = false, defaultValue = "0")
	int taxiCompanyID;

	
	
	
	public PFAttrListItem() {
		super();
	}
	
	

	public PFAttrListItem(String attrShortName, String attrLongName,
			String attrWebBooker) {
		super();
		this.attrShortName = attrShortName;
		this.attrLongName = attrLongName;
		this.attrWebBooker = attrWebBooker;
	}
	
	public PFAttrListItem(SysAttrListItem sysAttr){
		super();
		if (sysAttr != null){
			this.attrShortName = sysAttr.getAttrShortName();
			this.attrLongName = sysAttr.getAttrLongName();
			this.attrWebBooker = sysAttr.getAttrWebBooker();
			this.taxiCompanyID = sysAttr.getTaxiCompanyID();
		}
	}

	public String getAttrShortName() {
		return attrShortName;
	}

	public void setAttrShortName(String attrShortName) {
		this.attrShortName = attrShortName;
	}
	public String getAttrLongName() {
		return attrLongName;
	}

	public void setAttrLongName(String attrLongName) {
		this.attrLongName = attrLongName;
	}
	public String getAttrWebBooker() {
		return attrWebBooker;
	}

	public void setAttrWebBooker(String attrWebBooker) {
		this.attrWebBooker = attrWebBooker;
	}

	public int getTaxiCompanyID() {
		return taxiCompanyID;
	}
	public void setTaxiCompanyID(int taxiCompanyID) {
		this.taxiCompanyID = taxiCompanyID;
	}
	
}
