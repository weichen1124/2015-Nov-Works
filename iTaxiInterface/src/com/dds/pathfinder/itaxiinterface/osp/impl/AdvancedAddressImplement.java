/****************************************************************************
 *
 *                            Copyright (c), 2010
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/AdvancedAddressImplement.java $
 * 
 * PF-16333, 12/11/14, DChen, some null exceptions in OSP.
 * 
 * 14    4/14/14 3:55p Dchen
 * PF-15051, added SUCCESS PARTIAL for ALS.
 * 
 * 13    4/27/12 2:27p Dchen
 * PF-14394, replace GetLandmarkData with GetAddressData.
 * 
 * 12    2/12/11 10:00a Ezhang
 * C36130 added system id validation.
 * 
 * 11    8/19/11 3:37p Dchen
 * port C34861, 34862, 35460 here.
 * 
 * 10    4/01/11 10:05a Dchen
 * C35215, N S, N ST, N STR etc.
 * 
 * 9     3/10/11 3:50p Dchen
 * C35100 address lookup with directions.
 * 
 * 8     1/02/11 7:02p Mkan
 * - upcaseRequest(): use upcaseRequest() in Utilities. (C34954)
 * 
 * 7     7/01/11 11:05a Mkan
 * getStandardStreetType() - updated to use iTaxiInterface current
 * session's language id,
 * so that for German we will get nothing. The data in STREET_TYPES are
 * only for English addresses.
 * 
 * 6     12/07/10 5:15p Mkan
 * createAddressListItem(), added organization for C34593
 * 
 * 5     11/26/10 12:29p Ezhang
 * C34558 add first letter of the direction to the search if it appears as the first token
 * 
 * 4     11/10/10 10:20a Ezhang
 * remove the % in front of street name before sending to address lookup.
 * 
 * 3     9/27/10 3:44p Ezhang
 * code clean up added search result limit of 100
 * 
 * 2     9/24/10 1:30p Ezhang
 * added business search
 * 
 * 1     9/22/10 3:47p Ezhang
 * OSP 2.0 new method
 */
package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.itaxiinterface.common.impl.AddressLookupImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.CarValidationImplement.CarValidationErrorCode;
import com.dds.pathfinder.itaxiinterface.util.Utilities;
import com.dds.pathfinder.itaxiinterface.webservice.AddressListItem;
import com.dds.pathfinder.itaxiinterface.webservice.AdvancedAddressReq;
import com.dds.pathfinder.itaxiinterface.webservice.AdvancedAddressRes;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.wslookup.ArrayOfPFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.CallbookerAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressData;
import com.dds.pathfinder.itaxiinterface.wslookup.PFAddressResponse;

/**
 * @author ezhang 
 * 		AdvanceAddressImplement validate user enter address info and
 *         return full address found in Pathfinder database It has more search
 *         conditions compared to ValidateAddressImplement. It will return
 *         partial search based on address name and synonyms search based on
 *         direction( NE vs NorthEast) and Type(RD vs Road).
 */
public class AdvancedAddressImplement extends OSPAddrLookupImplement {

	private Logger logger = LogManager.getLogger(this.getClass());

	private DataSource pfDataSource;
	private IAddressLookup addressLookUp = null;
	// private String direction = null;						
	private ArrayList<String> directionList = null;				//C35100, multiple directions
	private String type = null;
	private String name = null;
	private boolean isBusiness = true; // is Business address
	private ArrayList<String> businessTokens = new ArrayList<String> ();
	
	public final static String SEPARATOR_CHAR = " ";
	public final static int MIN_CHARACTER_CHECK = 3;

	public final static String DIRECTION_PATTERN = 
		"N|E|W|S|NE|NW|SE|SW|NORTH|SOUTH|EAST|WEST|NORTHEAST|NORTHWEST|SOUTHEAST|SOUTHWEST|NORTH EAST|NORTH WEST|SOUTH EAST|SOUTH WEST";


