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
* $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/pfrest/events/AcceptProperties.java $
* 
* PF-16653, 07/17/15, DChen, add event type for TG.
* 
* PF-16572, 05/19/15, DChen, review notification message json format.
* 
* **************************************/
package com.dds.pathfinder.pfrest.events;

import com.fasterxml.jackson.annotation.JsonProperty;



public class AcceptProperties extends EventProperties {
	
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

		
		
		
		public AcceptProperties() {
			super();
		}

		
		
		public AcceptProperties(PFJobEvent jobEvent){
			super();
			if(jobEvent != null){
				this.jobID = jobEvent.getJobID();
				this.externalJobID = jobEvent.getExternalJobID();
				this.taxiCompanyID = jobEvent.getTaxiCompanyID();
				this.vehicleCallsign = jobEvent.getVehicleCallsign();
				this.nbSeats = jobEvent.getNbSeats();
			}
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
