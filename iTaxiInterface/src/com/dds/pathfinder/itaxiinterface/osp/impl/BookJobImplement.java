/****************************************************************************
 *
 *		   		    Copyright (c), 2009
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/BookJobImplement.java $
 * 
 * PF-16772, 09/15/15, DChen, add unit match in OSP.
 * 
 * PF-16385, 03/03/15, DChen, add pfrest job reference.
 * 
 * 40    1/29/14 3:48p Ezhang
 * PF-15809 used cached company parameter
 * 
 * 39    1/16/14 3:28p Dchen
 * PF-15751, OSP to validate pickup and phone number.
 * 
 * 38    12/17/13 12:27p Mkan
 * Added new DEFAULT_ACCT_ERROR for generic account problem.
 * PF-15799
 * 
 * 37    3/12/13 4:34p Dchen
 * Added pf parameters load bean.
 * 
 * 36    2/22/13 3:33p Mkan
 * 
 * 35    2/21/13 7:02p Mkan
 * - modified setSingleJobResponse() to set meaningful 
 *   error response code and message (PF-15230)
 *   (MG job is single job only, and multi-car job does not 
 *   seem to do any validation on the server side)
 * 
 * 34    2/19/13 3:44p Dchen
 * PF-15014, add some attributes linked information in OSP.
 * 
 * 33    2/19/13 3:41p Dchen
 * PF-15014, add some attributes linked information in OSP.
 * 
 * 32    10/24/12 5:45p Ezhang
 * PF-14809 added support for mobile booking.
 * 
 * 31    9/21/12 4:08p Jwong
 * PF-14147 / PF-14368 - Undo previous bug fix
 * 
 * 30    9/21/12 11:28a Jwong
 * PF-14147 / PF-14368 - Bug Fix
 * 
 * 29    9/19/12 4:07p Jwong
 * PF-14147 / PF-14368 - Beverly Hill's webbooker request / Region
 * attribute is missing when booking in web booker job to an address with
 * region, area and building attributes.
 * 
 * 28    5/30/12 4:20p Ezhang
 * PF-14456 Fixed in getAddressAttributes() check return null value of jobReq.getAttributelist() before calling its method.
 * to prevent null pointer exception. Attributelist is a optional field in OSP API.
 * 
 * 27    3/28/12 4:22p Yyin
 * C36767 - Fixed in checkSingleJobResponse().
 * 
 * 26    9/12/11 5:11p Yyin
 * C36130- Fixed the problem that job failed to create when it's not a
 * GoFastCar job.
 * 
 * 25    9/12/11 2:38p Yyin
 * C36130- Fixed forced address trips with OSP.
 * 
 * 24    9/12/11 2:20p Yyin
 * C36130- Fixed forced address trips with OSP.
 * 
 * 23    12/02/11 4:47p Jwong
 * C35846 - Updated for code cleanup.
 * 
 * 22    2/12/11 10:00a Ezhang
 * C36130 Added SystemId validation and store the systemid to job_references table.
 * 
 * 21    11/21/11 4:27p Jwong
 * C35846 - Allow webbooker trip to be booked with held job flag.
 * 
 * 20    8/18/11 3:32p Dchen
 * upgrade to 3.75.
 * 
 * 19    4/19/11 6:28p Dchen
 * C35277, speed webbooker order tracking.
 * 
 * 18    12/10/10 3:13p Mkan
 * C34595
 *    - BookJobImplement(), added to set UserAccountHome
 *    - generateBookJobRes(BookJobReq), pass new UserAccount to
 * UpdateJobImplement.
 * 
 * 17    12/09/10 11:56a Ajiang
 * Removed bookjob eta. It's entered by the driver after the trip is
 * dispatched.
 * 
 * 16    11/10/10 2:09p Ezhang
 * update hardcoded action type to meaningful name and move upcaseRequest to parent class for sharing.
 * 
 * 15    10/29/10 4:23p Ezhang
 * OSP 2.0 add confirmation number to multiple jobs response.
 * 
 * 14    10/07/10 9:00a Ezhang
 * OSP 2.0 fixed multiple jobs response format to meet the spec.
 * 
 * 13    10/04/10 2:45p Ezhang
 * change the ride_id field from required to optional and remove the validation.
 * 
 * 12    9/28/10 1:34p Ezhang
 * code cleanup
 * 
 * 11    9/27/10 11:19a Ajiang
 * Fixed the eta format and confirmation required string comparison
 * 
 * 10    9/20/10 2:37p Ezhang
 * OSP 2.0 added new errorCode and retrieve ETA and fare Estimate and confirmation number.
 * 
 * 9     5/07/10 2:59p Dchen
 * handle user input landmark with street name and nb.
 * 
 * 8     5/03/10 3:46p Dchen
 * upcase input address.
 * 
 * 7     4/15/10 11:42a Mkan
 * Part of address fixes to fix 
 * - unit in pickup address not being used, 
 * - user specified drop off address was not being used,
 * - user specified invalid drop off address did not get booked
 *   generateBookJobRes()
 *    - moved request validation into validateRequest() for efficiency
 *    - added to trim drop off addresses and append perc char to pickup
 * address correctly before passing them to getWSAddressData()
 *   insertJob()
 *    - added to process address matching from Address Lookup service
 * before using
 * Commented out unused code
 * 
 * 6     2/23/10 2:39p Dchen
 * OSP interface.
 * 
 * 5     2/12/10 5:15p Dchen
 * Modified for web booker using.
 * 
 * 4     2/08/10 4:24p Dchen
 * OSP interface.
 * 
 * 3     2/02/10 2:35p Dchen
 * OSP interface.
 * 
 * 2     1/13/10 6:18p Dchen
 * OSP interface.
 * 
 * 1     1/11/10 1:32p Dchen
 * OSP interface.
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.ejb.EJB;
import javax.sql.DataSource;

import com.dds.pathfinder.callbooker.server.account.user.ejb.UserAccount;
import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartItem;
import com.dds.pathfinder.callbooker.server.citynav.model.TesTripCalc;
import com.dds.pathfinder.callbooker.server.citynav.model.TesTripCalcReply;
import com.dds.pathfinder.callbooker.server.citynav.model.TripAddr;
import com.dds.pathfinder.callbooker.server.controller.event.PfaEvent;
import com.dds.pathfinder.callbooker.server.order.ejb.Order;
import com.dds.pathfinder.callbooker.server.pfa.model.PfaItem;
import com.dds.pathfinder.callbooker.server.pfa.model.TripPriceRequest;
import com.dds.pathfinder.callbooker.server.pfa.model.TripPriceResult;
import com.dds.pathfinder.callbooker.server.facade.ejb.CallbookerFacade;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.BookJobReq;
import com.dds.pathfinder.itaxiinterface.webservice.BookJobRes;
import com.dds.pathfinder.itaxiinterface.webservice.CoreBookJobReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.TaxiJob;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class BookJobImplement extends OSPCartImplement {

	
	private Logger logger = LogManager.getLogger(this.getClass());
    
    private Order order;
    private CallbookerFacade facade;
    
    private String job_origin;
    
    /** The index of response code in the "databaseErrorMap" */
    private static final int ERRMAP_INDEX_RESPONSE_CODE = 0;
    /** The index of response message in the "databaseErrorMap" */
    private static final int ERRMAP_INDEX_RESPONSE_MSG = 1;
    /**
     * Contains map of
     * 	<database error code, <OSP response code, database error message>>
     */
