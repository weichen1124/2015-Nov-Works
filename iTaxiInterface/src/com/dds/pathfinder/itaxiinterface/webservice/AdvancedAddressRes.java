/****************************************************************************
 *
 *		   		    Copyright (c), 2010
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/AdvancedAddressRes.java $
 * 
 * 1     9/20/10 11:29a Ezhang
 * OSP 2.0
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdvancedAddressResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "taxiCoID","numberOfRec","addressList","errorCode"})
public class AdvancedAddressRes extends BaseRes{
		
	@XmlElement(name="taxi_co_id",   required = false)
	private Integer taxiCoID;
	
	@XmlElement(name="number_of_records",   required = false)
	private Integer numberOfRec;
	
	@XmlElement(name="address_list",   required = false)
	private AddressListItem[] addressList;

	@XmlElement(name="errorCode", required = true)
	private Integer errorCode;
	
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

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
}
