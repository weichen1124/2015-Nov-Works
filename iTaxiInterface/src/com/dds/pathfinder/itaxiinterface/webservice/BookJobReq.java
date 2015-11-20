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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/BookJobReq.java $
 * 17 Sep 2015 Y Yin
 * PF-16766. Added paymentMethod.
 * 
 * 10    11/14/13 5:27p Mkan
 * added deviceId for MobileGateway MG-259
 * 
 * 9     10/09/12 5:20p Dchen
 * PF-14785, added reference nb, and extra inf for BW webbooker.
 * 
 * 8     5/11/12 12:02p Ezhang
 * PF-14413 - added default value to "Y" for forcedAddressFlag since it is optional but 
 * OSPCartImplement.validateAddresses() use this value.
 * 
 * 7     4/23/12 4:35p Ajiang
 * PF-14393 - added default values to optional request fields
 * 
 * 6     9/27/10 11:02a Ajiang
 * Set default osp version to 2.0
 * 
 * 5     9/20/10 2:00p Ezhang
 * OSP 2.0 added confirmationNum etc.
 * 
 * 4     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 3     9/25/09 5:43p Dchen
 * pathfinder iTaxi interface.
 * 
 * 2     9/17/09 3:40p Dchen
 * pathfinder iTaxi interface.
 * 
 * 1     8/14/09 4:21p Dchen
 * pathfinder iTaxi interface.
 * 
 * 
 */
package com.dds.pathfinder.itaxiinterface.webservice;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BookJobReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", 
						  "deviceID",
						  "accountCode",
						  "accountName",
						  "authNum",
						  "adviseArrival",
						  "type",
						  "priority",
						  "priorityReason",
						  "cabNum",
						  "flatRate",
						  "attributeList",
						  "repPickupTime",
						  "repSuspendStart",
						  "repSuspendEnd",
						  "repStart",
						  "repEnd",
						  "holidayList",
						  "webUserID",
						  "email",
						  "callBackTime",
						  "nbofCallBacks",
						  "forcedAddressFlag",
						  "accountPassword",
						  "numTaxis",
						  "confirmationNumRequired",
						  "referenceNumber",
						  "extraInformation",
						  "ospVersion",
						  "confirmationNum",
						  "spExtraInfo1",
						  "jobExtraInfo1",
						  "jobExtraInfo2",
						  "jobExtraInfo3",
						  "jobExtraInfo4",
						  "jobExtraInfo5",
						  "paymentMethod"
						  })
public class BookJobReq extends CoreBookJobReq {

	@XmlElement(name="sessionID", required = false, defaultValue="")
	private String sessionID;
	
	//Added for MB/MG use only, this field will get ignored by PF (MG-259).
	@XmlElement(name="deviceID", required = false, defaultValue="")
	private String deviceID;
	
	@XmlElement(name="accountCode", required = false, defaultValue="")
	private String accountCode;
	
	@XmlElement(name="accountName", required = false, defaultValue="")
	private String accountName;
	
	@XmlElement(name="authNum", required = false, defaultValue="")
	private String authNum;
	
	@XmlElement(name="adviseArrival", required = false, defaultValue="")
	private String adviseArrival;
	
	@XmlElement(name="type", required = false, defaultValue="")
	private String type;
	
	@XmlElement(name="priority", required = false, defaultValue="")
	private String priority;
	
	@XmlElement(name="priorityReason", required = false, defaultValue="")
	private String priorityReason;
	
	@XmlElement(name="cabNum", required = false, defaultValue="")
	private String cabNum;
	
	@XmlElement(name="flatRate", required = false, defaultValue="0")
	private String flatRate;
	
	@XmlElement(name="attributelist", required = false)
	private Attribute[] attributeList;
	
	@XmlElement(name="rep_pickup_time", required = false, defaultValue="")
	private String repPickupTime;
	
	@XmlElement(name="repSuspendStart", required = false, defaultValue="")
	private String repSuspendStart;
	
	@XmlElement(name="repSuspendEnd", required = false, defaultValue="")
	private String repSuspendEnd;
	
	@XmlElement(name="repStart", required = false, defaultValue="")
	private String repStart;
	
	@XmlElement(name="repEnd", required = false, defaultValue="")
	private String repEnd;
	
	@XmlElement(name="HolidayList", required = false, defaultValue="")
	private String holidayList;
	
	@XmlElement(name="webUserID", required = false, defaultValue="")
	private String webUserID;
	
	@XmlElement(name="email", required = false, defaultValue="")
	private String email;
	
	@XmlElement(name="callBackTime", required = false, defaultValue="")
	private String callBackTime;
	
	@XmlElement(name="nofCallBacks", required = false, defaultValue = "0")
	private Integer nbofCallBacks;
	
	@XmlElement(name="forcedAddressFlag", required = false, defaultValue="Y")
	private String forcedAddressFlag;
	
	@XmlElement(name="accountPassword", required = false, defaultValue="")
	private String accountPassword;
	
	@XmlElement(name="numTaxis", required = true, defaultValue="1")
	private Integer numTaxis;

	@XmlElement(name="confirmationNumRequired", required = false, defaultValue="N")
	private String confirmationNumRequired;
	
	@XmlElement(name="referenceNumber", required = false, defaultValue="")
	private String referenceNumber;
	
	@XmlElement(name="extraInformation", required = false, defaultValue="")
	private String extraInformation;	
	
	@XmlElement(name="ospVersion", required = false, defaultValue="2.0")
	private String ospVersion;
	
	@XmlElement(name="confirmationNum",   required = false)
	private String confirmationNum;
	
	@XmlElement(name="spExtraInfo1",   required = false)
	private String spExtraInfo1;
	
