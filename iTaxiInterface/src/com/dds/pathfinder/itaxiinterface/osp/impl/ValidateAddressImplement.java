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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/ValidateAddressImplement.java $
 * 
 * PF-16398, 04/06/15, DChen, add isAddressServiceable service
 * 
 * 8     2/12/11 10:01a Ezhang
 * C36130 added system id validation.
 * 
 * 7     8/19/11 3:38p Dchen
 * port C34861, 34862, 35460 here.
 * 
 * 6     1/02/11 7:02p Mkan
 * - upcaseRequest(): use upcaseRequest() in Utilities. (C34954)
 * 
 * 5     5/07/10 2:59p Dchen
 * handle user input landmark with street name and nb.
 * 
 * 4     5/03/10 3:47p Dchen
 * upcase input address.
 * 
 * 3     2/23/10 2:39p Dchen
 * OSP interface.
 * 
 * 2     2/12/10 5:15p Dchen
 * Modified for web booker using.
 * 
 * 1     1/11/10 1:32p Dchen
 * OSP interface.
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.itaxiinterface.webservice.AddressListItem;
import com.dds.pathfinder.itaxiinterface.webservice.AddressReq;
import com.dds.pathfinder.itaxiinterface.webservice.AddressRes;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.BaseRes;
import com.dds.pathfinder.itaxiinterface.webservice.GPSServiceableRequest;
import com.dds.pathfinder.itaxiinterface.webservice.GPSServiceableResponse;
import com.dds.pathfinder.itaxiinterface.webservice.GPSServiceableResponse.GPSErrorCode;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.wslookup.ArrayOfPFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.CallbookerAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;

public class ValidateAddressImplement extends OSPAddrLookupImplement {

	private Logger logger = LogManager.getLogger(this.getClass());
	
    private IAddressLookup addressLookUp = null;
    protected DataSource pfDataSource;
	
	public ValidateAddressImplement(IAddressLookup addressLookUp,  DataSource pfDataSource) {
		super();
		this.addressLookUp = addressLookUp;
		this.pfDataSource = pfDataSource;
	}

	public AddressRes generateResponse(BaseReq request) {
		return generateAddressRes((AddressReq)request);
	}
	
	
	public GPSServiceableResponse getGPSServiceableResponse(BaseReq request){
		return checkGPSServiceable((GPSServiceableRequest)request);
	}
	
	public GPSServiceableResponse checkGPSServiceable(GPSServiceableRequest request){
		GPSServiceableResponse response = new GPSServiceableResponse();
		setDefaultResponse(response);
		response.setErrorCode(GPSErrorCode.ERROR_CODE_INVALID_UNKNOWN.getErrorCode());
		if(!validGPSRequest(request, response)) return response;
		
	   //validate system id
	   if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(GPSErrorCode.ERROR_CODE_NOTAUTH.getErrorCode());
			return response;
	   }
	   