	public enum AdvancedAddressErrorCode {
		NO_ERROR(0), 
		NOT_AUTHENTICATED(1), 
		INVALID_SESSIONID(2), 
		ADDRESS_NOT_FOUND(3), 
		INVALID_COMPID(4), 
		INVALID_HOUSE_NUM(5), 
		INVALID_STREET_NAME(6), 
		INVALID_UNIT(7), 
		INVALID_BUSINESS(8), 
		DEFAULT_ERROR(99);

		private int code;

		private AdvancedAddressErrorCode(int c) {
			code = c;
		}

		public int getCode() {
			return code;
		}
	};

	public enum DirectionPattern {
		NORTH("N|NORTH"), 
		SOUTH("S|SOUTH"), 
		EAST("E|EAST"), 
		WEST("W|WEST"), 
		NORTHEAST("NE|NORTHEAST|NORTH EAST"), 
		NORTHWEST("NW|NORTHWEST|NORTH WEST"), 
		SOUTHEAST("SE|SOUTHEAST|SOUTH EAST"), 
		SOUTHWEST("SW|SOUTHWEST|SOUTH WEST");

		private String Pattern;

		private DirectionPattern(String s) {

			this.Pattern = s;
		}

		public String getDirectionPattern() {
			return Pattern;
		}

	};


	public AdvancedAddressImplement(DataSource pfDataSource,
			IAddressLookup addressLookUp) {
		super();
		this.pfDataSource = pfDataSource;
		this.addressLookUp = addressLookUp;

	}

	public AdvancedAddressRes generateResponse(BaseReq request) {
		return generateAdvancedAddressRes((AdvancedAddressReq) request);
	}

	public AdvancedAddressRes generateAdvancedAddressRes(
			AdvancedAddressReq request) {

		AdvancedAddressRes response = getDefaultResponse(request);
		if (!validAddressRequest(request, response)) {
			return response;
		}

		upcaseRequest(request);
		response.setTaxiCoID(request.getTaxiCoID());
		response.setNumberOfRec(0);
		if(isBusiness){
			parseBusiness(request);
			if(request.getStreetName() != null) parseAddress(request);			//PF-16333 in some cases, street name is sent null
			generateBusinessAddressRes(request, response);
		}
		else{
			parseAddress(request);
			generateAddressRes(request, response);
		}
		
		return response;

	}

	/**
	 * @param request
	 * @param response
	 * call address lookup in the following order
	 * parse business to tokens 
	 * use token1%token2%...%tokenx as organization(table buildings.orgnization) if not found then
	 *  use token1%token2%...%tokenx as address (table addresses.name) if not found then
	 *  use partial token like token1% as organization if not found then
	 *  use partial token like token1% as address if not found then
	 *  Till all tokens are tried.
	 *  If address found then stop and return results.
	 */
	private AdvancedAddressRes generateBusinessAddressRes(AdvancedAddressReq request,
			AdvancedAddressRes response) {
		String temp = null;
		for(String b : businessTokens){
			temp = (temp == null)? b : temp + PERCENTAGE_CHAR + b;
		}
		String business = appendPercentage(temp, false);
		String strName = appendPercentage(name, false);
		String strNumber = appendPercentage(request.getStreetNr(), false);
		String unit = appendPercentage(request.getUnit(), true);	
		String landMark = PERCENTAGE_CHAR + business;
		String organization = landMark;
		String regionName = request.getDistrict();
		String regionAbbrev = null;
		// User pass in region full name and address look up requires region
		// abbreviation
		if (regionName != null && regionName.trim().length() != 0) {
			regionAbbrev = getRegionAbbreviation(regionName);
		}
		String region = appendPercentage(regionAbbrev, false);

		PFAddressResponse validAddress = null;
		
		//search organization first for full business name
		validAddress = getWSAddressData(strName, strNumber, unit, region,		//replace getWSLandmarkData with getWSAddressData
				PERCENTAGE_CHAR, organization, PERCENTAGE_CHAR,  request.getTaxiCoID(), "");
		//search landmark second for full business name
		if(!isValidAddressResponse(validAddress)){
				validAddress = getWSAddressData(strName, strNumber,
					unit, region, landMark, PERCENTAGE_CHAR, PERCENTAGE_CHAR,
					request.getTaxiCoID(), "");
		}
		// search landmark for partial business name if no result found
		if(!isValidAddressResponse(validAddress) && businessTokens.size()> 1){
			for(int i=0; i< businessTokens.size()&& !isValidAddressResponse(validAddress); i++){
				organization = PERCENTAGE_CHAR + businessTokens.get(i) + PERCENTAGE_CHAR;
				validAddress = getWSAddressData(strName, strNumber, unit, region,	//replace getWSLandmarkData with getWSAddressData
						PERCENTAGE_CHAR, organization, PERCENTAGE_CHAR, request.getTaxiCoID(), "");
				
				if(!isValidAddressResponse(validAddress)){
					landMark = organization;
					validAddress = getWSAddressData(strName, strNumber, unit, region,    //replace getWSLandmarkData with getWSAddressData
							PERCENTAGE_CHAR, landMark, PERCENTAGE_CHAR, request.getTaxiCoID(), "");
				}
				
			}
		}
		
		return checkValidAddressResponse(validAddress, response, null);
		
	}

