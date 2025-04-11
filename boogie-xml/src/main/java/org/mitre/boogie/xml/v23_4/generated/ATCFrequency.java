
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;


/**
 * 
 *                 ATC communications are between 118.000 - 420.000 MHz.
 *                 Air traffic control center frequency. 
 *                 examples: 269 327.8 322.35 123.225
 *                 Range of allowable values: 118.000 - 420.000 The decimal place is between the 
 *                 third and fourth characters. No trailing zeros. Or, just a three-digit integer 
 *                 in the range 118-420
 *             
 * 
 * <p>Java class for ATCFrequency complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ATCFrequency"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;&gt;Decimal-118.001-to-420.999-or-Integer-118-to-420"&gt;
 *       &lt;attribute name="units" type="{http://www.w3.org/2001/XMLSchema}string" fixed="Mhz" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ATCFrequency", propOrder = {
    "value"
})
@XmlSeeAlso({
    FrequencyPlusDirection.class
})
public class ATCFrequency {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "units")
    protected String units;

    /**
     * 
     *                 Range of allowable values: 118.001 - 420.999, 118 - 420 
     *             
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
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
            return "Mhz";
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
