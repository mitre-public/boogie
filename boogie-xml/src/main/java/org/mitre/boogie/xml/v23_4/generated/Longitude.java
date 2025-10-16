
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The Longitude field contains the longitude of the geographic position of the navigational feature identified in the record.
 * 
 * <p>Java class for Longitude complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Longitude"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="deg" type="{http://www.arinc424.com/xml/datatypes}longDegree"/&gt;
 *         &lt;element name="min" type="{http://www.arinc424.com/xml/datatypes}Minute"/&gt;
 *         &lt;element name="sec" type="{http://www.arinc424.com/xml/datatypes}Second"/&gt;
 *         &lt;element name="hSec" type="{http://www.arinc424.com/xml/datatypes}HundredthSecond"/&gt;
 *         &lt;element name="eastWest" type="{http://www.arinc424.com/xml/enumerations}EastWest"/&gt;
 *         &lt;element name="decimalDegreesLongitude" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Longitude", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "deg",
    "min",
    "sec",
    "hSec",
    "eastWest",
    "decimalDegreesLongitude"
})
public class Longitude {

    protected int deg;
    protected int min;
    protected int sec;
    protected int hSec;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected EastWest eastWest;
    protected BigDecimal decimalDegreesLongitude;

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
     * Gets the value of the eastWest property.
     * 
     * @return
     *     possible object is
     *     {@link EastWest }
     *     
     */
    public EastWest getEastWest() {
        return eastWest;
    }

    /**
     * Sets the value of the eastWest property.
     * 
     * @param value
     *     allowed object is
     *     {@link EastWest }
     *     
     */
    public void setEastWest(EastWest value) {
        this.eastWest = value;
    }

    /**
     * Gets the value of the decimalDegreesLongitude property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDecimalDegreesLongitude() {
        return decimalDegreesLongitude;
    }

    /**
     * Sets the value of the decimalDegreesLongitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDecimalDegreesLongitude(BigDecimal value) {
        this.decimalDegreesLongitude = value;
    }

}