	private AdvancedAddressRes getDefaultResponse(AdvancedAddressReq request) {

		AdvancedAddressRes response = new AdvancedAddressRes();

		response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
		response.setErrorCode(CarValidationErrorCode.DEFAULT_ERROR.getCode());

		return response;
	}

	private boolean validAddressRequest(AdvancedAddressReq request,
			AdvancedAddressRes response) {
		if (request == null || response == null)
			return false;

		//validate system id
		if(validateSystemId(pfDataSource, request)== false){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			response.setErrorCode(AdvancedAddressErrorCode.NOT_AUTHENTICATED.getCode());
			return false;
		}
		// validate taxi company id
		if (request.getTaxiCoID() == null || request.getTaxiCoID() <= 0
				|| request.getTaxiCoID() > 999999999) {
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
			response.setErrorCode(AdvancedAddressErrorCode.INVALID_COMPID.getCode());
			return false;
		}
		
		if (request.getBusiness() == null || request.getBusiness().trim().length() == 0) {
			isBusiness = false;
		}
		// address number and street name are required for nonBusiness address
		if (!isBusiness) {
			if (request.getStreetNr() == null
					|| request.getStreetNr().trim().length() == 0) {
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
				response.setErrorCode(AdvancedAddressErrorCode.INVALID_HOUSE_NUM.getCode());
				return false;
			}

			if (request.getStreetName() == null || request.getStreetName().trim().length() == 0) {
				response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
				response.setErrorCode(AdvancedAddressErrorCode.INVALID_STREET_NAME.getCode());
				return false;
			}
		}

		return true;
	}

	private void upcaseRequest(AdvancedAddressReq request) {
		if (request.getStreetName() != null
				&& request.getStreetName().trim().length() > 0) {
			request.setStreetName(Utilities.upcaseString(request.getStreetName()));
		}

		if (request.getBusiness() != null
				&& request.getBusiness().trim().length() > 0) {
			request.setBusiness(Utilities.upcaseString(request.getBusiness()));
		}
		
		if (request.getDistrict() != null
				&& request.getDistrict().trim().length() > 0) {
			request.setDistrict(Utilities.upcaseString(request.getDistrict()));
		}
	}

