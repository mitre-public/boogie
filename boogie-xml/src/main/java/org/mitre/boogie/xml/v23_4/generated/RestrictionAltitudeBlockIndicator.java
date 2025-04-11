
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class is an abstraction for Restriction Altitude and Block Indicator elements as they occur together in airways restriction records.
 * 
 * <p>Java class for RestrictionAltitudeBlockIndicator complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RestrictionAltitudeBlockIndicator"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="blockIndicator" type="{http://www.arinc424.com/xml/enumerations}BlockIndicator" minOccurs="0"/&gt;
 *         &lt;element name="restrictionAltitude" type="{http://www.arinc424.com/xml/datatypes}RestrictionAltitude" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RestrictionAltitudeBlockIndicator", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "blockIndicator",
    "restrictionAltitude"
})
public class RestrictionAltitudeBlockIndicator {

    @XmlSchemaType(name = "string")
    protected BlockIndicator blockIndicator;
    @XmlSchemaType(name = "unsignedInt")
    protected Long restrictionAltitude;

    /**
     * Gets the value of the blockIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link BlockIndicator }
     *     
     */
    public BlockIndicator getBlockIndicator() {
        return blockIndicator;
    }

    /**
     * Sets the value of the blockIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link BlockIndicator }
     *     
     */
    public void setBlockIndicator(BlockIndicator value) {
        this.blockIndicator = value;
    }

    /**
     * Gets the value of the restrictionAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRestrictionAltitude() {
        return restrictionAltitude;
    }

    /**
     * Sets the value of the restrictionAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRestrictionAltitude(Long value) {
        this.restrictionAltitude = value;
    }

}
