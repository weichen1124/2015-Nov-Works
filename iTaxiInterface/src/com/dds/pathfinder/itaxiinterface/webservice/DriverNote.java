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
 * File Name: DriverNote.java
 *
 * $Log $
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DriverNoteType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "driverNote" })
public class DriverNote {
	@XmlElement(name="driverNote", required = false)
	String driverNote;

	
	
	public DriverNote() {
		super();
	}
	
	

	public DriverNote(String driverNote) {
		super();
		this.driverNote = driverNote;
	}



	public String getDriverNote() {
		return driverNote;
	}

	public void setDriverNote(String driverNote) {
		this.driverNote = driverNote;
	}
}
