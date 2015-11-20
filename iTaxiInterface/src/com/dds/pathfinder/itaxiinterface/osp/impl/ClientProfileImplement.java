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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/ClientProfileImplement.java $
 * 
 * 5     2/14/12 12:13p Jwong
 * C36319 - No passenger name in GetClientProfileRequest in UDI interface.
 * 
 * 4     2/12/11 10:00a Ezhang
 * C36130 added system id validation.
 * 
 * 3     8/19/11 3:37p Dchen
 * port C34861, 34862, 35460 here.
 * 
 * 2     8/18/11 3:32p Dchen
 * upgrade to 3.75.
 * 
 * 1     2/25/10 3:09p Dchen
 * OSP interface.
 * 
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;


import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartModel;
import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;
import com.dds.pathfinder.callbooker.server.telephone.ejb.UserTelephone;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.itaxiinterface.webservice.Attribute;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.ClientProfileRequest;
import com.dds.pathfinder.itaxiinterface.webservice.ClientProfileResponse;
import com.dds.pathfinder.itaxiinterface.webservice.ClpAddrListItem;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;

public class ClientProfileImplement extends OSPBaseImplement {

	private Logger logger = LogManager.getLogger(this.getClass());
	
	private UserTelephone userTelephone;
	
	private DataSource pfDataSource;
	
	
	public ClientProfileImplement(UserTelephone userTelephone, DataSource dataSource) {
		super();
		this.userTelephone = userTelephone;
		pfDataSource = dataSource;
	}

	public ClientProfileResponse generateResponse(BaseReq request) {
		return generateClientProfileResponse((ClientProfileRequest)request);
	}
	
	private ClientProfileResponse generateClientProfileResponse(ClientProfileRequest request){
		ClientProfileResponse response = new ClientProfileResponse();
		//validate system id
	
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
		}
		if(!isValidClientProfileRequest(request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			return response;
		}else{
			CartItem cartItem = generateCartItem(request);
			try{
				// UserTelephone userTelephone = teleHome.create();
				CartModel cartModel = userTelephone.findTelephoneDetails(cartItem, request.getSessionID(), "");
				if(cartModel != null && cartModel.getSize() > 0){
					cartModelToResponse(cartModel, response);
				}else{
					response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
				}
			}catch(Exception e){
				logger.error("generateClientProfileResponse failed: ", e);
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
			}
		}
		return response;
	}
	
	private void cartModelToResponse(CartModel cartModel, ClientProfileResponse response){
		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
		int size = cartModel.getSize();
		ArrayList<ClpAddrListItem> listAddresses = new ArrayList<ClpAddrListItem>();
		for(int i=0; i<size; i++){
			CartItem cart = cartModel.getItemAt(i);
			if(cart.getStopPoint(0).getContactInfo().isServiceFlag()){  //C34862, check service flag
				listAddresses.add(createCliAddrListItem(cart));
			}
		}
		response.setNumberOfRec(listAddresses.size());
		ClpAddrListItem[] addresses = new ClpAddrListItem[listAddresses.size()];
		listAddresses.toArray(addresses);
		response.setRegionList(addresses);
	}
	
	private ClpAddrListItem createCliAddrListItem(CartItem cart){
		ClpAddrListItem clpAddress = new ClpAddrListItem();
		if(cart == null) return clpAddress;
		AddressItem pickupAddress = cart.getStopPoint(0).getPickupAddress();
		if(pickupAddress == null) return clpAddress;
// C36319 - Passenger Name
		clpAddress.setPassengerName(cart.getStopPoint(0).getContactInfo().getGivenName());
		clpAddress.setStreetName(pickupAddress.getStrName());
		clpAddress.setStreetNr(pickupAddress.getStrNum());
		clpAddress.setUnit(pickupAddress.getunitNr());
		clpAddress.setDistrict(pickupAddress.getRegion());
		clpAddress.setLandmark(pickupAddress.getBuilding_Name());
		clpAddress.setRemarks(pickupAddress.getDriverNotes());
		setJobAttributes(cart.getDriverAttributeBinary(), cart.getVehicleAttributeBinary(), clpAddress, cart.getTaxiCompanyID());
		
		return clpAddress;
	}
	
	private void setJobAttributes(String drvAttributes, String vehAttributes, ClpAddrListItem addItem, int companyID){
		
		ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>();
		CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
		attributes.addAll(Utilities.getAttributesItem(CompanyDefaultValues.DriverAttributesMap.get(companyID), drvAttributes));
		attributes.addAll(Utilities.getAttributesItem(CompanyDefaultValues.VehicleAttributesMap.get(companyID), vehAttributes));
		
		if(attributes.size() > 0){
			Attribute[] attrArray = new Attribute[attributes.size()];
			for(int i=0; i<attributes.size(); i++){
				attrArray[i] = new Attribute(attributes.get(i).getAttrShortName());
			}
			addItem.setAttributeList(attrArray);
		}
	}
	
	
	private boolean isValidClientProfileRequest(ClientProfileRequest request){
		if(request == null || request.getTaxiCoID() == null || request.getTaxiCoID() <= 0) return false;
		return (request.getPhoneNumber() != null && request.getPhoneNumber().trim().length() > 0);
	}
	
	private CartItem generateCartItem(ClientProfileRequest request){
		CartItem cartItem = new CartItem();
		
		String noteSep = CompanyDefaultValues.getCompanyParameterValue(pfDataSource, request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_NOTES_SEPARATOR);
		if(noteSep == null) noteSep = "\\";
		cartItem.setNotesSeparator(noteSep);
		cartItem.setIndex(0);
		cartItem.setTaxiCompanyID(request.getTaxiCoID());
		cartItem.getStopPoint(0).getContactInfo().setAreaCodeFlagAdded(false);
		cartItem.getStopPoint(0).getContactInfo().setTelephone(request.getPhoneNumber());
		
		return cartItem;
	}
	

}
