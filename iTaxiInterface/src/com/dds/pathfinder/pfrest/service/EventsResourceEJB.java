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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/service/EventsResourceEJB.java $
* 
* PF-16428, 03/06/15, DChen, pfrest notification service.
* 
* 
* **************************************/
package com.dds.pathfinder.pfrest.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.dds.pathfinder.pfrest.events.AliveEvent;
import com.dds.pathfinder.pfrest.events.PFRestEvent;
import com.dds.pathfinder.pfrest.events.PostEvent;
import com.dds.pathfinder.pfrest.resources.GetAliveEventResponse;
import com.dds.pathfinder.pfrest.resources.ResponseResource;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class EventsResourceEJB {
	private static Logger logger = Logger.getLogger(EventsResourceEJB.class);
	
	@GET
	@Produces("application/json")
	@Path("/events/{eventType}")
	public ResponseResource getPFRestEventbyType(@PathParam("eventType") String eventType){
		ResponseResource responseGetAlive = new ResponseResource();
		if(PFRestEvent.EVENT_TYPE_PFREST_ALIVE.equalsIgnoreCase(eventType)){
			responseGetAlive.setDataResource(new GetAliveEventResponse());
		}
		return responseGetAlive;
	}
}
