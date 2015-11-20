/****************************************************************************
 *
 *                            Copyright (c), 2010
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/UpdateOrderByConfNumImplement.java $
 * 
 * PF-16104, June 5th, 2014, UDI should be able to cancel any job too.
 * 
 * 16    1/16/14 3:28p Dchen
 * PF-15751, OSP to validate pickup and phone number.
 * 
 * 15    12/20/13 2:16p Dchen
 * PF-15469, add origin code for cancelling job as well.
 * 
 * 14    12/20/13 2:13p Dchen
 * PF-15469, add origin code for cancelling job as well.
 * 
 * 13    3/12/13 4:34p Dchen
 * Added pf parameters load bean.
 * 
 * 12    10/25/12 11:00a Ezhang
 * PF_14809 added validateConfirmationNumber() to make sure the conf number is from the same system( Webbooker, GFC or MB)
 * as it created. 
 * 
 * 11    9/12/11 2:22p Yyin
 * C36130- Fixed forced address trips with OSP.
 * 
 * 10    2/12/11 10:01a Ezhang
 * C36130, added system id validation.
 * 
 * 9     8/18/11 3:33p Dchen
 * upgrade to 3.75.
 * 
 * 8     4/19/11 6:28p Dchen
 * C35277, speed webbooker order tracking.
 * 
 * 7     4/02/11 7:20p Mkan
 * C34962
 * - processUpdateJobList(): modified query to obtain job's state_text, 
 *   so that we know if some jobs were cancelled and can get the real
 * "numOfTaxi" to use
 * - createJob(): added "cancelledJobs" parameter, so that it knows
 * exactly how many jobs need to be created for the update
 * 
 * 6     12/23/10 3:24p Mkan
 * C34841
 * processUpdateJobList()
 * - allow update of dispatched job (job state "V")
 * - return a tripUpdateStatus 1 if the dispatched job was updated
 * successfully
 * 
 * 5     12/10/10 3:21p Mkan
 * C34595
 *    - UpdateOrderByConfNumImplement(), added to set UserAccountHome
 *    - processUpdateJobList(), pass in new UserAccount to
 * UpdateJobImplement().
 * 
 * 4     11/10/10 2:34p Ezhang
 * allow create more trip or cancel existing trip when the number of taxi changes.
 * added function cancelJob() and createJob()
 * 
 * 3     9/27/10 3:25p Ezhang
 * added more error code check.
 * 
 * 2     9/24/10 1:35p Ezhang
 * fixed the status match
 * 
 * 1     9/20/10 2:40p Ezhang
 * OSP 2.0 new method
 */
package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.account.user.ejb.UserAccount;
import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartItem;
import com.dds.pathfinder.callbooker.server.facade.ejb.CallbookerFacade;
import com.dds.pathfinder.callbooker.server.order.ejb.Order;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.osp.impl.FindJobImplement.UniformTripStatusCode;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.BookJobReq;
import com.dds.pathfinder.itaxiinterface.webservice.BookJobRes;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.UpdateJobListItem;
import com.dds.pathfinder.itaxiinterface.webservice.UpdateOrderByConfNumRes;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;

/**
 * @author ezhang
 * @version 2.0  14 Sep 2010
 * Implement backend for webservice method updateOrderByConfirmationNum
 * It uses confirmation number (aka booing_id in PF table Orders) to find all jobs and then 
 * modify the job. return error message if the job can't be modified.
 */
public class UpdateOrderByConfNumImplement extends OSPCartImplement {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	private Order order;
	ArrayList<JobListItem> jobList = new ArrayList<JobListItem>();
	ArrayList<UpdateJobListItem> returnJobList = new ArrayList<UpdateJobListItem>();
	 
	public enum UpdateOrderByConfNumErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_SESSIONID(2),
		DISPATCH_SYS_ERR(3),
		ORDER_NOT_FOUND(4),
		INVALID_CONFNUM(5),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private UpdateOrderByConfNumErrorCode(int c) {
			   code = c;
		}