	/**
	 * @param request
	 * parse the Street name into direction(such as East West), type(eg. PL, RD)
	 * and name, use name to do the search through PF addresslookup service.
	 * rule 0: one word or input character less than minimum (4 char) whole input considered as name;
	 * rule 1: street type should not be 1st token, otherwise will be considered as name;
	 * rule 2: if more than one street type, consider the last one as type move the previous input to name;
	 * rule 3: if 1st token is direction, take the 1st char as name, but not consider it as a meaningful name;
	 * rule 4: if finally no meaningful name found, take the last token as name, and remove it from either type or direct; 
	 */
	private void parseAddress(AdvancedAddressReq request) {
		directionList = new ArrayList<String>();
		String streetName = request.getStreetName();
		boolean isNameField = false;			//meaningful name
		boolean isLastFieldDirection = false;
		boolean isLastFieldStreetType = false;

		String[] result = streetName.split("\\s");
		if(result == null || result.length == 0 || streetName.length() <= MIN_CHARACTER_CHECK) return;  //one word is name
		String lastToken = (result.length > 0)? result[result.length -1] : "";
		String streetType = "";
		for (int i = 0; i < result.length; i++) {
			// by convention the type such as XXX PL or XXX ST should not be the first token
			// If there are more than token matches type then take the last one
			// like "Bridge Road" take "Road" as type instead of "bridge".
			// move the previous input to name.
			if(result[i].trim().length() != 0){
				String standardType = getStandardStreetType(result[i]);
				if (standardType != null && i != 0) {
					//if (type != null && name == null) {
					if (type != null && streetType.length() > 0) {    
						name = (name == null) ? streetType : name + PERCENTAGE_CHAR + SEPARATOR_CHAR + streetType;
						isNameField = true;
					}							
					type = standardType;
					streetType = result[i];    //original input
					if(i== (result.length -1)) isLastFieldStreetType = true;
				} else if (isDirection(result[i])) {
					// for case like NORTH WEST
					String directToken = result[i];
					boolean isDoubleWords = false;
					if(i < (result.length -1) && isDirection(result[i]+ SEPARATOR_CHAR + result[i+1])){
						directToken = result[i]+ SEPARATOR_CHAR + result[i+1];
						isDoubleWords = true;
					}
					directionList.add(directToken);
					
					// C34558 keep direction in the name search if it's the first token
					if(i == 0){       //still not meaningful name, just because we can not start searching with wild card
						name = directToken.charAt(0) + "";
					}
					
					if(isDoubleWords) i++;      					//two words direction
					if(i == (result.length -1)) isLastFieldDirection = true;
				} else {
					isNameField = true;
					name = (name == null) ? result[i] : name + PERCENTAGE_CHAR + SEPARATOR_CHAR + result[i];
				}
			}	
		}

		if(!isNameField){  //No meaningful name, ex. N ST move the last token to name field
			moveLastTokenToName(lastToken, isLastFieldStreetType, isLastFieldDirection, streetName);
		}
	}
	
	private void moveLastTokenToName(String lastToken, boolean isLastFieldStreetType, boolean isLastFieldDirection, String streetName){
		if(lastToken == null || lastToken.length() == 0) return;
		if(isLastFieldStreetType){
			type = null;
			name = (name == null) ? lastToken : name + PERCENTAGE_CHAR + SEPARATOR_CHAR + lastToken;
		}else if(isLastFieldDirection){
			if(directionList.size() >= 1){
				String directToken = directionList.remove(directionList.size() -1 );
				if(directToken != null && directToken.contains(SEPARATOR_CHAR)){  //double words
					String[] directs = directToken.split("\\s");
					if(directs != null && directs.length > 1) directionList.add(directs[0]);  //put back the 1st one in double words direct
				}
				name = (name == null) ? lastToken : name + PERCENTAGE_CHAR + SEPARATOR_CHAR + lastToken;
			}
		}else name = streetName;
	}
	
	private void parseBusiness(AdvancedAddressReq request) {
		String business = request.getBusiness();

		String[] result = business.split("\\s");
		for (int i = 0; i < result.length; i++) {
			if(result[i] != null && result[i].trim().length()>0){
				businessTokens.add(result[i]);
			}
			
		}
		
	}

