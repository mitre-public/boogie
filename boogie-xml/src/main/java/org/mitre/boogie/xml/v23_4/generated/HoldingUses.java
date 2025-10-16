
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This data type provides boolean flags indicating how the holding pattern is used
 * 
 * <p>Java class for HoldingUses complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HoldingUses"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isOnUndefined" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isOnHigh" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isOnLow" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isOnSid" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isOnStar" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isOnApproach" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isOnMissedApproach" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HoldingUses", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "isOnUndefined",
    "isOnHigh",
    "isOnLow",
    "isOnSid",
    "isOnStar",
    "isOnApproach",
    "isOnMissedApproach"
})
public class HoldingUses {

    @XmlElement(defaultValue = "false")
    protected Boolean isOnUndefined;
    @XmlElement(defaultValue = "false")
    protected Boolean isOnHigh;
    @XmlElement(defaultValue = "false")
    protected Boolean isOnLow;
    @XmlElement(defaultValue = "false")
    protected Boolean isOnSid;
    @XmlElement(defaultValue = "false")
    protected Boolean isOnStar;
    @XmlElement(defaultValue = "false")
    protected Boolean isOnApproach;
    @XmlElement(defaultValue = "false")
    protected Boolean isOnMissedApproach;

    /**
     * Gets the value of the isOnUndefined property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOnUndefined() {
        return isOnUndefined;
    }

    /**
     * Sets the value of the isOnUndefined property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOnUndefined(Boolean value) {
        this.isOnUndefined = value;
    }

    /**
     * Gets the value of the isOnHigh property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOnHigh() {
        return isOnHigh;
    }

    /**
     * Sets the value of the isOnHigh property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOnHigh(Boolean value) {
        this.isOnHigh = value;
    }

    /**
     * Gets the value of the isOnLow property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOnLow() {
        return isOnLow;
    }

    /**
     * Sets the value of the isOnLow property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOnLow(Boolean value) {
        this.isOnLow = value;
    }

    /**
     * Gets the value of the isOnSid property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOnSid() {
        return isOnSid;
    }

    /**
     * Sets the value of the isOnSid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOnSid(Boolean value) {
        this.isOnSid = value;
    }

    /**
     * Gets the value of the isOnStar property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOnStar() {
        return isOnStar;
    }

    /**
     * Sets the value of the isOnStar property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOnStar(Boolean value) {
        this.isOnStar = value;
    }

    /**
     * Gets the value of the isOnApproach property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOnApproach() {
        return isOnApproach;
    }

    /**
     * Sets the value of the isOnApproach property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOnApproach(Boolean value) {
        this.isOnApproach = value;
    }

    /**
     * Gets the value of the isOnMissedApproach property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOnMissedApproach() {
        return isOnMissedApproach;
    }

    /**
     * Sets the value of the isOnMissedApproach property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOnMissedApproach(Boolean value) {
        this.isOnMissedApproach = value;
    }

}
