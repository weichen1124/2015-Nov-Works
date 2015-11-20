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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/service/BaseResourceEJB.java $
* 
* PF-16385, 10/08/15, DZhou, add vehicle location request
* 
* **************************************/
package com.dds.pathfinder.pfrest.service;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.callbooker.server.vehicle.ejb.VehicleLocal;
import com.dds.pathfinder.itaxiinterface.common.impl.BaseImplement;
import com.dds.pathfinder.pfrest.impl.PFRestImplement;
import com.dds.pathfinder.pfrest.impl.VehicleResourceImplement;
import com.dds.pathfinder.pfrest.resources.GpsLocationResponse;
import com.dds.pathfinder.pfrest.resources.ResponseResource;

public abstract class BaseResourceEJB {
	
	private static Logger logger = Logger.getLogger(BaseResourceEJB.class);
	  
	@Resource(mappedName =com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_PF_DATA_SOURCE)
    protected DataSource pfDataSource;   
	  
	protected int validSystemID(HttpHeaders headers, BaseImplement resourceImplement) {
		int systemID = -1;
		if (headers == null || resourceImplement == null)
			return systemID;
		if (headers.getRequestHeader(PFRestImplement.PFREST_HTTP_HEADER_SYSTEM_ID) != null
				&& headers.getRequestHeader(PFRestImplement.PFREST_HTTP_HEADER_SYSTEM_ID).size() > 0) {
			try {
				systemID = Integer.parseInt(headers.getRequestHeader(
						PFRestImplement.PFREST_HTTP_HEADER_SYSTEM_ID).get(0));
				String password = (headers
						.getRequestHeader(PFRestImplement.PFREST_HTTP_HEADER_PASSWORD) == null || headers
						.getRequestHeader(PFRestImplement.PFREST_HTTP_HEADER_PASSWORD).size() == 0) ? null
						: headers.getRequestHeader(PFRestImplement.PFREST_HTTP_HEADER_PASSWORD)
								.get(0);
				if (!resourceImplement.validateSystemId(pfDataSource, systemID, password))
					return -1;
			} catch (NumberFormatException ne) {
				logger.error("validSystemID with invalid system id");
			}
		}
		return systemID;
	}
}
