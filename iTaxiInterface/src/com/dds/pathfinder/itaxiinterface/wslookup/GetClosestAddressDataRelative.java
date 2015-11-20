
package com.dds.pathfinder.itaxiinterface.wslookup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="GPSLatitude" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="GPSLongitude" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
    "gpsLatitude",
    "gpsLongitude"
})
@XmlRootElement(name = "GetClosestAddressDataRelative")
public class GetClosestAddressDataRelative {

    @XmlElement(name = "GPSLatitude")
    protected Integer gpsLatitude;
    @XmlElement(name = "GPSLongitude")
    protected Integer gpsLongitude;

    /**
     * Gets the value of the gpsLatitude property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getGPSLatitude() {
        return gpsLatitude;
    }

    /**
     * Sets the value of the gpsLatitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setGPSLatitude(Integer value) {
        this.gpsLatitude = value;
    }

    /**
     * Gets the value of the gpsLongitude property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getGPSLongitude() {
        return gpsLongitude;
    }

    /**
     * Sets the value of the gpsLongitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setGPSLongitude(Integer value) {
        this.gpsLongitude = value;
    }

}