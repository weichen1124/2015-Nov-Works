/****************************************************************************
 * 
 *					Copyright (c), 2013
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 * **************************************************************************
 * File Name: TripStatusServiceLocal.java
 * C36130 Add GoFastCab Trip Status update.
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/scheduletask/TripStatusServiceLocal.java $
 * 
 * 1     3/08/13 3:31p Dchen
 * Separate rider charge service from callbooker package.
 * 
 * 
 * *****************************************************************/
package com.dds.pathfinder.itaxiinterface.scheduletask;

public interface TripStatusServiceLocal {
	public static final String JNDI_BINDING="ospitaxi_interface/TripStatusService!com.dds.pathfinder.itaxiinterface.scheduletask.TripStatusServiceLocal";
}
