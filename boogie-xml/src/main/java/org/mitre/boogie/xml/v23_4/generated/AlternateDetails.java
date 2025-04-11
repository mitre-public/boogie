
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class is an abstraction used to represent the details of an Alternate.
 * 
 * <p>Java class for AlternateDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AlternateDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="alternateIdentifier" type="{http://www.arinc424.com/xml/datatypes}PointReference"/&gt;
 *         &lt;element name="distanceToAlternate" type="{http://www.arinc424.com/xml/datatypes}DistanceIntegerNM"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AlternateDetails", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "alternateIdentifier",
    "distanceToAlternate"
})
public class AlternateDetails {

    @XmlElement(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object alternateIdentifier;
    @XmlSchemaType(name = "unsignedInt")
    protected long distanceToAlternate;

    /**
     * Gets the value of the alternateIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getAlternateIdentifier() {
        return alternateIdentifier;
    }

    /**
     * Sets the value of the alternateIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setAlternateIdentifier(Object value) {
        this.alternateIdentifier = value;
    }

    /**
     * Gets the value of the distanceToAlternate property.
     * 
     */
    public long getDistanceToAlternate() {
        return distanceToAlternate;
    }

    /**
     * Sets the value of the distanceToAlternate property.
     * 
     */
    public void setDistanceToAlternate(long value) {
        this.distanceToAlternate = value;
    }

}
