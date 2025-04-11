
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AeroTelecomNetworks complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AeroTelecomNetworks"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="aeroTelecomNetwork" type="{}AeroTelecomNetwork" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AeroTelecomNetworks", propOrder = {
    "aeroTelecomNetwork"
})
public class AeroTelecomNetworks {

    protected List<AeroTelecomNetwork> aeroTelecomNetwork;

    /**
     * Gets the value of the aeroTelecomNetwork property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the aeroTelecomNetwork property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAeroTelecomNetwork().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AeroTelecomNetwork }
     * 
     * 
     */
    public List<AeroTelecomNetwork> getAeroTelecomNetwork() {
        if (aeroTelecomNetwork == null) {
            aeroTelecomNetwork = new ArrayList<AeroTelecomNetwork>();
        }
        return this.aeroTelecomNetwork;
    }

}
