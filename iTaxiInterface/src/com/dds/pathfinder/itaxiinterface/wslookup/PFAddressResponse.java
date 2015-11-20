
package com.dds.pathfinder.itaxiinterface.wslookup;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PFAddressResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PFAddressResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddressData" type="{http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService}ArrayOfPFAddressData" minOccurs="0"/>
 *         &lt;element name="NumberOfRecords" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PFAddressResponse", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", propOrder = {
    "addressData",
    "numberOfRecords",
    "status"
})
public class PFAddressResponse {

    @XmlElementRef(name = "AddressData", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfPFAddressData> addressData;
    @XmlElement(name = "NumberOfRecords")
    protected Integer numberOfRecords;
    @XmlElementRef(name = "Status", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> status;

    /**
     * Gets the value of the addressData property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPFAddressData }{@code >}
     *     
     */
    public JAXBElement<ArrayOfPFAddressData> getAddressData() {
        return addressData;
    }

    /**
     * Sets the value of the addressData property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPFAddressData }{@code >}
     *     
     */
    public void setAddressData(JAXBElement<ArrayOfPFAddressData> value) {
        this.addressData = ((JAXBElement<ArrayOfPFAddressData> ) value);
    }

    /**
     * Gets the value of the numberOfRecords property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfRecords() {
        return numberOfRecords;
    }

    /**
     * Sets the value of the numberOfRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfRecords(Integer value) {
        this.numberOfRecords = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStatus(JAXBElement<String> value) {
        this.status = ((JAXBElement<String> ) value);
    }

}
