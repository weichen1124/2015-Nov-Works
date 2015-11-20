
package com.dds.pathfinder.itaxiinterface.wslookup;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="GetDirectionsResult" type="{http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService}PFDirectionsResponse" minOccurs="0"/>
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
    "getDirectionsResult"
})
@XmlRootElement(name = "GetDirectionsResponse")
public class GetDirectionsResponse {

    @XmlElementRef(name = "GetDirectionsResult", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<PFDirectionsResponse> getDirectionsResult;

    /**
     * Gets the value of the getDirectionsResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PFDirectionsResponse }{@code >}
     *     
     */
    public JAXBElement<PFDirectionsResponse> getGetDirectionsResult() {
        return getDirectionsResult;
    }

    /**
     * Sets the value of the getDirectionsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PFDirectionsResponse }{@code >}
     *     
     */
    public void setGetDirectionsResult(JAXBElement<PFDirectionsResponse> value) {
        this.getDirectionsResult = ((JAXBElement<PFDirectionsResponse> ) value);
    }

}
