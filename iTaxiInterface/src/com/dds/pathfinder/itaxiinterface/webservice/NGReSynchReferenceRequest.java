/****************************************************************************
 *
 *                            Copyright (c), 2009
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/NGReSynchReferenceRequest.java $
 * 
 * 1     9/14/10 2:41p Ajiang
 * C34078 - Texas Taxi Enhancements
 *
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NGReSynchReferenceReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "nofRef", 
						  "RefList" 
						  })
public class NGReSynchReferenceRequest extends BaseReq{
	
	@XmlElement(name="nofRef", required = true )
	private Integer nofRef;
	
	@XmlElement(name="RefList", required = true )
	private NGReSynchListItem[] RefList;
	
	public Integer getNofRef() {
		return nofRef;
	}
	
	public void setNofRef(Integer nofRef) {
		this.nofRef = nofRef;
	}
	
	public NGReSynchListItem[] getRefList() {
		return RefList;
	}

	public void setRefList(NGReSynchListItem[] RefList) {
		this.RefList = RefList;
	}
}