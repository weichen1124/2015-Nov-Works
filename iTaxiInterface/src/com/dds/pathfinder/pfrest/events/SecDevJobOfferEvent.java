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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/SecDevJobOfferEvent.java $
* 
* PF-16607, 08/25/15, DChen, 2nd device bug fix.
* 
* PF-16606, 08/17/15, DChen, job offer event.
* 
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SecDevJobOfferEvent extends SecDeviceEvent {
	
	private static final long serialVersionUID = 1L;

	
	@JsonProperty(value = "typeLabel")
	private String jobTypeLabel; 
	
	@JsonProperty(value = "jobType")
	private String jobType; 
		
	@JsonProperty(value = "zoneLabel")
	private String pickupZoneLabel;
	
	@JsonProperty(value = "zone")
	private String pickupZone;
	
	
	@JsonProperty(value = "areaLabel")
	private String pickupAreaLabel; 
	
	@JsonProperty(value = "area")
	private String pickupArea; 
	
	@JsonProperty(value = "numberOfPassengerLabel")
	private String nbPassengerLabel;
	
	@JsonProperty(value = "numberOfPassenger")
	private int nbPassenger;
	
	@JsonProperty(value = "numberOfStopLabel")
	private String nbStopLabel;
	
	@JsonProperty(value = "numberOfStop")
	private int nbStop;
	
	@JsonProperty(value = "pickupAddrLabel")
	private String pickupAddressLabel;
	
	@JsonProperty(value = "pickupAddr")
	private String pickupAddress; 
	
	@JsonProperty(value = "dropoffAddrLabel")
	private String dropoffAddressLabel; 
	
	@JsonProperty(value = "dropoffAddr")
	private String dropoffAddress; 
	
	@JsonProperty(value = "paymentTypeLabel")
	private String paymentTypeLabel;
	
	@JsonProperty(value = "paymentType")
	private String paymentType;
	
	@JsonProperty(value = "layoutAmountLabel")
	private String layoutAmountLabel;
	
	@JsonProperty(value = "layoutAmount")
	private float layoutAmount;
	
	@JsonProperty(value = "fixedPriceLabel")
	private String fixedPriceLabel;
	
	@JsonProperty(value = "fixedPrice")
	private float fixedPrice;
	
	@JsonProperty(value = "autoRejectTimeout")
	private int autoRejectTimeout;
	
	@JsonProperty(value = "backInVehicleTimeout")
	private int backInVehicleTimeout;
	
	@JsonProperty(value = "streetHireButton")
	private boolean streetHireButton;	
	
	
	public SecDevJobOfferEvent(){
		super();
	}
	
	public SecDevJobOfferEvent(Long jobID, int taxiCompanyID, int driverID,
			String badgeNumber, String secDeviceID, String vehicleCallsign) {
		super(jobID, taxiCompanyID, driverID, badgeNumber, secDeviceID, vehicleCallsign);
	}
	
	public SecDevJobOfferEvent(SecDeviceEvent secEvent){
		super();
		if(secEvent != null){
			setDataType(secEvent.getDataType());
			setEventID(secEvent.getEventID());
			setJobID(secEvent.getJobID());
			setTaxiCompanyID(secEvent.getTaxiCompanyID());
			setDriverID(secEvent.getDriverID());
			setBadgeNumber(secEvent.getBadgeNumber());
			setSecDeviceID(secEvent.getSecDeviceID());
			setVehicleCallsign(secEvent.getVehicleCallsign());	
		}
	}


	public int getNbPassenger() {
		return nbPassenger;
	}


	public void setNbPassenger(int nbPassenger) {
		this.nbPassenger = nbPassenger;
	}


	public int getNbStop() {
		return nbStop;
	}


	public void setNbStop(int nbStop) {
		this.nbStop = nbStop;
	}


	public String getPickupAddress() {
		return pickupAddress;
	}


	public void setPickupAddress(String pickupAddress) {
		this.pickupAddress = pickupAddress;
	}


	public String getDropoffAddress() {
		return dropoffAddress;
	}


	public void setDropoffAddress(String dropoffAddress) {
		this.dropoffAddress = dropoffAddress;
	}


	public String getPaymentType() {
		return paymentType;
	}


	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getJobType() {
		return jobType;
	}


	public void setJobType(String jobType) {
		this.jobType = jobType;
	}


	public String getPickupZone() {
		return pickupZone;
	}


	public void setPickupZone(String pickupZone) {
		this.pickupZone = pickupZone;
	}


	public String getPickupArea() {
		return pickupArea;
	}


	public void setPickupArea(String pickupArea) {
		this.pickupArea = pickupArea;
	}

	public String getJobTypeLabel() {
		return jobTypeLabel;
	}

	public void setJobTypeLabel(String jobTypeLabel) {
		this.jobTypeLabel = jobTypeLabel;
	}

	public String getPickupZoneLabel() {
		return pickupZoneLabel;
	}

	public void setPickupZoneLabel(String pickupZoneLabel) {
		this.pickupZoneLabel = pickupZoneLabel;
	}

	public String getPickupAreaLabel() {
		return pickupAreaLabel;
	}

	public void setPickupAreaLabel(String pickupAreaLabel) {
		this.pickupAreaLabel = pickupAreaLabel;
	}

	public String getNbPassengerLabel() {
		return nbPassengerLabel;
	}

	public void setNbPassengerLabel(String nbPassengerLabel) {
		this.nbPassengerLabel = nbPassengerLabel;
	}

	public String getNbStopLabel() {
		return nbStopLabel;
	}

	public void setNbStopLabel(String nbStopLabel) {
		this.nbStopLabel = nbStopLabel;
	}

	public String getPickupAddressLabel() {
		return pickupAddressLabel;
	}

	public void setPickupAddressLabel(String pickupAddressLabel) {
		this.pickupAddressLabel = pickupAddressLabel;
	}

	public String getDropoffAddressLabel() {
		return dropoffAddressLabel;
	}

	public void setDropoffAddressLabel(String dropoffAddressLabel) {
		this.dropoffAddressLabel = dropoffAddressLabel;
	}

	public String getPaymentTypeLabel() {
		return paymentTypeLabel;
	}

	public void setPaymentTypeLabel(String paymentTypeLabel) {
		this.paymentTypeLabel = paymentTypeLabel;
	}

	public String getLayoutAmountLabel() {
		return layoutAmountLabel;
	}

	public void setLayoutAmountLabel(String layoutAmountLabel) {
		this.layoutAmountLabel = layoutAmountLabel;
	}

	public float getLayoutAmount() {
		return layoutAmount;
	}

	public void setLayoutAmount(float layoutAmount) {
		this.layoutAmount = layoutAmount;
	}

	public String getFixedPriceLabel() {
		return fixedPriceLabel;
	}

	public void setFixedPriceLabel(String fixedPriceLabel) {
		this.fixedPriceLabel = fixedPriceLabel;
	}

	public float getFixedPrice() {
		return fixedPrice;
	}

	public void setFixedPrice(float fixedPrice) {
		this.fixedPrice = fixedPrice;
	}

	public int getAutoRejectTimeout() {
		return autoRejectTimeout;
	}

	public void setAutoRejectTimeout(int autoRejectTimeout) {
		this.autoRejectTimeout = autoRejectTimeout;
	}

	public int getBackInVehicleTimeout() {
		return backInVehicleTimeout;
	}

	public void setBackInVehicleTimeout(int backInVehicleTimeout) {
		this.backInVehicleTimeout = backInVehicleTimeout;
	}

	public boolean isStreetHireButton() {
		return streetHireButton;
	}

	public void setStreetHireButton(boolean streetHireButton) {
		this.streetHireButton = streetHireButton;
	}
	
}
