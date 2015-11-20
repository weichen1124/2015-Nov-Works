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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/common/impl/CommonImplement.java $
 * 
 * PF-16515, 04/29/15, DChen, some pfrest bug fixes.
 * 
 * PF-16385, 03/03/15, DChen, share with pfrest.
 * 
 * 
 * ******/
package com.dds.pathfinder.itaxiinterface.common.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface CommonImplement {
	public static String PERCENTAGE_CHAR="%";
    public static final String RESPONSE_SUCCESS = "SUCCESS";
    public static final String RESPONSE_SUCCESS_PARTIAL = "SUCCESS PARTIAL";
    public static final int MAX_FLOAT_VALUE = 181;
    public static final int MAX_ADDRESS_MATCH = 100;
    
    public static final int REQUEST_TYPE_BOOK_JOB = 1; 
    public static final int REQUEST_TYPE_CANCEL_JOB = 2;
	public static final int REQUEST_TYPE_UPDATE_JOB = 3;
    //please refer to PF DB package RTL_TYP for the meaning of the rest of the status
    public static final String VEHICLES_STATUS_TAXI_POB = "P";
    public static final String VEHICLES_STATUS_TAXI_ARRIVED = "A";
    public static final String VEHICLES_STATUS_TAXI_ACCEPTED = "Y";
    
    public static final String JOB_STATUS_VEHICLE = "V";
    public static final String JOB_STATUS_HOLD = "H";
    public static final String JOB_STATUS_COMPLETE = "C";
    public static final String JOB_STATUS_PENDING = "P";
    public static final String JOB_STATUS_DESPATCHING = "W";
    public static final String JOB_STATUS_BOOKED = "B";  
    
    public final static String JOB_TYPE_ASAP = "ASAP";
    public final static String JOB_TYPE_PBOK = "PBOK";
    public final static String JOB_TYPE_RTC  = "RTC";
    public final static String JOB_TYPE_HELDJOB = "HELD JOB";
    public final static String JOB_TYPE_STREETHIRE = "STREET HIRE";
    public final static String JOB_TYPE_HELDTEMPLATE = "HELD TEMPLATE";
    public final static String JOB_TYPE_COMPLETED = "COMPLETED";
    public final static String JOB_TYPE_TBA = "TBA";
    public final static String JOB_TYPE_DISPATCH = "DISPATCH";
    public final static String JOB_TYPE_QUICKBOOKER = "QUICKBOOKER";
    public final static String JOB_TYPE_AUTOBOOKER = "AUTOBOOKER";
    
	public final static String JOB_TYPE_ASAP_RAW = "I";
	public final static String JOB_TYPE_PBOK_RAW= "P";
	public final static String JOB_TYPE_TBA_RAW = "T"; //TODO: does T mean TBA? if not, please rename
	
	
	public static final int ACTION_TYPE_MODIFYBOOKING = 88;
	public static final int ACTION_TYPE_MODIFYJOB = 11;
	public static final int ACTION_TYPE_BOOK_MULTIPLEJOBS = 99;
	public static final int PF_REFERENCE_NUMBER_MAX_LENGTH = 40;
    
    public enum ExternalSystemId {
		SYSTEM_ID_DEFAULT("", 0,"SYSADMIN"),		//default
		SYSTEM_ID_GOFASTCAB("GF", 1, "GFBOOKER"),
		SYSTEM_ID_MB("MB", 2, "MBBOOKER"),  //PF-14809 for DDS mobile booking solution
		SYSTEM_ID_MOB_PDA("MOB", 501, "IUSER"), //hard code until C35317 is solved
		SYSTEM_ID_UDI_USER("UD", 834, "UDIBOOKER"),
		SYSTEM_ID_TSS_RIDER("SR", 6, "TSSRIDER"),
		SYSTEM_ID_TRANSIT_GW("TG", 7, "TRANSITGW"),	//PF-16385, pfrest for transit gateway.
		SYSTEM_ID_WEB_BOOKER("WB", 999, "WEBBOOKER");
		
		private int systemId;
		private String reference;
		private String logonCode;
		
		private ExternalSystemId(String ref, int s, String logon) {
			   this.reference = ref;
			   this.systemId = s;
			   this.logonCode = logon;
		}

		public final int getSystemId() {
			   return systemId;
		}
		
		public String getReference(){
			return reference;
		}
		
		public String getLogonCode(){
			return logonCode;
		}
	};
	
	public static final Map<String, String[]> databaseErrorMap = 
			Collections.unmodifiableMap(new HashMap<String, String[] >(){
				private static final long serialVersionUID = 1L;

			{
				//These are copied from Callbooker resource string file, for OSP code that is not defined we use the default error
				//
				// Format: 
				//	put ("database error code", new String[]{String.valueOf("OSP RESPONSE CODE"), "Error message to return back in the response"});
				//
			        put("2", new String[]{String.valueOf(BookJobErrorCode.INVALID_ACCOUNT_CODE_OR_NAME.code), "No such account"});//"No Account Card or Account present"
			        put("3", new String[]{String.valueOf(BookJobErrorCode.INVALID_ACCOUNT_CODE_OR_NAME.code), "Account id /Account card id is not related with this Company"});
			        put("10",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code), "User is Suspended "}); //we get this when the account is suspended
			        put("13",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code), "Account/Group is Suspended"});
			        put("14",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"Account/Group is Closed "});
			        put("15",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"No trips left for Account/Card"});
			        put("16",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"Account is currently disabled"});
			        put("17",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"Group is currently disabled "});
			        put("23",new String[]{String.valueOf(BookJobErrorCode.DEFAULT_ACCT_ERROR.code),"Limit of Account is less than the Balance"});
			        put("24",new String[]{String.valueOf(BookJobErrorCode.DEFAULT_ACCT_ERROR.code),"card is Expired"});
			        put("26",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"Bookings are not allowed to this account"});
			        put("28",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"Account is Inactive"});
			        put("29",new String[]{String.valueOf(BookJobErrorCode.INACTIVE_ACCOUNT.code),"Booking is not Allowed on this Account"});
			        put("30",new String[]{String.valueOf(BookJobErrorCode.DEFAULT_ACCT_ERROR.code),"Account is Pass the trip threshold limit"});
			        put("32",new String[]{String.valueOf(BookJobErrorCode.DEFAULT_ACCT_ERROR.code),"Limit of Account User is less than the Balance"});
			        put("34",new String[]{String.valueOf(BookJobErrorCode.DEFAULT_ERROR.code),"PO# is required"});
		     
	        }
	    });

    public enum BookJobErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_SESSIONID(2),
		SYS_CAPACITYLIMIT(3),
		INVALID_WEBUSERID(4),
		TRIP_NO_FOUND(5),
		INVALID_REQTYP(6),
		INVALID_RIDEID(7),
		INVALID_TAXI_RIDEID(8),
		INVALID_TAXI_COMPID(9),
		INVALID_PHONENUM(10),
		INVALID_PHONEEXT(11),
		INVALID_PICKUP_HOUSE_NUM(12),
		INVALID_PICKUP_STREET_NUM(13),
		INVALID_PICKUP_DISTRICT(14),
		INVALID_PICKUP_LANDMARK(15),
		INVALID_PICKUP_GPS(16),
		INVALID_DROPOFF_GPS(17),
		NO_ASAP_PREFEERRED_TRIP(18),
		TC_PICKUP_DATETIME_TOOSOON(19),
		TC_PICKUP_DATETIME_TOOFAR(20),
		CAB_NOT_MATCH_TRIP_COMP(21),
		INVALID_PASSENGER_NAME(22),
		INVALID_ACCOUNT_CODE_OR_NAME(23),
		INVALID_FLAT_RATE(24),
		INVALID_AUTH_NUM(25),
		INVALID_PRIORITY_REASON(26),
		INVALID_PRIORITY(27),
		INVALID_NUMTAXIS(28),
		INVALID_NUM_OF_PASSENGER(29),
		INVALID_ADVISE_ARRIVAL(30),
		INVALID_TRIP_ATTRIBUTE(31),
		INVALID_CALLBACK_TIME(32),
		INVALID_NUM_OF_CALLBACKS(33),
		INVALID_FORCED_ADDRESS_FLAG(34),
		INVALID_REMARK(35),
		INACTIVE_ACCOUNT(36),
		INCORRECT_ACCT_PASSWORD(37),
		PICKUP_ADDR_NOT_FOUND(38),
		INVALID_ADDRESS(39),
		DEFAULT_ACCT_ERROR(40),
		INVALID_INPUT_TARIFF_NB(41),
		INVALID_INPUT_JOB_ATTR(42),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private BookJobErrorCode(int c) {
			   this.code = c;
		}

		public int getCode() {
			   return code;
		}
	};
	
	
	public static enum AdviseArrivalType  {
		/** no advise */
		NoAdvise("1", "Denied", "N"),
		
		/** voice advise */
		VoiceAdvise("2", "Required", "R"),
		
		/** email advise, not supported by PF */
		EmailAdvise("3", null, null),  //set to null here so that it'll not be returned to OSP system
		
		/** SMS via Email, not supported by PF*/
		SMSAdvise("4", "Required SMS", "C");	//PF added SMS with C36064, update OSP as well. PF has other SMS optional as well, just use required one here.
		
		private final String ospValue;
		private String pfValue;
		private String pfValueAbbr;
		
		
		AdviseArrivalType(String ospValue, String pfValue, String pfValueAbbr){
			this.ospValue = ospValue;
			this.pfValue = pfValue;
			this.pfValueAbbr = pfValueAbbr;
		}
		
		public String toOSPValue(){
			return this.ospValue;
		}
		public String toPFValue(){
			return this.pfValue;
		}
		public String toPFValueAbbr() {
			return this.pfValueAbbr;
		}
		/**
		 * Clear PF value mapping for this advise arrival.
		 */
		public void clearPF() {
			this.pfValue = null;
			this.pfValueAbbr = null;
		}
	};

}
