
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Bearing complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Bearing"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="bearingValue" type="{http://www.arinc424.com/xml/datatypes}BearingValue"/&gt;
 *         &lt;element name="isTrueBearing" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bearing", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "bearingValue",
    "isTrueBearing"
})
public class Bearing {

    @XmlElement(required = true)
    protected BigDecimal bearingValue;
    @XmlElement(defaultValue = "false")
    protected Boolean isTrueBearing;

    /**
     * Gets the value of the bearingValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBearingValue() {
        return bearingValue;
    }

    /**
     * Sets the value of the bearingValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBearingValue(BigDecimal value) {
        this.bearingValue = value;
    }

    /**
     * Gets the value of the isTrueBearing property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTrueBearing() {
        return isTrueBearing;
    }

    /**
     * Sets the value of the isTrueBearing property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTrueBearing(Boolean value) {
        this.isTrueBearing = value;
    }

}
