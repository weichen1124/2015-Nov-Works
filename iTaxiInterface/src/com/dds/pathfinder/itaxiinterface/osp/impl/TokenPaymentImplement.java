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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/TokenPaymentImplement.java $
 * PF-16074 get different PG comp num and password based on request from taxilimo or MB2.0
 * 
 * 12    4/30/14 11:22a Ezhang
 * PF-15968 only allow mobile booker (1.0/2.0) and TaxiLimo Mobile Booker(MB3.0) to do token payment
 * 
 * 11    3/12/14 3:32p Dchen
 * Added osp_users table instead of hard coding.
 * 
 * 10    2/06/14 3:53p Ezhang
 * PF-15850 use MB specific PG company ID and password 
 * 
 * 9     1/29/14 3:48p Ezhang
 * PF-15809 use cached parameter value
 * 
 * 8     12/30/13 5:30p Ezhang
 * PF- 15897 update getJobPickDropInfo() to return quoted_location as dropoff location.
 * 
 * 7     12/30/13 1:17p Ezhang
 * PF- 15806 update get_osp_job_pay_info to retrieve driver's badge number.
 * 
 * 6     11/28/13 11:35a Ezhang
 * changed the get_osp_job_info database call.
 * 
 * 5     11/21/13 5:01p Ezhang
 * PF-15582 change the logic to allow the payment after job completed. and rsp code to store in payments table
 * 
 * 4     11/13/13 10:41a Ezhang
 * PF-15582 added two PG timeout response code
 * 
 * 3     11/05/13 1:16p Ezhang
 * PF-15582 Added error handling for invalid trip id and invalid amount, and email and emailSent
 * 
 * 2     10/30/13 4:08p Ezhang
 * PF-15582 added email and emailSent to the response
 * return INVALID_JOB_LIST if recall by joblist is not valid(more than 15 jobs or jobid has non-digital value)
 * 
 * 1     10/23/13 3:06p Ezhang
 * 
 * PF-15582 Add Credit card Payment by Token Support for Mobile
 * Booker V2.0
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

import paymentgateway.dds.com.webservice.paymentservice.PaymentGatewayService;
import paymentgateway.dds.com.webservice.paymentservice.PaymentGatewayServiceInterface;
import paymentgateway.dds.com.webservice.paymentservice.TokenPaymentRequestType;
import paymentgateway.dds.com.webservice.paymentservice.TokenPaymentResponseType;

import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.TokenPaymentReq;
import com.dds.pathfinder.itaxiinterface.webservice.TokenPaymentRes;


/**
 * TokenPaymentImplement will validate the tokenized credit card payment request from MG
 * and pass on to PG to validate. In return from PG, it will send response to both MG and MDT
 * and update the payments table if the transaction is approved.
 * 
 * @author ezhang
 *
 */
public class TokenPaymentImplement extends OSPBaseImplement {


	public final static String PAYLOAD_SEPARATOR = ";";
	public final static String CARD_MASK = "XXXXXXXXXXXX";
	public final static int  PG_RSP_APPROVED = 1;
	public final static int  PG_RSP_DECLINED = 7;
	public final static int PG_RSP_TIMEOUT_PROCESSOR = 20;
	public final static int PG_RSP_TIMEOUT_FAILED_SEND = 21;
	public final static int PG_RSP_DUP_PG = 12;
	public final static int PG_RSP_DUP_PROCESSOR =23;
	/*
	public final static int PG_DECLINE_NO_CARD = 19;
	public final static int PG_DECLINE_CALL_HOST = 22;
	public final static int PG_DECLINE_FRAUD_ALERT = 24;
	public final static int PG_DECLINE_RETRY = 25;
	public final static int PG_DECLINE_MERCHANT_ACCT = 26;
	public final static int PG_DECLINE_SETUP_PROBLEM = 27;
	public final static int PG_DECLINE_SERVER_PROBLEM = 28;
	public final static int PG_DECLINE_INVALID_CARD = 29;
	public final static int PG_DECLINE_NOT_AUTHED = 30;
	public final static int PG_DECLINE_CARD_LIMIT_PROBLEM = 31;
	*/
	private Logger logger = LogManager.getLogger(this.getClass());
	
