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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/common/impl/BaseImplement.java $
 * 
 * PF-16597, 06/09/15, DChen, add system id in pfrest.
 * 
 * PF-16385, 03/03/15, DChen, share with pfrest.
 * 
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.common.impl;


import javax.sql.DataSource;

import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.callbooker.server.paramcache.OSPUser;

public abstract class BaseImplement implements CommonImplement {
	
	
	protected LoadDispatchParametersLocal cachedParam;
	
	//PF-15917 this id is stored in osp_users table
	public static final int TAXILIMO_MOBILE_BOOKER_SYS_ID  = 102;
	public static final int TAXILIMO_WEB_BOOKER_SYS_ID = 103;

	public String getUserName(int systemID, String sessionID){
		if(cachedParam != null && cachedParam.getOSPLogonCode(systemID) != null){
			return cachedParam.getOSPLogonCode(systemID);
		}
		
		for(ExternalSystemId id : ExternalSystemId.values()){
			if(systemID == id.getSystemId()) return id.getLogonCode();
		}
		
		
		if(sessionID == null || sessionID.length() == 0){
			return ExternalSystemId.SYSTEM_ID_DEFAULT.getLogonCode();
		}
		return sessionID;
	}
	/**
	 * Validate Request system id and password
	 * @param pfDataSource
	 * @param request
	 * @return
	 */
	public boolean validateSystemId(DataSource pfDataSource, int systemID, String systemPwd){
		boolean sysValid = false;
		int systemId = -1;
		systemId = systemID;
		String password = null;
		int ospSystemId = -1;
		
		
		//we don't check for web booker for now
		if(systemId > 0) {
			if(systemId == ExternalSystemId.SYSTEM_ID_WEB_BOOKER.getSystemId() || systemId == ExternalSystemId.SYSTEM_ID_UDI_USER.getSystemId()
					|| isAccessableInternalJobs(systemId)){
					sysValid = true;
			}
			else if (systemId == ExternalSystemId.SYSTEM_ID_GOFASTCAB.getSystemId()){
				//validate the password
				//password = CompanyDefaultValues.getSystemParameterValue(pfDataSource, "S_GFC_SYS_PWD");
				password = cachedParam.getSystemParameterValue(CompanyDefaultValues.SYSTEM_PARAMETER_GFC_PWD);
				if(password != null && password.equals(systemPwd)){
					sysValid = true;
				}
			}
			else if (systemId == ExternalSystemId.SYSTEM_ID_MB.getSystemId()){
				//PF-14809 validate the password
				//password = CompanyDefaultValues.getSystemParameterValue(pfDataSource, "S_MB_SYS_PWD");
				password = cachedParam.getSystemParameterValue(CompanyDefaultValues.SYSTEM_PARAMETER_MB_PWD);
				if(password != null && password.equals(systemPwd)){
					sysValid = true;
				}
			}else if ( cachedParam != null ) {
				// PF-15967 two more system id need to validate with S_MB_SYS_PWD
				OSPUser ospUser = cachedParam.getOSPUser(systemId);
				if(ospUser != null) {
					ospSystemId = ospUser.getSystemID();
					if(ospSystemId == TAXILIMO_MOBILE_BOOKER_SYS_ID || ospSystemId == TAXILIMO_WEB_BOOKER_SYS_ID){
						password = cachedParam.getSystemParameterValue(CompanyDefaultValues.SYSTEM_PARAMETER_MB_PWD);
						if(password != null && password.equals(systemPwd)){
							sysValid = true;
						}
					}else sysValid = true;
				}
				
			}
			
		}
		return sysValid;
	}
	/**
	 * get system reference type from system id  PF-14809
	 * @param systemId systemId from SOAP request
	 * @return matching systemReference
	 */
	public String getSystemReference(int systemId) {
		String sysReference = null;
		
		if(systemId == ExternalSystemId.SYSTEM_ID_GOFASTCAB.getSystemId() ) {
			sysReference = ExternalSystemId.SYSTEM_ID_GOFASTCAB.getReference();
		}	
		else if(systemId == ExternalSystemId.SYSTEM_ID_MB.getSystemId() ) {
			sysReference = ExternalSystemId.SYSTEM_ID_MB.getReference();
		}
		// ezhang no need for now but might need later
		else if(systemId == ExternalSystemId.SYSTEM_ID_WEB_BOOKER.getSystemId() ) {
			sysReference =  ExternalSystemId.SYSTEM_ID_WEB_BOOKER.getReference();
		}else if(systemId == ExternalSystemId.SYSTEM_ID_UDI_USER.getSystemId() ) {
			sysReference =  ExternalSystemId.SYSTEM_ID_UDI_USER.getReference();
		}else{
			if(cachedParam != null) return cachedParam.getOSPReference(systemId); 
		}
		
		
		return sysReference;
		
	}
	
//	/**
//	 * Get OSP advise arrival flag from stop point address and contact info.
//	 * @param stopPoint	the stop point
//	 * @param contact	the contact info
//	 * @return	OSP advise arrival flag value.
//	 */
//	public String getAdviseArrivalFlag(DataSource pfDataSource, StopPoint stopPoint, ContactInformation contact){
//		//Get PF callout value from address or contact info
//		//(This is very similar to com.dds.pathfinder.callbooker.client.util.visitor.CartItemWriterVisitor's getCalloutValue())
//		AddressItem pickup = stopPoint.getPickupAddress();
//		String pfCallout = AdviseArrivalType.NoAdvise.toPFValueAbbr();
//		if(pickup != null && pickup.getJCallOutValue() != null && pickup.getJCallOutValue().length() > 0){
//			pfCallout = pickup.getJCallOutValue();
//		}else if(contact != null && contact.getCallOutValue() != null && contact.getCallOutValue().length() > 0){
//			pfCallout = contact.getCallOutValue();
//		}else if(pickup != null){
//			pfCallout = pickup.getCallOutValue();
//		}
//		
//		//return OSP advise arrival value using AdviseArrivalTypesImplement AdviseArrivalType mapping
//		return AdviseArrivalTypesImplement.getOSPAdviseArrivalValue(pfDataSource, pfCallout); 
//	}
	
	
	public LoadDispatchParametersLocal getCachedParam() {
		return cachedParam;
	}
	public void setCachedParam(LoadDispatchParametersLocal cachedParam) {
		this.cachedParam = cachedParam;
	}
	
