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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/RecallJobDetailResponse.java $
 * 
 * PF-16496, 05/29/15, DChen, add after dispatch eta.
 * 
 * PF-16547, Add car colour, brand, driverID, taxiID and registration plate
 * 
 * 11    11/27/12 1:09p Dchen
 * PF-14930, Veolia osp changes.
 * 
 * 10    9/20/10 1:59p Ezhang
 * OSP 2.0 added errorCode.
 * 
 * 9     2/04/10 3:14p Dchen
 * pickup latitude, longitude.
 * 
 * 8     1/22/10 4:53p Dchen
 * OSP interface.
 * 
 * 7     11/23/09 3:18p Yyin
 * Added email
 * 
 * 6     11/17/09 9:23a Yyin
 * Added trip reservation time
 * 
 * 5     10/16/09 2:28p Yyin
 * crePerID added
 * 
 * 4     10/15/09 6:21p Yyin
 * 
 * 3     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 2     10/09/09 2:28p Yyin
 * Added
 * 
 * 1     10/01/09 1:47p Yyin
 * Added
 * 
 */
package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecallJobDetailResType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "taxiCoID",
		  				  "taxiRideID",
		  				  "telephoneNr",
		  				  "telephoneExt",
		  				  "pickupStreetNr",
		  				  "pickupStreetName",
		  				  "pickupRegion",
		  				  "pickupUnit",
		  				  "pickupLandmark",
		  				  "pickupLon",
		  				  "pickupLat",
		  				  "dropoffStreetNr",
		  				  "dropoffStreetName",
		  				  "dropoffRegion",
		  				  "dropoffUnit",
		  				  "dropoffLandmark",
		  				  "dropoffLon",
		  				  "dropoffLat",
		  				  "passangerName",
		  				  "passangerNr",
		  				  "remarks",
		  				  "pickupTime",
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
						  "callBackTime",
						  "nbofCallBacks",
						  "pickupZoneNumber",
						  "dropoffZoneNumber",
						  "tripStatus",
						  "tripCreationTime",
						  "tripCancelledTime",
						  "tripModificationTime",
						  "crePerID",
						  "modPerID",
						  "cancelPerID",
						  "tripSeqNum",
						  "numTaxis",
						  "meterOnTime",
						  "meterOffTime",
						  "dispatchTime",
						  "noTripTime",
						  "onSiteTime",
						  "dispatchedCar",
						  "dispatchedDriver",
						  "carLongitude",
						  "carLatitude",
						  "carRapidFlag",
						  "tripRsvTime",
						  "email",
						  "fareAmount",
						  "fareCollected",
						  "errorCode",
						  "spExtraInfo1",
						  "jobExtraInfo1",
						  "jobExtraInfo2",
						  "jobExtraInfo3",
						  "jobExtraInfo4",
						  "jobExtraInfo5",
						  "carColour",
						  "carBrand",
						  "carRegPlate",
						  "etaInSec"
						  })
public class RecallJobDetailResponse extends BaseRes {

	@XmlElement(name="taxi_company_id", required = true )
	private Integer taxiCoID;
	
	@XmlElement(name="taxi_ride_id", required = false)
	private Long taxiRideID;
	
	@XmlElement(name="phonenum",   required = false)
	private String telephoneNr;

	@XmlElement(name="phoneext",   required = false)
	private String telephoneExt;

	@XmlElement(name="pickup_house_number",   required = false)
	private String pickupStreetNr;
	
	@XmlElement(name="pickup_street_name",   required = false)
	private String pickupStreetName;
	
	@XmlElement(name="pickup_unit",   required = false)
	private String pickupUnit;
	
	@XmlElement(name="pickup_district",  required = false)
	private String pickupRegion;
	
	@XmlElement(name="pickup_landmark",  required = false)
	private String pickupLandmark;
	
	@XmlElement(name="pickup_longitude", required = true)
	private Double pickupLon;
	
	@XmlElement(name="pickup_latitude",  required = true)
	private Double pickupLat;
	
	@XmlElement(name="dropoff_house_number",   required = false)
	private String dropoffStreetNr;
	
