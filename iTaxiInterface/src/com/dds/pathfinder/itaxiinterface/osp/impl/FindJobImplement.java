/****************************************************************************
 *
 *		   		    Copyright (c), 2010
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/FindJobImplement.java $
 * 
 * PF-16303, 21/11/14, DChen, move 2 car status to be cancelled.
 * 
 * PF-15903 changed the job end time in setResponseTripSubStatus().
 *  
 * PF-15827, 15/07/14 DChen, OSP added redispatched related job ID return.
 * 
 * 38    3/12/14 3:32p Dchen
 * Added osp_users table instead of hard coding.
 * 
 * 37    2/24/14 3:15p Dchen
 * PF-15803, UDI user should retrieve all other jobs not booked by UDI.
 * 
 * 36    1/23/14 12:08p Ezhang
 * PF_15848 parse all possible completed status and return code. 
 * commented out getPrevTripStatus(), mobile booker client will handle it at UI site
 * 
 * 35    11/21/13 4:59p Ezhang
 * fixed recallcriteria check for joblist to make it backward compatible
 * 
 * 34    11/20/13 4:28p Ezhang
 * fixed typo
 * 
 * 33    11/20/13 10:57a Ezhang
 * PF-15582 Recall job by joblist added <jobEndDateTime> in response
 * for token payment after job completed.
 * 
 * 32    10/30/13 4:09p Ezhang
 * 
 * 31    10/23/13 3:01p Ezhang
 * PF-15615, 15627, added detailTripStatusUniformCode and new recall criteria.
 * PF-15289 return previous status if current status is onhold.
 * 
 * 30    9/03/13 4:10p Dchen
 * PF-15569, null exception when validating setdown address.
 * 
 * 29    3/27/13 4:40p Dchen
 * PF-15241, limit number of jobs search query return.
 * 
 * 28    3/12/13 4:34p Dchen
 * Added pf parameters load bean.
 * 
 * 27    10/24/12 5:45p Ezhang
 * PF-14809 added job reference type check so GFC can't review MB's job, vice versa.
 * 
 * 26    7/31/12 4:45p Ezhang
 * PF-14630 updated convertCart2Job() to add landmark and unit number to response.
 * 
 * 25    12/28/11 3:08p Yzhan
 * C36130 - changed NAV_TYPE from MAPPOINT to DWNAV
 * 
 * 24    9/12/11 2:21p Yyin
 * C36130- Fixed forced address trips with OSP.
 * 
 * 23    2/12/11 10:01a Ezhang
 * C36130 Added SystemId validation
 * 
 * 22    8/18/11 3:32p Dchen
 * upgrade to 3.75.
 * 
 * 21    5/04/11 7:15p Mkan
 * C35323, added missing job status mapping.
 * WebBooker was not able to cancel job because of missing status "Vehicle
 * Offered".
 * 
 * 20    4/19/11 6:28p Dchen
 * C35277, speed webbooker order tracking.
 * 
 * 19    4/02/11 6:46p Mkan
 * - added "SERVICED_CANCELLED" IN UniformTripStatusCode for future use,
 * it maps to "Serviced" status at the moment.
 * 
 * 18    12/23/10 7:00p Mkan
 * C34847
 * 
 * UniformTripStatusCode
 * - fixed status 4 to match ARRIVED instead of POB
 * - added to map POB with status 5
 * 
 * convertCart2Job()
 * - added to map the new states
 * 
 * 17    12/21/10 2:16p Mkan
 * C34781 
 * - generateSearchSQL(): only limit time period if neither JOB ID or
 * Confirmation number was provided
 * - renamed setTimeAndDateFilter() to getTimeAndDateFilter()
 * - added getUserLookAhead() to get look ahead values for specified user
 * - added getUserQueryTimePeriod() to get look behind values for
 * specified user
 * 
 * 16    12/09/10 2:55p Ajiang
 * 
 * 15    12/09/10 1:52p Ajiang
 * 34608, 34621
 * 
 * 14    10/07/10 9:00a Ezhang
 * fixed tripStatusUniformCode case for completed job.
 * 
 * 13    10/04/10 2:46p Ezhang
 * fixed the addresslookup without correct taxi company ID.
 * 
 * 12    10/04/10 10:48a Ezhang
 * added job confirmation number, pickup and dropoff address latitude and longitude.
 * 
 * 11    10/01/10 11:05a Ezhang
 * query sort by pickup time from newest to oldest trip.
 * 
 * 10    9/29/10 2:09p Ezhang
 * revert webuserID from required to optional to allow user recall trip without login.
 * 
 * 9     9/28/10 1:34p Ezhang
 * 
 * 8     9/28/10 1:24p Ezhang
 * make webuserID the required field to prevent user recall other people's trip
 * 
 * 7     9/27/10 11:25a Ajiang
 * Added est and estimated distance to the recall job response
 * 
 * 6     9/20/10 2:32p Ezhang
 * OSP 2.0 added errorCode. eta etc. code commented out for now.
 * 
 * 5     4/15/10 11:47a Mkan
 * convertCart2Job()
 *   - fixed passenger name not being booked
 *   - update for getAdviseArrivalFlag signature change
 * setBookingDTM()  
 *   - fix for booking date/time not booked correctly
 * added comments
 * 
 * 4     3/03/10 4:39p Dchen
 * OSP interface.
 * 
 * 3     2/12/10 5:15p Dchen
 * Modified for web booker using.
 * 
 * 2     1/26/10 5:43p Dchen
 * OSP interface.
 * 
 * 1     1/22/10 4:52p Dchen
 * OSP interface.
 * 
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartModel;
import com.dds.pathfinder.callbooker.server.cart.model.StopPoint;
import com.dds.pathfinder.callbooker.server.citynav.model.TripAddr;
import com.dds.pathfinder.callbooker.server.controller.event.SysUserEvent;
import com.dds.pathfinder.callbooker.server.facade.ejb.CallbookerFacade;
import com.dds.pathfinder.callbooker.server.jobsearch.ejb.JobSearch;
import com.dds.pathfinder.callbooker.server.jobsearch.ejb.JobSearchDAO;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.callbooker.server.sysuser.model.QueryTimeFrameItem;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.JobListItem;
import com.dds.pathfinder.itaxiinterface.webservice.RecallJobsRequest;
import com.dds.pathfinder.itaxiinterface.webservice.RecallJobsResponse;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;



public class FindJobImplement extends OSPCartImplement {

	private Logger logger = LogManager.getLogger(this.getClass());
	//protected DataSource pfDataSource;
	private JobSearch jobSearch;
	private CallbookerFacade facade;
	
	public final static int MAX_ROWNUM_JOB_QUERY = 300;
	public final static int RECALL_BY_JOBLIST = 10;  //PF-15627 new recall criteria
	public final static int MAX_NUM_RECALL_BY_JOBLIST = 15; //PF-15627 only allow 15 in recall job list
	
	public enum FindJobErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_SESSIONID(2),
		NO_MATCH_TRIP(3),
		INVALID_TAXI_COMPID(4),
		INVALID_RIDE_ID(5),	
		INVALID_TAXI_RIDEID(6),
		INVALID_WEBUSER_ID(7),
		INVALID_PASSENGER_NAME(8),
		INVALID_PHONENUM(9),
		INVALID_PCIKUP_ADDRESS(10),
		INVALID_ACCT_CODE(11),
		INVALID_RECALL_TRIPTYPE(12),
		INVALID_RECALL_CRITERIA(13),
		INVALID_FROMTRIP_RSVTIME(14),
		INVALID_TOTRIP_RSVTIME(15),
		NO_RECALL_CRITERIA_PROVIDED(16),
		INVALID_JOB_LIST(17),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private FindJobErrorCode(int c) {
			   this.code = c;
		}

		public int getCode() {
			   return code;
		}
	};
	
	//string is combined job and vehicle status. it's just a info what we match to.
	public enum UniformTripStatusCode{
		UNMATURE("Booked", 1), //Trip is un-matured (the trip is for future pickup and has not yet been dispatched),
		MATURED("Under dispatch", 2),	//Trip is matured and awaiting "dispatch Under dispatch Group Dispatch" or "Under dispatch Stand"
		OFFERED("Vehicle Offered", 2), //Job offered to car but driver has not yet accept (C35323)
		IN_TRANSIT("Vehicle Accepted", 3), //The taxi is on the way to pick up the passenger
		ARRIVED("Vehicle Arrived", 4),  //The car has arrived but hasn’t picked up the passenger (C34847)
		SERVICED_LOAD("Vehicle LOAD", 5), //The car has picked up the passenger (C34847)
		SERVICED_COMP("Completed", 5), // The trip has been serviced
		SERVICED_CANCELLED("Completed Cancelled", 5); // The trip has been canceled (OSP spec does not have the status defined now but we put it here for future use)
		
		private int code;
		private String status;
		
		private UniformTripStatusCode(String s, int c) {
			   this.code = c;
			   this.status = s;
		}

		public int getCode() {
			   return code;
		}
		public String getStatus(){
			return status;
		}
		
	};
	
	//EF-15615 Added more detail Trip Status
	public enum UniformTripStatusSubCode{
		SERVICED_LOAD("Vehicle LOAD", 1), //The car has picked up the passenger
		SERVICED_METEROFF("Completed", 2), //The car stop and trip paid complete
		SERVICED_ALTERED("A", 2), //The car stop and trip paid complete
		SERVICED_2CAR("2", 2), //The car stop and trip paid complete
		SERVICED_CANCELLED("C", 3), // The trip has been canceled
		SERVICED_SCRUBBED("S", 3), //Trip has been scrubbed
		SERVICED_NO_SHOW("C", 4), // no show this is not happened because status return as Completed Cancelled as well
		SERVICED_FORCED_CLEAR("F", 5), //The trip is completed by force
		SERVICED_OTHERS("Completed Others", 6); // to be ignored.
		
		private int code;
		private String status; //this matches with PFDB jobs.record_change_type
		
		private UniformTripStatusSubCode(String s, int c) {
			   this.code = c;
			   this.status = s;
		}

		public int getCode() {
			   return code;
		}
		public String getStatus(){
			return status;
		}
		
	};
	
	public FindJobImplement(DataSource pfDataSource, JobSearch jobSearch, CallbookerFacade facade, IAddressLookup addressLookup , LoadDispatchParametersLocal cachedParam){
		super(addressLookup, pfDataSource, cachedParam);
		this.facade = facade; //add to retrieve trip matrix such as trip price
		this.jobSearch = jobSearch;

	}
	
	public RecallJobsResponse generateResponse(BaseReq request) {
		RecallJobsResponse response = getDefaultResponse(request);
		
		if(isValidRecallJobRequest((RecallJobsRequest)request, response)){
			generateJobsResponse((RecallJobsRequest)request, response);
		}
		return response;
	}
	
	/**
	 * @param request
	 * @return
	 */
	private RecallJobsResponse getDefaultResponse(BaseReq request) {
		RecallJobsResponse response = new RecallJobsResponse();
		response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
		response.setErrorCode(FindJobErrorCode.DEFAULT_ERROR.getCode());
		   
		return response;
	}

	private boolean isValidRecallJobRequest(RecallJobsRequest request, RecallJobsResponse response){
		if(request == null ) return false;
		//validate taxi company id
		if(request.getTaxiCoID() == null ||request.getTaxiCoID() <= 0 || request.getTaxiCoID()> 999999999){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
			response.setErrorCode(FindJobErrorCode.INVALID_TAXI_COMPID.getCode());
			return false;
		}
		//C36130 validate the system id and password
		if(validateSystemId(pfDataSource, request.getSystemID(), request.getSystemPassword()) == false){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(FindJobErrorCode.NOT_AUTHENTICATED.getCode());
			return false;
		}
		
		
		//validate recallCriteria
		if(request.getRecallCriteria() == null || request.getRecallCriteria()<=0 || request.getRecallCriteria()>RECALL_BY_JOBLIST){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(FindJobErrorCode.INVALID_RECALL_CRITERIA.getCode());
			return false;
		}
		
		//valid job id based on system if recall trip has job_id
		if(request.getTaxiRideID() != null && request.getTaxiRideID() != 0 
				&& !isValidateJobId(request.getTaxiRideID(), request.getSystemID())) {
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_JOB_ID);
			response.setErrorCode(FindJobErrorCode.INVALID_TAXI_RIDEID.getCode());
			return false;
		}
	
		//PF-15635 valid job list if recall by job list, only allow  to 15 jobs in the list.
		if(request.getRecallCriteria() == RECALL_BY_JOBLIST) {
			if(!isRecallByJobList(request)) {
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
				response.setErrorCode(FindJobErrorCode.INVALID_JOB_LIST.getCode());
				return false;
			}
		}
		
		return inputSomeCriteria(request);
	}
	
	private boolean inputSomeCriteria(RecallJobsRequest request){
		long jobID = (request.getTaxiRideID() == null)? 0:request.getTaxiRideID();
		String clientName = request.getPassengerName();
		String phoneNumber = request.getPhoneNumber();
		String streetName = request.getPickupStreetName();
		String streetNumber = request.getPickupStreetNumber();
		String unitNumber = request.getPickupUnitNumber();
		String landMark = request.getPickupLandmark();
		String accountCode = request.getAccountCode();
		String tripType = request.getRecallTripType();
		String webUserID = request.getWebUserID();
		String confirmationNumber = request.getConfirmationNum();
		String jobList = request.getJobList();
		
		return (jobID > 0 
				|| isSearchInput(clientName) 
				|| isSearchInput(phoneNumber)
				|| isSearchInput(streetName)
				|| isSearchInput(streetNumber)
				|| isSearchInput(unitNumber)
				|| isSearchInput(landMark)
				|| isSearchInput(accountCode)
				|| (isSearchInput(tripType) && isValidTripType(tripType))
				|| isSearchInput(confirmationNumber)
				|| isSearchInput(webUserID)
				|| isSearchInput(jobList)
				);
	}
	
	private void generateJobsResponse(RecallJobsRequest request, RecallJobsResponse response){
		CartItem cart = generateSearchSQL(request);
		if(cart == null || jobSearch == null) {
			return;
		}
		try {	
			CartModel jobs =  jobSearch.getSearchJobDetails_EJB(request.getSessionID(), "", Arrays.asList(new CartItem[]{cart}), JobSearchDAO.JOB_RECALL_FROM_OSP);
			response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
	    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
	    	response.setErrorCode(FindJobErrorCode.NO_ERROR.getCode());
			response.setNofJobs(0);
			int compID = request.getTaxiCoID();
			if(jobs != null && jobs.getSize() > 0) {
				convertJobsResponse(request,jobs, response, compID);
			}
		}catch(Exception e){
			logger.error("generateJobsResponse failed..", e);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void convertJobsResponse(RecallJobsRequest request, CartModel jobs, RecallJobsResponse response, int compID){
		Vector<CartItem> carts = (Vector<CartItem>)jobs.getCart();
		int nbOfJobs = carts.size();
		JobListItem[] jobList = new JobListItem[nbOfJobs];
		for(int i=0; i<nbOfJobs; i++){
			JobListItem job = new JobListItem();
			CartItem cart = carts.get(i);
			cart.setTaxiCompanyID(compID);
			convertCart2Job(cart, job, compID, request);
			jobList[i] = job;
		}
		response.setNofJobs(nbOfJobs);
		response.setJobList(jobList);
	}
	

	
	/**
	 * Convert PF cartItem into OSP JobListItem
	 * @param cart	PathFinder cart item
	 * @param job	OSP JobListItem to update
	 */
	private void convertCart2Job(CartItem cart, JobListItem job, int compID, RecallJobsRequest req){
		if(cart == null || job == null) {
			return;
		}
		StopPoint stopPoint = cart.getStopPoint(0);
		long jobID = 0;
		int bookingID = 0;
		boolean isRecallByJobList = isRecallByJobList(req);
		try{
			jobID = Long.parseLong(cart.getJobId());
		}catch(NumberFormatException ne){
			logger.warn("parse job ID failed..", ne);
		}
		
		
		// get booking_if for each job
		bookingID = getBookingId(Integer.parseInt(cart.getJobId()));
		if(bookingID != 0){
			job.setConfirmationNum(String.format("%d", bookingID));
		}
		job.setTaxiRideID(jobID);
		job.setDispatchedCar(cart.getVehicleCallsign());
		job.setTripStatus(cart.getState_Text());
		job.setPassengerName(stopPoint.getContactInfo().getFamilyName());	//note: job search uses family name not given name
		job.setPhoneNumber(stopPoint.getContactInfo().getTelephone());
		job.setPickupStreetName(stopPoint.getPickupAddress().getStrName());
		job.setPickupStreetNumber(stopPoint.getPickupAddress().getStrNum());
		
		if(cart.getrelatedJobId() != null && cart.getrelatedJobId().trim().length() > 0) job.setRdspRelatedJobID(cart.getrelatedJobId());
		
		//new OSP add following field to get ETA and distance
		job.setPickupRegion(stopPoint.getPickupAddress().getRegion());
		
		job.setDropoffStreetName(stopPoint.getSetdownAddress().getStrName());
		job.setDropoffStreetNumber(stopPoint.getSetdownAddress().getStrNum());
		job.setDropoffRegion(stopPoint.getSetdownAddress().getRegion());
		//PF-14630 Added landmark and unit number
		job.setPickupLandmark(stopPoint.getPickupAddress().getBuilding_Name());
		job.setPickupUnitNumber(stopPoint.getPickupAddress().getunitNr());
		job.setDropoffLandmark(stopPoint.getSetdownAddress().getBuilding_Name());
		job.setDropoffUnitNumber(stopPoint.getSetdownAddress().getunitNr());
		
		job.setAccountCode(cart.getCreditCardNr());
		job.setPickupDate(Utilities.convertDB2OSPDateFormat(cart.getDate()));
		job.setPickupTime(Utilities.convertDB2OSPTimeFormat(cart.getTime()));
		job.setAdviseArrival(AdviseArrivalTypesImplement.AdviseArrivalType.NoAdvise.toOSPValue());
		setBookingDTM(cart, job);
		if(stopPoint != null){
			job.setAdviseArrival(getAdviseArrivalFlag(pfDataSource, stopPoint, stopPoint.getContactInfo()));
		}
		//convert PF tripStatus to UniformCode
		String tripStatus = cart.getState_Text();
		
		//PF-15289
		//PF-15848 Mobile will use previous status if the status does not have uniform status code
		/*
		if(tripStatus.startsWith("On Hold")) {
			//get previous status
			tripStatus = getPrevTripStatus(job);
			
		}*/
		
		if(tripStatus.equalsIgnoreCase(UniformTripStatusCode.UNMATURE.getStatus())){
			job.setTripStatusUniformCode(UniformTripStatusCode.UNMATURE.getCode());
		}
		else if(tripStatus.startsWith(UniformTripStatusCode.MATURED.getStatus())){
			job.setTripStatusUniformCode(UniformTripStatusCode.MATURED.getCode());
		}
		else if(tripStatus.equalsIgnoreCase(UniformTripStatusCode.ARRIVED.getStatus())){
			job.setTripStatusUniformCode(UniformTripStatusCode.ARRIVED.getCode());
		}
		else if(tripStatus.equalsIgnoreCase(UniformTripStatusCode.IN_TRANSIT.getStatus())){		
			job.setTripStatusUniformCode(UniformTripStatusCode.IN_TRANSIT.getCode());
		}	
		else if (tripStatus.equalsIgnoreCase(UniformTripStatusCode.OFFERED.getStatus())) {
			job.setTripStatusUniformCode(UniformTripStatusCode.OFFERED.getCode());
		}
		else if(tripStatus.equalsIgnoreCase(UniformTripStatusCode.SERVICED_LOAD.getStatus())){
			job.setTripStatusUniformCode(UniformTripStatusCode.SERVICED_LOAD.getCode());
			//EF-15615
			if(isRecallByJobList) {
				job.setDetailTripStatusUniformCode(UniformTripStatusSubCode.SERVICED_LOAD.getCode());
			}
		}//PF-15848 parsing the completed trip status for status sub code
		else if (tripStatus.startsWith(UniformTripStatusCode.SERVICED_COMP.getStatus())){ 
			job.setTripStatusUniformCode(UniformTripStatusCode.SERVICED_COMP.getCode());
			if(isRecallByJobList) {
				setResponseTripSubStatus(job);
				
			}
		}
		
		job.setJobExtraInfo1(cart.getExtraInfo1());
		job.setJobExtraInfo2(cart.getExtraInfo2());
		job.setJobExtraInfo3(cart.getExtraInfo3());
		job.setJobExtraInfo4(cart.getExtraInfo4());
		job.setJobExtraInfo5(cart.getExtraInfo5());
		job.setSpExtraInfo1(stopPoint.getExtraInfo1());
		
		setResponseCarGPS(job); //PF-14810 get car lat and long
		
		//PF-15627 get detail Job Status for Mobile booker
		if(!isRecallByJobList)
		{
			//PF-15627 don't return pickup and dropoff address GPS for recall by joblist
			//can add later, right now just try to save some time and resource.
			//add ETA and estimated Distance to response
		
			PFAddressResponse pickupAddressRes = getWSAddressData(
					   appendPercentage(job.getPickupStreetName(), false), 
					   appendPercentage(job.getPickupStreetNumber() , false),
					   appendPercentage(job.getPickupUnitNumber() , true), //except for unit here, we want to use percentage to match
					   appendPercentage(job.getPickupRegion(), false), 
					   PERCENTAGE_CHAR, PERCENTAGE_CHAR, PERCENTAGE_CHAR,  //organization and postal code
					   compID, "");
			
			//PFAddressResponse closestAddressRes = getWSClosestAddressData(stopPoint.getPickupAddress().getLatitude() , stopPoint.getPickupAddress().getLongitude());
			
			PFAddressResponse setdownAddressRes = getWSAddressData(
					   appendPercentage(job.getDropoffStreetName(), false), 
					   appendPercentage(job.getDropoffStreetNumber(), false), 
					   appendPercentage(job.getDropoffUnitNumber(), false), 
					   appendPercentage(job.getDropoffRegion(),false),
					   appendPercentage(job.getDropoffLandmark(), false),
					   PERCENTAGE_CHAR, PERCENTAGE_CHAR, //organization and postal code
					   compID, "");
			
			PFAddressData pickupAddress = getMatchAddress(pickupAddressRes, job.getPickupStreetName(), job.getPickupStreetNumber(), job.getPickupRegion());
			AddressItem pickup = null;
			if(pickupAddress != null){
				pickup = getAddressItem(pickupAddress, compID);
				if(pickup != null){
					job.setPickupLat(pickup.getLatitude());
					job.setPickupLon(pickup.getLongitude());
				}
				setEstimatedDistance(job);
				if(cart.getETATime() != null && cart.getETATime().length() > 0)
				{
					job.setEta1(Utilities.convertDB2OSPTimeFormat(cart.getETATime()));
				}
			}
			
			PFAddressData setdownAddress = getMatchAddress(setdownAddressRes, job.getDropoffStreetName(), job.getDropoffStreetNumber(), job.getDropoffRegion());
			if(setdownAddress != null){
		    	AddressItem setdown = getAddressItem(setdownAddress, compID);
		    	if(setdown != null){
			    	job.setDropoffLat(setdown.getLatitude());
			    	job.setDropoffLon(setdown.getLongitude());
		    	}
		    }
		
		}
		
	}
	
	private void setEstimatedDistance(JobListItem job)
	{
		double mPi = 3.14159265358979323846;
		/*
		int unitNone = 1;
		int	unitKm = 110;
		int unitFeet = 365000;
		int unitMiles = 69;
		*/
		int unitMeters = 110000;
		long estimateDistance = 0;
		
		double dLat;
		double dLon;
		double avgLat;

		
		if (job.getCarLatitude() == null || job.getCarLongitude() == null ||
			job.getPickupLat() == null || job.getPickupLon() == null)
		{
			return;
		}
		dLat = job.getCarLatitude() - job.getPickupLat();
		avgLat = (job.getCarLatitude() + job.getPickupLat()) / 2;
		dLon = (job.getCarLongitude() - job.getPickupLon()) * java.lang.Math.cos((mPi * avgLat) / 180.0);
		
		// unit in Meters
		estimateDistance = (long) (java.lang.Math.abs(dLat) + java.lang.Math.abs(dLon)) * unitMeters;
		
		job.setEstimateDistance(estimateDistance);
	}
	
	private void setResponseCarGPS(JobListItem job){
		if(pfDataSource != null && job.getTaxiRideID() > 0){
			Connection con = null;
			Statement stmt = null;
	        ResultSet rs = null;
			try{
				con = pfDataSource.getConnection();
		        stmt = con.createStatement();
				rs = stmt.executeQuery("select vs.latitude, vs.longitude from jobs j, vehicle_states vs where j.vehicle_id = vs.vehicle_id and j.job_id = "+job.getTaxiRideID());
				if(rs.next()){
					double latitude = rs.getDouble("latitude");
					double longitude = rs.getDouble("longitude");
					if(isValidDoubleValue(latitude) && isValidDoubleValue(longitude) 
							&& latitude != 0 && longitude != 0){
						job.setCarLatitude(latitude);
						job.setCarLongitude(longitude);
					}
				}
			}catch(SQLException se){
				se.printStackTrace();
				logger.error("setResponseCarGPS failed: ", se);
			}finally{
			
				if (rs != null) try{rs.close();}catch(SQLException ignore){};
	    	    if (stmt != null) try{stmt.close();}catch(SQLException ignore){};
	    	    if(con != null) try{con.close();}catch(SQLException ignore){};
			}
		}
	}
	
	private void setBookingDTM(CartItem cart, JobListItem job){
		String bookingDate = cart.getBookDate();
		String bookingTime = cart.getBookTime();
		
		if(bookingDate == null || bookingDate.trim().length() == 0){
			job.setBookingDateTime(job.getPickupDate()+ " " +job.getPickupTime());
		}else{
			bookingDate = Utilities.convertDB2OSPDateFormat(bookingDate);
			bookingTime = Utilities.convertDB2OSPTimeFormat(bookingTime);
			job.setBookingDateTime(Utilities.composeOSPDefaultDate(bookingDate, bookingTime));
		}
	}
	


	/**
	 * Generate search query to recall jobs. The query is copied from callbooker.client.SearchView.
	 * @param request	the recall job request from OSP
	 * @return CartItem that contains the search query
	 */
	private CartItem generateSearchSQL(RecallJobsRequest request){
		long jobID = (request.getTaxiRideID() == null)? 0:request.getTaxiRideID();
		String clientName = request.getPassengerName();
		String phoneNumber = request.getPhoneNumber();
		String streetName = request.getPickupStreetName();
		String streetNumber = request.getPickupStreetNumber();
		String unitNumber = request.getPickupUnitNumber();
		String landMark = request.getPickupLandmark();
		String accountCode = request.getAccountCode();
		String tripType = request.getRecallTripType();
		
		StringBuffer sb = new StringBuffer();
		String user = getUserName(request.getSystemID(), request.getSessionID());
		String webUserID = request.getWebUserID();
		String confirmationNumber = request.getConfirmationNum();
		String jobList = request.getJobList();
		
		//PF-14809
		String systemReference = getSystemReference(request.getSystemID());
		
		//PF-15627
		boolean isRecallByJobList = isRecallByJobList(request);
		
		// the following sql is supposed to be synchronized with the one in web booker SearchJobCommand.java
		// and both sql are sent to JobSearchDAO, so, if add some new column here, please modify SearchJobCommand as well.
        sb.append("SELECT od.order_id, jb.shared_ride_state, jb.JOB_ID, \n");
        if( isRecallByJobList) {
        	sb.append(" v.CALLSIGN, \n");
        }
        sb.append(" PASSENGER_NAME,STREET_NAME,STREET_NR, sp.forced_address, ara.area_name, \n");
		sb.append(" bld.region_abbreviation, nvl(bld.post_code, bf.post_code) post_code, \n");
        sb.append(" TO_CHAR(jb.PICKUP_DTM_TAKER_QUOTE,'DD,MM,YYYY') DT, \n");
        sb.append(" TO_CHAR(jb.PICKUP_DTM_TAKER_QUOTE,'HH24:MI:SS') TI, \n");
        sb.append(" TO_CHAR(jb.PICKUP_DTM_DRIVER_QUOTE,'DD,MM,YYYY') ETD, \n");
        sb.append(" TO_CHAR(jb.PICKUP_DTM_DRIVER_QUOTE,'HH24:MI:SS') ETT, \n");
        sb.append(" TO_CHAR(bk.CREATED_DTM,'DD,MM,YYYY') BDT, \n");
		sb.append(" TO_CHAR(bk.CREATED_DTM,'HH24:MI') BTI, \n");	
        sb.append(" jobpf.get_jobs_state_text(jobpf.get_ext_jobs_real_state(jb.job_id)) as job_status, \n");
        sb.append(" jb.job_type as job_type, \n");
        sb.append("(SELECT acu.acct_card_nr FROM account_users acu WHERE od.booked_acct_user_id = acu.acct_user_id) as payment, \n");
        sb.append("(select max(related_job_id) from inter_jobs ij where ij.relationship = 'RJ' and ij.base_job_id = jb.job_id) as rdsp_related_job_id, \n");
        sb.append(" jb.PICKUP_DTM_TAKER_QUOTE, \n");     
        sb.append(" sp.EXTRA_INFO_1 SP_EXTRA_INFO_1, \n");
        sb.append(" jb.EXTRA_INFO_1 JOB_EXTRA_INFO_1, \n");
        sb.append(" jb.EXTRA_INFO_2 JOB_EXTRA_INFO_2, \n");
        sb.append(" jb.EXTRA_INFO_3 JOB_EXTRA_INFO_3, \n");
        sb.append(" jb.EXTRA_INFO_4 JOB_EXTRA_INFO_4, \n");
        sb.append(" jb.EXTRA_INFO_5 JOB_EXTRA_INFO_5 \n");
        sb.append(" FROM jobs          jb \n");
        sb.append(" ,orders        od \n");
        sb.append(" ,stop_points   sp \n");
        sb.append(" ,bookings	   bk \n");
        sb.append(" ,addresses     ad \n");
        sb.append(" ,areas         ara \n");
        sb.append(" ,buildings     bld \n");
        sb.append(" ,block_faces   bf \n");
        sb.append(" ,taxi_co_users t  \n");
        if(isRecallByJobList) {
        	sb.append(" ,vehicles v  \n");
        }
        
        if(isSearchInput(unitNumber)) sb.append(" ,units         u \n");
        if(isSearchInput(phoneNumber)) sb.append(" ,telephone_nrs tn \n");
//        if(systemReference != null && !systemReference.isEmpty() && !systemReference.equalsIgnoreCase(ExternalSystemId.SYSTEM_ID_WEB_BOOKER.getReference())
//        		&& !systemReference.equalsIgnoreCase(ExternalSystemId.SYSTEM_ID_UDI_USER.getReference()) 
//        		&& !isAccessableInternalJobs(request.getSystemID())) {
//        	sb.append(" ,job_references jr  \n");       //PF-14809
//        }
        if( !ExternalSystemId.SYSTEM_ID_WEB_BOOKER.getReference().equalsIgnoreCase(systemReference)
        		&& !ExternalSystemId.SYSTEM_ID_UDI_USER.getReference().equalsIgnoreCase(systemReference) 
        		&& !isAccessableInternalJobs(request.getSystemID())) {
        	sb.append(" ,job_references jr  \n");       //PF-14809
        }
        sb.append("WHERE \n");
        sb.append(" jb.order_id         = od.order_id \n");
        sb.append(" AND od.booking_id		= bk.booking_id \n");
        sb.append(" AND jb.shared_ride_state IN ('N', 'C', 'K', 'A') AND sp.order_id = od.order_id  AND sp.stop_enum_alternative = 1  \n");
        sb.append(" AND sp.stop_type        = 'P' \n");
        sb.append(" AND sp.address_id       = ad.address_id \n");
        sb.append(" AND ad.building_id      = bld.building_id \n");
        sb.append(" AND sp.gps_area_id      = ara.area_id \n");
        sb.append(" AND jb.taxi_co_id       = ara.taxi_co_id \n");
        sb.append(" AND bld.block_face_id   = bf.block_face_id \n");
        if(isSearchInput(unitNumber)) sb.append(" AND ad.unit_id          = u.unit_id(+) \n");
        if(isSearchInput(phoneNumber)) sb.append(" AND sp.aa_telephone_id  = tn.telephone_id(+) \n");
        if(isSearchInput(streetName)) sb.append(" AND (bld.STREET_NAME LIKE '"+ streetName +"%' OR sp.forced_address LIKE '" + streetName + "%') \n");
        if(isSearchInput(streetNumber)) sb.append(" AND bld.STREET_NR = '"+streetNumber +"' \n");
        if(isSearchInput(unitNumber)) sb.append(" AND (u.UNIT_NR = '"+ unitNumber +"' OR  sp.forced_unit_nr = '" + unitNumber +"') \n");
        if(isSearchInput(landMark)) sb.append(" AND ad.NAME LIKE '" + landMark + "%' \n");
        if(isSearchInput(clientName)) sb.append(" AND upper(sp.PASSENGER_NAME) LIKE '" + clientName + "%' \n");
        if(isSearchInput(phoneNumber)) sb.append(" AND tn.TELEPHONE_NR = '" + phoneNumber + "' AND tn.TELEPHONE_NR IS NOT NULL \n");
        if(isSearchInput(accountCode)) sb.append(" AND od.BOOKED_ACCT_USER_ID IN (SELECT acct_user_id FROM account_users WHERE acct_card_nr = '" + accountCode + "') \n");
        if(isSearchInput(tripType) && isValidTripType(tripType)) sb.append(" AND jb.JOB_TYPE = '"+ convertTripType(tripType) + "' \n");
        boolean confirmNbProvided = false;
        if(isSearchInput(confirmationNumber)) {
        	confirmNbProvided = true;
        	sb.append(" AND od.booking_id  = '" + confirmationNumber + "' \n");
        }
        //webUserId is required and has to be an exact match
        if(isSearchInput(webUserID)) sb.append( " AND jb.USER_CODE = '" + webUserID + "' \n"); 
        //PF-15627
        if(isRecallByJobList) {
        	sb.append(" AND jb.JOB_ID in (" + jobList +") \n");
    		sb.append(" AND jb.vehicle_id = v.vehicle_id(+)\n");
        }
        else {
	        if(jobID > 0 ) {
	        	
	        	sb.append(" AND jb.JOB_ID = " + jobID +" \n");
	        	
	        }
	        else{
	        	if (!confirmNbProvided) {//(C34781) search with time range only if job ID and confirmation number is not specified 
	        		String timeFilter = getTimeAndDateFilter(request, user);
	        		if (timeFilter != null) sb.append(timeFilter);
	        	}
	        }
        }
        
      //PF-14809 limit the job search to only return the job booking from the same origin
//        if(systemReference != null && !systemReference.isEmpty() && !systemReference.equalsIgnoreCase(ExternalSystemId.SYSTEM_ID_UDI_USER.getReference())) {
//        	if (!systemReference.equalsIgnoreCase(ExternalSystemId.SYSTEM_ID_WEB_BOOKER.getReference()) ) {
//        		sb.append(" AND jb.JOB_ID = jr.job_id AND jr.reference_type = '" + systemReference + "'\n");
//        	}
//        	else{
//        		sb.append(" AND not exists ( select * from job_references jr where jr.job_id = jb.job_id) \n");
//        	}
//        }
        
        if(ExternalSystemId.SYSTEM_ID_WEB_BOOKER.getReference().equalsIgnoreCase(systemReference)){				// web booker not return other osp user orig job
        	sb.append(" AND not exists ( select * from job_references jr where jr.job_id = jb.job_id) \n");
        }else if(!ExternalSystemId.SYSTEM_ID_UDI_USER.getReference().equalsIgnoreCase(systemReference) && !isAccessableInternalJobs(request.getSystemID())){
        	sb.append(" AND jb.JOB_ID = jr.job_id AND jr.reference_type = '" + systemReference + "'\n");
        }
        
        
        sb.append(" \n AND jb.taxi_co_id = t.taxi_co_id and t.logon_code = '" + user + "' and t.relation_type = 'A'");
        sb.append(" \n ORDER BY PICKUP_DTM_TAKER_QUOTE DESC"); 
        //logger.info("sb: " + sb);
        CartItem cart = new CartItem();
        cart.setQueryBuild(wrapRowNumReturn(sb, request));
        return cart;
	}
	
	
	/**
	 * Set search query period according to recall job request and user/system look-ahead/look-behind settings.
	 * Implementation based on com.dds.pathfinder.callbooker.client.controller.command.commandModifyJob (buildLookBehindDaysFilterForJobSearch) 
	 * 	and com.dds.pathfinder.callbooker.client.controller.client.view.SearchView
	 * 
	 * @param request 	the recall job request
	 * @param user 		the WebBooker user, possibly "IUSER"
	 * 
	 * @return the time period SQL partial statement
	 */
	private String getTimeAndDateFilter(RecallJobsRequest request, String user) {
		String fromDate = request.getFromTripRsvTime();
		String toDate = request.getToTripRsvTime();
		
		Date currentDTM = new Date();
		Date fromDTM = Utilities.convertOSPDefaultDate(fromDate);
		Date toDTM = Utilities.convertOSPDefaultDate(toDate);

		int lookBehindDays = 0, lookBehindHrs = 0;
		int plusMinusMinutes = 0;
		int[] lookForwardDays = new int[1], lookForwardSecs = new int[1]; 
		lookForwardDays[0] = 0; lookForwardSecs[0] = 0;
		
		//get user query period if time frame not specified
		QueryTimeFrameItem queryPeriod = null;
		if (fromDTM == null || toDTM == null) {
			queryPeriod = getUserQueryTimePeriod(user);
			lookBehindDays = queryPeriod.getLookBehindDays();
			lookBehindHrs = queryPeriod.getLookBehindHours();
			plusMinusMinutes = queryPeriod.getPlusMinusMinutes();
			getUserLookAhead(user, lookForwardDays, lookForwardSecs);
		}
		
		Calendar calendar = Calendar.getInstance();
		if(fromDTM == null && queryPeriod != null) {
			calendar.setTime(currentDTM);
			calendar.add(Calendar.DAY_OF_MONTH, -lookBehindDays);
			calendar.add(Calendar.HOUR_OF_DAY, -lookBehindHrs);
			
			int currMin = calendar.get(Calendar.MINUTE);
			currMin -= plusMinusMinutes;
			if (currMin < 0) {
				currMin = 0;
			}
			calendar.set(Calendar.MINUTE, currMin);
			
			fromDTM = calendar.getTime();
		}
		if(toDTM == null  && (lookForwardDays[0] != 0 || lookForwardSecs[0] != 0)) {
			calendar.setTime(currentDTM);
			calendar.add(Calendar.DAY_OF_MONTH, lookForwardDays[0]);
			
			int currMin = calendar.get(Calendar.MINUTE);
			currMin += plusMinusMinutes;
			if (currMin > 59) {
				currMin = 59;
			}
			calendar.set(Calendar.MINUTE, currMin);
			
			int currSec = calendar.get(Calendar.SECOND);
			currSec += lookForwardSecs[0];
			if (currSec > 59) {
				currSec = 59;
			}
			calendar.set(Calendar.SECOND, currSec);
			
			toDTM = calendar.getTime();
		}
		
		boolean useSearchPeriod = false;
		
		if (fromDTM != null || toDTM != null) {
			if (fromDTM != null && toDTM != null && fromDTM.before(toDTM)) {
				useSearchPeriod = true; //both specified and valid (fromDTM needs to come strictly before toDTM)
			}
			else if ((fromDTM != null && toDTM == null) 
					|| (fromDTM == null && toDTM != null)) {
				useSearchPeriod = true; //either one is specified
			}
			//else we leave useSearchPeriod as false
		}
		
		//we have some sort of search query period limit that we should use
		if (useSearchPeriod) {
			String filter = "";
			if (fromDTM != null) {
				filter = " AND OD.REQUESTED_FIRST_PICKUP_DTM >= TO_DATE('" + Utilities.formatOSPDefaultDate(fromDTM) + "','YYYY-MM-DD HH24:MI:SS') \n";
			}
			if (toDTM != null) {
				filter += " AND OD.REQUESTED_FIRST_PICKUP_DTM <= TO_DATE('"+ Utilities.formatOSPDefaultDate(toDTM) + "','YYYY-MM-DD HH24:MI:SS') \n";
			}
			return filter;
		}
		
		return null;
	}
	
	private QueryTimeFrameItem getUserQueryTimePeriod(String user) {
		if (user == null || user.length() == 0) {
			return null;
		}
		
		//CallbookerFacade facade;
		try {
			//facade = facadeHome.create();
			SysUserEvent suEvent = new SysUserEvent(SysUserEvent.GET_QUERY_TIME_FRAME, user, null);
			QueryTimeFrameItem timeFrameItem = (QueryTimeFrameItem)facade.handleSysUserEvent(suEvent);
			logger.debug("lookbehind- d:" + timeFrameItem.getLookBehindDays() + " h:"
						   + timeFrameItem.getLookBehindHours() + " +-m:" +timeFrameItem.getPlusMinusMinutes());
			return timeFrameItem;
		} catch (Exception e) {
			logger.error("failed to get query time for user-"+user, e);
			return null;
		}
	}
	
	private void getUserLookAhead(String user, int[] lookAheadDays, int[] lookAheadSecs) {
		if (pfDataSource == null || user == null || user.length() == 0) {
			return;
		}
		ResultSet rs = null; Statement stmt = null; Connection con = null;
		try {
			con = pfDataSource.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select look_ahead_days, look_ahead_seconds from users where logon_code = '" + user + "'");
			if (rs.next()) {
				lookAheadDays[0] = (int) rs.getFloat(1);
				lookAheadSecs[0] = (int) rs.getFloat(2);
			}
		} catch (Exception e) {
			logger.error("Failed to get look ahead for user-"+user, e);
			lookAheadDays[0] = 0;
			lookAheadSecs[0] = 0;
			return;
		} finally {
			if (rs != null) try{rs.close();}catch(Exception ignore){};
    	    if (stmt != null) try{stmt.close();}catch(Exception ignore){};
    	    if (con != null) try{con.close();}catch(Exception ignore){};
		}
	}
	
	private boolean isSearchInput(String searchInput){
		return (searchInput != null && searchInput.trim().length() > 0);
	}
	
	private boolean isValidTripType(String type){
		String types = "123";
		return (type.length() == 1 && types.indexOf(type) >= 0);
	}
	
	private String convertTripType(String type){
		if("1".equals(type)) return "I";
		else if("2".equals(type)) return "P";
		else if("3".equals(type)) return "T";
		else return "A";
	}

	
	public int[] getAccountIDs(String accountCode) throws SQLException {
        int[] account_Details  = {0,0};
      String query = "select   ACCT_USER_ID,profile_id from account_users where   ACCT_CARD_NR  ='" + accountCode + "'";
      Connection con=null;
      Statement stmt =null;
      ResultSet result =null;
      try {
        con = pfDataSource.getConnection();
        stmt = con.createStatement();
        result = stmt.executeQuery(query);

        if (result.next()) {
        	account_Details[0] = result.getInt("ACCT_USER_ID");
        	account_Details[1] = result.getInt("profile_id");
         }
      }finally {
    	  if(result != null) try{result.close();}catch(SQLException ignore){};
    	  if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
    	  if(con != null) try{con.close();}catch(SQLException ignore){};
      }        
      return account_Details;
	}
	
	public TripAddr getTripAddr(AddressItem addressItem, String type) {
		TripAddr tripAddr = null;
		long addressId;
		String streetName;
		String streetNr;
		String apartment;
		String city;
		String zone;
		long gpsLong;
		long gpsLat;

		if (addressItem != null && addressItem.getAddressId() != 0 ) {
			addressId = addressItem.getAddressId();
			streetName = addressItem.getStrName();
			streetNr = addressItem.getStrNum();
			apartment = addressItem.getunitNr();
			city = addressItem.getRegion();
			zone = addressItem.getAreaName();
			gpsLong = (long)(addressItem.getLongitude() * 1000000);
			gpsLat = (long)(addressItem.getLatitude() * 1000000);

			tripAddr = new TripAddr(addressId, streetName, streetNr, apartment, city, zone, gpsLong,
					gpsLat, type);
		}
		return tripAddr;
	}

	/**
	 * Retrieves the booking id on the basis of job id.
	 * 
	 * @param : jobId int
	 * @return : int positive integer
	 */

	private int getBookingId(int jobId){
		Connection con = null;
		CallableStatement cs = null;
		int bookingId = 0;
		try {
			con = pfDataSource.getConnection();
			cs = con.prepareCall("{call booking.get_booking(?,?)}");

			cs.setInt(1, jobId);
			cs.registerOutParameter(2, java.sql.Types.DECIMAL);
			cs.executeUpdate();

			bookingId = cs.getInt(2);

		} catch (SQLException se) {
			logger.error("getBookingId failed....", se);
		}
		finally {
			if (cs != null)try {cs.close();} catch (SQLException ignore) {};
			if (con != null)try {con.close();} catch (SQLException ignore) {};
		}

		return bookingId;

	}
	
	private String wrapRowNumReturn(StringBuffer sb, RecallJobsRequest request){
		if(sb == null || sb.length() == 0 || request == null || request.getTaxiCoID() <= 0) return "";
		else{
			int rowNum = cachedParam.getCompanyParameterIntValue(request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_MAX_JOBS_TO_RETURN);
			if(rowNum <=0 || rowNum > 999) rowNum = MAX_ROWNUM_JOB_QUERY;
			sb.insert(0, "SELECT * FROM ( \n" );
			sb.append(" ) WHERE ROWNUM <= " + rowNum + " \n");
			return sb.toString();
		}
	}
	
	//PF-15627
	private boolean isRecallByJobList(RecallJobsRequest request) {
		
		String jobList = request.getJobList();
		if(request.getRecallCriteria() == RECALL_BY_JOBLIST && jobList != null && jobList.trim().length()> 0) {
			//make sure that JobList has just "jobid,jobid" format
			String[] tokens = jobList.split(",");
			
			if(tokens.length > MAX_NUM_RECALL_BY_JOBLIST || tokens.length <= 0) {
				return false;
			}
			for(String jobId: tokens ) {
				
				if(jobId== null || jobId.isEmpty()) {
					return false;
				}
				int n = jobId.length();
				
				for(int i=0; i<n; i++) {
					if(!Character.isDigit(jobId.charAt(i))) {
						return false;
					}
				}
			}
			
			
			return true;
		}
		else {
			return false;
		}
		
	}
	//PF-15615 return different status if it's completed based record change flag
	private void setResponseTripSubStatus(JobListItem job){
		if(pfDataSource != null && job.getTaxiRideID() > 0){
			Connection con = null;
			Statement stmt = null;
	        ResultSet rs = null;
			try{
				con = pfDataSource.getConnection();
		        stmt = con.createStatement();
				rs = stmt.executeQuery("select j.record_change_type, TO_CHAR(j.last_status_upd_dtm, 'yyyy-MM-dd HH24:MI:ss') as end_dtm from jobs j where  j.job_id = "+job.getTaxiRideID());
				if(rs.next()){
				
					String recordChgType = rs.getString("record_change_type");
					String jobEndDtm = rs.getString("end_dtm");
					
					if(recordChgType == null || 
							UniformTripStatusSubCode.SERVICED_ALTERED.getStatus().equalsIgnoreCase(recordChgType )){ 
							// || UniformTripStatusSubCode.SERVICED_2CAR.getStatus().equalsIgnoreCase(recordChgType)){	//PF-16303, move it to serviced cancelled below						//normal paid complete or completed altered or completed 2 car
						job.setDetailTripStatusUniformCode(UniformTripStatusSubCode.SERVICED_METEROFF.getCode());
						//PF-15582 add job end dtm for token payment after job complete feature
						job.setJobEndDateTime(jobEndDtm);
					}
					else if( UniformTripStatusSubCode.SERVICED_FORCED_CLEAR.getStatus().equals(recordChgType)) {
						//PF-15848 added forced completed sub status code
						job.setDetailTripStatusUniformCode(UniformTripStatusSubCode.SERVICED_FORCED_CLEAR.getCode());
						job.setJobEndDateTime(jobEndDtm);
					}
					else if( UniformTripStatusSubCode.SERVICED_CANCELLED.getStatus().equals(recordChgType) || 
							UniformTripStatusSubCode.SERVICED_SCRUBBED.getStatus().equalsIgnoreCase(recordChgType)
							|| UniformTripStatusSubCode.SERVICED_2CAR.getStatus().equalsIgnoreCase(recordChgType)) {
						//PF-15848 added cancelled and scrubbed completed sub status code
						job.setDetailTripStatusUniformCode(UniformTripStatusSubCode.SERVICED_CANCELLED.getCode());
						job.setJobEndDateTime(jobEndDtm);
					}
					else {
						//PF-15848 completed unkown
						job.setDetailTripStatusUniformCode(UniformTripStatusSubCode.SERVICED_OTHERS.getCode());
						//PF-15582
						//job.setJobEndDateTime(jobEndDtm);
					}
				}
			}catch(SQLException se){
				se.printStackTrace();
				logger.error("setResponseTripSubStatus failed: ", se);
			}finally{
			
				if (rs != null) try{rs.close();}catch(SQLException ignore){};
	    	    if (stmt != null) try{stmt.close();}catch(SQLException ignore){};
	    	    if(con != null) try{con.close();}catch(SQLException ignore){};
			}
		}
	}
	
	//PF-15289 retrieve previous job status if the current status is on hold
	/*
	private String getPrevTripStatus(JobListItem job){
		String tripStatus = null;
		if(pfDataSource != null && job.getTaxiRideID() > 0){
			Connection con = null;
			Statement stmt = null;
	        ResultSet rs = null;
	        
			try{
				con = pfDataSource.getConnection();
		        stmt = con.createStatement();
				rs = stmt.executeQuery("select job.get_jobs_state_text(job.get_ext_jobs_prev_state(job_id)) as status from jobs where job_id = "+job.getTaxiRideID());
				if(rs.next()){
					
					tripStatus = rs.getString("status");
					
				}
			}catch(SQLException se){
				se.printStackTrace();
				logger.error("setResponseTripSubStatus failed: ", se);
			}finally{
			
				if (rs != null) try{rs.close();}catch(SQLException ignore){};
	    	    if (stmt != null) try{stmt.close();}catch(SQLException ignore){};
	    	    if(con != null) try{con.close();}catch(SQLException ignore){};
			}
		}
		return tripStatus;
	}*/
}
