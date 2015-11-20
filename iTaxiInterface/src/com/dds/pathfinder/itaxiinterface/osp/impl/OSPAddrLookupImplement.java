package com.dds.pathfinder.itaxiinterface.osp.impl;

import javax.sql.DataSource;

import com.dds.pathfinder.callbooker.server.address.model.AddressItem;
import com.dds.pathfinder.callbooker.server.cart.model.StopPoint;
import com.dds.pathfinder.callbooker.server.util.ContactInformation;
import com.dds.pathfinder.itaxiinterface.common.impl.AddressLookupImplement;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;

public abstract class OSPAddrLookupImplement extends AddressLookupImplement implements OSPImplement{
	
	public OSPAddrLookupImplement(){
		super();
	}
	
	public OSPAddrLookupImplement(IAddressLookup addressLookUp){
		super(addressLookUp);
	}
	
	public String getUserName(BaseReq req, String sessionID){
		return getUserName(req.getSystemID(), sessionID);
	}
	
	public boolean validateSystemId(DataSource pfDataSource, BaseReq req){
		return validateSystemId(pfDataSource, req.getSystemID(), req.getSystemPassword());
	}
	
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
