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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/LogoffImplement.java $
 * 
 * 1     2/25/10 3:09p Dchen
 * OSP interface.
 * 
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.BaseRes;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.LogoffRequest;
import com.dds.pathfinder.itaxiinterface.webservice.LogoffResponse;


public class LogoffImplement implements OSPImplement {

	private Logger logger = LogManager.getLogger(this.getClass());
	
	public LogoffResponse generateResponse(BaseReq request) {
		return generateLogoffResponse((LogoffRequest)request);
	}
	
	private LogoffResponse generateLogoffResponse(LogoffRequest request){
		LogoffResponse response = new LogoffResponse();
		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
		
		return response;
	}

}
