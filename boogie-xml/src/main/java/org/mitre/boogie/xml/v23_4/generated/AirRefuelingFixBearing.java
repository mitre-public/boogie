
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigInteger;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;


/**
 * 
 *                 Degrees magnetic from the navigational aid or waypoint. 
 *                 Examples: 055, 226
 *                 Range of allowable values: 001-360 Values are padded with leading zeros.
 *             
 * 
 * <p>Java class for AirRefuelingFixBearing complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AirRefuelingFixBearing"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;&gt;Integer-001-to-360"&gt;
 *       &lt;attribute name="units" type="{http://www.w3.org/2001/XMLSchema}string" fixed="degrees" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirRefuelingFixBearing", propOrder = {
    "value"
})
public class AirRefuelingFixBearing {

    @XmlValue
    protected BigInteger value;
    @XmlAttribute(name = "units")
    protected String units;

    /**
     * 
     *                 Range of allowable values: 001-360 (values are padded with leading zeros)
     *             
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setValue(BigInteger value) {
        this.value = value;
    }

    /**
     * Gets the value of the units property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnits() {
        if (units == null) {
            return "degrees";
        } else {
            return units;
        }
    }

    /**
     * Sets the value of the units property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnits(String value) {
        this.units = value;
    }

}
