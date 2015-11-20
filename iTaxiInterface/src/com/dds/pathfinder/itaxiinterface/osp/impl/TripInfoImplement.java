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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/TripInfoImplement.java $
 * 
 * 5     2/12/11 10:01a Ezhang
 * C36130 Added system id validation.
 * 
 * 4     8/18/11 3:33p Dchen
 * upgrade to 3.75.
 * 
 * 3     1/02/11 7:02p Mkan
 * - upcaseRequest(): use upcaseRequest() in Utilities. (C34954)
 * 
 * 2     5/03/10 3:46p Dchen
 * upcase input address.
 * 
 * 1     2/23/10 2:39p Dchen
 * OSP interface.
 * 
 * 
 * ******/
package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.citynav.model.TesTripCalc;
import com.dds.pathfinder.callbooker.server.citynav.model.TesTripCalcReply;
import com.dds.pathfinder.callbooker.server.citynav.model.TripAddr;
import com.dds.pathfinder.callbooker.server.controller.event.PfaEvent;
import com.dds.pathfinder.callbooker.server.controller.event.ZoneStatisticsEvent;
import com.dds.pathfinder.callbooker.server.datacache.AreaStatisticsResults;
import com.dds.pathfinder.callbooker.server.facade.ejb.CallbookerFacade;
import com.dds.pathfinder.callbooker.server.pfa.model.PfaItem;
import com.dds.pathfinder.callbooker.server.pfa.model.TripPriceRequest;
import com.dds.pathfinder.callbooker.server.pfa.model.TripPriceResult;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.TripInfoRequest;
import com.dds.pathfinder.itaxiinterface.webservice.TripInfoResponse;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;

public class TripInfoImplement extends OSPAddrLookupImplement{
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	private DataSource pfDataSource;
	private CallbookerFacade facade;
    
	private PFAddressResponse pickupAddressResp;
	private PFAddressResponse setdownAddressResp;
	
	public TripInfoImplement(DataSource pfDataSource,IAddressLookup addressLookUp, CallbookerFacade facade) {
		super(addressLookUp);
		this.pfDataSource = pfDataSource;
		this.facade = facade;
	}

	public TripInfoResponse generateResponse(BaseReq request) {
		return generateTripInfoResponse((TripInfoRequest)request);
	}
	
