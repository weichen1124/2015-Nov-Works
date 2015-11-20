/****************************************************************************
 *
 *		   		    Copyright (c), 2010
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/JobListItem.java $
 * 
 * 2     11/29/10 10:23a Ezhang
 * 
 * Helper class used by UpdateOrderByConfNumImplement.java
 * ******/
package com.dds.pathfinder.itaxiinterface.osp.impl;

/**
 * @author ezhang
 *
 */
public class JobListItem {
	private Long jobId;
	private String jobStatus;
	private String vehicleStatus;
	
	
	public JobListItem(){
		super();
	}
	public JobListItem(Long jobId, String jobStatus, String vehicleStatus){
		super();
		this.jobId = jobId;
		this.jobStatus = jobStatus;
		this.vehicleStatus = vehicleStatus;
	}
	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	/**
	 * @return the jobId
	 */
	public Long getJobId() {
		return jobId;
	}
	/**
	 * @param jobStatus the jobStatus to set
	 */
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	/**
	 * @return the jobStatus
	 */
	public String getJobStatus() {
		return jobStatus;
	}
	/**
	 * @param vehicleStatus the vehicleStatus to set
	 */
	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}
	/**
	 * @return the vehicleStatus
	 */
	public String getVehicleStatus() {
		return vehicleStatus;
	}
	
	
	
}
