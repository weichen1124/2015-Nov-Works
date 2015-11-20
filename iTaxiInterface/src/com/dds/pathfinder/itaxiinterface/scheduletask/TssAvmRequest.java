/****************************************************************************
 * 
 *					Copyright (c), 2014
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 * **************************************************************************
 * File Name: TssAvmRequest.java
 * 
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/scheduletask/TssAvmRequest.java $
 * 
 * 9/15/14, DChen, PF-16183, to create avm request.
 * 
 * 9/11/14, DChen, PF-16183, TSS shared rider AVM part.
 * 
 * ********************************/
package com.dds.pathfinder.itaxiinterface.scheduletask;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name="avm_req")
@XmlType(propOrder={"callsign","jobID","tssID","badgeNr","companyID","geoLatitude","geoLongitude","lastGPSDtm"})
public class TssAvmRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String callsign;
	private int jobID;
	private int tssID;
    private String badgeNr;
    private int companyID; 
    private double geoLatitude;
    private double geoLongitude;
    private String lastGPSDtm;
    private String xmlContent;
    
    
    public TssAvmRequest(){
    	super();
    }
    
	public TssAvmRequest(String callsign, int jobID, int tssID, String badgeNr,
			int companyID, double geoLatitude, double geoLongitude,
			String lastGPSDtm) {
		super();
		this.callsign = callsign;
		this.jobID = jobID;
		this.tssID = tssID;
		this.badgeNr = badgeNr;
		this.companyID = companyID;
		this.geoLatitude = geoLatitude;
		this.geoLongitude = geoLongitude;
		this.lastGPSDtm = lastGPSDtm;
	}
	
	@XmlElement(name = "callsign")
	public String getCallsign() {
		return callsign;
	}
	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}
	
	@XmlElement(name = "job_id")
	public int getJobID() {
		return jobID;
	}
	public void setJobID(int jobID) {
		this.jobID = jobID;
	}

	@XmlElement(name = "tss_id")
	public int getTssID() {
		return tssID;
	}
	public void setTssID(int tssID) {
		this.tssID = tssID;
	}
	
	@XmlElement(name = "badge_nr")
	public String getBadgeNr() {
		return badgeNr;
	}
	public void setBadgeNr(String badgeNr) {
		this.badgeNr = badgeNr;
	}
	
	@XmlElement(name = "company_id")
	public int getCompanyID() {
		return companyID;
	}
	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}
	
	@XmlElement(name = "geo_lat")
	public double getGeoLatitude() {
		return geoLatitude;
	}
	public void setGeoLatitude(double geoLatitude) {
		this.geoLatitude = geoLatitude;
	}
	
	@XmlElement(name = "geo_long")
	public double getGeoLongitude() {
		return geoLongitude;
	}
	public void setGeoLongitude(double geoLongitude) {
		this.geoLongitude = geoLongitude;
	}
	
	@XmlElement(name = "last_gps_dtm")
	public String getLastGPSDtm() {
		return lastGPSDtm;
	}
	public void setLastGPSDtm(String lastGPSDtm) {
		this.lastGPSDtm = lastGPSDtm;
	}
	
	@XmlTransient
	public String getXmlContent() {
		return xmlContent;
	}
	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}
    
    
}
