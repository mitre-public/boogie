
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The “Latitude” field contains the latitude of the navigational feature identified in the record.  The latitude is a complex type that consists of children elements that contain scaled integers.  
 * 
 * <p>Java class for Latitude complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Latitude"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="deg" type="{http://www.arinc424.com/xml/datatypes}LatDegree"/&gt;
 *         &lt;element name="min" type="{http://www.arinc424.com/xml/datatypes}Minute"/&gt;
 *         &lt;element name="sec" type="{http://www.arinc424.com/xml/datatypes}Second"/&gt;
 *         &lt;element name="hSec" type="{http://www.arinc424.com/xml/datatypes}HundredthSecond"/&gt;
 *         &lt;element name="northSouth" type="{http://www.arinc424.com/xml/enumerations}NorthSouth"/&gt;
 *         &lt;element name="decimalDegreesLatitude" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Latitude", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "deg",
    "min",
    "sec",
    "hSec",
    "northSouth",
    "decimalDegreesLatitude"
})
public class Latitude {

    protected int deg;
    protected int min;
    protected int sec;
    protected int hSec;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected NorthSouth northSouth;
    protected BigDecimal decimalDegreesLatitude;

    /**
     * Gets the value of the deg property.
     * 
     */
    public int getDeg() {
        return deg;
    }

    /**
     * Sets the value of the deg property.
     * 
     */
    public void setDeg(int value) {
        this.deg = value;
    }

    /**
     * Gets the value of the min property.
     * 
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the value of the min property.
     * 
     */
    public void setMin(int value) {
        this.min = value;
    }

    /**
     * Gets the value of the sec property.
     * 
     */
    public int getSec() {
        return sec;
    }

    /**
     * Sets the value of the sec property.
     * 
     */
    public void setSec(int value) {
        this.sec = value;
    }

    /**
     * Gets the value of the hSec property.
     * 
     */
    public int getHSec() {
        return hSec;
    }

    /**
     * Sets the value of the hSec property.
     * 
     */
    public void setHSec(int value) {
        this.hSec = value;
    }

    /**
     * Gets the value of the northSouth property.
     * 
     * @return
     *     possible object is
     *     {@link NorthSouth }
     *     
     */
    public NorthSouth getNorthSouth() {
        return northSouth;
    }

    /**
     * Sets the value of the northSouth property.
     * 
     * @param value
     *     allowed object is
     *     {@link NorthSouth }
     *     
     */
    public void setNorthSouth(NorthSouth value) {
        this.northSouth = value;
    }

    /**
     * Gets the value of the decimalDegreesLatitude property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDecimalDegreesLatitude() {
        return decimalDegreesLatitude;
    }

    /**
     * Sets the value of the decimalDegreesLatitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDecimalDegreesLatitude(BigDecimal value) {
        this.decimalDegreesLatitude = value;
    }

}