	@XmlElement(name="dropoff_street_name",   required = false)
	private String dropoffStreetName;
	
	@XmlElement(name="dropoff_district",   required = false)
	private String dropoffRegion;
	
	@XmlElement(name="dropoff_landmark",   required = false)
	private String dropoffLandmark;
	
	@XmlElement(name="dropoff_unit",   required = false)
	private String dropoffUnit;
	
	@XmlElement(name="dropoff_longitude",  required = false)
	private Double dropoffLon;
	
	@XmlElement(name="dropoff_latitude",   required = false)
	private Double dropoffLat;
	
	@XmlElement(name="passenger_name",   required = false)
	private String passangerName;
	
	@XmlElement(name="number_of_passenger",   required = false)
	private Integer passangerNr;
	
	@XmlElement(name="remarks",   required = false)
	private String remarks;
	
	@XmlElement(name="pickup_time",   required = false)
	private String pickupTime;
	
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
	
	@XmlElement(name="callBackTime", required = false, defaultValue="")
	private String callBackTime;
	
	@XmlElement(name="nofCallBacks", required = false)
	private Integer nbofCallBacks;
	
	@XmlElement(name="pickupZoneNumber", required = false, defaultValue="")
	private String pickupZoneNumber;
	
	@XmlElement(name="dropoffZoneNumber", required = false, defaultValue="")
	private String dropoffZoneNumber;
	
	@XmlElement(name="tripStatus", required = false, defaultValue="")
	private String tripStatus;
	
	@XmlElement(name="tripCreationTime", required = false, defaultValue="")
	private String tripCreationTime;
	
	@XmlElement(name="tripCancelledTime", required = false, defaultValue="")
	private String tripCancelledTime;
	
	@XmlElement(name="tripModificationTime", required = false, defaultValue="")
	private String tripModificationTime;
		
	@XmlElement(name="crePerID", required = false, defaultValue="")
	private String crePerID;
	
	@XmlElement(name="modPerID", required = false, defaultValue="")
	private String modPerID;
	
	@XmlElement(name="cancelPerID", required = false, defaultValue="")
	private String cancelPerID;
	
	@XmlElement(name="tripSeqNum", required = false, defaultValue="1")
	private Integer tripSeqNum;
	
	@XmlElement(name="numTaxis", required = false, defaultValue="1")
	private Integer numTaxis;
	
	@XmlElement(name="meterOnTime", required = false, defaultValue="")
	private String meterOnTime;

	@XmlElement(name="meterOffTime", required = false, defaultValue="")
	private String meterOffTime;
	
	@XmlElement(name="dispatchTime", required = false, defaultValue="")
	private String dispatchTime;
	
	@XmlElement(name="noTripTime", required = false, defaultValue="")
	private String noTripTime;
	
	@XmlElement(name="onSiteTime", required = false, defaultValue="")
	private String onSiteTime;
	
	@XmlElement(name="dispatchedCar", required = false, defaultValue="")
	private String dispatchedCar;
	
	@XmlElement(name="dispatchedDriver", required = false, defaultValue="")
	private String dispatchedDriver;
	
	@XmlElement(name="carLongitude", required = false)
	private Double carLongitude;

	@XmlElement(name="carLatitude",  required = false)
	private Double carLatitude;
	
	@XmlElement(name="carRapidFlag",  required = false)
	private String carRapidFlag;
	
	@XmlElement(name="tripRsvTime", required = false)
	private String tripRsvTime;
	
	@XmlElement(name="email", required = false)
	private String email;
	
	@XmlElement(name="fare_amount", required = false)
	private String fareAmount;
	
	@XmlElement(name="fare_collected", required = false)
	private String fareCollected;
	
	@XmlElement(name="errorCode",   required = false)
	private Integer errorCode;
	
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
	
	@XmlElement(name="carColour",   required = false, defaultValue="")
	private String carColour;
	
	@XmlElement(name="carBrand",   required = false, defaultValue="")
	private String carBrand;
	
	@XmlElement(name="carRegPlate",   required = false, defaultValue="")
	private String carRegPlate;
	
	@XmlElement(name="eta_in_sec",   required = false, defaultValue="")
	private String etaInSec;
	
