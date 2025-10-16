
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The waypoint usage field is employed to indicate the structure in which the waypoint is utilized.
 * 
 * <p>Java class for WaypointUsage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WaypointUsage"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isHi" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isLo" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isTerminal" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WaypointUsage", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "isHi",
    "isLo",
    "isTerminal"
})
public class WaypointUsage {

    @XmlElement(defaultValue = "false")
    protected Boolean isHi;
    @XmlElement(defaultValue = "false")
    protected Boolean isLo;
    @XmlElement(defaultValue = "false")
    protected Boolean isTerminal;

    /**
     * Gets the value of the isHi property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsHi() {
        return isHi;
    }

    /**
     * Sets the value of the isHi property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsHi(Boolean value) {
        this.isHi = value;
    }

    /**
     * Gets the value of the isLo property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsLo() {
        return isLo;
    }

    /**
     * Sets the value of the isLo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsLo(Boolean value) {
        this.isLo = value;
    }

    /**
     * Gets the value of the isTerminal property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTerminal() {
        return isTerminal;
    }

    /**
     * Sets the value of the isTerminal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTerminal(Boolean value) {
        this.isTerminal = value;
    }

}
