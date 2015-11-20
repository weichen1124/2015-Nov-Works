/****************************************************************************
 *
 *                            Copyright (c), 2013
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/SendDriverMessageImplement.java $
 * PF-16208 remove the parameter C_MB_SND_MSG_DRV check from server
 * this parameter only used by mobile booker.
 * 
 * 6     3/12/14 3:32p Dchen
 * Added osp_users table instead of hard coding.
 * 
 * 5     1/29/14 3:48p Ezhang
 * PF-15809 used cached company parameter
 * 
 * 4     11/21/13 4:58p Ezhang
 * PF-15612 modified send message to driver to use structure msg instead of mail message.
 * 
 * 3     11/12/13 2:52p Ezhang
 * PF-15612 only allow when the job status is between accepted and arrived state
 * 
 * 2     11/05/13 1:16p Ezhang
 * 
 * PF-15612 Add send Message from Mobile Booker to Driver vie trip id.
 * 
 * 1     10/30/13 4:26p Sfoladian
 * 
 * 
 * PF-15612 Send Message to Driver,  Mobile Booker V2.0
 * 
 */
package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.osp.impl.TokenPaymentImplement.CarStatusCode;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.SendDriverMessageRequest;
import com.dds.pathfinder.itaxiinterface.webservice.SendDriverMessageResponse;



/**
 * @author sfoladian
 *
 */
public class SendDriverMessageImplement extends OSPBaseImplement {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	
    private DataSource pfDataSource;
    
 
	private LoadDispatchParametersLocal cachedParam;
    /* those type is not supported for now
    private static final String DESTINATION_TYPE_CAR = "1";
    private static final String DESTINATION_TYPE_DRIVER = "2";
    private static final String DESTINATION_TYPE_ALL_CARS_IN_COM = "3";
    private static final String DESTINATION_TYPE_ALL_CARS_IN_ZONE = "4";
    private static final String DESTINATION_TYPE_ALL_CARS_IN_AREA = "5";
    private static final String DESTINATION_TYPE_ALL_CARS_DRIVERS_WITH_ATTR = "6";
    private static final String DESTINATION_TYPE_ALL_CARS_IN_ZONES = "7";
    private static final String DESTINATION_TYPE_ALL_CARS = "8";
    */
    private static final String DESTINATION_TYPE_JOB = "9";
    public final static String MSG_PRIORITY_NORMAL = "N";
    public final static String MSG_PRIORITY_HIGH = "H";
    public final static String MSG_PRIORITY_URGENT = "U";
    public final static String JOB_STATUS_COMPLETE = "C";
    public final static int 	MAX_MESSAGE_LENGTH = 180;
    
    
    private String driverName;
	private String vehicleNumber;
	private String jobStatus;
	private String vehicleStatus;
	private int jobId;
	private int taxi_co_id;
	private int mdt_id;
	private String priority;
	private String messageBody;
	
    
    
    public enum SendDriverMessageErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_MSG(2),
		INVALID_DATE_FORMAT(3),
		INVALID_DESTINATION(4),
		TRIP_ID_NOT_ACTIVE(5),
		INVALID_CAR_STATUS(6),
		INVALID_PRIORITY(7),
		VEHICLE_NOT_FOUND(8),
		VEHICLE_NOT_SIGNED_ON(9),
		INVALID_DESTINATION_TYPE_ID(10),
		FEATURE_DISABLED(11),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private SendDriverMessageErrorCode(int c) {
			   code = c;
		}