	public Integer getTaxiCoID() {
		return taxiCoID;
	}

	public void setTaxiCoID(Integer taxiCoID) {
		this.taxiCoID = taxiCoID;
	}

	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}

	public String getTelephoneNr() {
		return telephoneNr;
	}

	public void setTelephoneNr(String telephoneNr) {
		this.telephoneNr = telephoneNr;
	}

	public String getTelephoneExt() {
		return telephoneExt;
	}

	public void setTelephoneExt(String telephoneExt) {
		this.telephoneExt = telephoneExt;
	}

	public String getPickupStreetNr() {
		return pickupStreetNr;
	}

	public void setPickupStreetNr(String pickupStreetNr) {
		this.pickupStreetNr = pickupStreetNr;
	}

	public String getPickupStreetName() {
		return pickupStreetName;
	}

	public void setPickupStreetName(String pickupStreetName) {
		this.pickupStreetName = pickupStreetName;
	}

	public String getPickupUnit() {
		return pickupUnit;
	}

	public void setPickupUnit(String pickupUnit) {
		this.pickupUnit = pickupUnit;
	}

	public String getPickupRegion() {
		return pickupRegion;
	}

	public void setPickupRegion(String pickupRegion) {
		this.pickupRegion = pickupRegion;
	}

	public String getPickupLandmark() {
		return pickupLandmark;
	}

	public void setPickupLandmark(String pickupLandmark) {
		this.pickupLandmark = pickupLandmark;
	}

	public Double getPickupLon() {
		return pickupLon;
	}

	public void setPickupLon(Double pickupLon) {
		this.pickupLon = pickupLon;
	}

	public Double getPickupLat() {
		return pickupLat;
	}

	public void setPickupLat(Double pickupLat) {
		this.pickupLat = pickupLat;
	}

	public String getDropoffStreetNr() {
		return dropoffStreetNr;
	}

	public void setDropoffStreetNr(String dropoffStreetNr) {
		this.dropoffStreetNr = dropoffStreetNr;
	}

	public String getDropoffStreetName() {
		return dropoffStreetName;
	}

	public void setDropoffStreetName(String dropoffStreetName) {
		this.dropoffStreetName = dropoffStreetName;
	}

	public String getDropoffRegion() {
		return dropoffRegion;
	}

	public void setDropoffRegion(String dropoffRegion) {
		this.dropoffRegion = dropoffRegion;
	}

	public String getDropoffLandmark() {
		return dropoffLandmark;
	}

	public void setDropoffLandmark(String dropoffLandmark) {
		this.dropoffLandmark = dropoffLandmark;
	}

	public String getDropoffUnit() {
		return dropoffUnit;
	}

	public void setDropoffUnit(String dropoffUnit) {
		this.dropoffUnit = dropoffUnit;
	}

	public Double getDropoffLon() {
		return dropoffLon;
	}

	public void setDropoffLon(Double dropoffLon) {
		this.dropoffLon = dropoffLon;
	}

	public Double getDropoffLat() {
		return dropoffLat;
	}

	public void setDropoffLat(Double dropoffLat) {
		this.dropoffLat = dropoffLat;
	}

	public String getPassangerName() {
		return passangerName;
	}

	public void setPassangerName(String passangerName) {
		this.passangerName = passangerName;
	}

	public Integer getPassangerNr() {
		return passangerNr;
	}

	public void setPassangerNr(Integer passangerNr) {
		this.passangerNr = passangerNr;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
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

	public String getPickupZoneNumber() {
		return pickupZoneNumber;
	}

	public void setPickupZoneNumber(String pickupZoneNumber) {
		this.pickupZoneNumber = pickupZoneNumber;
	}
	
	public String getDropoffZoneNumber() {
		return dropoffZoneNumber;
	}

	public void setDropoffZoneNumber(String dropoffZoneNumber) {
		this.dropoffZoneNumber = dropoffZoneNumber;
	}
	
	public String getTripStatus() {
		return tripStatus;
	}

	public void setTripStatus(String tripStatus) {
		this.tripStatus = tripStatus;
	}
	
	public String getTripCreationTime() {
		return tripCreationTime;
	}

	public void setTripCreationTime(String tripCreationTime) {
		this.tripCreationTime = tripCreationTime;
	}
	
	public String getTripCancelledTime() {
		return tripCancelledTime;
	}

	public void setTripCancelledTime(String tripCancelledTime) {
		this.tripCancelledTime = tripCancelledTime;
	}
	
	public String getCancelPerID() {
		return cancelPerID;
	}

	public void setCancelPerID(String cancelPerID) {
		this.cancelPerID = cancelPerID;
	}
	public String getCrePerID() {
		return crePerID;
	}

	public void setCrePerID(String crePerID) {
		this.crePerID = crePerID;
	}
	public String getModPerID() {
		return modPerID;
	}

	public void setModPerID(String modPerID) {
		this.modPerID = modPerID;
	}
	
	public String getTripModificationTime() {
		return tripModificationTime;
	}
	public void setTripModificationTime(String tripModificationTime) {
		this.tripModificationTime = tripModificationTime;
	}
	
	public Integer getTripSeqNum() {
		return tripSeqNum;
	}

	public void setTripSeqNum(Integer tripSeqNum) {
		this.tripSeqNum = tripSeqNum;
	}
	
	public Integer getNumTaxis() {
		return numTaxis;
	}

	public void setNumTaxis(Integer numTaxis) {
		this.numTaxis = numTaxis;
	}
	public String getMeterOnTime() {
		return meterOnTime;
	}

	public void setMeterOnTime(String meterOnTime) {
		this.meterOnTime = meterOnTime;
	}
	
	public String getMeterOffTime() {
		return meterOffTime;
	}

	public void setMeterOffTime(String meterOffTime) {
		this.meterOffTime = meterOffTime;
	}
	
	public String getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(String dispatchTime) {
		this.dispatchTime = dispatchTime;
	}
	
	public String getNoTripTime() {
		return noTripTime;
	}

	public void setNoTripTime(String noTripTime) {
		this.noTripTime = noTripTime;
	}
	
	public String getOnSiteTime() {
		return onSiteTime;
	}

	public void setOnSiteTime(String onSiteTime) {
		this.onSiteTime = onSiteTime;
	}
	
	public String getDispatchedCar() {
		return dispatchedCar;
	}

	public void setDispatchedCar(String dispatchedCar) {
		this.dispatchedCar = dispatchedCar;
	}
	
	public String getDispatchedDriver() {
		return dispatchedDriver;
	}

	public void setDispatchedDriver(String dispatchedDriver) {
		this.dispatchedDriver = dispatchedDriver;
	}
	
	public Double getCarLongitude() {
		return carLongitude;
	}

	public void setCarLongitude(Double carLongitude) {
		this.carLongitude = carLongitude;
	}
	
	public Double getCarLatitude() {
		return carLatitude;
	}

	public void setCarLatitude(Double carLatitude) {
		this.carLatitude = carLatitude;
	}
	
	public String getCarRapidFlag() {
		return carRapidFlag;
	}

	public void setCarRapidFlag(String carRapidFlag) {
		this.carRapidFlag = carRapidFlag;
	}
	
	public String getTripRsvTime() {
		return tripRsvTime;
	}

	public void setTripRsvTime(String tripRsvTime) {
		this.tripRsvTime = tripRsvTime;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getFareAmount() {
		return fareAmount;
	}

	public void setFareAmount(String fareAmount) {
		this.fareAmount = fareAmount;
	}

	public String getFareCollected() {
		return fareCollected;
	}

	public void setFareCollected(String fareCollected) {
		this.fareCollected = fareCollected;
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

	public String getCarColour() {
		return carColour;
	}

	public void setCarColour(String carColour) {
		this.carColour = carColour;
	}

	public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getCarRegPlate() {
		return carRegPlate;
	}

	public void setCarRegPlate(String carRegPlate) {
		this.carRegPlate = carRegPlate;
	}

	public String getEtaInSec() {
		return etaInSec;
	}

	public void setEtaInSec(String etaInSec) {
		this.etaInSec = etaInSec;
	}
	
}
