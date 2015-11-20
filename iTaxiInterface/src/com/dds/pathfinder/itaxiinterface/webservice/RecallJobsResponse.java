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
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/RecallJobsResponse.java $
 * 
 * 3     9/20/10 1:55p Ezhang
 * OSP 2.0 added errorCode.
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecallJobsResType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { 
						  "nofJobs",
						  "jobList",
						  "errorCode"
})
public class RecallJobsResponse extends BaseRes{
	
	@XmlElement(name="nofJobs", required = true )
	private Integer nofJobs;
	
	@XmlElement(name="listOfJobs", required = true )
	private JobListItem[] jobList;
	
	@XmlElement(name="errorCode",   required = false)
	private Integer errorCode;
	
	public Integer getNofJobs() {
		return nofJobs;
	}
	
	public void setNofJobs(Integer nofJobs) {
		this.nofJobs = nofJobs;
	}
	
	public JobListItem[] getJobList() {
		return jobList;
	}

	public void setJobList(JobListItem[] jobList) {
		this.jobList = jobList;
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
}
