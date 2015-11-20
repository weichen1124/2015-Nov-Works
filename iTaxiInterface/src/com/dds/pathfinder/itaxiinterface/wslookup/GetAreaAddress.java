
package com.dds.pathfinder.itaxiinterface.wslookup;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PartialStreetName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StreetNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UnitNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RegionAbbreviation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LandmarkName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OrganizationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PostCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PartialAreaName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TaxiCoID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="OrderBy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "partialStreetName",
    "streetNumber",
    "unitNumber",
    "regionAbbreviation",
    "landmarkName",
    "organizationName",
    "postCode",
    "partialAreaName",
    "taxiCoID",
    "orderBy"
})
@XmlRootElement(name = "GetAreaAddress")
public class GetAreaAddress {

    @XmlElementRef(name = "PartialStreetName", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> partialStreetName;
    @XmlElementRef(name = "StreetNumber", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> streetNumber;
    @XmlElementRef(name = "UnitNumber", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> unitNumber;
    @XmlElementRef(name = "RegionAbbreviation", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> regionAbbreviation;
    @XmlElementRef(name = "LandmarkName", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> landmarkName;
    @XmlElementRef(name = "OrganizationName", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> organizationName;
    @XmlElementRef(name = "PostCode", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> postCode;
    @XmlElementRef(name = "PartialAreaName", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> partialAreaName;
    @XmlElement(name = "TaxiCoID")
    protected Integer taxiCoID;
    @XmlElementRef(name = "OrderBy", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> orderBy;

    /**
     * Gets the value of the partialStreetName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPartialStreetName() {
        return partialStreetName;
    }

    /**
     * Sets the value of the partialStreetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPartialStreetName(JAXBElement<String> value) {
        this.partialStreetName = ((JAXBElement<String> ) value);
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
     * Gets the value of the partialAreaName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPartialAreaName() {
        return partialAreaName;
    }

    /**
     * Sets the value of the partialAreaName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPartialAreaName(JAXBElement<String> value) {
        this.partialAreaName = ((JAXBElement<String> ) value);
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
     * Gets the value of the orderBy property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOrderBy() {
        return orderBy;
    }

    /**
     * Sets the value of the orderBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOrderBy(JAXBElement<String> value) {
        this.orderBy = ((JAXBElement<String> ) value);
    }

}
