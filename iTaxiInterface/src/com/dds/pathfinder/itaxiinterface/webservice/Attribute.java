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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/Attribute.java $
 * 
 * 3     1/22/10 4:52p Dchen
 * OSP interface.
 * 
 * 2     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 1     9/25/09 5:44p Dchen
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
@XmlType(name = "AttributeType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "attrShortName"})
public class Attribute{
	
	@XmlElement(name="attrShortName", required = false)
	String attrShortName;

	
	
	public Attribute() {
		super();
	}
	
	

	public Attribute(String attrShortName) {
		super();
		this.attrShortName = attrShortName;
	}



	public String getAttrShortName() {
		return attrShortName;
	}

	public void setAttrShortName(String attrShortName) {
		this.attrShortName = attrShortName;
	}
}
