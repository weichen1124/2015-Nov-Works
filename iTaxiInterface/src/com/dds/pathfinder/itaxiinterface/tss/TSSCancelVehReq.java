/****************************************************************************
 *
 *		   		    Copyright (c), 2014
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/tss/TSSCancelVehReq.java $
 * 
 * 08/25/14, DChen, create TSS project.
 * 
 */

package com.dds.pathfinder.itaxiinterface.tss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSSCancelVehReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = {"taxiCoID",
						  "tssID"})
public class TSSCancelVehReq {
	
	@XmlElement(name="company_id", required = true )
	private Integer taxiCoID;
	
	@XmlElement(name="tss_id", required = true)
	private Integer tssID;

	public Integer getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(Integer taxiCoID) {
		this.taxiCoID = taxiCoID;
	}

	public Integer getTssID() {
		return tssID;
	}

	public void setTssID(Integer tssID) {
		this.tssID = tssID;
	}
	
	
}
