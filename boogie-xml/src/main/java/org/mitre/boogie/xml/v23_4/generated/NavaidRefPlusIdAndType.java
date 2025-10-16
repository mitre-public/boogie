
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 AN IDREF to a navaid, the ID of the
 *                 referenced navaid, and a string description
 *                 (vor, vortac, tacan, etc.) of the referenced navaid
 *             
 * 
 * <p>Java class for NavaidRefPlusIdAndType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NavaidRefPlusIdAndType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}navaidRef"/&gt;
 *         &lt;element ref="{}navID"/&gt;
 *         &lt;element ref="{}navType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NavaidRefPlusIdAndType", propOrder = {
    "navaidRef",
    "navID",
    "navType"
})
public class NavaidRefPlusIdAndType {

    @XmlElement(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object navaidRef;
    @XmlElement(required = true)
    protected String navID;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected NavaidType navType;

    /**
     * Gets the value of the navaidRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getNavaidRef() {
        return navaidRef;
    }

    /**
     * Sets the value of the navaidRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setNavaidRef(Object value) {
        this.navaidRef = value;
    }

    /**
     * Gets the value of the navID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNavID() {
        return navID;
    }

    /**
     * Sets the value of the navID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNavID(String value) {
        this.navID = value;
    }

    /**
     * Gets the value of the navType property.
     * 
     * @return
     *     possible object is
     *     {@link NavaidType }
     *     
     */
    public NavaidType getNavType() {
        return navType;
    }

    /**
     * Sets the value of the navType property.
     * 
     * @param value
     *     allowed object is
     *     {@link NavaidType }
     *     
     */
    public void setNavType(NavaidType value) {
        this.navType = value;
    }

}
