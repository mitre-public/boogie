
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This element provides the upper and lower altitudes for this airway leg.
 * 
 * <p>Java class for AirspaceAltitudeConstraint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AirspaceAltitudeConstraint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="upperLimit" type="{http://www.arinc424.com/xml/datatypes}UpperLimitConstraint" minOccurs="0"/&gt;
 *         &lt;element name="lowerLimit" type="{http://www.arinc424.com/xml/datatypes}LowerLimitConstraint" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirspaceAltitudeConstraint", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "upperLimit",
    "lowerLimit"
})
public class AirspaceAltitudeConstraint {

    protected UpperLimitConstraint upperLimit;
    protected LowerLimitConstraint lowerLimit;

    /**
     * Gets the value of the upperLimit property.
     * 
     * @return
     *     possible object is
     *     {@link UpperLimitConstraint }
     *     
     */
    public UpperLimitConstraint getUpperLimit() {
        return upperLimit;
    }

    /**
     * Sets the value of the upperLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpperLimitConstraint }
     *     
     */
    public void setUpperLimit(UpperLimitConstraint value) {
        this.upperLimit = value;
    }

    /**
     * Gets the value of the lowerLimit property.
     * 
     * @return
     *     possible object is
     *     {@link LowerLimitConstraint }
     *     
     */
    public LowerLimitConstraint getLowerLimit() {
        return lowerLimit;
    }

    /**
     * Sets the value of the lowerLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link LowerLimitConstraint }
     *     
     */
    public void setLowerLimit(LowerLimitConstraint value) {
        this.lowerLimit = value;
    }

}
