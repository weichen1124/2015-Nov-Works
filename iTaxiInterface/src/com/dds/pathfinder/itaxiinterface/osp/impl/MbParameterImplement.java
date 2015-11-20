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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/MbParameterImplement.java $
 * 
 * 6     3/12/14 3:32p Dchen
 * Added osp_users table instead of hard coding.
 * 
 * 5     1/24/14 12:21p Mkan
 * getMBParamList() - Removed usage of obsolete C_MB_TIP_BUTTON2
 * (PF-15865)
 * 
 * 4     11/22/13 5:00p Sfoladian
 * Changed C_MB_CC_PAYMNT_TMOUT to C_MB_PAYMNT_TMOUT, used for both CC and
 * Account Payment
 * 
 * 3     11/20/13 11:29a Sfoladian
 * PF-15595- Mobile Booker Parameter, Added new Parameter:
 * C_MB_CC_PAYMNT_TMOUT
 * 
 * 2     10/31/13 12:19p Sfoladian
 * Changed to get the parameter from the cachedParam instead of DB
 * 
 * 1     10/28/13 12:13p Sfoladian
 * 
 * PF-15595 - Mobile Booker Parameter 
 * Mobile Booker V2.0
 */


package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.GetMBParamRequest;
import com.dds.pathfinder.itaxiinterface.webservice.GetMBParamResponse;
import com.dds.pathfinder.itaxiinterface.webservice.MbParamListItem;

/**
 * @author sfoladian
 *
 */
public class MbParameterImplement extends OSPBaseImplement {

	private Logger logger = LogManager.getLogger(this.getClass());
	
    private DataSource pfDataSource;
    protected LoadDispatchParametersLocal cachedParam;

    private String EMPTY_TEXT = "";
    
    private enum MbParameterErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_SESSIONID(2),
		INVALID_COM_ID(4),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private MbParameterErrorCode(int c) {
			   code = c;
		}