		public int getCode() {
			   return code;
		}
	};
	
    
    public SendDriverMessageImplement(DataSource pfDataSource, LoadDispatchParametersLocal cachedParam) {
		super();
		this.pfDataSource = pfDataSource;
		this.cachedParam = cachedParam;
		setCachedParam(cachedParam);
	}
    
    public SendDriverMessageResponse generateResponse(BaseReq request) {
    	
    	return generateSendDriverMsgResponse((SendDriverMessageRequest) request);
	}
    
    public SendDriverMessageResponse generateSendDriverMsgResponse(SendDriverMessageRequest request) {
    	
    	SendDriverMessageResponse response = new SendDriverMessageResponse();
    	
    	response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
    	response.setErrorCode(SendDriverMessageErrorCode.DEFAULT_ERROR.getCode());
    	
    	if(!validateRequest(request, response)) {
    		return response;
    	}
    	
    	// Send request to DB for processing
	    Connection con = null;
	    CallableStatement cs = null;
	    int result = -1;
	    
	    try
	    {
	        con = pfDataSource.getConnection ();
	        cs = con.prepareCall ("{?=call mail.mb_send_driver_message (?,?,?,?)}");
	   
	        logger.info ("message body: " + messageBody);
	        logger.info ("message priority: " + priority);
	        logger.info ("delivery dtm: " + request.getDeliveryTime());
	        logger.info ("mdt id: " + mdt_id);
	       
	   
	        cs.registerOutParameter (1, java.sql.Types.INTEGER);
		    cs.setString (2, messageBody);
		    cs.setString (3, priority);
		    cs.setString (4, "");
		    cs.setInt (5, mdt_id);
		    
		    cs.execute ();
		    logger.info ("Driver message Request Result: " + cs.getInt (1));
		    result = cs.getInt (1);
 	    }
	    catch(SQLException se)
	    {
      	    logger.error ("Driver message request failed with exception", se);
      	    
        }
	    finally
	    {
            if (cs != null) try {cs.close ();} catch (SQLException ignore) {};
    	    if (con != null) try {con.close ();} catch (SQLException ignore) {};
        }
    	
    	logger.info("SendDriverMessageImplement " +response);
		
    	ProcessMessageResult(result, response);
		return response;
    }

    /**
	 * Validate Send Driver Message request, check all required fields
	 * 
	 * @param request
	 *            the request to validate
	 * @param response
	 *            the response to update if error
	 * @return true if valid, false otherwise
	 */
	private boolean validateRequest(SendDriverMessageRequest request, SendDriverMessageResponse response) {
		
		if (request == null || response == null) {
			return false;
		}
		
		
		
		//validate system id	
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(SendDriverMessageErrorCode.NOT_AUTHENTICATED.getCode());
			return false;
		}
		
		//validate message		
		messageBody = request.getMessage();
    	if(messageBody == null || messageBody.trim().length() <= 0) {
    		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
    		response.setErrorCode(SendDriverMessageErrorCode.INVALID_MSG.getCode());
    		return false;
    	}else if(messageBody.length() > MAX_MESSAGE_LENGTH) {
    		//truncate the message to the max length.
    		messageBody = messageBody.substring(0, MAX_MESSAGE_LENGTH);
    		
    	}
    	//validate destinationTypeID
    	String destinationTypeId = request.getDestinationTypeID();
    
    	if(destinationTypeId == null || destinationTypeId.trim().length() <=0 || !destinationTypeId.equalsIgnoreCase(DESTINATION_TYPE_JOB) ){
    		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
    		response.setErrorCode(SendDriverMessageErrorCode.INVALID_DESTINATION_TYPE_ID.getCode());
    		return false;
    	}
    	//validate destination if it's for a job
    	String destination = request.getDestination();
    	if(!validateJobId(destination)) {
    		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
    		response.setErrorCode(SendDriverMessageErrorCode.INVALID_DESTINATION.getCode());
    		return false;
    	}else {
    		jobId = Integer.parseInt(destination);
    	}
    	

		//validate priority
    	priority = request.getPriority();
    	if(priority == null || priority.trim().length() <= 0 ) {
    		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
    		response.setErrorCode(SendDriverMessageErrorCode.INVALID_PRIORITY.getCode());
    		return false;
    	}else if(!priority.equalsIgnoreCase(MSG_PRIORITY_NORMAL) && !priority.equalsIgnoreCase(MSG_PRIORITY_HIGH)) {
    		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
    		response.setErrorCode(SendDriverMessageErrorCode.INVALID_PRIORITY.getCode());
    		return false;
    	}else if(priority.equalsIgnoreCase(MSG_PRIORITY_HIGH)){
    		priority = MSG_PRIORITY_URGENT;
    	}
    	
    	//get trip Info
		String systemRef = getSystemReference(request.getSystemID());
		getJobInfo(jobId, systemRef); 
		
		//check job status
		if(driverName == null || vehicleNumber == null || JOB_STATUS_COMPLETE.equalsIgnoreCase(jobStatus)) {
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(SendDriverMessageErrorCode.TRIP_ID_NOT_ACTIVE.getCode());
			return false;
		}
		//check vehicle status must be between accepted and arrived state
		if(vehicleStatus == null || (!vehicleStatus.equals(CarStatusCode.IN_TRANSIT.getStatus()) && !vehicleStatus.equals(CarStatusCode.ARRIVED.getStatus()) ))
		{
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(SendDriverMessageErrorCode.INVALID_CAR_STATUS.getCode());
			return false;
		}
		
		/* PF-16208
		//check parameter C_MB_SND_MSG_DRV whether the company support this feature or not.
		String sendMsgToDriver =  cachedParam.getCompanyParameterValue(taxi_co_id, CompanyDefaultValues.COMP_PARAMETER_C_MB_SND_MSG_DRV);
    	logger.info("sendMessageToDriver " + sendMsgToDriver);
		if(sendMsgToDriver == null || !sendMsgToDriver.equalsIgnoreCase("Y")) {
    		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(SendDriverMessageErrorCode.FEATURE_DISABLED.getCode());
    		return false;
    	}
		*/
		
    	return true;
	}
	
	private boolean validateJobId(String jobString) {
		
		try {
			if (jobString == null || jobString.trim().length() == 0){
				return false;
			}
			else {
				//jobId must be all digits			
				Integer.parseInt(jobString);
				
			}
		}catch(Exception e) {
			logger.error("invalid job Id");
	    	e.printStackTrace();	
			return false;
		}
		return true;
		
	}
	private void getJobInfo(int jobId, String systemRef) {
		
		//initialize values
		vehicleNumber = null;
		driverName = null;
		jobStatus = null;
		taxi_co_id = 0;
		vehicleStatus = null;
		
		StringBuffer query = new StringBuffer();
		
		query.append("select jobs.taxi_co_id, mdt_id, CALLSIGN, DRIVER_NAME, current_job_stage, vs.vehicle_status");
		query.append(" from jobs, job_references, vehicles, drivers, vehicle_states vs");
		query.append(" where jobs.job_id = ? ");
		query.append(" and vs.current_job_id = jobs.job_id");
		query.append(" and jobs.job_id = job_references.job_id and job_references.reference_type = ? ");
		query.append(" and jobs.vehicle_id = vehicles.vehicle_id(+)");
		query.append(" and jobs.driver_id = drivers.driver_id(+)");
		
		//logger.info("getJobInfo query " + query); 
		
		PreparedStatement stmt = null;
		ResultSet rset = null;

		Connection dbConnection = null;
		CallableStatement cs = null;
		
		try {
			if ((dbConnection = pfDataSource.getConnection()) == null) {
				logger.error("Failed to get db connection.");
				return;
			}
			stmt = dbConnection.prepareStatement(query.toString());
			stmt.setInt(1, jobId);
			stmt.setString(2, systemRef);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				taxi_co_id = rset.getInt("taxi_co_id");
				mdt_id = rset.getInt("mdt_id");
				vehicleNumber = rset.getString("CALLSIGN");
				driverName = rset.getString("DRIVER_NAME");
				jobStatus = rset.getString("current_job_stage");
				vehicleStatus = rset.getString("vehicle_status");
				
		
			}
		
		}catch (SQLException e) {
			logger.error("Exception in getJobInfo(jobID " + jobId + ")", e);
		} finally {
			try {
				if (cs != null)
					cs.close();
			} catch (SQLException ignore) {};
			try {
				if (rset != null)
					rset.close();
			} catch (SQLException ignore) {};
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException ignore) {};
			try {
				if (dbConnection != null)
					dbConnection.close();
			} catch (SQLException ignore) {};
		}
	}
	
	private void ProcessMessageResult(int result, SendDriverMessageResponse response)
    {
    	
    	
    	if (result == SendDriverMessageErrorCode.NO_ERROR.getCode()){
    		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
		    response.setErrorMessage(GenErrMsgRes.ERROR_TEXT_EMPTY);
		    response.setErrorCode(SendDriverMessageErrorCode.NO_ERROR.getCode());
	    }
	    else if (result == SendDriverMessageErrorCode.INVALID_DATE_FORMAT.getCode()){
	    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
	    	response.setErrorCode(SendDriverMessageErrorCode.INVALID_DATE_FORMAT.getCode());
	 
	    }
		else if (result == SendDriverMessageErrorCode.VEHICLE_NOT_FOUND.getCode()){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(SendDriverMessageErrorCode.VEHICLE_NOT_FOUND.getCode());
		}
		else if (result == SendDriverMessageErrorCode.VEHICLE_NOT_SIGNED_ON.getCode()){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(SendDriverMessageErrorCode.VEHICLE_NOT_SIGNED_ON.getCode());
		}			
	    
    	
    }
}
