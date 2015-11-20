package com.dds.pathfinder.itaxiinterface.osp.impl;

import javax.sql.DataSource;

import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.StopPoint;
import com.dds.pathfinder.callbooker.server.util.ContactInformation;
import com.dds.pathfinder.itaxiinterface.common.impl.BaseImplement;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;

public abstract class OSPBaseImplement extends BaseImplement implements OSPImplement{
	
	public String getUserName(BaseReq req, String sessionID){
		return getUserName(req.getSystemID(), sessionID);
	}
	
	public boolean validateSystemId(DataSource pfDataSource, BaseReq req){
		return validateSystemId(pfDataSource, req.getSystemID(), req.getSystemPassword());
	}
	
	/**
	 * Get OSP advise arrival flag from stop point address and contact info.
	 * @param stopPoint	the stop point
	 * @param contact	the contact info
	 * @return	OSP advise arrival flag value.
	 */
	public String getAdviseArrivalFlag(DataSource pfDataSource, StopPoint stopPoint, ContactInformation contact){
		//Get PF callout value from address or contact info
		//(This is very similar to com.dds.pathfinder.callbooker.client.util.visitor.CartItemWriterVisitor's getCalloutValue())
		AddressItem pickup = stopPoint.getPickupAddress();
		String pfCallout = AdviseArrivalType.NoAdvise.toPFValueAbbr();
		if(pickup != null && pickup.getJCallOutValue() != null && pickup.getJCallOutValue().length() > 0){
			pfCallout = pickup.getJCallOutValue();
		}else if(contact != null && contact.getCallOutValue() != null && contact.getCallOutValue().length() > 0){
			pfCallout = contact.getCallOutValue();
		}else if(pickup != null){
			pfCallout = pickup.getCallOutValue();
		}
		
		//return OSP advise arrival value using AdviseArrivalTypesImplement AdviseArrivalType mapping
		return AdviseArrivalTypesImplement.getOSPAdviseArrivalValue(pfDataSource, pfCallout); 
	}
}