	public TripInfoResponse generateTripInfoResponse(TripInfoRequest request){
		TripInfoResponse response = new TripInfoResponse();
		//validate system id
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);	
		}
		if(!isValidTripInfoRequest(request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			return response;
		}else{
			PFAddressData pickupAddress = getFirstAddress(pickupAddressResp);
			response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
			response.setPickupZoneNumber(pickupAddress.getAreaName().getValue());
			AddressItem pickup = getAddressItem(pickupAddress, request.getTaxiCoID());
			
			checkZoneStatistics(pickup, request, response);
			if(isValidSetdownAddress(request)){
				PFAddressData setdownAddress = getFirstAddress(setdownAddressResp);
				AddressItem setdown = getAddressItem(setdownAddress, request.getTaxiCoID());
				checkTripMatrix(pickup, setdown, request, response);
			}
		}
		return response;
	}
	
	private AddressItem getAddressItem(PFAddressData addressData, int taxiCoID){
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
	
	private void checkZoneStatistics(AddressItem pickupAddress, TripInfoRequest request, TripInfoResponse response){
		Connection con = null;
		try{
			con = pfDataSource.getConnection();
			
			
			if(pickupAddress != null){
				byte[] drvAttr = new byte[16];
				byte[] vehAttr = new byte[16];
				for(int i=0; i<16; i++){
			          drvAttr[i] = (byte)0x00;
			          vehAttr[i] = (byte)0x00;
			    }  

				int mapScale = CompanyDefaultValues.getSystemParameterIntValue(pfDataSource, CompanyDefaultValues.SYSTEM_PARAMETER_MAP_SCALE, 4);
								
				ZoneStatisticsEvent zoneEvent = new ZoneStatisticsEvent(pickupAddress.getAreaId(), pickupAddress.getX_coordinate(), pickupAddress.getY_coordinate(), 
						pickupAddress.getGdesp_max_radius()/mapScale, drvAttr, vehAttr, ZoneStatisticsEvent.TYPE_PRIMARY_ZONE);
				// CallbookerFacade facade = facadeHome.create();
				AreaStatisticsResults statisResult = facade.handleZoneStatisticsEvent(zoneEvent);
				setStatisticsResponse(statisResult, response, true);
				zoneEvent.setActionType(ZoneStatisticsEvent.TYPE_SURROUND_ZONE);
				statisResult = facade.handleZoneStatisticsEvent(zoneEvent);
				setStatisticsResponse(statisResult, response, false);
			}
		}catch(Exception se){
			logger.error("checkZoneStatistics failed....", se);
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
		}finally{
			if(con != null) try{con.close();}catch(SQLException ignore){};
		}
	}
	
	private void setStatisticsResponse(AreaStatisticsResults result, TripInfoResponse response, boolean isPrimaryZone){
		if(isPrimaryZone){
			response.setNofCarsInPrimaryZones(result.getNbOfFree());
			response.setNofCarsSoonToClear(result.getNbOfSTC());
		}else{
			response.setNofCarsInBackupZones(result.getNbOfFree());
			response.setNofTripsInBackupZones((result.getNbOfUnderDispatch() + result.getNbOfSTC()));
		}
	}
	
	
	private void checkTripMatrix(AddressItem pickupAddress, AddressItem setdownAddress, TripInfoRequest request, TripInfoResponse response){
		String navType = CompanyDefaultValues.getSystemParameterValue(pfDataSource, TripPriceRequest.SYSTEM_PARAMETER_S_NAV_TYPE);
		if(TripPriceRequest.NAV_TYPE_TTES.equalsIgnoreCase(navType)){
			checkTTESTripMatrix(pickupAddress, setdownAddress, request, response);
		}else{
			checkOtherTripMatrix(pickupAddress, setdownAddress, request, response, navType);
		}
	}
	
	private void checkTTESTripMatrix(AddressItem pickupAddress, AddressItem setdownAddress, TripInfoRequest request, TripInfoResponse response){
		if(pickupAddress == null || setdownAddress == null) return;
		
		ArrayList<TripAddr> tripAddrArray = new ArrayList<TripAddr>();
		TripAddr tripAddress = getTripAddr(pickupAddress, TripAddr.ADDRESS_TYPE_PICKUP);
		if(tripAddress != null) tripAddrArray.add(tripAddress);
		tripAddress = getTripAddr(setdownAddress, TripAddr.ADDRESS_TYPE_SETDOWN);
		if(tripAddress != null) tripAddrArray.add(tripAddress);
		
        TesTripCalc ttesRequest = new TesTripCalc(0, "", new Date(), "", tripAddrArray);
		
        ttesRequest.setServer(CompanyDefaultValues.getCompanyParameterValue(pfDataSource, request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_CITYNAV_SERVER));
        ttesRequest.setPort(CompanyDefaultValues.getCompanyParameterIntValue(pfDataSource, request.getTaxiCoID(), CompanyDefaultValues.COMP_PARAMETER_C_CITYNAV_PORT));
        ttesRequest.setRequestTimeout(CompanyDefaultValues.getSystemParameterIntValue(pfDataSource, CompanyDefaultValues.SYSTEM_PARAMETER_S_NAV_RESP_TIMEOUT, 5)*1000);
        try{
        	// CallbookerFacade facade = facadeHome.create();
        	TesTripCalcReply ttesResponse = facade.handleCityNavRequest(ttesRequest);
        	if(ttesResponse == null) return;
        	int tripTime = 0;
			try{
				tripTime = Integer.parseInt(ttesResponse.getTripTime());
			}catch(NumberFormatException ne){
			}
			response.setSvcAvgTime(tripTime);	
			response.setRateQuote(""+ttesResponse.getEstimatedPrice());
        }catch(Exception e){
			logger.error("checkTTESTripMatrix failed....", e);
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
		}
	}
	
	private void checkOtherTripMatrix(AddressItem pickupAddress, AddressItem setdownAddress, TripInfoRequest request, TripInfoResponse response, String navType){
		if(pickupAddress == null || setdownAddress == null) return;
		PfaEvent pfaEvent = createPfaEvent(pickupAddress, setdownAddress, request, navType);
		try{
			// CallbookerFacade facade = facadeHome.create();
			PfaItem pfaResult = facade.handlePfaEvent(pfaEvent);
			if(pfaResult == null) return;
			TripPriceResult result = pfaResult.getPriceResult();
			if(result == null) return;
			if(result.getStatus() != TripPriceResult.SUCCESS){
				response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
			}else{
				response.setSvcAvgTime(result.getTripTime());
				response.setRateQuote("" + result.getPrice());
			}
			
		}catch(Exception e){
			logger.error("checkOtherTripMatrix failed", e);
			response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
		}
		
	}
	
	private PfaEvent createPfaEvent(AddressItem pickupAddress, AddressItem setdownAddress, TripInfoRequest request, String navType){
		TripPriceRequest priceRequest = new TripPriceRequest();
		
		priceRequest.pickupStreetNumber = pickupAddress.getStrNum();
		priceRequest.pickupStreetName = pickupAddress.getStrName();
		priceRequest.pickupAreaId = pickupAddress.getAreaId();
		priceRequest.pickupAddressId = pickupAddress.getAddressId();

		priceRequest.destStreetNumber = setdownAddress.getStrNum();
		priceRequest.destStreetName = setdownAddress.getStrName();
		priceRequest.destAreaId = setdownAddress.getAreaId();
		priceRequest.destAddressId = setdownAddress.getAddressId();

		priceRequest.taxiCoId = request.getTaxiCoID();
		Date currentDate = new Date();
		priceRequest.pickupDate = Utilities.formatUtilDate(currentDate, Utilities.CALLBOOKER_DEFAULT_DATE_FORMAT);
		
		String accountCode = request.getAccountCode();
		if(accountCode == null || accountCode.trim().length() == 0) {
			accountCode = CompanyDefaultValues.getCompanyDefaultCashAccountNb(pfDataSource, request.getTaxiCoID());
		}
		setAccountIDs(priceRequest, accountCode);
		setAttributes(priceRequest);
		priceRequest.accountSetId = CompanyDefaultValues.DEFAULT_PICKUP_ACCOUNT_SET_ID;
		priceRequest.numberOfVias = 0;
		priceRequest.setNavigationEngine(navType);
		priceRequest.priceType = TripPriceRequest.TRIP_MATRIX;
		
		return new PfaEvent(PfaEvent.PRICE_REQUEST, priceRequest, "", "");
		
	}
	
	private void setAttributes(TripPriceRequest priceRequest){
		StringBuffer attrBinary = new StringBuffer();
        for(int i=0; i<Utilities.ATTRIBUTES_BITS_NUMBER; i++ ) attrBinary.append("0");
        priceRequest.driverAttributes = attrBinary.toString();
        priceRequest.vehicleAttributes = attrBinary.toString();
	}
	
	private void setAccountIDs(TripPriceRequest priceRequest, String accountCode){
		try{
			int[] accountDetails = getAccountIDs(accountCode);
			priceRequest.accountId = accountDetails[0];
			priceRequest.account_Profile_ID = accountDetails[1];
		}catch(SQLException se){
			logger.error("setAccountIDs failed", se);
		}
		
	}
	
	public int[] getAccountIDs(String accountCode) throws SQLException {
        int[] account_Details  = {0,0};
      String query = "select   ACCT_USER_ID,profile_id from account_users where   ACCT_CARD_NR  ='" + accountCode + "'";
      Connection con=null;
      Statement stmt =null;
      ResultSet result =null;
      try {
        con = pfDataSource.getConnection();
        stmt = con.createStatement();
        result = stmt.executeQuery(query);

        if (result.next()) {
        	account_Details[0] = result.getInt("ACCT_USER_ID");
        	account_Details[1] = result.getInt("profile_id");
         }
      }finally {
    	  if(result != null) try{result.close();}catch(SQLException ignore){};
    	  if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
    	  if(con != null) try{con.close();}catch(SQLException ignore){};
      }        
      return account_Details;
	}
	
	
	private boolean isValidTripInfoRequest(TripInfoRequest request){
		if(request == null || request.getTaxiCoID() == null || request.getTaxiCoID() <= 0) return false;
		upcaseRequest(request);
		return isValidPickupAddress(request);
	}
	
	
	private void upcaseRequest(TripInfoRequest request){
		request.setPickupStreetName(Utilities.upcaseString(request.getPickupStreetName()));
		request.setPickupLandmark(Utilities.upcaseString(request.getPickupLandmark()));
		request.setPickupRegion(Utilities.upcaseString(request.getPickupRegion()));
		   
		request.setDropoffStreetName(Utilities.upcaseString(request.getDropoffStreetName()));
		request.setDropoffLandmark(Utilities.upcaseString(request.getDropoffLandmark()));
		request.setDropoffRegion(Utilities.upcaseString(request.getDropoffRegion()));
	}
	
	private boolean isEmptyValue(String value){
		return (value == null || value.trim().length() == 0);
	}
	
	
	private boolean isValidPickupAddress(TripInfoRequest request){
		if((isEmptyValue(request.getPickupStreetName()) || isEmptyValue(request.getPickupStreetNumber()))
				&& isEmptyValue(request.getPickupLandmark())) return false;
			
		String strName = appendPercentage(request.getPickupStreetName(), false);
		String strNumber = appendPercentage(request.getPickupStreetNumber(), false);
		String unit = appendPercentage(request.getPickupUnitNumber(), true);
		String region = appendPercentage(request.getPickupRegion(),false);
		String landMark = appendPercentage(request.getPickupLandmark(), false);
		   	   	 
		pickupAddressResp = getWSAddressData(strName, strNumber, unit, 
				   region, landMark, PERCENTAGE_CHAR, PERCENTAGE_CHAR, request.getTaxiCoID(), "");
		return isValidAddressResponse(pickupAddressResp);

	}
	
	private boolean isValidSetdownAddress(TripInfoRequest request){
		if((isEmptyValue(request.getDropoffStreetName()) || isEmptyValue(request.getDropoffStreetNumber()))
				&& isEmptyValue(request.getDropoffLandmark())) return false;
		
		String strName = appendPercentage(request.getDropoffStreetName(), false);
		String strNumber = appendPercentage(request.getDropoffStreetNumber(), false);
		String unit = appendPercentage(request.getDropoffUnitNumber(), true);
		String region = appendPercentage(request.getDropoffRegion(),false);
		String landMark = appendPercentage(request.getDropoffLandmark(), false);
		   	   	 
		setdownAddressResp = getWSAddressData(strName, strNumber, unit, 
				   region, landMark, PERCENTAGE_CHAR, PERCENTAGE_CHAR, request.getTaxiCoID(), "");
		return isValidAddressResponse(setdownAddressResp);
		
	}
	
	
	public TripAddr getTripAddr(AddressItem addressItem, String type) {
		TripAddr tripAddr = null;
		long addressId;
		String streetName;
		String streetNr;
		String apartment;
		String city;
		String zone;
		long gpsLong;
		long gpsLat;

		if (addressItem != null && addressItem.getAddressId() != 0 ) {
			addressId = addressItem.getAddressId();
			streetName = addressItem.getStrName();
			streetNr = addressItem.getStrNum();
			apartment = addressItem.getunitNr();
			city = addressItem.getRegion();
			zone = addressItem.getAreaName();
			gpsLong = (long)(addressItem.getLongitude() * 1000000);
			gpsLat = (long)(addressItem.getLatitude() * 1000000);

			tripAddr = new TripAddr(addressId, streetName, streetNr, apartment, city, zone, gpsLong,
					gpsLat, type);
		}
		return tripAddr;
	}


}
