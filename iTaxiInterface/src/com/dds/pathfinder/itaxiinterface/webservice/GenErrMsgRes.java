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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GenErrMsgRes.java $
 * 
 * PF-16496, 05/27/15, DChen, add pre dispatch eta.
 * 
 * 16    1/16/14 3:28p Dchen
 * PF-15751, OSP to validate pickup and phone number.
 * 
 * 15    1/07/14 10:49a Sfoladian
 * PF-15808- Added the validation on the Account Status (Closed/Suspended)
 * Corrected  the typo on ERROR_CODE_ACC_NOT_ACTIVE
 * 
 * 14    12/31/13 1:49p Sfoladian
 * MG-304- account expiry and effective dates should also be validated
 * during registration 
 * 
 * 13    11/29/13 1:51p Sfoladian
 * Added new error code for Account Payment: ERROR_CODE_JOB_NOT_READY
 * 
 * 12    11/28/13 11:06a Sfoladian
 * 
 * 11    11/28/13 9:52a Sfoladian
 * Added error codes for PF-15748- Mobile Booker Account Payment Support
 * Part I
 * 
 * 10    11/21/13 3:18p Sfoladian
 * Added error message for AccountPayment
 * 
 * 9     10/23/13 3:05p Ezhang
 * PF_15613
 * 
 * 8     11/27/12 1:08p Dchen
 * PF-14930, Veolia osp changes.
 * 
 * 7     2/12/11 10:04a Ezhang
 * 
 * 6     5/13/11 11:05a Dchen
 * MACI support.
 * 
 * 5     4/15/10 11:05a Mkan
 * - added error message for preferred driver/vehicle not allwed for ASAP
 * job
 * 
 * 4     2/23/10 2:40p Dchen
 * OSP interface.
 * 
 * 3     1/11/10 1:22p Dchen
 * some code changes.
 * 
 * 2     9/25/09 5:44p Dchen
 * pathfinder iTaxi interface.
 * 
 * 1     8/13/09 12:04p Dchen
 * pathfinder iTaxi interface.
 * 
 */

package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetErrMsgResType", namespace="http://message.riderway.webservice.taxiproduct.dds.com/", propOrder = { "errorCode","errorText"})
public class GenErrMsgRes {
	@XmlElement(name="ErrorCode", namespace="http://message.riderway.webservice.taxiproduct.dds.com/",  required = true)
	private String errorCode;
	@XmlElement(name="ErrText", namespace="http://message.riderway.webservice.taxiproduct.dds.com/",  required = true)
	private String errorText;
	
