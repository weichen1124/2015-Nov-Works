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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/OSPImplement.java $
 * 
 * 8     4/14/14 3:55p Dchen
 * PF-15051, added SUCCESS PARTIAL for ALS.
 * 
 * 7     2/12/11 10:01a Ezhang
 * C36130 Add systemID and system Password support for GoFastCar
 * 
 * 6     9/28/10 1:23p Ezhang
 * added more job_status and vehicle_status.
 * 
 * 5     9/27/10 3:44p Ezhang
 * 
 * 4     9/27/10 3:36p Ezhang
 * added MAX_ADDRESS_MATCH to set upper limit of Advanced Address search result.
 * 
 * 3     9/27/10 11:30a Ezhang
 * added new job state constant
 * 
 * 2     9/20/10 2:17p Ezhang
 * OSP 2.0 added new const.
 * 
 * 1     1/11/10 1:32p Dchen
 * OSP interface.
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.osp.impl;

import com.dds.pathfinder.itaxiinterface.common.impl.CommonImplement;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.BaseRes;

public interface OSPImplement extends CommonImplement {
	public BaseRes generateResponse(BaseReq request);
}
