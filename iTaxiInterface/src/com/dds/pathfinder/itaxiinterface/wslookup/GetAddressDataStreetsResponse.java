
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
 *         &lt;element name="GetAddressDataStreetsResult" type="{http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService}PFAddressResponse" minOccurs="0"/>
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
    "getAddressDataStreetsResult"
})
@XmlRootElement(name = "GetAddressDataStreetsResponse")
public class GetAddressDataStreetsResponse {

    @XmlElementRef(name = "GetAddressDataStreetsResult", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<PFAddressResponse> getAddressDataStreetsResult;

    /**
     * Gets the value of the getAddressDataStreetsResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}
     *     
     */
    public JAXBElement<PFAddressResponse> getGetAddressDataStreetsResult() {
        return getAddressDataStreetsResult;
    }

    /**
     * Sets the value of the getAddressDataStreetsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}
     *     
     */
    public void setGetAddressDataStreetsResult(JAXBElement<PFAddressResponse> value) {
        this.getAddressDataStreetsResult = ((JAXBElement<PFAddressResponse> ) value);
    }

}
