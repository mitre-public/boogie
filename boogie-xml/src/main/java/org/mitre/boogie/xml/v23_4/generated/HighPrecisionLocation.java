
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * High Precision Location
 * 
 * <p>Java class for HighPrecisionLocation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HighPrecisionLocation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="highPrecisionLatitude" type="{http://www.arinc424.com/xml/datatypes}HighPrecisionLatitude"/&gt;
 *         &lt;element name="highPrecisionLongitude" type="{http://www.arinc424.com/xml/datatypes}HighPrecisionLongitude"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HighPrecisionLocation", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "highPrecisionLatitude",
    "highPrecisionLongitude"
})
public class HighPrecisionLocation {

    @XmlElement(required = true)
    protected String highPrecisionLatitude;
    @XmlElement(required = true)
    protected String highPrecisionLongitude;

    /**
     * Gets the value of the highPrecisionLatitude property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHighPrecisionLatitude() {
        return highPrecisionLatitude;
    }

    /**
     * Sets the value of the highPrecisionLatitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHighPrecisionLatitude(String value) {
        this.highPrecisionLatitude = value;
    }

    /**
     * Gets the value of the highPrecisionLongitude property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHighPrecisionLongitude() {
        return highPrecisionLongitude;
    }

    /**
     * Sets the value of the highPrecisionLongitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHighPrecisionLongitude(String value) {
        this.highPrecisionLongitude = value;
    }

}
