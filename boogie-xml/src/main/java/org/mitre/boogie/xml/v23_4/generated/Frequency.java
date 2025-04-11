
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This field specifies frequency and unit of measure of the frequency information. 
 * 
 * <p>Java class for Frequency complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Frequency"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="freqUnitOfMeasure" type="{http://www.arinc424.com/xml/enumerations}FreqUnitOfMeasure"/&gt;
 *         &lt;element name="frequencyValue" type="{http://www.arinc424.com/xml/datatypes}FrequencyValue"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Frequency", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "freqUnitOfMeasure",
    "frequencyValue"
})
public class Frequency {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected FreqUnitOfMeasure freqUnitOfMeasure;
    @XmlElement(required = true)
    protected BigDecimal frequencyValue;

    /**
     * Gets the value of the freqUnitOfMeasure property.
     * 
     * @return
     *     possible object is
     *     {@link FreqUnitOfMeasure }
     *     
     */
    public FreqUnitOfMeasure getFreqUnitOfMeasure() {
        return freqUnitOfMeasure;
    }

    /**
     * Sets the value of the freqUnitOfMeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link FreqUnitOfMeasure }
     *     
     */
    public void setFreqUnitOfMeasure(FreqUnitOfMeasure value) {
        this.freqUnitOfMeasure = value;
    }

    /**
     * Gets the value of the frequencyValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFrequencyValue() {
        return frequencyValue;
    }

    /**
     * Sets the value of the frequencyValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFrequencyValue(BigDecimal value) {
        this.frequencyValue = value;
    }

}
