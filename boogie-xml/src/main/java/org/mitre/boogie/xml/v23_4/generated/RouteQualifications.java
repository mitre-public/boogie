
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This element provides boolean expressions describing qualifications of the route.  The qualifications in this element are common to different types of routes
 * 
 * <p>Java class for RouteQualifications complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RouteQualifications"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isDmeReq" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isGnssReq" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isRadarReq" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFmsReq" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isConventional" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RouteQualifications", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "isDmeReq",
    "isGnssReq",
    "isRadarReq",
    "isFmsReq",
    "isConventional"
})
public class RouteQualifications {

    @XmlElement(defaultValue = "false")
    protected Boolean isDmeReq;
    @XmlElement(defaultValue = "false")
    protected Boolean isGnssReq;
    @XmlElement(defaultValue = "false")
    protected Boolean isRadarReq;
    @XmlElement(defaultValue = "false")
    protected Boolean isFmsReq;
    @XmlElement(defaultValue = "false")
    protected Boolean isConventional;

    /**
     * Gets the value of the isDmeReq property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDmeReq() {
        return isDmeReq;
    }

    /**
     * Sets the value of the isDmeReq property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDmeReq(Boolean value) {
        this.isDmeReq = value;
    }

    /**
     * Gets the value of the isGnssReq property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsGnssReq() {
        return isGnssReq;
    }

    /**
     * Sets the value of the isGnssReq property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsGnssReq(Boolean value) {
        this.isGnssReq = value;
    }

    /**
     * Gets the value of the isRadarReq property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRadarReq() {
        return isRadarReq;
    }

    /**
     * Sets the value of the isRadarReq property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRadarReq(Boolean value) {
        this.isRadarReq = value;
    }

    /**
     * Gets the value of the isFmsReq property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFmsReq() {
        return isFmsReq;
    }

    /**
     * Sets the value of the isFmsReq property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFmsReq(Boolean value) {
        this.isFmsReq = value;
    }

    /**
     * Gets the value of the isConventional property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsConventional() {
        return isConventional;
    }

    /**
     * Sets the value of the isConventional property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsConventional(Boolean value) {
        this.isConventional = value;
    }

}
