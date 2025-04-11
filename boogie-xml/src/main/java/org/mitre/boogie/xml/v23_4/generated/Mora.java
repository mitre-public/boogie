
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Grid Minimum Off-route Altitudes (MORA) provides terrain and obstruction clearance within the section defined by the enclosing record.  For unknown MORAs, the isUnknown flag will be set to true, and the altitude field will not be set.
 * 
 * <p>Java class for Mora complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Mora"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isUnknown" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="altitude" type="{http://www.arinc424.com/xml/datatypes}AltitudeValue" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Mora", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "isUnknown",
    "altitude"
})
public class Mora {

    @XmlElement(defaultValue = "false")
    protected Boolean isUnknown;
    @XmlSchemaType(name = "integer")
    protected Integer altitude;

    /**
     * Gets the value of the isUnknown property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsUnknown() {
        return isUnknown;
    }

    /**
     * Sets the value of the isUnknown property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsUnknown(Boolean value) {
        this.isUnknown = value;
    }

    /**
     * Gets the value of the altitude property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAltitude() {
        return altitude;
    }

    /**
     * Sets the value of the altitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAltitude(Integer value) {
        this.altitude = value;
    }

}
