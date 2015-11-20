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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/service/PFRestApiApplication.java $
* 
* 
* PF-16385, 02/13/15, DChen, create pfrest project.
* 
* **************************************/
package com.dds.pathfinder.pfrest.service;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.dds.pathfinder.itaxiinterface.util.Utilities;

@ApplicationPath("/" + Utilities.PFREST_VERSION_NUMBER)
public class PFRestApiApplication extends Application {

}
