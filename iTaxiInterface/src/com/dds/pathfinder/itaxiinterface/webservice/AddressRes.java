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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/AddressRes.java $
 * 
 * 3     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 2     8/14/09 4:20p Dchen
 * pathfinder iTaxi interface.
 * 
 * 1     8/13/09 5:46p Dchen
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
@XmlType(name = "AddressResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "taxiCoID","numberOfRec","addressList"})
public class AddressRes extends BaseRes{
		
	@XmlElement(name="taxi_co_id",   required = false)
	private Integer taxiCoID;
	
	@XmlElement(name="number_of_records",   required = false)
	private Integer numberOfRec;
	
	@XmlElement(name="address_list",   required = false)
	private AddressListItem[] addressList;

	
	public Integer getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(Integer taxiCoID) {
		this.taxiCoID = taxiCoID;
	}

	public Integer getNumberOfRec() {
		return numberOfRec;
	}

	public void setNumberOfRec(Integer numberOfRec) {
		this.numberOfRec = numberOfRec;
	}

	public AddressListItem[] getAddressList() {
		return addressList;
	}

	public void setAddressList(AddressListItem[] addressList) {
		this.addressList = addressList;
	}

}
