
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
 *         &lt;element name="GetClosestAddressDataResult" type="{http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService}PFAddressResponse" minOccurs="0"/>
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
    "getClosestAddressDataResult"
})
@XmlRootElement(name = "GetClosestAddressDataResponse")
public class GetClosestAddressDataResponse {

    @XmlElementRef(name = "GetClosestAddressDataResult", namespace = "http://DDSAddressConnectionManagerService", type = JAXBElement.class, required = false)
    protected JAXBElement<PFAddressResponse> getClosestAddressDataResult;

    /**
     * Gets the value of the getClosestAddressDataResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}
     *     
     */
    public JAXBElement<PFAddressResponse> getGetClosestAddressDataResult() {
        return getClosestAddressDataResult;
    }

    /**
     * Sets the value of the getClosestAddressDataResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}
     *     
     */
    public void setGetClosestAddressDataResult(JAXBElement<PFAddressResponse> value) {
        this.getClosestAddressDataResult = ((JAXBElement<PFAddressResponse> ) value);
    }

}
