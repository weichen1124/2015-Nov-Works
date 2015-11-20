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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/JobDetailsImplement.java $
 * 
 * PF-16547, 09/09/15, DChen, using call sign for taxi plate id.
 * 
 * PF-16547, 08/10/15, DChen, using taxi plate id for eCab requirement.
 * 
 * PF-16496, 05/29/15, DChen, add after dispatch eta.
 * 
 * PF-16067, 6/24/14 DChen, added carRapidFlag in OSP RecallJobDetails
 * 
 * 21    3/12/14 3:32p Dchen
 * Added osp_users table instead of hard coding.
 * 
 * 20    2/24/14 3:15p Dchen
 * PF-15803, UDI user should retrieve all other jobs not booked by UDI.
 * 
 * 19    11/27/12 1:09p Dchen
 * PF-14930, Veolia osp changes.
 * 
 * 18    10/24/12 6:00p Ajiang
 * PF-14812 - Retrieve the stored forced address GPS in the job details
 * 
 * 17    10/24/12 5:45p Ezhang
 * * PF-14809 added job reference type check so GFC can't review MB's job, vice versa.
 * 
 * 16    7/31/12 4:45p Ezhang
 * PF-14630 fixed pickup and dropoff lat and long missing.
 * 
 * 15    2/12/11 10:01a Ezhang
 * C36130 added system id validation for GoFastCar.
 * 
 * 14    8/19/11 3:37p Dchen
 * port C34861, 34862, 35460 here.
 * 
 * 13    8/18/11 3:32p Dchen
 * upgrade to 3.75.
 * 
 * 12    4/02/11 7:13p Mkan
 * - generateJobDetailResponse(), fixed number of taxi returned in
 * response
 *   only set number of "not-cancelled" job in the response (C34962)
 * 
 * 11    12/22/10 10:28a Mkan
 * C34778, fix preferred cab not showing up in recall job detail
 * convertCart2Response()
 * - added to set callsign into response from cartVehicleOrderVO
 * 
 * 10    12/10/10 3:15p Mkan
 * - setStopPointInformation(), changed to get the remarks from the 
 *    editable manual driver notes (fixed while fixing C34595)
 * 
 * 9     12/08/10 11:07a Ajiang
 * Added droppoff GPS info
 * 
 * 8     9/27/10 5:14p Ajiang
 * added company validation
 * 
 * 7     9/20/10 2:19p Ezhang
 * OSP 2.0 added support of errorCode.
 * 
 * 6     4/30/10 4:30p Dchen
 * added pickup latitude longitude in searching job.
 * 
 * 5     4/15/10 11:49a Mkan
 * setContactInformation()
 * - fixed account code/name not being set correctly
 * convertCart2Response()
 * - update for getAdviseArrivalFlag signature change
 * - update for setContactInformation signature change
 * 
 * 4     3/03/10 4:40p Dchen
 * OSP interface.
 * 
 * 3     2/04/10 3:14p Dchen
 * pickup latitude, longitude.
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


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartModel;
import com.dds.pathfinder.callbooker.server.cart.model.PricingItem;
import com.dds.pathfinder.callbooker.server.cart.model.StopPoint;
import com.dds.pathfinder.callbooker.server.entities.JobEntity;
import com.dds.pathfinder.callbooker.server.jobsearch.ejb.JobSearch;
import com.dds.pathfinder.callbooker.server.jobsearch.ejb.JobSearchDAO;
import com.dds.pathfinder.callbooker.server.order.ejb.Order;
import com.dds.pathfinder.callbooker.server.order.model.VehicleOrderVO;
import com.dds.pathfinder.callbooker.server.order.model.VehiclePrefVO;
import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;
import com.dds.pathfinder.callbooker.server.util.ContactInformation;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.osp.impl.FindJobImplement.UniformTripStatusCode;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.itaxiinterface.webservice.Attribute;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.RecallJobDetailRequest;
import com.dds.pathfinder.itaxiinterface.webservice.RecallJobDetailResponse;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;


public class JobDetailsImplement extends OSPAddrLookupImplement {
	private Logger logger = LogManager.getLogger(this.getClass());
	
	private JobSearch jobSearch;
	private DataSource pfDataSource;
	private Order order;
	
	
	public enum JobDetailsErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_SESSIONID(2),
		NO_MATCH_TRIP(3),
		INVALID_TAXI_COMPID(4),
		INVALID_RIDE_ID(5),	
		INVALID_TAXI_RIDEID(6),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private JobDetailsErrorCode(int c) {
			   this.code = c;
		}

