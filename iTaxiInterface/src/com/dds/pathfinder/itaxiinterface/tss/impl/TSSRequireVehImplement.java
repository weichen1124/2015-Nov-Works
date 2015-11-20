/****************************************************************************
 *
 *		   		    Copyright (c), 2014
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/tss/impl/TSSRequireVehImplement.java $
 * 
 * 9/15/14, DChen, PF-16183, add new tss enable parameter.
 * 
 * 08/29/14,DChen, added TSS require service.
 * 
 * 08/25/14, DChen, create TSS project.
 * 
 */

package com.dds.pathfinder.itaxiinterface.tss.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.address.ejb.AddressDAO2;
import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartItem;
import com.dds.pathfinder.callbooker.server.cart.model.StopPoint;
import com.dds.pathfinder.callbooker.server.order.ejb.Order;
import com.dds.pathfinder.callbooker.server.util.ContactInformation;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.osp.impl.AdviseArrivalTypesImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.DrvVehAttributes;
import com.dds.pathfinder.itaxiinterface.osp.impl.OSPCartImplement;
import com.dds.pathfinder.itaxiinterface.tss.TSSRequireVehReq;
import com.dds.pathfinder.itaxiinterface.tss.TSSRequireVehRes;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;


public class TSSRequireVehImplement extends TSSBaseImplement {
	private Logger logger = LogManager.getLogger(this.getClass());
	
    private DataSource pfDataSource;
    private Order order;
    protected IAddressLookup addressLookUp = null;
    
    
    
	public TSSRequireVehImplement(IAddressLookup addressLookUp, DataSource pfDataSource, Order orderLocal) {
		super();
		this.pfDataSource = pfDataSource;
		this.addressLookUp = addressLookUp;
		this.order = orderLocal;
	}

	public TSSRequireVehRes generateTSSResponse(TSSRequireVehReq request){
		TSSRequireVehRes response = new TSSRequireVehRes(TSSErrorCode.TSS_FAILURE);
		
		if(!validateRequest(request, response)){
			   return response;
		}
		
		upcaseRequest(request);
		
		PFAddressResponse pickupAddress = getWSAddressData(
				   appendPercentage(request.getPickupStreetName(), false), 
				   appendPercentage(request.getPickupStreetNr(), false),
				   appendPercentage(request.getPickupUnit(), true), //except for unit here, we want to use pe/rcentage to match
				   appendPercentage(request.getPickupRegion(), false), 
				   appendPercentage(request.getPickupLandmark(), false), 
				   PERCENTAGE_CHAR, PERCENTAGE_CHAR,  //organization and postal code
				   request.getTaxiCoID(), "");
		
		if(!isValidAddressResponse(pickupAddress) && isBothLandMarkAndAddressProvided(request.getPickupStreetName(), request.getPickupStreetNr(), request.getPickupLandmark())){
			   pickupAddress = getWSAddressData(
					   appendPercentage(request.getPickupStreetName(), false), 
					   appendPercentage(request.getPickupStreetNr(), false),
					   appendPercentage(request.getPickupUnit(), true), //except for unit here, we want to use percentage to match
					   appendPercentage(request.getPickupRegion(), false), 
					   PERCENTAGE_CHAR, PERCENTAGE_CHAR, PERCENTAGE_CHAR,  //organization and postal code
					   request.getTaxiCoID(), "");
			   
		}
		
		PFAddressResponse closestAddress = getWSClosestAddressData(request.getPickupLat(), request.getPickupLon());
		
		if (!isValidAddressResponse(pickupAddress) && !isValidAddressResponse(closestAddress)){
			response.setErrorCode(TSSErrorCode.TSS_INVALID_ADDRESS);
		}else{
			response = insertTSSJob(request, pickupAddress, closestAddress); //return create job response
		}
		return response;
	}
	
	private boolean validateRequest(TSSRequireVehReq request, TSSRequireVehRes response){
		if(request == null) return false;
		if(request.getTaxiCoID() <= 0) {
			response.setErrorCode(TSSErrorCode.TSS_INVALID_COMPANY);
			return false;
		}
		if(!isTssSetEnabled(request.getTaxiCoID())){
			response.setErrorCode(TSSErrorCode.TSS_NOT_ENABLED);
			return false;
		}
		return true;
	}
	
	private boolean isTssSetEnabled(int companyID){
		return "Y".equalsIgnoreCase( cachedParam.getCompanyParameterValue(companyID, PARAMETER_C_TSS_ENABLE) );
	}
	