		public int getCode() {
			   return code;
		}
	}
    
    public MbParameterImplement(DataSource pfDataSource) {
		super();
		this.pfDataSource = pfDataSource;
	}
    
    public MbParameterImplement(DataSource pfDataSource, LoadDispatchParametersLocal cachedParam){
		this.pfDataSource = pfDataSource;
		this.cachedParam = cachedParam;
		setCachedParam(cachedParam);
	}
    
	public GetMBParamResponse generateResponse(BaseReq request) {
		
		return getAllParameters((GetMBParamRequest)request);
	}
    
    private GetMBParamResponse getAllParameters(GetMBParamRequest request){
    	GetMBParamResponse response = new GetMBParamResponse();
    	
    	if(!validGetMbParameterRequest(request)){
    		response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
        	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
        	response.setErrorCode(MbParameterErrorCode.DEFAULT_ERROR.getCode());
			return response;
		}
    	if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(MbParameterErrorCode.NOT_AUTHENTICATED.getCode());
			return response;
		}
    	
    	//validate taxi company id
		if(request.getTaxiCoID() == null || request.getTaxiCoID() <= 0 || request.getTaxiCoID()> 999999999){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
			response.setErrorCode(MbParameterErrorCode.INVALID_COM_ID.getCode());
			return response;
		}
    	int taxiID = request.getTaxiCoID();
    	logger.info("taxiID " + taxiID);
		
		
		ArrayList<MbParamListItem> paramList = new ArrayList<MbParamListItem>();
		getMBParamList(taxiID, paramList);
		
		setMBParamListResponse(response, paramList);
    	return response;
    }
    
    
	private void setMBParamListResponse(GetMBParamResponse response, ArrayList<MbParamListItem> paramList){
		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
    	response.setErrorCode(MbParameterErrorCode.NO_ERROR.getCode());
    	int nrofParameters = paramList.size();
    	response.setNrOfParameters(nrofParameters);
    	if(nrofParameters > 0){
    		MbParamListItem[] parArray = new MbParamListItem[nrofParameters];
    		paramList.toArray(parArray);
    		response.setListOfParameters(parArray);
    	}
	}
	
	private boolean validGetMbParameterRequest(GetMBParamRequest request){
		if(request == null  ){ 
			return false;
		}else 
			return true;
		
	}
	
	/*
	 * Get the company parameters from the cachedParam
	 * The list of parameter to send
	 * 	USE_ACCT_PW	  
		CC_PAYMENT	
		TIP_BUTTON1	A number, 0-99	Tip % Button 1
		TIP_BUTTON2	A number, 0-99	Tip % Button 2
		BASE_RATE	A number	Base Rate
		RATE_PER_DIST	A number	Rate per Distance
		SND_MSG_DRV	
		DROP_OFF_MANDATORY
		MULTI_BOOK_ALLOWED
		SAME_LOC_BOOK_ALLOWED
		PAYMNT_TMOUT
	 */
	private void getMBParamList(int taxiID, ArrayList<MbParamListItem> paramList){
		
		String useAccPassword = cachedParam.getCompanyParameterValue(taxiID, CompanyDefaultValues.COMP_PARAMETER_C_MB_USE_ACCOUNT_PWD);
		String baseRate = cachedParam.getCompanyParameterValue(taxiID, CompanyDefaultValues.COMP_PARAMETER_C_MB_BASE_RATE);
		String enableCCpayment = cachedParam.getCompanyParameterValue(taxiID, CompanyDefaultValues.COMP_PARAMETER_C_MB_CC_PAMNT_ENABLE);
		String dropOffMand = cachedParam.getCompanyParameterValue(taxiID, CompanyDefaultValues.COMP_PARAMETER_C_MB_DRP_OFF_MAND);
		String multiBookAllowed = cachedParam.getCompanyParameterValue(taxiID, CompanyDefaultValues.COMP_PARAMETER_C_MB_MLT_BOOK_ALWD);
		String ratePerDist = cachedParam.getCompanyParameterValue(taxiID, CompanyDefaultValues.COMP_PARAMETER_C_MB_RATE_PER_DIST);
		String sameLocBookAllowed = cachedParam.getCompanyParameterValue(taxiID, CompanyDefaultValues.COMP_PARAMETER_C_MB_SAME_LOC_BK_ALW);
		String sendMsgToDriver = cachedParam.getCompanyParameterValue(taxiID, CompanyDefaultValues.COMP_PARAMETER_C_MB_SND_MSG_DRV);
		String tipButton1 = cachedParam.getCompanyParameterValue(taxiID, CompanyDefaultValues.COMP_PARAMETER_C_MB_TIP_BUTTON1);
		String ccPaymentTimeout = cachedParam.getCompanyParameterValue(taxiID, CompanyDefaultValues.COMP_PARAMETER_PAYMNT_TMOUT);
		
		if(useAccPassword !=null){
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_USE_ACCOUNT_PWD, useAccPassword));
		}else{
			//After talking with George we decided to pass the empty value in the parameter list
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_USE_ACCOUNT_PWD, EMPTY_TEXT));
		}
		
		if(baseRate != null){
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_BASE_RATE, baseRate));
		}else{
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_BASE_RATE, EMPTY_TEXT));
		}
		
		if(enableCCpayment != null){
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_CC_PAMNT_ENABLE, enableCCpayment));
		}else{
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_CC_PAMNT_ENABLE, EMPTY_TEXT));
		}
		
		if(dropOffMand != null){
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_DRP_OFF_MAND, dropOffMand));
		}else{
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_DRP_OFF_MAND, EMPTY_TEXT));
		}
		
		if(multiBookAllowed != null){
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_MLT_BOOK_ALWD, multiBookAllowed));
		}else{
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_MLT_BOOK_ALWD, EMPTY_TEXT));
		}
		
		if(ratePerDist != null){
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_RATE_PER_DIST, ratePerDist));
		}else{
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_RATE_PER_DIST, EMPTY_TEXT));
		}
		if(sameLocBookAllowed != null){
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_SAME_LOC_BK_ALW, sameLocBookAllowed));
		}else{
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_SAME_LOC_BK_ALW, EMPTY_TEXT));
		}
		if(sendMsgToDriver != null){
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_SND_MSG_DRV, sendMsgToDriver));
		}else{
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_SND_MSG_DRV, EMPTY_TEXT));
		}
		
		if(tipButton1 != null){
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_TIP_BUTTON1, tipButton1));
		}else{
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_C_MB_TIP_BUTTON1, EMPTY_TEXT));
		}
		
		if(ccPaymentTimeout != null){
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_PAYMNT_TMOUT, ccPaymentTimeout));
		}else{
			paramList.add(new MbParamListItem(CompanyDefaultValues.COMP_PARAMETER_PAYMNT_TMOUT, EMPTY_TEXT));
		}
		
	}

}