		public int getCode() {
			   return code;
		}
	};
	
	public JobDetailsImplement( JobSearch jobSearch, DataSource dataSource, IAddressLookup addressLookup, Order orderLocal){
		super(addressLookup);
		this.jobSearch = jobSearch;
		this.pfDataSource = dataSource;
		this.order = orderLocal;
		
//		if(jobSearchHome != null){
//			try{
//				jobSearch = jobSearchHome.create();
//			}catch(Exception e){
//				logger.error("JobDetailsImplement construct failed.", e);
//			}
//		}
	}
	
	public RecallJobDetailResponse generateResponse(BaseReq request) {
		RecallJobDetailResponse response = new RecallJobDetailResponse();
		//C36130 validate the system id and password
		if(validateSystemId(pfDataSource, request) == false){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(JobDetailsErrorCode.NOT_AUTHENTICATED.getCode());	
		}
		else if(!isValidJobDetailRequest((RecallJobDetailRequest)request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(JobDetailsErrorCode.DEFAULT_ERROR.getCode());
		}else if(!isValidCompany((RecallJobDetailRequest)request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(JobDetailsErrorCode.INVALID_TAXI_COMPID.getCode());
		}
		//PF-14809
		else if(!validateJobId((RecallJobDetailRequest)request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_JOB_ID);
			response.setErrorCode(JobDetailsErrorCode.INVALID_TAXI_RIDEID.getCode());
		}
		else{
			generateJobDetailResponse((RecallJobDetailRequest)request, response);
		}
		return response;
	}
	
	private boolean isValidJobDetailRequest(RecallJobDetailRequest request){
		if(request == null) return false;
		return (request.getTaxiCoID() > 0 && request.getTaxiRideID() > 0);
	}
	
	private boolean isValidCompany(RecallJobDetailRequest request){
		boolean res = false;
		String query = "select taxi_co_id from taxi_companies where taxi_co_id = " + request.getTaxiCoID();
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;
		try {
			con = pfDataSource.getConnection();
			stmt = con.createStatement();
			result = stmt.executeQuery(query);
			if (result.next()) {
				res = true;
			}
		} catch (SQLException se) {
			logger.error("isValidCompany failed....", se);
		} finally {
			if (result != null)try {result.close();} catch (SQLException ignore) {};
			if (stmt != null)try {stmt.close();} catch (SQLException ignore) {};
			if (con != null)try {con.close();} catch (SQLException ignore) {};
		}
		return res;
	}
	
	private void generateJobDetailResponse(RecallJobDetailRequest request, RecallJobDetailResponse response){
		if(jobSearch == null) return;
		try{	
			CartModel jobs =  jobSearch.getModifyJobDetails_EntityEJB(request.getSessionID(), "", null, ""+request.getTaxiRideID(), JobSearchDAO.JOB_RECALL_FROM_OSP);
			response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
	    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
	    	response.setErrorCode(JobDetailsErrorCode.NO_ERROR.getCode());
	    	response.setTaxiCoID(request.getTaxiCoID());
	    	response.setTaxiRideID(request.getTaxiRideID());
			if(jobs != null && jobs.getSize() > 0){
				CartItem cart = jobs.getItemAt(0);
				convertCart2Response(request.getTaxiCoID(), cart, response);
				//Number of taxi used to be the number of "non-canceled" job, right now it seems not
				//so if the number of taxi returned is equal to the number of jobs, double check each job status here to minus out the cancell job
				if (response.getNumTaxis() == jobs.getSize()) 
				{
					int cancelledJobs = 0;
					for (int i=0; i<jobs.getSize(); i++)
					{
						String state = jobs.getItemAt(i).getState_Text();
						if (state != null && state.equalsIgnoreCase(UniformTripStatusCode.SERVICED_CANCELLED.getStatus()))
						{
							cancelledJobs++;
						}
					}
					response.setNumTaxis(jobs.getSize() - cancelledJobs);
				}
			}
		}catch(Exception e){
			logger.error("generateJobsResponse failed..");
		}
	}
	
	private void convertCart2Response(Integer taxiCoID, CartItem cart, RecallJobDetailResponse response){
		if(cart == null) {
			return;
		}
		
		response.setNumTaxis(cart.getTaxiCount());
		response.setPassangerNr(string2Int(cart.getPassengetCount()));
		StopPoint stopPoint = cart.getStopPoint(0);
		response.setAdviseArrival(AdviseArrivalTypesImplement.AdviseArrivalType.NoAdvise.toOSPValue());
		if(stopPoint != null){
			setStopPointInformation(stopPoint, response);
			ContactInformation contact = stopPoint.getContactInfo();
			if(contact != null){
				setContactInformation(taxiCoID, contact, response);
			}
			response.setAdviseArrival(getAdviseArrivalFlag(pfDataSource, stopPoint, contact));

		}
		
		response.setType(convertOSPTripType(cart.getJOB_TYPE()) );
		response.setTripStatus(cart.getState_Text());
		response.setPriority(Utilities.calcJobPriority(cart.getJob_priority()));
		VehicleOrderVO prefCar = cart.getVehicleOrderVO();
		if (prefCar != null)
		{
			VehiclePrefVO prefCarVO = prefCar.getVehiclePrefVO();
			if (prefCarVO != null && prefCarVO.getCalliSign() != null)
			{
				response.setCabNum(prefCarVO.getCalliSign().trim()); //C34778, fix preferred cab not showing up in recall job detail
			}
		}
		response.setDispatchedCar(cart.getVehicleCallsign());
		setCarDetailResponse(response);
		setJobAttributes(cart.getDriverAttributeBinary(), cart.getVehicleAttributeBinary(), response, taxiCoID);
		if (OSPCartImplement.JOB_TYPE_ASAP_RAW.equals(cart.getJOB_TYPE())) {
			response.setPickupTime(null); //this is ASAP job, set pickup as null in response to indicate
		} else {
			response.setPickupTime(Utilities.composeOSPDefaultDate(
									Utilities.convertDB2OSPDateFormat(cart.getDate()),
									Utilities.convertDB2OSPTimeFormat(cart.getTime())));
		}
		response.setTripRsvTime(Utilities.composeOSPDefaultDate(
									Utilities.convertDB2OSPDateFormat(cart.getBookDate()),  
									Utilities.convertDB2OSPTimeFormat(cart.getBookTime())));
		setPricingResponse(cart.getPricingItem(), response);
		setMissedJobResponse(response);
		
		response.setJobExtraInfo1(cart.getExtraInfo1());
		response.setJobExtraInfo2(cart.getExtraInfo2());
		response.setJobExtraInfo3(cart.getExtraInfo3());
		response.setJobExtraInfo4(cart.getExtraInfo4());
		response.setJobExtraInfo5(cart.getExtraInfo5());
		if(stopPoint != null)
			response.setSpExtraInfo1(stopPoint.getExtraInfo1());
		response.setEtaInSec(cart.getTime_estimated());
	}
	
	
	private void setMissedJobResponse(RecallJobDetailResponse response){
		response.setCarRapidFlag("N");
		if(order != null && response.getTaxiRideID() > 0 ){
			JobEntity jobEntity = order.findJobByJobID(response.getTaxiRideID());
			if(jobEntity != null && "Y".equalsIgnoreCase(jobEntity.getRapidMeterFlag()) ) response.setCarRapidFlag("Y");
		}
	}
	
	
	private void setJobAttributes(String drvAttributes, String vehAttributes, RecallJobDetailResponse response, int companyID){
		
		ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>();
		CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
		attributes.addAll(Utilities.getAttributesItem(CompanyDefaultValues.DriverAttributesMap.get(companyID), Utilities.ToBinaryAttributesString(drvAttributes)));
		attributes.addAll(Utilities.getAttributesItem(CompanyDefaultValues.VehicleAttributesMap.get(companyID), Utilities.ToBinaryAttributesString(vehAttributes)));
		
		if(attributes.size() > 0){
			Attribute[] attrArray = new Attribute[attributes.size()];
			for(int i=0; i<attributes.size(); i++){
				attrArray[i] = new Attribute(attributes.get(i).getAttrShortName());
			}
			response.setAttributeList(attrArray);
		}
	}
	
	private void setContactInformation(Integer taxiCoID, ContactInformation contact, RecallJobDetailResponse response){
		response.setTelephoneNr(contact.getTelephone());
		response.setTelephoneExt(contact.getTelExt());
		response.setPassangerName(contact.getGivenName());
		
		//contact stores the account code, check here to see if it's the "CASH" account code
		String accountCode = contact.getAccount();
		if (accountCode != null) {
			if (accountCode.equals(CompanyDefaultValues.getCompanyDefaultCashAccountNb(pfDataSource, taxiCoID))) {
				response.setAccountCode(null); //reply with null account to indicate "pay by cash"
				response.setAccountName(null);
			} else {
				response.setAccountCode(accountCode);
				response.setAccountName(contact.getAccount_Name());
			} 
		} else {
			//somehow we failed to get what this job is booked with
			response.setAccountCode("N/A"); //TODO: reply with error 
			response.setAccountName("N/A");
		}
		
		response.setAuthNum(contact.getReference());
	}
	
	
	
	private void setStopPointInformation(StopPoint stopPoint, RecallJobDetailResponse response){
		AddressItem pickup = stopPoint.getPickupAddress();
		AddressItem forcedPickup = stopPoint.getForced_Address_Item();
		if(pickup != null){
			response.setPickupStreetName(pickup.getStrName());
			response.setPickupStreetNr(pickup.getStrNum());
			response.setPickupRegion(pickup.getRegion());
			response.setPickupLandmark(pickup.getBuilding_Name());
			response.setPickupUnit(pickup.getunitNr());
			response.setPickupZoneNumber(pickup.getAreaName());
			response.setRemarks(pickup.getManualDrvNotes()); //we should only send back the "editable" driver notes, not the read-only (possibly from account) driver notes.
			if(pickup.getForcedAddressFlag() == false){
				setResponsePickupGPS(pickup, response);
			}else{
				setResponsePickupGPS(forcedPickup, response);
			}
		}
		AddressItem setdown = stopPoint.getSetdownAddress();
		if(setdown != null ){
			response.setDropoffStreetName(setdown.getStrName());
			response.setDropoffStreetNr(setdown.getStrNum());
			response.setDropoffRegion(setdown.getRegion());
			response.setDropoffLandmark(setdown.getBuilding_Name());
			response.setDropoffUnit(setdown.getunitNr());
			response.setDropoffZoneNumber(setdown.getAreaName());
			/* PF-14630 get dropoff GPS lat and long if address is there*/
			if(!setdown.getStrName().isEmpty() && !setdown.getStrNum().isEmpty() && !setdown.getRegion().isEmpty()) {
				PFAddressResponse setdownAddressRes = getWSAddressData(
					   appendPercentage(setdown.getStrName(), false), 
					   appendPercentage(setdown.getStrNum(), false), 
					   appendPercentage(setdown.getunitNr(), false), 
					   appendPercentage(setdown.getRegion(),false),
					   appendPercentage(setdown.getBuilding_Name(), false),
					   PERCENTAGE_CHAR, PERCENTAGE_CHAR, //organization and postal code
					   response.getTaxiCoID(), "");
			
				PFAddressData setdownAddress = getMatchAddress(setdownAddressRes, setdown.getStrName(), setdown.getStrNum(), setdown.getRegion());
				if(setdownAddress != null) {
					response.setDropoffLat(setdownAddress.getLatitude());
					response.setDropoffLon(setdownAddress.getLongitude());
				}
				
			}
			
			
		}
	}
	
	
	private void setResponsePickupGPS(AddressItem pickup, RecallJobDetailResponse response){
		response.setPickupLat(pickup.getLatitude());
		response.setPickupLon(pickup.getLongitude());
		setResponseCarGPS(response);
	}
	

	private void setResponseCarGPS(RecallJobDetailResponse response){
		if(pfDataSource != null && response.getTaxiRideID() > 0){
			Connection con = null;
			Statement stmt = null;
	        ResultSet rs = null;
			try{
				con = pfDataSource.getConnection();
		        stmt = con.createStatement();
				rs = stmt.executeQuery("select vs.latitude, vs.longitude from jobs j, vehicle_states vs where j.vehicle_id = vs.vehicle_id and j.job_id = "+response.getTaxiRideID());
				if(rs.next()){
					double latitude = rs.getDouble("latitude");
					double longitude = rs.getDouble("longitude");
					if(isValidDoubleValue(latitude) && isValidDoubleValue(longitude) 
							&& latitude != 0 && longitude != 0){
						response.setCarLatitude(latitude);
						response.setCarLongitude(longitude);
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
	
	private String convertOSPTripType(String type){
		if(OSPCartImplement.JOB_TYPE_ASAP_RAW.equals(type)) return "1";
		else if(OSPCartImplement.JOB_TYPE_PBOK_RAW.equals(type)) return "2";
		else if(OSPCartImplement.JOB_TYPE_TBA_RAW.equals(type)) return "3";
		else return "0";
	}
	

	
	private int string2Int(String s){
		int i = 0;
		if(s == null ) return i;
		else{
			try{
				i= Integer.parseInt(s);
			}catch(NumberFormatException ne){
				logger.info("string2Int failed: "+s, ne);
			}
		}
		return i;
	}
	
	//PF-14809 check the job_id with job reference type to prevent recall job from other type(eg. Webbooker, GFC, MB)
	private boolean validateJobId(RecallJobDetailRequest request) {
		boolean res = false;
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;
		long jobID = request.getTaxiRideID();
		String referenceType = null;
		int systemId = request.getSystemID();
		
		//PF-14809 check job_references table if request job match the reference type
		referenceType = getSystemReference(systemId);
		
		if (referenceType != null && !referenceType.isEmpty()) {
		   String query = "select job_id, reference_type from job_references "
				   + " where job_id = " + jobID;
		
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
				else if(systemId == ExternalSystemId.SYSTEM_ID_WEB_BOOKER.getSystemId() 
						|| systemId == ExternalSystemId.SYSTEM_ID_UDI_USER.getSystemId()  //UDI user should access the same way as web booker.
						|| isAccessableInternalJobs(systemId)) {		
					res = true; 
				}
				
				
	       }catch(SQLException se){
		       	logger.error("validateJobId() failed with exception", se);
		       	
	       }finally{
	    	    if (result != null)try {result.close();} catch (SQLException ignore) {};
	    	    if (stmt != null)try {stmt.close();} catch (SQLException ignore) {};
	       		if(con != null) try{con.close();}catch(SQLException ignore){};
	       		
	       }
		}
		
	    return res;
	   
	}
	
	private void setPricingResponse(PricingItem pricingItem, RecallJobDetailResponse response){
		if(pricingItem == null) return;
		DecimalFormat df = new DecimalFormat("0.00");
		double amount = pricingItem.calculateTotalPrice();
		try{
			response.setFareAmount(df.format(amount));
			response.setFareCollected(df.format(amount));
		}catch(ArithmeticException ae){
			logger.error("setPricingResponse failed with format error" + amount);
		}
		
	}
	
	//PF-16547 Add car detail to recallJobdetail response
	private void setCarDetailResponse(RecallJobDetailResponse response){
		
		if(pfDataSource != null && response.getTaxiRideID() > 0 && response.getDispatchedCar() != null){
			Connection con = null;
			Statement stmt = null;
	        ResultSet rs = null;
			try{
				con = pfDataSource.getConnection();
		        stmt = con.createStatement();
				// rs = stmt.executeQuery("select v.vehicle_colour, v.vehicle_make, v.taxi_plate_id, d.badge_nr from jobs j, vehicles v, drivers d where j.vehicle_id = v.vehicle_id and j.driver_id = d.driver_id and j.job_id = "+response.getTaxiRideID());
		        rs = stmt.executeQuery("select v.vehicle_colour, v.vehicle_make, v.callsign, d.badge_nr from jobs j, vehicles v, drivers d where j.vehicle_id = v.vehicle_id and j.driver_id = d.driver_id and j.job_id = "+response.getTaxiRideID());
				if(rs.next()){
					String carColour = rs.getString("vehicle_colour");
					String carBrand = rs.getString("vehicle_make");
					String carRegPlate = rs.getString("callsign");
					String badgeNr = rs.getString("badge_nr");
				
					//logger.info("setCarDetailResponse: "+ carColour + " " + carBrand + " " + carRegPlate + " " + badgeNr);
					response.setCarColour(carColour);
					response.setCarBrand(carBrand);
					response.setCarRegPlate(carRegPlate);
					response.setDispatchedDriver(badgeNr);
				
				}
			}catch(SQLException se){
				logger.error("setCarDetailResponse failed: ", se);
			}finally{
			
				if (rs != null) try{rs.close();}catch(SQLException ignore){};
	    	    if (stmt != null) try{stmt.close();}catch(SQLException ignore){};
	    	    if(con != null) try{con.close();}catch(SQLException ignore){};
			}
		}
	}

}
