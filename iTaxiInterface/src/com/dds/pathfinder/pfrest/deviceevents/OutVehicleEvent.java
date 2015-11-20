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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/deviceevents/OutVehicleEvent.java $
* 
* PF-16605, 06/18/15, DChen, created for 2nd device.
* 
* **************************************/

package com.dds.pathfinder.pfrest.deviceevents;

import com.dds.pathfinder.pfrest.events.PFRestEvent;

public class OutVehicleEvent extends PFRestEvent {

	private static final long serialVersionUID = 1L;

	public OutVehicleEvent() {
		super();
		setDataType(PFRestEventType.PFEvent_Type_2nd_Out_Vehicle.getEventType());
	}
	
	

}
