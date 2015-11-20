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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/service/VehiclesResourceEJB.java $
* 
* PF-16385, 10/08/15, DZhou, add vehicle location request
* 
* **************************************/
package com.dds.pathfinder.pfrest.service;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.callbooker.server.vehicle.ejb.VehicleLocal;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.pfrest.impl.VehicleResourceImplement;
import com.dds.pathfinder.pfrest.resources.GpsLocationResponse;
import com.dds.pathfinder.pfrest.resources.ResponseResource;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class VehiclesResourceEJB extends BaseResourceEJB {
	
	private static Logger logger = Logger.getLogger(VehiclesResourceEJB.class);
		
	@EJB(mappedName= com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + VehicleLocal.JNDI_BINDING)
	private VehicleLocal vehicleLocal;
    
	@EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
    private LoadDispatchParametersLocal cachedParam;
	
    @Context
    private HttpServletResponse httpServletResponse;
       
	   @GET
	   @Produces("application/json")
	   @Path("/vehicles/{callsign}/location")
	   public Response getVehicleLocationByCallsign(@Context HttpHeaders httpHeader, @PathParam("callsign") String callsign, @QueryParam("date-greater-than") String strDateGreaterThan){
		   VehicleResourceImplement vehicleResourceImplemant = new VehicleResourceImplement(vehicleLocal, pfDataSource, cachedParam);
		   
		   int systemID = validSystemID(httpHeader, vehicleResourceImplemant);
		   if(systemID <= 0) return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(null).build();
		   else vehicleResourceImplemant.setSystemID(systemID);
		   
		   ResponseResource responseRes = vehicleResourceImplemant.getGpsLocationRespByCallsign(callsign, strDateGreaterThan);
		   
		   GpsLocationResponse  gpsLocResp = (GpsLocationResponse)responseRes.getDataResource();
		   
		   
		   if(gpsLocResp != null && gpsLocResp.getGpsLocations() != null && gpsLocResp.getGpsLocations().size() > 0){
			   return Response.status(gpsLocResp.getHttpStatus()).entity(responseRes).build();
		   }else{
			   return Response.status(HttpServletResponse.SC_NOT_FOUND).entity(null).build();
		   }
	   }
}