	   getGPSAreaID(request.getLatitude(), request.getLongitude(), request.getTaxiCoID(), response);
	   return response;
		
		
	}
	
	private void getGPSAreaID(double latitude, double longitude, int companyID, GPSServiceableResponse response){
		addressLookUp = getAddressLookUp();
		if(addressLookUp != null){		
			if( addressLookUp.getAreaIDForGPS(latitude, longitude, companyID) > 0){
				response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
				response.setErrorCode(GPSErrorCode.ERROR_CODE_NOERROR.getErrorCode());
				response.setErrorMessage(GPSErrorCode.ERROR_CODE_NOERROR.getErrorString());
			}else{
				response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
				response.setErrorCode(GPSErrorCode.ERROR_CODE_NOTSERVICEABLE.getErrorCode());
				response.setErrorMessage(GPSErrorCode.ERROR_CODE_NOTSERVICEABLE.getErrorString());
			}
		}else{
			response.setRequestStatus(GenErrMsgRes.STATUS_CANCELLED);
			response.setErrorCode(GPSErrorCode.ERROR_CODE_ALS_FAILED.getErrorCode());
			response.setErrorMessage(GPSErrorCode.ERROR_CODE_ALS_FAILED.getErrorString());
		}
		  
	}
	
	
	private boolean validGPSRequest(GPSServiceableRequest request, GPSServiceableResponse response){
		if(request == null) return false;
		if(request.getTaxiCoID() <= 0){
			response.setErrorCode(GPSErrorCode.ERROR_CODE_INVALID_COMPANY.getErrorCode());
			response.setErrorMessage(GPSErrorCode.ERROR_CODE_INVALID_COMPANY.getErrorString());
			return false;
		}
		if(!isValidDoubleValue(request.getLatitude()) || !isValidDoubleValue(request.getLongitude()) ){
			response.setErrorCode(GPSErrorCode.ERROR_CODE_INVALID_GPS.getErrorCode());
			response.setErrorMessage(GPSErrorCode.ERROR_CODE_INVALID_GPS.getErrorString());
			return false;
		}
		return true;
	}
	
	
	
	   public AddressRes generateAddressRes(AddressReq request){
		   AddressRes response = new AddressRes();
		   setDefaultResponse(response);
		   //validate system id
		   if(!validateSystemId(pfDataSource, request)){
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
				return response;
		   }
		   if(!validAddressRequest(request, response)){
			   response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			   return response;
		   }else{
			   upcaseRequest(request);
			   response.setTaxiCoID(request.getTaxiCoID());
			   response.setNumberOfRec(0);
			   String strName = appendPercentage(request.getStreetName(), false);
			   String strNumber = appendPercentage(request.getStreetNr(), false);
			   String unit = appendPercentage(request.getUnit(), true);
			   String region = appendPercentage(request.getDistrict(),false);
			   String landMark = appendPercentage(request.getLandmark(), false);
			   
			   PFAddressResponse validAddress = getWSAddressData(strName, strNumber, unit, region, 
					   			landMark, PERCENTAGE_CHAR, PERCENTAGE_CHAR, request.getTaxiCoID(), "");
			   if(!isValidAddressResponse(validAddress) && isBothLandMarkAndAddressProvided(request)){
				   validAddress = getWSAddressData(strName, strNumber, unit, region, 
						   PERCENTAGE_CHAR, PERCENTAGE_CHAR, PERCENTAGE_CHAR, request.getTaxiCoID(), "");
			   }
			   return checkValidAddressResponse(validAddress, response);
		   }
	   }
	   
	   private boolean isBothLandMarkAndAddressProvided(AddressReq request){
		   if(request == null) return false;
		   return (request.getStreetName() != null && request.getStreetName().trim().length() > 0
				   && request.getStreetNr() != null && request.getStreetNr().trim().length() > 0
				   && request.getLandmark() != null && request.getLandmark().trim().length() >0);
	   }
	   
	   private void upcaseRequest(AddressReq request){
		   if(request.getStreetName() != null && request.getStreetName().trim().length() > 0){
			   request.setStreetName(Utilities.upcaseString(request.getStreetName()));
		   }
		   
		   if(request.getLandmark() != null && request.getLandmark().trim().length() > 0){
			   request.setLandmark(Utilities.upcaseString(request.getLandmark()));
		   }
		   
		   if(request.getDistrict() != null && request.getDistrict().trim().length() > 0){
			   request.setDistrict(Utilities.upcaseString(request.getDistrict()));
		   }
	   }
	
	   private void setDefaultResponse(BaseRes response){
		   response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
		   response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
	   }
	   
	   private boolean validAddressRequest(AddressReq request, AddressRes response){
		   if(request == null) return false;
		   //validate system id
			if(!validateSystemId(pfDataSource, request)){
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
				return false;
			}
//		   if(request.getDistrict() == null || request.getDistrict().trim().length() == 0) return false;
//		   if(request.getState() == null || request.getState().trim().length() == 0) return false;
//		   if(request.getCountry() == null || request.getCountry().trim().length() == 0) return false;
		   
		   return true;
	   }
	   
	   @Override
	   public String appendPercentage(String inputString, boolean returnEmpty){
		   if(inputString == null || inputString.trim().length() == 0) {
			   if(returnEmpty) return "";
			   else return PERCENTAGE_CHAR;
		   }else {
			   if(inputString.endsWith(PERCENTAGE_CHAR)) return inputString;
			   else{
				   return inputString+PERCENTAGE_CHAR;
			   }
		   }
	   }
	   
	   @Override
	   public PFAddressResponse getWSAddressData(String streetName, String streetNumber, String unit, 
				String region, String building, String organization, String postCode, int companyID, String orderBy){
		   addressLookUp = getAddressLookUp();
		   if(addressLookUp != null){
				return addressLookUp.getAddressData(streetName, streetNumber, unit, 
													region, building, organization, postCode, companyID, orderBy);
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
	   
		   private AddressRes checkValidAddressResponse(PFAddressResponse addResponse, AddressRes response){
			   if(!isValidAddressResponse(addResponse)) return response;
			   else{
				   Connection con = null;
				   try{
					   con = pfDataSource.getConnection();
					   ArrayOfPFAddressData arrayOfData = addResponse.getAddressData().getValue();
					   if(arrayOfData != null ){
							List<PFAddressData> listOfData = arrayOfData.getPFAddressData();
							ArrayList<AddressListItem> validAddresses = new ArrayList<AddressListItem>();
							for(PFAddressData address: listOfData){
								if(isValidAddressItem(con, address, response.getTaxiCoID()))
										validAddresses.add(createAddressListItem(address));
							}
							//AddressListItem[] addArray = new AddressListItem[addResponse.getNumberOfRecords()];
							AddressListItem[] addArray = new AddressListItem[validAddresses.size()];
							validAddresses.toArray(addArray);
							response.setNumberOfRec(validAddresses.size());
							response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
							response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
							response.setAddressList(addArray);
					   }
					   return response;
				   }catch(SQLException se){
					   logger.error("checkValidAddressResponse failed: ", se);
					   return response;
				   }finally{
					   if(con != null) try{con.close();}catch(SQLException ignore){};
				   }

			   }
		   }	
		   
		   private AddressListItem createAddressListItem(PFAddressData address){
			   if(address == null) return null;
			   AddressListItem validAddress = new AddressListItem();
			   validAddress.setStreetName(address.getStreetName().getValue());
			   validAddress.setStreetNr(address.getStreetNumber().getValue());
			   validAddress.setUnit(address.getUnitNumber().getValue());
			   validAddress.setLandmark(address.getLandmarkName().getValue());
			   validAddress.setDistrict(address.getRegionAbbreviation().getValue());
			   validAddress.setPostCode(address.getPostCode().getValue());
			   validAddress.setLatitude(address.getLatitude());
			   validAddress.setLongitude(address.getLowLongitude());
			   return validAddress;
			   
		   }
		   

}
