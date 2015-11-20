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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/AdviseArrivalTypesImplement.java $
 * 
 * 7     1/27/14 5:14p Dchen
 * attempts count typo.
 * 
 * 6     1/16/14 3:43p Dchen
 * PF-15847, UDI callout extensions.
 * 
 * 5     8/29/12 4:45p Dchen
 * PF-14689, add PF SMS arrival advise to OSP.
 * 
 * 4     2/12/11 10:00a Ezhang
 * C36130 added system id validation.
 * 
 * 3     4/15/10 11:25a Mkan
 * To fix callout not being booked correctly
 * - added pfValueAbbr in AdviseArrivalType. 
 *   PF sometimes use the whole callout name and sometimes use the abbr.
 *   To ensure advise arrival from OSP works, both will be used to book a
 * job.
 * - also added to check PF options before allowing advise arrival values
 * to be returned to OSP
 * 
 * 2     2/02/10 2:35p Dchen
 * OSP interface.
 * 
 * 1     1/26/10 5:43p Dchen
 * OSP interface.
 * 
 * 
 * ******/
package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.order.ejb.Order;
import com.dds.pathfinder.itaxiinterface.common.impl.BaseImplement;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.webservice.AdviseArrivalListItem;
import com.dds.pathfinder.itaxiinterface.webservice.AdviseArrivalRequest;
import com.dds.pathfinder.itaxiinterface.webservice.AdviseArrivalResponse;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.GetUDICalloutRequest;
import com.dds.pathfinder.itaxiinterface.webservice.GetUDICalloutResponse;
import com.dds.pathfinder.itaxiinterface.webservice.UDICalloutListItem;
import com.dds.pathfinder.itaxiinterface.webservice.UpdateUDICalloutReq;
import com.dds.pathfinder.itaxiinterface.webservice.UpdateUDICalloutRes;

public class AdviseArrivalTypesImplement extends OSPBaseImplement {

	private static final Logger logger = LogManager.getLogger(AdviseArrivalTypesImplement.class);
	private DataSource pfDataSource;
	
	private Order order;
	
	private static final int UDI_CALLOUT_TYPE_ALLTYPE = 0;
	private static final int UDI_CALLOUT_TYPE_TOARRIVE = 1;
	private static final int UDI_CALLOUT_TYPE_STALECALL = 2;
	
	
	/**
	 * Contains available OSP/PF advise arrival value mapping.
	 * AdviseType(OSPValue, PFValue)
	 */
//	public static enum AdviseArrivalType  {
//		/** no advise */
//		NoAdvise("1", "Denied", "N"),
//		
//		/** voice advise */
//		VoiceAdvise("2", "Required", "R"),
//		
//		/** email advise, not supported by PF */
//		EmailAdvise("3", null, null),  //set to null here so that it'll not be returned to OSP system
//		
//		/** SMS via Email, not supported by PF*/
//		SMSAdvise("4", "Required SMS", "C");	//PF added SMS with C36064, update OSP as well. PF has other SMS optional as well, just use required one here.
//		
//		private final String ospValue;
//		private String pfValue;
//		private String pfValueAbbr;
//		
//		
//		AdviseArrivalType(String ospValue, String pfValue, String pfValueAbbr){
//			this.ospValue = ospValue;
//			this.pfValue = pfValue;
//			this.pfValueAbbr = pfValueAbbr;
//		}
//		
//		public String toOSPValue(){
//			return this.ospValue;
//		}
//		public String toPFValue(){
//			return this.pfValue;
//		}
//		public String toPFValueAbbr() {
//			return this.pfValueAbbr;
//		}
//		/**
//		 * Clear PF value mapping for this advise arrival.
//		 */
//		public void clearPF() {
//			this.pfValue = null;
//			this.pfValueAbbr = null;
//		}
//	};
	
	/** indicate if we've set up the advise arrival values */
	private static boolean adviseArrivalSetup = false; 
	
	public AdviseArrivalTypesImplement(DataSource pfDataSource) {
		setupAdviseArrivalValue(pfDataSource);
		this.pfDataSource = pfDataSource;
	}
	
	public AdviseArrivalTypesImplement(DataSource pfDataSource, Order orderLocal,  boolean isUDI) {
			this.pfDataSource = pfDataSource;
			this.order = orderLocal;
			if(!isUDI){
				setupAdviseArrivalValue(pfDataSource);
			}
	}
	