		public int getCode() {
			   return code;
		}
	};
	
	
	public UpdateOrderByConfNumImplement(IAddressLookup addressLookUp, DataSource pfDataSource, Order order, UserAccount userAccount, CallbookerFacade facade, LoadDispatchParametersLocal cachedParam) {
		super(addressLookUp, pfDataSource, cachedParam);
		this.order = order;
		setUserAccount(userAccount); //added for C34595, to populate account detail into booking
	}
	/* (non-Javadoc)
	 * @see com.dds.pathfinder.itaxiinterface.osp.impl.OSPImplement#generateResponse(com.dds.pathfinder.itaxiinterface.webservice.BaseReq)
	 */
	public UpdateOrderByConfNumRes generateResponse(BaseReq request) {
		return generateUpdateOrderByConfNumResponse((BookJobReq)request);
	}
	
	private UpdateOrderByConfNumRes generateUpdateOrderByConfNumResponse(BookJobReq request){
		
		UpdateOrderByConfNumRes response = getDefaultUpdateResponse();
		if(!validateRequest(request, response)){
			   return response;
		}
		//validate all addresses
		upcaseRequest(request);
		PFAddressResponse pickupAddressRes = getWSAddressData(
				   appendPercentage(request.getPickupStreetName(), false), 
				   appendPercentage(request.getPickupStreetNr(), false),
				   appendPercentage(request.getPickupUnit(), true), //except for unit here, we want to use percentage to match
				   appendPercentage(request.getPickupRegion(), false), 
				   appendPercentage(request.getPickupLandmark(), false), 
				   PERCENTAGE_CHAR, PERCENTAGE_CHAR,  //organization and postal code
				   request.getTaxiCoID(), "");
		
		if(!isValidAddressResponse(pickupAddressRes) && isBothLandMarkAndAddressProvided(request.getPickupStreetName(), request.getPickupStreetNr(), request.getPickupLandmark())){
			   pickupAddressRes = getWSAddressData(
					   appendPercentage(request.getPickupStreetName(), false), 
					   appendPercentage(request.getPickupStreetNr(), false),
					   appendPercentage(request.getPickupUnit(), true), //except for unit here, we want to use percentage to match
					   appendPercentage(request.getPickupRegion(), false), 
					   PERCENTAGE_CHAR, PERCENTAGE_CHAR, PERCENTAGE_CHAR,  //organization and postal code
					   request.getTaxiCoID(), "");
			   
		   }
		
		PFAddressResponse closestAddressRes = getWSClosestAddressData(request.getPickupLat(), request.getPickupLon());
		
		PFAddressResponse setdownAddressRes = getWSAddressData(
				   appendPercentage(request.getDropoffStreetName(), false), 
				   appendPercentage(request.getDropoffStreetNr(), false), 
				   appendPercentage(request.getDropoffUnit(), false), 
				   appendPercentage(request.getDropoffRegion(),false),
				   appendPercentage(request.getDropoffLandmark(), false),
				   PERCENTAGE_CHAR, PERCENTAGE_CHAR, //organization and postal code
				   request.getTaxiCoID(), "");
		
		processUpdateJobList(request, response, pickupAddressRes, closestAddressRes, setdownAddressRes);
		
		return response;
	}
	
	private UpdateOrderByConfNumRes getDefaultUpdateResponse(){
		
		UpdateOrderByConfNumRes response = new UpdateOrderByConfNumRes();
		   
		response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
		response.setErrorCode(UpdateOrderByConfNumErrorCode.DEFAULT_ERROR.getCode());
		   
		return response;
	}
	
	private UpdateOrderByConfNumRes getDefaultUpdateResponse(BookJobRes res){
		if(res == null) return getDefaultUpdateResponse();
		
		UpdateOrderByConfNumRes response = new UpdateOrderByConfNumRes();
		   
		response.setRequestStatus(res.getRequestStatus());
		response.setErrorMessage(res.getErrorMessage());
		response.setErrorCode(res.getErrorCode());
		   
		return response;
	}
	
	
	
	/**
	 * Validate Update Order by Confirmation request.
	 * 
	 * @param request
	 *            the request to validate
	 * @param response
	 *            the response to update if error
	 * @return true if valid, false otherwise
	 */
	private boolean validateRequest(BookJobReq request, UpdateOrderByConfNumRes response) {
		
		if (request == null || response == null) {
			return false;
		}
		
		//C36130 Validate system id
		if(validateSystemId(pfDataSource, request.getSystemID(), request.getSystemPassword()) == false){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(UpdateOrderByConfNumErrorCode.NOT_AUTHENTICATED.getCode());
			return false;
		}
		//validate confirmation number
		if (request.getConfirmationNum() == null || request.getConfirmationNum().trim().length() == 0){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(UpdateOrderByConfNumErrorCode.INVALID_CONFNUM.getCode());
			return false;
		}
		//validate request type
		if( request.getRequestType()== null || request.getRequestType() != REQUEST_TYPE_UPDATE_JOB){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			return false;
		}
		
		//PF-14809 validate the confirmation number from the same system as it was created
		if(!validateConfirmationNumber(request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(UpdateOrderByConfNumErrorCode.INVALID_CONFNUM.getCode());
			return false;
		}
		return true;
	}
	/**
	 * get the job_id and job_status associated with the booking_id.
	 * 
	 * @param confNum
	 *            the request to get the jobs
	 * @param response
	 *            the response to update if error
	 * @return true if jobs found, false otherwise
	 */
	private void processUpdateJobList(BookJobReq request, UpdateOrderByConfNumRes response, PFAddressResponse pickupAddress,
			PFAddressResponse closestAddress,PFAddressResponse setdownAddress) {
		Connection con = null;
		PreparedStatement stmt = null;
        ResultSet rs = null;
        Long jobId = null;
        
        try{
        	con = pfDataSource.getConnection();
        	String query = " select jb.job_id, DECODE(jb.current_job_stage, 'H', DECODE(NVL(jb.locked_by_code, user), user, jb.prev_job_stage, 'H'),jb.current_job_stage)status, vs.vehicle_status," +
        					" jobpf.get_jobs_state_text(jobpf.get_ext_jobs_real_state(jb.job_id)) as job_status " + 
        					" from jobs jb, Orders od, vehicle_states vs" +
        					" where jb.order_id = od.order_id" +
        					" and vs.current_job_id (+) = jb.job_id" +
        					" and od.booking_id = " + request.getConfirmationNum() +
        					" order by status, vehicle_status";
        	logger.info("UpdateOrderByConfNum " + query);
        	stmt = con.prepareStatement(query);
        	rs = stmt.executeQuery();
        
        	int numOfTaxi = 0;
        	int cancelledJobs = 0;
        	while(rs.next()){
        		numOfTaxi++;
        		String job_status = rs.getString("job_status");
        		if (job_status != null && job_status.equalsIgnoreCase(UniformTripStatusCode.SERVICED_CANCELLED.getStatus()))
				{
					cancelledJobs++;
				}
        		String status = rs.getString("status");
        		String vStatus = rs.getString("Vehicle_Status");
        		jobId = rs.getLong("job_id");
        		
        		int tripUpdateStatus = 0;  //default is successfully updated
        		if(status.charAt(0)== JOB_STATUS_HOLD.charAt(0)){
        			tripUpdateStatus = 2; // The trip is locked by another user, the modification is not allowed
        		}  
        		else {
        			//update individual job
        			UpdateJobImplement updateJob = new UpdateJobImplement(order, userAccount, pfDataSource, addressLookUp, cachedParam);
        			if(getUserTelephone() != null) updateJob.setUserTelephone(getUserTelephone());
        			request.setTaxiRideID(jobId);
        			BookJobRes bres = new BookJobRes();
        			bres.setTaxiRideID(-1L);
     			    updateJob.generateUpdateJobResponse(request, bres, pickupAddress, closestAddress, setdownAddress);
        			if(bres.getErrorCode() != 0){
        				tripUpdateStatus = 3; //trip update failed
        			}
        			else{
        				tripUpdateStatus = 0;
        				if (status.charAt(0) == JOB_STATUS_VEHICLE.charAt(0)) {
                			//The trip is dispatched and new trip might be created (C34841)
                			tripUpdateStatus = 1; 
                		}
        			}
        		}
        		returnJobList.add(new UpdateJobListItem(jobId, tripUpdateStatus));
        		jobList.add(new JobListItem(jobId, status, vStatus));
        	}
        	numOfTaxi -= cancelledJobs; //only count the "not-canceled" job
        	
        	//check the number of Taxi changes after update all of them
        	//when no change then send response to client
        	if(request.getNumTaxis().intValue() == numOfTaxi){
        		generateUpdateJobListResponse(response, returnJobList);
        	}
        	else if (request.getNumTaxis().intValue() > numOfTaxi){
        		//need to create some new job(s)
        		createJob(request, response, pickupAddress, closestAddress, setdownAddress, cancelledJobs);
        		generateUpdateJobListResponse(response, returnJobList);
        	}
        	else{
        		//need to cancel some old job(s)		
        		jobId = null;
        		int i = 0;
        		//find job to cancel in following order 
        		// unmatched (un-offered) before matched (offered)
        		// matched before accepted
        		//??TODO: we're just checking sequentially here on an unordered job list? 
        		//		  also what about "More recently accepted trips shall be cancelled before less recently accepted trips."
    			String userName = getUserName(request);
        		for(int j =0; j<jobList.size(); j++){

        			if(i < numOfTaxi - request.getNumTaxis().intValue())
        			{
        				
        				if(jobList.get(j).getJobStatus().charAt(0)== JOB_STATUS_BOOKED.charAt(0) ||
        						jobList.get(j).getJobStatus().charAt(0)== JOB_STATUS_DESPATCHING.charAt(0)
        						){
        					jobId = jobList.get(j).getJobId();	
        					
        				}
        				else if (jobList.get(j).getJobStatus().charAt(0)== JOB_STATUS_VEHICLE.charAt(0)&&
        				jobList.get(j).getVehicleStatus().charAt(0) == VEHICLES_STATUS_TAXI_ACCEPTED.charAt(0)){
        					jobId = jobList.get(j).getJobId();
        					
        				}
        				if(jobId != null){
        					if(cancelJob(jobId, userName)){
        						i++;
        						//update the return response, removed canceled trip
        						for(int s = 0; s< returnJobList.size(); s++){
        							if(returnJobList.get(s).getTaxiRideID() == jobId){				
        								returnJobList.remove(s);
        							}
        						}
        						//reset
        						jobId = null;
        						
        						
        					}
        				}
        			}
        		}
        		generateUpdateJobListResponse(response, returnJobList);
        	}
        	
        }catch(SQLException se){
        	logger.error("process Update Order by Confirmation Number failed:", se);
			response.setErrorCode(UpdateOrderByConfNumErrorCode.DISPATCH_SYS_ERR.getCode());
        }finally{
        	if(rs != null) try{rs.close();}catch(SQLException ignore){};
        	if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
        	if(con != null) try{con.close();}catch(SQLException ignore){};
        }
        
	}
	
	/**
	 * This is used by Update Order only, to create more jobs when it needs.
	 * @param request
	 * @param response
	 * @param cancelledJobs 
	 * 				number of canceled jobs in the booking under modification.
	 */
	@SuppressWarnings("unchecked")
	private void createJob(BookJobReq request, UpdateOrderByConfNumRes response, PFAddressResponse pickupAddressRes,
			PFAddressResponse closestAddressRes,PFAddressResponse setdownAddressRes,
			int cancelledJobs) {
		
		// If the address is invalid, don't continue 
		if (!validateAddresses(request, pickupAddressRes, closestAddressRes)) {
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_ADDRESS);
			response.setErrorCode(BookJobErrorCode.PICKUP_ADDR_NOT_FOUND.getCode());
			return;
		}
		PFAddressData pickupAddress = getMatchAddress(pickupAddressRes, request.getPickupStreetName(), request.getPickupStreetNr(), request.getPickupRegion());
		PFAddressData closestAddress = getFirstAddress(closestAddressRes);
		PFAddressData setdownAddress = getMatchAddress(setdownAddressRes, request.getDropoffStreetName(), request.getDropoffStreetNr(), request.getDropoffRegion());
		
		if(closestAddress!= null)
		{
			logger.error("generateUpdateJobResponse Retrieve closest address");
			// Get detail of the closest address
			closestAddressRes = getWSAddressData(
			closestAddress.getStreetName().getValue(),
			closestAddress.getStreetNumber().getValue(), 
			closestAddress.getUnitNumber().getValue(),
			closestAddress.getRegionAbbreviation().getValue(),
			closestAddress.getLandmarkName().getValue(),
				   PERCENTAGE_CHAR, PERCENTAGE_CHAR, //organization and postal code
				   request.getTaxiCoID(), "");
			closestAddress = getFirstAddress(closestAddressRes);
		}
		
		modifyPickupClosestAddresses(pickupAddress, closestAddress);
		
		String userName = getUserName(request);
		try{
			// Order order = orderHome.create();
	    	Collection<?> results = null;
		
	    	modifyPickupClosestAddresses(pickupAddress, closestAddress);
	    	AddressItem closestAddrItem = null;
			if( pickupAddress == null && closestAddress != null)
			{
			    closestAddrItem = getAddressItem(closestAddress, request.getTaxiCoID());
			}
	    	//generateCartItems will only create new job if the requested number of job is less than what's inside the job list
	    	//since "canceled" job still exists in the job list, we need to add that number in order to create new job
	    	Vector<CartItem> cartItems = generateCartItems(request.getNumTaxis().intValue() + cancelledJobs, 
	    			request, pickupAddress, setdownAddress, closestAddrItem);
	    	
	    	BookJobRes bookRes = getDefaultResponse();
	    	if(!validateCartItems(cartItems, bookRes)){
	    		response = getDefaultUpdateResponse(bookRes);
	    		return;
	    	}
	    	
	     
	    	results = order.multipleTrips_OrderEJB(userName, "", ACTION_TYPE_MODIFYBOOKING, cartItems);
	    	if (results != null && results.size() > 0){
	    		Vector<CartItem> carts = (Vector<CartItem>)results;
	    		int nbOfJobs = carts.size();
	    		for(int i=0; i< nbOfJobs; i++){
				   CartItem cartItem = carts.get(i);
				   if(cartItem != null ){
					   long jobID = Integer.parseInt(cartItem.getJobId());
					   boolean foundId = false;
					   for(int j=0; j<returnJobList.size()&& foundId == false; j++){
						   if(returnJobList.get(j).getTaxiRideID() == jobID){
							   //It's a old jobID so we move on
							   foundId = true;
						   }
					   }
					   if(foundId == false){
						   //add new job Id to the return job List
						   returnJobList.add(new UpdateJobListItem(jobID, 0));   
					   }
					   			  
				   }
			   }
	    	}
		}catch(Exception e){
			logger.error("generateUpdateOrderByConfNumResponse failed to create new jobs", e);
		}
	
	}
	
	private void generateUpdateJobListResponse(UpdateOrderByConfNumRes response, ArrayList<UpdateJobListItem> jobList){
		
    	int nbOfJobs = jobList.size();
    	if(nbOfJobs > 0){
    		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
        	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
        	response.setErrorCode(UpdateOrderByConfNumErrorCode.NO_ERROR.getCode());
    		UpdateJobListItem[] updateJobList = new UpdateJobListItem[nbOfJobs];
    		jobList.toArray(updateJobList);
    		response.setUpdateJobList(updateJobList);
    	}
    	else{
    		response.setErrorCode(UpdateOrderByConfNumErrorCode.ORDER_NOT_FOUND.getCode());
    	}
	}
	/**
	 * @param jobId
	 * @return true if job canceled return false if failed
	 */
	private boolean cancelJob(Long jobId, String userName) {
		Connection con = null;
        CallableStatement cs = null;
        boolean status = true;
		try{
			//cancel job now
			con = pfDataSource.getConnection();
			cs = con.prepareCall("{ call despatch.cancel(?,?,?,?,?)}");
	       	cs.setLong(1, jobId);
	       	cs.setString(2, "Cancel Job by OSP update order");       	   
       	    cs.setNull(3, Types.VARCHAR);		//log_classes.log_type%TYPE DEFAULT NULL,
       	    cs.setString(4, "N");  				//p_is_redesp IN VARCHAR2 DEFAULT 'N',
       	    cs.setString(5, userName);
	       	cs.execute(); 
			
		}catch(SQLException se){
		   	logger.error("job cancellation failed with exception", se);
		   	status = false;
	       	
		}finally{
       		if(cs != null) try{cs.close();}catch(SQLException ignore){};
       		if(con != null) try{con.close();}catch(SQLException ignore){};
		}
		return status;
	}
	
	
	
	private Vector<CartItem> generateCartItems (int nbOfJob, BookJobReq request, PFAddressData pickupAddress, PFAddressData setdownAddress, AddressItem closestAddrItem){
		   Vector<CartItem> carts = new Vector<CartItem>();
		   if(nbOfJob <=0) nbOfJob = 1;
		   
		   for(int i = 0; i< nbOfJob; i++){
				
				CartItem cartItem = convertRequest2CartItem(request, pickupAddress, setdownAddress, closestAddrItem); 
				if(cartItem != null && i < jobList.size()){
					cartItem.setJobId("" + jobList.get(i).getJobId());
				}
				else{
					cartItem.setJobId(""); //this will create new jobs.
				}
				cartItem.setAlready_Address_job(true);
				carts.add(cartItem);
			
			}
		   return carts;
	   }
	
	//PF-14809 validate confirmation number whether from the same system
	private boolean validateConfirmationNumber(BookJobReq request) {
	
		boolean res = false;
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;
		String referenceType = null;
		int systemId = request.getSystemID();
		
		//check job_references table if request job match the reference type
		referenceType = getSystemReference(request.getSystemID());
		
		if (referenceType != null && !referenceType.isEmpty()) {
		   String query = "select jb.job_id, jr.reference_type from jobs jb, orders od, job_references jr "
				 +  " where jb.order_id = od.order_id" 
				 + " and od.booking_id = " + request.getConfirmationNum()
				 + " and jb.job_id = jr.job_id";
				
				
		
		
		   try{
			   	con = pfDataSource.getConnection();
				stmt = con.createStatement();
				result = stmt.executeQuery(query);
				//PF-14809 the job is in job_references table, if the request from match the job's reference then return true
				if (result.next() ) {
					if(result.getString("reference_type").equalsIgnoreCase(referenceType)) {
						res = true; //GFC/MB system cancel GFC/MB job PF-14809
					}
							
				}
				//PF-14809 the job is not in job_references table, if the request from webbooker then return true
				else if(systemId == ExternalSystemId.SYSTEM_ID_WEB_BOOKER.getSystemId() || systemId == ExternalSystemId.SYSTEM_ID_UDI_USER.getSystemId() || isAccessableInternalJobs(systemId)) {
					res = true; //webbooker cancel webbooker job
				}
				
	       }catch(SQLException se){
		       	logger.error("validateConfirmationNumber() failed with exception", se);
		       	
	       }finally{
	    	    if (result != null)try {result.close();} catch (SQLException ignore) {};
	    	    if (stmt != null)try {stmt.close();} catch (SQLException ignore) {};
	       		if(con != null) try{con.close();}catch(SQLException ignore){};
	       		
	       }
		}
		
		
		return res;
	}
	
}
