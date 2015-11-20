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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/CarValidationImplement.java $
 * 
 * 3     2/12/11 10:00a Ezhang
 * C36130 Added system id validation.
 * 
 * 2     9/24/10 3:25p Ezhang
 * bug fixed
 * 
 * 1     9/20/10 2:08p Ezhang
 * OSP 2.0 new method.
 */
package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.CarValidationReq;
import com.dds.pathfinder.itaxiinterface.webservice.CarValidationRsp;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;


/**
 * CarValidationImplement performs call sign validation against the pathfinder table vehicles
 *  and return the vehicle status from vehicle_states as well  if the call sign exists.
 * @author ezhang
 * @version 2.0  06 Sep 2010
 */
public class CarValidationImplement extends OSPBaseImplement {

	private Logger logger = LogManager.getLogger(this.getClass());
	
    private DataSource pfDataSource;
    
    
    public enum CarValidationErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_SESSIONID(2),
		CAR_NOT_FOUND(3),
		NOMATCH_COMP_CAR(4),
		INVALID_CARNUM(5),
		INVALID_COMPID(7),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private CarValidationErrorCode(int c) {
			   code = c;
		}

		public int getCode() {
			   return code;
		}
	};
	//match Pathfinder vehicle_status string value of vehicle_states table to OSP car status code number
	public enum CarStatusCode{
		SIGNON("F", 1), //Car signed on, 
		BOOKIN("O", 2),	//Car get job offer, Pathfiner does not have bookin status as Taxitrack
		IN_TRANSIT("Y", 3), //car accept the offer but hasn't arrived yet
		ARRIVED("A", 4),  //The car has been arrived but hasn’t pickup the passenger
		IN_SERVICE("P", 5), //Passenger on board
		SIGNOFF("Z", 6); //car is booked off
		
		private int code;
		private String status;
		
		private CarStatusCode(String s, int c) {
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
    
    public CarValidationImplement(DataSource pfDataSource) {
		super();
		this.pfDataSource = pfDataSource;
	}
    
	public CarValidationRsp generateResponse(BaseReq request) {
		return generateCarValidationResponse((CarValidationReq) request);
	
	}
	
	private CarValidationRsp generateCarValidationResponse(CarValidationReq request){
		CarValidationRsp response = getDefaultResponse(request);
		
		if(!validateRequest(request, response)){
			   return response;
		}
		
		if(isValidCar(request, response)){
				response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
		    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
		    	response.setErrorCode(CarValidationErrorCode.NO_ERROR.getCode());
				return response;
		}
	    return response;	  //return default response
		
	}
	
	private CarValidationRsp getDefaultResponse(CarValidationReq request){
		
		CarValidationRsp response = new CarValidationRsp();
		   
		response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
		response.setErrorCode(CarValidationErrorCode.DEFAULT_ERROR.getCode());
		   
		return response;
	}
	/**
	 * Validate car validation request.
	 * 
	 * @param request
	 *            the request to validate
	 * @param response
	 *            the response to update if error
	 * @return true if valid, false otherwise
	 */
	private boolean validateRequest(CarValidationReq request, CarValidationRsp response) {
		
		if (request == null || response == null) {
			return false;
		}
		//validate system id
		
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(CarValidationErrorCode.NOT_AUTHENTICATED.getCode());
		}
		
		//validate callsign
		if (request.getCarID() == null || request.getCarID().trim().length() == 0){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(CarValidationErrorCode.INVALID_CARNUM.getCode());
			return false;
		}
		//validate taxi company id
		if(request.getTaxiCoID() == null ||request.getTaxiCoID() <= 0 || request.getTaxiCoID()> 999999999){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
			response.setErrorCode(CarValidationErrorCode.INVALID_COMPID.getCode());
			return false;
		}
		return true;
	}
	//validate car with DB and retrieve the vehicle status.
	private boolean isValidCar(CarValidationReq request, CarValidationRsp response){
		
		String query = "select vs.vehicle_status, tcv.taxi_co_id" + 
						" from vehicles ve, taxi_co_vehicles tcv, vehicle_states vs" +
						" where tcv.vehicle_id = ve.vehicle_id" +
						" and ve.vehicle_id = vs.vehicle_id" +
						" and tcv.relation_type = 'A' " +
						" and ve.CALLSIGN = '" + request.getCarID() +"'";
		
		Connection con = null;
		Statement stmt= null;
		ResultSet result = null;
		String vehicleStatus = null;
		int compId = -1;
		boolean status = true;
		
		try{			
			con = pfDataSource.getConnection();
			stmt = con.createStatement();
			result = stmt.executeQuery(query);
			if(result.next()){
				vehicleStatus = result.getString("VEHICLE_STATUS");
				compId = result.getInt("TAXI_CO_ID");			
			}

		}catch(SQLException se){
			logger.error("isValidCar failed...." , se);
		}finally{
			if(result != null) try{result.close();}catch(SQLException ignore){};
			if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
			if(con != null) try{con.close();}catch(SQLException ignore){};
		}
		if( vehicleStatus != null && compId != request.getTaxiCoID()){
			response.setErrorCode(CarValidationErrorCode.NOMATCH_COMP_CAR.getCode());
			return false;
		}
		else if (vehicleStatus == null){
			response.setErrorCode(CarValidationErrorCode.CAR_NOT_FOUND.getCode());
			return false;
		}
		//match the vehicle status to return vehicle status code
		if(Character.toString(vehicleStatus.charAt(0)).equals (CarStatusCode.SIGNON.getStatus())){
			response.setCarStatus(CarStatusCode.SIGNON.getCode());
		}
		else if(Character.toString(vehicleStatus.charAt(0)).equals (CarStatusCode.BOOKIN.getStatus())){
			response.setCarStatus(CarStatusCode.BOOKIN.getCode());
		}
		else if(Character.toString(vehicleStatus.charAt(0)).equals (CarStatusCode.ARRIVED.getStatus())){
			response.setCarStatus(CarStatusCode.ARRIVED.getCode());
		}
		else if(Character.toString(vehicleStatus.charAt(0)).equals (CarStatusCode.IN_TRANSIT.getStatus())){
			response.setCarStatus(CarStatusCode.IN_TRANSIT.getCode());
		}
		else if(Character.toString(vehicleStatus.charAt(0)).equals (CarStatusCode.IN_SERVICE.getStatus())){
			response.setCarStatus(CarStatusCode.IN_SERVICE.getCode());
		}
		else if(Character.toString(vehicleStatus.charAt(0)).equals (CarStatusCode.SIGNOFF.getStatus())){
			response.setCarStatus(CarStatusCode.SIGNOFF.getCode());
		}
		else{
			status = false;
		}
		return status;
	}
	
	
}