	public String getPFRestServiceAttribute(String attrName){
		if(attrName == null || attrName.trim().length() == 0) return attrName;
		if(cachedParam != null && cachedParam.getPFRestConfigAttributes() != null){
			return cachedParam.getPFRestConfigAttributes().get(attrName);
		}else{
			return null;
		}
	}
	
	public int getPFRestServiceAttributeAsInt(String attrName, int defaultValue){
		if(attrName == null || attrName.trim().length() == 0) return defaultValue;
		if(cachedParam != null && cachedParam.getPFRestConfigAttributes() != null){
			String strValue = cachedParam.getPFRestConfigAttributes().get(attrName);
			if(strValue == null || strValue.trim().length() == 0) return defaultValue;
			try{
				return Integer.parseInt(strValue);
			}catch(NumberFormatException e){
				return defaultValue;
			}
		}else{
			return defaultValue;
		}
	}
	
	public boolean isAccessableInternalJobs(int systemID){
		if(cachedParam == null ) return false;
		else return cachedParam.getOSPAccessableIntJobs(systemID);
	}
	
	public boolean isValidFloatValue(float value){
		return(value <= MAX_FLOAT_VALUE && value >= (-MAX_FLOAT_VALUE));
	}
	   
	public boolean isValidDoubleValue(double value){
		return(value <= MAX_FLOAT_VALUE && value >= (-MAX_FLOAT_VALUE));
	}
	
}
