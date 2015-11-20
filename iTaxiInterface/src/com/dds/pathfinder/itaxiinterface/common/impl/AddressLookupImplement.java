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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/common/impl/AddressLookupImplement.java $
 * 
 * PF-16772, 09/15/15, DChen, add unit match in OSP ALS.
 * 
 * PF-16385, 03/03/15, DChen, share with pfrest.
 * 
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.common.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.address.ejb.AddressDAO2;
import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.itaxiinterface.wslookup.ArrayOfPFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.CallbookerAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;

public abstract class AddressLookupImplement extends BaseImplement{
	
	private Logger logger = Logger.getLogger(this.getClass());
	protected IAddressLookup addressLookUp = null;
	
	public final static String ATTR_OSP_ORDER_FIND_ADDR = "orderby-in-findaddress";
	private String preDefinedOrderBy = null;
	
	public AddressLookupImplement(){
		super();
	}
	
	public AddressLookupImplement(IAddressLookup addressLookUp) {
		super();
		this.addressLookUp = addressLookUp;
	}
	
	   public PFAddressResponse getWSAddressData(String streetName, String streetNumber, String unit, 
				String region, String building, String organization, String postCode, int companyID, String orderBy){
		   addressLookUp = getAddressLookUp();   
		   if(addressLookUp != null){
			    // we need at least both region and street name to match address properly 
			    if (streetName != null && streetName.length() > 0
			    		// && streetNumber != null && streetNumber.length() > 0  C34708 to allow empty street nb
			    		&& region != null && region.length() > 0) {
			    	return addressLookUp.getAddressData(streetName, streetNumber, unit, 
							region, building, organization, postCode, companyID, orderBy);
			    }
			    return null;
		   }
		   return null;
		   
	   }

	/**
	 * Trim and append percentage character to string as specified
	 * 
	 * @param inputString	the string to trim and append
	 * @param appendPer		true to append percentage
	 * 					    false otherwise
	 * @return the updated string
	 */
	public String appendPercentage(String inputString, boolean appendPer) {
		if (inputString == null || inputString.trim().length() == 0) {
			if (appendPer) {
				return PERCENTAGE_CHAR;
			}
			return "";
		}
		inputString = inputString.trim();
		if (inputString.endsWith(PERCENTAGE_CHAR)) {
			return inputString; //already end with percentage, return the trimmed string
		}
		if (appendPer) {
			return inputString + PERCENTAGE_CHAR;
		}
		return inputString; //append percentage not requested, return the trimmed string

	}

	   public PFAddressResponse getWSClosestAddressData(Double latitude, Double longitude){
		   if(latitude == null || longitude == null) return null;
		   if(!isValidDoubleValue(latitude) || !isValidDoubleValue(longitude)) return null;
		   addressLookUp = getAddressLookUp();
		   if(addressLookUp != null){
				return addressLookUp.getClosestAddressData(latitude.doubleValue(), longitude.doubleValue());
		   }else{
			   return null;
		   }
		   
	  }
	   
	   
	   
		private IAddressLookup getAddressLookUp(){
			if(addressLookUp == null){
				long time = System.currentTimeMillis();
				CallbookerAddressLookup cbAddressLookup = new CallbookerAddressLookup();
				addressLookUp = cbAddressLookup.getBasicHttpBindingIAddressLookup();
				if(addressLookUp != null){
					logger.info("get address look up interface ....." + (System.currentTimeMillis() - time)/1000.00+"s");
				}else{
					logger.error("get address look up interface failed .....");
				}
			}
			return addressLookUp;
		}   
		

		   
		   public boolean isValidAddressResponse(PFAddressResponse addressResponse){
			   if(addressResponse == null) return false;
			   String status = addressResponse.getStatus().getValue();
			   int nbRecord = addressResponse.getNumberOfRecords();
			   return ((RESPONSE_SUCCESS.equalsIgnoreCase(status) || RESPONSE_SUCCESS_PARTIAL.equalsIgnoreCase(status)) && nbRecord > 0);
		   }
		   
		   public boolean isBothLandMarkAndAddressProvided(String streetName, String streetNb, String landMark){
			   return (streetName != null && streetName.trim().length() > 0
					   // && streetNb != null && streetNb.trim().length() > 0   C34708 to allow empty street nb
					   && landMark != null && landMark.trim().length() >0);
		   }		   
		   
