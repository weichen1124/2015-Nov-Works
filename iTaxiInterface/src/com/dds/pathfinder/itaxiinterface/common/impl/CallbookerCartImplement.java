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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/common/impl/CallbookerCartImplement.java $
 * 
 * PF-16385, 03/03/15, DChen, share with pfrest.
 * 
 * 
 * ******/
package com.dds.pathfinder.itaxiinterface.common.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.CartItem;
import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;

public abstract class CallbookerCartImplement extends AddressLookupImplement {
	
	private static Logger logger = Logger.getLogger(CallbookerCartImplement.class);
	
	public CallbookerCartImplement(){
		super();
	}
	
	public CallbookerCartImplement(IAddressLookup addressLookUp){
		super(addressLookUp);
	}
	
	protected boolean validateAddresses(boolean forcedAddress, PFAddressResponse pickupAddress, PFAddressResponse closestAddress){
	    return (isValidAddressResponse(pickupAddress) || (forcedAddress && isValidAddressResponse(closestAddress)));
	}
	
	public void setAddressItem(AddressItem address, PFAddressData addressData, String forceUnitNr){
		if(address == null || addressData == null) return;
		address.setAddressId(getWSAddressID(addressData));
		//address.setAddressId(addressItem.getAddressId());
		address.setAreaId(getWSIntegerValue(addressData.getAreaID()));
		address.setBlockFaceId(getWSIntegerValue(addressData.getBlockFaceID()));
		address.setRegionID(getWSIntegerValue(addressData.getRegionID()));
		address.setStreetNameID(getWSIntegerValue(addressData.getStreetNameID()));
		
		address.setStrName(getWSStringValue(addressData.getStreetName().getValue()));
		address.setStrNum(getWSStringValue(addressData.getStreetNumber().getValue()));
		
		//Following JCallbooker behaviour, user entered unit number 
		//is put into StopPointItem's forcedUnitNum via AddressItem's unitNr
		if (forceUnitNr != null && forceUnitNr.length() > 0) {
			address.setunitNr(forceUnitNr);
		} else {
			address.setunitNr(getWSStringValue(addressData.getUnitNumber().getValue()));
		}
		
		address.setRegion(getWSStringValue(addressData.getRegionAbbreviation().getValue()));
		address.setAreaName(getWSStringValue(addressData.getAreaName().getValue()));
		address.setBuilding_Name(getWSStringValue(addressData.getLandmarkName().getValue()));
		address.setOrganisation(getWSStringValue(addressData.getOrganizationName().getValue()));
		address.setPostalCode(getWSStringValue(addressData.getPostCode().getValue()));
		
		address.setLatitude(getWSDoubleValue(addressData.getLatitude()));
		address.setLongitude(getWSDoubleValue(addressData.getLongitude()));
		address.setLowLatitude(getWSDoubleValue(addressData.getLowLatitude()));
		address.setLowLongitude(getWSDoubleValue(addressData.getLowLongitude()));
		address.setHighLatitude(getWSDoubleValue(addressData.getHighLatitude()));
		address.setHighLongitude(getWSDoubleValue(addressData.getHighLongitude()));
		
		address.setLowestNumber(getWSIntegerValue(addressData.getLowBlockFaceNumber()) );
		address.setHighestNumber(getWSIntegerValue(addressData.getHighBlockFaceNumber()));
		
	}
	
	public ArrayList<String> insertRequestAttributes(int companyID, String drvAttributes, String vehAttributes, ArrayList<SysAttrListItem> missedAttributes){
	
		findMissedUnExpAttributes(companyID, drvAttributes, vehAttributes, missedAttributes);   //PF-16172, all requested attributes should include not exposed attributes.
		
		ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>(); 
		// CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
		attributes.addAll(Utilities.getAttributesItem(cachedParam.getDrvAttributeMap().get(companyID), drvAttributes));
		attributes.addAll(Utilities.getAttributesItem(cachedParam.getVehAttributeMap().get(companyID), vehAttributes));
		
		return Utilities.getAttrShortNameList(attributes); 
	}
	
	public void findMissedUnExpAttributes(int taxiCoID, String drvAttributes, String vehAttributes, ArrayList<SysAttrListItem> missedAttributes){
		ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>(); 
		attributes.addAll(Utilities.getAttributesItem(cachedParam.getUnExpDrvAttributeMap().get(taxiCoID), drvAttributes));
		attributes.addAll(Utilities.getAttributesItem(cachedParam.getUnExpVehAttributeMap().get(taxiCoID), vehAttributes));
		
		if(attributes != null && attributes.size() > 0){
			missedAttributes.removeAll(attributes);
			missedAttributes.addAll(attributes);
		}
	}
	
