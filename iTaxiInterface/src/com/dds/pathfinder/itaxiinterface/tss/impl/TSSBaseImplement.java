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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/tss/impl/TSSBaseImplement.java $
 * 
 * 9/15/14, DChen, PF-16183, re arrange error code.
 * 
 *  PF-16183, 08/29/14, DChen,added TSS require service.
 * 
 * 08/25/14, DChen, create TSS project. 
 * 
 */

package com.dds.pathfinder.itaxiinterface.tss.impl;

import com.dds.pathfinder.itaxiinterface.osp.impl.OSPAddrLookupImplement;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.BaseRes;

public class TSSBaseImplement extends OSPAddrLookupImplement {

	public enum TSSErrorCode{
		TSS_SUCCESS("0001","SUCCESS"),
		TSS_FAILURE("0002","Failed"),
		TSS_INVALID_ADDRESS("0003","Invalid Adress and GPS coordinates"),
		TSS_BOOKING_FAILED("0004","Trip create failed"),
		TSS_INVALID_COMPANY("0005","Invalid company ID"),
		TSS_NOT_FINDPF_JOB_ID("0006","Not find PF job ID"),
		TSS_CANCEL_FAILED("0007","PF cancel job failed"),
		TSS_NOT_ENABLED("0008","Tss is not enabled"),
		TSS_HTTP_RESP_ERROR("0009","Tss http response error: "),
		TSS_UNKNOWN("9999","Unknown");
		
		private String errorCode;
		private String errorText;
		
		private TSSErrorCode(String errorCode, String errorText){
			this.errorCode = errorCode;
			this.errorText = errorText;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public String getErrorText() {
			return errorText;
		}
		
	};
	
	public final static String 	PARAMETER_C_TSS_ENABLE = "C_ENABLE_TSS";
	
	@Override
	public BaseRes generateResponse(BaseReq request) {
		return null;
	}

}
