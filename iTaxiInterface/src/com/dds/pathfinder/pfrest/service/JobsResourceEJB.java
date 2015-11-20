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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/service/JobsResourceEJB.java $
* 
* PF-16616, 06/22/15, DChen, add get job by external job id.
* 
* PF-16597, 06/15/15, DChen, check empty for system id.
* 
* PF-16597, 06/09/15, DChen, add system id in pfrest.
* 
* PF-16522, 05/19/15, DChen, if not booked by TG, send not found response.
* 
* PF-16568, 05/15/15, DChen, add node cancellation in PF side.
* 
* PF-16539, 05/01/15, DChen, move cancel job from delete to put.
* 
* PF-16539, 04/29/15, DChen, add delete job resources.
* 
* PF-16385, 02/13/15, DChen, create pfrest project.
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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

import com.dds.pathfinder.callbooker.server.account.user.ejb.UserAccountLocal;
import com.dds.pathfinder.callbooker.server.jobsearch.ejb.JobSearchLocal;
import com.dds.pathfinder.callbooker.server.order.ejb.OrderLocal;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.callbooker.server.telephone.ejb.UserTelephoneLocal;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.pfrest.impl.JobResourceImplement;
import com.dds.pathfinder.pfrest.resources.JobResource;
import com.dds.pathfinder.pfrest.resources.PFResourceData.PFDataType;
import com.dds.pathfinder.pfrest.resources.PostJobResponse;
import com.dds.pathfinder.pfrest.resources.ResponseResource;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class JobsResourceEJB extends BaseResourceEJB {
	
	private static Logger logger = Logger.getLogger(JobsResourceEJB.class);
	
	@EJB(mappedName= com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + UserAccountLocal.JNDI_BINDING)
	private UserAccountLocal userAccountLocal;
    
    @EJB(mappedName= com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + JobSearchLocal.JNDI_BINDING)
    private JobSearchLocal jobSearchLocal;
    
    @EJB(mappedName= com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + OrderLocal.JNDI_BINDING)
    private OrderLocal orderLocal;
    
    @EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
    private LoadDispatchParametersLocal cachedParam;
    
    @EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + UserTelephoneLocal.JNDI_BINDING)
    private UserTelephoneLocal telephoneLocal;
    
    @Context
    private HttpServletResponse httpServletResponse;
    
    public static final String UPDATE_JOB_RESOURCE_TYPE_CANCEL = "cancel";
    
    
	
	   @GET
	   @Produces("application/json")
	   @Path("/jobs/{id}")
	   public Response getJobbyID(@Context HttpHeaders httpHeader,  @PathParam("id") long id) {
		   
		   JobResourceImplement jobResourceImplemant = new JobResourceImplement(jobSearchLocal, pfDataSource, orderLocal, cachedParam, userAccountLocal);
		   int systemID = validSystemID(httpHeader, jobResourceImplemant);
		   if(systemID <= 0) return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(null).build();
		   else jobResourceImplemant.setSystemID(systemID);
		   
		   JobResource jobResource = jobResourceImplemant.getJobResourceByID(id);
		   if(jobResource == null){
			   return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(null).build();
		   }else if(jobResource.getHttpStatus() == 0 || jobResource.getHttpStatus() == HttpServletResponse.SC_OK ){
			   return Response.status(HttpServletResponse.SC_OK).entity(jobResource).build();
		   }else{
			   return Response.status(jobResource.getHttpStatus()).entity(jobResource).build();
		   }   
		   // return jobResourceImplemant.getJobResourceByID(id);
	   }
	   
	   @GET
	   @Produces("application/json")
	   @Path("/jobs/")
	   public Response getJobbyQueryParameters(@Context HttpHeaders httpHeader,  @QueryParam("external-job-id") String externalJobID) {
		   
		   JobResourceImplement jobResourceImplemant = new JobResourceImplement(jobSearchLocal, pfDataSource, orderLocal, cachedParam, userAccountLocal);
		   int systemID = validSystemID(httpHeader, jobResourceImplemant);
		   if(systemID <= 0) return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(null).build();
		   else jobResourceImplemant.setSystemID(systemID);
		   
		   JobResource jobResource = jobResourceImplemant.getJobResourceByExternalJobID(externalJobID);
		   if(jobResource == null){
			   return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(null).build();
		   }else if(jobResource.getHttpStatus() == 0 || jobResource.getHttpStatus() == HttpServletResponse.SC_OK ){
			   return Response.status(HttpServletResponse.SC_OK).entity(jobResource).build();
		   }else{
			   return Response.status(jobResource.getHttpStatus()).entity(jobResource).build();
		   }   
		   // return jobResourceImplemant.getJobResourceByID(id);
	   }
	   
	   @POST
	   @Produces("application/json")
	   @Path("/jobs")
	   public Response postJobResource(@Context HttpHeaders httpHeader, JobResource job){
		   JobResourceImplement jobResourceImplemant = new JobResourceImplement(jobSearchLocal, pfDataSource, orderLocal, cachedParam, userAccountLocal);
		   
		   int systemID = validSystemID(httpHeader, jobResourceImplemant);
		   if(systemID <= 0) return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(null).build();
		   else jobResourceImplemant.setSystemID(systemID);

		   ResponseResource postResponse = jobResourceImplemant.postJobResource(job);
		   PostJobResponse jobResponse = (PostJobResponse) postResponse.getDataResource();
		   httpServletResponse.addHeader("Location", Utilities.PFREST_CONTEXT_ROOT + PFDataType.PFData_Type_Jobs.getLinkPath());
		   
		   return Response.status( (jobResponse != null && jobResponse.getHttpStatus() == 0) ? HttpServletResponse.SC_OK : jobResponse.getHttpStatus())
				   .entity(postResponse)
				   .build();
	   }
	   
	   @PUT
	   @Produces("application/json")
	   @Path("/jobs/{id}")
	   public Response updateJobResource(@Context HttpHeaders httpHeader, @PathParam("id") long id, @QueryParam("change_type") String changeType){
		   if(UPDATE_JOB_RESOURCE_TYPE_CANCEL.equalsIgnoreCase(changeType)){
			   return deleteJobResourceByID(id, httpHeader);
		   }else{
			   return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(null).build();
		   }
	   }
	   
	   @PUT
	   @Produces("application/json")
	   @Path("/jobs/{jobid}/route/stops/{sp_index:\\d[,\\d{1,2}]*}")
	   public Response updateStopPointResources(@Context HttpHeaders httpHeader, @PathParam("jobid") long jobID, @PathParam("sp_index") String spIndexes, @QueryParam("change_type") String changeType){
		   if(UPDATE_JOB_RESOURCE_TYPE_CANCEL.equalsIgnoreCase(changeType)){
			   
			   JobResourceImplement jobResourceImplemant = new JobResourceImplement(jobSearchLocal, pfDataSource, orderLocal, cachedParam, userAccountLocal);
			   
			   int systemID = validSystemID(httpHeader, jobResourceImplemant);
			   if(systemID <= 0) return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(null).build();
			   else jobResourceImplemant.setSystemID(systemID);
			   
			   ResponseResource nodeCancel = jobResourceImplemant.cancelStopPoints(jobID, spIndexes);
			   PostJobResponse jobResponse = (PostJobResponse) nodeCancel.getDataResource();
			   
			   if(jobResponse != null && (jobResponse.getHttpStatus() == 0 || jobResponse.getHttpStatus() == HttpServletResponse.SC_OK)){
				   return Response.status(HttpServletResponse.SC_OK).entity(null).build();
			   }else{
				   return Response.status(jobResponse.getHttpStatus()).entity(jobResponse).build();
			   }
			   
		   }else{
			   return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(null).build();
		   }
	   }
	   
	   @GET
	   @Produces("application/json")
	   @Path("/jobs/{jobid}/route")
	   public Response getJobSubRouteByJobID(@Context HttpHeaders httpHeader, @PathParam("jobid") long id){
		   JobResourceImplement jobResourceImplemant = new JobResourceImplement(jobSearchLocal, pfDataSource, orderLocal, cachedParam, userAccountLocal);
		   
		   int systemID = validSystemID(httpHeader, jobResourceImplemant);
		   if(systemID <= 0) return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(null).build();
		   else jobResourceImplemant.setSystemID(systemID);
		   
		   JobResource jobResource = jobResourceImplemant.getJobResourceByID(id);
		   if(jobResource.getDataResource() != null){
			   return Response.status(jobResource.getHttpStatus()).entity(jobResource.getDataResource().getSubRoute()).build();
		   }else{
			   return Response.status(HttpServletResponse.SC_NOT_FOUND).entity(null).build();
		   }
	   }
	   
	   @GET
	   @Produces("application/json")
	   @Path("/jobs/{jobid}/route/stops")
	   public Response getJobStopPointsByJobID(@Context HttpHeaders httpHeader, @PathParam("jobid") long id){
		   JobResourceImplement jobResourceImplemant = new JobResourceImplement(jobSearchLocal, pfDataSource, orderLocal, cachedParam, userAccountLocal);
		   
		   int systemID = validSystemID(httpHeader, jobResourceImplemant);
		   if(systemID <= 0) return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(null).build();
		   else jobResourceImplemant.setSystemID(systemID);
		   
		   JobResource jobResource = jobResourceImplemant.getJobResourceByID(id);
		   if(jobResource.getDataResource() != null){
			   return Response.status(jobResource.getHttpStatus()).entity(jobResource.getDataResource().getSubRoute().getStopPoints()).build();
		   }else{
			   return Response.status(HttpServletResponse.SC_NOT_FOUND).entity(null).build();
		   }
	   }
	   
	   
	   
	   
