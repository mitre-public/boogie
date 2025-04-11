
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This datatype contains the routes that are selected for ViaSIDs and ViaSTARs as part of the CompanyRoute.
 * 
 * <p>Java class for SidStarSelectedRoutes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SidStarSelectedRoutes"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="viaEnroute" type="{}ViaEnroute" minOccurs="0"/&gt;
 *         &lt;element name="viaCommon" type="{}ViaCommon" minOccurs="0"/&gt;
 *         &lt;element name="viaRunway" type="{}ViaRunway" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SidStarSelectedRoutes", propOrder = {
    "viaEnroute",
    "viaCommon",
    "viaRunway"
})
public class SidStarSelectedRoutes {

    protected ViaEnroute viaEnroute;
    protected ViaCommon viaCommon;
    protected ViaRunway viaRunway;

    /**
     * Gets the value of the viaEnroute property.
     * 
     * @return
     *     possible object is
     *     {@link ViaEnroute }
     *     
     */
    public ViaEnroute getViaEnroute() {
        return viaEnroute;
    }

    /**
     * Sets the value of the viaEnroute property.
     * 
     * @param value
     *     allowed object is
     *     {@link ViaEnroute }
     *     
     */
    public void setViaEnroute(ViaEnroute value) {
        this.viaEnroute = value;
    }

    /**
     * Gets the value of the viaCommon property.
     * 
     * @return
     *     possible object is
     *     {@link ViaCommon }
     *     
     */
    public ViaCommon getViaCommon() {
        return viaCommon;
    }

    /**
     * Sets the value of the viaCommon property.
     * 
     * @param value
     *     allowed object is
     *     {@link ViaCommon }
     *     
     */
    public void setViaCommon(ViaCommon value) {
        this.viaCommon = value;
    }

    /**
     * Gets the value of the viaRunway property.
     * 
     * @return
     *     possible object is
     *     {@link ViaRunway }
     *     
     */
    public ViaRunway getViaRunway() {
        return viaRunway;
    }

    /**
     * Sets the value of the viaRunway property.
     * 
     * @param value
     *     allowed object is
     *     {@link ViaRunway }
     *     
     */
    public void setViaRunway(ViaRunway value) {
        this.viaRunway = value;
    }

}