	public AdviseArrivalResponse generateResponse(BaseReq request) {
		AdviseArrivalResponse response = new AdviseArrivalResponse();
		if(!isValidAdviseArrivalRequest((AdviseArrivalRequest)request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
		}else if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			//response.setErrorCode(BookJobErrorCode.NOT_AUTHENTICATED.getCode());
		}
		else{
			generateAdviseArrivalResponse(response);
		}
		return response;
	}
	
	
	public GetUDICalloutResponse generateGetUDICalloutResponse(GetUDICalloutRequest request) {		
		GetUDICalloutResponse response = new GetUDICalloutResponse();
		
		if(!isValidGetUDICalloutRequest(request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
		}else if (!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
		}else{
			generateGetUDICalloutResponse(request, response);
		}
		
		return response;
	}
	
	public UpdateUDICalloutRes generateUpdateUDICalloutResponse(UpdateUDICalloutReq request) {		
		UpdateUDICalloutRes response = new UpdateUDICalloutRes();
		
		if(!isValidUpdateUDICalloutRequest(request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
		}else if (!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
		}else{
			updateUDICalloutResponse(request, response);
			if(order != null) order.addLogEntry( getUserName(request, null), "", request.getTaxiRideID(), "UDI_CLL_UPD", request.getDisposition());
			else logger.error("add job log failed with order null.");
		}
		
		return response;
	}
	
	private boolean isValidAdviseArrivalRequest(AdviseArrivalRequest request){
		return (request != null && request.getTaxiCoID() != null && request.getTaxiCoID() > 0);
	}
	
	
	private boolean isValidGetUDICalloutRequest(GetUDICalloutRequest request){
		if(request == null || request.getTaxiCoID() == null || request.getTaxiCoID() <= 0) return false;
		if(request.getCalloutType() == null || request.getCalloutType() < UDI_CALLOUT_TYPE_ALLTYPE || request.getCalloutType() > UDI_CALLOUT_TYPE_STALECALL) return false;
		if(request.getCalloutType() != UDI_CALLOUT_TYPE_TOARRIVE) return isValidStaleCallMinutes(request.getStaleTripPassedMin());
		return true;
	}
	
	private boolean isValidStaleCallMinutes(Integer staleMinutes){
		return (staleMinutes != null && staleMinutes > 0);
	}
	
	private boolean isValidUpdateUDICalloutRequest(UpdateUDICalloutReq request){
		if(request == null || request.getTaxiCoID() == null || request.getTaxiCoID() <= 0) return false;
		if(request.getTaxiRideID() == null || request.getTaxiRideID() <= 0) return false;
		if(request.getCalloutID() == null || request.getCalloutID() <= 0) return false;
		return true;
	}
	
	private void updateUDICalloutResponse(UpdateUDICalloutReq request, UpdateUDICalloutRes response){
		if(pfDataSource == null) {
			logger.error("updateUDICalloutResponse failed with pfDataSource null.  ");
			return;
		}
		
		Connection dbConnection = null;
		CallableStatement stmt = null;
		
		try {
			if ((dbConnection = pfDataSource.getConnection()) == null){
				logger.error("Failed to get db connection in updateUDICalloutResponse.....");
				return;
			}
			
			stmt = dbConnection.prepareCall("{call udi_callout_event.update_udi_event_fromUDI(?,?,?,?,?,?)}");
			if(request.getTaxiRideID() != null) stmt.setLong(1, request.getTaxiRideID());
			else stmt.setNull(1, Types.BIGINT);
			
			if(request.getTaxiCoID() != null) stmt.setInt(2, request.getTaxiCoID());
			else stmt.setNull(2, Types.INTEGER);
			
			
			stmt.setString(3, request.getCompletedFlag() == null ? "N" : request.getCompletedFlag());
			stmt.setString(4, request.getCallNumber());
			stmt.setInt(5, (request.getNumberOfAttempt() == null ? 0: request.getNumberOfAttempt() ) );
			
			if(request.getCalloutID() != null) stmt.setLong(6, request.getCalloutID());
			else stmt.setNull(6, Types.BIGINT);
				
			stmt.execute();
				
		} catch (SQLException e) {
			logger.error("SQLException in getToArriveCalloutItems: " ,e);
			return;
		} finally {
			try {if (stmt != null) stmt.close();} catch (SQLException ignore) {};
			try {if (dbConnection != null) dbConnection.close();} catch (SQLException ignore) {};
		}
		
		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
	}
	
	
	private void generateGetUDICalloutResponse(GetUDICalloutRequest request, GetUDICalloutResponse response){
		if(pfDataSource == null) {
			logger.error("generateGetUDICalloutResponse failed with pfDataSource null.  ");
			return;
		}
		
		ArrayList<UDICalloutListItem> calloutItems = new ArrayList<UDICalloutListItem>();
		if(request.getCalloutType() == UDI_CALLOUT_TYPE_TOARRIVE || request.getCalloutType() == UDI_CALLOUT_TYPE_ALLTYPE){
			getToArriveCalloutItems(calloutItems);
		}
		
		if(request.getCalloutType() == UDI_CALLOUT_TYPE_STALECALL || request.getCalloutType() == UDI_CALLOUT_TYPE_ALLTYPE){
			getStaleCalloutItems(calloutItems, request.getStaleTripPassedMin());
		}
		
		addCalloutItemListResponse(calloutItems, response);
	}
	
	private void addCalloutItemListResponse(ArrayList<UDICalloutListItem> calloutItems,  GetUDICalloutResponse response){
		if(calloutItems == null) return;
		int nbOfEvents = calloutItems.size();
		response.setNumberOfEvents(nbOfEvents);
		if(nbOfEvents > 0){
			UDICalloutListItem[] events = new UDICalloutListItem[nbOfEvents];
			calloutItems.toArray(events);
    		response.setListOfCalloutEvents(events);
		}
		
		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
	}
	
	
	private void getToArriveCalloutItems(ArrayList<UDICalloutListItem> calloutItems){
		
	
		String query = "select job_id, taxi_co_id, pickup_dtm, callout_type, completed_flag, telephone_number, email_address, contact_type, " +
				" callout_attempts_cnt, last_requested_dtm, callout_id from TABLE(udi_callout_event.get_to_arrive_udi_callout)" ;
		
		
		PreparedStatement stmt = null;
		ResultSet rset = null;
		Connection dbConnection = null;
		
		try {
			if ((dbConnection = pfDataSource.getConnection()) == null){
				logger.error("Failed to get db connection in getToArriveCalloutItems.....");
				return;
			}
			
			
			//log.info(query.toString()); 
			stmt = dbConnection.prepareStatement(query);
			rset = stmt.executeQuery();
				
			
			//Process the timers that we got from the query above
			while (rset.next()) {
				long jobID = rset.getLong("job_id");
				
				UDICalloutListItem callItem = new UDICalloutListItem(jobID, UDI_CALLOUT_TYPE_TOARRIVE);
				
				String value = rset.getString("telephone_number");
				if(value != null && !value.isEmpty()) callItem.setPhoneNumber(value);
				value = rset.getString("email_address");
				if(value != null && !value.isEmpty()) callItem.setEmailAddress(value);
				value = rset.getString("contact_type");
				if(value != null && !value.isEmpty()) callItem.setContactType(value);
				long calloutID = rset.getLong("callout_id");
				if(calloutID > 0) callItem.setCalloutID(calloutID);
				else callItem.setCalloutID(null);
				
				calloutItems.add(callItem);
			
			}
		} catch (SQLException e) {
			logger.error("SQLException in getToArriveCalloutItems: " ,e);
		} finally {
			try {if (rset != null) rset.close();} catch (SQLException ignore) {};
			try {if (stmt != null) stmt.close();} catch (SQLException ignore) {};
			try {if (dbConnection != null) dbConnection.close();} catch (SQLException ignore) {};
		}
				
	}
	
	private void getStaleCalloutItems(ArrayList<UDICalloutListItem> calloutItems, int statleMinutes){
		
		
		String query = "select job_id, taxi_co_id, pickup_dtm, callout_type, completed_flag, telephone_number, email_address, contact_type, " +
				" callout_attempts_cnt, last_requested_dtm, callout_id from TABLE(udi_callout_event.get_stale_udi_callout(?))" ;
		
		
		PreparedStatement stmt = null;
		ResultSet rset = null;
		Connection dbConnection = null;
		
		try {
			if ((dbConnection = pfDataSource.getConnection()) == null){
				logger.error("Failed to get db connection in getStaleCalloutItems .....");
				return;
			}
			
			
			//log.info(query.toString()); 
			stmt = dbConnection.prepareStatement(query);
			stmt.setInt(1, statleMinutes);
			rset = stmt.executeQuery();
				
			
			//Process the timers that we got from the query above
			while (rset.next()) {
				long jobID = rset.getLong("job_id");
				
				UDICalloutListItem callItem = new UDICalloutListItem(jobID, UDI_CALLOUT_TYPE_STALECALL);
				
				String value = rset.getString("telephone_number");
				if(value != null && !value.isEmpty()) callItem.setPhoneNumber(value);
				value = rset.getString("email_address");
				if(value != null && !value.isEmpty()) callItem.setEmailAddress(value);
				value = rset.getString("contact_type");
				if(value != null && !value.isEmpty()) callItem.setContactType(value);
				long calloutID = rset.getLong("callout_id");
				if(calloutID > 0) callItem.setCalloutID(calloutID);
				else callItem.setCalloutID(null);
				
				calloutItems.add(callItem);
			
			}
		} catch (SQLException e) {
			logger.error("SQLException in getStaleCalloutItems: " ,e);
		} finally {
			try {if (rset != null) rset.close();} catch (SQLException ignore) {};
			try {if (stmt != null) stmt.close();} catch (SQLException ignore) {};
			try {if (dbConnection != null) dbConnection.close();} catch (SQLException ignore) {};
		}
				
	}
	
	
	/**
	 * Setup advise arrival value according to system parameter.
	 * 
	 * From help:-
	 * Callbacks can be completed by the supervisor or by an automated recording.
	 *	Supervisor callbacks - When the driver, using the MDT terminal, indicates the pending arrival to 
	 *						   the pickup address, an event is raised on the Supervisor screen. 
	 *						   The supervisor can then telephone the customer to inform them of the vehicle's arrival time.
	 *	Automatic recorded message callbacks - When the driver, using the MDT termi­nal, indicates the pending arrival 
	 *						   to the pickup address, the PBX / IVR tel­ephones the customer automatically and 
	 *						   plays recorded message.
	 *	To create an advise arrival and callout job:
	 *		In the Service area, select the type of callout for the trip
	 *			Required - enable callout for this job, even for drivers with callout disa­bled
	 *			Denied - will disable callout for this job
	 *			Not specified - driver settings will be used.
	 *		Book the job the rest of the job as needed.
	 *  NOTE: You can only use the Callout feature if you have purchased the Callout option with your edition of PathFinder.
	 */
	private static void setupAdviseArrivalValue(DataSource datasource) {
		
		if (adviseArrivalSetup) {
			return; //already setup
		}
		
		boolean adviseArrival = CompanyDefaultValues.isOptionEnabled(datasource, CompanyDefaultValues.RTL_TYPE_CALLOUT_OPTION);
		if (!adviseArrival) { //callout feature is disabled in PF
			AdviseArrivalType.VoiceAdvise.clearPF();
		}
		
		adviseArrivalSetup = true;
	}
	
	private void generateAdviseArrivalResponse(AdviseArrivalResponse response){

		//build list for types that are available to use (the ones that have pfValue set)
		ArrayList<AdviseArrivalListItem> adviseArrivalList = new ArrayList<AdviseArrivalListItem>();
		for(AdviseArrivalType t : AdviseArrivalType.values()){
			if (t.toPFValue() != null) {
				adviseArrivalList.add(new AdviseArrivalListItem(t.toOSPValue()));
			}
    	}
		
    	int nbOfType = 0;
    	if((nbOfType = adviseArrivalList.size()) > 0){
    		response.setNumberOfRec(nbOfType);
    		AdviseArrivalListItem[] types = new AdviseArrivalListItem[nbOfType];
    		adviseArrivalList.toArray(types);
    		response.setAdviseArrivalList(types);
    	}
    	
		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
	}

	/**
	 * Return PF callout value for specified OSP advise arrival value.
	 * @param pfDataSource PF data source
	 * @param ospValue	OSP advise arrival value
	 * @param pfValue	PF callout value
	 * @param pfValueAbbr PF callout value associated abbreviation
	 */
	public static void getPFCalloutValue(DataSource pfDataSource, String ospValue, String[] pfValue, String[] pfValueAbbr){
		if (pfValue == null && pfValueAbbr == null) {
			logger.error("u forgot to init pfValue or pfValueAbbr...");
			return;
		}
		
		setupAdviseArrivalValue(pfDataSource);
		
		//no advise by default
		if (pfValue != null) {
			pfValue[0] = AdviseArrivalType.NoAdvise.toPFValue();
		}
		if (pfValueAbbr != null) {
			pfValueAbbr[0] = AdviseArrivalType.NoAdvise.toPFValueAbbr(); 
		}
		
		if(ospValue == null || ospValue.length() == 0)  {
			return; //return using default above
		}
		for(AdviseArrivalType t : AdviseArrivalType.values()){
			//set associated mapping values to return
			if(ospValue.equals(t.toOSPValue())) {
				if (pfValue != null) {
					pfValue[0] =  t.toPFValue();
				}
				if (pfValueAbbr != null) {
					pfValueAbbr[0] = t.toPFValueAbbr(); 
				}
				return;
			}
		}
		logger.warn("No PF callout mapping is found for OSP requested value: " + ospValue);
	} 
	
	/**
	 * Return OSP advise arrival value for specified PF callout value (abbreviation)
	 * @param pfDataSource	PF data source
	 * @param pfValueAbbr	PF callout abbreviated value
	 * @return OSP advise arrival value
	 */
	public static String getOSPAdviseArrivalValue(DataSource pfDataSource, String pfValueAbbr){
		if(pfValueAbbr == null || pfValueAbbr.length() == 0) { 
			return AdviseArrivalType.NoAdvise.toOSPValue();
		}
		
		setupAdviseArrivalValue(pfDataSource);
		
		for(AdviseArrivalType t : AdviseArrivalType.values()){
			if(pfValueAbbr.equals(t.toPFValueAbbr())) return t.toOSPValue();
		}
		return AdviseArrivalType.NoAdvise.toOSPValue();  //by default
	}


}
