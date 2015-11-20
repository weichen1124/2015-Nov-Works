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
 * added filter for user account's account_status and allow booking flag for account list.
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/AccountListImplement.java $
 * 
 * 5     2/12/11 9:53a Ezhang
 * C36130 added system id validation.
 * 
 * 4     11/10/10 10:18a Ezhang
 * Exclude account that not allowed booking and account status inactive ones.
 * 
 * 3     9/20/10 2:02p Ezhang
 * OSP 2.0 added errorCode support.
 * 
 * 2     4/15/10 11:10a Mkan
 * - getListAttributes(): changed to use account number instead of account
 * name to get list of accounts
 *   		       (OSP user's account code is in essence PF's account card
 * number)
 * 
 * 1     1/13/10 6:20p Dchen
 * OSP interface.
 * 
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.GetAcctListRequest;
import com.dds.pathfinder.itaxiinterface.webservice.GetAcctListResponse;
import com.dds.pathfinder.itaxiinterface.webservice.MatchAccountListItem;


public class AccountListImplement extends OSPBaseImplement {

	private Logger logger = LogManager.getLogger(this.getClass());
	
    private DataSource pfDataSource;
    
    private enum AccountListErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_SESSIONID(2),
		NO_MATCH_ACCOUNT(3),
		INVALID_COM_ID(4),
		INVALID_ACCT_CODE(5),
		INVALID_ACCT_NAME(6),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private AccountListErrorCode(int c) {
			   code = c;
		}

		public int getCode() {
			   return code;
		}
	}
	public AccountListImplement(DataSource pfDataSource) {
		super();
		this.pfDataSource = pfDataSource;
	}


	public GetAcctListResponse generateResponse(BaseReq request) {
		return getListAttributes((GetAcctListRequest)request);
	}
	
	private GetAcctListResponse getListAttributes(GetAcctListRequest request){
		GetAcctListResponse response = new GetAcctListResponse();
		response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
    	response.setErrorCode(AccountListErrorCode.DEFAULT_ERROR.getCode());
		if(!validGetAcctListRequest(request)){
			return response;
		}
		//validate system id
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(AccountListErrorCode.NOT_AUTHENTICATED.getCode());
			return response;
		}
		Connection con = null;
		PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	con = pfDataSource.getConnection();
        	String query = "select acct_card_nr, ca.name, acct_card_pin from account_users au, customer_accounts ca, profiles p1 \n"
        					+" where au.account_id = ca.account_id \n"
        					+" and ca.allow_bookings_flag = 'Y' \n"
        					+" and NVL(p1.account_status, 'A') = 'A' \n"
        					+" and au.profile_id = p1.profile_id \n"
        					+" and p1.parent_profile_id in \n" 
        					+" (select p2.profile_id from profiles p2 where p2.parent_profile_id in \n"
      					    +" 	(select p3.profile_id from profiles p3,taxi_companies tc \n" 
      					    +"    where p3.parent_profile_id = tc.profile_id and tc.taxi_co_id = "+ request.getTaxiCoID() +")) \n";
        	
        	String accountCode = request.getAccountCode();
        	String accountName = request.getAccountName();
        	if(accountCode != null && accountCode.length() > 0) query += " and acct_card_nr like '"+accountCode+"%' \n";
        	if(accountName != null && accountName.length() > 0) query += " and name like '"+accountName+"%' \n";
        	query += " order by acct_card_nr \n";
        	
        	stmt = con.prepareStatement(query);
        	rs = stmt.executeQuery();
        	ArrayList<MatchAccountListItem> acctList = new ArrayList<MatchAccountListItem>();
        	while(rs.next()){
        		
        		String actCode = rs.getString("acct_card_nr"); //OSP user's account code is in essence PF's account card number
        		String actName = rs.getString("name"); //PF's account name
        		String password = rs.getString("acct_card_pin");
        		acctList.add(new MatchAccountListItem(actCode, actName, password));
        	}
        	generateAcctListResponse(response, acctList);
        }catch(SQLException se){
        	logger.error("getListAttributes failed:", se);
        }finally{
        	if(rs != null) try{rs.close();}catch(SQLException ignore){};
        	if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
        	if(con != null) try{con.close();}catch(SQLException ignore){};
        }
        return response;
		
	}
	
	private boolean validGetAcctListRequest(GetAcctListRequest request){
		if(request == null || request.getTaxiCoID() == null || request.getTaxiCoID() <= 0) return false;
		String accountCode = request.getAccountCode();
		String accountName = request.getAccountName();
		return (accountCode != null && accountCode.length() > 0 ) 
				|| (accountName != null && accountName.length() > 0); 
		
	}
	
	private void generateAcctListResponse(GetAcctListResponse response, ArrayList<MatchAccountListItem> acctList){
		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
    	response.setErrorCode(AccountListErrorCode.NO_ERROR.getCode());
    	int nbOfAccounts = acctList.size();
    	response.setNofAccounts(nbOfAccounts);
    	if(nbOfAccounts > 0){
    		MatchAccountListItem[] acctArray = new MatchAccountListItem[nbOfAccounts];
    		acctList.toArray(acctArray);
    		response.setListOfAccounts(acctArray);
    	}
	}
}
