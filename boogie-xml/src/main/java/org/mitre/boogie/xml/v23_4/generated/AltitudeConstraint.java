
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The altitude constraint provides an at, at or above, at or below, or between altitude for a parent object. A window (e.g., between altitudes) is done by coding both an atOrAbove and an atOrBelow element.
 * 
 * <p>Java class for AltitudeConstraint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AltitudeConstraint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="at" type="{http://www.arinc424.com/xml/datatypes}Constraint" minOccurs="0"/&gt;
 *         &lt;element name="atOrAbove" type="{http://www.arinc424.com/xml/datatypes}Constraint" minOccurs="0"/&gt;
 *         &lt;element name="atOrBelow" type="{http://www.arinc424.com/xml/datatypes}Constraint" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AltitudeConstraint", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "at",
    "atOrAbove",
    "atOrBelow"
})
public class AltitudeConstraint {

    protected Constraint at;
    protected Constraint atOrAbove;
    protected Constraint atOrBelow;

    /**
     * Gets the value of the at property.
     * 
     * @return
     *     possible object is
     *     {@link Constraint }
     *     
     */
    public Constraint getAt() {
        return at;
    }

    /**
     * Sets the value of the at property.
     * 
     * @param value
     *     allowed object is
     *     {@link Constraint }
     *     
     */
    public void setAt(Constraint value) {
        this.at = value;
    }

    /**
     * Gets the value of the atOrAbove property.
     * 
     * @return
     *     possible object is
     *     {@link Constraint }
     *     
     */
    public Constraint getAtOrAbove() {
        return atOrAbove;
    }

    /**
     * Sets the value of the atOrAbove property.
     * 
     * @param value
     *     allowed object is
     *     {@link Constraint }
     *     
     */
    public void setAtOrAbove(Constraint value) {
        this.atOrAbove = value;
    }

    /**
     * Gets the value of the atOrBelow property.
     * 
     * @return
     *     possible object is
     *     {@link Constraint }
     *     
     */
    public Constraint getAtOrBelow() {
        return atOrBelow;
    }

    /**
     * Sets the value of the atOrBelow property.
     * 
     * @param value
     *     allowed object is
     *     {@link Constraint }
     *     
     */
    public void setAtOrBelow(Constraint value) {
        this.atOrBelow = value;
    }

}
