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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/service/AddressesResourceEJB.java $
* 
* PF-16819, 10/07/15, DChen, modify url for driver notes.
* 
* PF-16819, 10/05/15, DChen, added address driver notes resource.
* 
* **************************************/
package com.dds.pathfinder.pfrest.service;

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
import com.dds.pathfinder.pfrest.impl.AddressResourceImplement;
import com.dds.pathfinder.pfrest.resources.DriverNotesResource;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class AddressesResourceEJB extends BaseResourceEJB {
	
	private static Logger logger = Logger.getLogger(AddressesResourceEJB.class);
	
	@EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
    private LoadDispatchParametersLocal cachedParam;
	
	@Context
    private HttpServletResponse httpServletResponse;
	
	
	@GET
	@Produces("application/json")
	@Path("/address/company-id/{companyID}/driver-notes")
	public Response getAddressDriverNotes(@Context HttpHeaders httpHeader, @PathParam("companyID") int companyID, @QueryParam("region") String regionAbbr, @QueryParam("street-name") String streetName, @QueryParam("street-nr") String streetNumber,
							@QueryParam("unit") String unitNb){
		
	   logger.info("getAddressDriverNotes is called: companyID =" + companyID + ", region=" + regionAbbr +", street name="+ streetName+", street nb=" + streetNumber +", unit=" + unitNb );	
	   AddressResourceImplement addressResourceImplemant = new AddressResourceImplement(pfDataSource, cachedParam);
	   
	   int systemID = validSystemID(httpHeader, addressResourceImplemant);
	   if(systemID <= 0) return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(null).build();
	   
	   
	   DriverNotesResource drvNotesResource = addressResourceImplemant.getAddressDriverNotes(companyID, streetName, streetNumber, unitNb, regionAbbr);
	   
	   
	   if(drvNotesResource == null){
		   return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(null).build();
	   }else if(drvNotesResource.getHttpStatus() == 0 || drvNotesResource.getHttpStatus() == HttpServletResponse.SC_OK ){
		   return Response.status(HttpServletResponse.SC_OK).entity(drvNotesResource).build();
	   }else{
		   return Response.status(drvNotesResource.getHttpStatus()).entity(drvNotesResource).build();
	   }   
	}
	
}
