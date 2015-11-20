
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
 *         &lt;element name="GPSLatitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="GPSLongitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="TaxiCoID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
    "gpsLongitude",
    "taxiCoID"
})
@XmlRootElement(name = "GetAreaIDForGPS")
public class GetAreaIDForGPS {

    @XmlElement(name = "GPSLatitude")
    protected Double gpsLatitude;
    @XmlElement(name = "GPSLongitude")
    protected Double gpsLongitude;
    @XmlElement(name = "TaxiCoID")
    protected Integer taxiCoID;

    /**
     * Gets the value of the gpsLatitude property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getGPSLatitude() {
        return gpsLatitude;
    }

    /**
     * Sets the value of the gpsLatitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setGPSLatitude(Double value) {
        this.gpsLatitude = value;
    }

    /**
     * Gets the value of the gpsLongitude property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getGPSLongitude() {
        return gpsLongitude;
    }

    /**
     * Sets the value of the gpsLongitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setGPSLongitude(Double value) {
        this.gpsLongitude = value;
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

}