	public void setAttributesBinary(CartItem cartItem, String[] attributes){
		if(attributes != null && attributes.length > 0){
			// CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
			cartItem.setDriverAttributeBinary(Utilities.getPFAttributesBinary(cachedParam.getDrvAttributeMap().get(cartItem.getTaxiCompanyID()), attributes));
			cartItem.setVehicleAttributeBinary(Utilities.getPFAttributesBinary(cachedParam.getVehAttributeMap().get(cartItem.getTaxiCompanyID()), attributes));
		}
	}
	
	public int getWSIntegerValue(Integer value){
		return (value == null? 0:value);
	}
	
	public String getWSStringValue(String value){
		return (value == null? "":value);
	}
	
	public float getWSDoubleValue(Double value){
		return (value == null? 0.00f:value.floatValue());
	}
	
	public void validateAttributes(CartItem cartItem, ArrayList<SysAttrListItem> missedAttributes){
		
		String drvAttributes = cartItem.getDriverAttributeBinary();
		String vehAttributes = cartItem.getVehicleAttributeBinary();
		ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>(); 
		// CompanyDefaultValues.getDefaultAttributesMaps(pfDataSource);
		attributes.addAll(Utilities.getAttributesItem(cachedParam.getDrvAttributeMap().get(cartItem.getTaxiCompanyID()), drvAttributes));
		attributes.addAll(Utilities.getAttributesItem(cachedParam.getVehAttributeMap().get(cartItem.getTaxiCompanyID()), vehAttributes));
		
		if(missedAttributes != null && missedAttributes.size() > 0){
			attributes.addAll(missedAttributes);
			addMissedAttributes(cartItem, missedAttributes);
		}
		
	
		cartItem.setAttribute_lead_Time(calculateAttrLeadTime(attributes, cachedParam.getAttributeLeadTime()));
		setCartItemLeadTime(cartItem, cachedParam.getSystemParameterIntValue("DEFAULT_LEAD_TIME"));
		
		cartItem.setAttribute_priority(calculateAttrPriority(attributes, cachedParam.getAttributePriority()));
		setCartItemPriority(cartItem);
		
		String separator = cachedParam.getCompanyParameterValue(cartItem.getTaxiCompanyID(), "C_NOTES_SEPARATOR");
		String attrNotes = getAttrNotes(cartItem.getOperatorNotes(), attributes, cachedParam.getAttributeOptNotes(), separator);
		cartItem.setOperatorNotes(Utilities.addNotes(cartItem.getOperatorNotes(), attrNotes , separator));
		attrNotes = getAttrNotes(cartItem.getStopPoint(0).getDrvNotes(), attributes, cachedParam.getAttributeDrvNotes(), separator);
		cartItem.getStopPoint(0).setDrvNotes(attrNotes);
		
		if(!cartItem.getDisable_Reservable_Flag()) cartItem.setReservableFlagInAttributes(isAttrReservable(attributes, cachedParam.getAttributeReservableFlag()));
		else cartItem.setReservableFlagInAttributes(false);
	}
	
	public static int calculateAttrLeadTime(ArrayList<SysAttrListItem> attributes, HashMap<String, String> attrLeadTime){
		int attributeTotalLeadTime = 0;
		if(attributes == null || attributes.size() == 0 || attrLeadTime == null || attrLeadTime.size() == 0) return attributeTotalLeadTime;
		for(SysAttrListItem attr : attributes){
			if(attrLeadTime.containsKey(attr.getAttrShortName())){
				try{
					attributeTotalLeadTime += Integer.parseInt(attrLeadTime.get(attr.getAttrShortName()));
				}catch(NumberFormatException ne){
					logger.error("calculateAttrLeadTime parseInt failed: ", ne);
				}
			}
		}
        return attributeTotalLeadTime;
	}
	