	private void upcaseRequest(TSSRequireVehReq request){
		
		request.setPickupStreetNr(Utilities.upcaseString(request.getPickupStreetNr()));
		request.setPickupStreetName(Utilities.upcaseString(request.getPickupStreetName()));
		request.setPickupLandmark(Utilities.upcaseString(request.getPickupLandmark()));
		request.setPickupRegion(Utilities.upcaseString(request.getPickupRegion()));
		request.setPickupUnit(Utilities.upcaseString(request.getPickupUnit()));
		request.setPickupPostCode(Utilities.upcaseString(request.getPickupPostCode()));
		request.setAttributes(Utilities.upcaseString(request.getAttributes()));
	}
	
	@SuppressWarnings("unchecked")
	private TSSRequireVehRes insertTSSJob(TSSRequireVehReq request, PFAddressResponse pickupAddressRes, PFAddressResponse closestAddressRes){
		TSSRequireVehRes response = new TSSRequireVehRes(TSSErrorCode.TSS_FAILURE);
		
		PFAddressData pickupAddress = getMatchAddress(pickupAddressRes, request.getPickupStreetName(), request.getPickupStreetNr(), request.getPickupRegion());
		PFAddressData closestAddress = getFirstAddress(closestAddressRes);
	 
	   
	   if(closestAddress!= null)
	   {
		   // Some details of the closest address, (e.g., area ID) are missing from the closestAddress
		   // which is getting by closest lookup service. We get detail of it by calling lookup service
		   
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
	   if( pickupAddress == null && closestAddress != null){
	      closestAddrItem = getAddressItem(closestAddress, request.getTaxiCoID());
	   }
	   
	   CartItem cartItem = convertTSSRequest2CartItem(request, pickupAddress, closestAddrItem); 
	   
	   try{
		    String userName = ExternalSystemId.SYSTEM_ID_TSS_RIDER.getLogonCode();
		    
		    // Order order = orderHome.create();
		    ArrayList<Object> results = (ArrayList<Object>)order.setOrderDetails(userName, "", null, cartItem);
		    setInsertTSSJobResponse(results, request, response);	
		    
		}catch(Exception e){
			logger.error("insertTSSJob failed", e);
			response.setErrorCode(TSSErrorCode.TSS_BOOKING_FAILED);
		}
	   
	   return response;

	}
	
	
	private void validateAttributes(CartItem cartItem){
		
		String drvAttributes = cartItem.getDriverAttributeBinary();
		String vehAttributes = cartItem.getVehicleAttributeBinary();
		ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>(); 
		// CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
		attributes.addAll(Utilities.getAttributesItem(cachedParam.getFullDrvAttributeMap(cartItem.getTaxiCompanyID()), drvAttributes));
		attributes.addAll(Utilities.getAttributesItem(cachedParam.getFullDrvAttributeMap(cartItem.getTaxiCompanyID()), vehAttributes));
		
	
		cartItem.setAttribute_lead_Time(OSPCartImplement.calculateAttrLeadTime(attributes, cachedParam.getAttributeLeadTime()));
		OSPCartImplement.setCartItemLeadTime(cartItem, cachedParam.getSystemParameterIntValue("DEFAULT_LEAD_TIME"));
		
		cartItem.setAttribute_priority(OSPCartImplement.calculateAttrPriority(attributes, cachedParam.getAttributePriority()));
		OSPCartImplement.setCartItemPriority(cartItem);
		
		String separator = cachedParam.getCompanyParameterValue(cartItem.getTaxiCompanyID(), "C_NOTES_SEPARATOR");
		String attrNotes = OSPCartImplement.getAttrNotes(cartItem.getOperatorNotes(), attributes, cachedParam.getAttributeOptNotes(), separator);
		cartItem.setOperatorNotes(Utilities.addNotes(cartItem.getOperatorNotes(), attrNotes , separator));
		attrNotes = OSPCartImplement.getAttrNotes(cartItem.getStopPoint(0).getDrvNotes(), attributes, cachedParam.getAttributeDrvNotes(), separator);
		cartItem.getStopPoint(0).setDrvNotes(attrNotes);
		
		if(!cartItem.getDisable_Reservable_Flag()) cartItem.setReservableFlagInAttributes(OSPCartImplement.isAttrReservable(attributes, cachedParam.getAttributeReservableFlag()));
		else cartItem.setReservableFlagInAttributes(false);
	}
	
	private CartItem convertTSSRequest2CartItem(TSSRequireVehReq request, PFAddressData pickupAddress, AddressItem closestAddrItem){
		
		CartItem cartItem = new CartItem();
		cartItem.setTaxiCompanyID(request.getTaxiCoID());
		cartItem.setTaxi_Company(cachedParam.getTaxiCompanyName(request.getTaxiCoID()) );      //CompanyDefaultValues.getTaxiCompanyName(pfDataSource, request.getTaxiCoID()));
		cartItem.setJobId("");
		cartItem.setOriginCode(ExternalSystemId.SYSTEM_ID_TSS_RIDER.getReference());
		
		setCartStopPoint(cartItem.getStopPoint(0), request, pickupAddress, closestAddrItem);
		if( pickupAddress == null && closestAddrItem!= null) {
			cartItem.setForcedAddressFlag(true);
			cartItem.setforced_address_id(closestAddrItem.getAddressId());
			cartItem.setforced_area_id(closestAddrItem.getAreaId());
		}
		
		cartItem.setJob_priority("30");
		setAttributesBinary(cartItem, request);
		addAutoAttributes( request.getTaxiCoID(), cartItem, cartItem.getStopPoint(0).getPickupAddress(), closestAddrItem);
		
		cartItem.getServiceTypeAccount().setAccountSetId(CompanyDefaultValues.DEFAULT_PICKUP_ACCOUNT_SET_ID);		//default service type pickup 4
		cartItem.setAlready_Address_job(true);
		setCartPickupDTM(cartItem, request);
		
		validateAttributes(cartItem);
		return cartItem;
	}
	
	
	public void setAttributesBinary(CartItem cartItem, TSSRequireVehReq request){
		String sTSSAttributes = request.getAttributes();
		if(sTSSAttributes != null && !sTSSAttributes.isEmpty()){
			HashMap<String,String> tssAttrMap = cachedParam.getPFOSPTssAttrMapping();
			if(tssAttrMap != null && tssAttrMap.size() > 0){
				ArrayList<String> pfAttributes = new ArrayList<String>();
				StringTokenizer st = new StringTokenizer(sTSSAttributes, ",");
				while (st.hasMoreTokens())  {
	                String tssAtt = st.nextToken();
	                if(tssAtt != null && !tssAtt.isEmpty() && tssAttrMap.containsKey(tssAtt)){
	                	pfAttributes.add(tssAttrMap.get(tssAtt));
	                }
	            }
				if(pfAttributes.size() > 0){
					cartItem.setDriverAttributeBinary(Utilities.getPFAttributesBinaryByDesc(cachedParam.getFullDrvAttributeMap(request.getTaxiCoID()), pfAttributes));
					cartItem.setVehicleAttributeBinary(Utilities.getPFAttributesBinaryByDesc(cachedParam.getFullVehAttributeMap(request.getTaxiCoID()), pfAttributes));
				}
				
			}
			
		}
		
		
	}
	
	private void addAutoAttributes( int companyID, CartItem cartItem, AddressItem pickupAddress, AddressItem closestAddress ){

		DrvVehAttributes autoAttributes = null;
		if( pickupAddress!= null && !pickupAddress.getForcedAddressFlag() ) {
			autoAttributes = getAddressAttributes(pickupAddress, companyID, false);
		}else if(closestAddress!=null ){
			autoAttributes = getAddressAttributes(closestAddress, companyID, true);
		}
		
		if(autoAttributes != null){
			String binaryAttr = Utilities.binStringBitOR(cartItem.getDriverAttributeBinary(), autoAttributes.getDrvAttrBinary());
			if(binaryAttr != null && !binaryAttr.isEmpty()) cartItem.setDriverAttributeBinary(binaryAttr);
			binaryAttr = Utilities.binStringBitOR(cartItem.getVehicleAttributeBinary(), autoAttributes.getVehAttrBinary());
			if(binaryAttr != null && !binaryAttr.isEmpty()) cartItem.setVehicleAttributeBinary(binaryAttr);
		}
		  
	}
	
	
	private DrvVehAttributes getAddressAttributes(AddressItem address, int companyID, boolean isForcedAddress ){
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
			AddressItem addressItem = dao2.createAddressDeCarta(con, address, companyID, false);

			if( "Y".equals(cachedParam.getCompanyParameterValue(companyID, CompanyDefaultValues.COMP_PARAMETER_C_USE_AREA_ATTR_FLAG) )) 
			{
				drvAttributes = Utilities.ToBinaryAttributesString(addressItem.getDriverAttributes());
				vehAttributes = Utilities.ToBinaryAttributesString(addressItem.getVehicleAttributes());
				// findMissedUnExpAttributes(jobReq.getTaxiCoID(), drvAttributes, vehAttributes, missedAttributes);
									
			}
			// A forced address is forced to it's closest address in the database. Don't apply building attribute.
			if( !isForcedAddress && "Y".equals(cachedParam.getCompanyParameterValue(companyID, CompanyDefaultValues.COMP_PARAMETER_C_USE_ADDR_ATTR_FLAG)) )
			{
				drvAttributes = Utilities.binStringBitOR(drvAttributes, Utilities.ToBinaryAttributesString(addressItem.getDrvAddAttributes()) );
				vehAttributes = Utilities.binStringBitOR(vehAttributes, Utilities.ToBinaryAttributesString(addressItem.getVehAddAttributes()) );
					
			}
			
			if( "Y".equals(cachedParam.getCompanyParameterValue(companyID, CompanyDefaultValues.COMP_PARAMETER_C_USE_REGN_ATTR_FLAG) ) ){
				drvAttributes = Utilities.binStringBitOR(drvAttributes, Utilities.ToBinaryAttributesString(addressItem.getDrvRegAttributes()) );
				vehAttributes = Utilities.binStringBitOR(vehAttributes, Utilities.ToBinaryAttributesString(addressItem.getVehRegAttributes()) );
			}
			return new DrvVehAttributes(drvAttributes, vehAttributes);
			
		}catch(SQLException se){
			logger.error("address detail retrieval failed with exception", se );
			return null;
		}finally{
			if(cs != null) try{cs.close();}catch(SQLException ignore){};
	       	if(con != null) try{con.close();}catch(SQLException ignore){};
		}

	}
	