		   public void modifyPickupClosestAddresses(PFAddressData pickupAddress, PFAddressData closestAddress){
			   if(pickupAddress == null && closestAddress != null){
				   if(!isValidDoubleValue(closestAddress.getLatitude()) || !isValidDoubleValue(closestAddress.getLongitude())){
					   setLowestBlocakFaceNumber(closestAddress);
				   }
			   }
		   }
		   
		   public void setLowestBlocakFaceNumber(PFAddressData address){
			   if(!isValidDoubleValue(address.getLowLatitude()) || !isValidDoubleValue(address.getLowLongitude()) || address.getLowBlockFaceNumber() == null) return;
			   
			   address.getStreetNumber().setValue(""+address.getLowBlockFaceNumber());
			   address.setLatitude(address.getLowLatitude());
			   address.setLongitude(address.getLowLongitude());
		   }
		   
		   /**
		    * Pick an address to use from the web service address match result.
		    * 
		    * @param addressResponse	the web service address match result
		    * @param streetName			the requested street name to match
		    * @param streetNum			the requested street no. to match
		    * @param region				the requested region to match
		    * @return the best match from the list of matching addresses
		    */
		   public PFAddressData getMatchAddress(PFAddressResponse addressResponse,
				   String streetName, String streetNum, String region){  
			   if(!isValidAddressResponse(addressResponse)) return null;
			   ArrayOfPFAddressData arrayOfData = addressResponse.getAddressData().getValue();
			   if(arrayOfData != null ) {
				    PFAddressData bestMatchSoFar = null;    
					List<PFAddressData> listOfData = arrayOfData.getPFAddressData();
					for (PFAddressData data : listOfData) {
						boolean streetNameMatch = true;
						boolean streetNumMatch = true;
						boolean regionMatch = true;
						if (streetName != null) {
							if (!streetName.equals(data.getStreetName().getValue())) {
								streetNameMatch = false; //street name provided and did not match result
							}
						} // else street name was not provided in search
						if (streetNum != null) {
							if (!streetNum.equals(data.getStreetNumber().getValue())) {
								streetNumMatch = false; //street number provided and did not match result
							}
						} // else street number was not provided in search
						if (region != null) {
							if (!region.equals(data.getRegionAbbreviation().getValue())) {
								regionMatch = false; //region provided and did not match result
							}
						} // else region was not provided in search
						if (streetNameMatch && streetNumMatch && regionMatch) {
							return data; //the most important criteria match, return this match
						}
						if (bestMatchSoFar == null && streetNameMatch && regionMatch) {
							bestMatchSoFar = data; //if we haven't found a match yet, 
												   //remember the one that has street name and region match
						}
					}
					if (bestMatchSoFar == null) {
						//we did our best to find a match, just return the first data here...
						return listOfData.get(0); 
					}
					return bestMatchSoFar;
			   }
			   return null;
			   
		   }
		   
		   public PFAddressData getMatchAddressWithUnit(PFAddressResponse addressResponse,
				   String streetName, String streetNum, String region, String unitNumber){  
			   if(!isValidAddressResponse(addressResponse)) return null;
			   ArrayOfPFAddressData arrayOfData = addressResponse.getAddressData().getValue();
			   if(arrayOfData != null ) {
				    PFAddressData bestMatchSoFar = null;    
					List<PFAddressData> listOfData = arrayOfData.getPFAddressData();
					for (PFAddressData data : listOfData) {
						boolean streetNameMatch = true;
						boolean streetNumMatch = true;
						boolean regionMatch = true;
						boolean unitMatch = true;
						if (streetName != null) {
							if (!streetName.equals(data.getStreetName().getValue())) {
								streetNameMatch = false; //street name provided and did not match result
							}
						} // else street name was not provided in search
						if (streetNum != null) {
							if (!streetNum.equals(data.getStreetNumber().getValue())) {
								streetNumMatch = false; //street number provided and did not match result
							}
						} // else street number was not provided in search
						if (region != null) {
							if (!region.equals(data.getRegionAbbreviation().getValue())) {
								regionMatch = false; //region provided and did not match result
							}
						} // else region was not provided in search
						String unit = data.getUnitNumber().getValue();
						if(unitNumber != null && !unitNumber.isEmpty()){
							unitMatch = unitNumber.equals(unit);
						}else{
							unitMatch = (unit == null || unit.isEmpty());
						}
						
						if (streetNameMatch && streetNumMatch && regionMatch && unitMatch) {
							return data; //the most important criteria match, return this match
						}
						
						if (bestMatchSoFar == null && streetNumMatch && streetNameMatch && regionMatch) {
							bestMatchSoFar = data; //if we haven't found a match yet, 
												   //remember the one that has street name and region match
						}
					}
					if (bestMatchSoFar == null) {
						//we did our best to find a match, just return the first data here...
						return listOfData.get(0); 
					}
					return bestMatchSoFar;
			   }
			   return null;
			   
		   }
		   