	public static void setCartItemLeadTime(CartItem cart, int defaultLeadTime){
		int totalLeadTime = 0;
		try{
			totalLeadTime = Integer.parseInt(cart.getTotal_Lead_Time());
		}catch(NumberFormatException ne){
			logger.warn("current cart lead time in invalid format: " + cart.getTotal_Lead_Time());
		}
		if(totalLeadTime == 0){
			totalLeadTime = defaultLeadTime;
			logger.info("using totalLeadTime - " + totalLeadTime);
		}
		
		totalLeadTime = (int)Math.ceil((totalLeadTime + cart.getAttribute_lead_Time())/60.0);
		cart.setTotal_Lead_Time("" + totalLeadTime);
		// cart.setLead_Time("" + totalLeadTime);
	}
	
	public static void setCartItemPriority(CartItem cart){ 
		int priority = 30;
		try{
			priority = Integer.parseInt(cart.getJob_priority());
		}catch(NumberFormatException ne){
			logger.warn("current cart priority in invalid format: " + cart.getJob_priority());
		}
		cart.setJob_priority("" + Math.min(priority, cart.getAttribute_priority()));
		if(!cart.getJob_priority_Flag()){
			cart.setManualJobPriority(cart.getJob_priority());
		}
		
	}
	
	public static int calculateAttrPriority(ArrayList<SysAttrListItem> attributes, HashMap<String, String> attrPriority){
		int maxAttrPriority = 100;
		if(attributes == null || attributes.size() == 0 || attrPriority == null || attrPriority.size() == 0) return maxAttrPriority;
		for(SysAttrListItem attr : attributes){
			if(attrPriority.containsKey(attr.getAttrShortName())){
				try{
					maxAttrPriority = Math.min(maxAttrPriority, Integer.parseInt(attrPriority.get(attr.getAttrShortName())) );
				}catch(NumberFormatException ne){
					logger.error("calculateAttrPriority parseInt failed: ", ne);
				}
			}
		}
        return maxAttrPriority;
	}
	
	
	
	public static String getAttrNotes(String origNotes, ArrayList<SysAttrListItem> attributes, HashMap<String, String> attrNotes, String separator){
		String notes = origNotes;
		if(attributes == null || attributes.size() == 0 || attrNotes == null || attrNotes.size() == 0) return notes;
		for(SysAttrListItem attr : attributes){
			if(attrNotes.containsKey(attr.getAttrShortName())){
				notes = Utilities.addNotes(notes, attrNotes.get(attr.getAttrShortName()), separator);
			}
		}

		return notes;		
	}
	
	public static boolean isAttrReservable(ArrayList<SysAttrListItem> attributes, HashMap<String, String> attrReservable){
		boolean reservable = false;
		if(attributes == null || attributes.size() == 0 || attrReservable == null || attrReservable.size() == 0) return reservable;
		for(SysAttrListItem attr : attributes){
			if(attrReservable.containsKey(attr.getAttrShortName()) && "Y".equalsIgnoreCase(attrReservable.get(attr.getAttrShortName()))){
				reservable = true;
				break;
			}
		}
        return reservable;
	}
	
	public void addMissedAttributes(CartItem cartItem, ArrayList<SysAttrListItem> missedAttributes){
		if(missedAttributes == null || missedAttributes.size() == 0) return;
		
		String drvAttributeBinary = Utilities.binStringBitOR(cartItem.getDriverAttributeBinary(), 
												Utilities.getPFAttributesBinary(cachedParam.getUnExpDrvAttributeMap().get(cartItem.getTaxiCompanyID()), missedAttributes));
		String vehAttributeBinary = Utilities.binStringBitOR(cartItem.getVehicleAttributeBinary(), 
												Utilities.getPFAttributesBinary(cachedParam.getUnExpVehAttributeMap().get(cartItem.getTaxiCompanyID()), missedAttributes));
		
		cartItem.setDriverAttributeBinary(drvAttributeBinary);
		cartItem.setVehicleAttributeBinary(vehAttributeBinary);
	}
	
	public void insertExternalJobReference(DataSource pfDataSource, Long jobId, String referenceID, String jobOrigin){
		Connection con = null;
		CallableStatement cs = null;
		try{
	       	con = pfDataSource.getConnection();
	       	cs = con.prepareCall("{ call pfadapter.insert_external_job_reference(?,?,?)}");
	       	cs.setLong(1, jobId);
	       	cs.setString(2, referenceID); 
	       	cs.setString(3, jobOrigin);
	       	cs.execute();

	       }catch(SQLException se){
		       	logger.error("insert external job reference failed with exception", se);	  
		       	
	       }finally{
	       	if(cs != null) try{cs.close();}catch(SQLException ignore){};
	       	if(con != null) try{con.close();}catch(SQLException ignore){};
	       }
	}
	
}
