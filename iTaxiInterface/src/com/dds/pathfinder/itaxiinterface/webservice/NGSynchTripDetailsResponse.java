/****************************************************************************
 *
 *                            Copyright (c), 2009
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/NGSynchTripDetailsResponse.java $
 * 
 * 1     9/14/10 2:41p Ajiang
 * C34078 - Texas Taxi Enhancements
 *
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NGSynchTripDetailsResType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { 
						  "nofJobs",
						  "jobList"
						})
public class NGSynchTripDetailsResponse extends BaseRes{
	
	@XmlElement(name="nofJobs", required = true )
	private Integer nofJobs;
	
	@XmlElement(name="listOfJobs", required = true )
	private NGSynchTripListItem[] jobList;
	
	public Integer getNofJobs() {
		return nofJobs;
	}
	
	public void setNofJobs(Integer nofJobs) {
		this.nofJobs = nofJobs;
	}
	
	public NGSynchTripListItem[] getJobList() {
		return jobList;
	}

	public void setJobList(NGSynchTripListItem[] jobList) {
		this.jobList = jobList;
	}
}
