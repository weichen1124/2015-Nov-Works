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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/AcceptEvent.java $
* 
* PF-16653, 07/23/15, DChen, remove properties from pf events.
* 
* PF-16572, 05/19/15, DChen, review notification message json format.
* 
* PF-16428, 03/25/15, DChen, pfrest job events service.
* 
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AcceptEvent extends PFRestEvent {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "job-id")
	private Long jobID;
	
	@JsonProperty(value = "external-job-id")
	private String externalJobID;
	
	@JsonProperty(value = "taxi-company-id")
	private int taxiCompanyID;
	
	@JsonProperty(value = "vehicle")
	private String vehicleCallsign;
	
	@JsonProperty(value = "seats")
	private int nbSeats;
		
	@JsonProperty(value = "attributes")
	private String[] jobAttributes;
	
	public AcceptEvent(){
		super();
		setDataType(PFRestEventType.PFEvent_Type_Accept.getEventType());
		// setDataID("" + PFRestEventType.PFEvent_Type_Accept.getEventID());
	}
	
	public AcceptEvent(PFJobEvent jobEvent){
		super();
		setDataType(PFRestEventType.PFEvent_Type_Accept.getEventType());
		if(jobEvent != null){
			this.jobID = jobEvent.getJobID();
			this.externalJobID = jobEvent.getExternalJobID();
			this.taxiCompanyID = jobEvent.getTaxiCompanyID();
			this.vehicleCallsign = jobEvent.getVehicleCallsign();
			this.nbSeats = jobEvent.getNbSeats();
		}
		// setProperties(new AcceptProperties(jobEvent));
	}

	public Long getJobID() {
		return jobID;
	}

	public void setJobID(Long jobID) {
		this.jobID = jobID;
	}

	public String getExternalJobID() {
		return externalJobID;
	}

	public void setExternalJobID(String externalJobID) {
		this.externalJobID = externalJobID;
	}

	public int getTaxiCompanyID() {
		return taxiCompanyID;
	}

	public void setTaxiCompanyID(int taxiCompanyID) {
		this.taxiCompanyID = taxiCompanyID;
	}

	public String getVehicleCallsign() {
		return vehicleCallsign;
	}

	public void setVehicleCallsign(String vehicleCallsign) {
		this.vehicleCallsign = vehicleCallsign;
	}

	public int getNbSeats() {
		return nbSeats;
	}

	public void setNbSeats(int nbSeats) {
		this.nbSeats = nbSeats;
	}

	public String[] getJobAttributes() {
		return jobAttributes;
	}

	public void setJobAttributes(String[] jobAttributes) {
		this.jobAttributes = jobAttributes;
	}

}