	public static String ERROR_CODE_SUCCESS = "Success";
	public static String ERROR_CODE_FAILED  = "Failed";
	public static String ERROR_CODE_EMPTY_ADDRESS = "Empty Address. Ensure Street name, district and a valid house number or landmark is defined ";
	public static String ERROR_CODE_INVALID_REGION = "Invalid District";
	public static String ERROR_CODE_INVALID_STREET = "Invalid Street name";
	public static String ERROR_CODE_INVALID_LANDMARK = "Invalid Address. Landmark name is not valid. Either correct landmark name or enter correct house number";
	public static String ERROR_CODE_INVALID_BLOCKFACE = " Invalid Blockface";
	public static String ERROR_CODE_INVALID_BUILDING = "Invalid Building address";
	public static String ERROR_CODE_INVALID_ADDRESS = "Invalid address";
	public static String ERROR_CODE_INVALID_ACCOUNT_DETAILS= "Invalid Account. Rider Way account is not setup";
	public static String ERROR_CODE_INVALID_COMPANY = "Invalid Company";
	public static String ERROR_CODE_INVALID_DATE_FORMAT = "Invalid Date. ";
	public static String ERROR_CODE_INVALID_GPS = "Invalid GPS point";
	public static String ERROR_CODE_INVALID_GPS_ADDRESS = "Can't find any address for this GPS";
	public static String ERROR_CODE_INVALID_JOB_ID = "Invalid Job Id";
	public static String ERROR_CODE_INVALID_REQUEST = "Invalid Request";
	public static String ERROR_CODE_INVALID_RIDE_ID = "Rider ID must be defined";
	public static String ERROR_CODE_INVALID_USER_NAME = "Invalid User Name or Password";
	public static String ERROR_CODE_INVALID_PREF_ASAP = "Preferred Driver or Vehicle is not allowed for ASAP Job";
	public static String ERROR_CODE_NOT_AUTHENTICATED = "Not authenticated. Invalid systemID or systemPassword";
	public static String ERROR_CODE_NOT_FIND_ANYTHING = "Not find anything";
	public static String ERROR_CODE_INVALID_ACCOUNT= "Invalid Account. Invalid Account or Password";
	public static String ERROR_CODE_INVALID_DEVICE_ID= "Invalid Device Id";
	public static String ERROR_CODE_ACC_EXPIRED= "Account has expired";
	public static String ERROR_CODE_ACC_NOT_ACTIVE= "Account is not active";
	public static String ERROR_CODE_INVALID_TELEPHONE = "Invalid telephone number";
	public static String ERROR_CODE_INVALID_MAX_AGE = "Invalid GPS latency";
	
	
	
	
	//ACCOUNT PAYMENT
	public static String ERROR_CODE_INVALID_PAYMNT_AMOUNT= "Invalid payment amount";
	public static String ERROR_CODE_INVALID_PAYMNT_TIP= "Invalid payment tip";
	public static String ERROR_CODE_DUPLICATE_PAYMNT= "Payment declined. Already paid";
	public static String ERROR_CODE_NOT_AUTH_PAYMNT= "Payment declined. Not Authorized";
	public static String ERROR_CODE_CASH_ACCOUNTT= "Payment declined. Cash Account";
	public static String ERROR_CODE_CC_ACCOUNT= "Payment declined. Credit card Account";
	public static String ERROR_CODE_PREPAID_ACCOUNT= "Payment declined. Prepaid Account";
	public static String ERROR_CODE_ACCOUNT_EXPIRED= "Payment declined. Account Expired";
	public static String ERROR_CODE_ACCOUNT_CLOSED= "Payment declined. Account Closed";
	public static String ERROR_CODE_ACCOUNT_SUSPENDED= "Payment declined. Account Suspended";
	public static String ERROR_CODE_EXCEED_MAX_TRIPS= "Payment declined. Exceeded max number of trips";
	public static String ERROR_CODE_OVER_LIMIT= "Payment declined. Account Over limit";
	public static String ERROR_CODE_DISABLED_ACCOUNT= "Payment declined. Account disabled";
	public static String ERROR_CODE_NO_FUND= "Payment declined. No funds";
	public static String ERROR_CODE_JOB_NOT_READY= "Payment declined. Trip not ready for payment";
	
	
	
	
	public static int STATUS_SUCCESS = 0;
	public static int STATUS_FAILED = 1;
	public static int STATUS_CANCELLED = 2;
	public static int STATUS_BOOKING_FAIL_INVALIDADDRESS = 3;
	
	public static int ERROR_CODE_PAYMENT_SUCCESS = 0;
	public static int ERROR_CODE_PAYMENT_FAILED  = 1;
	public static int ERROR_CODE_PAYMENT_INVALID_JOB  = 2;
	public static String ERROR_TEXT_PAYMENT_INVALID_JOB = "Invalid Job Id";
	public static String ERROR_TEXT_EMPTY = "";
	public static String ERROR_TEXT_FAILURE = "Failed to send payment to mobile";
	
	//public static int SYSTEM_ID_WEB_BOOKER = 999;
	//public static int SYSTEM_ID_MOB_PDA = 501;				//hard code until C35317 is solved
	
	public static String PF_USER_WEB_BOOKER = "IUSER";
	public static String PF_DEFAULT_USER_NAME = "SYSADMIN";
	
	public GenErrMsgRes()
	{
		
	}
	
	public GenErrMsgRes(String code, String text)
	{
		errorCode = code;
		errorText = text;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorText() {
		return errorText;
	}
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
}
