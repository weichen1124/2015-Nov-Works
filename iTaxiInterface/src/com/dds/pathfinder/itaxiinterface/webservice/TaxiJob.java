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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/TaxiJob.java $
 * 
 * 4     9/20/10 11:35a Ezhang
 * Added FareEstimate, eta1 and eta2 for OSP 2.0
 * 
 * 3     2/08/10 4:23p Dchen
 * OSP interface.
 * 
 * 2     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 1     9/25/09 5:45p Dchen
 * pathfinder iTaxi interface.
 * 
 * 
 *****/

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxiJobType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "taxiRideID",
						  "fareEstimate",
						  "eta1",
						  "eta2"})
public class TaxiJob {
	@XmlElement(name="taxi_ride_id", required = false)
	Long taxiRideID;
	
	@XmlElement(name="fareEstimate",   required = false)
	private Long fareEstimate;
	
	@XmlElement(name="eta1",   required = false)
	private String eta1;
	
	@XmlElement(name="eta2",   required = false)
	private String eta2;
	
	public TaxiJob() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public TaxiJob(Long taxiRideID) {
		super();
		this.taxiRideID = taxiRideID;
	}

	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}
	
	public Long getFareEstimate() {
		return fareEstimate;
	}

	public void setFareEstimate(Long fareEstimate) {
		this.fareEstimate = fareEstimate;
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
}
