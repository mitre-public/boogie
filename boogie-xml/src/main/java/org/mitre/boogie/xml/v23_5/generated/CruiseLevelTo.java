
package org.mitre.boogie.xml.v23_5.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This element indicates the "Cruise Level To" values from the cruising tables.  The value can be represented as a level with a unit of measure (feet or meters) or with the isUnlimited flag.
 * 
 * <p>Java class for CruiseLevelTo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CruiseLevelTo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="level" type="{http://www.arinc424.com/xml/datatypes}CruiseLevelOrSeparation" minOccurs="0"/&gt;
 *         &lt;element name="isUnlimited" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CruiseLevelTo", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "level",
    "isUnlimited"
})
public class CruiseLevelTo {

    protected CruiseLevelOrSeparation level;
    @XmlElement(defaultValue = "false")
    protected Boolean isUnlimited;

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link CruiseLevelOrSeparation }
     *     
     */
    public CruiseLevelOrSeparation getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link CruiseLevelOrSeparation }
     *     
     */
    public void setLevel(CruiseLevelOrSeparation value) {
        this.level = value;
    }

    /**
     * Gets the value of the isUnlimited property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsUnlimited() {
        return isUnlimited;
    }

    /**
     * Sets the value of the isUnlimited property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsUnlimited(Boolean value) {
        this.isUnlimited = value;
    }

}
