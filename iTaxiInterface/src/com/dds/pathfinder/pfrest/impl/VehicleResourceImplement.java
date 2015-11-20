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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/impl/VehicleResourceImplement.java $
 * 
 * PF-16385, 10/08/15, DZhou, add vehicle location request
 * 
 * ******/

package com.dds.pathfinder.pfrest.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.entities.VehicleState;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.callbooker.server.vehicle.ejb.InterfaceVehicle;
import com.dds.pathfinder.itaxiinterface.common.impl.BaseImplement;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.pfrest.resources.GpsLocation;
import com.dds.pathfinder.pfrest.resources.GpsLocationResponse;
import com.dds.pathfinder.pfrest.resources.ResponseResource;

public class VehicleResourceImplement extends BaseImplement {
	
	private static Logger logger = Logger.getLogger(VehicleResourceImplement.class);
	
	private DataSource pfDataSource;
	private int systemID;
	private InterfaceVehicle vehicle;
	
	public final static String NOT_FOUND_JOB_REF = "not found that job";
	public final static String ATTR_NAME_INPUT_ATTR_CHECK="input-attributes-validate";
	 
	
	public VehicleResourceImplement(InterfaceVehicle vehicle, DataSource pfDataSource, LoadDispatchParametersLocal cachedParam) {
		super();
		this.vehicle = vehicle;
		this.pfDataSource = pfDataSource;
		setCachedParam(cachedParam);
		this.systemID = -1;
	}

	public ResponseResource getGpsLocationRespByCallsign(String callsign, String strOldestValidDtm) {

		GpsLocationResponse gpsLocResponse = null;

		java.util.Date oldestValidDtm = Utilities.convertOSPDefaultDate(strOldestValidDtm);
		//	null is fine, but if date was provided but we could not parse a date from it then date format issue
		boolean isDateFormatIssue = strOldestValidDtm != null && !strOldestValidDtm.isEmpty() && oldestValidDtm == null;

		if (callsign != null && !callsign.isEmpty() && !isDateFormatIssue) {
			VehicleState vs = vehicle.requestVehicleLocationByCallsign(callsign, oldestValidDtm);

			if (vs != null) {
				gpsLocResponse = new GpsLocationResponse();
				GpsLocation location = new GpsLocation();

				if (vs.getLatitude() != null)
					location.setLatitude(vs.getLatitude().doubleValue());
				if (vs.getLongitude() != null)
					location.setLongitude(vs.getLongitude().doubleValue());
				if (vs.getPositionUpdateDtm() != null)
					location.setDateTime(Utilities.formatOSPDefaultDate(vs.getPositionUpdateDtm()));
				List<GpsLocation> locations = new ArrayList<GpsLocation>();
				locations.add(location);

				gpsLocResponse.setGpsLocations(locations);
				gpsLocResponse.setErrorMessage(ResponseResource.ERROR_CODE_SUCCESS);
				gpsLocResponse.setResponseCode(ResponseResource.STATUS_SUCCESS);
				gpsLocResponse.setHttpStatus(HttpServletResponse.SC_OK);
			}
		}

		if (gpsLocResponse == null) {
			gpsLocResponse = new GpsLocationResponse();
			gpsLocResponse.setErrorMessage(isDateFormatIssue ? ResponseResource.ERROR_CODE_INVALID_DATE_FORMAT : ResponseResource.ERROR_CODE_FAILED);
			gpsLocResponse.setResponseCode(ResponseResource.STATUS_FAILED);
			gpsLocResponse.setHttpStatus(HttpServletResponse.SC_NOT_FOUND);
		}

		return new ResponseResource(gpsLocResponse);
	}

	public String getLogonCode(){
		if(systemID >0) return getUserName(systemID, ""+systemID);
		else return ExternalSystemId.SYSTEM_ID_TRANSIT_GW.getLogonCode();  		//default TG
	}
	
	public String getReference(){
		if(systemID >0) return getSystemReference(systemID);
		else return ExternalSystemId.SYSTEM_ID_TRANSIT_GW.getReference();		//default TG
	}

	public int getSystemID() {
		return systemID;
	}

	public void setSystemID(int systemID) {
		this.systemID = systemID;
	}
}