	private AdvancedAddressRes generateAddressRes(AdvancedAddressReq request,
			AdvancedAddressRes response) {
		String strName = null;
		// if street name after stripped direction and type is null then use the
		// whole name to search
		if (name == null || name.trim().length() == 0) {

			strName = request.getStreetName()
					+ PERCENTAGE_CHAR;

		} else {
			strName = name + PERCENTAGE_CHAR;
		}
		strName = appendPercentage(strName, false);
		String strNumber = appendPercentage(request.getStreetNr(), false);
		String unit = appendPercentage(request.getUnit(), true);
		String regionName = request.getDistrict();
		String regionAbbrev = null;
		// User pass in region full name and address look up requires region
		// abbreviation
		if (regionName != null && regionName.trim().length() != 0) {
			regionAbbrev = getRegionAbbreviation(regionName);
		}
		String region = appendPercentage(regionAbbrev, false);
		/*
		logger.info("strName " + strName + " strNumber " + strNumber
				 + " region " + region);
		*/
		PFAddressResponse validAddress = null;
		
		validAddress = getWSAddressData(strName, strNumber,
				unit, region, PERCENTAGE_CHAR, PERCENTAGE_CHAR, PERCENTAGE_CHAR,
				request.getTaxiCoID(), "");
		
		return checkValidAddressResponse(validAddress, response, request.getStreetName());
	}

	private boolean isDirection(String token) {

		return token.matches(DIRECTION_PATTERN);
	}

	private String getStandardStreetType(String token) {
		String typeName = null;
		String query = "select standard_street_type from street_types, users u"
				+ " where street_type = '" + token 
				+ "' and street_types.language_id = nvl(u.language_id, 0) " 
				+ " and u.logon_code = upper(USER)";
		
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;
		try {
			con = pfDataSource.getConnection();
			stmt = con.createStatement();
			result = stmt.executeQuery(query);
			if (result.next()) {
				typeName = result.getString("STANDARD_STREET_TYPE");
			}

		} catch (SQLException se) {
			logger.error("getStreetType failed....", se);
		} finally {
			if (result != null)try {result.close();} catch (SQLException ignore) {};
			if (stmt != null)try {stmt.close();} catch (SQLException ignore) {};
			if (con != null)try {con.close();} catch (SQLException ignore) {};
		}
		return typeName;
		
	}

	private String getMatchingType(String standardType) {

		String typePattern = null;
		if (standardType == null)
			return null;

		String query = "select street_type from street_types"
				+ " where standard_street_type = '" + standardType + "'";
		
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;
		try {
			con = pfDataSource.getConnection();
			stmt = con.createStatement();
			result = stmt.executeQuery(query);
			while (result.next()) {
				String type = result.getString("STREET_TYPE");
				typePattern = (typePattern == null) ? type : typePattern + "|" + type;
			}

		} catch (SQLException se) {
			logger.error("getMatchingType failed....", se);
		} finally {
			if (result != null)try {result.close();} catch (SQLException ignore) {};
			if (stmt != null)try {stmt.close();} catch (SQLException ignore) {};
			if (con != null)try {con.close();} catch (SQLException ignore) {};
		}
		//logger.info("typePattern " + typePattern);
		return typePattern;

	}

	// helper class can later move to utility class
	@Override
	public String appendPercentage(String inputString, boolean returnEmpty) {
		if (inputString == null || inputString.trim().length() == 0) {
			if (returnEmpty)
				return "";
			else
				return PERCENTAGE_CHAR;
		} else {
			if (inputString.endsWith(PERCENTAGE_CHAR))
				return inputString;
			else {
				return inputString + PERCENTAGE_CHAR;
			}
		}
	}

	@Override
	public PFAddressResponse getWSAddressData(String streetName,
			String streetNumber, String unit, String region, String building,
			String organization, String postCode, int companyID, String orderBy) {
		addressLookUp = getAddressLookUp();
		if (addressLookUp != null) {
			return addressLookUp.getAddressData(streetName, streetNumber, unit,
					region, building, organization, postCode, companyID,
					orderBy);
		} else {
			return null;
		}

	}
	
	
	//As ALS is not supporting getLandmarkData any longer, we should use getAddressData instead
//	private PFAddressResponse getWSLandmarkData(String streetName,
//			String streetNumber, String unit, String region, String building,
//			String organization, String postCode, String orderBy) {
//		addressLookUp = getAddressLookUp();
//		if (addressLookUp != null) {
//			return addressLookUp.getLandmarkData(streetName, streetNumber, unit, region, building, organization, postCode, orderBy);
//		} else {
//			return null;
//		}
//
//	}

