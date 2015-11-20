/****************************************************************************
 *
 *		   		    Copyright (c), 2014
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/tss/impl/TSSCancelVehImplement.java $
 * 
 *  PF-16183, 08/29/14, DChen,added TSS require service.
 * 
 * 08/25/14, DChen, create TSS project. 
 * 
 */

package com.dds.pathfinder.itaxiinterface.tss.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.order.ejb.Order;
import com.dds.pathfinder.itaxiinterface.tss.TSSCancelVehReq;
import com.dds.pathfinder.itaxiinterface.tss.TSSCancelVehRes;


public class TSSCancelVehImplement extends TSSBaseImplement {
	private Logger logger = LogManager.getLogger(this.getClass());
	
    private DataSource pfDataSource;
    private Order order;
    
	public TSSCancelVehImplement(DataSource pfDataSource, Order orderLocal) {
		super();
		this.pfDataSource = pfDataSource;
		this.order = orderLocal;
	}

	public TSSCancelVehRes generateTSSResponse(TSSCancelVehReq request){
		TSSCancelVehRes response = new TSSCancelVehRes(TSSErrorCode.TSS_FAILURE);
		if(!validateRequest(request, response)){
			   return response;
		}
		
		long jobID = order.findJobIDByExternalID(request.getTssID(), ExternalSystemId.SYSTEM_ID_TSS_RIDER.getReference());
		if(jobID <= 0){
			response.setErrorCode(TSSErrorCode.TSS_NOT_FINDPF_JOB_ID);
			return response;
		}  
		   	   
		Connection con = null;
		CallableStatement cs = null;
		     
		try{
	       	con = pfDataSource.getConnection();
	       	cs = con.prepareCall("{ call despatch.cancel(?,?,?,?,?)}");
	       	cs.setLong(1, jobID);
	       	cs.setString(2, "Cancel Job by OSP TSS");       	   
    	    cs.setNull(3, Types.VARCHAR);		//log_classes.log_type%TYPE DEFAULT NULL,
    	    cs.setString(4, "N");  				//p_is_redesp IN VARCHAR2 DEFAULT 'N',
    	    cs.setString(5, ExternalSystemId.SYSTEM_ID_TSS_RIDER.getLogonCode());
	       	cs.execute();
	       
	       	response.setErrorCode(TSSErrorCode.TSS_SUCCESS);
			
		}catch(SQLException se){
		       	logger.error("job cancellation failed with exception", se);
		       	response.setErrorCode(TSSErrorCode.TSS_CANCEL_FAILED);
		}finally{
	       	if(cs != null) try{cs.close();}catch(SQLException ignore){};
	       	if(con != null) try{con.close();}catch(SQLException ignore){};
		}
	       
		return response;
	}
	
	private boolean validateRequest(TSSCancelVehReq request, TSSCancelVehRes response){
		if(request == null) return false;
		if(request.getTaxiCoID() <= 0) {
			response.setErrorCode(TSSErrorCode.TSS_INVALID_COMPANY);
			return false;
		}
		if(request.getTssID() < 0) return false;
		return true;
	}

}