	private void setCartStopPoint(StopPoint stopPoint, TSSRequireVehReq request, PFAddressData pickup, AddressItem closestAddrItem){	
		AddressItem pickupAddress = null;
		if( pickup!=null){
			pickupAddress = getAddressItem(pickup, request.getTaxiCoID());
			
		}else if(closestAddrItem!=null){
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
		
		setContactInformation(stopPoint.getContactInfo(), request);

		//set driver notes (a.k.a. remarks) from OSP webbooker to show up in driver notes in callbooker
		String remarks = request.getDriverNotes();
		if (remarks != null && stopPoint.getPickupAddress()!=null) {
			remarks = remarks.trim();
			if (remarks.length() > 0) {
				stopPoint.getPickupAddress().setManualDrvNotes(remarks.trim()); //set the writable driver notes
			}
		}
		
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
	
	private void setInsertTSSJobResponse(ArrayList<Object> pfResults, TSSRequireVehReq request, TSSRequireVehRes response) throws SQLException{
		if(pfResults != null && pfResults.size() > 1 && "true".equals(pfResults.get(0))){
			response.setErrorCode(TSSErrorCode.TSS_SUCCESS);
			Integer jobID = (Integer)pfResults.get(1);
			response.setPfJobID( jobID == null? 0 : jobID.longValue() );
			insertExternalJobReference(jobID, request.getTssID(), ExternalSystemId.SYSTEM_ID_TSS_RIDER.getReference());
		}
	}
	
	private void insertExternalJobReference(int jobID, int referenceID, String jobOrigin) throws SQLException{
		Connection con = null;
		CallableStatement cs = null;
		try{
	       	con = pfDataSource.getConnection();
	       	cs = con.prepareCall("{ call pfadapter.insert_external_job_reference(?,?,?)}");
	       	cs.setLong(1, jobID);
	       	cs.setLong(2, referenceID); 
	       	cs.setString(3, jobOrigin);
	       	cs.execute();

		}finally{
	       	if(cs != null) try{cs.close();}catch(SQLException ignore){};
	       	if(con != null) try{con.close();}catch(SQLException ignore){};
		}
	}
	
	private void setCartPickupDTM(CartItem cartItem, TSSRequireVehReq request){
		Date pickupDTM = Utilities.convertOSPDefaultDate(request.getPickupTime());
		if (pickupDTM == null) {
			cartItem.setJOB_TYPE(OSPCartImplement.JOB_TYPE_ASAP);
		} else {
			cartItem.setJOB_TYPE(OSPCartImplement.JOB_TYPE_PBOK);
		}
		if(pickupDTM == null) pickupDTM = new Date();
		cartItem.setDateForMultilangual(pickupDTM);
		cartItem.setTime(Utilities.formatUtilDate(pickupDTM, "HH:mm"));
		cartItem.setDate(Utilities.formatUtilDate(pickupDTM, "EEEE, MMMM dd,yyyy"));
	}
	
	private void setContactInformation(ContactInformation contact, TSSRequireVehReq request){
		if(contact == null || request == null)  {
			return;
		}
		
		String accountCode = cachedParam.getCompanyParameterValue(request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_GENERAL_CASH_ACCOUNT_NR, CompanyDefaultValues.DEFAULT_CASH_ACCOUNT_NAME_NR);;
		String accountName = cachedParam.getCompanyParameterValue(request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_GENERAL_CASH_ACCOUNT_NAME, CompanyDefaultValues.DEFAULT_CASH_ACCOUNT_NAME_NR);
		
		
		//set the account to book with
		contact.setAccount(accountCode); //account card number
		contact.setAccount_Name(accountName); //account name
		contact.setAccount_Pin(null); 
		
		//OSP user selected advise arrival value 
		String[] pfCallout = new String[1];
		AdviseArrivalTypesImplement.getPFCalloutValue(pfDataSource, null, null, pfCallout);
		contact.setCallOutValue(pfCallout[0]);
	}
	
}
