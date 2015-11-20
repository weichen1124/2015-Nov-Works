
package com.dds.pathfinder.itaxiinterface.wslookup;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfPFAddressData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfPFAddressData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PFAddressData" type="{http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService}PFAddressData" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfPFAddressData", namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", propOrder = {
    "pfAddressData"
})
public class ArrayOfPFAddressData {

    @XmlElement(name = "PFAddressData", nillable = true)
    protected List<PFAddressData> pfAddressData;

    /**
     * Gets the value of the pfAddressData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pfAddressData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPFAddressData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PFAddressData }
     * 
     * 
     */
    public List<PFAddressData> getPFAddressData() {
        if (pfAddressData == null) {
            pfAddressData = new ArrayList<PFAddressData>();
        }
        return this.pfAddressData;
    }

}
