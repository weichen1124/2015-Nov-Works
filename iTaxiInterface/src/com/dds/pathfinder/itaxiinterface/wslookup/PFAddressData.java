
package com.dds.pathfinder.itaxiinterface.wslookup;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PFAddressData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PFAddressData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddressIDBuilding" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="AddressIDUnit" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="AreaID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="AreaName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BlockFaceID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="BuildingID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="HighBlockFaceNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="HighLatitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="HighLongitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="LandmarkName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Latitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="Longitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="LowBlockFaceNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="LowLatitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="LowLongitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="OrganizationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PostCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RegionAbbreviation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RegionID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="StreetName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StreetNameID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="StreetNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TaxiCoID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="UnitNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PFAddressData", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", propOrder = {
    "addressIDBuilding",
    "addressIDUnit",
    "areaID",
    "areaName",
    "blockFaceID",
    "buildingID",
    "highBlockFaceNumber",
    "highLatitude",
    "highLongitude",
    "landmarkName",
    "latitude",
    "longitude",
    "lowBlockFaceNumber",
    "lowLatitude",
    "lowLongitude",
    "organizationName",
    "postCode",
    "regionAbbreviation",
    "regionID",
    "streetName",
    "streetNameID",
    "streetNumber",
    "taxiCoID",
    "unitNumber"
})
public class PFAddressData {

