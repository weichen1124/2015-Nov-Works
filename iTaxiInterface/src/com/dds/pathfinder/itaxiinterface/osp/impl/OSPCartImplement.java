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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/OSPCartImplement.java $
 * PF-16074 taxiLimo default to credit card payment
 * 
 * PF-16183, 08/29/14, DChen,added TSS require service.
 * 
 * PF-16172, DChen, 08/19/14, OSP should include all linked attributes even not exposed.
 * 
 * PF-16136, DChen, 6/30/14 Booking origin for OSP or Taxi Magic.
 * 
 * 38    4/07/14 4:20p Dchen
 * PF-15958, like 15872 OSP should link region attributes even not
 * exposed.
 * 
 * 37    3/12/14 3:32p Dchen
 * Added osp_users table instead of hard coding.
 * 
 * 36    2/24/14 3:15p Dchen
 * PF-15803, UDI user should retrieve all other jobs not booked by UDI.
 * 
 * 35    2/13/14 3:41p Dchen
 * PF15872, OSP should link area attributes even not exposed.
 * 
 * 34    1/16/14 3:28p Dchen
 * PF-15751, OSP to validate pickup and phone number.
 * 
 * 33    1/07/14 2:54p Sfoladian
 * MG-317- PF OSP is doing uppercase for account validation but not
 * account payment
 * 
 * 32    11/01/13 5:39p Dchen
 * MG-252, forced address with area attributes.
 * 
 * 31    4/08/13 2:35p Dchen
 * PF-15014, attribute lead time doubled in total lead time.
 * 
 * 30    3/12/13 4:34p Dchen
 * Added pf parameters load bean.
 * 
 * 29    3/06/13 3:59p Dchen
 * null point reported with C_WB_HELD_JOB parameter.
 * 
 * 28    2/26/13 3:10p Dchen
 * Clean some warning message.
 * 
 * 27    2/21/13 6:58p Mkan
 * - modified setContactInformation() to use default account if request
 * did not specify (PF-15230)
 * 
 * 26    2/19/13 3:44p Dchen
 * PF-15014, add some attributes linked information in OSP.
 * 
 * 25    2/19/13 3:41p Dchen
 * PF-15014, add some attributes linked information in OSP.
 * 
 * 24    10/24/12 5:59p Ajiang
 * PF-14812 - Stored the forced address GPS
 * 
 * 23    10/24/12 5:46p Ezhang
 * PF-14809 Added job_id validation since we have mutilple booking sources(GoFastCab, Mobile Booking and WebBooker).
 * 
 * 22    10/09/12 5:23p Dchen
 * PF-14785, added reference nb, and extra inf for BW webbooker.
 * 
 * 21    9/19/12 4:07p Jwong
 * PF-14147 / PF-14368 - Beverly Hill's webbooker request / Region
 * attribute is missing when booking in web booker job to an address with
 * region, area and building attributes.
 * 
 * 20    5/30/12 4:01p Ezhang
 * PF-14445 roll back to the version before because we want GFC job behaves like web booker job.
 * 
 * 19    5/25/12 11:50a Ezhang
 * PF-14445 modify convertRequest2CartItem() to exclude GFC job from this webbooker feature. 
 * 
 * 18    9/12/11 2:21p Yyin
 * C36130- Fixed forced address trips with OSP.
 * 
 * 17    12/02/11 4:47p Jwong
 * C35846 - Updated for code cleanup.
 * 
 * 16    11/21/11 4:28p Jwong
 * C35846 - Allow webbooker trip to be booked with held job flag.
 * 
 * 15    8/19/11 3:37p Dchen
 * port C34861, 34862, 35460 here.
 * 
 * 14    8/18/11 3:32p Dchen
 * upgrade to 3.75.
 * 
 * 13    6/27/11 11:19a Dchen
 * dateformat.
 * 
 * 12    1/02/11 7:02p Mkan
 * - upcaseRequest(): use upcaseRequest() in Utilities. (C34954)
 * 
 * 11    12/10/10 3:19p Mkan
 * C34595
 *    - added UserAccount 
 *    - added setUserAccountHome()
 *    - convertRequest2CartItem(): set account detail info into CartItem
 *    - added populateAccountDetails() to set account details, copied from
 * Callbooker
 *    - setCartStopPoint(): changed to set driver notes into the editable
 * one, "manual driver notes".
 * 
 * 10    12/02/10 4:03p Ajiang
 * C34600 - searching a webbooker created prebooked job displays a
 * 'connection lost ' error
 * 
 * 9     11/30/10 2:20p Ezhang
 * C34587 make unit uppercase
 * 
 * 8     11/26/10 6:29p Ezhang
 * C34524 get correct addressItem before calling BookJob.
 * 
 * 7     11/10/10 2:07p Ezhang
 * Added action type constant and move upcaseRequest() to here.
 * 
 * 6     4/30/10 4:29p Dchen
 * include telephone ext in web booker.
 * 
 * 5     4/15/10 11:58a Mkan
 * convertRequest2CartItem()
 * - changed to book with both callout value and callout value
 * abbreviation
 * - fixed to set job type correctly (asap vs prebook)
 * - added to book with preferred vehicle if requested
 * 
 * setCartStopPoint()
 * - fixed to use user specified drop down address if it's not valid
 *   (otherwise job with invalid drop off address will be booked with "As
 * directed" instead)
 * - fixed driver notes not being booked into job
 * 
 * setAddressItem()
 * - added to use user specified unit.
 *   if it's not specified, we'll use the unit number returned from
 * address lookup service
 * 
 * setContactInformation()
 * - added to use telephone extension
 * - fix booking with CASH account
 *   (get cash account code/name when account code/name not specified by
 * OSP)
 * - update for getAdviseArrivalFlag signature change
 * 
 * 4     3/03/10 4:40p Dchen
 * OSP interface.
 * 
 * 3     2/23/10 2:39p Dchen
 * OSP interface.
 * 
 * 2     2/12/10 5:15p Dchen
 * Modified for web booker using.
 * 
 * 1     2/08/10 4:24p Dchen
 * OSP interface.
 * 
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.account.user.ejb.UserAccount;
import com.dds.pathfinder.callbooker.server.account.user.model.UserAccountItem;
import com.dds.pathfinder.callbooker.server.address.ejb.AddressDAO2;
import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartModel;
import com.dds.pathfinder.callbooker.server.cart.model.StopPoint;
import com.dds.pathfinder.callbooker.server.order.model.VehiclePrefVO;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;
import com.dds.pathfinder.callbooker.server.telephone.ejb.UserTelephone;
import com.dds.pathfinder.callbooker.server.util.ContactInformation;
import com.dds.pathfinder.callbooker.server.vehicle.ejb.VehicleDAO;
import com.dds.pathfinder.itaxiinterface.common.impl.CallbookerCartImplement;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.itaxiinterface.webservice.Attribute;
import com.dds.pathfinder.itaxiinterface.webservice.BookJobReq;
import com.dds.pathfinder.itaxiinterface.webservice.BookJobRes;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;

/**
 * A base class for booking and updating a job. Used to convert request to CartItem.
 *
 */
public abstract class OSPCartImplement extends CallbookerCartImplement implements OSPImplement{
//	public static final int ACTION_TYPE_MODIFYBOOKING = 88;
//	public static final int ACTION_TYPE_MODIFYJOB = 11;
//	public static final int ACTION_TYPE_BOOK_MULTIPLEJOBS = 99;
//	public static final int PF_REFERENCE_NUMBER_MAX_LENGTH = 40;
	
	private static Logger logger = LogManager.getLogger(OSPCartImplement.class);
	protected DataSource pfDataSource;
	
	protected LoadDispatchParametersLocal cachedParam;
	
	protected UserAccount userAccount = null; //added for C34595, to populate account detail into booking
	protected UserTelephone userTelephone = null;
	
	public void setUserTelephone(UserTelephone userTelephone) {
		this.userTelephone = userTelephone;
	}


	public UserTelephone getUserTelephone() {
		return userTelephone;
	}


	protected void setUserAccount(UserAccount userAccountLocal) {
		this.userAccount = userAccountLocal;
//		if (userAccountHome != null) {
//			try {
//				userAccount = userAccountHome.create();
//			} catch (Exception e) {
//				logger.error("get account ejb failed", e);
//			}
//		}
	}
	
	
	
	public OSPCartImplement() {
		super();
	}
	
	public OSPCartImplement(DataSource pfDataSource) {
		super();
		this.pfDataSource = pfDataSource;
	}
	
	public OSPCartImplement(IAddressLookup addressLookUp,DataSource pfDataSource, LoadDispatchParametersLocal cachedParam){
		super(addressLookUp);
		this.pfDataSource = pfDataSource;
		this.cachedParam = cachedParam;
		setCachedParam(cachedParam);
	}


	public CartItem convertRequest2CartItem(BookJobReq request, PFAddressData pickupAddress, PFAddressData setdownAddress, AddressItem closestAddrItem){
		
		CartItem cartItem = new CartItem();
		cartItem.setTaxiCompanyID(request.getTaxiCoID());
		cartItem.setTaxi_Company(cachedParam.getTaxiCompanyName(request.getTaxiCoID()) );      //CompanyDefaultValues.getTaxiCompanyName(pfDataSource, request.getTaxiCoID()));
		cartItem.setJobId(""+request.getTaxiRideID());
		cartItem.setOriginCode(getSystemReference(request.getSystemID()));
		setCartStopPoint(cartItem.getStopPoint(0), request, pickupAddress, setdownAddress, closestAddrItem);
		if( pickupAddress == null && closestAddrItem!= null) // Forced address trip
		{
			// Use the closest address as the forced address
			cartItem.setForcedAddressFlag(true);
			cartItem.setforced_address_id(closestAddrItem.getAddressId());
			cartItem.setforced_area_id(closestAddrItem.getAreaId());
		}

		String jobCalloutValue = request.getAdviseArrival();
		String[] pfCallout = new String[1];
		String[] pfCalloutAbbr = new String[1];
		if (jobCalloutValue != null && jobCalloutValue.length() > 0) {
			AdviseArrivalTypesImplement.getPFCalloutValue(pfDataSource, jobCalloutValue, pfCallout, pfCalloutAbbr);
			cartItem.setJobCalloutValue(pfCallout[0]);
			cartItem.setJobCalloutValueAbbr(pfCalloutAbbr[0]);
		}
				
		cartItem.setJob_priority("30");
		cartItem.setCallSign(request.getCabNum());
		cartItem.setCustCallbackCount(getWSIntegerValue(request.getNbofCallBacks()));
		setCartPickupDTM(cartItem, request);
		setAttributesBinary(cartItem, request);
		cartItem.getServiceTypeAccount().setAccountSetId(CompanyDefaultValues.DEFAULT_PICKUP_ACCOUNT_SET_ID);		//default service type pickup 4
		cartItem.setPassengetCount("" + getWSIntegerValue(request.getPassangerNr()));
		cartItem.setUserCode(request.getWebUserID());
		cartItem.setExtraInformation(request.getExtraInformation());
		cartItem.setExtraInfo1(request.getJobExtraInfo1());
		cartItem.setExtraInfo2(request.getJobExtraInfo2());
		cartItem.setExtraInfo3(request.getJobExtraInfo3());
		cartItem.setExtraInfo4(request.getJobExtraInfo4());
		cartItem.setExtraInfo5(request.getJobExtraInfo5());		
		
		ArrayList<SysAttrListItem> missedAttributes = new ArrayList<SysAttrListItem>();
		//C34595, add account info to cart if booking by account
		if ((request.getAccountCode() != null && request.getAccountCode().length() > 0)
				|| (request.getAccountName() != null && request.getAccountName().length() > 0))
		{
			Vector<UserAccountItem> account = null;
			try {
				if (userAccount == null)
				{
					logger.debug("Implementation of OSPCartImplement did not set UserAccountHome");
				}
				else
				{
					account = userAccount.getByAccountDetails(request.getSessionID(), "", cartItem, 1, "");
				}
			} catch (Exception e) {
				logger.error("getByAccountDetails failed....", e);
			}
			if (account == null || account.size() <= 0) 
			{
				//specified account not found. should return error.
				logger.error("no account found");
			} 
			else 
			{
				UserAccountItem foundAccount = account.get(0);
				if (account.size() > 1)
				{
					logger.info("more than one account, use 1st account - " + foundAccount.getAccountId() 
							+ " " + foundAccount.getAccountName());
				}
				
				populateAccountDetails(cartItem, foundAccount, request, missedAttributes);
			}
			
			
		}
		
		
		// C36745 - Add all non-manual attributes here
		if( !addAutoAttributes( request, cartItem, cartItem.getStopPoint(0).getPickupAddress(), closestAddrItem, missedAttributes ) ){
			logger.error("auto populate attributes failed with exceptions................");
		} 
			   
		String requestCabNum = request.getCabNum(); //the preferred vehicle callsign
		if (requestCabNum != null) {
			requestCabNum = requestCabNum.trim();
			if (requestCabNum.length() > 0) {
				String preferredType = "P";
				VehiclePrefVO vechiclePref = createVehiclePref(requestCabNum);
				vechiclePref.setPrefType(preferredType);
				cartItem.getVehicleOrderVO().appendVehiclePrefVO(vechiclePref);
			}
		}
		
		
		
		// C35846 - Set webbooker held job flag if parameter C_WB_HELD_JOB is enabled
		String value = cachedParam.getCompanyParameterValue(request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_WB_HELD_JOB);//CompanyDefaultValues.getCompanyParameterValue(pfDataSource, request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_WB_HELD_JOB);
		if("Y".equalsIgnoreCase(value))  {
			cartItem.setJOB_TYPE("HELD JOB");
			cartItem.getDetailInfo().add("Held Job");
			cartItem.setWBHoldFlag(true);
		}else{
			cartItem.setWBHoldFlag(false);
		}// End of C35864
		
		validateAttributes(cartItem, missedAttributes);
		
		return cartItem;
	}
	
	private void setCartPickupDTM(CartItem cartItem, BookJobReq request){
		Date pickupDTM = Utilities.convertOSPDefaultDate(request.getPickupTime());
		setJobType(cartItem, pickupDTM);  //set job type using pickup date/time user requested
		if(pickupDTM == null) pickupDTM = new Date();
		cartItem.setDateForMultilangual(pickupDTM);
		cartItem.setTime(Utilities.formatUtilDate(pickupDTM, "HH:mm"));
		cartItem.setDate(Utilities.formatUtilDate(pickupDTM, "EEEE, MMMM dd,yyyy"));
	}
	
	/**
	 * Populate cart with account detail.
	 * This is based on CommmandPopulateAccount found in callbooker.client.controller.
	 * 
	 * The followings will be populated:
	 * - Monitor flag, Client company, Organization, account calltaker notes, account driver notes,
	 *   account profile id, the fees (run in, layout..etc), and job priority
	 * 
	 * Note that callbooker also check "account not disabled" and "account has trip left" before doing this.
	 * We assume that the booking request only contains valid "booking allowed" account.
	 * 
	 * Note: Unlike Callbooker client, the following info from account will not be used.
	 * 		 - phone (in callbooker, it pops up dialog and warn user about it, here we can't)
	 * 		 - callout value
	 * 		 - service detail (service type, tariff number)
	 * 		 - fixed trip, account trip
	 * 		 - setCurrentStopEnumerationsBack created for C31839
	 * 
	 * @param cartItem	the cart item to update
	 * @param uaItem	the User Account Item containing the account information
	 */
	private void populateAccountDetails(CartItem cartItem, UserAccountItem uaItem, BookJobReq request, ArrayList<SysAttrListItem> missedAttributes)
	{
		ContactInformation accountContact = uaItem.getContactInfo();
		ContactInformation currentContact = cartItem.getStopPoint(0).getContactInfo();
		
		if (accountContact != null)
		{
			if (currentContact != null && accountContact.getMonitor_Flag())
			{
				cartItem.getDetailInfo().add("Monitor");
				currentContact.setMonitor_Flag(true);
			}
			currentContact.setClientCompany(uaItem.getOrganization());
		}
		
		cartItem.setAcctOrganization(uaItem.getOrganization());
		cartItem.getStopPoint(0).setDrvNotes(uaItem.getAccountDriverNotes()); //set the read-only driver notes
		cartItem.setOperatorNotes(uaItem.getAccountCalltakerNotes());
		
		cartItem.setAccount_Profile_ID(uaItem.getAccountUserProfile());
		
		// if job is not in completed state, overwrite the currentItem with values from account C19662	
		if (cartItem.getJobStage() != null && cartItem.getJobStage().compareToIgnoreCase("") != 0) 
		{
			if (cartItem.getJobStage().compareToIgnoreCase("C") != 0 )	{
				cartItem.setRunIn(uaItem.getAccountRunIn());
				cartItem.setLayout_amount(uaItem.getAccountLayout());
				cartItem.setASAPBookingFee(uaItem.getAccountASAPFee());
				cartItem.setPBOKBookingFee(uaItem.getAccountPBOKFee());
				cartItem.setUpliftFee(uaItem.getAccountUpliftFee());
			}	
		} else {
			cartItem.setRunIn(uaItem.getAccountRunIn());
			cartItem.setLayout_amount(uaItem.getAccountLayout());
			cartItem.setASAPBookingFee(uaItem.getAccountASAPFee());
			cartItem.setPBOKBookingFee(uaItem.getAccountPBOKFee());
			cartItem.setUpliftFee(uaItem.getAccountUpliftFee());		
		}
		
		cartItem.setJob_priority(uaItem.getJobPriority()); // C16415 - aleung - fixed for not populating account priority
		
		String drvAttributes = Utilities.ToBinaryAttributesString(uaItem.getDriverAttributes());
		String vehAttributes = Utilities.ToBinaryAttributesString(uaItem.getVehicleAttributes());
		// Insert account attributes to request
		if( insertRequestAttributes( request, drvAttributes, vehAttributes, missedAttributes ) ) {
				setAttributesBinary( cartItem, request );
		}
		
	}
	
	/**
	 * Create vehicle preference based on call-sign
	 * @param callsign	call-sign of vehicle
	 * @return the vehicle preference result from database.
	 */
	public VehiclePrefVO createVehiclePref(String callsign) {
		if (callsign == null || callsign.trim().length() == 0) {
			return null;
		}
		if (this.pfDataSource != null) {
			Connection con = null;
			try {
				con = this.pfDataSource.getConnection();
				return VehicleDAO.findByCallSign(con, callsign); 
			} catch (Exception e) {
				logger.error("Failed to get vehicle", e);
			} finally {
				if(con != null) try{con.close();}catch(SQLException ignore){};
			}
		}
		return null;
	}
	
	public String getUserName(BookJobReq request){
		return getUserName(request.getSystemID(), request.getSessionID());
	}
	
//	public final static String JOB_TYPE_ASAP = "ASAP";
//    public final static String JOB_TYPE_PBOK = "PBOK";
//    public final static String JOB_TYPE_RTC  = "RTC";
//    public final static String JOB_TYPE_HELDJOB = "HELD JOB";
//    public final static String JOB_TYPE_STREETHIRE = "STREET HIRE";
//    public final static String JOB_TYPE_HELDTEMPLATE = "HELD TEMPLATE";
//    public final static String JOB_TYPE_COMPLETED = "COMPLETED";
//    public final static String JOB_TYPE_TBA = "TBA";
//    public final static String JOB_TYPE_DISPATCH = "DISPATCH";
//    public final static String JOB_TYPE_QUICKBOOKER = "QUICKBOOKER";
//    public final static String JOB_TYPE_AUTOBOOKER = "AUTOBOOKER";
//    
//	public final static String JOB_TYPE_ASAP_RAW = "I";
//	public final static String JOB_TYPE_PBOK_RAW= "P";
//	public final static String JOB_TYPE_TBA_RAW = "T"; //TODO: does T mean TBA? if not, please rename
	
    /**
     * Set job type according to pickup date time. 
     * If pickupDTM is null, it is an ASAP job. Otherwise, PBOK.
     * 
     * @param cartItem	the cartItem to update
     * @param pickupDTM	the pickup date time
     */
	private void setJobType(CartItem cartItem, Date pickupDTM){ 
		if (pickupDTM == null) {
			cartItem.setJOB_TYPE(JOB_TYPE_ASAP);
		} else {
			cartItem.setJOB_TYPE(JOB_TYPE_PBOK);
		}
	}
	
	public void setAttributesBinary(CartItem cartItem, BookJobReq request){
		Attribute[] attributes = request.getAttributeList();
//		List<Attribute> attributeList = request.getAttributeList();
//		Attribute[] attributes = (Attribute[]) attributeList.toArray();
		
		if(attributes != null && attributes.length > 0){
			// CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
			cartItem.setDriverAttributeBinary(Utilities.getPFAttributesBinary(cachedParam.getDrvAttributeMap().get(cartItem.getTaxiCompanyID()), attributes));
			cartItem.setVehicleAttributeBinary(Utilities.getPFAttributesBinary(cachedParam.getVehAttributeMap().get(cartItem.getTaxiCompanyID()), attributes));
		}
	}
	
	private void setCartStopPoint(StopPoint stopPoint, BookJobReq request, PFAddressData pickup, PFAddressData setdown, AddressItem closestAddrItem){	
		Connection con = null;
		AddressItem pickupAddress = null;
		if( pickup!=null)
		{
			try{
				con = pfDataSource.getConnection();
				pickupAddress = getAddressItem(con, pickup, request.getTaxiCoID());
			}catch(SQLException se){
			logger.error("getAddressItem failed", se);
			}finally{
				if(con != null) try{con.close();}catch(SQLException ignore){};
			}
		}
		else if(closestAddrItem!=null)// A forced address trip
		{
			// Use the closest address as the address forced to
			pickupAddress=new AddressItem();
			stopPoint.setForced_Address_Item(closestAddrItem);
			stopPoint.getForced_Address_Item().setforced_address_id(closestAddrItem.getAddressId());
			stopPoint.getForced_Address_Item().setforced_block_id(closestAddrItem.getBlockFaceId());
			stopPoint.getForced_Address_Item().setforced_area_id(closestAddrItem.getAreaId());
			
			// Use the pickup information in the request as the pickup address
			pickupAddress.setStrName(request.getPickupStreetName());
			pickupAddress.setStrNum(request.getPickupStreetNr());
			pickupAddress.setRegion(request.getPickupRegion());

			pickupAddress.setForcedAddressFlag(true);
			// Store the original forced address GPS
			if (request.getPickupLat() > -90.0 && request.getPickupLat() < 90.0 &&
				request.getPickupLon() > -180.0 && request.getPickupLon() < 180.0){
				pickupAddress.setLatitude(request.getPickupLat());
				pickupAddress.setLongitude(request.getPickupLon());
			}
		}
		if (request.getPickupUnit() != null && request.getPickupUnit().length() > 0) {
			pickupAddress.setunitNr(request.getPickupUnit());
		} 
		stopPoint.setPickupAddress(pickupAddress);

		
		//set down address was requested although not valid
		if (setdown == null && !isSetdownEmpty(request)) {
			//use the address from request directly
			setForceSetdownAddressItem(stopPoint.getSetdownAddress(), request);
		} else {
			setAddressItem(stopPoint.getSetdownAddress(), setdown, request.getDropoffUnit());
		}
		
		setContactInformation(stopPoint.getContactInfo(), request);
		//set driver notes (a.k.a. remarks) from OSP webbooker to show up in driver notes in callbooker
		String remarks = request.getRemarks();
		if (remarks != null && stopPoint.getPickupAddress()!=null) {
			remarks = remarks.trim();
			if (remarks.length() > 0) {
				stopPoint.getPickupAddress().setManualDrvNotes(remarks.trim()); //set the writable driver notes
			}
		}
		String spExtraInfo1 = request.getSpExtraInfo1();
		if (spExtraInfo1 != null && !spExtraInfo1.isEmpty())
			stopPoint.setExtraInfo1(spExtraInfo1);			
		
	}

	/**
	 * Set setdown address item according to booking request
	 * @param dropoffAddress	the address Item to update
	 * @param request			the booking request
	 */
	private void setForceSetdownAddressItem(AddressItem dropoffAddress,  BookJobReq request) {
		dropoffAddress.setStrName(request.getDropoffStreetName());
		dropoffAddress.setStrNum(request.getDropoffStreetNr());
		dropoffAddress.setunitNr(request.getDropoffUnit());
		dropoffAddress.setRegion(request.getDropoffRegion());
		dropoffAddress.setBuilding_Name(request.getDropoffLandmark());
	}
	
	/**
	 * Test if set down address in request is empty
	 * @param request	the booking request
	 * @return true if all of street name, street no., unit no., region, and building are empty.
	 */
	private boolean isSetdownEmpty(BookJobReq request) {
		return (request.getDropoffStreetName() == null || request.getDropoffStreetName().trim().length() == 0)
				&& (request.getDropoffStreetNr() == null || request.getDropoffStreetNr().trim().length() == 0)
				&& (request.getDropoffLandmark() == null || request.getDropoffLandmark().trim().length() == 0)
				&& (request.getDropoffRegion() == null || request.getDropoffRegion().trim().length() == 0)
				&& (request.getDropoffUnit() == null || request.getDropoffUnit().trim().length() == 0);
	}
	
	
//	private void setAddressItem(AddressItem address, PFAddressData addressData, String forceUnitNr){
//		if(address == null || addressData == null) return;
//		address.setAddressId(getWSAddressID(addressData));
//		//address.setAddressId(addressItem.getAddressId());
//		address.setAreaId(getWSIntegerValue(addressData.getAreaID()));
//		address.setBlockFaceId(getWSIntegerValue(addressData.getBlockFaceID()));
//		address.setRegionID(getWSIntegerValue(addressData.getRegionID()));
//		address.setStreetNameID(getWSIntegerValue(addressData.getStreetNameID()));
//		
//		address.setStrName(getWSStringValue(addressData.getStreetName().getValue()));
//		address.setStrNum(getWSStringValue(addressData.getStreetNumber().getValue()));
//		
//		//Following JCallbooker behaviour, user entered unit number 
//		//is put into StopPointItem's forcedUnitNum via AddressItem's unitNr
//		if (forceUnitNr != null && forceUnitNr.length() > 0) {
//			address.setunitNr(forceUnitNr);
//		} else {
//			address.setunitNr(getWSStringValue(addressData.getUnitNumber().getValue()));
//		}
//		
//		address.setRegion(getWSStringValue(addressData.getRegionAbbreviation().getValue()));
//		address.setAreaName(getWSStringValue(addressData.getAreaName().getValue()));
//		address.setBuilding_Name(getWSStringValue(addressData.getLandmarkName().getValue()));
//		address.setOrganisation(getWSStringValue(addressData.getOrganizationName().getValue()));
//		address.setPostalCode(getWSStringValue(addressData.getPostCode().getValue()));
//		
//		address.setLatitude(getWSDoubleValue(addressData.getLatitude()));
//		address.setLongitude(getWSDoubleValue(addressData.getLongitude()));
//		address.setLowLatitude(getWSDoubleValue(addressData.getLowLatitude()));
//		address.setLowLongitude(getWSDoubleValue(addressData.getLowLongitude()));
//		address.setHighLatitude(getWSDoubleValue(addressData.getHighLatitude()));
//		address.setHighLongitude(getWSDoubleValue(addressData.getHighLongitude()));
//		
//		address.setLowestNumber(getWSIntegerValue(addressData.getLowBlockFaceNumber()) );
//		address.setHighestNumber(getWSIntegerValue(addressData.getHighBlockFaceNumber()));
//		
//	}
	
	private void setContactInformation(ContactInformation contact, BookJobReq request){
		if(contact == null || request == null)  {
			return;
		}
		
		contact.setTelephone(request.getTelephoneNr());
		contact.setTelExt(request.getTelephoneExt());
		String referenceNumber = (request.getReferenceNumber() == null)? null : request.getReferenceNumber().trim();
		if(referenceNumber != null && referenceNumber.length() > PF_REFERENCE_NUMBER_MAX_LENGTH) referenceNumber = referenceNumber.substring(0, PF_REFERENCE_NUMBER_MAX_LENGTH);
		contact.setReference(referenceNumber);
		
		//Some parts of Callbooker use given name, some parts use family name.
		//To make sure we get the passenger name out both in booking and searching, 
		//we book with both fields set in contact information.
		contact.setGivenName(request.getPassangerName());
		contact.setFamilyName(request.getPassangerName()); 
		
		//trim account code
		String accountCode = request.getAccountCode();
		if (accountCode != null && accountCode.length() > 0) {
			accountCode = accountCode.trim().toUpperCase();
		} else {
			accountCode = null;
		}
		
		//trim account name
		String accountName = request.getAccountName();
		if (accountName != null && accountName.length() > 0) {
			accountName = accountName.trim();
		} else {
			accountName = null;
		}
		
		String accountPassword = request.getAccountPassword();
		
		//check if it's pay by cash
		if(accountCode == null && accountName == null){ 
			
			if (ExternalSystemId.SYSTEM_ID_MB.getSystemId() == request.getSystemID() || TAXILIMO_MOBILE_BOOKER_SYS_ID == request.getSystemID()) {
				accountCode = getCompanyDefaultMGAccountCode(cachedParam, request.getTaxiCoID());
				request.setAccountCode(accountCode);
				//book by account code is enough, we don't need account name
			} else {
				accountCode = cachedParam.getCompanyParameterValue(request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_GENERAL_CASH_ACCOUNT_NR, CompanyDefaultValues.DEFAULT_CASH_ACCOUNT_NAME_NR);;
				request.setAccountCode(accountCode);
				
				accountName = cachedParam.getCompanyParameterValue(request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_GENERAL_CASH_ACCOUNT_NAME, CompanyDefaultValues.DEFAULT_CASH_ACCOUNT_NAME_NR);
				request.setAccountName(accountName);
			}
			
			accountPassword = null;
			
			
		}
		
		//set the account to book with
		contact.setAccount(accountCode); //account card number
		contact.setAccount_Name(accountName); //account name
		contact.setAccount_Pin(accountPassword); 
		
		//OSP user selected advise arrival value 
		String[] pfCallout = new String[1];
		AdviseArrivalTypesImplement.getPFCalloutValue(pfDataSource, request.getAdviseArrival(), null, pfCallout);
		contact.setCallOutValue(pfCallout[0]);
	}
	
//	private int getWSIntegerValue(Integer value){
//		return (value == null? 0:value);
//	}
//	
//	private String getWSStringValue(String value){
//		return (value == null? "":value);
//	}
//	
//	private float getWSDoubleValue(Double value){
//		return (value == null? 0.00f:value.floatValue());
//	}
	
	public void upcaseRequest(BookJobReq request){
		
		request.setPickupStreetName(Utilities.upcaseString(request.getPickupStreetName()));
		request.setPickupLandmark(Utilities.upcaseString(request.getPickupLandmark()));
		request.setPickupRegion(Utilities.upcaseString(request.getPickupRegion()));
		request.setPickupUnit(Utilities.upcaseString(request.getPickupUnit()));
		   
		request.setDropoffStreetName(Utilities.upcaseString(request.getDropoffStreetName()));
		request.setDropoffLandmark(Utilities.upcaseString(request.getDropoffLandmark()));
		request.setDropoffRegion(Utilities.upcaseString(request.getDropoffRegion()));
		request.setDropoffUnit(Utilities.upcaseString(request.getDropoffUnit()));
		
		request.setReferenceNumber(Utilities.upcaseString(request.getReferenceNumber()));
		request.setExtraInformation(Utilities.upcaseString(request.getExtraInformation()));
		
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
	protected boolean validateAddresses(BookJobReq request, PFAddressResponse pickupAddress, PFAddressResponse closestAddress){
	    return (isValidAddressResponse(pickupAddress) || (request.getForcedAddressFlag().equals("Y") && isValidAddressResponse(closestAddress)));
	}
	
	//PF-14809 check the job_id with job reference type to prevent recall job from other type(eg. Webbooker, GFC, MB)
	public boolean isValidateJobId(long jobId, int systemId ) {
			boolean res = false;
			Connection con = null;
			Statement stmt = null;
			ResultSet result = null;
			String referenceType = null;

			
			//check job_references table if request job match the reference type
			referenceType = getSystemReference(systemId);
			
			if (referenceType != null && !referenceType.isEmpty()) {
			   String query = "select job_id, reference_type from job_references "
					+ " where job_id = " + jobId;
			
			
			   try{
				   	con = pfDataSource.getConnection();
					stmt = con.createStatement();
					result = stmt.executeQuery(query);
					//PF-14809 the job is in job_references table, if the request from match the job's reference then return true
					if (result.next() ) {
						if(result.getString("reference_type").equalsIgnoreCase(referenceType)) {
							res = true; //GFC/MB system cancel GFC/MB job PF-14809
						}
								
					}
					//PF-14809 the job is not in job_references table, if the request from webbooker then return true
					else if(systemId == ExternalSystemId.SYSTEM_ID_WEB_BOOKER.getSystemId()
							|| systemId == ExternalSystemId.SYSTEM_ID_UDI_USER.getSystemId()
							|| isAccessableInternalJobs(systemId)) {
						res = true; //webbooker cancel webbooker job
					}
					
		       }catch(SQLException se){
			       	logger.error("isValidateJobId() failed with exception", se);
			       	
		       }finally{
		    	    if (result != null)try {result.close();} catch (SQLException ignore) {};
		    	    if (stmt != null)try {stmt.close();} catch (SQLException ignore) {};
		       		if(con != null) try{con.close();}catch(SQLException ignore){};
		       		
		       }
			}
		   return res;
		   
		}
	
	
	   /**
	    * Function responsible for adding automatic address, region, building, & account attributes
	    * and other types of attributes.
	    * @param request, response, cartItems, drvAttributes, vehAttributes
	    * @return true and false
	    */
		private boolean addAutoAttributes( BookJobReq request, CartItem cartItem, AddressItem pickupAddress, AddressItem closestAddress, ArrayList<SysAttrListItem> missedAttributes ){

			   if( pickupAddress!= null && !pickupAddress.getForcedAddressFlag() )  //pickupAddress will never be null
			   {
				   getAddressAttributes(pickupAddress, request, false, missedAttributes);
			   }
			   else if(closestAddress!=null )
			   {
				   getAddressAttributes(closestAddress, request, true, missedAttributes);
			   }
			   
				// Update carItems list for the new attributes
				setAttributesBinary( cartItem, request );
			   
			   return true;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		// Apply automatic attribute to an OSP trip
		// INPUT:
		//   address - The address used to get automatic attributes
		//   jobReq  - The OSP Job request
		//   isForcedAddress - Is this a forced address? A pickup address which doesn't exist is attached
		//                     to a forced address so it can be dispatched to a correct area. The forced address
		//					   is the closest to the GPS specified in the jobReq.
		////////////////////////////////////////////////////////////////////////////////////////////////////
// C35846 - Added new function to retrieve address attributes.
// C36160 - Added isForcedAddress flag.
		private void getAddressAttributes(AddressItem address, BookJobReq jobReq, boolean isForcedAddress, ArrayList<SysAttrListItem> missedAttributes){
			Connection con = null;
			CallableStatement cs = null;
			// ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>();
			// CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
			String drvAttributes = "";
			String vehAttributes = "";
			
			try{
				con = pfDataSource.getConnection();
				
				AddressDAO2 dao2 = new AddressDAO2();
				
				// Call stored procedure to retrieve address related attributes.
				AddressItem addressItem = dao2.createAddressDeCarta(con, address, jobReq.getTaxiCoID(), false);

				if( "Y".equals(cachedParam.getCompanyParameterValue(jobReq.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_USE_AREA_ATTR_FLAG) )) 
				{
					drvAttributes = Utilities.ToBinaryAttributesString(addressItem.getDriverAttributes());
					vehAttributes = Utilities.ToBinaryAttributesString(addressItem.getVehicleAttributes());
					// findMissedUnExpAttributes(jobReq.getTaxiCoID(), drvAttributes, vehAttributes, missedAttributes);
										
					// Insert area attributes to request
					insertRequestAttributes( jobReq, drvAttributes, vehAttributes, missedAttributes );
				}
				// A forced address is forced to it's closest address in the database. Don't apply building attribute.
				if( !isForcedAddress && "Y".equals(cachedParam.getCompanyParameterValue(jobReq.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_USE_ADDR_ATTR_FLAG)) )
				{
					drvAttributes = Utilities.ToBinaryAttributesString(addressItem.getDrvAddAttributes());
					vehAttributes = Utilities.ToBinaryAttributesString(addressItem.getVehAddAttributes());
					// Insert address attributes to request
					insertRequestAttributes( jobReq, drvAttributes, vehAttributes, missedAttributes );					
				}
				
				if( "Y".equals(cachedParam.getCompanyParameterValue(jobReq.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_USE_REGN_ATTR_FLAG) ) ){
					drvAttributes = Utilities.ToBinaryAttributesString(addressItem.getDrvRegAttributes());
					vehAttributes = Utilities.ToBinaryAttributesString(addressItem.getVehRegAttributes());
					// findMissedUnExpAttributes(jobReq.getTaxiCoID(), drvAttributes, vehAttributes, missedAttributes);  //PF-15958, add region un exposed attributes
					// Insert region attributes to request
					insertRequestAttributes( jobReq, drvAttributes, vehAttributes, missedAttributes );
				}
				
			}catch(SQLException se){
				logger.error("address detail retrieval failed with exception", se );
				
			}finally{
				if(cs != null) try{cs.close();}catch(SQLException ignore){};
		       	if(con != null) try{con.close();}catch(SQLException ignore){};
			}

		}
		
		/**
		    * Function to return given attributes along with existing ones in an array.
		    * @param request, drvAttributes, vehAttributes
		    * @return true if there are given attributes, else false.
		    */
			private boolean insertRequestAttributes(BookJobReq request, String drvAttributes, String vehAttributes, ArrayList<SysAttrListItem> missedAttributes){
				boolean skipFlag = false;
				Attribute[] attrArray = null;
				Attribute[] tempArray = null;
				int index = 0;
				findMissedUnExpAttributes(request.getTaxiCoID(), drvAttributes, vehAttributes, missedAttributes);   //PF-16172, all requested attributes should include not exposed attributes.
				
				ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>(); 
				// CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
				attributes.addAll(Utilities.getAttributesItem(cachedParam.getDrvAttributeMap().get(request.getTaxiCoID()), drvAttributes));
				attributes.addAll(Utilities.getAttributesItem(cachedParam.getVehAttributeMap().get(request.getTaxiCoID()), vehAttributes));
				
				if(attributes.size() > 0){
					//PF-14456 check jobReq.getAttributelist() is null value first since it's optional field of SOAP request
					if( request.getAttributeList() != null )
					    tempArray = new Attribute[attributes.size() + request.getAttributeList().length];
					else
						tempArray = new Attribute[attributes.size()];

					//attrArray = new Attribute[attributes.size()];
					for(int i=0; i<attributes.size(); i++, index++){
						tempArray[i] = new Attribute(attributes.get(i).getAttrShortName());
					}

					// Insert back the attributes from the original request
					//PF-14456 check jobReq.getAttributelist() is null value first since it's optional field of SOAP request
					for(int i=0; request.getAttributeList() != null && i<request.getAttributeList().length; i++)
					{
						skipFlag = false;
						
						// Check for duplicated attributes
						if( request.getAttributeList()[i].getAttrShortName() == null )
						{
							continue;	// Go to next one if current attribute from request is null
						}
						for( int j=0; j<attributes.size() && !skipFlag; j++ ) {
							// Skip if attribute already exists
							if( request.getAttributeList()[i].getAttrShortName().equals( tempArray[j].getAttrShortName() ) ) {
								skipFlag = true;
							}
						}
						if( !skipFlag ) {
							tempArray[index] = request.getAttributeList()[i];
							index++;
						}
					}

					attrArray = new Attribute[index];
					for( int i=0; i< index; i++ ) {
						attrArray[i] = tempArray[i];
					}
					
					request.setAttributeList(attrArray);
					return true;
				}
				else
					return false;
			}
			
		public boolean validateCartItem(CartItem cartItem, BookJobRes response){
			if(response == null) response = getDefaultResponse();
			if(cartItem == null) return false;
			if(!isValidPickupAddress(cartItem)){
				response.setRequestStatus(GenErrMsgRes.STATUS_BOOKING_FAIL_INVALIDADDRESS);
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_ADDRESS);
				response.setErrorCode(BookJobErrorCode.INVALID_ADDRESS.getCode());
				return false;
			}
			if(!isValidClientTelephone(cartItem)){
				response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_TELEPHONE);
				response.setErrorCode(BookJobErrorCode.INVALID_PHONENUM.getCode());
				return false;
			}
			return true;
				
		}
		
		public boolean validateCartItems(Collection<CartItem> cartItems, BookJobRes response){
			if(response == null) response = getDefaultResponse();
			if(cartItems == null) return false;
			for(CartItem cartItem : cartItems){
				if(!validateCartItem(cartItem, response)) return false;
			}
			return true;
		}
		
		
//		private void validateAttributes(CartItem cartItem, ArrayList<SysAttrListItem> missedAttributes){
//			
//			String drvAttributes = cartItem.getDriverAttributeBinary();
//			String vehAttributes = cartItem.getVehicleAttributeBinary();
//			ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>(); 
//			// CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
//			attributes.addAll(Utilities.getAttributesItem(cachedParam.getDrvAttributeMap().get(cartItem.getTaxiCompanyID()), drvAttributes));
//			attributes.addAll(Utilities.getAttributesItem(cachedParam.getVehAttributeMap().get(cartItem.getTaxiCompanyID()), vehAttributes));
//			
//			if(missedAttributes != null && missedAttributes.size() > 0){
//				attributes.addAll(missedAttributes);
//				addMissedAttributes(cartItem, missedAttributes);
//			}
//			
//		
//			cartItem.setAttribute_lead_Time(calculateAttrLeadTime(attributes, cachedParam.getAttributeLeadTime()));
//			setCartItemLeadTime(cartItem, cachedParam.getSystemParameterIntValue("DEFAULT_LEAD_TIME"));
//			
//			cartItem.setAttribute_priority(calculateAttrPriority(attributes, cachedParam.getAttributePriority()));
//			setCartItemPriority(cartItem);
//			
//			String separator = cachedParam.getCompanyParameterValue(cartItem.getTaxiCompanyID(), "C_NOTES_SEPARATOR");
//			String attrNotes = getAttrNotes(cartItem.getOperatorNotes(), attributes, cachedParam.getAttributeOptNotes(), separator);
//			cartItem.setOperatorNotes(Utilities.addNotes(cartItem.getOperatorNotes(), attrNotes , separator));
//			attrNotes = getAttrNotes(cartItem.getStopPoint(0).getDrvNotes(), attributes, cachedParam.getAttributeDrvNotes(), separator);
//			cartItem.getStopPoint(0).setDrvNotes(attrNotes);
//			
//			if(!cartItem.getDisable_Reservable_Flag()) cartItem.setReservableFlagInAttributes(isAttrReservable(attributes, cachedParam.getAttributeReservableFlag()));
//			else cartItem.setReservableFlagInAttributes(false);
//		}
//		
//		public static int calculateAttrLeadTime(ArrayList<SysAttrListItem> attributes, HashMap<String, String> attrLeadTime){
//			int attributeTotalLeadTime = 0;
//			if(attributes == null || attributes.size() == 0 || attrLeadTime == null || attrLeadTime.size() == 0) return attributeTotalLeadTime;
//			for(SysAttrListItem attr : attributes){
//				if(attrLeadTime.containsKey(attr.getAttrShortName())){
//					try{
//						attributeTotalLeadTime += Integer.parseInt(attrLeadTime.get(attr.getAttrShortName()));
//					}catch(NumberFormatException ne){
//						logger.error("calculateAttrLeadTime parseInt failed: ", ne);
//					}
//				}
//			}
//	        return attributeTotalLeadTime;
//		}
//		
//		public static void setCartItemLeadTime(CartItem cart, int defaultLeadTime){
//			int totalLeadTime = 0;
//			try{
//				totalLeadTime = Integer.parseInt(cart.getTotal_Lead_Time());
//			}catch(NumberFormatException ne){
//				logger.warn("current cart lead time in invalid format: " + cart.getTotal_Lead_Time());
//			}
//			if(totalLeadTime == 0){
//				totalLeadTime = defaultLeadTime;
//				logger.info("using totalLeadTime - " + totalLeadTime);
//			}
//			
//			totalLeadTime = (int)Math.ceil((totalLeadTime + cart.getAttribute_lead_Time())/60.0);
//			cart.setTotal_Lead_Time("" + totalLeadTime);
//			// cart.setLead_Time("" + totalLeadTime);
//		}
//		
//		public static void setCartItemPriority(CartItem cart){ 
//			int priority = 30;
//			try{
//				priority = Integer.parseInt(cart.getJob_priority());
//			}catch(NumberFormatException ne){
//				logger.warn("current cart priority in invalid format: " + cart.getJob_priority());
//			}
//			cart.setJob_priority("" + Math.min(priority, cart.getAttribute_priority()));
//			if(!cart.getJob_priority_Flag()){
//				cart.setManualJobPriority(cart.getJob_priority());
//			}
//			
//		}
//		
//		public static int calculateAttrPriority(ArrayList<SysAttrListItem> attributes, HashMap<String, String> attrPriority){
//			int maxAttrPriority = 100;
//			if(attributes == null || attributes.size() == 0 || attrPriority == null || attrPriority.size() == 0) return maxAttrPriority;
//			for(SysAttrListItem attr : attributes){
//				if(attrPriority.containsKey(attr.getAttrShortName())){
//					try{
//						maxAttrPriority = Math.min(maxAttrPriority, Integer.parseInt(attrPriority.get(attr.getAttrShortName())) );
//					}catch(NumberFormatException ne){
//						logger.error("calculateAttrPriority parseInt failed: ", ne);
//					}
//				}
//			}
//	        return maxAttrPriority;
//		}
//		
//		
//		
//		public static String getAttrNotes(String origNotes, ArrayList<SysAttrListItem> attributes, HashMap<String, String> attrNotes, String separator){
//			String notes = origNotes;
//			if(attributes == null || attributes.size() == 0 || attrNotes == null || attrNotes.size() == 0) return notes;
//			for(SysAttrListItem attr : attributes){
//				if(attrNotes.containsKey(attr.getAttrShortName())){
//					notes = Utilities.addNotes(notes, attrNotes.get(attr.getAttrShortName()), separator);
//				}
//			}
//	
//			return notes;		
//		}
//		
//		public static boolean isAttrReservable(ArrayList<SysAttrListItem> attributes, HashMap<String, String> attrReservable){
//			boolean reservable = false;
//			if(attributes == null || attributes.size() == 0 || attrReservable == null || attrReservable.size() == 0) return reservable;
//			for(SysAttrListItem attr : attributes){
//				if(attrReservable.containsKey(attr.getAttrShortName()) && "Y".equalsIgnoreCase(attrReservable.get(attr.getAttrShortName()))){
//					reservable = true;
//					break;
//				}
//			}
//	        return reservable;
//		}
		
		private String getCompanyDefaultMGAccountCode(LoadDispatchParametersLocal cachedParam, int taxiID) {
			String defaultMGAcctCode = cachedParam.getCompanyParameterValue(taxiID, CompanyDefaultValues.COMP_PARAMETER_C_MG_ACCOUNT, CompanyDefaultValues.DEFAULT_MG_ACCOUNT_CODE);
			String mgAccountCode = defaultMGAcctCode.trim(); 
			if (mgAccountCode.length() == 0) {
				return CompanyDefaultValues.DEFAULT_MG_ACCOUNT_CODE; //user configured whitespace, default to use "CREDIT" account
			}
			return mgAccountCode; //use the configured account code
		}
		
		@SuppressWarnings("unchecked")
		private boolean isValidPickupAddress(CartItem cartItem){
			if(cartItem == null || cartItem.getStopPoints() == null || cartItem.getStopPoints().size() == 0) return false;
			for(StopPoint stopPoint : (Vector<StopPoint>)cartItem.getStopPoints()){
				if(stopPoint != null && stopPoint.getPickupAddress() != null){
					AddressItem pickupAddress = stopPoint.getPickupAddress();
					if(pickupAddress.getHold_Flag() || pickupAddress.getBlockFaceHoldFlag() || ! pickupAddress.isServiceRegionFlag()) return false;
				}else return false;
			}
			return true;
		}
		
		public BookJobRes getDefaultResponse(){
			   BookJobRes response = new BookJobRes();
			   
			   response.setTaxiRideID(-1L);
			   response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
			   response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
			   response.setErrorCode(BookJobErrorCode.DEFAULT_ERROR.getCode());
			   
			   return response;
		}
		
		private boolean isValidClientTelephone(CartItem cartItem){
			if(userTelephone == null){
				logger.error("isValidClientTelephone checking failed with userTelephone ejb is null.");
				return true;
			}
			if(!isTelephoneNumber(cartItem.getStopPoint(0).getContactInfo()) ) return true;
			
			try {
				CartModel cartModel = userTelephone.validateTelephoneService(cartItem, "", "");
				CartItem cart = cartModel.getItemAt(0);
				if(cart != null){
					return (cart.getStopPoint(0).getContactInfo().isServiceFlag());
				}
			} catch (Exception e) {
				logger.error("validate telephone service failed...", e);
			} 
			return true;
		}
		
		private boolean isTelephoneNumber(ContactInformation contactInf){
			return (contactInf != null && contactInf.getTelephone() != null && contactInf.getTelephone().trim().length() > 0);
		}
		
		
//		private void findMissedUnExpAttributes(int taxiCoID, String drvAttributes, String vehAttributes, ArrayList<SysAttrListItem> missedAttributes){
//			ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>(); 
//			attributes.addAll(Utilities.getAttributesItem(cachedParam.getUnExpDrvAttributeMap().get(taxiCoID), drvAttributes));
//			attributes.addAll(Utilities.getAttributesItem(cachedParam.getUnExpVehAttributeMap().get(taxiCoID), vehAttributes));
//			
//			if(attributes != null && attributes.size() > 0){
//				missedAttributes.removeAll(attributes);
//				missedAttributes.addAll(attributes);
//			}
//		}
		
//		private void addMissedAttributes(CartItem cartItem, ArrayList<SysAttrListItem> missedAttributes){
//			if(missedAttributes == null || missedAttributes.size() == 0) return;
//			
//			String drvAttributeBinary = Utilities.binStringBitOR(cartItem.getDriverAttributeBinary(), 
//													Utilities.getPFAttributesBinary(cachedParam.getUnExpDrvAttributeMap().get(cartItem.getTaxiCompanyID()), missedAttributes));
//			String vehAttributeBinary = Utilities.binStringBitOR(cartItem.getVehicleAttributeBinary(), 
//													Utilities.getPFAttributesBinary(cachedParam.getUnExpVehAttributeMap().get(cartItem.getTaxiCompanyID()), missedAttributes));
//			
//			cartItem.setDriverAttributeBinary(drvAttributeBinary);
//			cartItem.setVehicleAttributeBinary(vehAttributeBinary);
//		}
		
		public String getAdviseArrivalFlag(DataSource pfDataSource, StopPoint stopPoint, ContactInformation contact){
			//Get PF callout value from address or contact info
			//(This is very similar to com.dds.pathfinder.callbooker.client.util.visitor.CartItemWriterVisitor's getCalloutValue())
			AddressItem pickup = stopPoint.getPickupAddress();
			String pfCallout = AdviseArrivalType.NoAdvise.toPFValueAbbr();
			if(pickup != null && pickup.getJCallOutValue() != null && pickup.getJCallOutValue().length() > 0){
				pfCallout = pickup.getJCallOutValue();
			}else if(contact != null && contact.getCallOutValue() != null && contact.getCallOutValue().length() > 0){
				pfCallout = contact.getCallOutValue();
			}else if(pickup != null){
				pfCallout = pickup.getCallOutValue();
			}
			
			//return OSP advise arrival value using AdviseArrivalTypesImplement AdviseArrivalType mapping
			return AdviseArrivalTypesImplement.getOSPAdviseArrivalValue(pfDataSource, pfCallout); 
		}
		
}
