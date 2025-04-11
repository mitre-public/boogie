
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 AN IDREF to a waypoint plus its
 *                 ICAO code.
 *             
 * 
 * <p>Java class for WaypointRefPlusIcaoCode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WaypointRefPlusIcaoCode"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}waypointRef"/&gt;
 *         &lt;element ref="{}icaoCode"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WaypointRefPlusIcaoCode", propOrder = {
    "waypointRef",
    "icaoCode"
})
public class WaypointRefPlusIcaoCode {

    @XmlElement(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object waypointRef;
    @XmlElement(required = true)
    protected String icaoCode;

    /**
     * Gets the value of the waypointRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getWaypointRef() {
        return waypointRef;
    }

    /**
     * Sets the value of the waypointRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setWaypointRef(Object value) {
        this.waypointRef = value;
    }

    /**
     * Gets the value of the icaoCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIcaoCode() {
        return icaoCode;
    }

    /**
     * Sets the value of the icaoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIcaoCode(String value) {
        this.icaoCode = value;
    }

}
