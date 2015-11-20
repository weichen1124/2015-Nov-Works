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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/RecallJobsRequest.java $
 * 
 * 12    10/23/13 3:04p Ezhang
 * PF-15627 -add new recall criteria
 * 
 * 11    4/23/12 4:37p Ajiang
 * PF-14393 - added default values to optional request fields
 * 
 * 10    9/29/10 2:08p Ezhang
 * change the webuserId field back from required to optional to allow user to recall trip without login.
 * 
 * 9     9/28/10 1:22p Ezhang
 * Added recall by confirmation number
 * 
 * 8     9/27/10 11:04a Ajiang
 * Set default osp version to 2.0
 * 
 * 7     9/20/10 1:54p Ezhang
 * OSP 2.0 added ospVersion
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecallJobsReqType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "sessionID", 
						  "taxiCoID", 
						  "ride_id", 
						  "taxiRideID", 
						  "webUserID",
						  "passengerName",
						  "phoneNumber",
						  "pickupStreetNumber",
						  "pickupStreetName",
						  "pickupUnitNumber",
						  "pickupLandmark",
						  "accountCode",
						  "recallTripType",
			 			  "recallCriteria",
						  "fromTripRsvTime",
						  "toTripRsvTime",
						  "ospVersion",
						  "confirmationNum",
						  "jobList"
						  })
public class RecallJobsRequest extends BaseReq{
	
	@XmlElement(name="sessionID", required = false)
	private String sessionID;
	
	@XmlElement(name="taxi_company_id", required = true )
	private Integer taxiCoID;
	
	@XmlElement(name="ride_id", required = false, defaultValue = "0")
	private Long ride_id;
	
	@XmlElement(name="taxi_ride_id", required = false, defaultValue = "0")
	private Long taxiRideID;
	
	@XmlElement(name="webUserID", required = false)
	private String webUserID;
	
	@XmlElement(name="passengerName", required = false)
	private String passengerName;
	
	@XmlElement(name="phoneNumber", required = false)
	private String phoneNumber;
	
	@XmlElement(name="pickupStreetNumber", required = false)
	private String pickupStreetNumber;
	
	@XmlElement(name="pickupStreetName", required = false)
	private String pickupStreetName;
	
	@XmlElement(name="pickupUnitNumber", required = false)
	private String pickupUnitNumber;
	
	@XmlElement(name="pickupLandmark", required = false)
	private String pickupLandmark;
	
	@XmlElement(name="accountCode", required = false)
	private String accountCode;
	
	@XmlElement(name="recallTripType", required = false)
	private String recallTripType;
	
	@XmlElement(name="recallCriteria", required = true)
	private Integer recallCriteria;
		
	@XmlElement(name="fromTripRsvTime", required = false)
	private String fromTripRsvTime;
	
	@XmlElement(name="toTripRsvTime", required = false)
	private String toTripRsvTime;
		
	@XmlElement(name="ospVersion", required = false, defaultValue="2.0")
	private String ospVersion;
	
	@XmlElement(name="confirmationNum", required = false)
	private String confirmationNum;
	
	@XmlElement(name="jobList", required = false)
	private String jobList;
	
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	public Integer getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(Integer taxiCoID) {
		this.taxiCoID = taxiCoID;
	}	

	public Long getRideID() {
		return ride_id;
	}

	public void setRideID(Long rideID) {
		this.ride_id = rideID;
	}
	
	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}
	
	public String getWebUserID() {
		return webUserID;
	}

	public void setWebUserID(String webUserID) {
		this.webUserID = webUserID;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPickupStreetNumber() {
		return pickupStreetNumber;
	}

	public void setPickupStreetNumber(String pickupStreetNumber) {
		this.pickupStreetNumber = pickupStreetNumber;
	}
	
	public String getPickupStreetName() {
		return pickupStreetName;
	}

	public void setPickupStreetName(String pickupStreetName) {
		this.pickupStreetName = pickupStreetName;
	}
	
	public String getPickupUnitNumber() {
		return pickupUnitNumber;
	}

	public void setPickupUnitNumber(String pickupUnitNumber) {
		this.pickupUnitNumber = pickupUnitNumber;
	}
	
	public String getPickupLandmark() {
		return pickupLandmark;
	}

	public void setPickupLandmark(String pickupLandmark) {
		this.pickupLandmark = pickupLandmark;
	}
	
	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	
	public String getRecallTripType() {
		return recallTripType;
	}

	public void setRecallTripType(String recallTripType) {
		this.recallTripType = recallTripType;
	}

	public Integer getRecallCriteria() {
		return recallCriteria;
	}

	public void setRecallCriteria(Integer recallCriteria) {
		this.recallCriteria = recallCriteria;
	}
	
	public String getFromTripRsvTime() {
		return fromTripRsvTime;
	}
	
	public void setFromTripRsvTime(String fromTripRsvTime) {
		this.fromTripRsvTime = fromTripRsvTime;
	}
	
	public String getToTripRsvTime() {
		return toTripRsvTime;
	}
	
	public void setToTripRsvTime(String toTripRsvTime) {
		this.toTripRsvTime = toTripRsvTime;
	}	
	
	public String getOspVersion() {
		return ospVersion;
	}

	public void setOspVersion(String ospVersion) {
		this.ospVersion = ospVersion;
	}

	/**
	 * @param confirmationNum the confirmationNum to set
	 */
	public void setConfirmationNum(String confirmationNum) {
		this.confirmationNum = confirmationNum;
	}

	/**
	 * @return the confirmationNum
	 */
	public String getConfirmationNum() {
		return confirmationNum;
	}

	/**
	 * @return the jobList
	 */
	public String getJobList() {
		return jobList;
	}

	/**
	 * @param jobList the jobList to set
	 */
	public void setJobList(String jobList) {
		this.jobList = jobList;
	}
}
