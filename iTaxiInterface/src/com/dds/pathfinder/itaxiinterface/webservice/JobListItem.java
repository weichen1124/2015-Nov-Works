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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/JobListItem.java $
 * 
 * PF-15827, 15/07/14 DChen, OSP added redispatched related job ID return.
 * 
 * 12    11/20/13 10:57a Ezhang
 * PF-15582 Recall job by joblist added <jobEndDateTime> in response
 * for token payment after job completed.
 * 
 * 11    10/23/13 3:05p Ezhang
 * PF-15615 Added detailTripStatusUniformCode.
 * 
 * 10    10/04/10 10:48a Ezhang
 * OSP 2.0 added confirmation number, pickup and dropoff longitude and latitude.
 * 
 * 9     9/20/10 1:56p Ezhang
 * OSP 2.0 added eta1 etc.
 * 
 * 8     2/09/10 11:20a Jwong
 * 
 * 7     1/22/10 4:52p Dchen
 * OSP interface.
 * 
 * 6     1/19/10 6:40p Jwong
 * Added dispatchedCar, dispatchedDriver, carLongitude, and carLatitude.
 * 
 * 5     11/20/09 12:22p Yyin
 * Added tripStatus and adviseArrival
 * 
 * 4     11/17/09 2:29p Jwong
 * 
 * 3     11/16/09 11:13a Jwong
 * 
 * 2     10/19/09 2:22p Jwong
 * 
 * 1     10/14/09 6:02p Jwong
 * 
 * 
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JobListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "passengerName",
						  "phoneNumber",
						  "phoneExtension",
						  "pickupStreetNumber",
						  "pickupStreetName",
						  "pickupRegion",
						  "pickupUnitNumber",
						  "pickupLandmark",
						  "pickupLon",
		  				  "pickupLat",
						  "dropoffStreetNumber",
						  "dropoffStreetName",
						  "dropoffRegion",
						  "dropoffUnitNumber",
						  "dropoffLandmark",
						  "dropoffLon",
		  				  "dropoffLat",
						  "accountCode",
						  "accountName",
						  "pickupDate",
						  "pickupTime",
						  "repPickupTime",
						  "repPickupDays",
						  "repStart",
						  "repEnd",
						  "bookingDateTime",
						  "taxiRideID",
						  "tripStatus",
						  "adviseArrival",
						  "dispatchedDriver",
						  "dispatchedCar",
						  "carLongitude",
						  "carLatitude",
						  "tripStatusUniformCode",
						  "eta1",
						  "eta2",
						  "estimateDistance",
						  "confirmationNum",
						  "detailTripStatusUniformCode",
						  "jobEndDateTime",
						  "rdspRelatedJobID",
						  "spExtraInfo1",
						  "jobExtraInfo1",
						  "jobExtraInfo2",
						  "jobExtraInfo3",
						  "jobExtraInfo4",
						  "jobExtraInfo5"	
						  })
public class JobListItem {
		
	@XmlElement(name="passengerName", required = false)
	private String passengerName;
	
	@XmlElement(name="phoneNumber", required = false)
	private String phoneNumber;
	
	@XmlElement(name="phoneExtension", required = false)
	private String phoneExtension;
	
	@XmlElement(name="pickupStreetNumber", required = false)
	private String pickupStreetNumber;

	@XmlElement(name="pickupStreetName", required = false)
	private String pickupStreetName;
	
	@XmlElement(name="pickupRegion", required = false)
	private String pickupRegion;
	
	@XmlElement(name="pickupUnitNumber", required = false)
	private String pickupUnitNumber;	
	
	@XmlElement(name="pickupLandmark", required = false)
	private String pickupLandmark;	
	
	@XmlElement(name="pickup_longitude", required = true)
	private Double pickupLon;
	
	@XmlElement(name="pickup_latitude",  required = true)
	private Double pickupLat;
	
	@XmlElement(name="dropoffStreetNumber", required = false)
	private String dropoffStreetNumber;

	@XmlElement(name="dropoffStreetName", required = false)
	private String dropoffStreetName;
	
	@XmlElement(name="dropoffRegion", required = false)
	private String dropoffRegion;
	
	@XmlElement(name="dropoffUnitNumber", required = false)
	private String dropoffUnitNumber;	
	
	@XmlElement(name="dropoffLandmark", required = false)
	private String dropoffLandmark;	
	
	@XmlElement(name="dropoff_longitude",  required = false)
	private Double dropoffLon;
	
	@XmlElement(name="dropoff_latitude",   required = false)
	private Double dropoffLat;
	
	@XmlElement(name="accountCode", required = false)
	private String accountCode;	
	
	@XmlElement(name="accountName", required = false)
	private String accountName;	
	
	@XmlElement(name="pickupDate", required = false)
	private String pickupDate;		

	@XmlElement(name="pickupTime", required = false)
	private String pickupTime;		
	
	@XmlElement(name="repPickupTime", required = false)
	private String repPickupTime;		
	
	@XmlElement(name="repPickupDays", required = false)
	private String repPickupDays;	
	
	@XmlElement(name="repStart", required = false)
	private String repStart;

	@XmlElement(name="repEnd", required = false)
	private String repEnd;

	@XmlElement(name="bookingDateTime", required = false)
	private String bookingDateTime;
	
	@XmlElement(name="taxiRideID", required = false)
	private Long taxiRideID;
	
	@XmlElement(name="tripStatus", required = false)
	private String tripStatus;
	
	@XmlElement(name="adviseArrival", required = false)
	private String adviseArrival;
	
	@XmlElement(name="dispatchedCar", required = false)
	private String dispatchedCar;
	