//	private static final Map<String, String[]> databaseErrorMap = 
//			Collections.unmodifiableMap(new HashMap<String, String[] >(){
//				private static final long serialVersionUID = 3448870900858391596L;
//			{
//				//These are copied from Callbooker resource string file, for OSP code that is not defined we use the default error
//				//
//				// Format: 
//				//	put ("database error code", new String[]{String.valueOf("OSP RESPONSE CODE"), "Error message to return back in the response"});
//				//
//			        put("2", new String[]{String.valueOf(BookJobErrorCode.INVALID_ACCOUNT_CODE_OR_NAME.code), "No such account"});//"No Account Card or Account present"
//			        put("3", new String[]{String.valueOf(BookJobErrorCode.INVALID_ACCOUNT_CODE_OR_NAME.code), "Account id /Account card id is not related with this Company"});
//			        put("10",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code), "User is Suspended "}); //we get this when the account is suspended
//			        put("13",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code), "Account/Group is Suspended"});
//			        put("14",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"Account/Group is Closed "});
//			        put("15",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"No trips left for Account/Card"});
//			        put("16",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"Account is currently disabled"});
//			        put("17",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"Group is currently disabled "});
//			        put("23",new String[]{String.valueOf(BookJobErrorCode.DEFAULT_ACCT_ERROR.code),"Limit of Account is less than the Balance"});
//			        put("24",new String[]{String.valueOf(BookJobErrorCode.DEFAULT_ACCT_ERROR.code),"card is Expired"});
//			        put("26",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"Bookings are not allowed to this account"});
//			        put("28",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"Account is Inactive"});
//			        put("29",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"Booking is not Allowed on this Account"});
//			        put("30",new String[]{String.valueOf(BookJobErrorCode.DEFAULT_ACCT_ERROR.code),"Account is Pass the trip threshold limit"});
//			        put("32",new String[]{String.valueOf(BookJobErrorCode.DEFAULT_ACCT_ERROR.code),"Limit of Account User is less than the Balance"});
//			        put("34",new String[]{String.valueOf(BookJobErrorCode.DEFAULT_ERROR.code),"PO# is required"});
//		     
//	        }
//	    });
//
//    public enum BookJobErrorCode {
//		NO_ERROR(0),
//		NOT_AUTHENTICATED(1),
//		INVALID_SESSIONID(2),
//		SYS_CAPACITYLIMIT(3),
//		INVALID_WEBUSERID(4),
//		TRIP_NO_FOUND(5),
//		INVALID_REQTYP(6),
//		INVALID_RIDEID(7),
//		INVALID_TAXI_RIDEID(8),
//		INVALID_TAXI_COMPID(9),
//		INVALID_PHONENUM(10),
//		INVALID_PHONEEXT(11),
//		INVALID_PICKUP_HOUSE_NUM(12),
//		INVALID_PICKUP_STREET_NUM(13),
//		INVALID_PICKUP_DISTRICT(14),
//		INVALID_PICKUP_LANDMARK(15),
//		INVALID_PICKUP_GPS(16),
//		INVALID_DROPOFF_GPS(17),
//		NO_ASAP_PREFEERRED_TRIP(18),
//		TC_PICKUP_DATETIME_TOOSOON(19),
//		TC_PICKUP_DATETIME_TOOFAR(20),
//		CAB_NOT_MATCH_TRIP_COMP(21),
//		INVALID_PASSENGER_NAME(22),
//		INVALID_ACCOUNT_CODE_OR_NAME(23),
//		INVALID_FLAT_RATE(24),
//		INVALID_AUTH_NUM(25),
//		INVALID_PRIORITY_REASON(26),
//		INVALID_PRIORITY(27),
//		INVALID_NUMTAXIS(28),
//		INVALID_NUM_OF_PASSENGER(29),
//		INVALID_ADVISE_ARRIVAL(30),
//		INVALID_TRIP_ATTRIBUTE(31),
//		INVALID_CALLBACK_TIME(32),
//		INVALID_NUM_OF_CALLBACKS(33),
//		INVALID_FORCED_ADDRESS_FLAG(34),
//		INVALID_REMARK(35),
//		INACTIVE_ACCOUNT(36),
//		INCORRECT_ACCT_PASSWORD(37),
//		PICKUP_ADDR_NOT_FOUND(38),
//		INVALID_ADDRESS(39),
//		DEFAULT_ACCT_ERROR(40),
//		DEFAULT_ERROR(99);
//		
//		private int code;
//		
//		private BookJobErrorCode(int c) {
//			   this.code = c;
//		}
//
//		public int getCode() {
//			   return code;
//		}
//	};
    
    
	public BookJobImplement(IAddressLookup addressLookUp, DataSource pfDataSource, Order orderLocal, UserAccount userAccountLocal, CallbookerFacade facadeLocal, LoadDispatchParametersLocal cachedParam) {
		super(addressLookUp, pfDataSource, cachedParam);
		this.order = orderLocal;
		this.facade = facadeLocal; //add to retrieve trip matrix such as trip price
		job_origin = null;
		setUserAccount(userAccountLocal); //added for C34595, to populate account detail into booking
	}

	public BookJobRes generateResponse(BaseReq request) {
		return (generateBookJobRes((BookJobReq)request));
	}
	
	/**
	 * If request is an ASAP job, return true.
	 * @param request
	 * @return 
	 */
	private boolean isASAPJob(BookJobReq request) {
		return (request.getPickupTime() == null);
	}
	
	   /**
	    * Function to generate Job Transaction response. 
	    * @param request
	    * @return response
	    */
	   private BookJobRes generateBookJobRes(BookJobReq request){
		   BookJobRes response = getDefaultResponse();
		   if(!validateRequest(request, response)){
			   return response;
		   }
		   	   
		   upcaseRequest(request);
		   //This address validation takes some time. 
		   //To be more efficient, all validation checks against request
		   //   have been moved inside validateRequest() above.
		   //Note: not appending any percentage to end of string here. Address sent from OSP for booking should be absolute.
		   PFAddressResponse pickupAddress = getWSAddressData(
				   appendPercentage(request.getPickupStreetName(), false), 
				   appendPercentage(request.getPickupStreetNr(), false),
				   appendPercentage(request.getPickupUnit(), true), //except for unit here, we want to use pe/rcentage to match
				   appendPercentage(request.getPickupRegion(), false), 
				   appendPercentage(request.getPickupLandmark(), false), 
				   PERCENTAGE_CHAR, PERCENTAGE_CHAR,  //organization and postal code
				   request.getTaxiCoID(), "");
		   
		   if(!isValidAddressResponse(pickupAddress) && isBothLandMarkAndAddressProvided(request.getPickupStreetName(), request.getPickupStreetNr(), request.getPickupLandmark())){
			   pickupAddress = getWSAddressData(
					   appendPercentage(request.getPickupStreetName(), false), 
					   appendPercentage(request.getPickupStreetNr(), false),
					   appendPercentage(request.getPickupUnit(), true), //except for unit here, we want to use percentage to match
					   appendPercentage(request.getPickupRegion(), false), 
					   PERCENTAGE_CHAR, PERCENTAGE_CHAR, PERCENTAGE_CHAR,  //organization and postal code
					   request.getTaxiCoID(), "");
			   
		   }
 
		   PFAddressResponse closestAddress = getWSClosestAddressData(request.getPickupLat(), request.getPickupLon());

		   //Note: not appending any percentage to end of string here. 
		   //If there is no match, drop-off address will be used as-is in insertJob() below.
		   //(i.e. forced address, a.k.a exactLocation in StopPointItem)
		   PFAddressResponse setdownAddress = getWSAddressData(
				   appendPercentage(request.getDropoffStreetName(), false), 
				   appendPercentage(request.getDropoffStreetNr(), false), 
				   appendPercentage(request.getDropoffUnit(), false), 
				   appendPercentage(request.getDropoffRegion(),false),
				   appendPercentage(request.getDropoffLandmark(), false),
				   PERCENTAGE_CHAR, PERCENTAGE_CHAR, //organization and postal code
				   request.getTaxiCoID(), "");
			
		   
		   if ( request.getRequestType() == REQUEST_TYPE_BOOK_JOB ) {        // create job 
			   response = insertJob(request, pickupAddress, closestAddress, setdownAddress); //return create job response
			   // C36130 update job_references table if the external job is created successfully
			   if(job_origin != null && !job_origin.isEmpty()  
					   && !job_origin.equalsIgnoreCase(ExternalSystemId.SYSTEM_ID_WEB_BOOKER.getReference()) //PF-14809 webbooker job doesn't go to this table
					   && response.getRequestStatus() == GenErrMsgRes.STATUS_SUCCESS){
				   insertExternalJobReference(response.getTaxiRideID());
				   //insert more if it's multiple job booking
				   if(response.getNbOfJobs()> 1){
					   TaxiJob[] list = response.getTaxiJobIDList();
					   for(int i = 0; i< response.getNbOfJobs(); i++){
						   Long jobId = list[i].getTaxiRideID();
						   insertExternalJobReference(jobId);
					   }
				   }
			   }
			 
			   return response; //return create job response
		   }

		   if ( request.getRequestType() == REQUEST_TYPE_UPDATE_JOB) {   //update job
			   UpdateJobImplement updateJob = new UpdateJobImplement(order, userAccount, pfDataSource, addressLookUp, cachedParam);
			   if(getUserTelephone() != null) updateJob.setUserTelephone(getUserTelephone());
			   updateJob.generateUpdateJobResponse(request, response, pickupAddress, closestAddress, setdownAddress);
			   return response; //return update job response       
		   }
		   
		   return response;	  //return default response
		   
	   }  
	   
	   
	   private BookJobRes insertJob(BookJobReq request, PFAddressResponse pickupAddressRes, PFAddressResponse closestAddressRes, PFAddressResponse setdownAddressRes){
		   BookJobRes response = getDefaultResponse();
		   if(!validateAddresses(request, pickupAddressRes, closestAddressRes)){
			   response.setRequestStatus(GenErrMsgRes.STATUS_BOOKING_FAIL_INVALIDADDRESS);
			   response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_ADDRESS);
			   response.setErrorCode(BookJobErrorCode.INVALID_ADDRESS.getCode());
			   return response;
		   }
		   
		   //PFAddressData pickupAddress = getMatchAddress(pickupAddressRes, request.getPickupStreetName(), request.getPickupStreetNr(), request.getPickupRegion());
		   PFAddressData pickupAddress = getMatchAddressWithUnit(pickupAddressRes, request.getPickupStreetName(), request.getPickupStreetNr(), request.getPickupRegion(), request.getPickupUnit());
		   PFAddressData closestAddress = getFirstAddress(closestAddressRes);
		   PFAddressData setdownAddress = getMatchAddressWithUnit(setdownAddressRes, request.getDropoffStreetName(), request.getDropoffStreetNr(), request.getDropoffRegion(), request.getDropoffUnit());
		   
		   if(closestAddress!= null)
		   {
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
				   request.getTaxiCoID(), "");
			   closestAddress = getFirstAddress(closestAddressRes);
		   }
		   modifyPickupClosestAddresses(pickupAddress, closestAddress);

		   int nbOfTaxi = request.getNumTaxis() == null? 0: request.getNumTaxis();
		   AddressItem closestAddrItem = null;
		   if( pickupAddress == null && closestAddress != null)
		   {
		      closestAddrItem = getAddressItem(closestAddress, request.getTaxiCoID());
		   }
		   Vector<CartItem> cartItems = generateCartItems(nbOfTaxi, request, pickupAddress, setdownAddress, closestAddrItem);
		   if(!validateCartItems(cartItems, response)) return response;
		   
