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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/impl/JobResourceImplement.java $
 * 
 * PF-16841, 10/15/15, DChen, add percentage after building name.
 * 
 * PF-16838, 10/13/15, DChen, add pickup address held job checking.
 * 
 * PF-16776, 09/16/15, DChen, add pickup address hold flag checking.
 * 
 * PF-16772, 09/15/15, DChen, add unit match in OSP als.
 * 
 * PF-16653, 07/17/15, DChen, add event type for TG.
 * 
 * PF-16615, 06/23/15, DChen, add operator notes in stop point.
 * 
 * PF-16616, 06/22/15, DChen, add get job by external job id.
 * 
 * PF-16614, 06/19/15, DChen, add suti particular stop points resource.
 * 
 * PF-16597, 06/09/15, DChen, add system id in pfrest.
 * 
 * PF-16580, 06/03/15, DChen, add customer number in stop points.
 * 
 * PF-16554, 05/19/15, DChen, check status before cancel job.
 * 
 * PF-16522, 05/19/15, DChen, if not booked by TG, send not found response.
 * 
 * PF-16568, 05/15/15, DChen, add node cancellation in PF side.
 * 
 * PF-16539, 05/01/15, DChen, move cancel job from delete to put.
 * 
 * PF-16515, 04/29/15, DChen, validate attribute before post job.
 * 
 * PF-16514, 04/29/15, DChen, null telephone nb invalid tariff nb.
 * 
 * PF-16385, 04/02/15, DChen, add tariff number to job.
 * 
 * PF-16428, 03/25/15, DChen, pfrest job events service.
 * 
 * PF-16428, 03/13/15, DChen, based on json-api format
 * 
 * PF-16428, 03/06/15, DChen, add pfrest notification service.
 * 
 * PF-16385, 03/03/15, DChen, add pfrest job reference.
 * 
 * PF-16385, 02/13/15, DChen, create pfrest project.
 * 
 * ******/

package com.dds.pathfinder.pfrest.impl;

import java.rmi.RemoteException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.account.user.ejb.UserAccount;
import com.dds.pathfinder.callbooker.server.account.user.ejb.UserAccountLocal;
import com.dds.pathfinder.callbooker.server.account.user.model.UserAccountItem;
import com.dds.pathfinder.callbooker.server.address.ejb.AddressDAO2;
import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartModel;
import com.dds.pathfinder.callbooker.server.cart.model.StopPoint;
import com.dds.pathfinder.callbooker.server.controller.event.CartEvent;
import com.dds.pathfinder.callbooker.server.jobsearch.ejb.JobSearch;
import com.dds.pathfinder.callbooker.server.order.ejb.Order;
import com.dds.pathfinder.callbooker.server.order2.ejb.OrderDAO2;
import com.dds.pathfinder.callbooker.server.order2.model.JobStatusItem;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;
import com.dds.pathfinder.callbooker.server.telephone.ejb.UserTelephone;
import com.dds.pathfinder.callbooker.server.util.ContactInformation;
import com.dds.pathfinder.itaxiinterface.common.impl.CallbookerCartImplement;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;
import com.dds.pathfinder.pfrest.resources.Account;
import com.dds.pathfinder.pfrest.resources.Address;
import com.dds.pathfinder.pfrest.resources.Job;
import com.dds.pathfinder.pfrest.resources.JobResource;
import com.dds.pathfinder.pfrest.resources.Passenger;
import com.dds.pathfinder.pfrest.resources.PostJobIDReference;
import com.dds.pathfinder.pfrest.resources.PostJobResponse;
import com.dds.pathfinder.pfrest.resources.ResponseResource;
import com.dds.pathfinder.pfrest.resources.StopPointResource;
import com.dds.pathfinder.pfrest.resources.SubRoute;

public class JobResourceImplement extends CallbookerCartImplement {
	
	private static Logger logger = Logger.getLogger(JobResourceImplement.class);
	
	private JobSearch jobSearch;
	private DataSource pfDataSource;
	private Order order;
	private int systemID;
	
	protected UserAccount userAccount = null;
	protected UserTelephone userTelephone = null;
	
	public final static String NOT_FOUND_JOB_REF = "not found that job";
	public final static String ATTR_NAME_INPUT_ATTR_CHECK="input-attributes-validate";
	
	public final static String CB_TRIP_ATTRIBUTE_HELDJOB="Held job";
	public final static String CB_JOB_TYPE_HELDJOB = "HELD JOB";
	
	public JobResourceImplement(JobSearch jobSearch, DataSource pfDataSource,Order order, LoadDispatchParametersLocal cachedParam, UserAccountLocal userAccount) {
		super();
		this.jobSearch = jobSearch;
		this.pfDataSource = pfDataSource;
		this.order = order;
		setCachedParam(cachedParam);
		this.userAccount = userAccount;
		this.systemID = -1;
	}


	
	public JobResource getJobResourceByExternalJobID(String externalJobID){
		
		long jobID = getJobIDFromExternalJobID(Utilities.upcaseString(externalJobID));
		if(jobID <= 0){
			JobResource jobResource = new JobResource();
			jobResource.setHttpStatus(HttpServletResponse.SC_NOT_FOUND);
			return jobResource;
		}else{
			return getJobResourceByID(jobID);
		}
		
	}

	public JobResource getJobResourceByID(long jobID){
		JobResource jobResource = new JobResource();
		if(jobSearch == null) {
			jobResource.setHttpStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return jobResource;
		}
		if(NOT_FOUND_JOB_REF.equals(getExternalJobIDByJobID(jobID))){
			jobResource.setHttpStatus(HttpServletResponse.SC_NOT_FOUND);
			return jobResource;
		}
		try{	
			CartModel jobs =  jobSearch.getModifyJobDetails_EntityEJB("", "", null, "" + jobID, CartEvent.ACTIONTYPE_JOB_RETRIEVAL);
			if(jobs != null && jobs.getSize() > 0){
				CartItem cart = jobs.getItemAt(0);
				convertCart2JobResource(cart, jobResource, jobID);
				jobResource.setHttpStatus(HttpServletResponse.SC_OK);
			}else{
				jobResource.setHttpStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		}catch(Exception e){
			logger.error("generateJobsResponse failed..");
		}
		return jobResource;
		
	}
	
	public void setUserTelephone(UserTelephone userTelephone) {
		this.userTelephone = userTelephone;
	}


	public UserTelephone getUserTelephone() {
		return userTelephone;
	}
	
	public ResponseResource deleteJobResourceByExternalID(String externalID){
		return deleteJobResourceByID(getJobIDFromExternalJobID(externalID));
	}
	
	public ResponseResource cancelStopPoints(long jobID, String spIndexes){
		PostJobResponse response = new PostJobResponse();
		response.setErrorMessage(ResponseResource.ERROR_CODE_FAILED);
		response.setResponseCode(ResponseResource.STATUS_FAILED);
		response.setHttpStatus(HttpServletResponse.SC_BAD_REQUEST);
		if(jobID <= 0){
			response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_JOB_ID);
			return new ResponseResource(response);
		}	
		
		try {
			CartModel jobs = jobSearch.getModifyJobDetails_EntityEJB("", "", null, "" + jobID, CartEvent.ACTIONTYPE_JOB_RETRIEVAL);
			CartItem cart = null;
			if(jobs != null && jobs.getSize() > 0){
				cart = jobs.getItemAt(0);
			}
			if(cart != null){
				if(validateInputSpIndex(cart, spIndexes) ){
					
					if(order.modifyStopPoints(getLogonCode(), jobID, cart, "TG node cancellation stops :" + spIndexes)){
						response.setResponseCode(ResponseResource.STATUS_SUCCESS);
						response.setErrorMessage(ResponseResource.ERROR_CODE_SUCCESS);
						response.setHttpStatus(HttpServletResponse.SC_OK);
					}else{
						logger.info("PF modify stop points failed.");
						response.setHttpStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
				}
				
			}else{
				return setNotFoundJobResponse(response);
			}
		} catch (RemoteException e) {
			logger.error("cancelStopPoints failed",e);
			response.setHttpStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return new ResponseResource(response);
	}
	
	private boolean validateInputSpIndex(CartItem cart, String spIndexes){
		if(cart == null || spIndexes == null || spIndexes.trim().length() == 0) return false;
		Vector<StopPoint> stops = cart.getStopPoints();
		if(stops == null || stops.size() == 0) return false;
		String[] stopIndexes = spIndexes.split(",");
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(String sIndex : stopIndexes){
			try{
				int idx = Integer.parseInt(sIndex);
				if(idx < 0 || idx >= stops.size()) return false;		//bad index
				
				indexes.add(idx);
			}catch(NumberFormatException ne){
				logger.error("parse stop points index failed: " + sIndex);
			}
		}
		
		if(indexes.size() == 0 || indexes.size() == stops.size()) return false;		//can't cancel all stops
		else{
			boolean allStopsCancelled = true;
			for(int i=0; i< stops.size(); i++){
				if(indexes.contains(i)){
					cart.getStopPoint(i).setCancelled(true);
				}
				if(allStopsCancelled && !cart.getStopPoint(i).isCancelled()){
					allStopsCancelled = false;
				}
			}
			return (!allStopsCancelled);				//can's cancel all stops
		}
	}
	
	
	public ResponseResource setNotFoundJobResponse(PostJobResponse response){
		if(response == null) response = new PostJobResponse();
		response.setErrorMessage(ResponseResource.ERROR_CODE_NOT_FIND_ANYTHING);
		response.setResponseCode(ResponseResource.STATUS_FAILED);
		response.setHttpStatus(HttpServletResponse.SC_NOT_FOUND);
		return new ResponseResource(response);
	}
	
	
	public ResponseResource deleteJobResourceByID(long jobID){
		
		PostJobResponse response = new PostJobResponse();
		response.setErrorMessage(ResponseResource.ERROR_CODE_FAILED);
		response.setResponseCode(ResponseResource.STATUS_FAILED);
		response.setHttpStatus(HttpServletResponse.SC_BAD_REQUEST);
		if(jobID <= 0){
			response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_JOB_ID);
			return new ResponseResource(response);
		}	  
		   	   
	   Connection con = null;
	   CallableStatement cs = null;
		  
	   String externalJobID = getExternalJobIDByJobID(jobID); 
	   if(NOT_FOUND_JOB_REF.equals(externalJobID)) {
		   response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_JOB_ID);
		   return new ResponseResource(response);
	   }
	   
	   if(!isValidJobStatusToCancel(jobID)){
		   response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_JOB_STATUS);
		   return new ResponseResource(response);
	   }
		   
