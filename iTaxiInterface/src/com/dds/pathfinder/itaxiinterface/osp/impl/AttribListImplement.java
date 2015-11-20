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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/AttribListImplement.java $
 * 
 * PF-15816, 15/07/14 DChen, added taxi company ID in attributes list.
 * 
 * 6     2/12/11 10:00a Ezhang
 * 
 * 5     8/19/11 3:37p Dchen
 * port C34861, 34862, 35460 here.
 * 
 * 4     9/20/10 2:06p Ezhang
 * OSP 2.0 added support of errorCode.
 * 
 * 3     1/26/10 5:43p Dchen
 * OSP interface.
 * 
 * 2     1/21/10 11:39a Dchen
 * OSP interface.
 * 
 * 1     1/13/10 6:20p Dchen
 * OSP interface.
 * 
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;
import com.dds.pathfinder.itaxiinterface.common.impl.BaseImplement;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.GetAttrListRequest;
import com.dds.pathfinder.itaxiinterface.webservice.GetAttrListResponse;
import com.dds.pathfinder.itaxiinterface.webservice.PFAttrListItem;

public class AttribListImplement extends OSPBaseImplement {

	private Logger logger = LogManager.getLogger(this.getClass());
	
    private DataSource pfDataSource;
    
//    public static HashMap<String, SysAttrListItem> DriverAttributesMap;
//    public static HashMap<String, SysAttrListItem> VehicleAttributesMap;
	private enum AttribListErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_SESSIONID(2),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private AttribListErrorCode(int c) {
			   code = c;
		}

		public int getCode() {
			   return code;
		}
	}
    
	public AttribListImplement(DataSource pfDataSource) {
		super();
		this.pfDataSource = pfDataSource;
	}

	public GetAttrListResponse generateResponse(BaseReq request) {
		
		return getAllAttributes((GetAttrListRequest)request);
	}
	
//	private GetAttrListResponse getAllAttributes(GetAttrListRequest request){
//		GetAttrListResponse response = new GetAttrListResponse();
//        //C36130 Validate system id
//		if(!validateSystemId(pfDataSource, request)){
//			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
//			response.setErrorCode(AttribListErrorCode.NOT_AUTHENTICATED.getCode());
//			return response;
//		}
//        CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
//        ArrayList<SysAttrListItem> attrList = new ArrayList<SysAttrListItem>();
//        for(int taxiID : CompanyDefaultValues.TaxiCompanies.keySet()){
//	        if(CompanyDefaultValues.DriverAttributesMap != null && CompanyDefaultValues.DriverAttributesMap.size() > 0){
//	        		attrList.addAll(CompanyDefaultValues.DriverAttributesMap.get(taxiID).values());
//	        }
//	        if(CompanyDefaultValues.VehicleAttributesMap != null && CompanyDefaultValues.VehicleAttributesMap.size() > 0){
//	        	attrList.addAll(CompanyDefaultValues.VehicleAttributesMap.get(taxiID).values());
//	        }
//        }
//        setAttrListResponse(response, attrList);
//        
//        return response;
//	}
	
	private GetAttrListResponse getAllAttributes(GetAttrListRequest request){
		GetAttrListResponse response = new GetAttrListResponse();
        //C36130 Validate system id
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(AttribListErrorCode.NOT_AUTHENTICATED.getCode());
			return response;
		}
		if(cachedParam == null ) {
			logger.error("getAllAttributes failed, can't find cached parameters.........");
			return response;
		}
		
        HashMap<Integer, String> allCompanys = cachedParam.getTaxiCompanies();
        if(allCompanys == null || allCompanys.size() == 0) {
        	logger.error("getAllAttributes failed, can't find any PF companies .........");
        	return response;
        }
        
        ArrayList<PFAttrListItem> attrList = new ArrayList<PFAttrListItem>();
        for(int taxiID : allCompanys.keySet()){
        	HashMap<Integer, HashMap<String, SysAttrListItem>> attributes = cachedParam.getDrvAttributeMap();
	        if(attributes != null && attributes.size() > 0){
	        		attrList.addAll(convertToPFAttrList(attributes.get(taxiID).values()));
	        }
	        attributes = cachedParam.getVehAttributeMap();
	        if(attributes != null && attributes.size() > 0){
	        	attrList.addAll(convertToPFAttrList(attributes.get(taxiID).values()));
	        }
        }
        setAttrListResponse(response, attrList);
        
        return response;
	}
	
	public ArrayList<PFAttrListItem> convertToPFAttrList(Collection<SysAttrListItem> sysAttrList){
		ArrayList<PFAttrListItem> attrList = new ArrayList<PFAttrListItem>();
		for(SysAttrListItem sysAttr : sysAttrList){
			attrList.add(new PFAttrListItem(sysAttr));
		}
		return attrList;
	}
	
	private void setAttrListResponse(GetAttrListResponse response, ArrayList<PFAttrListItem> attrList){
		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
    	response.setErrorCode(AttribListErrorCode.NO_ERROR.getCode());
    	int nbOfAttributes = attrList.size();
    	response.setNofAttributes(nbOfAttributes);
    	if(nbOfAttributes > 0){
    		PFAttrListItem[] attrArray = new PFAttrListItem[nbOfAttributes];
    		attrList.toArray(attrArray);
    		response.setListOfAttributes(attrArray);
    	}
	}
	
//	public static void getDefaultAttributesMaps(DataSource dataSource){
//		if(DriverAttributesMap != null && VehicleAttributesMap != null) return;
//		if(dataSource != null){
//			Connection con = null;
//			PreparedStatement stmt = null;
//	        ResultSet rs = null;
//	        
//	        try{
//	        	con = dataSource.getConnection();
//	        	stmt = con.prepareStatement( "select ar.BIT_POSITION, ar.ATTRIB_TYPE, ar.DESCRIPTION from attributes ar where ar.KILLED_FLAG ='N'" );
//	        	rs = stmt.executeQuery();
//	        	DriverAttributesMap = new HashMap<String, SysAttrListItem>();
//	        	VehicleAttributesMap = new HashMap<String, SysAttrListItem>();
//	        	while(rs.next()){
//	        		String bitPosition = rs.getString("BIT_POSITION");
//	                String attribType = rs.getString("ATTRIB_TYPE");
//	                String description = rs.getString("DESCRIPTION");
//	                String shortName = attribType+"_"+bitPosition;
//	                if("D".equalsIgnoreCase(attribType)){
//	                	DriverAttributesMap.put(bitPosition, new SysAttrListItem(shortName, description,"Y"));
//	                }else{
//	                	VehicleAttributesMap.put(bitPosition, new SysAttrListItem(shortName, description,"Y"));
//	                }                
//	        	}
//	
//	        }catch(SQLException se){
//	        	se.printStackTrace();
//	        }finally{
//	        	if(rs != null) try{rs.close();}catch(SQLException ignore){};
//	        	if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
//	        	if(con != null) try{con.close();}catch(SQLException ignore){};
//	        }
//		}
//	}
	
	
}
