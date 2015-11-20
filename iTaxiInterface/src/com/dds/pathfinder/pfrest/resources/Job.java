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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/resources/Job.java $
 * 
 * PF-16385, 04/02/15, DChen, add tariff number to job.
 * 
 * PF-16428, 03/06/15, DChen, create pfrest notification service.
 * 
 * PF-16385, 02/13/15, DChen, create pfrest project.
 * 
 * **************************************/
package com.dds.pathfinder.pfrest.resources;


import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonRootName(value = "job")
public class Job extends PFResourceData {

	private static final long serialVersionUID = 1L;
	
//	@JsonProperty(value = "job-id")
//	private Long jobID;
	
	@JsonProperty(value = "external-job-id")
	private String externalJobID;
	
	@JsonProperty(value = "taxi-company-id")
	private int taxiCompanyID;
	
	@JsonProperty(value = "sub-route")
	private SubRoute subRoute;
	
	@JsonProperty(value = "job-attributes")
	private String[] jobAttributes;
		
	@JsonProperty(value = "pickup-time")
	private String pickupTime;		
	

	@JsonProperty(value = "booking-time")
	private String bookingDateTime;
	
	
	@JsonProperty(value = "trip-status")
	private String tripStatus;
	
	@JsonProperty(value = "advise-arrival")
	private String adviseArrival;
	
	@JsonProperty(value = "dispatched-car")
	private String dispatchedCar;
	
	@JsonProperty(value = "dispatched-driver")
	private String dispatchedDriver;
	
	@JsonProperty(value = "car-longitude")
	private Double carLongitude;
	
	@JsonProperty(value = "car-latitude")
	private Double carLatitude;
	
	
	@JsonProperty(value = "eta-1")
	private String eta1;
	
	@JsonProperty(value = "eta-2")
	private String eta2;
	
	@JsonProperty(value = "estimated-distance")
	private Long estimateDistance;
	
	@JsonProperty(value = "confirmation-number")
	private String confirmationNum;
	
	@JsonProperty(value = "detail-trip-uniform-code")
	private Integer detailTripStatusUniformCode;
	
	@JsonProperty(value = "job-end-time")
	private String jobEndDateTime;
	
	@JsonProperty(value = "rdsp-related-job-id")
	private String rdspRelatedJobID;
	
	@JsonProperty(value = "sp-extra-inf")
	private String spExtraInfo1;
	
	@JsonProperty(value = "job-extra-info")
	private String[] jobExtraInfo;
	
	@JsonProperty(value = "operator-notes")
	private String operatorNotes;
	
	@JsonProperty(value = "tariff-number")
	private String tariffNumber;
	
	public Job(){
		super();
		setDataType(PFDataType.PFData_Type_Jobs.getDataType());
		subRoute = new SubRoute();	
	}
	
//	@GET
//	@Produces("application/json")
//	public Job get(){
//		return this;
//	}
	
//	public Long getJobID() {
//		return jobID;
//	}
//
//	public void setJobID(Long jobID) {
//		this.jobID = jobID;
//	}
	

	public int getTaxiCompanyID() {
		return taxiCompanyID;
	}

	public void setTaxiCompanyID(int taxiCompanyID) {
		this.taxiCompanyID = taxiCompanyID;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getBookingDateTime() {
		return bookingDateTime;
	}

	public void setBookingDateTime(String bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
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

	public Long getEstimateDistance() {
		return estimateDistance;
	}

	public void setEstimateDistance(Long estimateDistance) {
		this.estimateDistance = estimateDistance;
	}

	public String getConfirmationNum() {
		return confirmationNum;
	}

	public void setConfirmationNum(String confirmationNum) {
		this.confirmationNum = confirmationNum;
	}

	public Integer getDetailTripStatusUniformCode() {
		return detailTripStatusUniformCode;
	}

	public void setDetailTripStatusUniformCode(Integer detailTripStatusUniformCode) {
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

	public String[] getJobExtraInfo() {
		return jobExtraInfo;
	}

	public void setJobExtraInfo(String[] jobExtraInfo) {
		this.jobExtraInfo = jobExtraInfo;
	}

	public SubRoute getSubRoute() {
		return subRoute;
	}

	public void setSubRoute(SubRoute subRoute) {
		this.subRoute = subRoute;
	}

	

	public String[] getJobAttributes() {
		return jobAttributes;
	}

	public void setJobAttributes(String[] jobAttributes) {
		this.jobAttributes = jobAttributes;
	}

	public String getExternalJobID() {
		return externalJobID;
	}

	public void setExternalJobID(String externalJobID) {
		this.externalJobID = externalJobID;
	}

	public String getOperatorNotes() {
		return operatorNotes;
	}

	public void setOperatorNotes(String operatorNotes) {
		this.operatorNotes = operatorNotes;
	}

	public String getTariffNumber() {
		return tariffNumber;
	}

	public void setTariffNumber(String tariffNumber) {
		this.tariffNumber = tariffNumber;
	}
	
	
	
}
