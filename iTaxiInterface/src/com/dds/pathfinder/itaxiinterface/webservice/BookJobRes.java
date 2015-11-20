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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/BookJobRes.java $
 * 
 * 6     9/20/10 11:32a Ezhang
 * 
 * added confirmationNum, fareEstimate, eta1, eta2, errorCode fields to the request
 * 5     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 4     9/25/09 5:43p Dchen
 * pathfinder iTaxi interface.
 * 
 * 3     8/14/09 4:21p Dchen
 * pathfinder iTaxi interface.
 * 
 * 2     8/13/09 5:47p Dchen
 * pathfinder iTaxi interface.
 * 
 * 1     8/13/09 12:04p Dchen
 * pathfinder iTaxi interface.
 * 
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BookJobResType", namespace="http://com.dds.osp.itaxi.interface/", 
		propOrder = { "taxiRideID","nbOfJobs","taxiJobIDList","confirmationNum","fareEstimate", "eta1", "eta2", "errorCode"})
public class BookJobRes extends BaseRes{
		
	@XmlElement(name="taxi_ride_id",   required = true)
	private Long taxiRideID;
	
	@XmlElement(name="nofJobs",   required = false, defaultValue="0")
	private Integer nbOfJobs;
	
	@XmlElement(name="taxiJobIDList",   required = false)
	private TaxiJob[] taxiJobIDList;
	
	@XmlElement(name="confirmationNum",   required = false)
	private String confirmationNum;
	
	
	@XmlElement(name="fareEstimate",   required = false)
	private Long fareEstimate;
	
	@XmlElement(name="eta1",   required = false)
	private String eta1;
	
	@XmlElement(name="eta2",   required = false)
	private String eta2;
	
	@XmlElement(name="errorCode",   required = false)
	private Integer errorCode;
	
	public Long getTaxiRideID() {
		return taxiRideID;
	}

	public void setTaxiRideID(Long taxiRideID) {
		this.taxiRideID = taxiRideID;
	}

	public Integer getNbOfJobs() {
		return nbOfJobs;
	}

	public void setNbOfJobs(Integer nbOfJobs) {
		this.nbOfJobs = nbOfJobs;
	}

	public TaxiJob[] getTaxiJobIDList() {
		return taxiJobIDList;
	}

	public void setTaxiJobIDList(TaxiJob[] taxiJobIDList) {
		this.taxiJobIDList = taxiJobIDList;
	}
		
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getConfirmationNum() {
		return confirmationNum;
	}

	public void setConfirmationNum(String confirmationNum) {
		this.confirmationNum = confirmationNum;
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
