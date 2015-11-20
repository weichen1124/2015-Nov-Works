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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/impl/JobResourceImplement.java $
 * 
 * PF-16614, 06/19/15, DChen, add suti particular stop points resource.
 * 
 * 
 * ******/


package com.dds.pathfinder.pfrest.impl;

import java.util.Date;
import java.util.Vector;

import javax.sql.DataSource;

import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.cart.model.CartItem;
import com.dds.pathfinder.callbooker.server.cart.model.StopPoint;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.common.impl.BaseImplement;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.pfrest.resources.Job;
import com.dds.pathfinder.pfrest.resources.StopPointResource;

public class PFSutiJobResourceImplement extends BaseImplement {
	
	private static Logger logger = Logger.getLogger(PFSutiJobResourceImplement.class);
	
	private DataSource pfDataSource;
	
	public static final String PF_REST_SUTI_ATTRIBUTE_TRANSPORT_ID = "suti-transport-id-digits";

	public PFSutiJobResourceImplement(DataSource pfDataSource, LoadDispatchParametersLocal cachedParam) {
		super();
		this.pfDataSource = pfDataSource;
		setCachedParam(cachedParam);
	}
	
	public void generateCartItems(Vector<CartItem> carts, Job job){
		if(carts == null || carts.size() == 0 || job == null) return;
		int lastDigits = getPFRestServiceAttributeAsInt(PF_REST_SUTI_ATTRIBUTE_TRANSPORT_ID, 7);
		String externalID = job.getExternalJobID();
		if(externalID == null || externalID.trim().length() < lastDigits) return;
		
		String transportID = externalID.substring(externalID.length() - lastDigits);
		carts.get(0).getStopPoint(0).getContactInfo().setReference(transportID);
		for(CartItem cart : carts){
			setCartStopPoint(cart, job);
		}
	}
	
	private void setCartStopPoint(CartItem cartItem, Job job){	
			// Connection con = null;
			Vector<StopPoint> stopPoints = cartItem.getStopPoints();
			if(stopPoints != null){
				try{
					for(int i=0; i< stopPoints.size(); i++){
						StopPointResource spResource = job.getSubRoute().getStopPoints().get(i);
						StopPoint stopPoint = cartItem.getStopPoint(i);
						stopPoint.setCustomerNumber(spResource.getCustomerNumber());
						if(spResource.getRequiredDTM() != null && spResource.getRequiredDTM().trim().length() > 0){
							Date requiredDTM = Utilities.convertOSPDefaultDate(spResource.getRequiredDTM());
							if(requiredDTM != null) stopPoint.setRequiredPickupDTM(requiredDTM);
						}
						stopPoint.setOperatorNotes(spResource.getOperatorNotes());
					}
				}catch(IndexOutOfBoundsException e){
					logger.error("setCartStopPoint failed with stop points index out of bounds, and returned. ");
				}
			}
	}

}