	   try{
       	con = pfDataSource.getConnection();

       	cs = con.prepareCall("{ call despatch.cancel(?,?,?,?,?)}");
       	cs.setLong(1, jobID);
       	cs.setString(2, "Cancel Job by PFREST");       	   
  	    cs.setNull(3, Types.VARCHAR);		//log_classes.log_type%TYPE DEFAULT NULL,
  	    cs.setString(4, "N");  				//p_is_redesp IN VARCHAR2 DEFAULT 'N',
  	    cs.setString(5, getLogonCode());
       	cs.execute();
       	
       
       	response.setErrorMessage(ResponseResource.ERROR_CODE_SUCCESS);
	    
       	setDeleteJobResponse(jobID, externalJobID, response);
		
       }catch(SQLException se){
	       	logger.error("job cancellation failed with exception", se);
	       	response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_JOB_ID);
	       	
       }finally{
	       	if(cs != null) try{cs.close();}catch(SQLException ignore){};
	       	if(con != null) try{con.close();}catch(SQLException ignore){};
       }
       
	   return new ResponseResource(response);
	}
	
	public static final String JOB_STAGE_UNDER_DISPATCH = "W";
	public static final String JOB_STAGE_VEHICLE        = "V";
    public static final String JOB_STAGE_COMPLETED      = "C";
	public static final String VEHICLE_STATUS_OFFERING  = "O";
	public static final String VEHICLE_STATUS_ACCEPTED  = "Y";
	public static final String VEHICLE_STATUS_ARRIVED   = "A";
	public static final String VEHICLE_STATUS_POB       = "P";
	
	private boolean isValidJobStatusToCancel(long jobID){
		Connection con = null;
		JobStatusItem jsItem = null;
		try{
			con = pfDataSource.getConnection();
			jsItem = OrderDAO2.getJobStatus(con, (int) jobID);
		}catch(SQLException se){
			logger.error("getJobStatus failed with sql exception: ", se);
		}finally{
			if(con != null) try{con.close();}catch(SQLException ignore){};
		}
		
		if(jsItem == null) return true;
		
		String jobStage = jsItem.getStatusAbbr();
		String prevStage = jsItem.getPrevStage();
		String vehicleStatus = jsItem.getVehStatusAbbr();
		
		logger.info("cancel job " + jobID +", job stage, prev, veh status =" + jobStage+", " + prevStage +", " + vehicleStatus);

		// Can not scrub if job in POB, Complete, or on hold (but complete)
		if( jobStage != null
				&& (jobStage.startsWith(JOB_STAGE_VEHICLE) && VEHICLE_STATUS_POB.equalsIgnoreCase(vehicleStatus))
				|| (CartItem.JOB_STATE_HOLD.equalsIgnoreCase(jobStage) && JOB_STAGE_COMPLETED.equalsIgnoreCase(prevStage))
				|| JOB_STAGE_COMPLETED.equalsIgnoreCase(jobStage) ) return false;
		else return true;
	}
	
	
	
	public ResponseResource postJobResource(JobResource jobResource){
		
		PostJobResponse response = new PostJobResponse();
		response.setErrorMessage(ResponseResource.ERROR_CODE_FAILED);
		response.setResponseCode(ResponseResource.STATUS_FAILED);
		upcaseJobResource(jobResource);
		
		if(!isValidPostJobSource(jobResource, response)){
			return new ResponseResource(response);
		}
		 
		Job job = (Job) jobResource.getDataResource();       //jobResource.getJob();
		logger.info("get post job resource:  " + job.getTaxiCompanyID());
		
		ArrayList<PFAddressResponse> pickupAddresses = new ArrayList<PFAddressResponse>();
		ArrayList<PFAddressResponse> setdownAddresses = new ArrayList<PFAddressResponse>();
		ArrayList<PFAddressResponse> closestAddresses = new ArrayList<PFAddressResponse>();
		
		boolean forcedAddress = validateRequestAddresses(job.getSubRoute().getStopPoints(), job.getTaxiCompanyID(), pickupAddresses, setdownAddresses, closestAddresses);
		
//		Address pickup = job.getSubRoute().getStopPoints().get(0).getPickupAddress();
//		Address dropDown = job.getSubRoute().getStopPoints().get(0).getDropoffAddress();
//		
//		PFAddressResponse pickupAddress = getWSAddressData(
//				   appendPercentage(pickup.getStreetName(), false), 
//				   appendPercentage(pickup.getStreetNumber(), false),
//				   appendPercentage(pickup.getUnitNumber(), true), //except for unit here, we want to use pe/rcentage to match
//				   appendPercentage(pickup.getRegion(), false), 
//				   appendPercentage(pickup.getLandmark(), false), 
//				   PERCENTAGE_CHAR, PERCENTAGE_CHAR,  //organization and postal code
//				   job.getTaxiCompanyID(), "");
//		   
//		   if(!isValidAddressResponse(pickupAddress) && isBothLandMarkAndAddressProvided(pickup.getStreetName(), pickup.getStreetNumber(), pickup.getLandmark())){
//			   pickupAddress = getWSAddressData(
//					   appendPercentage(pickup.getStreetName(), false), 
//					   appendPercentage(pickup.getStreetNumber(), false),
//					   appendPercentage(pickup.getUnitNumber(), true), //except for unit here, we want to use percentage to match
//					   appendPercentage(pickup.getRegion(), false), 
//					   PERCENTAGE_CHAR, PERCENTAGE_CHAR, PERCENTAGE_CHAR,  //organization and postal code
//					   job.getTaxiCompanyID(), "");
//			   
//		   }
//
//		   PFAddressResponse closestAddress = getWSClosestAddressData(pickup.getLatitude(), pickup.getLongitude());
//
//		   //Note: not appending any percentage to end of string here. 
//		   //If there is no match, drop-off address will be used as-is in insertJob() below.
//		   //(i.e. forced address, a.k.a exactLocation in StopPointItem)
//		   PFAddressResponse setdownAddress = getWSAddressData(
//				   appendPercentage(dropDown.getStreetName(), false), 
//				   appendPercentage(dropDown.getStreetNumber(), false), 
//				   appendPercentage(dropDown.getUnitNumber(), false), 
//				   appendPercentage(dropDown.getRegion(),false),
//				   appendPercentage(dropDown.getLandmark(), false),
//				   PERCENTAGE_CHAR, PERCENTAGE_CHAR, //organization and postal code
//				   job.getTaxiCompanyID(), "");
		   
		   insertJobResource(job, response, pickupAddresses, closestAddresses, setdownAddresses, forcedAddress); 	//return create job response
		   
		   // C36130 update job_references table if the external job is created successfully
//		   if(job_origin != null && !job_origin.isEmpty()  
//				   && !job_origin.equalsIgnoreCase(ExternalSystemId.SYSTEM_ID_WEB_BOOKER.getReference()) //PF-14809 webbooker job doesn't go to this table
//				   && response.getRequestStatus() == GenErrMsgRes.STATUS_SUCCESS){
//			   insertExternalJobReference(response.getTaxiRideID());
//			   //insert more if it's multiple job booking
//			   if(response.getNbOfJobs()> 1){
//				   TaxiJob[] list = response.getTaxiJobIDList();
//				   for(int i = 0; i< response.getNbOfJobs(); i++){
//					   Long jobId = list[i].getTaxiRideID();
//					   insertExternalJobReference(jobId);
//				   }
//			   }
//		   }
		   return  new ResponseResource(response); //return create job response
		
	}
	
	private boolean validateRequestAddresses(List<StopPointResource> stopPoints, int taxiCompanyID, ArrayList<PFAddressResponse> pickupAddresses, ArrayList<PFAddressResponse> setdownAddresses, ArrayList<PFAddressResponse> closestAddresses){
		if(stopPoints == null || stopPoints.size() == 0 || taxiCompanyID <= 0) return false;
		boolean forcedAddress = false;
		for(StopPointResource stopPoint : stopPoints){
			Address pickup = stopPoint.getPickupAddress();
			Address dropDown = stopPoint.getDropoffAddress();
			if(dropDown == null) {
				dropDown = new Address(); 					//if null set empty drop down address
				stopPoint.setDropoffAddress(dropDown);
			}
			
			
			if(!forcedAddress) forcedAddress = "Y".equalsIgnoreCase(pickup.getForcedAddrFlag());		//any forced address
			
			boolean unitPercentage = (pickup.getUnitNumber() == null || pickup.getUnitNumber().isEmpty()); //no unit provided 
			String orderBy = getPreDefinedOrderBy();
			PFAddressResponse pickupAddress = getWSAddressData(
					   appendPercentage(pickup.getStreetName(), false), 
					   appendPercentage(pickup.getStreetNumber(), false),
					   //appendPercentage(pickup.getUnitNumber(), true), //except for unit here, we want to use pe/rcentage to match
					   appendPercentage(pickup.getUnitNumber(), unitPercentage), //except for unit here, we want to use pe/rcentage to match
					   appendPercentage(pickup.getRegion(), false), 
					   appendPercentage(pickup.getLandmark(), true), 		//PF-16841, add percentage after building name
					   PERCENTAGE_CHAR, PERCENTAGE_CHAR,  //organization and postal code
					   taxiCompanyID, orderBy);
			   
			   if(!isValidAddressResponse(pickupAddress) && isBothLandMarkAndAddressProvided(pickup.getStreetName(), pickup.getStreetNumber(), pickup.getLandmark())){
				   pickupAddress = getWSAddressData(
						   appendPercentage(pickup.getStreetName(), false), 
						   appendPercentage(pickup.getStreetNumber(), false),
						   appendPercentage(pickup.getUnitNumber(), true), //except for unit here, we want to use percentage to match
						   appendPercentage(pickup.getRegion(), false), 
						   PERCENTAGE_CHAR, PERCENTAGE_CHAR, PERCENTAGE_CHAR,  //organization and postal code
						   taxiCompanyID, orderBy);
				   
			   }

			   PFAddressResponse closestAddress = getWSClosestAddressData(pickup.getLatitude(), pickup.getLongitude());

			   //Note: not appending any percentage to end of string here. 
			   //If there is no match, drop-off address will be used as-is in insertJob() below.
			   //(i.e. forced address, a.k.a exactLocation in StopPointItem)
			   PFAddressResponse setdownAddress = getWSAddressData(
					   appendPercentage(dropDown.getStreetName(), false), 
					   appendPercentage(dropDown.getStreetNumber(), false), 
					   appendPercentage(dropDown.getUnitNumber(), false), 
					   appendPercentage(dropDown.getRegion(),false),
					   appendPercentage(dropDown.getLandmark(), true),    //PF-16841, add percentage after building name
					   PERCENTAGE_CHAR, PERCENTAGE_CHAR, //organization and postal code
					   taxiCompanyID, orderBy);
			   
			   pickupAddresses.add(pickupAddress);
			   setdownAddresses.add(setdownAddress);
			   closestAddresses.add(closestAddress);
		}
		return forcedAddress;
	}
	
	protected AddressItem getAddressItem(PFAddressData addressData, int taxiCoID){
		Connection con = null;
		try{
			con = pfDataSource.getConnection();
			return getAddressItem(con, addressData, taxiCoID);
		}catch(SQLException se){
			logger.error("getAddressItem failed", se);
			return null;
		}finally{
			if(con != null) try{con.close();}catch(SQLException ignore){};
		}
	}
	
	private void insertJobResource(Job job, PostJobResponse response, ArrayList<PFAddressResponse> pickupAddressList, ArrayList<PFAddressResponse> closestAddressList, ArrayList<PFAddressResponse> setdownAddressList, boolean forcedAddress){
		// ArrayList<PFAddressData> pickupList = new ArrayList<PFAddressData>();
		ArrayList<PFAddressData> setdownList = new ArrayList<PFAddressData>();
		ArrayList<AddressItem> closestList = new ArrayList<AddressItem>();
		ArrayList<AddressItem> pickupAddressItems = new ArrayList<AddressItem>();
		
		if(!validatePickupAddresses(job, response, pickupAddressList, closestAddressList, setdownAddressList, forcedAddress,
					pickupAddressItems, setdownList, closestList)) return;
			   
	   String userName = getLogonCode();
	   int nbOfTaxi = 1;
	   Vector<CartItem> cartItems = generateCartItems(userName, nbOfTaxi, job, pickupAddressItems, setdownList, closestList);
	   if(cartItems == null || cartItems.size() < 1 || !validateCartItems(cartItems, response)) return;
	   
	   try{
		    // Order order = orderHome.create();
		    Collection<?> results = null;
		    //get estimated fare and estimated arrival time
		    //make sure setdown address is valid
//		    if(isValidAddressResponse(setdownAddressRes)){
//		    	AddressItem pickup = getAddressItem(pickupAddress, request.getTaxiCoID());
//		    	AddressItem setdown = getAddressItem(setdownAddress, request.getTaxiCoID());
//		    	checkTripMatrix(pickup, setdown, request, response);
//		    }
//		    if(cartItems.size() == 1){
		    	CartItem ci = cartItems.get(0);
		    	ci.setAppendAutoPopNotesOnServer(true);	//	set flag to append auto pop notes in orderDAOOracle, for now we do pickup auto notes
		    	results = order.setOrderDetails(userName, "", null, cartItems.get(0));
		    	setInsertJobResponse(true, results, job, response);
		    	
//		    }else{
//		    	results = order.multipleTrips_OrderEJB(userName, "", ACTION_TYPE_BOOK_MULTIPLEJOBS, cartItems);
//		    	setInsertJobResponse(false, results, request, response);
//		    }
		}catch(Exception e){
			logger.error("generateUpdateJobResponse failed", e);
		}
		
	}
	
	
	private boolean validatePickupAddresses(Job job, PostJobResponse response, ArrayList<PFAddressResponse> pickupAddressList, ArrayList<PFAddressResponse> closestAddressList, ArrayList<PFAddressResponse> setdownAddressList, boolean forcedAddress,
			ArrayList<AddressItem> pickupAddressItems, ArrayList<PFAddressData> setdownList, ArrayList<AddressItem> closestList) {
		
		for(int i =0 ; i< pickupAddressList.size(); i++){
			PFAddressResponse pickupAddressRes = pickupAddressList.get(i);
			PFAddressResponse closestAddressRes = closestAddressList.get(i);
			PFAddressResponse setdownAddressRes = setdownAddressList.get(i);
			
			if(!validateAddresses(forcedAddress, pickupAddressRes, closestAddressRes)){
				   response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_ADDRESS + " at stop point: " + i);
				   response.setResponseCode(BookJobErrorCode.INVALID_ADDRESS.getCode());
				   return false;
			}
		
		   
			Address pickup = job.getSubRoute().getStopPoints().get(i).getPickupAddress();
			Address dropdown = job.getSubRoute().getStopPoints().get(i).getDropoffAddress();
			
			//PFAddressData pickupAddress = getMatchAddress(pickupAddressRes, pickup.getStreetName(), pickup.getStreetNumber(), pickup.getRegion());
			PFAddressData pickupAddress = getMatchAddressWithUnit(pickupAddressRes, pickup.getStreetName(), pickup.getStreetNumber(), pickup.getRegion(), pickup.getUnitNumber());
			PFAddressData closestAddress = getFirstAddress(closestAddressRes);
			PFAddressData setdownAddress = getMatchAddressWithUnit(setdownAddressRes, dropdown.getStreetName(), dropdown.getStreetNumber(), dropdown.getRegion(), dropdown.getUnitNumber());
			
			//validate pickup address if provided
			AddressItem pickupItem = null;
			if(pickupAddress != null){
				pickupItem = getAddressItem(pickupAddress, job.getTaxiCompanyID());
				if(pickupItem == null || pickupItem.getHold_Flag()){
					response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_ADDRESS );
					if(pickupItem == null)
						response.setResponseCode(BookJobErrorCode.PICKUP_ADDR_NOT_FOUND.getCode());
					else 
						response.setResponseCode(BookJobErrorCode.INVALID_ADDRESS.getCode());
					
					return false;
				}
			}
			   
		   if(closestAddress!= null){
			   // Some details of the closest address, (e.g., area ID) are missing from the closestAddress
			   // which is getting by closest lookup service. We get detail of it by calling lookup service
			   
			   // Get detail of the closest address
			   closestAddressRes = getWSAddressData(
				   closestAddress.getStreetName().getValue(),
				   closestAddress.getStreetNumber().getValue(), 
				   closestAddress.getUnitNumber().getValue(),
				   closestAddress.getRegionAbbreviation().getValue(),
				   closestAddress.getLandmarkName().getValue(),
				   PERCENTAGE_CHAR, PERCENTAGE_CHAR, //organization and postal code
				   job.getTaxiCompanyID(), "");
			   closestAddress = getFirstAddress(closestAddressRes);
		   }
		   
		   modifyPickupClosestAddresses(pickupAddress, closestAddress);
	
			
		   
		   AddressItem closestAddrItem = null;
		   if( pickupAddress == null && closestAddress != null){ 
		      closestAddrItem = getAddressItem(closestAddress, job.getTaxiCompanyID());
		   }
		   
		   //both pickup and forced address are not found
		   if(pickupItem == null && closestAddrItem == null){
			   response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_ADDRESS );
			   response.setResponseCode(BookJobErrorCode.INVALID_FORCED_ADDRESS_FLAG.getCode());
			   return false;
		   }
		   
		   pickupAddressItems.add(pickupItem);
		   //pickupList.add(pickupAddress);
		   setdownList.add(setdownAddress);
		   closestList.add(closestAddrItem);
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private void setInsertJobResponse(boolean isSingleJob, Collection<?> pfResults, Job job,  PostJobResponse response){
		   // if(isSingleJob){
			   ArrayList<Object> results = (ArrayList<Object>)pfResults;
			   setSingleJobResponse(job, results, response);
//		   }else{
//			   Vector<CartItem> carts = (Vector<CartItem>)pfResults;
//			   setMultipleJobsResponse(request, carts, response);
//		   }
	}
	
	private void setDeleteJobResponse(long jobID, String externalID, PostJobResponse response){
		if(jobID > 0 ){
		   response.setErrorMessage(ResponseResource.ERROR_CODE_SUCCESS);
		   response.setResponseCode(BookJobErrorCode.NO_ERROR.getCode());
		   response.setHttpStatus(HttpServletResponse.SC_OK);
		 
		   ArrayList<PostJobIDReference> jobs = new ArrayList<PostJobIDReference>();
		   jobs.add(new PostJobIDReference(jobID, externalID));
		   response.setJobIDs(jobs);
		}
	}
	
	private void setSingleJobResponse(Job job, ArrayList<Object> results, PostJobResponse response){
	   if(results != null && results.size() > 0 ){
			if("true".equalsIgnoreCase((String)results.get(0))){
				response.setErrorMessage(ResponseResource.ERROR_CODE_SUCCESS);
				response.setResponseCode(BookJobErrorCode.NO_ERROR.getCode());
				response.setHttpStatus(HttpServletResponse.SC_CREATED);
				checkSingleJobResponse(job, response, results);
				
				insertExternalJobReference(pfDataSource, response.getJobIDs().get(0).getTaxiRideID(), response.getJobIDs().get(0).getReferenceNb(), getReference());
			} else {
				if (results.size() < 2) {
					return;
				}
				//check database error code
				Vector<String> dbErrorCodeArr = (Vector<String>)results.get(1);
				String dbErrorCode = dbErrorCodeArr.get(0);
				if (!databaseErrorMap.containsKey(dbErrorCode)) {
					//leave to use the default error response
					logger.info("Booking failed - database code " + dbErrorCode);
					response.setHttpStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
					return; 
				}
				//return meaningful error
				String[] responseCodeMsg = databaseErrorMap.get(dbErrorCode); //get the pair of <OSP response code, database error message>>
				response.setResponseCode(Integer.valueOf(responseCodeMsg[0]));
				response.setErrorMessage(responseCodeMsg[1]);
				response.setHttpStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
	   }
	}
	
	
	private void checkSingleJobResponse(Job job, PostJobResponse response, ArrayList<Object> results){
		   //response.setNbOfJobs(1);
		
		   Integer jobID = (Integer)results.get(1);
		   ArrayList<PostJobIDReference> jobs = new ArrayList<PostJobIDReference>();
		   jobs.add(new PostJobIDReference(jobID == null? 0 : jobID.longValue(), job.getExternalJobID()));
		   response.setJobIDs(jobs);
		   
//		   response.setTaxiRideID(jobID == null? 0 : jobID.longValue());
//		   response.setReferenceNb(job.getExternalJobID());
		   
	} 
	
	public void upcaseJobResource(JobResource jobResource){
		Job job = (Job) jobResource.getDataResource(); 		//jobResource.getJob();
		List<StopPointResource> stopPoints = job.getSubRoute().getStopPoints();
		for(StopPointResource stopPoint : stopPoints){
			if(stopPoint != null){
				upcaseAddress(stopPoint.getPickupAddress());
				upcaseAddress(stopPoint.getDropoffAddress());
			}
		}
		
		job.setExternalJobID(Utilities.upcaseString(job.getExternalJobID()));
	}
	
	public void upcaseAddress(Address addr){
		if(addr != null){
			addr.setStreetName(Utilities.upcaseString(addr.getStreetName()));
			addr.setStreetNumber(Utilities.upcaseString(addr.getStreetNumber()));
			addr.setUnitNumber(Utilities.upcaseString(addr.getUnitNumber()));
			addr.setRegion(Utilities.upcaseString(addr.getRegion()));
			addr.setAreaName(Utilities.upcaseString(addr.getAreaName()));
			addr.setLandmark(Utilities.upcaseString(addr.getLandmark()));
			addr.setOrganization(Utilities.upcaseString(addr.getOrganization()));
		}
	}
	
	private boolean isValidPostJobSource(JobResource jobResource, PostJobResponse response){
		//if(jobResource == null || jobResource.getJob() == null || jobResource.getJob().getSubRoute() == null) return false;
		if(jobResource == null || jobResource.getDataResource() == null || !(jobResource.getDataResource() instanceof Job) 
				|| ((Job) jobResource.getDataResource()).getSubRoute() == null) return false;
		Job job = (Job) jobResource.getDataResource(); 		//jobResource.getJob();
		if(job.getTaxiCompanyID() <= 0) {
			response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_COMPANY);
			response.setResponseCode(BookJobErrorCode.INVALID_TAXI_COMPID.getCode());
			return false;
		}
		SubRoute route = job.getSubRoute();
		if(route.getStopPoints() == null || route.getStopPoints().size() == 0 || route.getStopPoints().get(0) == null){
			response.setErrorMessage(ResponseResource.ERROR_CODE_EMPTY_ADDRESS);
			response.setResponseCode(BookJobErrorCode.INVALID_ADDRESS.getCode());
			return false;
		}
		
		StopPointResource stopPoint = route.getStopPoints().get(0);
		if(stopPoint.getPickupAddress() == null){
			response.setErrorMessage(ResponseResource.ERROR_CODE_EMPTY_ADDRESS);
			response.setResponseCode(BookJobErrorCode.INVALID_ADDRESS.getCode());
			return false;
		}
		
		Account account = route.getAccount();
		if(account == null){
			response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_ACCOUNT);
			response.setResponseCode(BookJobErrorCode.INVALID_ACCOUNT_CODE_OR_NAME.getCode());
			return false;
		}
		
		if(job.getJobAttributes() != null && job.getJobAttributes().length > 0){
			if(cachedParam != null && cachedParam.getPFRestConfigAttributes() != null && "Y".equalsIgnoreCase(cachedParam.getPFRestConfigAttributes().get(ATTR_NAME_INPUT_ATTR_CHECK))){
				if(!validateJobAttributes(job.getJobAttributes(), job.getTaxiCompanyID())){
					response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_INPUT_JOB_ATTR);
					response.setResponseCode(BookJobErrorCode.INVALID_INPUT_JOB_ATTR.getCode());
					return false;
				}
			}
		}
		
		if(job.getTariffNumber() != null && job.getTariffNumber().trim().length() > 0){
			if(order != null ){
				String tariffID = order.getTariffIDByNumber(getLogonCode(), job.getTariffNumber(), job.getTaxiCompanyID());
				if(tariffID == null || tariffID.trim().length() == 0){
					response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_INPUT_TARIFF);
					response.setResponseCode(BookJobErrorCode.INVALID_INPUT_TARIFF_NB.getCode());
					return false;
				}
			}
		}
		
		if(job.getExternalJobID() != null && job.getExternalJobID().trim().length() > 0){
			if(getJobIDFromExternalJobID(job.getExternalJobID()) > 0){
				response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_EXTERNAL_JOB_ID);
				response.setResponseCode(BookJobErrorCode.INVALID_RIDEID.getCode());
				return false;
			}
		}
		
		return true;
	}
	
	private boolean validateJobAttributes(String[] jobAttrs, int companyID){
		if(cachedParam == null) return true;
		HashMap<String, SysAttrListItem> drvAttrMap = cachedParam.getDrvAttributeMap().get(companyID);
		HashMap<String, SysAttrListItem> vehAttrMap = cachedParam.getVehAttributeMap().get(companyID);
		for(String jobAttribute : jobAttrs){
			int bitPosit = Utilities.findBitPosition(drvAttrMap, jobAttribute);
			if(bitPosit < 1 || bitPosit > Utilities.ATTRIBUTES_BITS_NUMBER){
				bitPosit = Utilities.findBitPosition(vehAttrMap, jobAttribute);
				if(bitPosit < 1 || bitPosit > Utilities.ATTRIBUTES_BITS_NUMBER) return false;
			}
		}
		return true;
	}
	
	private void convertCart2JobResource(CartItem cart, JobResource resource, long jobID){
		if(cart == null) {
			return;
		}
		
		//if(resource.getJob() == null) resource.setJob(new Job());
		if(resource.getDataResource() == null) resource.setDataResource(new Job());
		Job job = (Job) resource.getDataResource();		//resource.getJob();
		
		int companyID = cart.getTaxiCompanyID();
		
		job.setDataID(""+jobID);   //job.setJobID(jobID);
		job.setTaxiCompanyID(companyID);
		
		// resource.setPassangerNr(string2Int(cart.getPassengetCount()));
		// StopPoint stopPoint = cart.getStopPoint(0);
		// resource.setAdviseArrival(AdviseArrivalTypesImplement.AdviseArrivalType.NoAdvise.toOSPValue());
		Vector<StopPoint> stopPoints = cart.getStopPoints();
		
		for(int i = 0; i<stopPoints.size(); i++){
			StopPoint stopPoint = stopPoints.get(i);
			setStopPointInformation(stopPoint, job.getSubRoute(), companyID, i);
			if(i == 0 && stopPoint != null) setAccountInformation(stopPoint, job, companyID);
		}
		
		job.setTripStatus(cart.getState_Text());
		job.setDispatchedCar(cart.getVehicleCallsign());
		
		setJobAttributes(cart.getDriverAttributeBinary(), cart.getVehicleAttributeBinary(), job, companyID);
		if (JOB_TYPE_ASAP_RAW.equals(cart.getJOB_TYPE())) {
			job.setPickupTime(null); //this is ASAP job, set pickup as null in response to indicate
		} else {
			job.setPickupTime(Utilities.composeOSPDefaultDate(
									Utilities.convertDB2OSPDateFormat(cart.getDate()),
									Utilities.convertDB2OSPTimeFormat(cart.getTime())));
		}
		
		// setPricingResponse(cart.getPricingItem(), response);
		// setMissedJobResponse(response);
		String[] jobExtraInfo = new String[5];
		jobExtraInfo[0] =  cart.getExtraInfo1();
		jobExtraInfo[1] =  cart.getExtraInfo2();
		jobExtraInfo[2] =  cart.getExtraInfo3();
		jobExtraInfo[3] =  cart.getExtraInfo4();
		jobExtraInfo[4] =  cart.getExtraInfo5();
		job.setJobExtraInfo(jobExtraInfo);
		logger.info("cart notes: " + cart.getOperatorNotes() + ", manual notes:" + cart.getManualOperatorNotes());
		job.setOperatorNotes(cart.getManualOperatorNotes());
		job.setTariffNumber(cart.getTraiffNumber());
		
		if(order != null) job.setExternalJobID(order.findExternalIDNbByJobID(jobID, getReference()));
		
	}
	
	private void setStopPointInformation(StopPoint stopPoint, SubRoute subRoute, int companyID, int stopID){
		
		AddressItem pickup = stopPoint.getPickupAddress();
		AddressItem forcedPickup = stopPoint.getForced_Address_Item();
		if(subRoute.getStopPoints() == null){
			subRoute.setStopPoints(new ArrayList<StopPointResource>());
		}
		ArrayList<StopPointResource> stopPoints = (ArrayList<StopPointResource>) subRoute.getStopPoints();
		
		StopPointResource spResource = new StopPointResource();
		Address pickupAddress = spResource.getPickupAddress();
		Address dropoffAddress = spResource.getDropoffAddress();
		Passenger passenger = spResource.getPassenger();
		
		if(pickup != null){
			
			setAddressResource(pickup, pickupAddress);
			if(pickup.getForcedAddressFlag() == false){
				setResponsePickupGPS(pickup, pickupAddress);
			}else{
				setResponsePickupGPS(forcedPickup, pickupAddress);
			}
		}
		
		AddressItem setdown = stopPoint.getSetdownAddress();
		if(setdown != null ){
			setAddressResource(setdown, dropoffAddress);
			
			/* PF-14630 get dropoff GPS lat and long if address is there*/
			if(!setdown.getStrName().isEmpty() && !setdown.getStrNum().isEmpty() && !setdown.getRegion().isEmpty()) {
				PFAddressResponse setdownAddressRes = getWSAddressData(
					   appendPercentage(setdown.getStrName(), false), 
					   appendPercentage(setdown.getStrNum(), false), 
					   appendPercentage(setdown.getunitNr(), false), 
					   appendPercentage(setdown.getRegion(),false),
					   appendPercentage(setdown.getBuilding_Name(), false),
					   PERCENTAGE_CHAR, PERCENTAGE_CHAR, //organization and postal code
					   companyID, getPreDefinedOrderBy());
			
				PFAddressData setdownAddress = getMatchAddress(setdownAddressRes, setdown.getStrName(), setdown.getStrNum(), setdown.getRegion());
				if(setdownAddress != null) {
					dropoffAddress.setLatitude(setdownAddress.getLatitude());
					dropoffAddress.setLongitude(setdownAddress.getLongitude());
				}
				
			}
		}
		
		ContactInformation contact = stopPoint.getContactInfo();
		if(contact != null){
			setContactInformation(contact, passenger);
		}
		
		// logger.info("driver notes: " + stopPoint.getDrvNotes() + ", pickup notes: " + pickup.getManualDrvNotes());
		spResource.setDriverNotes(pickup.getManualDrvNotes());
		spResource.setStopPointID(stopID);
		spResource.setCancelledFlag(stopPoint.isCancelled()? "Y" : "N");
		spResource.setCustomerNumber(stopPoint.getCustomerNumber());
		spResource.setRequiredDTMinDate(stopPoint.getRequiredPickupDTM());
		spResource.setOperatorNotes(stopPoint.getOperatorNotes());
		
		stopPoints.add(spResource);
	}
	
	private void setAddressResource(AddressItem addressItem, Address addr){
		addr.setStreetName(addressItem.getStrName());
		addr.setStreetNumber(addressItem.getStrNum());
		addr.setRegion(addressItem.getRegion());
		addr.setLandmark(addressItem.getBuilding_Name());
		addr.setOrganization(addressItem.getOrganisation());
		addr.setUnitNumber(addressItem.getunitNr());
		addr.setAreaName(addressItem.getAreaName());
	}
	
	private void setResponsePickupGPS(AddressItem addressItem, Address addr){
		addr.setLatitude(addressItem.getLatitude());
		addr.setLongitude(addressItem.getLongitude());
	}
	
	private void setContactInformation(ContactInformation contact, Passenger passenger){
		passenger.setPhoneNumber(contact.getTelephone());
		passenger.setPhoneExtension(contact.getTelExt());
		passenger.setPassengerName(contact.getGivenName());
	}
	
	private void setAccountInformation(StopPoint stopPoint, Job job, int companyID){
		ContactInformation contact = stopPoint.getContactInfo();
		if(contact != null){
			if(job.getSubRoute().getAccount() == null) job.getSubRoute().setAccount(new Account());
			Account account = job.getSubRoute().getAccount();
			
			String accountCode = contact.getAccount();
			if (accountCode != null) {
				account.setAccountCode(accountCode);
				account.setAccountName(contact.getAccount_Name()); 
			} else {
				//somehow we failed to get what this job is booked with
				account.setAccountCode("N/A"); //TODO: reply with error 
				account.setAccountName("N/A");
			}
		}
		job.setAdviseArrival(getAdviseArrivalFlag(stopPoint, contact));
		job.setSpExtraInfo1(stopPoint.getExtraInfo1());
	}
	
	private void setJobAttributes(String drvAttributes, String vehAttributes, Job job, int companyID){
		if(cachedParam == null) {
			logger.error("setJobAttributes failed with cached param null ........");
			return;
		}
		ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>();
		
		HashMap<String, SysAttrListItem> drvAttrMap = cachedParam.getDrvAttributeMap().get(companyID);
		HashMap<String, SysAttrListItem> vehAttrMap = cachedParam.getVehAttributeMap().get(companyID);
		// CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
		attributes.addAll(Utilities.getAttributesItem(drvAttrMap, Utilities.ToBinaryAttributesString(drvAttributes)));
		attributes.addAll(Utilities.getAttributesItem(vehAttrMap, Utilities.ToBinaryAttributesString(vehAttributes)));
		
		if(attributes.size() > 0){
			String[] attrArray = new String[attributes.size()];
			for(int i=0; i<attributes.size(); i++){
				attrArray[i] = attributes.get(i).getAttrShortName();
			}
			
			job.setJobAttributes(attrArray);
		}
	}
	
	
	private Vector<CartItem> generateCartItems (String userName, int nbOfJob, Job job, ArrayList<AddressItem> pickupAddress, ArrayList<PFAddressData> setdownAddress, ArrayList<AddressItem> closestAddrItem){
		   Vector<CartItem> carts = new Vector<CartItem>();
		   if(nbOfJob <=0) nbOfJob = 1;
		   for( int i=0; i<nbOfJob; i++){
			   CartItem cartItem = convertRequest2CartItem(userName, job, pickupAddress, setdownAddress, closestAddrItem); 
			   if(cartItem != null){
				   cartItem.setJobId("");
				   cartItem.setAlready_Address_job(true);
				   carts.add(cartItem);
			   }
		   }
		   checkAdditionalCartImplement(carts, job);
		   return carts;
	}
	
	
	public void checkAdditionalCartImplement(Vector<CartItem> carts, Job job){
		if(ExternalSystemId.SYSTEM_ID_TRANSIT_GW.getLogonCode().equals(getLogonCode())){
			PFSutiJobResourceImplement sutiImplement = new PFSutiJobResourceImplement(pfDataSource, cachedParam);
			sutiImplement.generateCartItems(carts, job);		
		}
	}
	
	
	public CartItem convertRequest2CartItem(String userName, Job job, ArrayList<AddressItem> pickupAddress, ArrayList<PFAddressData> setdownAddress, ArrayList<AddressItem> closestAddrItem){
		
		CartItem cartItem = new CartItem();
		cartItem.setTaxiCompanyID(job.getTaxiCompanyID());
		cartItem.setTaxi_Company(cachedParam.getTaxiCompanyName(job.getTaxiCompanyID()));  
		cartItem.setJobId(job.getDataID());    //cartItem.setJobId(""+ job.getJobID());
		cartItem.setOriginCode(getReference());
		cartItem.setManualOperatorNotes(job.getOperatorNotes());
		setCartStopPoint(cartItem, job, pickupAddress, setdownAddress, closestAddrItem);
		

		// String jobCalloutValue = job.getAdviseArrival();
		cartItem.setJobCalloutValue(AdviseArrivalType.NoAdvise.toPFValue());
		cartItem.setJobCalloutValueAbbr(AdviseArrivalType.NoAdvise.toPFValueAbbr());
//		String[] pfCallout = new String[1];
//		String[] pfCalloutAbbr = new String[1];
//		if (jobCalloutValue != null && jobCalloutValue.length() > 0) {
//			AdviseArrivalTypesImplement.getPFCalloutValue(pfDataSource, jobCalloutValue, pfCallout, pfCalloutAbbr);
//			cartItem.setJobCalloutValue(pfCallout[0]);
//			cartItem.setJobCalloutValueAbbr(pfCalloutAbbr[0]);
//		}
				
		cartItem.setJob_priority("30");
		cartItem.setCallSign("");
		// cartItem.setCustCallbackCount(getWSIntegerValue(request.getNbofCallBacks()));
		setCartPickupDTM(cartItem, job);
		// setAttributesBinary(cartItem, request);
		cartItem.getServiceTypeAccount().setAccountSetId(4);		//default service type pickup 4
		cartItem.setPassengetCount("1");
		cartItem.setUserCode(userName);
		cartItem.setExtraInformation("");
		String[] jobExtraInfo = job.getJobExtraInfo();
		if(jobExtraInfo != null && jobExtraInfo.length == 5){
			cartItem.setExtraInfo1(jobExtraInfo[0]);
			cartItem.setExtraInfo2(jobExtraInfo[1]);
			cartItem.setExtraInfo3(jobExtraInfo[2]);
			cartItem.setExtraInfo4(jobExtraInfo[3]);
			cartItem.setExtraInfo5(jobExtraInfo[4]);		
		}
		cartItem.setTraiffNumber(job.getTariffNumber());
		
		ArrayList<SysAttrListItem> missedAttributes = new ArrayList<SysAttrListItem>();
		//C34595, add account info to cart if booking by account
		Account account = job.getSubRoute().getAccount();
		if ((account.getAccountCode() != null && account.getAccountCode().length() > 0)
				|| (account.getAccountName() != null && account.getAccountName().length() > 0)){
			Vector<UserAccountItem> accounts = null;
			try {
				if (userAccount == null){
					logger.debug("Implementation of Jobresource implement did not set UserAccountHome");
				}else{
					accounts = userAccount.getByAccountDetails(getLogonCode(), "", cartItem, 1, "");
				}
			} catch (Exception e) {
				logger.error("getByAccountDetails failed....", e);
			}
			if (accounts == null || accounts.size() <= 0) {
				//specified account not found. should return error.
				logger.error("no account found");
			} else {
				UserAccountItem foundAccount = accounts.get(0);
				if (accounts.size() > 1){
					logger.info("more than one account, use 1st account - " + foundAccount.getAccountId() 
							+ " " + foundAccount.getAccountName());
				}
				
				populateAccountDetails(cartItem, foundAccount, job, missedAttributes);
			}
			
		}
		
		// C36745 - Add all non-manual attributes here
		if( !addAutoAttributes( job, cartItem, cartItem.getStopPoint(0).getPickupAddress(), closestAddrItem == null ? null:closestAddrItem.get(0), missedAttributes ) ){
			logger.error("auto populate attributes failed with exceptions................");
		} 
			   
//		String requestCabNum = request.getCabNum(); //the preferred vehicle callsign
//		if (requestCabNum != null) {
//			requestCabNum = requestCabNum.trim();
//			if (requestCabNum.length() > 0) {
//				String preferredType = "P";
//				VehiclePrefVO vechiclePref = createVehiclePref(requestCabNum);
//				vechiclePref.setPrefType(preferredType);
//				cartItem.getVehicleOrderVO().appendVehiclePrefVO(vechiclePref);
//			}
//		}
		
		
		
		// C35846 - Set webbooker held job flag if parameter C_WB_HELD_JOB is enabled
//		String value = cachedParam.getCompanyParameterValue(request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_WB_HELD_JOB);//CompanyDefaultValues.getCompanyParameterValue(pfDataSource, request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_WB_HELD_JOB);
//		if("Y".equalsIgnoreCase(value))  {
//			cartItem.setJOB_TYPE("HELD JOB");
//			cartItem.getDetailInfo().add("Held Job");
//			cartItem.setWBHoldFlag(true);
//		}else{
			cartItem.setWBHoldFlag(false);
//		}// End of C35864
		
		validateAttributes(cartItem, missedAttributes);
		
		logger.info("cart operator notes: " + cartItem.getOperatorNotes() + ", manual notes = " + cartItem.getManualOperatorNotes() + ", input notes =" + job.getOperatorNotes());
		
		return cartItem;
	}
	
	private boolean addAutoAttributes( Job job, CartItem cartItem, AddressItem pickupAddress, AddressItem closestAddress, ArrayList<SysAttrListItem> missedAttributes ){

		ArrayList<String> autoAttributes = null;
		int companyID = job.getTaxiCompanyID();
		
	   if( pickupAddress!= null && !pickupAddress.getForcedAddressFlag() ) {
		   autoAttributes = getAddressAttributes(pickupAddress, companyID, false, missedAttributes);
	   }else if(closestAddress!=null ){
		   autoAttributes = getAddressAttributes(closestAddress, companyID, true, missedAttributes);
	   }
	   setAttributesBinary(cartItem, Utilities.appendRequiredAttribute(job.getJobAttributes(), autoAttributes));
	   
	   return true;
	}
	
	private ArrayList<String> getAddressAttributes(AddressItem address, int companyID, boolean isForcedAddress, ArrayList<SysAttrListItem> missedAttributes){
		Connection con = null;
		CallableStatement cs = null;
		// ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>();
		// CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
		String drvAttributes = "";
		String vehAttributes = "";
		
		try{
			con = pfDataSource.getConnection();
			
			AddressDAO2 dao2 = new AddressDAO2();
			
			// Call stored procedure to retrieve address related attributes.
			AddressItem addressItem = dao2.createAddressDeCarta(con, address, companyID, false);
			
			ArrayList<String> attributes = new ArrayList<String>();

			if( "Y".equals(cachedParam.getCompanyParameterValue(companyID, CompanyDefaultValues.COMP_PARAMETER_C_USE_AREA_ATTR_FLAG) )) {
				drvAttributes = Utilities.ToBinaryAttributesString(addressItem.getDriverAttributes());
				vehAttributes = Utilities.ToBinaryAttributesString(addressItem.getVehicleAttributes());
				// findMissedUnExpAttributes(jobReq.getTaxiCoID(), drvAttributes, vehAttributes, missedAttributes);
									
				// Insert area attributes to request
				attributes = Utilities.appendArrayListString(attributes, insertRequestAttributes( companyID, drvAttributes, vehAttributes, missedAttributes ) );
			}
			// A forced address is forced to it's closest address in the database. Don't apply building attribute.
			if( !isForcedAddress && "Y".equals(cachedParam.getCompanyParameterValue(companyID, CompanyDefaultValues.COMP_PARAMETER_C_USE_ADDR_ATTR_FLAG)) )
			{
				drvAttributes = Utilities.ToBinaryAttributesString(addressItem.getDrvAddAttributes());
				vehAttributes = Utilities.ToBinaryAttributesString(addressItem.getVehAddAttributes());
				// Insert address attributes to request
				attributes = Utilities.appendArrayListString(attributes, insertRequestAttributes( companyID, drvAttributes, vehAttributes, missedAttributes ) );					
			}
			
			if( "Y".equals(cachedParam.getCompanyParameterValue(companyID, CompanyDefaultValues.COMP_PARAMETER_C_USE_REGN_ATTR_FLAG) ) ){
				drvAttributes = Utilities.ToBinaryAttributesString(addressItem.getDrvRegAttributes());
				vehAttributes = Utilities.ToBinaryAttributesString(addressItem.getVehRegAttributes());
				// findMissedUnExpAttributes(jobReq.getTaxiCoID(), drvAttributes, vehAttributes, missedAttributes);  //PF-15958, add region un exposed attributes
				// Insert region attributes to request
				attributes = Utilities.appendArrayListString(attributes, insertRequestAttributes( companyID, drvAttributes, vehAttributes, missedAttributes ) );
			}
			return attributes;
			
		}catch(SQLException se){
			logger.error("address detail retrieval failed with exception", se );
			return null;
		}finally{
			if(cs != null) try{cs.close();}catch(SQLException ignore){};
	       	if(con != null) try{con.close();}catch(SQLException ignore){};
		}

	}
	
	private void populateAccountDetails(CartItem cartItem, UserAccountItem uaItem, Job job, ArrayList<SysAttrListItem> missedAttributes)
	{
		ContactInformation accountContact = uaItem.getContactInfo();
		ContactInformation currentContact = cartItem.getStopPoint(0).getContactInfo();
		
		if (accountContact != null)
		{
			if (currentContact != null && accountContact.getMonitor_Flag())
			{
				cartItem.getDetailInfo().add("Monitor");
				currentContact.setMonitor_Flag(true);
			}
			currentContact.setClientCompany(uaItem.getOrganization());
		}
		
		cartItem.setAcctOrganization(uaItem.getOrganization());
		cartItem.getStopPoint(0).setDrvNotes(uaItem.getAccountDriverNotes()); //set the read-only driver notes
		cartItem.setOperatorNotes(Utilities.addNotes(cartItem.getOperatorNotes(), uaItem.getAccountCalltakerNotes(), "\\"));
		
		cartItem.setAccount_Profile_ID(uaItem.getAccountUserProfile());
		
		// if job is not in completed state, overwrite the currentItem with values from account C19662	
		if (cartItem.getJobStage() != null && cartItem.getJobStage().compareToIgnoreCase("") != 0) 
		{
			if (cartItem.getJobStage().compareToIgnoreCase("C") != 0 )	{
				cartItem.setRunIn(uaItem.getAccountRunIn());
				cartItem.setLayout_amount(uaItem.getAccountLayout());
				cartItem.setASAPBookingFee(uaItem.getAccountASAPFee());
				cartItem.setPBOKBookingFee(uaItem.getAccountPBOKFee());
				cartItem.setUpliftFee(uaItem.getAccountUpliftFee());
			}	
		} else {
			cartItem.setRunIn(uaItem.getAccountRunIn());
			cartItem.setLayout_amount(uaItem.getAccountLayout());
			cartItem.setASAPBookingFee(uaItem.getAccountASAPFee());
			cartItem.setPBOKBookingFee(uaItem.getAccountPBOKFee());
			cartItem.setUpliftFee(uaItem.getAccountUpliftFee());		
		}
		
		cartItem.setJob_priority(uaItem.getJobPriority()); // C16415 - aleung - fixed for not populating account priority
		
		String drvAttributes = Utilities.ToBinaryAttributesString(uaItem.getDriverAttributes());
		String vehAttributes = Utilities.ToBinaryAttributesString(uaItem.getVehicleAttributes());
		
		ArrayList<String> accountAttributes = insertRequestAttributes(job.getTaxiCompanyID(), drvAttributes, vehAttributes, missedAttributes);
		setAttributesBinary(cartItem, Utilities.appendRequiredAttribute(job.getJobAttributes(), accountAttributes));
		
	}