	private DataSource pfDataSource;
	private PaymentGatewayServiceInterface pgService;
	private int jobId;
	private String driver_id;
	private String vehicle_number;
	private int mdt_id;
	private String pickupAddress;
	private String dropoffAddress;
	private float paymentAmt;
	private float paidAmt;
	private String paidAuthCode;
	
	
	private LoadDispatchParametersLocal cachedParam;
	
	
	
	public enum TokenPaymentErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_DEVICEID(2),
		JOB_NOT_FOUND(3),
		INVALID_COMPID(4),
		INVALID_CARNUM(5),
		INVALID_CAR_STATUS(6),
		INVALID_TOKEN(7),
		INVALID_INFO3(8),
		INVALID_AMOUNT(9),
		INVALID_SEQNO(10),
		INVALID_PG_RSP(11),
		INVALID_CARD(12),
		ALREADY_PAID(13),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private TokenPaymentErrorCode(int c) {
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
	
		public TokenPaymentImplement(DataSource pfDataSource, LoadDispatchParametersLocal cachedParam) {
			super();
			this.pfDataSource = pfDataSource;
			this.pgService = getPGService();
			this.cachedParam = cachedParam;
			setCachedParam(cachedParam);
		}


		public TokenPaymentRes generateResponse(BaseReq request) {
	
			return generateTokenPaymentRepsonse((TokenPaymentReq) request);
		}
		
		private TokenPaymentRes generateTokenPaymentRepsonse(TokenPaymentReq request) {
			
			TokenPaymentRes response = getDefaultResponse(request);
			
			if(!validateRequest(request, response)){
				   return response;
			}
			
			
			//send payment request to PG
			
			//create paymentgateway token payment request
			TokenPaymentRequestType pgTokenReq = new TokenPaymentRequestType();
			setupTokenPaymentRequest(request,pgTokenReq);
			
			TokenPaymentResponseType pgTokenRes = getPGTokePaymentResult(pgTokenReq);
			
			//check the PG Token Payment Response
			if(pgTokenRes != null) {
			
				
				logger.info ("PG response Code: " + pgTokenRes.getResponseCode());
				logger.info(" PG Rsp Msg: " + pgTokenRes.getResponseMSG());
				logger.info(" PG Auth #: " + pgTokenRes.getAuthCode());
				logger.info(" PG Rsp Seq: " + pgTokenRes.getSeqNum());
				
				
				//send successful response to Mobile Gateway regardless payment declined or approved
				response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
				response.setSeqNo(pgTokenRes.getSeqNum());
				response.setRspCode(pgTokenRes.getResponseCode());
				response.setRspMsg(pgTokenRes.getResponseMSG());
				response.setAuthCode(pgTokenRes.getAuthCode());
				response.setRefNo(pgTokenRes.getReferenceNum());
				response.setErrorCode(TokenPaymentErrorCode.NO_ERROR.getCode());
				response.setEmail(pgTokenRes.getEmail());
				response.setEmailSend(pgTokenRes.isEmailSent());
				
			}else {
				// the payment gateway did not return result due to network or system failure
				//create default declined message to both Mobile booker
				response.setSeqNo(pgTokenReq.getSeqNum());
				response.setErrorCode(TokenPaymentErrorCode.INVALID_PG_RSP.getCode());	
			
			}
			
			
			sendPaymentResToMDT(request, pgTokenRes);
			
			//forward the response to mobile gateway
			
			
			return response;
			
		}
		
		
		private void setupTokenPaymentRequest(TokenPaymentReq request,
				TokenPaymentRequestType pgTokenReq) {
			
				int systemId = request.getSystemID();
				String pwd = "";
				//PF-16074 get different PG comp num and password based on request from taxilimo or MB2.0
				if(systemId == TAXILIMO_MOBILE_BOOKER_SYS_ID){
					pgTokenReq.setCompanyID(cachedParam.getCompanyParameterIntValue(request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_TL_PG_COMP_NUM));
					pwd = getEncrCompanyParameter(CompanyDefaultValues.COMP_PARAMETER_C_TL_PG_COMP_PWD, request.getTaxiCoID() );
				}else if(systemId == ExternalSystemId.SYSTEM_ID_MB.getSystemId()){
				//set up PG company number and password from parameters
					pgTokenReq.setCompanyID(cachedParam.getCompanyParameterIntValue(request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_MB_PG_COMP_NUM));
					pwd = getEncrCompanyParameter(CompanyDefaultValues.COMP_PARAMETER_C_MB_PG_COMP_PWD, request.getTaxiCoID() );
				}
				pgTokenReq.setPassword(pwd);
				//set up request from OSP request
				pgTokenReq.setDeviceID(request.getDeviceID());
				pgTokenReq.setSeqNum(request.getSeqNo());
				pgTokenReq.setReqType(request.getReqType());
				pgTokenReq.setToken(request.getToken());
				pgTokenReq.setAmount(request.getAmount());
				pgTokenReq.setTip(request.getTip());
				//set up job info
				pgTokenReq.setInfo3(String.valueOf(jobId)); //trip id is required
				pgTokenReq.setInfo1(driver_id);
				pgTokenReq.setInfo2(vehicle_number);
				//set up pickup and dropoff address
				getJobPickDropInfo(jobId);
				pgTokenReq.setInfo4(pickupAddress);
				pgTokenReq.setInfo5(dropoffAddress);
				
				logger.info("job id " + jobId);
				logger.info( "driver id " + driver_id);
				logger.info("vehicle_number " + vehicle_number);
				logger.info("pickup " + pickupAddress + " dropoff " + dropoffAddress);
				
				
					
			
		}


		private TokenPaymentRes getDefaultResponse(TokenPaymentReq request){
			
			TokenPaymentRes response = new TokenPaymentRes();
			   
			response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
			response.setErrorCode(TokenPaymentErrorCode.DEFAULT_ERROR.getCode());
			response.setSeqNo(request.getSeqNo());
			   
			return response;
		}
		
		/**
		 * Validate token Payment request, check all required fields
		 * 
		 * @param request
		 *            the request to validate
		 * @param response
		 *            the response to update if error
		 * @return true if valid, false otherwise
		 */
		private boolean validateRequest(TokenPaymentReq request, TokenPaymentRes response) {
			
			if (request == null || response == null) {
				return false;
			}
			//validate system id	
			if(!validateSystemId(pfDataSource, request)){
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
				response.setErrorCode(TokenPaymentErrorCode.NOT_AUTHENTICATED.getCode());
				return false;
			}
			//system allow token payment
			if(!cachedParam.getOSPAllowTokenPay(request.getSystemID())) {
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
				response.setErrorCode(TokenPaymentErrorCode.NOT_AUTHENTICATED.getCode());
				return false;
			}
			//validate deviceID(Mobile booker hardware id) is required
			if (request.getDeviceID() == null || request.getDeviceID().trim().length() == 0){
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
				response.setErrorCode(TokenPaymentErrorCode.INVALID_DEVICEID.getCode());
				return false;
			}
			
			
			
			
			//validate amount, it is required
			String amtStr = request.getAmount();
			if(!validateAmount(amtStr)) {
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
				response.setErrorCode(TokenPaymentErrorCode.INVALID_AMOUNT.getCode());
				return false;
			}else {
				paymentAmt = Float.parseFloat(amtStr);
			}
			
			//validate taxi company id
			if(request.getTaxiCoID() == null ||request.getTaxiCoID() <= 0 || request.getTaxiCoID()> 999999999){
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
				response.setErrorCode(TokenPaymentErrorCode.INVALID_COMPID.getCode());
				return false;
			}
			
			//validate sequence number, it is required
			if(request.getSeqNo() == null ||request.getSeqNo().trim().length() == 0){
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
				response.setErrorCode(TokenPaymentErrorCode.INVALID_SEQNO.getCode());
				return false;
			}
			//validate token, it is required
			if(request.getToken() == null ||request.getToken().trim().length() == 0){
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
				response.setErrorCode(TokenPaymentErrorCode.INVALID_TOKEN.getCode());
				return false;
			}
			//validate cardNr and cardBrand, it is required
			if(request.getCardNr() == null || request.getCardNr().trim().length() == 0 ||
					request.getCardBrand() == null || request.getCardBrand().trim().length() == 0) {
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
				response.setErrorCode(TokenPaymentErrorCode.INVALID_CARD.getCode());
				return false;
			}
			
			//get trip Info
			//validate Info3 jobId is required
			String info3 = request.getInfo3();
			if( !validateJobId(info3)) {
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
				response.setErrorCode(TokenPaymentErrorCode.INVALID_INFO3.getCode());
				return false;
			}else {
				jobId = Integer.parseInt(info3);
			}
			
			String systemRef = getSystemReference(request.getSystemID());
			int taxiCoId = request.getTaxiCoID();
			getJobInfo(jobId, taxiCoId, systemRef); 
			
			//PF-16100 check successful payment
			if(paidAmt > 0 ) {
				if( paidAuthCode != null && paidAuthCode.length() > 0){
					//response.setAuthCode(paidAuthCode);
					response.setErrorMessage(GenErrMsgRes.ERROR_CODE_DUPLICATE_PAYMNT + " CC amt " + paidAmt + " auth# " + paidAuthCode);
					response.setErrorCode(TokenPaymentErrorCode.ALREADY_PAID.getCode());
				}else{
					response.setErrorMessage(GenErrMsgRes.ERROR_CODE_DUPLICATE_PAYMNT + " AC amt " + paidAmt);
					response.setErrorCode(TokenPaymentErrorCode.ALREADY_PAID.getCode());
				}
				return false;
			}		
			//check driver_id
			else if(driver_id == null || vehicle_number == null) {
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
				response.setErrorCode(TokenPaymentErrorCode.INVALID_CARNUM.getCode());
				return false;
			}
			
			return true;
		}
		
		//call after payment gateway send response back regardless succeed or declined
		//Create a declined response if system error happened
		//or no response got from Payment Gateway due to wrong configuration or 
		//communication problem.
		private void sendPaymentResToMDT(TokenPaymentReq req, TokenPaymentResponseType res) {
				
				int result = 1; //failed
			 	Connection con = null;
			    CallableStatement cs = null;
			    String authCode = "";  
			    String rspMsg = ""; 
			    float tipAmt = 0;
			    int rspcode = 0;
			    
			    String userName = getUserName(req);
			    
			    //this is valid payment response
			    if(res != null ) {
			    	
			    	//cardType = res.getCardType();
			    	if(res.getResponseCode() == PG_RSP_APPROVED ) {
			    		authCode  = res.getAuthCode();
			    		rspMsg = "APPROVED";
			    		rspcode = PG_RSP_APPROVED;
			    	}else if(res.getResponseCode() == PG_RSP_TIMEOUT_PROCESSOR || res.getResponseCode() == PG_RSP_TIMEOUT_FAILED_SEND) {
			    		authCode = "";
				    	rspMsg = "TIMEOUT";
				    	rspcode = PG_RSP_TIMEOUT_PROCESSOR;
			    	}else if(res.getResponseCode() == PG_RSP_DUP_PG || res.getResponseCode() == PG_RSP_DUP_PROCESSOR) {
			    		authCode = res.getAuthCode();
			    		rspMsg = "DUPLICATE";
			    		rspcode = PG_RSP_DUP_PG;
			    	}
			    	else {
			    		authCode = "";
			    		rspMsg = "DECLINED";
			    		rspcode = PG_RSP_DECLINED;
			    	}
		    		//approved payment or declined payment
			    	//authCode  = res.getAuthCode();
			    	
			    	//payload:jobId;cardNr;cardType;amount;PaymentResponseMessage;authCode
			    	//eg 12345;4242;VISA;20.00;Approved;635590
			    	/*
			    	payLoad = req.getInfo3() + PAYLOAD_SEPARATOR + CARD_MASK + req.getCardNr() + PAYLOAD_SEPARATOR + req.getCardBrand() + PAYLOAD_SEPARATOR 
			    			+ req.getAmount() + PAYLOAD_SEPARATOR 
			    			+ rspMsg + PAYLOAD_SEPARATOR + authCode; 
			    	
			    	logger.info("succeed payload: " + payLoad);
			    	*/

			    }
			    else {
			    	//invalid payment gateway response
			    	authCode = "";
			    	rspMsg = "TIMEOUT";
			    	rspcode = PG_RSP_TIMEOUT_PROCESSOR;
			    	
			    	
			    	//payload:jobId;cardNr;cardType;amount;PaymentResponseMessage;authCode
			    	//eg 12345;4242;VISA;20.00;DECLINED;
			    	/*
			    	payLoad = req.getInfo3() +  PAYLOAD_SEPARATOR+ CARD_MASK + req.getCardNr() + PAYLOAD_SEPARATOR + req.getCardBrand() + PAYLOAD_SEPARATOR  
			    			+ req.getAmount() + PAYLOAD_SEPARATOR
			    			+ rspMsg +  PAYLOAD_SEPARATOR; 
			    	
			    	logger.info("failed payload: " + payLoad);
			    	*/
			    }
			    
			    try
			    {
			        con = pfDataSource.getConnection ();
			       	
			        cs = con.prepareCall ("{?=call payment.process_mb_cc_payment (?,?,?,?,?,?,?,?,?,?,?)}");
			   
			        logger.info ("Job Id: " + jobId);
			        logger.info ("Payment Amount: " + req.getAmount ());
			        logger.info ("Confirmation Num: " + authCode);
			        logger.info(" user name: " + userName);
			        //logger.info("last 4 digit: " + cardNr.substring((cardNr.length() -4)));
			   
			        cs.registerOutParameter (1, java.sql.Types.INTEGER);
				    cs.setInt (2, jobId); //job id
				    cs.setInt(3, mdt_id);
				    cs.setString(4, vehicle_number);//callsign
				    cs.setFloat (5, paymentAmt);
				    cs.setString (6, authCode);
				    cs.setString(7, rspMsg);
				    
				    String cardNr = req.getCardNr();
				    if(cardNr.length() > 4) {
				    	cs.setString(8, cardNr.substring(cardNr.length()-4)); //save the last 4 digits.
				    }
				    else {
				    	cs.setString(8, cardNr);
				    }
				    
				    cs.setString(9, req.getCardBrand());
				    //set up tip amount  
				    if(validateAmount(req.getTip())) {
				    	tipAmt = Float.parseFloat(req.getTip());
				    }
				    cs.setFloat(10, tipAmt);
				    cs.setInt(11, rspcode);
				    cs.setString(12, userName);
				    cs.execute ();
				   
				    logger.info ("Token Payment sendPaymentResToMDT() Result" + cs.getInt (1));
			       
				    result = cs.getInt (1);
				    if (result == 0) 
				    {
				    	logger.info ("Token Payment sendPaymentResToMDT() job: " + jobId + " succeed");
					    
				    } 	
				    else if (result == 2) 
				    {
				    	logger.info ("Token Payment sendPaymentResToMDT() job: " + jobId + " failed");
					    
				    } 
		 	    }
			    catch(SQLException se)
			    {
		      	    logger.error ("Token Payment sendPaymentResToMDT() failed with exception", se);
		      	   
		        }
			    finally
			    {
		            if (cs != null) try {cs.close ();} catch (SQLException ignore) {};
		    	    if (con != null) try {con.close ();} catch (SQLException ignore) {};
		        }
			   
		}
		
		//PF-15968 only allow mobile booker (1.0/2.0) and TaxiLimo Mobile Booker(MB3.0)
		//webbooker and TaxiLimo WebBooker is not allowed.
		private void getJobInfo(int jobId, int taxiCoId, String systemRef) {
			
			//initialize values
			vehicle_number = null;
			driver_id = null;
			mdt_id = 0;
			paidAmt = 0;
			paidAuthCode = null;
			
			
			Connection dbConnection = null;
			CallableStatement cs = null;
			
		    
			try {
				
				dbConnection = pfDataSource.getConnection ();
				
		       	//updated to support payment when vehicle is on Arrived, POB state or job completed within
				//configured time range.
		        cs = dbConnection.prepareCall ("{call jobpf.get_osp_job_pay_info (?,?,?,?,?,?,?,?,?)}");
		        
		        
		        cs.registerOutParameter (1, java.sql.Types.INTEGER);
			    cs.setInt (1, jobId); //job id
			    cs.setString(2, systemRef); //systemRef
			    cs.setInt(3, taxiCoId); //taxi_co_id
			    cs.registerOutParameter(4, java.sql.Types.VARCHAR);
			    cs.registerOutParameter(5, java.sql.Types.VARCHAR);
			    cs.registerOutParameter(6, java.sql.Types.INTEGER); 
			    cs.registerOutParameter(7, java.sql.Types.INTEGER);
			    cs.registerOutParameter(8, java.sql.Types.FLOAT); //amount
			    cs.registerOutParameter(9, java.sql.Types.VARCHAR); //auth code
			    cs.execute ();
				   
			    logger.info ("Token Payment getJobInfo()" );
		       
			    vehicle_number = cs.getString(4);
			    driver_id = cs.getString(5);
			    mdt_id = cs.getInt(6);
			    paidAmt = cs.getFloat(8);
			    paidAuthCode = cs.getString(9);
			    
			    logger.info ("Job Id: " + jobId);
		        logger.info ("vehicle_number " + vehicle_number);
		        logger.info ("driver_id " + driver_id);
			   
			
			}catch (SQLException e) {
				logger.error("Exception in getJobInfo(jobID " + jobId + ")", e);
			} 
		    finally
		    {
	            if (cs != null) try {cs.close ();} catch (SQLException ignore) {};
	    	    if (dbConnection != null) try {dbConnection.close ();} catch (SQLException ignore) {};
	        }
		   
		}
		
		//get job's pickup and dropoff address
		private void getJobPickDropInfo(int jobId) {
			
			StringBuffer query = new StringBuffer();
			/* actual SQL query added quoted_location
			SELECT job_id, DECODE(sp.quoted_location, NULL, (DECODE(sp.forced_address, NULL, spvrpages.expand_addr(sp.address_id, sp.forced_unit_nr), spvrpages.format_forced_address(sp.forced_address, sp.forced_unit_nr))),  spvrpages.format_forced_address(sp.quoted_location, null) )as address, sp.stop_enumeration as stop_order 
			from jobs j, stop_points sp 
			where (j.shared_ride_state IS NULL OR j.shared_ride_state = 'N') 
			AND j.order_id = sp.order_id and j.job_id = 498455
      		UNION ALL 
 			SELECT job_id, DECODE(sp.quoted_location, NULL, (DECODE(sp.forced_address, NULL, spvrpages.expand_addr(sp.address_id, sp.forced_unit_nr), spvrpages.format_forced_address(sp.forced_address, sp.forced_unit_nr))),  spvrpages.format_forced_address(sp.quoted_location, null) )as address, sp.stop_enumeration as stop_order 
			from jobs j, stop_points sp 
			where (j.shared_ride_state IS NULL OR j.shared_ride_state = 'A') 
			AND j.order_id = sp.order_id and j.job_id = 498455
			*/
			query.append("SELECT job_id, DECODE(sp.quoted_location, NULL, (DECODE(sp.forced_address, NULL, spvrpages.expand_addr(sp.address_id, sp.forced_unit_nr), spvrpages.format_forced_address(sp.forced_address, sp.forced_unit_nr))),  spvrpages.format_forced_address(sp.quoted_location, null) )as address, sp.stop_enumeration as stop_order ");
			query.append("from jobs j, stop_points sp ");
			query.append("where (j.shared_ride_state IS NULL OR j.shared_ride_state = 'N') ");
			query.append("AND j.order_id = sp.order_id and j.job_id = ? ");
			query.append("UNION ALL ");
			query.append("SELECT job_id, DECODE(sp.quoted_location, NULL, (DECODE(sp.forced_address, NULL, spvrpages.expand_addr(sp.address_id, sp.forced_unit_nr), spvrpages.format_forced_address(sp.forced_address, sp.forced_unit_nr))),  spvrpages.format_forced_address(sp.quoted_location, null) )as address,  ");
			query.append(" sp.sr_stop_enumeration as stop_order ");
			query.append("from jobs j, stop_points sp ");
			query.append("where j.shared_ride_state = 'A' ");
			query.append("AND j.order_id = sp.sr_order_id and j.job_id = ? ");
			
			
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
				stmt.setInt(2, jobId);
				rset = stmt.executeQuery();
				
				int stopOrder = -1;
				pickupAddress = "";
				dropoffAddress = "";
				
				while (rset.next()) {
					
					stopOrder = rset.getInt("stop_order");
					String address = rset.getString("address");
					
					 if(stopOrder == 1 && address != null && address.length() > 0) {
						 pickupAddress = address;
					 }
					 
					 if(stopOrder == 2 && address != null && address.length() > 0) {
						 dropoffAddress = rset.getString("address");
					 }
					
				}
				
			}catch (SQLException e) {
				logger.error("Exception in getJobPickDropInfo(jobID " + jobId + ")", e);
			}finally {
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
		
		
		
		private String getEncrCompanyParameter(String param, int taxiCoID) {
			Connection dbConnection = null;
			CallableStatement cs = null;
			String paramValue = "";
			
			try {
				if ((dbConnection = pfDataSource.getConnection()) == null) {
					logger.error("Failed to get db connection.");
					return null;
				}
				
				cs = dbConnection.prepareCall("{?= call SERVER_UTILITY.Get_encr_company_parameter(?, ?)}");
				cs.registerOutParameter (1, java.sql.Types.VARCHAR);
				cs.setString(2, param);
				cs.setInt(3, taxiCoID);
				
				cs.execute ();
				
				if (cs.executeUpdate() != 0) {
					paramValue = cs.getString(1);
				}
				
				
			}catch (SQLException e) {
				logger.error("Exception in getEncrCompanyParameter ", e);
			}finally
		    {
	            if (cs != null) try {cs.close ();} catch (SQLException ignore) {};
	    	    if (dbConnection != null) try {dbConnection.close ();} catch (SQLException ignore) {};
	        }
			return paramValue;
			
		}
		
	
		//PF-15582
		private PaymentGatewayServiceInterface getPGService(){
			
			if(pgService == null){
				long time = System.currentTimeMillis();
				try {
					PaymentGatewayService cbPGService = new PaymentGatewayService();
					pgService = cbPGService.getPaymentGatewayServiceInterfacePort();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				if(pgService != null){
					logger.info("get payment gateway service interface ....." + (System.currentTimeMillis() - time)/1000.00+"s");
				}else{
					logger.error("get payment gateway service interface failed .....");
				}
			}
			return pgService;
		}
		
		
		public TokenPaymentResponseType getPGTokePaymentResult(TokenPaymentRequestType pgTokenReq){
			      
			   pgService = getPGService();
			   if(pgService != null){
				    try {
				    	return pgService.serviceToken(pgTokenReq);
				    }catch(Exception e) {
				    	logger.error("serviceToken failed");
				    	e.printStackTrace();
				    	return null;
				    }
			   }else{
				   return null;
			   }
			   
		}
		
		private boolean validateJobId(String info3) {
			
			try {
				if (info3 == null || info3.trim().length() == 0){
					return false;
				}
				else {
					//jobId must be all digits			
					Integer.parseInt(info3);
					
				}
			}catch(Exception e) {
				logger.error("invalid job Id");
		    	e.printStackTrace();	
				return false;
			}
			return true;
			
		}
		
		private boolean validateAmount(String amtStr) {
			
			try {
				
				if (amtStr == null || amtStr.trim().length() == 0){
					return false;
				}
				else {
					Float.parseFloat(amtStr);
					
				}
			}catch(Exception e) {
				logger.error("invalid amount");
		    	e.printStackTrace();	
				return false;
			}
			return true;
			
		}
		
		public String getUserName(TokenPaymentReq request){
			return getUserName(request.getSystemID(), null);
		}
}
