
package com.dds.pathfinder.itaxiinterface.wslookup;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PFDirectionsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PFDirectionsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DirectionsToDestination" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MetersToDestination" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="SecondsToDestination" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
@XmlType(name = "PFDirectionsResponse", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", propOrder = {
    "directionsToDestination",
    "metersToDestination",
    "secondsToDestination",
    "status"
})
public class PFDirectionsResponse {

    @XmlElementRef(name = "DirectionsToDestination", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> directionsToDestination;
    @XmlElement(name = "MetersToDestination")
    protected Integer metersToDestination;
    @XmlElement(name = "SecondsToDestination")
    protected Integer secondsToDestination;
    @XmlElementRef(name = "Status", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> status;

    /**
     * Gets the value of the directionsToDestination property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDirectionsToDestination() {
        return directionsToDestination;
    }

    /**
     * Sets the value of the directionsToDestination property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDirectionsToDestination(JAXBElement<String> value) {
        this.directionsToDestination = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the metersToDestination property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMetersToDestination() {
        return metersToDestination;
    }

    /**
     * Sets the value of the metersToDestination property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMetersToDestination(Integer value) {
        this.metersToDestination = value;
    }

    /**
     * Gets the value of the secondsToDestination property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSecondsToDestination() {
        return secondsToDestination;
    }

    /**
     * Sets the value of the secondsToDestination property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSecondsToDestination(Integer value) {
        this.secondsToDestination = value;
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
