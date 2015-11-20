/****************************************************************************
 *
 *                            Copyright (c), 2009
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/GetAccountDetailResponse.java $
 * 
 * 6     9/20/10 12:15p Ezhang
 * OSP 2.0 interface added errorCode.
 */
package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAccountDetailResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "accountCode",
					  "accountName",
					  "accountAttributeList",
					  "acctAddressList",
					  "inactive",
					  "startDate",
					  "expiryDate",
					  "callTakerMessagesList",
					  "driverNotesList",
					  "accountPriority",
					  "remarks",
					  "errorCode"
})
public class GetAccountDetailResponse extends BaseRes{
	
	@XmlElement(name="accountCode", required = true)
	private String accountCode;
	
	@XmlElement(name="accountName", required = true)
	private String accountName;
	
	@XmlElement(name="accountAttributeList", required = true)
	private Attribute[] accountAttributeList;

	@XmlElement(name="acctAddressList", required = true)
	private AcctAddrListItem[] acctAddressList;
	
	@XmlElement(name="inactive", required = true, defaultValue="N")
	private String inactive;

	@XmlElement(name="startDate", required = true)
	private String startDate;

	@XmlElement(name="expiryDate", required = true)
	private String expiryDate;

	@XmlElement(name="callTakerMessagesList", required = true)
	private Message[] callTakerMessagesList;
	
	@XmlElement(name="driverNotesList", required = true)
	private DriverNote[] driverNotesList;
	
	@XmlElement(name="accountPriority", required = true)
	private String accountPriority;
	
	@XmlElement(name="remarks", required = true)
	private String remarks;
	
	@XmlElement(name="errorCode", required = false)
	private Integer errorCode;
	
	public String getAccountCode() {
		return accountCode;
	}
	
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public Attribute[] getAccountAttributeList() {
		return accountAttributeList;
	}

	public void setAccountAttributeList(Attribute[] accountAttributeList) {
		this.accountAttributeList = accountAttributeList;
	}
	
	public AcctAddrListItem[] getAcctAddressList() {
		return acctAddressList;
	}

	public void setAcctAddressList(AcctAddrListItem[] acctAddressList) {
		this.acctAddressList = acctAddressList;
	}
	
	public String getInactive() {
		return inactive;
	}

	public void setInactive(String inactive) {
		this.inactive = inactive;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public Message[] getCallTakerMessagesList() {
		return callTakerMessagesList;
	}

	public void setCallTakerMessagesList(Message[] callTakerMessagesList) {
		this.callTakerMessagesList = callTakerMessagesList;
	}
	
	public DriverNote[] getDriverNotesList() {
		return driverNotesList;
	}

	public void setDriverNotesList(DriverNote[] driverNotesList) {
		this.driverNotesList = driverNotesList;
	}
	
	public String getAccountPriority() {
		return accountPriority;
	}

	public void setAccountPriority(String accountPriority) {
		this.accountPriority = accountPriority;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

}
