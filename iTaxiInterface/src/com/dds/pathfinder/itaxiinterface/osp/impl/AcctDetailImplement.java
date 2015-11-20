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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/AcctDetailImplement.java $
 * 
 * 7     2/12/11 9:53a Ezhang
 * C36130 Added system id validation.
 * 
 * 6     8/19/11 3:37p Dchen
 * port C34861, 34862, 35460 here.
 * 
 * 5     8/18/11 3:32p Dchen
 * upgrade to 3.75.
 * 
 * 4     9/20/10 2:03p Ezhang
 * OSP 2.0 added support to errorCode.
 * 
 * 3     4/15/10 11:10a Mkan
 * 
 * 2     1/26/10 5:43p Dchen
 * OSP interface.
 * 
 * 1     1/20/10 3:37p Dchen
 * OSP interface.
 * 
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.util.ArrayList;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.itaxiinterface.webservice.AcctAddrListItem;
import com.dds.pathfinder.itaxiinterface.webservice.Attribute;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.DriverNote;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.GetAccountDetailRequest;
import com.dds.pathfinder.itaxiinterface.webservice.GetAccountDetailResponse;
import com.dds.pathfinder.itaxiinterface.webservice.Message;
import com.dds.pathfinder.itaxiinterface.common.impl.BaseImplement;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
//import com.dds.pathfinder.itaxiinterface.webservice.PFAttrListItem;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartItem;
import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;
import com.dds.pathfinder.callbooker.server.account.user.ejb.UserAccount;
import com.dds.pathfinder.callbooker.server.account.user.model.UserAccountItem;

public class AcctDetailImplement extends OSPBaseImplement {
	
	private UserAccount userAccount = null;
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	private DataSource pfDataSource;

	public enum AcctDetailErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_SESSIONID(2),
		NO_MATCH_ACCOUNT(3),
		INVALID_COMPID(4),
		INVALID_ACCTCODE(5),
		INVALID_ACCTNAME(6),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private AcctDetailErrorCode(int c) {
			   code = c;
		}

		public int getCode() {
			   return code;
		}
	};
	public AcctDetailImplement(UserAccount userAccount, DataSource dataSource){
//		if(userAccountHome != null){
//			try{
//				userAccount = userAccountHome.create();
//			}catch(Exception e){
//				logger.error("get account ejb failed", e);
//			}
//		}
		this.userAccount = userAccount;
		pfDataSource = dataSource;
	}
	
	public GetAccountDetailResponse generateResponse(BaseReq request) {
		GetAccountDetailResponse response = new GetAccountDetailResponse();
		//C36130 validate system id
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(AcctDetailErrorCode.NOT_AUTHENTICATED.getCode());
			return response;
		}
		if(isValidAcctDetailRequest((GetAccountDetailRequest)request)){
			generateAcctDetailResponse((GetAccountDetailRequest)request, response);
		}
		return response;
	}
	
	private boolean isValidAcctDetailRequest(GetAccountDetailRequest request){
		return (request.getTaxiCoID() != null && request.getTaxiCoID() > 0
				&& request.getAccountCode() != null && request.getAccountCode().length() > 0);
	}

	private void generateAcctDetailResponse(GetAccountDetailRequest request, GetAccountDetailResponse response){
		if(userAccount == null) return;
		CartItem cartItem = new CartItem();
		cartItem.setTaxiCompanyID(request.getTaxiCoID());
		cartItem.getStopPoint(0).getContactInfo().setAccount(request.getAccountCode());
		try{
			Vector<UserAccountItem> accounts = userAccount.getByAccountDetails(request.getSessionID(), "", cartItem, 1, "");
			if(accounts != null && accounts.size() > 0){
				response.setAccountCode(request.getAccountCode());
				response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
		    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
		    	response.setErrorCode(AcctDetailErrorCode.NO_ERROR.getCode());
				convertResponse(response, accounts, request.getTaxiCoID());
			}
		}catch(Exception e){
			logger.error("getByAccountDetails failed....", e);
		}
	}
	
	private void convertResponse(GetAccountDetailResponse response, Vector<UserAccountItem> accounts, int companyID){
		UserAccountItem accountItem = accounts.get(0);
		if(accountItem != null){
			response.setAccountName(accountItem.getAccountName());
			String inactive = "A".equalsIgnoreCase(accountItem.getStatus())? "N":"Y";
			response.setInactive(inactive);
			setAccountNotes(response, accountItem);
			if(accountItem.getJobPriority() != null){
				response.setAccountPriority(Utilities.calcJobPriority(accountItem.getJobPriority()));
			}
			setAccountAttributes(response, accountItem, companyID);
			setAccountAddresses(response, accountItem);
		}
	}
	
	private void setAccountNotes(GetAccountDetailResponse response, UserAccountItem accountItem){
		String notes = accountItem.getAccountDriverNotes();
		if(notes != null && notes.trim().length() > 0){
			response.setDriverNotesList(new DriverNote[]{new DriverNote(notes)});
			response.setRemarks(notes);
		}
		notes = accountItem.getAccountCalltakerNotes();
		if(notes != null && notes.trim().length() > 0){
			response.setCallTakerMessagesList(new Message[]{new Message(notes)});
		}
	}
	
