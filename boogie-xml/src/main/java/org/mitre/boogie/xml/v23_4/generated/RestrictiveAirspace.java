
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Restrictive Airspace Record.
 * 
 * <p>Java class for RestrictiveAirspace complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RestrictiveAirspace"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Airspace"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="icaoCode" type="{http://www.arinc424.com/xml/datatypes}IcaoCode" minOccurs="0"/&gt;
 *         &lt;element name="restrictiveAirspaceDesignation" type="{http://www.arinc424.com/xml/datatypes}RestrictiveAirspaceDesignation"/&gt;
 *         &lt;element name="restrictiveAirspaceName" type="{http://www.arinc424.com/xml/datatypes}Name" minOccurs="0"/&gt;
 *         &lt;element name="restrictiveAirspaceType" type="{http://www.arinc424.com/xml/enumerations}RestrictiveAirspaceType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RestrictiveAirspace", propOrder = {
    "icaoCode",
    "restrictiveAirspaceDesignation",
    "restrictiveAirspaceName",
    "restrictiveAirspaceType"
})
public class RestrictiveAirspace
    extends Airspace
{

    protected String icaoCode;
    @XmlElement(required = true)
    protected String restrictiveAirspaceDesignation;
    protected String restrictiveAirspaceName;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected RestrictiveAirspaceType restrictiveAirspaceType;

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
     * Gets the value of the restrictiveAirspaceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestrictiveAirspaceName() {
        return restrictiveAirspaceName;
    }

    /**
     * Sets the value of the restrictiveAirspaceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestrictiveAirspaceName(String value) {
        this.restrictiveAirspaceName = value;
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
