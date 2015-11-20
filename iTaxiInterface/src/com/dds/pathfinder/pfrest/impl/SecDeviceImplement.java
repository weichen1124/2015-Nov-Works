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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/impl/SecDeviceImplement.java $
* 
* PF-16760, 09/14/15, DChen, change street hire to rejectafteraccept.
* 
* PF-16760, 09/10/15, DChen, reject for street hire.
* 
* PF-16606, 09/03/15, DChen, validate driver issue.
* 
* PF-16606, 09/03/15, DChen, add some timeout in job offer.
* 
* PF-16605, 08/28/15, DChen, some changes required from MG.
* 
* PF-16607, 08/21/15, DChen, add event id and use diff url to notify
* 
* PF-16607, 08/19/15, DChen, accept reject from 2nd device.
* 
* PF-16605, 06/18/15, DChen, created for 2nd device.
* 
* **************************************/
package com.dds.pathfinder.pfrest.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.driver.ejb.Driver;
import com.dds.pathfinder.callbooker.server.entities.DriverEntity;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.common.impl.BaseImplement;
import com.dds.pathfinder.pfrest.resources.ResponseResource;
import com.dds.pathfinder.pfrest.seconddevices.DeviceResource;
import com.dds.pathfinder.pfrest.seconddevices.SecondDeviceResponse;
import com.dds.pathfinder.pfrest.seconddevices.SecondDevice;

public class SecDeviceImplement extends BaseImplement {
	private static Logger logger = Logger.getLogger(SecDeviceImplement.class);
	
	protected Driver driver;
	
	private DataSource pfDataSource;
	private int systemID;
	