//	private void setJobCarGPS(Job job){
//		if(pfDataSource != null ){
//			Connection con = null;
//			Statement stmt = null;
//	        ResultSet rs = null;
//			try{
//				con = pfDataSource.getConnection();
//		        stmt = con.createStatement();
//				rs = stmt.executeQuery("select vs.latitude, vs.longitude from jobs j, vehicle_states vs where j.vehicle_id = vs.vehicle_id and j.job_id = " + job.getDataID());		//job.getJobID());
//				if(rs.next()){
//					double latitude = rs.getDouble("latitude");
//					double longitude = rs.getDouble("longitude");
//					if(isValidDoubleValue(latitude) && isValidDoubleValue(longitude) 
//							&& latitude != 0 && longitude != 0){
//						job.setCarLatitude(latitude);
//						job.setCarLongitude(longitude);
//					}
//				}
//			}catch(SQLException se){
//				se.printStackTrace();
//				logger.error("setResponseCarGPS failed: ", se);
//			}finally{
//			
//				if (rs != null) try{rs.close();}catch(SQLException ignore){};
//	    	    if (stmt != null) try{stmt.close();}catch(SQLException ignore){};
//	    	    if(con != null) try{con.close();}catch(SQLException ignore){};
//			}
//		}
//	}
	
	public String getAdviseArrivalFlag(StopPoint stopPoint, ContactInformation contact){
		//Get PF callout value from address or contact info
		//(This is very similar to com.dds.pathfinder.callbooker.client.util.visitor.CartItemWriterVisitor's getCalloutValue())
		AddressItem pickup = stopPoint.getPickupAddress();
		String pfCallout = "N";
		if(pickup != null && pickup.getJCallOutValue() != null && pickup.getJCallOutValue().length() > 0){
			pfCallout = pickup.getJCallOutValue();
		}else if(contact != null && contact.getCallOutValue() != null && contact.getCallOutValue().length() > 0){
			pfCallout = contact.getCallOutValue();
		}else if(pickup != null){
			pfCallout = pickup.getCallOutValue();
		}
		
		return pfCallout;
		
		//return OSP advise arrival value using AdviseArrivalTypesImplement AdviseArrivalType mapping
		// return AdviseArrivalTypesImplement.getOSPAdviseArrivalValue(pfDataSource, pfCallout); 
	}
	
	
	private void setCartStopPoint(CartItem cartItem, Job job, ArrayList<AddressItem> pickupList, ArrayList<PFAddressData> setdownList, ArrayList<AddressItem> closestAddrItemList){	
		// Connection con = null;
		for(int i= 0; i< pickupList.size(); i++){
			AddressItem pickupAddress = pickupList.get(i);
			StopPointResource spResource = job.getSubRoute().getStopPoints().get(i);
			Address pickAddr = spResource.getPickupAddress();
			Address dropAddr = spResource.getDropoffAddress();
			// PFAddressData pickup = pickupList.get(i);
			PFAddressData setdown = setdownList.get(i);
			AddressItem closestAddrItem = closestAddrItemList.get(i);
			if(i > 0) cartItem.addStopPoint();
			StopPoint stopPoint = cartItem.getStopPoint(i);
			
//			if( pickup!=null){
//				pickupAddress = getAddressItem(pickup, job.getTaxiCompanyID());
//			} else if(closestAddrItem!=null) {// A forced address trip
				
			if(pickupAddress == null && closestAddrItem != null){
				
				if( i == 0 ){
					cartItem.setForcedAddressFlag(true);
					cartItem.setforced_address_id(closestAddrItem.getAddressId());
					cartItem.setforced_area_id(closestAddrItem.getAreaId());
				}
				
				// Use the closest address as the address forced to
				pickupAddress=new AddressItem();
				stopPoint.setForced_Address_Item(closestAddrItem);
				stopPoint.getForced_Address_Item().setforced_address_id(closestAddrItem.getAddressId());
				stopPoint.getForced_Address_Item().setforced_block_id(closestAddrItem.getBlockFaceId());
				stopPoint.getForced_Address_Item().setforced_area_id(closestAddrItem.getAreaId());
				
				// Use the pickup information in the request as the pickup address
				pickupAddress.setStrName(pickAddr.getStreetName());
				pickupAddress.setStrNum(pickAddr.getStreetNumber());
				pickupAddress.setRegion(pickAddr.getRegion());
	
				pickupAddress.setForcedAddressFlag(true);
				// Store the original forced address GPS
				if (pickAddr.getLatitude() > -90.0 && pickAddr.getLatitude() < 90.0 &&
					 pickAddr.getLongitude() > -180.0 && pickAddr.getLongitude() < 180.0){
					pickupAddress.setLatitude(pickAddr.getLatitude());
					pickupAddress.setLongitude(pickAddr.getLongitude());
				}
			}
			if (pickAddr.getUnitNumber() != null && pickAddr.getUnitNumber().length() > 0) {
				pickupAddress.setunitNr(pickAddr.getUnitNumber());
			} 
			stopPoint.setPickupAddress(pickupAddress);
			setCartItemFlagsByPickup(cartItem, pickupAddress);
			
			// Suti particular setting
//			stopPoint.setCustomerNumber(spResource.getCustomerNumber());
//			if(spResource.getRequiredDTM() != null && spResource.getRequiredDTM().trim().length() > 0){
//				Date requiredDTM = Utilities.convertOSPDefaultDate(spResource.getRequiredDTM());
//				if(requiredDTM != null) stopPoint.setRequiredPickupDTM(requiredDTM);
//			}
//			stopPoint.setOperatorNotes(spResource.getOperatorNotes());
			
			//set down address was requested although not valid
			if (setdown == null && !isAddressEmpty(dropAddr)) {
				//use the address from request directly
				setForcedAddressItem(stopPoint.getSetdownAddress(), dropAddr);
			} else {
				setAddressItem(stopPoint.getSetdownAddress(), setdown, dropAddr.getUnitNumber());
			}
			
			setContactInformation(stopPoint.getContactInfo(), job.getSubRoute().getAccount(), job.getSubRoute().getStopPoints().get(i).getPassenger(), job.getTaxiCompanyID());
			//set driver notes (a.k.a. remarks) from OSP webbooker to show up in driver notes in callbooker
			String driverNotes = job.getSubRoute().getStopPoints().get(i).getDriverNotes();
			
			
			if (driverNotes != null && driverNotes.trim().length() > 0 && stopPoint.getPickupAddress()!=null) {
					stopPoint.getPickupAddress().setManualDrvNotes(driverNotes.trim()); //set the writable driver notes
			}
			String spExtraInfo1 = job.getSpExtraInfo1();
			if (spExtraInfo1 != null && !spExtraInfo1.isEmpty()) stopPoint.setExtraInfo1(spExtraInfo1);
			
			
		}
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	private void setCartItemFlagsByPickup(CartItem cartItem, AddressItem pickupAddress){
		if(cartItem == null || pickupAddress == null || cartItem.getDetailInfo() == null) return;
		if(pickupAddress.isHeldJobFlag()){
			if( !cartItem.getDetailInfo().contains(CB_TRIP_ATTRIBUTE_HELDJOB) ){
				cartItem.getDetailInfo().add(CB_TRIP_ATTRIBUTE_HELDJOB);
			}
			cartItem.setJOB_TYPE(CB_JOB_TYPE_HELDJOB);
		}
		
	}
	
	private boolean isJobTypeSetupCartAlready(CartItem cartItem){
		return (cartItem != null && cartItem.getJOB_TYPE() != null && cartItem.getJOB_TYPE().trim().length() > 0);
	}
	
	private void setContactInformation(ContactInformation contact, Account account, Passenger passenger, int companyID){

		if(contact == null)  {
			return;
		}
		
		// Account account = job.getSubRoute().getAccount();
		if(passenger != null){
			if(passenger.getPhoneNumber() == null) passenger.setPhoneNumber("");   //PF-16514, PF phone number can't be null
			contact.setTelephone(passenger.getPhoneNumber());
			contact.setTelExt(passenger.getPhoneExtension());
			
			contact.setReference("");
			
			//Some parts of Callbooker use given name, some parts use family name.
			//To make sure we get the passenger name out both in booking and searching, 
			//we book with both fields set in contact information.
			contact.setGivenName(passenger.getPassengerName());
			contact.setFamilyName(passenger.getPassengerName());
		}
		
		//trim account code
		String accountCode = account.getAccountCode();
		if (accountCode != null && accountCode.length() > 0) {
			accountCode = accountCode.trim().toUpperCase();
		} else {
			accountCode = null;
		}
		
		//trim account name
		String accountName = account.getAccountName();
		if (accountName != null && accountName.length() > 0) {
			accountName = accountName.trim();
		} else {
			accountName = null;
		}
		
		//check if it's pay by cash
		if(accountCode == null && accountName == null){ 
			
			accountCode = "CASH";
			accountName = "CASH";	
			accountCode = cachedParam.getCompanyParameterValue(companyID, CompanyDefaultValues.COMP_PARAMETER_GENERAL_CASH_ACCOUNT_NR, CompanyDefaultValues.DEFAULT_CASH_ACCOUNT_NAME_NR);;
			accountName = cachedParam.getCompanyParameterValue(companyID, CompanyDefaultValues.COMP_PARAMETER_GENERAL_CASH_ACCOUNT_NAME, CompanyDefaultValues.DEFAULT_CASH_ACCOUNT_NAME_NR);
		}
		
		contact.setAccount(accountCode); //account card number
		contact.setAccount_Name(accountName); //account name
		//using default in job resource if not defined.
		account.setAccountCode(accountCode);
		account.setAccountName(accountName);
		contact.setAccount_Pin(""); 
		
		//OSP user selected advise arrival value 
		// String[] pfCallout = new String[1];
		//AdviseArrivalTypesImplement.getPFCalloutValue(pfDataSource, request.getAdviseArrival(), null, pfCallout);
		// contact.setCallOutValue(pfCallout[0]);
	}
	
	private boolean isAddressEmpty(Address address) {
		return (address.getStreetName() == null || address.getStreetName().trim().length() == 0)
				&& (address.getStreetNumber() == null || address.getStreetNumber().trim().length() == 0)
				&& (address.getLandmark() == null || address.getLandmark().trim().length() == 0)
				&& (address.getRegion() == null || address.getRegion().trim().length() == 0)
				&& (address.getUnitNumber() == null || address.getUnitNumber().trim().length() == 0);
	}
	
	private void setForcedAddressItem(AddressItem addressItem,  Address addr) {
		addressItem.setStrName(addr.getStreetName());
		addressItem.setStrNum(addr.getStreetNumber());
		addressItem.setunitNr(addr.getUnitNumber());
		addressItem.setRegion(addr.getRegion());
		addressItem.setBuilding_Name(addr.getLandmark());
	}
	
	private void setCartPickupDTM(CartItem cartItem, Job job){
		Date pickupDTM = Utilities.convertOSPDefaultDate(job.getPickupTime());
		if(!isJobTypeSetupCartAlready(cartItem)) setJobType(cartItem, pickupDTM);  //set job type using pickup date/time user requested if not set yet
		if(pickupDTM == null) pickupDTM = new Date();
		cartItem.setDateForMultilangual(pickupDTM);
		cartItem.setTime(Utilities.formatUtilDate(pickupDTM, "HH:mm"));
		cartItem.setDate(Utilities.formatUtilDate(pickupDTM, "EEEE, MMMM dd,yyyy"));
	}
	
	private void setJobType(CartItem cartItem, Date pickupDTM){
		if (pickupDTM == null) {
			cartItem.setJOB_TYPE(JOB_TYPE_ASAP);
		} else {
			cartItem.setJOB_TYPE(JOB_TYPE_PBOK);
		}
	}
	
	public boolean validateCartItem(CartItem cartItem, PostJobResponse response){
		if(cartItem == null) return false;
		if(!isValidPickupAddress(cartItem)){
			response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_ADDRESS);
			response.setResponseCode(BookJobErrorCode.INVALID_ADDRESS.getCode());
			return false;
		}
//		if(!isValidClientTelephone(cartItem)){
//			response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
//			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_TELEPHONE);
//			response.setErrorCode(BookJobErrorCode.INVALID_PHONENUM.getCode());
//			return false;
//		}
		return true;
			
	}
	
	public boolean validateCartItems(Collection<CartItem> cartItems, PostJobResponse response){
		if(cartItems == null) return false;
		for(CartItem cartItem : cartItems){
			if(!validateCartItem(cartItem, response)) return false;
		}
		return true;
	}
	
	private boolean isValidPickupAddress(CartItem cartItem){
		if(cartItem == null || cartItem.getStopPoints() == null || cartItem.getStopPoints().size() == 0) return false;
		for(StopPoint stopPoint : (Vector<StopPoint>)cartItem.getStopPoints()){
			if(stopPoint != null && stopPoint.getPickupAddress() != null){
				AddressItem pickupAddress = stopPoint.getPickupAddress();
				if(pickupAddress.getHold_Flag() || pickupAddress.getBlockFaceHoldFlag() || ! pickupAddress.isServiceRegionFlag()) return false;
			}else return false;
		}
		return true;
	}
	
	
	private String getExternalJobIDByJobID(long jobID) {
		// boolean res = false;
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;

		String referenceType = null;
		String externalJobID = NOT_FOUND_JOB_REF;

		referenceType = getReference();
		
		if (referenceType != null && !referenceType.isEmpty()) {
		   String query = "select job_id, external_job_id, reference_type from job_references "
				   + " where job_id = " + jobID;
		
		   try{
			   	con = pfDataSource.getConnection();
				stmt = con.createStatement();
				result = stmt.executeQuery(query);
				
				if (result.next() ) {
					if(result.getString("reference_type").equalsIgnoreCase(referenceType)) {
						// res = true;
						externalJobID = result.getString("external_job_id");
					}
							
				}
				
	       }catch(SQLException se){
		       	logger.error("isRightJobToCancel() failed with exception", se);
		       	
	       }finally{
	    	    if (result != null)try {result.close();} catch (SQLException ignore) {};
	    	    if (stmt != null)try {stmt.close();} catch (SQLException ignore) {};
	       		if(con != null) try{con.close();}catch(SQLException ignore){};
	       		
	       }
		}
		
	    return externalJobID;
	   
	}
	
	private long getJobIDFromExternalJobID(String externalJobID) {
		// boolean res = false;
		if(externalJobID == null || externalJobID.trim().length() == 0) return 0;
		
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;

		String referenceType = null;
		long jobID = 0;
		

		referenceType = getReference();
		
		if (referenceType != null && !referenceType.isEmpty()) {
		   String query = "select job_id, external_job_id, reference_type from job_references "
				   + " where external_job_id = '" + externalJobID + "' and reference_type = '" + referenceType + "'";
		
		   try{
			   	con = pfDataSource.getConnection();
				stmt = con.createStatement();
				result = stmt.executeQuery(query);
				
				if (result.next() ) {
					jobID = result.getLong("job_id");		
				}
				
	       }catch(SQLException se){
		       	logger.error("isRightJobToCancel() failed with exception", se);
		       	
	       }finally{
	    	    if (result != null)try {result.close();} catch (SQLException ignore) {};
	    	    if (stmt != null)try {stmt.close();} catch (SQLException ignore) {};
	       		if(con != null) try{con.close();}catch(SQLException ignore){};
	       		
	       }
		}
		
	    return jobID;
	   
	}

	public String getLogonCode(){
		if(systemID >0) return getUserName(systemID, ""+systemID);
		else return ExternalSystemId.SYSTEM_ID_TRANSIT_GW.getLogonCode();  		//default TG
	}
	
	public String getReference(){
		if(systemID >0) return getSystemReference(systemID);
		else return ExternalSystemId.SYSTEM_ID_TRANSIT_GW.getReference();		//default TG
	}

	public int getSystemID() {
		return systemID;
	}

	public void setSystemID(int systemID) {
		this.systemID = systemID;
	}
	
}