//	private String calcJobPriority(String priority){
//		int jobPriority = 0;
//		try{
//			jobPriority = Integer.parseInt(priority);
//		}catch(NumberFormatException ne){
//			logger.error("calcJobPriority failed", ne);
//		}
//		if(jobPriority >0 && jobPriority <= 20){  		//should define later.
//			return "H";
//		}else if(jobPriority > 40){
//			return "L";
//		}else{
//			return "N";
//		}
//	}
	
	private void setAccountAddresses(GetAccountDetailResponse response, UserAccountItem accountItem){
		ArrayList<CartItem> trips = accountItem.getAccountTrips();
		if(trips != null && trips.size() > 0){
			AcctAddrListItem[] addArray = new AcctAddrListItem[trips.size()];
			for(int i=0; i<trips.size(); i++){
				
				CartItem trip = trips.get(i);
				if(trip != null && trip.getStopPoint(0).getPickupAddress() != null){
					addArray[i] = createAccountAddressItem(trip.getStopPoint(0).getPickupAddress());
				}else{
					addArray[i] = new AcctAddrListItem();
				}
			}
			response.setAcctAddressList(addArray);
		}	
	}
	
	private AcctAddrListItem createAccountAddressItem(AddressItem address){
		AcctAddrListItem acctAddress = new AcctAddrListItem();
		if(address == null) return acctAddress;
		acctAddress.setPickupStreetName(address.getStrName());
		acctAddress.setPickupStreetNumber(address.getStrNum());
		acctAddress.setPickupRegion(address.getRegion());
		acctAddress.setPickupUnitNumber(address.getunitNr());
		return acctAddress;
	}
	
	private void setAccountAttributes(GetAccountDetailResponse response, UserAccountItem accountItem, int companyID){
		String drvAttributes = Utilities.ToBinaryAttributesString(accountItem.getDriverAttributes());
		String vehAttributes = Utilities.ToBinaryAttributesString(accountItem.getVehicleAttributes());
		ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>(); 
		CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
		attributes.addAll(Utilities.getAttributesItem(CompanyDefaultValues.DriverAttributesMap.get(companyID), drvAttributes));
		attributes.addAll(Utilities.getAttributesItem(CompanyDefaultValues.VehicleAttributesMap.get(companyID), vehAttributes));
		
		if(attributes.size() > 0){
			Attribute[] attrArray = new Attribute[attributes.size()];
			for(int i=0; i<attributes.size(); i++){
				attrArray[i] = new Attribute(attributes.get(i).getAttrShortName());
			}
			response.setAccountAttributeList(attrArray);
		}
	}
	
	
	
//	public ArrayList<SysAttrListItem> getAttributesItem(HashMap<String, SysAttrListItem> attrMap, String attrString){
//		ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>();
//		if(attrMap == null || attrString == null || attrMap.size() == 0 || attrString.length() ==0) return attributes;
//		for(int i=0; i<attrString.length(); i++){
//			if("1".equals(Character.toString(attrString.charAt(i)) ) ){
//				String bitPosition = "" + (i+1);
//				if(attrMap.containsKey(bitPosition)){
//					attributes.add(attrMap.get(bitPosition));
//				}
//			}
//		}
//		return attributes;
//	}
	
}
