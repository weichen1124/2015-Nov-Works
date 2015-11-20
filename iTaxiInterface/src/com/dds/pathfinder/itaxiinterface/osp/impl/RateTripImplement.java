/****************************************************************************
 *
 *                            Copyright (c), 2015
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 *
 * $Log:  $
 * 
 * Created for Mobile Application Customer to Rate Trip Experience (PF-16547).
 * 
 */
package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;



import javax.sql.DataSource;

import com.dds.pathfinder.itaxiinterface.common.impl.BaseImplement;
import com.dds.pathfinder.itaxiinterface.scheduletask.JobInfo;
import com.dds.pathfinder.itaxiinterface.util.Debug2;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
//import com.dds.pathfinder.itaxiinterface.webservice.BaseRes;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.RateTripReq;
import com.dds.pathfinder.itaxiinterface.webservice.RateTripRes;

/**
 * @author ezhang
 *
 */
public class RateTripImplement extends OSPBaseImplement {
	
	private static Debug2 logger = Debug2.getLogger(JobInfo.class);

    private DataSource pfDataSource;
    
    
    
    private enum RateTripErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_SESSIONID(2),
		INVALID_JOB_ID(3),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private RateTripErrorCode(int c) {
			   code = c;
		}

		public int getCode() {
			   return code;
		}
	}
    
    public RateTripImplement(DataSource pfDataSource) {
		super();
		this.pfDataSource = pfDataSource;
	}


	public RateTripRes generateResponse(BaseReq request) {

		return generateRateTripResponse((RateTripReq)request);
	}
	
	private RateTripRes generateRateTripResponse(RateTripReq request){

		
		RateTripRes response = new RateTripRes();
		
		response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
    	response.setErrorCode(RateTripErrorCode.DEFAULT_ERROR.getCode());
    	
    	//validate system id
    	if(validateSystemId(pfDataSource, request) == false){
			   response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			   response.setErrorCode(RateTripErrorCode.NOT_AUTHENTICATED.getCode());
			   return response;
		}
    	
    	//validate job id
    	if(request.getTaxiRideID() <= 0){
    		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
			response.setErrorCode(RateTripErrorCode.INVALID_JOB_ID.getCode());
			return response;		
    	}
    	
    	//validate ratings, rating value has to be between 1 and 5
    	//set to null if value is not set or out of range.
    	//if(request.getGeneralRate() > 6 && request.getGeneralRate() > 1){
    	//	generalRate = request.getGeneralRate();
    	//}
    	
    	int result = 1; //failed
    	Connection con = null;
    	CallableStatement cs = null;
	
		
		try{
			
			if ((con = pfDataSource.getConnection()) == null) {
				logger.error("Failed to get db connection.");
				return response;
			}
			
		       	
	        cs = con.prepareCall ("{?=call jobpf.create_external_job_rating (?,?,?,?,?,?)}");
	        
	        logger.info ("Job Id: " + request.getTaxiRideID());
	        logger.info ("Car Rating: " + request.getCarRate());
	        logger.info ("Driver Rating: " + request.getDriverRate());
	        logger.info ("App Rating: " + request.getAppRate());
	        logger.info ("General Rating: " + request.getGeneralRate());
	        logger.info ("comments : " + request.getComments());
	        
	        cs.registerOutParameter (1, java.sql.Types.INTEGER);
		    cs.setLong (2, request.getTaxiRideID()); //job id
		    cs.setInt(3, request.getCarRate());
		    cs.setInt(4, request.getDriverRate());
		    cs.setInt (5, request.getAppRate());
		    cs.setInt (6, request.getGeneralRate());
		    cs.setString (7, request.getComments());
		    cs.execute ();
		    
		    result = cs.getInt (1);
		    if (result == 0) 
		    {
		    	response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
				response.setTaxiRideID(request.getTaxiRideID());
				response.setErrorCode(RateTripErrorCode.NO_ERROR.getCode());
		    	logger.info ("Job Rating create_external_job_rating(): " + request.getTaxiRideID() + " succeed");
			    
		    } 	
		    else if (result == 1) 
		    {
		    	logger.info ("Job Rating create_external_job_rating(): " + request.getTaxiRideID() + " failed");
			    
		    } 
		    
			
		}catch(SQLException se){
	       	logger.error("Job Rating request failed with exception", se);
	       	
       }finally{
    	   if (cs != null) try {cs.close ();} catch (SQLException ignore) {};
    	   if (con != null) try {con.close ();} catch (SQLException ignore) {};
       }
		
    	
		return response;
	}
	


}
