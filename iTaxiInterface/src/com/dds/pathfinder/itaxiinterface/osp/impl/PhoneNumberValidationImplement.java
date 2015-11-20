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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/PhoneNumberValidationImplement.java $
 * 
 * 2     2/12/11 10:01a Ezhang
 * C36130 added system id validation.
 * 
 * 1     9/20/10 2:14p Ezhang
 * OSP 2.0 new method.
 */
package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.PhoneNumberValidationReq;
import com.dds.pathfinder.itaxiinterface.webservice.PhoneNumberValidationRsp;

public class PhoneNumberValidationImplement extends OSPBaseImplement {
	
	private DataSource pfDataSource;
	public enum PhoneNoValidationErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_SESSIONID(2),
		INVALID_PHONENUM(3),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private PhoneNoValidationErrorCode(int c) {
			   code = c;
		}

		public int getCode() {
			   return code;
		}
	};
	
	public PhoneNumberValidationImplement(DataSource pfDataSource) {
		super();
		this.pfDataSource = pfDataSource;
	}
	public PhoneNumberValidationRsp generateResponse(BaseReq request) {
		return generatePhoneNoValidationResponse((PhoneNumberValidationReq) request);
	}

	private PhoneNumberValidationRsp generatePhoneNoValidationResponse(PhoneNumberValidationReq request){
		PhoneNumberValidationRsp response = new PhoneNumberValidationRsp();
		
		response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
		response.setErrorCode(PhoneNoValidationErrorCode.INVALID_PHONENUM.getCode());
		
		if(validateRequest(request, response)){
			response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
	    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
	    	response.setErrorCode(PhoneNoValidationErrorCode.NO_ERROR.getCode());
			return response;
		}
		
		return response;
	}
	
	/**
	 * Validate Phone Number validation request.
	 * 
	 * @param request
	 *            the request to validate
	 * @param response
	 *            the response to update if error
	 * @return true if valid, false otherwise
	 */
	private boolean validateRequest(PhoneNumberValidationReq request, PhoneNumberValidationRsp response) {
		
		String phoneNumber = null;
		
		
		if(request == null || response == null){
			return false;
		}
		//validate system id
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(PhoneNoValidationErrorCode.NOT_AUTHENTICATED.getCode());
		}
		//validation phone number
		if (request.getPhoneNum() == null || request.getPhoneNum().trim().length() == 0){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			response.setErrorCode(PhoneNoValidationErrorCode.INVALID_PHONENUM.getCode());
			return false;
		}
		phoneNumber = request.getPhoneNum();
		
		//Phone number accept any sting that contains a combination of digits as well as the symbols
		//"-"(hyphen), "+"(plus sign), and "."(period)
		//String expression = "^[-+.]*[0-9]*[-+.]*[0-9]*[-+.]*[0-9]+$";
		String expression = "^[0-9]*[-+.]*[0-9]*[-+.]*[0-9]+$";
		
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(phoneNumber);
		if(matcher.matches()){
			return true;
		}
		else{
			return false;
		}
		
	}
	
}