//	   @DELETE
//	   @Produces("application/json")
//	   @Path("/jobs/{id}")
	   private Response deleteJobResourceByID(long id, HttpHeaders httpHeader){
		   JobResourceImplement jobResourceImplemant = new JobResourceImplement(jobSearchLocal, pfDataSource, orderLocal, cachedParam, userAccountLocal);
		   
		   int systemID = validSystemID(httpHeader, jobResourceImplemant);
		   if(systemID <= 0) return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(null).build();
		   else jobResourceImplemant.setSystemID(systemID);

		   ResponseResource deleteResponse = jobResourceImplemant.deleteJobResourceByID(id);
		   
		   
		   PostJobResponse jobResponse = (PostJobResponse) deleteResponse.getDataResource();
		   httpServletResponse.addHeader("Location", Utilities.PFREST_CONTEXT_ROOT + PFDataType.PFData_Type_Jobs.getLinkPath());
		   
		   if(jobResponse != null && (jobResponse.getHttpStatus() == 0 || jobResponse.getHttpStatus() == HttpServletResponse.SC_OK)){
			   return Response.status(HttpServletResponse.SC_OK).entity("").build();
		   }else{
			   return Response.status(jobResponse.getHttpStatus()).entity(jobResponse).build();
		   }
		   
	   }
	   
//	   @DELETE
//	   @Produces("application/json")
//	   @Path("/jobs/reference_nb/{id}")
	   private Response deleteJobResourceByExternalID(String externalID){
		   JobResourceImplement jobResourceImplemant = new JobResourceImplement(jobSearchLocal, pfDataSource, orderLocal, cachedParam, userAccountLocal);

		   ResponseResource deleteResponse = jobResourceImplemant.deleteJobResourceByExternalID(externalID);
		   
		   
		   PostJobResponse jobResponse = (PostJobResponse) deleteResponse.getDataResource();
		   httpServletResponse.addHeader("Location", Utilities.PFREST_CONTEXT_ROOT + PFDataType.PFData_Type_Jobs.getLinkPath());
		   
		   return Response.status( (jobResponse != null && jobResponse.getHttpStatus() == 0) ? HttpServletResponse.SC_OK : jobResponse.getHttpStatus())
				   .entity(jobResponse)
				   .build();
	   }
}
