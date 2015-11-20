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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/CancelJobImplement.java $
 * 
 * PF-16104, June 5th, 2014, UDI should be able to cancel any job too.
 * 
 * 8     12/20/13 2:13p Dchen
 * PF-15469, add origin code for cancelling job as well.
 * 
 * 7     12/19/13 3:32p Dchen
 * PF-15469, add origin code for cancelling job as well.
 * 
 * 6     10/24/12 5:45p Ezhang
 * 
 * 5     5/25/12 11:49a Ezhang
 * PF-14445 added isRightJobToCancel() to only allow GFC to cancel GFC generated jobs.
 * 
 * 4     2/16/12 11:06a Yyin
 * C36567 - Fixed the problem that request_status is 1 when the trip is
 * cancelled successfully.
 * 
 * 3     2/12/11 10:00a Ezhang
 * C36130 Added SystemId validation
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
import java.sql.Types;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.itaxiinterface.common.impl.BaseImplement;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.CancelJobReq;
import com.dds.pathfinder.itaxiinterface.webservice.CancelJobRes;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;

public class CancelJobImplement extends OSPBaseImplement {

	private Logger logger = LogManager.getLogger(this.getClass());

    private DataSource pfDataSource;
    
    
    
	public CancelJobImplement(DataSource pfDataSource) {
		super();
		this.pfDataSource = pfDataSource;
	}

	public CancelJobRes generateResponse(BaseReq request) {
		return generateCancelJobResponse((CancelJobReq)request);
	}
	
	private CancelJobRes generateCancelJobResponse(CancelJobReq request){
		   CancelJobRes response = new CancelJobRes();
		   long jobID = request.getTaxiRideID();
		   if(jobID <= 0){
			   response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_JOB_ID);
			   return response;
		   }
		   //C36130 validate system id
		   if(validateSystemId(pfDataSource, request) == false){
			   response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
				return response;
		   }
		  
		   	   
		   Connection con = null;
		   CallableStatement cs = null;
		  
		   //PF-14445 GFC cancel job can only cancel GFC job
		   //webbooker has it's own check before calling cancel job
		  //if there is new system added, add checking like below to prevent one system delete another system generated job.
		   if(!isRightJobToCancel(request)) {
			   response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_JOB_ID);
			   return response;
		   }
		   
		   try{
	       	con = pfDataSource.getConnection();
//	       	cs = con.prepareCall("{ call despatch.cancel(?,?)}");
	       	cs = con.prepareCall("{ call despatch.cancel(?,?,?,?,?)}");
	       	cs.setLong(1, request.getTaxiRideID());
	       	cs.setString(2, "Cancel Job by OSP");       	   
       	    cs.setNull(3, Types.VARCHAR);		//log_classes.log_type%TYPE DEFAULT NULL,
       	    cs.setString(4, "N");  				//p_is_redesp IN VARCHAR2 DEFAULT 'N',
       	    cs.setString(5, getUserName(request, request.getSessionID()));
	       	cs.execute();
	       	
	       
	       	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
		    response.setTaxiRideID(request.getTaxiRideID());
		    response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
			
	       }catch(SQLException se){
		       	logger.error("job cancellation failed with exception", se);
		       	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_JOB_ID);
		       	
	       }finally{
	       	if(cs != null) try{cs.close();}catch(SQLException ignore){};
	       	if(con != null) try{con.close();}catch(SQLException ignore){};
	       }
	       return response;
	}
	
	//PF-14445 GFC cancel job can only cancel GFC job
	//if there is new system added, add checking like below to prevent one system delete another system generated job.
	private boolean isRightJobToCancel(CancelJobReq request) {
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
					//PF-16104 UDI should be able to cancel any job too.
					else if(systemId == ExternalSystemId.SYSTEM_ID_WEB_BOOKER.getSystemId() || systemId == ExternalSystemId.SYSTEM_ID_UDI_USER.getSystemId() || isAccessableInternalJobs(systemId)) {
						res = true; //webbooker cancel webbooker job
					}
					
					
		       }catch(SQLException se){
			       	logger.error("isRightJobToCancel() failed with exception", se);
			       	
		       }finally{
		    	    if (result != null)try {result.close();} catch (SQLException ignore) {};
		    	    if (stmt != null)try {stmt.close();} catch (SQLException ignore) {};
		       		if(con != null) try{con.close();}catch(SQLException ignore){};
		       		
		       }
			}
			
		    return res;
		   
	}
	

	
}