	private IAddressLookup getAddressLookUp() {
		if (addressLookUp == null) {
			long time = System.currentTimeMillis();
			CallbookerAddressLookup cbAddressLookup = new CallbookerAddressLookup();
			addressLookUp = cbAddressLookup.getBasicHttpBindingIAddressLookup();
			if (addressLookUp != null) {
				logger.info("get address look up interface ....."
						+ (System.currentTimeMillis() - time) / 1000.00 + "s");
			} else {
				logger.error("get address look up interface failed .....");
			}
		}
		return addressLookUp;
	}

 
	private AddressListItem createAddressListItem(PFAddressData address) {
		if (address == null)
			return null;
		AddressListItem validAddress = new AddressListItem();
		validAddress.setStreetName(address.getStreetName().getValue());
		validAddress.setStreetNr(address.getStreetNumber().getValue());
		validAddress.setUnit(address.getUnitNumber().getValue());
		validAddress.setLandmark(address.getLandmarkName().getValue());
		validAddress.setOrganization(address.getOrganizationName().getValue()); //C34593 - "organization" name not being displayed/used when using the "business" choice
		
		int regionId = address.getRegionID().intValue();
		// Advanced Address requires full region name
		validAddress.setDistrict(getRegionFullName(regionId)); 															
		validAddress.setPostCode(address.getPostCode().getValue());
		validAddress.setLatitude(address.getLatitude());
		validAddress.setLongitude(address.getLowLongitude());
		return validAddress;

	}

