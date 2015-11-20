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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/NGSynchTripListItem.java $
 * 
 * 1     9/14/10 2:41p Ajiang
 * C34078 - Texas Taxi Enhancements
 * 
 * 
 * 
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NGSynchTripListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
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
						  "email"})
public class NGSynchTripListItem {
		

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
	
	@XmlElement(name="accountCode", required = false)
	private String accountCode;
	
	@XmlElement(name="accountName", required = false)
	private String accountName;
	
	@XmlElement(name="authNum", required = false)
	private String authNum;
	
	@XmlElement(name="adviseArrival", required = false)
	private String adviseArrival;
	
	@XmlElement(name="type", required = false)
	private String type;
	
	@XmlElement(name="priority", required = false)
	private String priority;
	
	@XmlElement(name="priorityReason", required = false)
	private String priorityReason;
	
	@XmlElement(name="cabNum", required = false)
	private String cabNum;
	
	@XmlElement(name="flatRate", required = false)
	private String flatRate;
	
	@XmlElement(name="attributelist", required = false)
	private Attribute[] attributeList;

	@XmlElement(name="rep_pickup_time", required = false)
	private String repPickupTime;
	
	@XmlElement(name="repSuspendStart", required = false)
	private String repSuspendStart;
	
	@XmlElement(name="repSuspendEnd", required = false)
	private String repSuspendEnd;
	
	@XmlElement(name="repStart", required = false)
	private String repStart;
	
	@XmlElement(name="repEnd", required = false)
	private String repEnd;
	
	@XmlElement(name="HolidayList", required = false)
	private String holidayList;
	
	@XmlElement(name="callBackTime", required = false)
	private String callBackTime;
	
	@XmlElement(name="nofCallBacks", required = false)
	private Integer nbofCallBacks;
	
	@XmlElement(name="pickupZoneNumber", required = false)
	private String pickupZoneNumber;
	
	@XmlElement(name="dropoffZoneNumber", required = false)
	private String dropoffZoneNumber;
	
	@XmlElement(name="tripStatus", required = false)
	private String tripStatus;
	
	@XmlElement(name="tripCreationTime", required = false)
	private String tripCreationTime;
	
	@XmlElement(name="tripCancelledTime", required = false)
	private String tripCancelledTime;
	
	@XmlElement(name="tripModificationTime", required = false)
	private String tripModificationTime;
		
	@XmlElement(name="crePerID", required = false)
	private String crePerID;
	
	@XmlElement(name="modPerID", required = false)
	private String modPerID;
	
	@XmlElement(name="cancelPerID", required = false)
	private String cancelPerID;
	
	@XmlElement(name="tripSeqNum", required = false)
	private Integer tripSeqNum;
	
	@XmlElement(name="numTaxis", required = false)
	private Integer numTaxis;
	
	@XmlElement(name="meterOnTime", required = false)
	private String meterOnTime;

	@XmlElement(name="meterOffTime", required = false)
	private String meterOffTime;
	
	@XmlElement(name="dispatchTime", required = false)
	private String dispatchTime;
	
	@XmlElement(name="noTripTime", required = false)
	private String noTripTime;
	
	@XmlElement(name="onSiteTime", required = false)
	private String onSiteTime;
	
	@XmlElement(name="dispatchedCar", required = false)
	private String dispatchedCar;
	
	@XmlElement(name="dispatchedDriver", required = false)
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

}