    @XmlElement(name = "AddressIDBuilding")
    protected Integer addressIDBuilding;
    @XmlElement(name = "AddressIDUnit")
    protected Integer addressIDUnit;
    @XmlElement(name = "AreaID")
    protected Integer areaID;
    @XmlElementRef(name = "AreaName", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> areaName;
    @XmlElement(name = "BlockFaceID")
    protected Integer blockFaceID;
    @XmlElement(name = "BuildingID")
    protected Integer buildingID;
    @XmlElement(name = "HighBlockFaceNumber")
    protected Integer highBlockFaceNumber;
    @XmlElement(name = "HighLatitude")
    protected Double highLatitude;
    @XmlElement(name = "HighLongitude")
    protected Double highLongitude;
    @XmlElementRef(name = "LandmarkName", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> landmarkName;
    @XmlElement(name = "Latitude")
    protected Double latitude;
    @XmlElement(name = "Longitude")
    protected Double longitude;
    @XmlElement(name = "LowBlockFaceNumber")
    protected Integer lowBlockFaceNumber;
    @XmlElement(name = "LowLatitude")
    protected Double lowLatitude;
    @XmlElement(name = "LowLongitude")
    protected Double lowLongitude;
    @XmlElementRef(name = "OrganizationName", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> organizationName;
    @XmlElementRef(name = "PostCode", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> postCode;
    @XmlElementRef(name = "RegionAbbreviation", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> regionAbbreviation;
    @XmlElement(name = "RegionID")
    protected Integer regionID;
    @XmlElementRef(name = "StreetName", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> streetName;
    @XmlElement(name = "StreetNameID")
    protected Integer streetNameID;
    @XmlElementRef(name = "StreetNumber", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> streetNumber;
    @XmlElement(name = "TaxiCoID")
    protected Integer taxiCoID;
    @XmlElementRef(name = "UnitNumber", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> unitNumber;

    /**
     * Gets the value of the addressIDBuilding property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAddressIDBuilding() {
        return addressIDBuilding;
    }

    /**
     * Sets the value of the addressIDBuilding property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAddressIDBuilding(Integer value) {
        this.addressIDBuilding = value;
    }

    /**
     * Gets the value of the addressIDUnit property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAddressIDUnit() {
        return addressIDUnit;
    }

    /**
     * Sets the value of the addressIDUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAddressIDUnit(Integer value) {
        this.addressIDUnit = value;
    }

    /**
     * Gets the value of the areaID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAreaID() {
        return areaID;
    }

    /**
     * Sets the value of the areaID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAreaID(Integer value) {
        this.areaID = value;
    }

    /**
     * Gets the value of the areaName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAreaName() {
        return areaName;
    }

    /**
     * Sets the value of the areaName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAreaName(JAXBElement<String> value) {
        this.areaName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the blockFaceID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBlockFaceID() {
        return blockFaceID;
    }

    /**
     * Sets the value of the blockFaceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBlockFaceID(Integer value) {
        this.blockFaceID = value;
    }

    /**
     * Gets the value of the buildingID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBuildingID() {
        return buildingID;
    }

    /**
     * Sets the value of the buildingID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBuildingID(Integer value) {
        this.buildingID = value;
    }

    /**
     * Gets the value of the highBlockFaceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHighBlockFaceNumber() {
        return highBlockFaceNumber;
    }

    /**
     * Sets the value of the highBlockFaceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHighBlockFaceNumber(Integer value) {
        this.highBlockFaceNumber = value;
    }

    /**
     * Gets the value of the highLatitude property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getHighLatitude() {
        return highLatitude;
    }

    /**
     * Sets the value of the highLatitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setHighLatitude(Double value) {
        this.highLatitude = value;
    }

    /**
     * Gets the value of the highLongitude property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getHighLongitude() {
        return highLongitude;
    }

    /**
     * Sets the value of the highLongitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setHighLongitude(Double value) {
        this.highLongitude = value;
    }

    /**
     * Gets the value of the landmarkName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLandmarkName() {
        return landmarkName;
    }

    /**
     * Sets the value of the landmarkName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLandmarkName(JAXBElement<String> value) {
        this.landmarkName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the latitude property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of the latitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setLatitude(Double value) {
        this.latitude = value;
    }

    /**
     * Gets the value of the longitude property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of the longitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setLongitude(Double value) {
        this.longitude = value;
    }

    /**
     * Gets the value of the lowBlockFaceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLowBlockFaceNumber() {
        return lowBlockFaceNumber;
    }

    /**
     * Sets the value of the lowBlockFaceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLowBlockFaceNumber(Integer value) {
        this.lowBlockFaceNumber = value;
    }

    /**
     * Gets the value of the lowLatitude property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getLowLatitude() {
        return lowLatitude;
    }

    /**
     * Sets the value of the lowLatitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setLowLatitude(Double value) {
        this.lowLatitude = value;
    }

    /**
     * Gets the value of the lowLongitude property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getLowLongitude() {
        return lowLongitude;
    }

    /**
     * Sets the value of the lowLongitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setLowLongitude(Double value) {
        this.lowLongitude = value;
    }

    /**
     * Gets the value of the organizationName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOrganizationName() {
        return organizationName;
    }

    /**
     * Sets the value of the organizationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOrganizationName(JAXBElement<String> value) {
        this.organizationName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the postCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPostCode() {
        return postCode;
    }

    /**
     * Sets the value of the postCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPostCode(JAXBElement<String> value) {
        this.postCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the regionAbbreviation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRegionAbbreviation() {
        return regionAbbreviation;
    }

    /**
     * Sets the value of the regionAbbreviation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRegionAbbreviation(JAXBElement<String> value) {
        this.regionAbbreviation = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the regionID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRegionID() {
        return regionID;
    }

    /**
     * Sets the value of the regionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRegionID(Integer value) {
        this.regionID = value;
    }

    /**
     * Gets the value of the streetName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStreetName() {
        return streetName;
    }

    /**
     * Sets the value of the streetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStreetName(JAXBElement<String> value) {
        this.streetName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the streetNameID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getStreetNameID() {
        return streetNameID;
    }

    /**
     * Sets the value of the streetNameID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStreetNameID(Integer value) {
        this.streetNameID = value;
    }

    /**
     * Gets the value of the streetNumber property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStreetNumber() {
        return streetNumber;
    }

    /**
     * Sets the value of the streetNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStreetNumber(JAXBElement<String> value) {
        this.streetNumber = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the taxiCoID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTaxiCoID() {
        return taxiCoID;
    }

    /**
     * Sets the value of the taxiCoID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTaxiCoID(Integer value) {
        this.taxiCoID = value;
    }

    /**
     * Gets the value of the unitNumber property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUnitNumber() {
        return unitNumber;
    }

    /**
     * Sets the value of the unitNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUnitNumber(JAXBElement<String> value) {
        this.unitNumber = ((JAXBElement<String> ) value);
    }

}