		   public PFAddressData getFirstAddress(PFAddressResponse addressResponse){   
			   PFAddressData addressContent = null;
			   if(!isValidAddressResponse(addressResponse)) return addressContent;
			   
			   ArrayOfPFAddressData arrayOfData = addressResponse.getAddressData().getValue();
			   if(arrayOfData != null ){
					List<PFAddressData> listOfData = arrayOfData.getPFAddressData();
					addressContent = listOfData.get(0);
			   }
			   return addressContent;
		   }		
		   
		   public AddressItem getAddressItem(Connection con, PFAddressData address, int taxiCoID){
			   if(con == null || address == null) return null;
			   AddressItem addressItem = createAddressItem(address, taxiCoID);
			   AddressDAO2 dao = new AddressDAO2();
			   try{
				   return dao.createAddressDeCarta(con, addressItem, addressItem.getTaxiCoId(), false);
			   }catch(SQLException se){
				   logger.error("getAddressItem failed: ", se);
				   return null;
			   }
		   }
		   
			private AddressItem createAddressItem(PFAddressData addressContent, int taxiCoID){
				int addressID = addressContent.getAddressIDUnit();
				if(addressID <= 0) addressID = addressContent.getAddressIDBuilding();
				
				AddressItem addressItem = new AddressItem(addressID, //address_id
						addressContent.getStreetName().getValue(), addressContent.getStreetNumber().getValue(),
						addressContent.getOrganizationName().getValue(), addressContent.getLandmarkName().getValue(),
						addressContent.getRegionAbbreviation().getValue(), addressContent.getUnitNumber().getValue(), //unit number
						addressContent.getAreaName().getValue(), addressContent.getBlockFaceID(),
						addressContent.getAreaID(),"",
						"","",0);
				addressItem.setLatitude(addressContent.getLatitude().floatValue());
				addressItem.setLongitude(addressContent.getLongitude().floatValue());
				addressItem.setStreetNameID(addressContent.getStreetNameID());
				addressItem.setBuildingID(addressContent.getBuildingID());
				addressItem.setUnitId(0);					//unit should always be 0
				addressItem.setPostalCode(addressContent.getPostCode().getValue());
				addressItem.setLowLatitude(addressContent.getLowLatitude().floatValue());
				addressItem.setLowLongitude(addressContent.getLowLongitude().floatValue());
				addressItem.setHighLatitude(addressContent.getHighLatitude().floatValue());
				addressItem.setHighLongitude(addressContent.getHighLongitude().floatValue());
				addressItem.setLowestNumber(addressContent.getLowBlockFaceNumber());
				addressItem.setHighestNumber(addressContent.getHighBlockFaceNumber());
				addressItem.setTaxiCoId(taxiCoID);
				addressItem.setRegionID(addressContent.getRegionID());
				
				return addressItem;
			}	
		   
			public int getWSAddressID(PFAddressData address){
				if(address == null) return 0;
				int addressID = address.getAddressIDUnit();
				if(addressID <= 0) addressID = address.getAddressIDBuilding();
				return addressID;
			}
			
			public boolean isValidAddressItem(Connection con, PFAddressData address, int taxiCoID){
				AddressItem addressItem = getAddressItem(con, address, taxiCoID);
				if(addressItem == null) return false;
				else return (!addressItem.getHold_Flag());      //C34861, check address service flag
			}
			
			public String getPreDefinedOrderBy(){
		    	if(preDefinedOrderBy != null) return preDefinedOrderBy;
		    	else{					//check xml configuration
		    		preDefinedOrderBy = "";
		    		if(cachedParam != null && cachedParam.getPFOSPConfigAttributes() != null && 
		    				cachedParam.getPFOSPConfigAttributes().keySet() != null && cachedParam.getPFOSPConfigAttributes().keySet().contains(ATTR_OSP_ORDER_FIND_ADDR)){
		    			preDefinedOrderBy = cachedParam.getPFOSPConfigAttributes().get(ATTR_OSP_ORDER_FIND_ADDR);
		    		}
		    		return preDefinedOrderBy;
		    	}
		    }
			
		   
}
