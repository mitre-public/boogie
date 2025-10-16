
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class is a representation of restrictive airspace.
 * 
 * <p>Java class for RestrictiveAirspaceReference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RestrictiveAirspaceReference"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="restrictiveAirspaceDesignation" type="{http://www.arinc424.com/xml/datatypes}RestrictiveAirspaceDesignation"/&gt;
 *         &lt;element name="restrictiveAirspaceICAOCode" type="{http://www.arinc424.com/xml/datatypes}IcaoCode"/&gt;
 *         &lt;element name="restrictiveAirspaceMultipleCode" type="{http://www.arinc424.com/xml/datatypes}MultipleCode"/&gt;
 *         &lt;element name="restrictiveAirspaceType" type="{http://www.arinc424.com/xml/enumerations}RestrictiveAirspaceType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RestrictiveAirspaceReference", propOrder = {
    "restrictiveAirspaceDesignation",
    "restrictiveAirspaceICAOCode",
    "restrictiveAirspaceMultipleCode",
    "restrictiveAirspaceType"
})
public class RestrictiveAirspaceReference {

    @XmlElement(required = true)
    protected String restrictiveAirspaceDesignation;
    @XmlElement(required = true)
    protected String restrictiveAirspaceICAOCode;
    @XmlElement(required = true)
    protected String restrictiveAirspaceMultipleCode;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected RestrictiveAirspaceType restrictiveAirspaceType;

    /**
     * Gets the value of the restrictiveAirspaceDesignation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestrictiveAirspaceDesignation() {
        return restrictiveAirspaceDesignation;
    }

    /**
     * Sets the value of the restrictiveAirspaceDesignation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestrictiveAirspaceDesignation(String value) {
        this.restrictiveAirspaceDesignation = value;
    }

    /**
     * Gets the value of the restrictiveAirspaceICAOCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestrictiveAirspaceICAOCode() {
        return restrictiveAirspaceICAOCode;
    }

    /**
     * Sets the value of the restrictiveAirspaceICAOCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestrictiveAirspaceICAOCode(String value) {
        this.restrictiveAirspaceICAOCode = value;
    }

    /**
     * Gets the value of the restrictiveAirspaceMultipleCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestrictiveAirspaceMultipleCode() {
        return restrictiveAirspaceMultipleCode;
    }

    /**
     * Sets the value of the restrictiveAirspaceMultipleCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestrictiveAirspaceMultipleCode(String value) {
        this.restrictiveAirspaceMultipleCode = value;
    }

    /**
     * Gets the value of the restrictiveAirspaceType property.
     * 
     * @return
     *     possible object is
     *     {@link RestrictiveAirspaceType }
     *     
     */
    public RestrictiveAirspaceType getRestrictiveAirspaceType() {
        return restrictiveAirspaceType;
    }

    /**
     * Sets the value of the restrictiveAirspaceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RestrictiveAirspaceType }
     *     
     */
    public void setRestrictiveAirspaceType(RestrictiveAirspaceType value) {
        this.restrictiveAirspaceType = value;
    }

}
