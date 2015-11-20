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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/AccountPaymentImplement.java $
 * PF-16140 change error code DECLINED_ALEADY_PAID from 38 to 13.
 * 
 * 7     1/07/14 2:55p Sfoladian
 * MG-317- PF OSP is doing uppercase for account validation but not
 * account payment
 * 
 * 6     1/02/14 3:43p Mkan
 * allow 0.00 tip amount fix PF-15810
 * 
 * 5     11/29/13 1:52p Sfoladian
 * Added new error code for Account Payment: DECLINED_ACCT_JOB_NOT_READY
 * 
 * 4     11/28/13 11:03a Sfoladian
 * Uncommented the error code 38-DECLINED_ALREADY_PAID. 
 * 
 * 3     11/28/13 9:56a Sfoladian
 * PF-15748 - Mobile Booker Account Payment Support Part I
 * 
 * 2     10/30/13 4:29p Sfoladian
 * 
 * 1     10/28/13 12:49p Sfoladian
 * 
 * PF-15587 OSP Account Payment Support for Mobile Booker V2.0
 * 
 */
package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.itaxiinterface.common.impl.BaseImplement;
import com.dds.pathfinder.itaxiinterface.webservice.AccountPaymentRequest;
import com.dds.pathfinder.itaxiinterface.webservice.AccountPaymentResponse;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;


/**
 * @author sfoladian
 *
 */
public class AccountPaymentImplement extends OSPBaseImplement {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
    private DataSource pfDataSource;
    
    private float paymentAmt = 0;
    private int jobId;
    private float paymentTip = 0;
    private float paidAmount = 0; 
	private String paidAuthCode = null;
    
    public enum AccountPaymentErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_DEVICEID(2),
		NO_MATCH_TRIP(3),
		INVALID_TAXI_COMPID(4),
		INVALID_TAXI_RIDEID(8),
		INVALID_AMOUNT(9),
		INVALID_TIP(10),
		DECLINED_ALREADY_PAID(13),
		INVALID_ACCT_CODE(23),
		INACTIVE_ACCOUNT(36),	
		DECLINED_NOT_AUTHORIZED(39),
		DECLINED_ACCT_CASH(40),
		DECLINED_ACCT_CC(41),
		DECLINED_ACCT_PP(42),
		DECLINED_ACCT_EXPIRED(43),
		DECLINED_ACCT_CLOSED(44),
		DECLINED_ACCT_SUSPENDED(45),
		DECLINED_ACCT_NO_TRIPS(46),
		DECLINED_ACCT_OVERLIMIT(47),
		DECLINED_ACCT_DISABLED(48),
		DECLINED_ACCT_NO_FUNDS(49),
		DECLINED_ACCT_JOB_NOT_READY(50),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private AccountPaymentErrorCode(int c) {
			   code = c;
		}

