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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/impl/PFRestImplement.java $
 * 
 * PF-16597, 06/09/15, DChen, add system id in pfrest.
 * 
 * PF-16385, 02/13/15, DChen, create pfrest project.
 * 
 * ******/

package com.dds.pathfinder.pfrest.impl;

import com.dds.pathfinder.itaxiinterface.common.impl.CommonImplement;



public interface PFRestImplement extends CommonImplement{
		public final static String PFREST_HTTP_HEADER_SYSTEM_ID = "pfrest-system-id";
		public final static String PFREST_HTTP_HEADER_PASSWORD = "pfrest-usr-passwd";
}