//// C36745 - Add all non-manual attributes here
//		   if( !addAutoAttributes( request, response, cartItems, pickupAddress, closestAddress ) )
//			   return response;
		   
		   if(cartItems == null || cartItems.size() < 1) return response;
		   try{
			    String userName = getUserName(request);
			    // Order order = orderHome.create();
			    Collection<?> results = null;
			    //get estimated fare and estimated arrival time
			    //make sure setdown address is valid
			    if(isValidAddressResponse(setdownAddressRes)){
			    	AddressItem pickup = getAddressItem(pickupAddress, request.getTaxiCoID());
			    	AddressItem setdown = getAddressItem(setdownAddress, request.getTaxiCoID());
			    	checkTripMatrix(pickup, setdown, request, response);
			    }
			    if(cartItems.size() == 1){
			    	results = order.setOrderDetails(userName, "", null, cartItems.get(0));
			    	setInsertJobResponse(true, results, request, response);	    	
			    	
			    }else{
			    	results = order.multipleTrips_OrderEJB(userName, "", ACTION_TYPE_BOOK_MULTIPLEJOBS, cartItems);
			    	setInsertJobResponse(false, results, request, response);
			    }
			}catch(Exception e){
				logger.error("generateUpdateJobResponse failed", e);
			}
			return response;
	   }
	   
	@SuppressWarnings("unchecked")
	private void setInsertJobResponse(boolean isSingleJob, Collection<?> pfResults, BookJobReq request, BookJobRes response){
		   if(isSingleJob){
			   ArrayList<Object> results = (ArrayList<Object>)pfResults;
			   setSingleJobResponse(request, results, response);
		   }else{
			   Vector<CartItem> carts = (Vector<CartItem>)pfResults;
			   setMultipleJobsResponse(request, carts, response);
		   }
	   }
	   
	   private void setSingleJobResponse(BookJobReq request, ArrayList<Object> results, BookJobRes response){
		   if(results != null && results.size() > 0 ){
				if("true".equalsIgnoreCase((String)results.get(0))){
					response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
					response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
					response.setErrorCode(BookJobErrorCode.NO_ERROR.getCode());
					checkSingleJobResponse(request, response, results);
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
						return; 
					}
					//return meaningful error
					String[] responseCodeMsg = databaseErrorMap.get(dbErrorCode); //get the pair of <OSP response code, database error message>>
					response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
					response.setErrorCode(Integer.valueOf(responseCodeMsg[ERRMAP_INDEX_RESPONSE_CODE]));
					response.setErrorMessage(responseCodeMsg[ERRMAP_INDEX_RESPONSE_MSG]);
				}
		   }
	   }
	   
	   
	   private void setMultipleJobsResponse(BookJobReq request,Vector<CartItem> carts, BookJobRes response){
		   if(carts != null && carts.size() > 0){
			   response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
			   response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
			   response.setErrorCode(BookJobErrorCode.NO_ERROR.getCode());
			   int nbOfJob = carts.size();
			   response.setNbOfJobs(nbOfJob-1);
			   TaxiJob[] jobs = new TaxiJob[nbOfJob];   
			   for(int i=0; i< nbOfJob; i++){
				   CartItem cartItem = carts.get(i);
				   if(cartItem != null ){
					   long jobID = Integer.parseInt(cartItem.getJobId());
					   if(i == 0) {
						   response.setTaxiRideID(jobID);
						   // get booking_id for the first job since all job has the same booking id.
						   if(request.getConfirmationNumRequired().equals("Y") &&  request.getRequestType() == REQUEST_TYPE_BOOK_JOB )
						   {
							   int bookingID = getBookingId(Integer.parseInt(cartItem.getJobId()));
							   if(bookingID != 0){
								   response.setConfirmationNum(String.format("%d", bookingID));
							   }
						   }
					   }
					   else{
						   jobs[i-1] = new TaxiJob(jobID);
						   //add ETA and fare estimate to multi-job response.
						   jobs[i-1].setEta1(response.getEta1());
						   jobs[i-1].setFareEstimate(response.getFareEstimate());
					   }
				   }
			   }
			   response.setTaxiJobIDList(jobs);
		   }
	   }
	   
	   private Vector<CartItem> generateCartItems (int nbOfJob, BookJobReq request, PFAddressData pickupAddress, PFAddressData setdownAddress, AddressItem closestAddrItem){
		   Vector<CartItem> carts = new Vector<CartItem>();
		   if(nbOfJob <=0) nbOfJob = 1;
		   for( int i=0; i<nbOfJob; i++){
			   CartItem cartItem = convertRequest2CartItem(request, pickupAddress, setdownAddress, closestAddrItem); 
			   if(cartItem != null){
				   cartItem.setJobId("");
				   cartItem.setAlready_Address_job(true);
				   carts.add(cartItem);
			   }
		   }
		   return carts;
	   }
	   
	   private void checkSingleJobResponse(BookJobReq request, BookJobRes response, ArrayList<Object> results){
		   response.setNbOfJobs(1);
		   Integer jobID = (Integer)results.get(1);
		   response.setTaxiRideID(jobID == null? 0 : jobID.longValue());
		   // new OSP interface needs to add confirmation_num(booking_id) and eta and fare estimate
		   //If this flag is set to Y and request is book a job then add confirmationNum to response
		   if(request.getConfirmationNumRequired()!= null && request.getConfirmationNumRequired().equals("Y") &&  request.getRequestType() == REQUEST_TYPE_BOOK_JOB )
		   {
			   Integer confID = (Integer)results.get(3);
			   response.setConfirmationNum(confID == null? null : confID.toString());		   
		   }
 
	   } 
		   
		   
	   
	   
	/**
	 * Validate job book request.
	 * 
	 * @param request
	 *            the request to validate
	 * @param response
	 *            the response to update if error
	 * @return true if valid, false otherwise
	 */
	private boolean validateRequest(BookJobReq request, BookJobRes response) {
		int systemId = 0;

		if (request == null || response == null) {
			return false;
		}
		//C36130 need to check systemID
		if(validateSystemId(pfDataSource, request.getSystemID(), request.getSystemPassword()) == false){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(BookJobErrorCode.NOT_AUTHENTICATED.getCode());
			return false;
		}
		
		systemId = request.getSystemID().intValue();
		//PF-14809 get job origin and store in pf database table job_references
		job_origin = getSystemReference(systemId);
		

		
			
		
		if (request.getTaxiCoID() <= 0) {
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
			response.setErrorCode(BookJobErrorCode.INVALID_TAXI_COMPID.getCode());
			return false;
		}

		// If not the right kind of request, return right away
		if (request.getRequestType() != REQUEST_TYPE_BOOK_JOB
				&& request.getRequestType() != REQUEST_TYPE_UPDATE_JOB) {
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(BookJobErrorCode.INVALID_REQTYP.getCode());
			return false;
		}

		String requestCabNum = request.getCabNum(); // the preferred vehicle call sign
		if (requestCabNum != null) {
			if (requestCabNum.trim().length() > 0 && isASAPJob(request)) {
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_PREF_ASAP);
				response.setErrorCode(BookJobErrorCode.NO_ASAP_PREFEERRED_TRIP.getCode());
				return false;
			}
		}

		// Specific validation for update job
		if (request.getRequestType() == REQUEST_TYPE_UPDATE_JOB
				&& (request.getTaxiRideID() == null || request.getTaxiRideID() <= 0)) {
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_JOB_ID);
			response.setErrorCode(BookJobErrorCode.INVALID_TAXI_RIDEID.getCode());
			return false;
		}

		return true;
	}
	   	   
	   
	   
	   private void checkTripMatrix(AddressItem pickupAddress, AddressItem setdownAddress, BookJobReq request, BookJobRes response){
			String navType = CompanyDefaultValues.getSystemParameterValue(pfDataSource, TripPriceRequest.SYSTEM_PARAMETER_S_NAV_TYPE);
			if(TripPriceRequest.NAV_TYPE_TTES.equalsIgnoreCase(navType)){
				checkTTESTripMatrix(pickupAddress, setdownAddress, request, response);
			}else{
				checkOtherTripMatrix(pickupAddress, setdownAddress, request, response, navType);
			}
		}
	   
	   private void checkTTESTripMatrix(AddressItem pickupAddress, AddressItem setdownAddress, BookJobReq request, BookJobRes response){
			if(pickupAddress == null || setdownAddress == null) return;
			
			ArrayList<TripAddr> tripAddrArray = new ArrayList<TripAddr>();
			TripAddr tripAddress = getTripAddr(pickupAddress, TripAddr.ADDRESS_TYPE_PICKUP);
			if(tripAddress != null) tripAddrArray.add(tripAddress);
			tripAddress = getTripAddr(setdownAddress, TripAddr.ADDRESS_TYPE_SETDOWN);
			if(tripAddress != null) tripAddrArray.add(tripAddress);
			
	        TesTripCalc ttesRequest = new TesTripCalc(0, "", new Date(), "", tripAddrArray);
			
	        //PF_15809
	        ttesRequest.setServer(cachedParam.getCompanyParameterValue(request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_CITYNAV_SERVER));
	        ttesRequest.setPort(cachedParam.getCompanyParameterIntValue(request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_CITYNAV_PORT));
	        ttesRequest.setRequestTimeout(cachedParam.getSystemParameterIntValue( CompanyDefaultValues.SYSTEM_PARAMETER_S_NAV_RESP_TIMEOUT, 5)*1000);
	        
	        //ttesRequest.setServer(CompanyDefaultValues.getCompanyParameterValue(pfDataSource, request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_CITYNAV_SERVER));
	        //ttesRequest.setPort(CompanyDefaultValues.getCompanyParameterIntValue(pfDataSource, request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_CITYNAV_PORT));
	        //ttesRequest.setRequestTimeout(CompanyDefaultValues.getSystemParameterIntValue(pfDataSource, CompanyDefaultValues.SYSTEM_PARAMETER_S_NAV_RESP_TIMEOUT, 5)*1000);
	        try{
	        	// CallbookerFacade facade = facadeHome.create();
	        	TesTripCalcReply ttesResponse = facade.handleCityNavRequest(ttesRequest);
	        	if(ttesResponse == null) return;
	        	
	        	int tripTime = 0;
				try{
					tripTime = Integer.parseInt(ttesResponse.getTripTime());
				}catch(NumberFormatException ne){
				}
	        	//Estimate fare of the trip. It’s a number in cents.
				response.setFareEstimate(ttesResponse.getEstimatedPrice());
	        }catch(Exception e){
				logger.error("checkTTESTripMatrix failed....", e);
			}
		}
		
		private void checkOtherTripMatrix(AddressItem pickupAddress, AddressItem setdownAddress, BookJobReq request, BookJobRes response, String navType){
			if(pickupAddress == null || setdownAddress == null) return;
			PfaEvent pfaEvent = createPfaEvent(pickupAddress, setdownAddress, request, navType);
			try{
				// CallbookerFacade facade = facadeHome.create();
				PfaItem pfaResult = facade.handlePfaEvent(pfaEvent);
				if(pfaResult == null) return;
				TripPriceResult result = pfaResult.getPriceResult();
				if(result == null) return;
				if(result.getStatus() == TripPriceResult.SUCCESS){
					
					int tripTime = result.getTripTime();		
					//Estimate fare of the trip. It’s a number in cents.
					response.setFareEstimate((long)(result.getPrice()* 100));
				}
				
			}catch(Exception e){
				logger.error("checkOtherTripMatrix failed", e);
				
			}

		}
		
		private PfaEvent createPfaEvent(AddressItem pickupAddress, AddressItem setdownAddress, BookJobReq request, String navType){
			TripPriceRequest priceRequest = new TripPriceRequest();
			
			priceRequest.pickupStreetNumber = pickupAddress.getStrNum();
			priceRequest.pickupStreetName = pickupAddress.getStrName();
			priceRequest.pickupAreaId = pickupAddress.getAreaId();
			priceRequest.pickupAddressId = pickupAddress.getAddressId();

			priceRequest.destStreetNumber = setdownAddress.getStrNum();
			priceRequest.destStreetName = setdownAddress.getStrName();
			priceRequest.destAreaId = setdownAddress.getAreaId();
			priceRequest.destAddressId = setdownAddress.getAddressId();

			priceRequest.taxiCoId = request.getTaxiCoID();
			Date currentDate = new Date();
			priceRequest.pickupDate = Utilities.formatUtilDate(currentDate, Utilities.CALLBOOKER_DEFAULT_DATE_FORMAT);
			
			String accountCode = request.getAccountCode();
			if(accountCode == null || accountCode.trim().length() == 0) {
				accountCode = CompanyDefaultValues.getCompanyDefaultCashAccountNb(pfDataSource, request.getTaxiCoID());
			}
			setAccountIDs(priceRequest, accountCode);
			setAttributes(priceRequest);
			priceRequest.accountSetId = CompanyDefaultValues.DEFAULT_PICKUP_ACCOUNT_SET_ID;
			priceRequest.numberOfVias = 0;
			priceRequest.setNavigationEngine(navType);
			priceRequest.priceType = TripPriceRequest.TRIP_MATRIX;
			
			return new PfaEvent(PfaEvent.PRICE_REQUEST, priceRequest, "", "");
			
		}
		
		private void setAttributes(TripPriceRequest priceRequest){
			StringBuffer attrBinary = new StringBuffer();
	        for(int i=0; i<Utilities.ATTRIBUTES_BITS_NUMBER; i++ ) attrBinary.append("0");
	        priceRequest.driverAttributes = attrBinary.toString();
	        priceRequest.vehicleAttributes = attrBinary.toString();
		}
	
		
		private void setAccountIDs(TripPriceRequest priceRequest, String accountCode){
			try{
				int[] accountDetails = getAccountIDs(accountCode);
				priceRequest.accountId = accountDetails[0];
				priceRequest.account_Profile_ID = accountDetails[1];
			}catch(SQLException se){
				logger.error("setAccountID failed", se);
			}
			
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
	   
//		private int getWSIntegerValue(Integer value){
//			return (value == null? 0:value);
//		}
//		
//		private String getWSStringValue(String value){
//			return (value == null? "":value);
//		}
//		
//		private float getWSDoubleValue(Double value){
//			return (value == null? 0.00f:value.floatValue());
//		}
	  
		private void insertExternalJobReference(Long jobId){
			if(pfDataSource != null){
				insertExternalJobReference(pfDataSource, jobId, ""+jobId, job_origin);
			}
//			Connection con = null;
//			CallableStatement cs = null;
//			try{
//		       	con = pfDataSource.getConnection();
//		       	cs = con.prepareCall("{ call pfadapter.insert_external_job_reference(?,?,?)}");
//		       	cs.setLong(1, jobId);
//		       	cs.setLong(2, jobId); //GoFastCab does not have different external job id
//		       	cs.setString(3, job_origin);
//		       	cs.execute();
//	
//		       }catch(SQLException se){
//			       	logger.error("insert external job reference failed with exception", se);	  
//			       	
//		       }finally{
//		       	if(cs != null) try{cs.close();}catch(SQLException ignore){};
//		       	if(con != null) try{con.close();}catch(SQLException ignore){};
//		       }
		}


			
			

		
}
