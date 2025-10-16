
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The “Magnetic Variation” field specifies the angular difference between True North and Magnetic North at the location defined in the record. “Dynamic Magnetic Variation” is a computer model derived value and takes location and date into consideration. For the “Station Declination” used in some record types, refer to Section 5.66.
 * 
 * <p>Java class for MagneticVariation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MagneticVariation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="magneticVariationEWT" type="{http://www.arinc424.com/xml/enumerations}MagneticVariationEWT"/&gt;
 *         &lt;element name="magneticVariationValue" type="{http://www.arinc424.com/xml/datatypes}MagneticVariationValue"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MagneticVariation", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "magneticVariationEWT",
    "magneticVariationValue"
})
public class MagneticVariation {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected MagneticVariationEWT magneticVariationEWT;
    @XmlElement(required = true)
    protected BigDecimal magneticVariationValue;

    /**
     * Gets the value of the magneticVariationEWT property.
     * 
     * @return
     *     possible object is
     *     {@link MagneticVariationEWT }
     *     
     */
    public MagneticVariationEWT getMagneticVariationEWT() {
        return magneticVariationEWT;
    }

    /**
     * Sets the value of the magneticVariationEWT property.
     * 
     * @param value
     *     allowed object is
     *     {@link MagneticVariationEWT }
     *     
     */
    public void setMagneticVariationEWT(MagneticVariationEWT value) {
        this.magneticVariationEWT = value;
    }

    /**
     * Gets the value of the magneticVariationValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMagneticVariationValue() {
        return magneticVariationValue;
    }

    /**
     * Sets the value of the magneticVariationValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMagneticVariationValue(BigDecimal value) {
        this.magneticVariationValue = value;
    }

}
