/****************************************************************************
*
*		   		    Copyright (c), 2015
*					All rights reserved
*
*					DIGITAL DISPATCH SYSTEMS, INC
*					RICHMOND, BRITISH COLUMBIA
*					CANADA
*
****************************************************************************
*
*
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/PostEvent.java $
* 
* PF-16774, 09/30/15, DChen, modify to mask pickup address.
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import java.io.Serializable;

public class SecDevAddrFormatEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reqPickupDTM;
    private String addressMask; 
    private int areaIDOfrMask;
        
    
	public SecDevAddrFormatEvent() {
		super();
	}


	public SecDevAddrFormatEvent(String reqPickupDTM, String addressMask,
			int areaIDOfrMask) {
		super();
		this.reqPickupDTM = reqPickupDTM;
		this.addressMask = addressMask;
		this.areaIDOfrMask = areaIDOfrMask;
	}
	
	
	public String getReqPickupDTM() {
		return reqPickupDTM;
	}
	public void setReqPickupDTM(String reqPickupDTM) {
		this.reqPickupDTM = reqPickupDTM;
	}
	public String getAddressMask() {
		return addressMask;
	}
	public void setAddressMask(String addressMask) {
		this.addressMask = addressMask;
	}
	public int getAreaIDOfrMask() {
		return areaIDOfrMask;
	}
	public void setAreaIDOfrMask(int areaIDOfrMask) {
		this.areaIDOfrMask = areaIDOfrMask;
	} 
    
    
    
	
}