	public final static String ACCEPT_JOB_SEC_DEVICE = "E";
	public final static String REJECT_JOB_SEC_DEVICE = "K";
	public final static String TIMEOUT_JOB_SEC_DEVICE = "O";
	public final static String REJEPT_JOB_SEC_DEVICE = "D";
	
	
	public SecDeviceImplement(DataSource pfDataSource, LoadDispatchParametersLocal cachedParam, Driver driver) {
		super();
		
		this.pfDataSource = pfDataSource;
		setCachedParam(cachedParam);
		this.systemID = -1;
		this.driver = driver;
	}
	
	
	public DeviceResource getDriverByBadgeNb(String badgeNb){
		DeviceResource drvResource = new DeviceResource();
		if(driver == null) {
			drvResource.setHttpStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return drvResource;
		}
		try{
			DriverEntity aDriver = driver.findDriverByBadgeNumberOnly(badgeNb);
			if(aDriver != null){
				drvResource.setHttpStatus(HttpServletResponse.SC_OK);
				convertDriver2Resource(aDriver, drvResource);
			}else{
				drvResource.setHttpStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		}catch(Exception e){
			logger.error("getDriverByBadgeNb failed..");
		}
		
		return drvResource;
		
	}
	
	
	private void convertDriver2Resource(DriverEntity aDriver, DeviceResource drvResource){
		if(drvResource.getDataResource() == null) drvResource.setDataResource(new SecondDevice());
		SecondDevice secDevice = drvResource.getDataResource();
		secDevice.setBadgeNumber(aDriver.getBadgeNr());
		secDevice.setDriverPin(aDriver.getDriverPin());
		secDevice.setDataID(aDriver.getSecondaryDeviceID());
	}
	
	
	public ResponseResource register2ndDevice(DeviceResource deviceResource, int companyID){
		SecondDeviceResponse response = new SecondDeviceResponse();
		response.setErrorMessage(ResponseResource.ERROR_CODE_FAILED);
		response.setResponseCode(ResponseResource.STATUS_FAILED);
		response.setHttpStatus(HttpServletResponse.SC_BAD_REQUEST);
		DriverEntity driver = validateDriver4DeviceSource(deviceResource, response);
		if(driver == null) {
			response.setHttpStatus(HttpServletResponse.SC_NOT_FOUND);
			return new ResponseResource(response);
		}
		SecondDevice secDevice = deviceResource.getDataResource();
		
		Connection con = null;
		CallableStatement cs = null;
    	try{
    		con = pfDataSource.getConnection();
    		cs = con.prepareCall("{call pf_rest_event.register_driver_2nd_device(?,?)}");	
    		cs.setLong(1, driver.getDriverId());
    		cs.setString(2, secDevice.getDataID());
    		cs.execute();
    	}catch(SQLException se){
    		logger.error("register2ndDevice failed with sql exception: ", se);
    		return new ResponseResource(response);
    	}finally{
    		if(cs != null) try{cs.close();}catch(SQLException ignore){};
    		if(con != null) try{con.close();}catch(SQLException ignore){};
    	}
    	
    	response.setErrorMessage(ResponseResource.ERROR_CODE_SUCCESS);
    	response.setResponseCode(ResponseResource.STATUS_SUCCESS);
    	response.setHttpStatus(HttpServletResponse.SC_OK);
    	response.setDriverName(driver.getDriverName());
    	response.setCompanyName((cachedParam == null) ? null : cachedParam.getTaxiCompanyName(companyID));
    	return new ResponseResource(response);
	}
	
	public ResponseResource actionJob2ndDevice(long jobID, int companyID, DeviceResource deviceResource, String actionSecDevice){
		SecondDeviceResponse response = new SecondDeviceResponse();
		response.setErrorMessage(ResponseResource.ERROR_CODE_FAILED);
		response.setResponseCode(ResponseResource.STATUS_FAILED);
		response.setHttpStatus(HttpServletResponse.SC_BAD_REQUEST);
		DriverEntity driver = validateDeviceSourceJobOffer(jobID, companyID, deviceResource, response);
		if( driver == null ) return new ResponseResource(response);
		
		// SecondDevice secDevice = deviceResource.getDataResource();
		
		Connection con = null;
		CallableStatement cs = null;
    	try{
    		con = pfDataSource.getConnection();
    		cs = con.prepareCall("{call pf_rest_event.reply_job_offer_2nd_device(?,?,?)}");	
    		cs.setLong(1, driver.getDriverId());
    		cs.setLong(2, jobID);
    		cs.setString(3, actionSecDevice);
    		cs.execute();
    	}catch(SQLException se){
    		logger.error("actionJob2ndDevice failed with sql exception: ", se);
    		return new ResponseResource(response);
    	}finally{
    		if(cs != null) try{cs.close();}catch(SQLException ignore){};
    		if(con != null) try{con.close();}catch(SQLException ignore){};
    	}
    	
    	response.setErrorMessage(ResponseResource.ERROR_CODE_SUCCESS);
    	response.setResponseCode(ResponseResource.STATUS_SUCCESS);
    	response.setHttpStatus(HttpServletResponse.SC_OK);
    	return new ResponseResource(response);
	}
	
	
	public DriverEntity validateDriver4DeviceSource(DeviceResource deviceResource, SecondDeviceResponse response){
		if(deviceResource == null || deviceResource.getDataResource() == null || !(deviceResource.getDataResource() instanceof SecondDevice)) return null;
		SecondDevice device = deviceResource.getDataResource();
		if(device.getBadgeNumber() == null || device.getBadgeNumber().trim().length() == 0 || device.getDataID() == null || device.getDataID().trim().length() == 0) return null;
		
		DriverEntity aDriver = null;
		try{
			aDriver = driver.findDriverByBadgeNumberOnly(device.getBadgeNumber());
		}catch(Exception e){
			logger.error("isValidDeviceSource to find driver by bade number failed: " + device.getBadgeNumber());
			return null;
		}
		
		if(aDriver == null){
			response.setErrorMessage(ResponseResource.ERROR_CODE_DRIVER_NOT_FOUND);
			return null;
		}
		
		if(aDriver.getDriverPin() != null && !aDriver.getDriverPin().equals(device.getDriverPin())){
			response.setErrorMessage(ResponseResource.ERROR_CODE_INVALID_DRIVER_PIN);
			return null;
		}
		
		return aDriver;
		
	}
	
	
	public DriverEntity validateDeviceSourceJobOffer(long jobID, int companyID, DeviceResource deviceResource, SecondDeviceResponse response){
		if(jobID <= 0 || companyID <= 0 || deviceResource == null || deviceResource.getDataResource() == null || !(deviceResource.getDataResource() instanceof SecondDevice)) return null;
		
		SecondDevice device = deviceResource.getDataResource();
		if( device.getDataID() == null || device.getDataID().trim().length() == 0 || device.getBadgeNumber() == null || device.getBadgeNumber().trim().length() == 0) return null;
		
		
		DriverEntity aDriver = null;
		try{
			aDriver = driver.findByBadgeNumber(device.getBadgeNumber(), companyID);
			if(aDriver != null){
				if(device.getDataID().equals(aDriver.getSecondaryDeviceID())) return aDriver;
				else return null;
			}else{
				response.setErrorMessage(ResponseResource.ERROR_CODE_DRIVER_NOT_FOUND);
				return null;
			}
//			else{
//				aDriver = driver.findDriverBySecDeviceIDOnly(device.getDataID());
//			}
			
		}catch(Exception e){
			logger.error("validateDeviceSourceJobOffer to find driver by second device ID: " + device.getDataID());
			return null;
		}

		
	}

	public DataSource getPfDataSource() {
		return pfDataSource;
	}

	public void setPfDataSource(DataSource pfDataSource) {
		this.pfDataSource = pfDataSource;
	}

	public int getSystemID() {
		return systemID;
	}

	public void setSystemID(int systemID) {
		this.systemID = systemID;
	}



	public Driver getDriver() {
		return driver;
	}



	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	
}
