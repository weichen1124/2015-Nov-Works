/****************************************************************************
 *
 *                            Copyright (c), 2015
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 * 
 * Created for Mobile Application vehicle on the map
 * Aug 17, 2015 Y Yin
 * PF-16698. Added attributeIDList in the response.
 * 
 * PF-16496, 05/27/15, DChen, add pre dispatch eta.
 * 
 */
 
package com.dds.pathfinder.itaxiinterface.webservice;

import java.sql.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ezhang
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CarGPSListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "carNum",
						  "carLat",
						  "carLong",
						  "gpsTime",
						  "attributeIDList"
})

public class CarGPSListItem {
	
	@XmlElement(name="carNum", required = true)
	private long carNum;
	
	@XmlElement(name="carLat", required = true)
	private Double carLat;
	
	@XmlElement(name="carLong", required = true)
	private Double carLong;
	
	@XmlElement(name="gpsTime", required = true)
	private String gpsTime;
	
	
	@XmlElement(name="attributeIDList", required = false)
	private String attributeIDList;
	
	public CarGPSListItem(){
		super();
	}
	
	public CarGPSListItem(long carNum, Double carLat, Double carLong, String gpsTime, String attributeIDList){
		super();
		this.carNum = carNum;
		this.carLat = carLat;
		this.carLong = carLong;
		this.gpsTime = gpsTime;
		this.attributeIDList = attributeIDList;
	}

	public long getCarNum() {
		return carNum;
	}

	public void setCarNum(long carNum) {
		this.carNum = carNum;
	}

	public Double getCarLat() {
		return carLat;
	}

	public void setCarLat(Double carLat) {
		this.carLat = carLat;
	}

	public Double getCarLong() {
		return carLong;
	}

	public void setCarLong(Double carLong) {
		this.carLong = carLong;
	}

	public String getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(String gpsTime) {
		this.gpsTime = gpsTime;
	}
	
	
	public String getAttributeIDList() {
		return attributeIDList;
	}

	public void setAttributeIDList(String attributeIDList) {
		this.attributeIDList = attributeIDList;
	}

}
