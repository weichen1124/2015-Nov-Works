
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
 *         &lt;element name="GPSLatitudeFrom" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="GPSLongitudeFrom" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="GPSLatitudeTo" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="GPSLongitudeTo" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="LocationFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LocationTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "gpsLatitudeFrom",
    "gpsLongitudeFrom",
    "gpsLatitudeTo",
    "gpsLongitudeTo",
    "locationFrom",
    "locationTo"
})
@XmlRootElement(name = "GetDirections")
public class GetDirections {

    @XmlElement(name = "GPSLatitudeFrom")
    protected Double gpsLatitudeFrom;
    @XmlElement(name = "GPSLongitudeFrom")
    protected Double gpsLongitudeFrom;
    @XmlElement(name = "GPSLatitudeTo")
    protected Double gpsLatitudeTo;
    @XmlElement(name = "GPSLongitudeTo")
    protected Double gpsLongitudeTo;
    @XmlElementRef(name = "LocationFrom", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> locationFrom;
    @XmlElementRef(name = "LocationTo", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<String> locationTo;

    /**
     * Gets the value of the gpsLatitudeFrom property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getGPSLatitudeFrom() {
        return gpsLatitudeFrom;
    }

    /**
     * Sets the value of the gpsLatitudeFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setGPSLatitudeFrom(Double value) {
        this.gpsLatitudeFrom = value;
    }

    /**
     * Gets the value of the gpsLongitudeFrom property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getGPSLongitudeFrom() {
        return gpsLongitudeFrom;
    }

    /**
     * Sets the value of the gpsLongitudeFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setGPSLongitudeFrom(Double value) {
        this.gpsLongitudeFrom = value;
    }

    /**
     * Gets the value of the gpsLatitudeTo property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getGPSLatitudeTo() {
        return gpsLatitudeTo;
    }

    /**
     * Sets the value of the gpsLatitudeTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setGPSLatitudeTo(Double value) {
        this.gpsLatitudeTo = value;
    }

    /**
     * Gets the value of the gpsLongitudeTo property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getGPSLongitudeTo() {
        return gpsLongitudeTo;
    }

    /**
     * Sets the value of the gpsLongitudeTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setGPSLongitudeTo(Double value) {
        this.gpsLongitudeTo = value;
    }

    /**
     * Gets the value of the locationFrom property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocationFrom() {
        return locationFrom;
    }

    /**
     * Sets the value of the locationFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocationFrom(JAXBElement<String> value) {
        this.locationFrom = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the locationTo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocationTo() {
        return locationTo;
    }

    /**
     * Sets the value of the locationTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocationTo(JAXBElement<String> value) {
        this.locationTo = ((JAXBElement<String> ) value);
    }

}