	/**
	 * 
	 * @param addressResponse, response from PF addresslookup webservice
	 * @param response	advanced address match response
	 * @return the advanced address to ws client.
	 * this function filter the search results with direction and type part of the street name
	 * and return the matches.
	 */
	private AdvancedAddressRes checkValidAddressResponse(PFAddressResponse addressResponse, AdvancedAddressRes response, String inputStreetName) {
		if (addressResponse == null ){
			//dispatch system error
			return response;
		}
		else {
			//address not found error
			String status = addressResponse.getStatus().getValue();
			int nbRecord = addressResponse.getNumberOfRecords();
			if((RESPONSE_SUCCESS.equalsIgnoreCase(status) || RESPONSE_SUCCESS_PARTIAL.equalsIgnoreCase(status)) && nbRecord == 0){
				response.setErrorCode(AdvancedAddressErrorCode.ADDRESS_NOT_FOUND.getCode());
				return response;
			}
		} 
		ArrayOfPFAddressData arrayOfData = addressResponse.getAddressData().getValue();
		
		if (arrayOfData == null) {	return response; }

		List<PFAddressData> listOfData = arrayOfData.getPFAddressData();
		ArrayList<AddressListItem> validAddresses = new ArrayList<AddressListItem>();
		ArrayList<AddressListItem> mostMatchAddresses = new ArrayList<AddressListItem>();
		
		ArrayList<String> matchDirection = getMatchingPatterns(directionList); 
		String matchType = getMatchingType(type);
		
		Connection con = null;
		try{
			con = pfDataSource.getConnection();
			for (PFAddressData address : listOfData) {
				//set up the upper limit of matching address to 100 for now
				if(validAddresses.size() < MAX_ADDRESS_MATCH){
					// return all search results if user entered street don't have
					// direction and type
					if(!isValidAddressItem(con, address, response.getTaxiCoID())){
						continue;
					}
					boolean hasTypeMatch = false;
					if (matchDirection == null && matchType == null) {
						validAddresses.add(createAddressListItem(address));
						// count++;
					} else {
						// filter search result
						String streetName = address.getStreetName().getValue();
	
						String[] result = streetName.split("\\s");
						// use entered street has type but no direction
						if(inputStreetName != null  && inputStreetName.length() > 0 && isMatchInputStrName(streetName, inputStreetName)){
							mostMatchAddresses.add(createAddressListItem(address));
						}else if (matchDirection == null && matchType != null) {
							// filter results to match street type
							for (int i = 0; i < result.length; i++) {
								// found the match to the user request street type,
								// street type should not be the first token of the
								// address.
								if (result[i].matches(matchType) && i != 0) {
									hasTypeMatch = true;
								}
							}
							if (hasTypeMatch) {
								validAddresses.add(createAddressListItem(address));
							}
	
						} else if (matchDirection != null && matchType == null) {
							// filter results to match street Direction
	//						String returnedDirection = null;
	//						for (String s : result) {
	//							if (isDirection(s)) {
	//								returnedDirection = (returnedDirection == null) ? s
	//										: returnedDirection + " " + s;
	//							}
	//						}
							ArrayList<String> returnedDirectList = getDirectionList(result);
							if(returnedDirectList != null && returnedDirectList.size() > 0 
									&& containMatchPatterns(matchDirection, returnedDirectList)){
								validAddresses.add(createAddressListItem(address));
							}
	//						if (returnedDirection != null && returnedDirection.matches(matchDirection)) {
	//							validAddresses.add(createAddressListItem(address));
	//						}
						} else {
							// filter results to match street direction and street
							// type
							// String returnedDirection = null;
							// ArrayList<String> returnedDirectList = getDirectionList(result);
							for (int i = 0; i < result.length; i++) {
	//							if (isDirection(result[i])) {
	//								returnedDirection = (returnedDirection == null) ? result[i]
	//										: returnedDirection + " " + result[i];
	//							}
								if (result[i].matches(matchType) && i != 0) {
									hasTypeMatch = true;
									break;
								}
	
							}
							if (hasTypeMatch 
								&& containMatchPatterns(matchDirection, getDirectionList(result))) {
								validAddresses.add(createAddressListItem(address));
							}
						}
					}
				}
			}
		}catch(SQLException se){
			logger.error("checkValidAddressResponse failed", se);
		}finally{
			if(con != null) try{con.close();}catch(SQLException ignore){}
		}
		
		if(mostMatchAddresses.size() > 0) validAddresses.addAll(0, mostMatchAddresses);		//list most match addresses at beginning
		
		if(validAddresses.size() != 0){
			AddressListItem[] addArray = new AddressListItem[validAddresses.size()];
			validAddresses.toArray(addArray);
			//logger.info("actual address result " + count);
			response.setNumberOfRec(validAddresses.size());
			response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
			response.setErrorCode(AdvancedAddressErrorCode.NO_ERROR.getCode());
			response.setAddressList(addArray);
		}
		else{
			//address not found after filtering
			response.setErrorCode(AdvancedAddressErrorCode.ADDRESS_NOT_FOUND.getCode());
		}
		return response;

	}
	
	private boolean isMatchInputStrName(String streetName, String inputName){
		if(streetName == null || streetName.length() == 0 || inputName == null || inputName.length() == 0) return false;
		return streetName.startsWith(inputName);
	}

	private ArrayList<String> getMatchingPatterns(ArrayList<String> directList){
		if(directList == null || directList.size() == 0) return null;
		ArrayList<String> matchPatterns = new ArrayList<String>();
		for(String direct: directList){
			String pattern = getMatchingDirection(direct);
			if(pattern != null) matchPatterns.add(pattern);
		}
		if(matchPatterns.size() == 0) return null;
		else return matchPatterns;
	}
	
