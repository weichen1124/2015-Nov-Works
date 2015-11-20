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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/PickupTimeValidationImplement.java $
 * 
 * 2     2/12/11 10:01a Ezhang
 * C36130 Added system id validation.
 * 
 * 1     9/20/10 2:16p Ezhang
 * OSP 2.0 new method
 */
package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.osp.impl.CarValidationImplement.CarValidationErrorCode;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.PickupTimeValidationReq;
import com.dds.pathfinder.itaxiinterface.webservice.PickupTimeValidationRsp;

public class PickupTimeValidationImplement extends OSPBaseImplement {

	private Logger logger = LogManager.getLogger(this.getClass());
	private DataSource pfDataSource;
	
	public enum PickupTimeValidationErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_SESSIONID(2),
		INVALID_COMPID(3),
		INVALID_PICKUP_TIME(4),
		INVALID_ATTRIBUTE(5),
		PICKUP_TIME_TOOFAR(6),
		PICKUP_TIME_TOOSOON(7),
		INVALID_NUM_TAXIS(8),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private PickupTimeValidationErrorCode(int c) {
			   code = c;
		}

		public int getCode() {
			   return code;
		}
	};
	
	public PickupTimeValidationImplement(DataSource pfDataSource) {
		super();
		this.pfDataSource = pfDataSource;
	}
	
	public PickupTimeValidationRsp generateResponse(BaseReq request) {
		return generatePickupTimeValidationResponse((PickupTimeValidationReq) request);
	}

	private PickupTimeValidationRsp generatePickupTimeValidationResponse(PickupTimeValidationReq request){
		PickupTimeValidationRsp response = getDefaultResponse(request);
		
		if(!validateRequest(request, response)){
			   return response;
		}
		
		if(isValidPickupTime(request, response)){
			//get max number of Taxi allowed Pathfinder does not have feature like TaxiTrack overbooking check.
			//int maxNumTaxis = CompanyDefaultValues.getSystemParameterIntValue(pfDataSource, CompanyDefaultValues.SYSTEM_PARAMETER_MAX_TAXIS);
			response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
	    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
	    	response.setErrorCode(CarValidationErrorCode.NO_ERROR.getCode());
			//response.setNofTaxisAllowed(maxNumTaxis);
			return response;
		}
		return response;
		
	}
	
	private PickupTimeValidationRsp getDefaultResponse(PickupTimeValidationReq request){
		
		PickupTimeValidationRsp response = new PickupTimeValidationRsp();
		   
		response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
		response.setErrorCode(BookJobErrorCode.DEFAULT_ERROR.getCode());
		   
		return response;
	}
	
	/**
	 * Validate PickupTime validation request.
	 * 
	 * @param request
	 *            the request to validate
	 * @param response
	 *            the response to update if error
	 * @return true if valid, false otherwise
	 */
	private boolean validateRequest(PickupTimeValidationReq request, PickupTimeValidationRsp response) {
		
		if (request == null || response == null) {
			return false;
		}
		//validate system id
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(PickupTimeValidationErrorCode.NOT_AUTHENTICATED.getCode());
		}
		//validate pickup time string
		if (request.getPickupTime() == null || request.getPickupTime().trim().length() == 0){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(PickupTimeValidationErrorCode.INVALID_PICKUP_TIME.getCode());
			return false;
		}
		
		//validate taxi company id
		if(request.getTaxiCoID() == null ||request.getTaxiCoID() <= 0 || request.getTaxiCoID()> 999999999){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
			response.setErrorCode(PickupTimeValidationErrorCode.INVALID_COMPID.getCode());
			return false;
		}
		
		return true;
	}
	
	//validate pickup time to check whether is too soon or too far
	//also return the max number of taxis allowed.
	private boolean isValidPickupTime(PickupTimeValidationReq request, PickupTimeValidationRsp response){
		
		Calendar sysCalendar = Calendar.getInstance();
		
		//We assume it should take less than 5 minutes for the calltaker to book a job!
        sysCalendar.add(Calendar.MINUTE, - 5);

        sysCalendar.clear(Calendar.SECOND);

        Date sysDateComp = sysCalendar.getTime();
        
        Date pickupDTM = Utilities.convertOSPDefaultDate(request.getPickupTime());
        
        if (sysDateComp.compareTo(pickupDTM) > 0 ){
        	response.setErrorCode(PickupTimeValidationErrorCode.PICKUP_TIME_TOOSOON.getCode());	
        	return false;
        }
        
        int preBookInDays = CompanyDefaultValues.getCompanyParameterIntValue(pfDataSource, request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_PRE_BOOK_IN_DAYS);
        sysCalendar.add(Calendar.DATE, preBookInDays);
        sysDateComp = sysCalendar.getTime();
        if  (sysDateComp.compareTo(pickupDTM)< 0) {	
        	response.setErrorCode(PickupTimeValidationErrorCode.PICKUP_TIME_TOOFAR.getCode());	
        	return false;
        }
		
		return true;
		
	}
	
	
}