	@XmlElement(name="dispatchedDriver", required = false)
	private String dispatchedDriver;
	
	@XmlElement(name="carLongitude", required = true)
	private Double carLongitude;
	
	@XmlElement(name="carLatitude", required = true)
	private Double carLatitude;
	
	@XmlElement(name="tripStatusUniformCode", required = false)
	private Integer tripStatusUniformCode;
	
	@XmlElement(name="eta1",   required = false)
	private String eta1;
	
	@XmlElement(name="eta2",   required = false)
	private String eta2;
	
	@XmlElement(name="estimateDistance",   required = false)
	private Long estimateDistance;
	
	@XmlElement(name="confirmationNum", required = false)
	private String confirmationNum;
	
	@XmlElement(name="detailTripStatusUniformCode", required = false)
	private Integer detailTripStatusUniformCode;
	
	@XmlElement(name="jobEndDateTime", required = false)
	private String jobEndDateTime;
	
	@XmlElement(name="redispatchJobID", required = false)
	private String rdspRelatedJobID;
	
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

	public String getPhoneExtension() {
		return phoneExtension;
	}

	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
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
	
	public String getPickupRegion() {
		return pickupRegion;
	}

	public void setPickupRegion(String pickupRegion) {
		this.pickupRegion = pickupRegion;
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
	
	public String getDropoffStreetNumber() {
		return dropoffStreetNumber;
	}

	public void setDropoffStreetNumber(String dropoffStreetNumber) {
		this.dropoffStreetNumber = dropoffStreetNumber;
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
	
	public String getDropoffUnitNumber() {
		return dropoffUnitNumber;
	}

	public void setDropoffUnitNumber(String dropoffUnitNumber) {
		this.dropoffUnitNumber = dropoffUnitNumber;
	}
	
	public String getDropoffLandmark() {
		return dropoffLandmark;
	}

	public void setDropoffLandmark(String dropoffLandmark) {
		this.dropoffLandmark = dropoffLandmark;
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
	
	public String getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}	
	
	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}	
	
	public String getRepPickupTime() {
		return repPickupTime;
	}

	public void setRepPickupTime(String repPickupTime) {
		this.repPickupTime = repPickupTime;
	}	
	
	public String getRepPickupDays() {
		return repPickupDays;
	}

	public void setRepPickupDays(String repPickupDays) {
		this.repPickupDays = repPickupDays;
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
	
	public String getBookingDateTime() {
		return bookingDateTime;
	}

	public void setBookingDateTime(String bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
	}	
	
	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}	
	
	public String getTripStatus() {
		return tripStatus;
	}

	public void setTripStatus(String tripStatus) {
		this.tripStatus = tripStatus;
	}
	
	public String getAdviseArrival() {
		return adviseArrival;
	}

	public void setAdviseArrival(String adviseArrival) {
		this.adviseArrival = adviseArrival;
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
	
	public String getEta1() {
		return eta1;
	}

	public void setEta1(String eta1) {
		this.eta1 = eta1;
	}
	
	public String getEta2() {
		return eta2;
	}

	public void setEta2(String eta2) {
		this.eta2 = eta2;
	}
	
	public Integer getTripStatusUniformCode() {
		return tripStatusUniformCode;
	}
	
	public void setTripStatusUniformCode(Integer tripStatusUniformCode) {
		this.tripStatusUniformCode = tripStatusUniformCode;
	}
	
	public Long getEstimateDistance() {
		return estimateDistance;
	}
	
	public void setEstimateDistance(Long estimateDistance) {
		this.estimateDistance = estimateDistance;
	}

	public void setConfirmationNum(String confirmationNum) {
		this.confirmationNum = confirmationNum;
	}

	public String getConfirmationNum() {
		return confirmationNum;
	}

	/**
	 * @param pickupLon the pickupLon to set
	 */
	public void setPickupLon(Double pickupLon) {
		this.pickupLon = pickupLon;
	}

	/**
	 * @return the pickupLon
	 */
	public Double getPickupLon() {
		return pickupLon;
	}

	/**
	 * @param pickupLat the pickupLat to set
	 */
	public void setPickupLat(Double pickupLat) {
		this.pickupLat = pickupLat;
	}

	/**
	 * @return the pickupLat
	 */
	public Double getPickupLat() {
		return pickupLat;
	}

	/**
	 * @param dropoffLon the dropoffLon to set
	 */
	public void setDropoffLon(Double dropoffLon) {
		this.dropoffLon = dropoffLon;
	}

	/**
	 * @return the dropoffLon
	 */
	public Double getDropoffLon() {
		return dropoffLon;
	}

	/**
	 * @param dropoffLat the dropoffLat to set
	 */
	public void setDropoffLat(Double dropoffLat) {
		this.dropoffLat = dropoffLat;
	}

	/**
	 * @return the dropoffLat
	 */
	public Double getDropoffLat() {
		return dropoffLat;
	}

	public Integer getDetailTripStatusUniformCode() {
		return detailTripStatusUniformCode;
	}

	public void setDetailTripStatusUniformCode(
			Integer detailTripStatusUniformCode) {
		this.detailTripStatusUniformCode = detailTripStatusUniformCode;
	}

	public String getJobEndDateTime() {
		return jobEndDateTime;
	}

	public void setJobEndDateTime(String jobEndDateTime) {
		this.jobEndDateTime = jobEndDateTime;
	}

	public String getRdspRelatedJobID() {
		return rdspRelatedJobID;
	}

	public void setRdspRelatedJobID(String rdspRelatedJobID) {
		this.rdspRelatedJobID = rdspRelatedJobID;
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
	
	
}