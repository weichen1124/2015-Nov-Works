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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/ResponseResource.java $
* 
* PF-16605, 06/18/15, DChen, for 2nd device.
* 
* PF-16554, 05/19/15, DChen, check status before cancel job.
* 
* PF-16515, 04/29/15, DChen, validate attributes before post job.
* 
* PF-16428, 03/25/15, DChen, pfrest job events service.
* 
* PF-16428, 03/13/15, DChen, based on json-api format
* 
* **************************************/

package com.dds.pathfinder.pfrest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseResource extends PFResource{
	
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "data")
	private BaseResponse dataResource;

	public ResponseResource(){
		
	}
	
	
	
	public ResponseResource(BaseResponse response) {
		super();
		this.dataResource = response;
	}
	
	
	public static String ERROR_CODE_SUCCESS = "Success";
	public static String ERROR_CODE_FAILED  = "Failed";
	public static String ERROR_CODE_EMPTY_ADDRESS = "Empty Address. Ensure Street name, district and a valid house number or landmark is defined ";
	public static String ERROR_CODE_INVALID_REGION = "Invalid District";
	public static String ERROR_CODE_INVALID_STREET = "Invalid Street name";
	public static String ERROR_CODE_INVALID_LANDMARK = "Invalid Address. Landmark name is not valid. Either correct landmark name or enter correct house number";
	public static String ERROR_CODE_INVALID_BLOCKFACE = " Invalid Blockface";
	public static String ERROR_CODE_INVALID_BUILDING = "Invalid Building address";
	public static String ERROR_CODE_INVALID_ADDRESS = "Invalid address";
	public static String ERROR_CODE_INVALID_ACCOUNT_DETAILS= "Invalid Account. PF account is not setup";
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
	public static String ERROR_CODE_INVALID_INPUT_TARIFF = "Invalid tariff number";
	public static String ERROR_CODE_INVALID_INPUT_JOB_ATTR = "Invalid job attribute";
	public static String ERROR_CODE_INVALID_EXTERNAL_JOB_ID = "Invalid (not unique) external job id";
	public static String ERROR_CODE_INVALID_JOB_STATUS = "Invalid job status";
	public static String ERROR_CODE_DRIVER_NOT_FOUND = "Not find driver";
	public static String ERROR_CODE_INVALID_DRIVER_PIN = "Invalid driver pin";
	
	
	
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


	public void setDataResource(BaseResponse dataResource) {
		this.dataResource = dataResource;
	}

	@Override
	public BaseResponse getDataResource() {
		return dataResource;
	}

}
