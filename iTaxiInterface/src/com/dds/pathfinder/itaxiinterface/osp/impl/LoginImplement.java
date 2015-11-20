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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/LoginImplement.java $
 * 
 * 4     2/12/11 10:01a Ezhang
 * C36130 Added system id validation.
 * 
 * 3     1/19/10 4:44p Dchen
 * OSP interface.
 * 
 * 2     1/13/10 6:19p Dchen
 * OSP interface.
 * 
 * 1     1/11/10 1:32p Dchen
 * OSP interface.
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.CompanyListItem;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.LoginRequest;
import com.dds.pathfinder.itaxiinterface.webservice.LoginResponse;

public class LoginImplement extends OSPBaseImplement {

	private Logger logger = LogManager.getLogger(this.getClass());
	
    private DataSource pfDataSource;
    
    
    
	public LoginImplement(DataSource pfDataSource) {
		super();
		this.pfDataSource = pfDataSource;
	}

	public LoginResponse generateResponse(BaseReq request) {
		return generateLoginResponse((LoginRequest)request);
	}
	
	private LoginResponse generateLoginResponse(LoginRequest request){
		LoginResponse response = new LoginResponse();
		//validate system id
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
		}
		if(!isValidateLoginRequest(request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			return response;
		}else{
			if(isValidUserPassword(request)){
				response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
		    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
		    	response.setSessionID(request.getUserName());
				getAssignedCompanies(request.getUserName(), response);
			}
		}
		
		return response;
	}
	
	
	private boolean isValidUserPassword(LoginRequest request){
		Connection con = null;
		CallableStatement cs= null;
		int result = GenErrMsgRes.STATUS_FAILED;			//invalid 1;
		try{			
			con = pfDataSource.getConnection();
			cs = con.prepareCall("{ call open_service_platform.validate_password(?,?,?) }");
			cs.setString(1, request.getUserName());
            cs.setString(2, request.getPassword());
            cs.registerOutParameter(3, java.sql.Types.INTEGER);
            cs.executeUpdate();
            result = cs.getInt(3);
		}catch(SQLException se){
			logger.error("isValidUserPassword failed...." , se);
		}finally{
			if(cs != null) try{cs.close();}catch(SQLException ignore){};
			if(con != null) try{con.close();}catch(SQLException ignore){};
		}
		return (result == GenErrMsgRes.STATUS_SUCCESS);
	}
	
	
	private void getAssignedCompanies(String userName, LoginResponse response){
		Connection con = null;
		PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	con = pfDataSource.getConnection();
        	stmt = con.prepareStatement("select taxi_co_id from taxi_co_users t where t.logon_code = '" + 
        								userName + "' and t.relation_type='A'");
        	rs = stmt.executeQuery();
        	
        	ArrayList<CompanyListItem> compArray = new ArrayList<CompanyListItem>();
        	while(rs.next()){
        		int taxiCoID = rs.getInt("taxi_co_id");
        		compArray.add(new CompanyListItem(taxiCoID));
        	}
        	int nbCompany = compArray.size();
        	if(nbCompany > 0){
        		CompanyListItem[] compList = new CompanyListItem[nbCompany];
        		compArray.toArray(compList);
        		response.setAuthCompList(compList);
        	}
        }catch(SQLException se){
        	logger.error("getAssignedCompanies failed:", se);
        }finally{
        	if(rs != null) try{rs.close();}catch(SQLException ignore){};
        	if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
        	if(con != null) try{con.close();}catch(SQLException ignore){};
        } 
	}
	
	private boolean isValidateLoginRequest(LoginRequest request){
		if(request == null || request.getUserName() == null || request.getUserName().trim().length() == 0
				|| request.getPassword() == null || request.getPassword().trim().length() == 0) return false;
		request.setPassword(Utilities.MungePassword(request.getPassword()));  //encrypt password
		return true;
	}
	
	
}
