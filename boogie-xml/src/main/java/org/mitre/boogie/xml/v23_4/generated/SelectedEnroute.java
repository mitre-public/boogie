
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SelectedEnroute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SelectedEnroute"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="viaFix" type="{}ViaFix" minOccurs="0"/&gt;
 *         &lt;element name="viaAirway" type="{}ViaAirway" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SelectedEnroute", propOrder = {
    "viaFix",
    "viaAirway"
})
public class SelectedEnroute {

    protected ViaFix viaFix;
    protected ViaAirway viaAirway;

    /**
     * Gets the value of the viaFix property.
     * 
     * @return
     *     possible object is
     *     {@link ViaFix }
     *     
     */
    public ViaFix getViaFix() {
        return viaFix;
    }

    /**
     * Sets the value of the viaFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link ViaFix }
     *     
     */
    public void setViaFix(ViaFix value) {
        this.viaFix = value;
    }

    /**
     * Gets the value of the viaAirway property.
     * 
     * @return
     *     possible object is
     *     {@link ViaAirway }
     *     
     */
    public ViaAirway getViaAirway() {
        return viaAirway;
    }

    /**
     * Sets the value of the viaAirway property.
     * 
     * @param value
     *     allowed object is
     *     {@link ViaAirway }
     *     
     */
    public void setViaAirway(ViaAirway value) {
        this.viaAirway = value;
    }

}
