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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/RegionListItem.java $
 * 
 * 5     9/20/10 11:34a Ezhang
 * Added districtFullName
 * 
 * 4     1/26/10 5:44p Dchen
 * OSP interface.
 * 
 * 3     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 2     8/14/09 4:21p Dchen
 * pathfinder iTaxi interface.
 * 
 * 1     8/13/09 5:47p Dchen
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
@XmlType(name = "RegionListItemType", namespace="http://com.dds.osp.itaxi.interface/", 
			propOrder = { "district","districtFullName","state","country"})
public class RegionListItem {
		
	@XmlElement(name="district", required = true)
	private String district;
	
	@XmlElement(name="district_full_name", required = true)
	private String districtFullName;
	
	@XmlElement(name="state", required = true)
	private String state;
	
	@XmlElement(name="country", required = true)
	private String country;

	
	public RegionListItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public RegionListItem(String district, String districFullName, String state, String country) {
		super();
		this.district = district;
		this.districtFullName = districFullName;
		this.state = state;
		this.country = country;
	}



	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}



	/**
	 * @param districtFullName the districtFullName to set
	 */
	public void setDistrictFullName(String districtFullName) {
		this.districtFullName = districtFullName;
	}


	/**
	 * @return the districtFullName
	 */
	public String getDistrictFullName() {
		return districtFullName;
	}
}
