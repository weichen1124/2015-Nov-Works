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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GPSServiceableResponse.java $
 * 
 * PF-16398, 04/06/15, DChen, add isAddressServiceable service
 * 
 * 
 *****/
package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GPSServiceableResponseType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "errorCode"})
public class GPSServiceableResponse extends BaseRes {
	
	public enum GPSErrorCode {
		ERROR_CODE_NOERROR(0,"Area is serviceable"),			
		ERROR_CODE_NOTAUTH(1,"Not authenticated, invalid system ID or password"),
		ERROR_CODE_INVALID_COMPANY(2,"Invalid taxi company Id"),
		ERROR_CODE_INVALID_GPS(3,"Invalid latitude or longitude"),
		ERROR_CODE_INVALID_UNKNOWN(4,"Unknown error"),
		ERROR_CODE_ALS_FAILED(5,"ALS service not available"),
		ERROR_CODE_NOTSERVICEABLE(0,"Area is not serviceable");
			
		private int errorCode;
		private String errorString;
		
		private GPSErrorCode(int errorCode, String errorString) {
			   this.errorCode = errorCode;
			   this.errorString = errorString;
		}

		public final int getErrorCode() {
			   return errorCode;
		}
		
		public String getErrorString(){
			return errorString;
		}
		
	};
	
	
	@XmlElement(name="error_code",   required = true)
	int errorCode;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