	@XmlElement(name="jobExtraInfo1",   required = false)
	private String jobExtraInfo1;
	
	@XmlElement(name="jobExtraInfo2",   required = false)
	private String jobExtraInfo2;
	
	@XmlElement(name="jobExtraInfo3",   required = false)
	private String jobExtraInfo3;
	
	@XmlElement(name="jobExtraInfo4",   required = false)
	private String jobExtraInfo4;
	
	@XmlElement(name="jobExtraInfo5",   required = false)
	private String jobExtraInfo5;
	
	@XmlElement(name="paymentMethod",   required = false, defaultValue="0")
	private Integer paymentMethod;
	
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

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

	public String getAuthNum() {
		return authNum;
	}

	public void setAuthNum(String authNum) {
		this.authNum = authNum;
	}

	public String getAdviseArrival() {
		return adviseArrival;
	}

	public void setAdviseArrival(String adviseArrival) {
		this.adviseArrival = adviseArrival;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getPriorityReason() {
		return priorityReason;
	}

	public void setPriorityReason(String priorityReason) {
		this.priorityReason = priorityReason;
	}


	public String getWebUserID() {
		return webUserID;
	}

	public void setWebUserID(String webUserID) {
		this.webUserID = webUserID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCallBackTime() {
		return callBackTime;
	}

	public void setCallBackTime(String callBackTime) {
		this.callBackTime = callBackTime;
	}

	public Integer getNbofCallBacks() {
		return nbofCallBacks;
	}

	public void setNbofCallBacks(Integer nbofCallBacks) {
		this.nbofCallBacks = nbofCallBacks;
	}

	public String getForcedAddressFlag() {
		if(forcedAddressFlag == null) {
			forcedAddressFlag = "Y";
		}
		return forcedAddressFlag;
	}

	public void setForcedAddressFlag(String forcedAddressFlag) {
		this.forcedAddressFlag = forcedAddressFlag;
	}

	public String getCabNum() {
		return cabNum;
	}

	public void setCabNum(String cabNum) {
		this.cabNum = cabNum;
	}

	public String getFlatRate() {
		return flatRate;
	}

	public void setFlatRate(String flatRate) {
		this.flatRate = flatRate;
	}

	public Attribute[] getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(Attribute[] attributeList) {
		this.attributeList = attributeList;
	}

	public String getRepPickupTime() {
		return repPickupTime;
	}

	public void setRepPickupTime(String repPickupTime) {
		this.repPickupTime = repPickupTime;
	}

	public String getRepSuspendStart() {
		return repSuspendStart;
	}

	public void setRepSuspendStart(String repSuspendStart) {
		this.repSuspendStart = repSuspendStart;
	}

	public String getRepSuspendEnd() {
		return repSuspendEnd;
	}

	public void setRepSuspendEnd(String repSuspendEnd) {
		this.repSuspendEnd = repSuspendEnd;
	}

	public String getRepStart() {
		return repStart;
	}

	public void setRepStart(String repStart) {
		this.repStart = repStart;
	}

	public String getRepEnd() {
		return repEnd;
	}

	public void setRepEnd(String repEnd) {
		this.repEnd = repEnd;
	}

	public String getHolidayList() {
		return holidayList;
	}

	public void setHolidayList(String holidayList) {
		this.holidayList = holidayList;
	}

	public Integer getNumTaxis() {
		return numTaxis;
	}

	public void setNumTaxis(Integer numTaxis) {
		this.numTaxis = numTaxis;
	}

	public String getAccountPassword() {
		//PF-14413
		if(accountPassword == null) {
			accountPassword = "";
		}
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}
	
	public String getConfirmationNumRequired() {
		//PF_14413 added default value
		if(confirmationNumRequired == null) {
			confirmationNumRequired = "N";
		}
		return confirmationNumRequired;
	}

	public void setConfirmationNumRequired(String confirmationNumRequired) {
		this.confirmationNumRequired = confirmationNumRequired;
	}
	
	public String getOspVersion() {
		return ospVersion;
	}

	public void setOspVersion(String ospVersion) {
		this.ospVersion = ospVersion;
	}
	
	public String getConfirmationNum() {
		return confirmationNum;
	}

	public void setConfirmationNum(String confirmationNum) {
		this.confirmationNum = confirmationNum;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getExtraInformation() {
		return extraInformation;
	}

	public void setExtraInformation(String extraInformation) {
		this.extraInformation = extraInformation;
	}

	public String getSpExtraInfo1() {
		return spExtraInfo1;
	}

	public void setSpExtraInfo1(String spExtraInfo1) {
		this.spExtraInfo1 = spExtraInfo1;
	}

	public String getJobExtraInfo1() {
		return jobExtraInfo1;
	}

	public void setJobExtraInfo1(String jobExtraInfo1) {
		this.jobExtraInfo1 = jobExtraInfo1;
	}

	public String getJobExtraInfo2() {
		return jobExtraInfo2;
	}

	public void setJobExtraInfo2(String jobExtraInfo2) {
		this.jobExtraInfo2 = jobExtraInfo2;
	}

	public String getJobExtraInfo3() {
		return jobExtraInfo3;
	}

	public void setJobExtraInfo3(String jobExtraInfo3) {
		this.jobExtraInfo3 = jobExtraInfo3;
	}

	public String getJobExtraInfo4() {
		return jobExtraInfo4;
	}

	public void setJobExtraInfo4(String jobExtraInfo4) {
		this.jobExtraInfo4 = jobExtraInfo4;
	}

	public String getJobExtraInfo5() {
		return jobExtraInfo5;
	}

	public void setJobExtraInfo5(String jobExtraInfo5) {
		this.jobExtraInfo5 = jobExtraInfo5;
	}
	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}