	/**
	 * @return Matching Direction Pattern for the user entered direction
	 */
	private String getMatchingDirection(String direction) {
		// 
		if (direction == null) {
			return null;
		}
		if (direction.matches(DirectionPattern.EAST.getDirectionPattern())) {
			return DirectionPattern.EAST.getDirectionPattern();
		}
		if (direction.matches(DirectionPattern.WEST.getDirectionPattern())) {
			return DirectionPattern.WEST.getDirectionPattern();
		}
		if (direction.matches(DirectionPattern.NORTH.getDirectionPattern())) {
			return DirectionPattern.NORTH.getDirectionPattern();
		}
		if (direction.matches(DirectionPattern.SOUTH.getDirectionPattern())) {
			return DirectionPattern.SOUTH.getDirectionPattern();
		}
		if (direction.matches(DirectionPattern.SOUTHEAST.getDirectionPattern())) {
			return DirectionPattern.SOUTHEAST.getDirectionPattern();
		}
		if (direction.matches(DirectionPattern.SOUTHWEST.getDirectionPattern())) {
			return DirectionPattern.SOUTHWEST.getDirectionPattern();
		}
		if (direction.matches(DirectionPattern.NORTHEAST.getDirectionPattern())) {
			return DirectionPattern.NORTHEAST.getDirectionPattern();
		}
		if (direction.matches(DirectionPattern.NORTHWEST.getDirectionPattern())) {
			return DirectionPattern.NORTHWEST.getDirectionPattern();
		}

		return null;
	}

	private String getRegionFullName(int regionId) {
		String regionName = null;
		String query = "select region_name from street_lookup_regions"
				+ " where region_id = " + regionId;

		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;
		try {
			con = pfDataSource.getConnection();
			stmt = con.createStatement();
			result = stmt.executeQuery(query);
			if (result.next()) {
				regionName = result.getString("REGION_NAME");

			}

		} catch (SQLException se) {
			logger.error("getRegionName failed....", se);
		} finally {
			if (result != null)try {result.close();} catch (SQLException ignore) {};
			if (stmt != null)try {stmt.close();} catch (SQLException ignore) {};
			if (con != null)try {con.close();} catch (SQLException ignore) {};
		}
		return regionName;
	}

	private boolean containsMatchPattern(String checkPattern, ArrayList<String> checkList){
//		if(checkPattern == null || checkPattern.length() == 0 
//				|| checkList == null || checkList.size() == 0) return false;
		for(String checkString: checkList){
			if(checkString.matches(checkPattern)) return true;
		}
		return false;
	}
	
	private boolean containMatchPatterns(ArrayList<String> checkPatterns, ArrayList<String> checkList){
		if(checkPatterns == null || checkPatterns.size() == 0) return true;    //no direct check needed
		if( checkList == null || checkList.size() == 0) return false;          //no direct candidates
		
		for(String checkPattern : checkPatterns){
			if(!containsMatchPattern(checkPattern, checkList)) return false;
		}
		return true;
	}
	
	private String getRegionAbbreviation(String regionName) {
		String regionAbbreviation = null;
		String query = "select region_abbreviation from street_lookup_regions"
				+ " where region_name = '" + regionName + "'";
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;
		try {
			con = pfDataSource.getConnection();
			stmt = con.createStatement();
			result = stmt.executeQuery(query);
			if (result.next()) {
				regionAbbreviation = result.getString("REGION_ABBREVIATION");
			}

		} catch (SQLException se) {
			logger.error("getRegionAbbreviation failed....", se);
		} finally {
			if (result != null)try {result.close();} catch (SQLException ignore) {};
			if (stmt != null)try {stmt.close();} catch (SQLException ignore) {};
			if (con != null)try {con.close();} catch (SQLException ignore) {};
		}
		
		return regionAbbreviation;
	}
	
	private ArrayList<String> getDirectionList(String[] seqWords){
		ArrayList<String> directList = new ArrayList<String>();
		if(seqWords == null || seqWords.length == 0) return directList;
		
		for(int i=0; i<seqWords.length; i++){
			 if(i < (seqWords.length -1)){
				 String doubleWords = seqWords[i] + SEPARATOR_CHAR + seqWords[i+1];
				 if(isDirection(doubleWords)) {
					 directList.add(doubleWords);
					 i++;
					 continue;
				 }
			 }
			 if(isDirection(seqWords[i])) directList.add(seqWords[i]);
			 
		}
		return directList;
	}

}
