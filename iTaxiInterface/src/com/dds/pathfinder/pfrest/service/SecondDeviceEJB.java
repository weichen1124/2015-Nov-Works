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
* PF-16760, 09/14/15, DChen, change street hire to rejectafteraccept.
* 
* PF-16760, 09/10/15, DChen, reject for street hire.
* 
* PF-16606, 09/03/15, DChen, add some timeout in job offer.
* 
* PF-16605, 08/28/15, DChen, some changes required from MG.
* 
* PF-16607, 08/25/15, DChen, 2nd device bug fix.
* 
* PF-16607, 08/21/15, DChen, add timeout action.
* 
* PF-16607, 08/19/15, DChen, accept reject from 2nd device.
* 
* PF-16605, 06/18/15, DChen, create for 2nd device.
* 
* **************************************/
package com.dds.pathfinder.pfrest.service;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.driver.ejb.DriverLocal;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.pfrest.impl.SecDeviceImplement;
import com.dds.pathfinder.pfrest.resources.ResponseResource;
import com.dds.pathfinder.pfrest.seconddevices.DeviceResource;
import com.dds.pathfinder.pfrest.seconddevices.SecondDeviceResponse;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class SecondDeviceEJB {

	private static Logger logger = Logger.getLogger(SecondDeviceEJB.class);
	
	
	@Resource(mappedName =com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_PF_DATA_SOURCE)
    private DataSource pfDataSource;
	
	@EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + DriverLocal.JNDI_BINDING)
	private DriverLocal driverLocal;
	
	@EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
    private LoadDispatchParametersLocal cachedParam;
	
	public static final String SD_RESOURCE_ACTION_TYPE_REGISTER = "register";
	public static final String SD_RESOURCE_ACTION_TYPE_ACCEPT = "acceptjob";
	public static final String SD_RESOURCE_ACTION_TYPE_REJECT = "reject";
	public static final String SD_RESOURCE_ACTION_TYPE_TIMEOUT = "timeout";
	public static final String SD_RESOURCE_ACTION_TYPE_REJEPT = "rejectafteraccept";
	
   @GET
   @Produces("application/json")
   @Path("/2nd-device/company/{compid}/drivers/{badge}")
   public Response get2ndDevice(@Context HttpHeaders httpHeader,  @PathParam("compid") int companyID, @PathParam("badge") String badgeNumber){
	  
	   SecDeviceImplement secDeviceImplement = new SecDeviceImplement(pfDataSource, cachedParam, driverLocal);
	   
	   DeviceResource secDevice = secDeviceImplement.getDriverByBadgeNb(badgeNumber);
	   if(secDevice == null){
		   return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(null).build();
	   }else if(secDevice.getHttpStatus() == 0 || secDevice.getHttpStatus() == HttpServletResponse.SC_OK ){
		   return Response.status(HttpServletResponse.SC_OK).entity(secDevice).build();
	   }else{
		   return Response.status(secDevice.getHttpStatus()).entity(null).build();
	   }   
   }
   
   @PUT
   @Produces("application/json")
   @Path("/2nd-device/company/{compid}/drivers")
   public Response register2ndDevice(@Context HttpHeaders httpHeader, @PathParam("compid") int companyID, @QueryParam("action_type") String actionType, DeviceResource device){
	   if(SD_RESOURCE_ACTION_TYPE_REGISTER.equalsIgnoreCase(actionType)){
		   
		   SecDeviceImplement secDeviceImplement = new SecDeviceImplement(pfDataSource, cachedParam, driverLocal);
		   
		 
		   ResponseResource regResponse = secDeviceImplement.register2ndDevice(device, companyID);
		   SecondDeviceResponse devResponse = (SecondDeviceResponse) regResponse.getDataResource();
		   
		   if(devResponse != null && (devResponse.getHttpStatus() == 0 || devResponse.getHttpStatus() == HttpServletResponse.SC_OK)){
			   return Response.status(HttpServletResponse.SC_OK).entity(regResponse).build();
		   }else{
			   return Response.status(devResponse.getHttpStatus()).entity(regResponse).build();
		   }
		   
	   }else{
		   return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(null).build();
	   }
   }
   
   @PUT
   @Produces("application/json")
   @Path("/2nd-device/company/{compid}/joboffers/{jobid}")
   public Response JobOfferAction2ndDevice(@Context HttpHeaders httpHeader, @PathParam("compid") int companyID, @PathParam("jobid") long jobID, @QueryParam("action_type") String actionType, DeviceResource device){
	   if(SD_RESOURCE_ACTION_TYPE_ACCEPT.equalsIgnoreCase(actionType) ||
			   SD_RESOURCE_ACTION_TYPE_REJECT.equalsIgnoreCase(actionType) ||
			   SD_RESOURCE_ACTION_TYPE_TIMEOUT.equalsIgnoreCase(actionType) ||
			   SD_RESOURCE_ACTION_TYPE_REJEPT.equalsIgnoreCase(actionType)){
		   
		   SecDeviceImplement secDeviceImplement = new SecDeviceImplement(pfDataSource, cachedParam, driverLocal);
		   
		 
		   ResponseResource secResponse = null;
		   if(SD_RESOURCE_ACTION_TYPE_ACCEPT.equalsIgnoreCase(actionType)) secResponse = secDeviceImplement.actionJob2ndDevice(jobID, companyID, device, SecDeviceImplement.ACCEPT_JOB_SEC_DEVICE);
		   else if(SD_RESOURCE_ACTION_TYPE_TIMEOUT.equalsIgnoreCase(actionType)) secResponse = secDeviceImplement.actionJob2ndDevice(jobID, companyID, device, SecDeviceImplement.TIMEOUT_JOB_SEC_DEVICE);
		   else if(SD_RESOURCE_ACTION_TYPE_REJEPT.equalsIgnoreCase(actionType)) secResponse = secDeviceImplement.actionJob2ndDevice(jobID, companyID, device, SecDeviceImplement.REJEPT_JOB_SEC_DEVICE);
		   else secResponse = secDeviceImplement.actionJob2ndDevice(jobID, companyID, device, SecDeviceImplement.REJECT_JOB_SEC_DEVICE);
		   
		   SecondDeviceResponse devResponse = (SecondDeviceResponse) secResponse.getDataResource();
		   
		   if(devResponse != null && (devResponse.getHttpStatus() == 0 || devResponse.getHttpStatus() == HttpServletResponse.SC_OK)){
			   return Response.status(HttpServletResponse.SC_OK).entity(null).build();
		   }else{
			   return Response.status(devResponse.getHttpStatus()).entity(secResponse).build();
		   }
		   
	   }else{
		   return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(null).build();
	   }
   }

	
}