		public int getCode() {
			   return code;
		}
	};
	
    
    public AccountPaymentImplement(DataSource pfDataSource) {
		super();
		this.pfDataSource = pfDataSource;
	}
    
    
    public AccountPaymentResponse generateResponse(BaseReq request) {
		return generateAccountPaymentResponse((AccountPaymentRequest) request);
	
	}
	
	private AccountPaymentResponse generateAccountPaymentResponse(AccountPaymentRequest request){
		
		AccountPaymentResponse response = getDefaultResponse(request);
		
		if(!validateRequest(request, response)){
			   return response;
		}
    	int resultUpdatePayment = updateAccountPayment(request, response);
    	setAccountPaymentResponse(resultUpdatePayment,response);
    	
		return response;
	
	}
	
	private AccountPaymentResponse getDefaultResponse(AccountPaymentRequest request){
		
		AccountPaymentResponse response = new AccountPaymentResponse();
		   
		response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
		response.setErrorCode(AccountPaymentErrorCode.DEFAULT_ERROR.getCode());
		
		return response;
	}
	
	private boolean validateRequest(AccountPaymentRequest request, AccountPaymentResponse response) {
		
		if (request == null || response == null) {
			return false;
		}
		//validate system id	
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(AccountPaymentErrorCode.NOT_AUTHENTICATED.getCode());
			return false;
		}
		
		//validate taxi company id
		if(request.getTaxiCoID() == null ||request.getTaxiCoID() <= 0 || request.getTaxiCoID()> 999999999){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
			response.setErrorCode(AccountPaymentErrorCode.INVALID_TAXI_COMPID.getCode());
			return false;
		}
		//validate deviceID(Mobile booker hardware id)
		if (request.getDeviceID() == null || request.getDeviceID().trim().length() == 0){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_DEVICE_ID);
			response.setErrorCode(AccountPaymentErrorCode.INVALID_DEVICEID.getCode());
			return false;
		}
		
		//validate taxiRideId
		String taxiRideId = request.getTaxiRideID();
		if( !isPositiveNumber(taxiRideId)) {
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_JOB_ID);
			response.setErrorCode(AccountPaymentErrorCode.INVALID_TAXI_RIDEID.getCode());
			return false;
		}else {
			jobId = Integer.parseInt(taxiRideId);
		}
		
		//validate AccountId
		if (request.getAccountCode() == null || request.getAccountCode().trim().length() == 0 || request.getAccountCode().trim().length() >20){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_ACCOUNT);
			response.setErrorCode(AccountPaymentErrorCode.INVALID_ACCT_CODE.getCode());
			return false;
		}
		
		//validate amount, it is required
		String amtStr = request.getAmount();
		if(!isValidNumber(amtStr) || amtStr.length() > 7) {
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_PAYMNT_AMOUNT);
			response.setErrorCode(AccountPaymentErrorCode.INVALID_AMOUNT.getCode());
			return false;
		}else {
			paymentAmt = Float.parseFloat(amtStr);
			if(paymentAmt <= 0){
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_PAYMNT_AMOUNT);
				response.setErrorCode(AccountPaymentErrorCode.INVALID_AMOUNT.getCode());
				return false;
			}
		}
		
		//validate tip 
		String tipStr = request.getTip();
		if (tipStr != null && tipStr.length() > 0){
			if(!isValidNumber(tipStr) || tipStr.length() > 7) {
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_PAYMNT_TIP);
				response.setErrorCode(AccountPaymentErrorCode.INVALID_TIP.getCode());
				return false;
			}else {
				paymentTip = Float.parseFloat(tipStr);				
				if(paymentTip < 0 ){
					response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_PAYMNT_TIP);
					response.setErrorCode(AccountPaymentErrorCode.INVALID_TIP.getCode());
					return false;
				}
			}
		}
		
		return true;
	}
	
	private int updateAccountPayment(AccountPaymentRequest request, AccountPaymentResponse response) {
		
		String result = null; //failed
		int errorCode = 99; //Default Error
		
	 	Connection conn = null;
	    CallableStatement cs = null;
	    
	    int taxiCoID = 0;  
	    String accountCode = "";
	    String userName = getUserName(request, null);
		try
	    {
	        conn = pfDataSource.getConnection ();
	        taxiCoID = request.getTaxiCoID();  
	        accountCode = request.getAccountCode();
	        accountCode = accountCode!=null ? accountCode.toUpperCase(): accountCode;
	        
	        cs = conn.prepareCall ("{?=call aqueues.process_mb_ac_payment (?,?,?,?,?,?,?,?,?)}");
	   
	        logger.info ("Taxi Company Id: " + taxiCoID);
	        logger.info ("Job Id: " + jobId);
	        logger.info ("Account Card Number: " + accountCode);
	        logger.info ("Payment Amount(Total:Amount+Tip): " + paymentAmt);
	        logger.info ("Payment Tip: " + paymentTip);
	        
	   
	        cs.registerOutParameter (1, java.sql.Types.VARCHAR);
		    cs.setInt (2, taxiCoID); 
		    cs.setInt(3, jobId);//job id
		    cs.setString(4 ,accountCode);//acct_card_nr
		    cs.setFloat(5, paymentAmt);//Total= amount+tip
		    cs.setFloat (6, paymentTip);
		    cs.registerOutParameter (7, java.sql.Types.INTEGER);
		    cs.registerOutParameter (8, java.sql.Types.FLOAT);
		    cs.registerOutParameter (9, java.sql.Types.VARCHAR);
		    cs.setString(10, userName);
		    
		    cs.execute ();
	       
		    result = cs.getString (1);
		    errorCode= cs.getInt (7);
		    if(errorCode == AccountPaymentErrorCode.DECLINED_ALREADY_PAID.getCode()){
		    	paidAmount = cs.getFloat(8);
		    	paidAuthCode = cs.getString(9);
		    	logger.info ("Account Payment.updateAccountPayment() AMt: " + paidAmount + " auth "+ paidAuthCode);
		    }
		    
		    logger.info ("Account Payment.updateAccountPayment() Result: " + result);
		    logger.info ("Account Payment.updateAccountPayment() errorCode: " + errorCode);
		    
 	    }
	    catch(SQLException se)
	    {
      	    logger.error ("Account Payment updateAccountPayment() failed with exception", se);
      	   
        }
	    finally
	    {
            if (cs != null) try {cs.close ();} catch (SQLException ignore) {};
    	    if (conn != null) try {conn.close ();} catch (SQLException ignore) {};
        }
	    return errorCode;
	}
	
	private void setAccountPaymentResponse(int updatePaymentRes,
			AccountPaymentResponse response) {
		if(updatePaymentRes == 0){
			response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
	    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
			response.setErrorCode(AccountPaymentErrorCode.NO_ERROR.getCode());
		}else{
			switch(updatePaymentRes){
			case 3://No Match JobId(TripId)
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_JOB_ID);
				response.setErrorCode(AccountPaymentErrorCode.NO_MATCH_TRIP.getCode());
				break;		
			case 4://Invalid Taxi_co_id
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
				response.setErrorCode(AccountPaymentErrorCode.INVALID_TAXI_COMPID.getCode());
				break;
			case 23://Invalid_account
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_ACCOUNT);
				response.setErrorCode(AccountPaymentErrorCode.INVALID_ACCT_CODE.getCode());
				break;
			case 36://Inactive_account
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_ACCOUNT);
				response.setErrorCode(AccountPaymentErrorCode.INACTIVE_ACCOUNT.getCode());
				break;
			case 13://declined_already_paid
				if( paidAuthCode != null && paidAuthCode.length() > 0){
					//response.setAuthCode(paidAuthCode);
					response.setErrorMessage(GenErrMsgRes.ERROR_CODE_DUPLICATE_PAYMNT + " CC amt " + paidAmount + " auth# " + paidAuthCode);
			
				}else{
					response.setErrorMessage(GenErrMsgRes.ERROR_CODE_DUPLICATE_PAYMNT + " AC amt " + paidAmount);
				
				}		
				response.setErrorCode(AccountPaymentErrorCode.DECLINED_ALREADY_PAID.getCode());
				break;
			case 39://declined_not_autorized
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTH_PAYMNT);
				response.setErrorCode(AccountPaymentErrorCode.DECLINED_NOT_AUTHORIZED.getCode());
				break;
			case 40://declined_ACCT_CASH
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_CASH_ACCOUNTT);
				response.setErrorCode(AccountPaymentErrorCode.DECLINED_ACCT_CASH.getCode());
				break;
			case 41://declined_ACCT_CC
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_CC_ACCOUNT);
				response.setErrorCode(AccountPaymentErrorCode.DECLINED_ACCT_CC.getCode());
				break;
			case 42://declined_ACCT_PP
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_PREPAID_ACCOUNT);
				response.setErrorCode(AccountPaymentErrorCode.DECLINED_ACCT_PP.getCode());
				break;
			case 43://declined_ACCT_EXPIRED
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_ACCOUNT_EXPIRED);
				response.setErrorCode(AccountPaymentErrorCode.DECLINED_ACCT_EXPIRED.getCode());
				break;
			case 44://declined_ACCT_CLOSED
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_ACCOUNT_CLOSED);
				response.setErrorCode(AccountPaymentErrorCode.DECLINED_ACCT_CLOSED.getCode());
				break;
			case 45://declined_ACCT_SUSPENDED
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_ACCOUNT_SUSPENDED);
				response.setErrorCode(AccountPaymentErrorCode.DECLINED_ACCT_SUSPENDED.getCode());
				break;
			case 46://declined_NO_TRIPS
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_EXCEED_MAX_TRIPS);
				response.setErrorCode(AccountPaymentErrorCode.DECLINED_ACCT_NO_TRIPS.getCode());
				break;
			case 47://declined_OVERLIMIT
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_OVER_LIMIT);
				response.setErrorCode(AccountPaymentErrorCode.DECLINED_ACCT_OVERLIMIT.getCode());
				break;
			case 48://declined_DISABLED
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_DISABLED_ACCOUNT);
				response.setErrorCode(AccountPaymentErrorCode.DECLINED_ACCT_DISABLED.getCode());
				break;
			case 49://declined_NO_FUNDS
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NO_FUND);
				response.setErrorCode(AccountPaymentErrorCode.DECLINED_ACCT_NO_FUNDS.getCode());
				break;
			case 50://declined_JOB_NOT_READY
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_JOB_NOT_READY);
				response.setErrorCode(AccountPaymentErrorCode.DECLINED_ACCT_JOB_NOT_READY.getCode());
				break;
			default:
				break;
			}
		}
		
	}
	/**
	 * Returns true if s represents a number and false otherwise.
	 * 
	 * @param s
	 * @return
	 */
	private boolean isValidNumber(final String s) {
		boolean isNumber = true;
		try {
			Float.parseFloat(s);
		} catch (NumberFormatException ex) {
			isNumber = false;
		}
		return isNumber;
	}
	
	private boolean isPositiveNumber(final String s) {
		boolean isNumber = true;
		try {
			int i = Integer.parseInt(s);
			isNumber = i >= 0;
		} catch (NumberFormatException ex) {
			isNumber = false;
		}
		return isNumber;
	}

	//public String getUserName(AccountPaymentRequest request){
	//	return getUserName(request, null);
	//}
}
