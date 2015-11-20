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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/PredefinedMessageListRsp.java $
 * 
 * 1     1/12/10 2:28p Yyin
 * Added
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PredefinedMessageListResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "numberOfRec", "predefinedMessageList"})
public class PredefinedMessageListRsp extends BaseRes{
	@XmlElement(name="number_of_records",   required = false)
	private Integer numberOfRec;
	
	@XmlElement(name="predefinedMessageList",   required = false)
	private PredefinedMessageListItem[] predefinedMessageList;

	public Integer getNumberOfRec() {
		return numberOfRec;
	}

	public void setNumberOfRec(Integer numberOfRec) {
		this.numberOfRec = numberOfRec;
	}

	public PredefinedMessageListItem[] getPredefinedMessageList() {
		return predefinedMessageList;
	}

	public void setPredefinedMessageList(PredefinedMessageListItem[] predefinedMessageList) {
		this.predefinedMessageList = predefinedMessageList;
	}

}
