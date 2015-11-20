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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/AccountValidationImplement.java $
 * 
 * 8     3/12/14 3:32p Dchen
 * Added osp_users table instead of hard coding.
 * 
 * 7     1/29/14 3:48p Ezhang
 *  * PF-15809 use cached parameter value
 * 
 * 6     1/07/14 10:48a Sfoladian
 * PF-15808- Added the validation on the Account Status (Closed/Suspended)
 * Corrected  the typo on ERROR_CODE_ACC_NOT_ACTIVE
 *  
 * 
 * 5     12/31/13 1:48p Sfoladian
 * MG-304- account expiry and effective dates should also be validated
 * during registration 
 * 
 * 4     11/21/13 11:28a Sfoladian
 * fixed the issue with Invalid companyId validation
 * 
 * 3     11/20/13 11:33a Sfoladian
 * fixed the issue with validation on company Id
 * 
 * 2     10/30/13 4:12p Sfoladian
 * fixed the checkPassword when UseAccount Password Parameter=1
 * 
 * 1     10/28/13 12:10p Sfoladian
 * 
 * PF-15613 - OSP Account Validation for Mobile Booker V2.0
 * 
 */

package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.EJB;
import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.common.impl.BaseImplement;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.webservice.AccountValidationReq;
import com.dds.pathfinder.itaxiinterface.webservice.AccountValidationRsp;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;


/**
 * AccountValidationImplement performs validation against the pathfinder table Account_user, customer_account
 *  and return the result based on the accountCode and the accountPassword.
 * @version 2.0  04 Oct 2013
 */
public class AccountValidationImplement extends OSPBaseImplement {

	private Logger logger = LogManager.getLogger(this.getClass());
	
    private DataSource pfDataSource;
    
    private Date accExpiryDtm;
	private Date accEffectiveDtm;
	private Date custExpiryDtm;
	private Date custEffectiveDtm;
	private String accountStatus;
	private String customerStatus;
	private String groupStatus;
	
	private LoadDispatchParametersLocal cachedParam;
	
    
    
    public enum AccountValidationErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_ACCOUNT(2),
		INVALID_COMPID(4),
		ACCOUNT_NOT_ACTIVE(5),
		ACCOUNT_EXPIRED(6),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private AccountValidationErrorCode(int c) {
			   code = c;
		}

