/****************************************************************************
 *
 *		   		    Copyright (c), 2010
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/DispRegionListImplement.java $
 * 
 * 5     2/12/11 10:01a Ezhang
 * C36130 Added system id validation.
 * 
 * 4     4/05/11 10:53a Mkan
 * C35249 fix, added to not return "VSR" region.
 * 
 * 3     9/20/10 2:11p Ezhang
 * OSP 2.0 Added Region's full name to the response
 * 
 * 2     2/12/10 5:15p Dchen
 * Modified for web booker using.
 * 
 * 1     1/26/10 5:42p Dchen
 * OSP interface.
 * 
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.DispatchRegionReq;
import com.dds.pathfinder.itaxiinterface.webservice.DispatchRegionRes;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.RegionListItem;

public class DispRegionListImplement extends OSPBaseImplement {

	private Logger logger = LogManager.getLogger(this.getClass());
	private DataSource pfDataSource;
	
	private final static String DEFAULT_STATE_NAME="BRITISH COLUMBIA";
	private final static String DEFAULT_COUNTRY_NAME="CANADA";
	
	public DispRegionListImplement(DataSource pfDataSource) {
		super();
		this.pfDataSource = pfDataSource;
	}

	public DispatchRegionRes generateResponse(BaseReq request) {
		DispatchRegionRes response = new DispatchRegionRes();
		//validate system id
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
		}
		if(!isValidRegionListRequest((DispatchRegionReq)request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
		}else{
			generateRegionListResponse((DispatchRegionReq)request, response);
		}
		return response;
	}
	
	private boolean isValidRegionListRequest(DispatchRegionReq request){
		return (request != null && request.getTaxiCoID() != null && request.getTaxiCoID() > 0);
	}
	
	private void generateRegionListResponse(DispatchRegionReq request, DispatchRegionRes response){
		String stateName = DEFAULT_STATE_NAME;
		String countryName = DEFAULT_COUNTRY_NAME;
		int taxiID = request.getTaxiCoID();
		CompanyDefaultValues.getDispatchParameters(pfDataSource, taxiID);
		HashMap<String, String> parameters = null;
		if(CompanyDefaultValues.DispatchParameters != null && CompanyDefaultValues.DispatchParameters.containsKey(taxiID)){
			parameters = CompanyDefaultValues.DispatchParameters.get(taxiID);
		}
		if(parameters != null 
				&& parameters.containsKey(CompanyDefaultValues.COMP_PARAMETER_C_DISPATCH_STATE_NAME)){
			stateName = parameters.get(CompanyDefaultValues.COMP_PARAMETER_C_DISPATCH_STATE_NAME);
		}
		if(parameters != null 
				&& parameters.containsKey(CompanyDefaultValues.COMP_PARAMETER_C_DISPATCH_COUNTRY_NAME)){
			countryName = parameters.get(CompanyDefaultValues.COMP_PARAMETER_C_DISPATCH_COUNTRY_NAME);
		}
		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
		getRegionsList(stateName, countryName, response);
	}
	
	private void getRegionsList(String stateName, String countryName, DispatchRegionRes response){
		Connection con = null;
		PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<RegionListItem> regionList = new ArrayList<RegionListItem>();
        try{
        	con = pfDataSource.getConnection();
        	//String query = "select region_name from street_lookup_regions ";
        	String query = "select region_abbreviation, region_name from street_lookup_regions where region_abbreviation <> 'VSR'"; //"virtual stand" region should never be available for dispatch
        	
        	stmt = con.prepareStatement(query);
        	rs = stmt.executeQuery();
        	
        	while(rs.next()){
        		String regionFullName = rs.getString("region_name");
        		String regionName = rs.getString("region_abbreviation");
        		regionList.add(new RegionListItem(regionName, regionFullName, stateName, countryName));
        	}
        }catch(SQLException se){
        	logger.error("getRegionsList failed: ", se);
        }finally{
        	if(rs != null) try{rs.close();}catch(SQLException ignore){};
        	if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
        	if(con != null) try{con.close();}catch(SQLException ignore){};
        }
        
        int nbOfRegions = regionList.size();
        if(nbOfRegions > 0){
        	response.setNumberOfRec(nbOfRegions);
        	RegionListItem[] regions = new RegionListItem[nbOfRegions];
        	regionList.toArray(regions);
        	response.setRegionList(regions);
        }
	}

}
