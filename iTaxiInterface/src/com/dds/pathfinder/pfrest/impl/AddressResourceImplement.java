/****************************************************************************
 *
 *		   		    Copyright (c), 2015
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/impl/AddressResourceImplement.java $
 * 
 * PF-16819, 10/05/15, DChen, added address driver notes resource.
 *
 * ******/
package com.dds.pathfinder.pfrest.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.common.impl.CallbookerCartImplement;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;
import com.dds.pathfinder.pfrest.resources.DriverNotesData;
import com.dds.pathfinder.pfrest.resources.DriverNotesResource;

public class AddressResourceImplement extends CallbookerCartImplement {
	
	private static Logger logger = Logger.getLogger(AddressResourceImplement.class);
	
	private DataSource pfDataSource;
	
	public AddressResourceImplement(DataSource pfDataSource, LoadDispatchParametersLocal cachedParam){
		super();
		this.pfDataSource = pfDataSource;
		setCachedParam(cachedParam);
	}
	
	private boolean isEmptyInputString(String inputStr){
		return (inputStr == null || inputStr.trim().isEmpty());
	}
	
    public DriverNotesResource getAddressDriverNotes(int companyID, String streetName, String streetNumber, String unitNb, String regionAbbr){
    	DriverNotesResource drvNotesResource = new DriverNotesResource();
    	
    	if(pfDataSource == null){
    		drvNotesResource.setHttpStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		return drvNotesResource;
    	}
    	
    	String strName = Utilities.upcaseString(streetName);
    	String strNumber = Utilities.upcaseString(streetNumber);
    	String unit = Utilities.upcaseString(unitNb);
    	String region = Utilities.upcaseString(regionAbbr);
    	
    	if(isEmptyInputString(strName) || isEmptyInputString(strNumber) || isEmptyInputString(region) || companyID <= 0){
    		drvNotesResource.setHttpStatus(HttpServletResponse.SC_BAD_REQUEST);
    		return drvNotesResource;
    	}
    	
    	AddressItem address = getPFAddressItem(companyID, strName, strNumber, unit, region);
    	
    	if(address == null){
    		drvNotesResource.setHttpStatus(HttpServletResponse.SC_NOT_FOUND);
    		return drvNotesResource;
    	}
 
    	
    	Connection con = null;
 	   	CallableStatement cs = null;
		try{	
			con = pfDataSource.getConnection();
			
	       	cs = con.prepareCall("{ call address_decarta.get_address_notes(?,?,?,?,?,?,?,?,?,?)}");
	       	cs.setInt(1, address.getAddressId());
	       	cs.setInt(2, address.getAreaId());
	       	cs.registerOutParameter(3, java.sql.Types.VARCHAR);
		    cs.registerOutParameter(4, java.sql.Types.VARCHAR);
		    cs.registerOutParameter(5, java.sql.Types.VARCHAR);
		    cs.registerOutParameter(6, java.sql.Types.VARCHAR);
		    cs.registerOutParameter(7, java.sql.Types.VARCHAR);
		    cs.registerOutParameter(8, java.sql.Types.VARCHAR);
		    cs.registerOutParameter(9, java.sql.Types.VARCHAR);
		    cs.registerOutParameter(10, java.sql.Types.VARCHAR);
		    
		    logger.info("call get_address_notes with input: " + address.getAddressId() +", " + address.getAreaId());
	       	
		    cs.execute ();
		    
		    String addrDrvNotes = cs.getString(3);
		    String buldDrvNotes = cs.getString(4);
		    String blfcDrvNotes = cs.getString(5);
		    String areaDrvNotes = cs.getString(6);
		    String addrOprNotes = cs.getString(7);
		    String buldOprNotes = cs.getString(8);
		    String blfcOprNotes = cs.getString(9);
		    String areaOprNotes = cs.getString(10);
	       	
	       	drvNotesResource.setDataResource(new DriverNotesData(addrDrvNotes, buldDrvNotes, blfcDrvNotes, areaDrvNotes));
	       	DriverNotesData notesData = (DriverNotesData) drvNotesResource.getDataResource();
	       	if(notesData != null){
	       		logger.info("drvNotesResource, get data=" + notesData.getAddrDriverNotes() +", " + notesData.getLandMarkDriverNotes() +", " + notesData.getBlockFcDriverNotes() +", " + notesData.getAreaDriverNotes());
	       	}else{
	       		logger.info("drvNotesResouce data is null.....");
	       	}
		}catch(SQLException e){
			logger.error("generateJobsResponse failed....");
			drvNotesResource.setHttpStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}finally{
			if(cs != null) try{cs.close();}catch(SQLException ignore){};
			if(con != null) try{con.close();}catch(SQLException ignore){};
		}
		return drvNotesResource;
    }
    
    private AddressItem getPFAddressItem(int companyID, String strName, String strNumber, String unit, String region){
    	
    	boolean unitPercentage = (unit == null || unit.trim().isEmpty()); //no unit provided
    	String orderBy = getPreDefinedOrderBy();
		PFAddressResponse pickupAddress = getWSAddressData(
				   appendPercentage(strName, false), 
				   appendPercentage(strNumber, false),
				   //appendPercentage(pickup.getUnitNumber(), true), //except for unit here, we want to use pe/rcentage to match
				   appendPercentage(unit, unitPercentage), //except for unit here, we want to use pe/rcentage to match
				   appendPercentage(region, false), 
				   PERCENTAGE_CHAR, 
				   PERCENTAGE_CHAR, PERCENTAGE_CHAR,  //organization and postal code
				   companyID, orderBy);
		
		
		if(!isValidAddressResponse(pickupAddress)) return null;
		PFAddressData addressData = getMatchAddressWithUnit(pickupAddress, strName, strNumber, region, unit);
		
		logger.info("getMatchAddressWithUnit returns: ");
		
		AddressItem addressItem = null;
		if(addressData != null){
			logger.info("getPFAddressItem getMatchAddressWithUnit returns: not null");
			addressItem = getAddressItem(addressData, companyID);
			logger.info("addressItem, returned: address id =" + addressItem.getAddressId() +", area id=" + addressItem.getAreaId());
			if(addressItem == null || addressItem.getAddressId() <= 0 || addressItem.getAreaId() <= 0){
				return null;
			}
		}
		return addressItem;
    }
    
    protected AddressItem getAddressItem(PFAddressData addressData, int taxiCoID){
		Connection con = null;
		try{
			con = pfDataSource.getConnection();
			return getAddressItem(con, addressData, taxiCoID);
		}catch(SQLException se){
			logger.error("getAddressItem failed", se);
			return null;
		}finally{
			if(con != null) try{con.close();}catch(SQLException ignore){};
		}
	}
}