		public int getCode() {
			   return code;
		}
	};
	
    
    public AccountValidationImplement(DataSource pfDataSource, LoadDispatchParametersLocal cachedParam) {
		super();
		this.pfDataSource = pfDataSource;
		this.cachedParam = cachedParam;
		setCachedParam(cachedParam);
	}
    
	public AccountValidationRsp generateResponse(BaseReq request) {
		return generateAccountValidationResponse((AccountValidationReq) request);
	
	}
	
	private AccountValidationRsp generateAccountValidationResponse(AccountValidationReq request){
		AccountValidationRsp response = getDefaultResponse(request);
		
		if(!validateRequest(request, response)){
			   return response;
		}
		
		if(!isCompanyIdExist(request, response)){
			response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
	    	response.setErrorCode(AccountValidationErrorCode.INVALID_COMPID.getCode());
	    	return response;
		}else if(!isValidAccount(request, response)){
			response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
	    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_ACCOUNT);
	    	response.setErrorCode(AccountValidationErrorCode.INVALID_ACCOUNT.getCode());
	    	return response;
		}		
		else if(!isValidCompanyId(request, response)){
			response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
	    	response.setErrorCode(AccountValidationErrorCode.INVALID_COMPID.getCode());
	    	return response;
		}
		else if(!isAccEffDateValid(accEffectiveDtm,custEffectiveDtm)){
			response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
	    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_ACC_NOT_ACTIVE);
	    	response.setErrorCode(AccountValidationErrorCode.ACCOUNT_NOT_ACTIVE.getCode());
	    	return response;
		}
		else if(isAccExpired(accExpiryDtm, custExpiryDtm)){
			response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
	    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_ACC_EXPIRED);
	    	response.setErrorCode(AccountValidationErrorCode.ACCOUNT_EXPIRED.getCode());
	    	return response;
		}
		else{
			response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
	    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
	    	response.setErrorCode(AccountValidationErrorCode.NO_ERROR.getCode());
		}
		return response;
		
	}
	
	private AccountValidationRsp getDefaultResponse(AccountValidationReq request){
		
		AccountValidationRsp response = new AccountValidationRsp();
		   
		response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
		response.setErrorCode(AccountValidationErrorCode.DEFAULT_ERROR.getCode());
		   
		return response;
	}
	/**
	 * Validate Account validation request.
	 * 
	 * @param request
	 *            the request to validate
	 * @param response
	 *            the response to update if error
	 * @return true if valid, false otherwise
	 */
	private boolean validateRequest(AccountValidationReq request, AccountValidationRsp response) {
		
		if (request == null || response == null) {
			return false;
		}
		//validate system id
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(AccountValidationErrorCode.NOT_AUTHENTICATED.getCode());
			return false;
		}
		
		//validate AccountId
		if (request.getAccountCode() == null || request.getAccountCode().trim().length() == 0 || request.getAccountCode().trim().length() >20){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_ACCOUNT);
			response.setErrorCode(AccountValidationErrorCode.INVALID_ACCOUNT.getCode());
			return false;
		}
		//validate taxi company id
		
		if(request.getTaxiCoID() == null ||request.getTaxiCoID() <= 0 || request.getTaxiCoID()> 999999999){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
			response.setErrorCode(AccountValidationErrorCode.INVALID_COMPID.getCode());
			return false;
		}
		
		return true;
	}
	
	//validate account with DB and retrieve the validation result.
	private boolean isValidAccount(AccountValidationReq request, AccountValidationRsp response){
		boolean isValidAcc = false;
		
		String query = "select au.acct_user_id,au.account_id,au.acct_card_nr,au.acct_card_pin "+
		" from ACCOUNT_USERS au" +
		" where UPPER(au.acct_card_nr) = '" + request.getAccountCode().toUpperCase() +"'";
		
		
		Connection con = null;
		Statement stmt= null;
		ResultSet result = null;
		int accountId = 0;
		//int accountUserId = 0;
		String accountCardNumber = null;
		String accountPassword = "";
		
		
		
		try{			
		con = pfDataSource.getConnection();
		stmt = con.createStatement();
		result = stmt.executeQuery(query);
		
		if(result.next()){
			accountId = result.getInt("ACCOUNT_ID");
			System.out.println("accountId: " + accountId);
			//accountUserId = result.getInt("ACCT_USER_ID");
			accountCardNumber = result.getString("ACCT_CARD_NR");
			accountPassword = result.getString("ACCT_CARD_PIN");
			
		}else{
			isValidAcc= false;
		}
		
		
		}catch(SQLException se){
		logger.error("isValidAccount failed...." , se);
		}finally{
		if(result != null) try{result.close();}catch(SQLException ignore){};
		if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
		if(con != null) try{con.close();}catch(SQLException ignore){};
		}
		
		logger.info("account Card Number: " + accountCardNumber);
		
		if (accountId > 0){
			boolean isBookable = isAccountBookable(accountId);
			if(isBookable){
				if (accountCardNumber == null){
					return false;
				}else{
					
					if( accountCardNumber != null && accountCardNumber.trim().length()>0 ){
						int usePassword = cachedParam.getCompanyParameterIntValue(request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_MB_USE_ACCOUNT_PWD);;
						logger.info("UseAccount Password Parameter value: " + usePassword);
						
						/*
						 * 0 – By-pass password check
						 * 1 – Use password when account is configured with password
						 * 2 – Always use password
						 * 3 – Do not use account
						 */
						String reqAaccountPwd = request.getAccountPassword();
						
						switch(usePassword){
							case 0://By-pass password check
								isValidAcc = true;
								break;		
							case 1://Use password when account is configured with password
								logger.info("reqAaccountPwd: " + reqAaccountPwd);
								logger.info("accountPassword: " + accountPassword);
								if(accountPassword!= null && accountPassword.equals(reqAaccountPwd)){
									isValidAcc = true;
								}else if(accountPassword == null && (reqAaccountPwd == null || "".equals(reqAaccountPwd))){
									isValidAcc = true;
								}else{
									isValidAcc = false;
								}
								break;
							case 2:// Always use password
								if(reqAaccountPwd!= null && reqAaccountPwd.equals(accountPassword)){
									isValidAcc = true;
								}else{
									isValidAcc = false;
								}
								break;
							case 3://Do not use account
								isValidAcc = false;
								break;
							default:
								break;
						}
						
						
					}
				}
			}
		}else{
			return false;
		}
		return isValidAcc;
	}
	/*
	 * If the user's effective date is null then inherit from the
	   parent account.  This is just a 1-level inheritance (groups do not have effective date).
	 */
	private boolean isAccEffDateValid(Date accEffectiveDate, Date custEffectiveDate){
		boolean isDateValid = true;
		Date currentDate = new Date();
		if(accEffectiveDate == null){
			if(custEffectiveDate != null && custEffectiveDate.after(currentDate)){
				isDateValid = false;
			}
		}else{
			if(accEffectiveDate.after(currentDate)){
				isDateValid = false;
			}
		}
		//Include the validation on Closed and Suspended Account(PF-15808)
		if(isAccountInactive(accountStatus, customerStatus, groupStatus)){
			isDateValid = false;
		}
		else if(isAccountSuspended(accountStatus, customerStatus, groupStatus)){
			isDateValid = false;
		}
		return isDateValid;
	}

	/*
	 * If the user's expiry date is null then inherit from the
	   parent account.  This is just a 1-level inheritance (groups do not have expiry dates).		   
	 */
	private boolean isAccExpired(Date accExpiryDate, Date custExpiryDate){
		boolean isExpired = false;
		Date currentDate = new Date();
		if(accExpiryDate == null){
			if(custExpiryDate != null && custExpiryDate.before(currentDate)){
				isExpired = true;
			}
		}else{
			if(accExpiryDate.before(currentDate)){
				isExpired = true;
			}
		}
		
		return isExpired;
	}
	private boolean isAccountBookable(int accountId){
		
		String query = "select t.allow_bookings_flag from CUSTOMER_ACCOUNTS t "+
						" where t.account_id =  "+accountId;
						
		
		Connection con = null;
		Statement stmt= null;
		ResultSet result = null;
		String allowBookingsFlag = null;
		boolean isBookable = false;
		
		try{			
			con = pfDataSource.getConnection();
			stmt = con.createStatement();
			result = stmt.executeQuery(query);
			if(result.next()){
				allowBookingsFlag = result.getString("ALLOW_BOOKINGS_FLAG");
			}

		}catch(SQLException se){
			logger.error("isAccountBookable failed...." , se);
		}finally{
			if(result != null) try{result.close();}catch(SQLException ignore){};
			if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
			if(con != null) try{con.close();}catch(SQLException ignore){};
		}
		
		
		if( allowBookingsFlag != null && "Y".equals(allowBookingsFlag.toUpperCase())){
			isBookable = true;
		}else{
			isBookable = false;
		}
		
		return isBookable;
	}
	
	/**
	 * Check if the taxiCoId is valid for the account
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean isValidCompanyId(AccountValidationReq request, AccountValidationRsp response) {
		
		
		String query = "select ag.acct_group_id, ag.taxi_co_id, "+
		"au.expiry_dtm as au_expiry_dtm,ca.expiry_dtm as ca_expiry_dtm,au.effective_dtm as au_effective_dtm,ca.effective_dtm as ca_effective_dtm "+
		", p1.account_status as au_status, p2.account_status as ca_status, p3.account_status as ag_status "+		
		"from ACCOUNT_USERS au, CUSTOMER_ACCOUNTS ca, ACCOUNT_GROUPS ag ,profiles p1 , profiles p2 ,profiles p3, taxi_companies tc "+
		"where au.account_id = ca.account_id and ca.acct_group_id = ag.acct_group_id " +
		"and au.profile_id = p1.profile_id "+ 
		"and p1.parent_profile_id = p2.profile_id "+
		"and p2.parent_profile_id = p3.profile_id "+
		"and p3.parent_profile_id = tc.profile_id  "+
		"and UPPER(au.acct_card_nr) = '" + request.getAccountCode().toUpperCase() +"' " +
		"and ag.taxi_co_id = " +request.getTaxiCoID();
						
		
		Connection con = null;
		Statement stmt= null;
		ResultSet result = null;
		int taxiCoId = 0;
		int acctGroupId = 0;
		boolean isValidCompanyId = false;
		
		try{			
			con = pfDataSource.getConnection();
			stmt = con.createStatement();
			result = stmt.executeQuery(query);
			if(result.next()){
				taxiCoId = result.getInt("TAXI_CO_ID");
				acctGroupId = result.getInt("ACCT_GROUP_ID");
				accExpiryDtm = result.getDate("AU_EXPIRY_DTM");
				accEffectiveDtm = result.getDate("AU_EFFECTIVE_DTM");
				custExpiryDtm = result.getDate("CA_EXPIRY_DTM");
				custEffectiveDtm = result.getDate("CA_EFFECTIVE_DTM");
				accountStatus = result.getString("AU_STATUS");
				customerStatus = result.getString("CA_STATUS");
				groupStatus = result.getString("AG_STATUS");
			}

		}catch(SQLException se){
			logger.error("isValidCompanyId failed...." , se);
		}finally{
			if(result != null) try{result.close();}catch(SQLException ignore){};
			if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
			if(con != null) try{con.close();}catch(SQLException ignore){};
		}
		
		logger.debug("accExpiryDtm: " + accExpiryDtm);
		logger.debug("accEffectiveDtm: " + accEffectiveDtm);
		logger.debug("custExpiryDtm: " + custExpiryDtm);
		logger.debug("custEffectiveDtm: " + custEffectiveDtm);
		
		logger.info("taxi co id in request: " + request.getTaxiCoID());
		logger.info("taxi co id: " + taxiCoId);
		
		if( taxiCoId >0 && taxiCoId == request.getTaxiCoID() ){
			isValidCompanyId = true;
		}else{
			isValidCompanyId = false;
		}
		
		return isValidCompanyId;
	}
	
	/**
	 * Check if the taxi_co_id exist (taxi_co_id is used in isValidAccount to get the C_MB_USE_ACCPW parameter)
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean isCompanyIdExist(AccountValidationReq request, AccountValidationRsp response) {
		
		String query = "select ag.taxi_co_id "+
		"from ACCOUNT_GROUPS ag "+
		"where ag.taxi_co_id = " +request.getTaxiCoID();
						
		
		Connection con = null;
		Statement stmt= null;
		ResultSet result = null;
		int taxiCoId = 0;
		boolean isCoIdExist = false;
		
		try{			
			con = pfDataSource.getConnection();
			stmt = con.createStatement();
			result = stmt.executeQuery(query);
			if(result.next()){
				taxiCoId = result.getInt("TAXI_CO_ID");
			}

		}catch(SQLException se){
			logger.error("isCompanyIdExist failed...." , se);
		}finally{
			if(result != null) try{result.close();}catch(SQLException ignore){};
			if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
			if(con != null) try{con.close();}catch(SQLException ignore){};
		}
		
		logger.info("taxi co id in request: " + request.getTaxiCoID());
		logger.info("taxi co id: " + taxiCoId);
		
		if( taxiCoId >0 && taxiCoId == request.getTaxiCoID() ){
			isCoIdExist = true;
		}else{
			isCoIdExist = false;
		}
		
		return isCoIdExist;
	}
	/**
	 * retrieve the value for parameter: C_MB_USE_ACCPW
	 * 0 – By-pass password check
	 * 1 – Use password when account is configured with password
	 * 2 – Always use password
	 * 3 – Do not use account
	 * @param request
	 * @return
	 */
	/*
	private int getUsePwdAccountParameter(AccountValidationReq request){
		//PF-15809 get company parameter from cache
		//int usePwdAccParam = cachedParam.getCompanyParameterIntValue(request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_MB_USE_ACCOUNT_PWD);
		int usePwdAccParam = CompanyDefaultValues.getCompanyParameterIntValue(pfDataSource, request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_MB_USE_ACCOUNT_PWD);
		logger.info("Parameter C_MB_USE_ACCPW value 2: " + usePwdAccParam);
		return usePwdAccParam;
	}*/
	
	/**
	 * This method veryfy if staus of account_users, customer_accounts or account_groups related to the account is set to SUSPENDED("S").
	 * @param au_status
	 * @param ca_status
	 * @param ag_status
	 * @return
	 */
	private boolean isAccountSuspended(String au_status, String ca_status, String ag_status ) {
		boolean isAccSusp = false;
		if("S".equalsIgnoreCase(au_status) ||
		   "S".equalsIgnoreCase(ca_status)	||
		   "S".equalsIgnoreCase(ag_status)){
			isAccSusp = true;
		}
		return isAccSusp;
	}
	
	/**
	 * This method veryfy if staus of account_users, customer_accounts or account_groups related to the account is set to CLOSED("C").
	 * @param au_status
	 * @param ca_status
	 * @param ag_status
	 * @return
	 */
	private boolean isAccountInactive(String au_status, String ca_status, String ag_status ) {
		boolean isAccInactive = false;
		if("C".equalsIgnoreCase(au_status) ||
		   "C".equalsIgnoreCase(ca_status)	||
		   "C".equalsIgnoreCase(ag_status)){
			isAccInactive = true;
		}
		return isAccInactive;
	}
	
	
}
