
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
 *         &lt;element name="GetAreaAddressResult" type="{http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService}PFAddressResponse" minOccurs="0"/>
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
    "getAreaAddressResult"
})
@XmlRootElement(name = "GetAreaAddressResponse")
public class GetAreaAddressResponse {

    @XmlElementRef(name = "GetAreaAddressResult", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<PFAddressResponse> getAreaAddressResult;

    /**
     * Gets the value of the getAreaAddressResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}
     *     
     */
    public JAXBElement<PFAddressResponse> getGetAreaAddressResult() {
        return getAreaAddressResult;
    }

    /**
     * Sets the value of the getAreaAddressResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}
     *     
     */
    public void setGetAreaAddressResult(JAXBElement<PFAddressResponse> value) {
        this.getAreaAddressResult = ((JAXBElement<PFAddressResponse> ) value);
    }

}
