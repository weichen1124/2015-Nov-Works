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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/UpdateJobImplement.java $
 * 
 * PF-16772, 09/15/15, DChen, add unit match in OSP.
 * 
 * 14    3/13/14 3:38p Dchen
 * PF-15742, added log journal in callbooker.
 * 
 * 13    1/16/14 3:28p Dchen
 * PF-15751, OSP to validate pickup and phone number.
 * 
 * 12    3/12/13 4:34p Dchen
 * Added pf parameters load bean.
 * 
 * 11    9/12/11 2:21p Yyin
 * C36130- Fixed forced address trips with OSP.
 * 
 * 10    8/18/11 3:33p Dchen
 * upgrade to 3.75.
 * 
 * 9     4/19/11 6:28p Dchen
 * C35277, speed webbooker order tracking.
 * 
 * 8     12/10/10 3:20p Mkan
 * C34595
 *    - UpdateJobImplement(), added to set UserAccountHome
 * 
 * 7     11/10/10 2:16p Ezhang
 * fixed hardcoded action type
 * 
 * 6     9/27/10 11:27a Ajiang
 * Added errorCode for failed responses
 * 
 * 5     4/15/10 12:02p Mkan
 * generateUpdateJobResponse()
 * - similar fix to booking request: process address match from address
 * lookup service
 * - added to call modifyPickupClosestAddresses (this was also done by
 * booking request but was missing here)
 * - commented out unused code
 * 
 * 4     2/23/10 2:39p Dchen
 * OSP interface.
 * 
 * 3     2/12/10 5:15p Dchen
 * Modified for web booker using.
 * 
 * 2     2/08/10 4:24p Dchen
 * OSP interface.
 * 
 * 1     2/02/10 2:34p Dchen
 * OSP interface.
 * 
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.account.user.ejb.UserAccount;
import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartItem;
import com.dds.pathfinder.callbooker.server.order.ejb.Order;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.BookJobReq;
import com.dds.pathfinder.itaxiinterface.webservice.BookJobRes;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;



public class UpdateJobImplement extends OSPCartImplement {

	private Logger logger = LogManager.getLogger(this.getClass());
	private Order order;
	
	public UpdateJobImplement(Order order, UserAccount userAccount, DataSource pfDataSource, IAddressLookup addressLookup, LoadDispatchParametersLocal cachedParam){
		super(addressLookup, pfDataSource, cachedParam);
//		if(orderHome != null){
//			try{
//				this.order = orderHome.create();
//			}catch(Exception e){
//				logger.error("get order ejb failed.", e);
//			}
//		}
		this.order = order;
		this.userAccount = userAccount; //added for C34595, to populate account detail into booking
	}
	
	public BookJobRes generateResponse(BaseReq request) {
		//BookJobRes response = new BookJobRes();
		return null;
	}
	
	/**
	 * Process job update request.
	 * @param request
	 * @param response
	 * @param pickupAddressRes
	 * @param closestAddressRes
	 * @param setdownAddressRes
	 */
	@SuppressWarnings("unchecked")
	public void generateUpdateJobResponse(BookJobReq request,
			BookJobRes response, PFAddressResponse pickupAddressRes,
			PFAddressResponse closestAddressRes,
			PFAddressResponse setdownAddressRes) {

		if (order == null)
			return;

		if (!validateAddresses(request, pickupAddressRes, closestAddressRes)) {
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_ADDRESS);
			response.setErrorCode(BookJobErrorCode.PICKUP_ADDR_NOT_FOUND.getCode());
			return;
		}

		// PFAddressData pickupAddress = getMatchAddress(pickupAddressRes, request.getPickupStreetName(), request.getPickupStreetNr(), request.getPickupRegion());
		PFAddressData pickupAddress = getMatchAddressWithUnit(pickupAddressRes, request.getPickupStreetName(), request.getPickupStreetNr(), request.getPickupRegion(), request.getPickupUnit());
		PFAddressData closestAddress = getFirstAddress(closestAddressRes);
		PFAddressData setdownAddress = getMatchAddressWithUnit(setdownAddressRes, request.getDropoffStreetName(), request.getDropoffStreetNr(), request.getDropoffRegion(), request.getDropoffUnit());

		if(closestAddress!= null)
		{
			// Get detail of the closest address
			closestAddressRes = getWSAddressData(
			closestAddress.getStreetName().getValue(),
			closestAddress.getStreetNumber().getValue(), 
			closestAddress.getUnitNumber().getValue(),
			closestAddress.getRegionAbbreviation().getValue(),
			closestAddress.getLandmarkName().getValue(),
				   PERCENTAGE_CHAR, PERCENTAGE_CHAR, //organization and postal code
				   request.getTaxiCoID(), "");
			closestAddress = getFirstAddress(closestAddressRes);
		}
		modifyPickupClosestAddresses(pickupAddress, closestAddress);
		AddressItem closestAddrItem = null;
		if( pickupAddress == null && closestAddress != null)
		{
		    closestAddrItem = getAddressItem(closestAddress, request.getTaxiCoID());
		}
		CartItem cartItem = convertRequest2CartItem(request, pickupAddress, setdownAddress, closestAddrItem);
		if(!validateCartItem(cartItem, response)) return;

		try {
			String userName = getUserName(request);
			ArrayList<Object> results = (ArrayList<Object>) order.modifyOrderDetails(userName, "", null, ACTION_TYPE_MODIFYJOB, 
												"" + request.getTaxiRideID(), cartItem, null);
			if (results != null && results.size() > 0) {
				response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
				response.setErrorCode(BookJobErrorCode.NO_ERROR.getCode());
				response.setNbOfJobs(1);
				if (results.size() >= 2) {
					Integer jobID = (Integer) results.get(1);
					response.setTaxiRideID(new Long(jobID));
				}
			}
		} catch (Exception e) {
			logger.error("generateUpdateJobResponse failed", e);
		}

	}
}
