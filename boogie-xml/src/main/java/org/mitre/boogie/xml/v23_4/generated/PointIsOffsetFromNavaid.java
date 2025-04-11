
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 A point on an air refueling route may be
 *                 offset from a navaid. Point to the portion 
 *                 of the XML document containing the complete
 *                 set of information about the navaid, give
 *                 the offset to that navaid. Since offset
 *                 information is often unreliable, provide the
 *                 lat/long location of the point.
 *             
 * 
 * <p>Java class for PointIsOffsetFromNavaid complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PointIsOffsetFromNavaid"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}navaid"/&gt;
 *         &lt;element ref="{}offsetFromNavaid"/&gt;
 *         &lt;element ref="{}location"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PointIsOffsetFromNavaid", propOrder = {
    "navaid",
    "offsetFromNavaid",
    "location"
})
public class PointIsOffsetFromNavaid {

    @XmlElement(required = true)
    protected NavaidRefPlusIdAndType navaid;
    @XmlElement(required = true)
    protected Offset offsetFromNavaid;
    @XmlElement(required = true)
    protected Location location;

    /**
     * Gets the value of the navaid property.
     * 
     * @return
     *     possible object is
     *     {@link NavaidRefPlusIdAndType }
     *     
     */
    public NavaidRefPlusIdAndType getNavaid() {
        return navaid;
    }

    /**
     * Sets the value of the navaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link NavaidRefPlusIdAndType }
     *     
     */
    public void setNavaid(NavaidRefPlusIdAndType value) {
        this.navaid = value;
    }

    /**
     * Gets the value of the offsetFromNavaid property.
     * 
     * @return
     *     possible object is
     *     {@link Offset }
     *     
     */
    public Offset getOffsetFromNavaid() {
        return offsetFromNavaid;
    }

    /**
     * Sets the value of the offsetFromNavaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Offset }
     *     
     */
    public void setOffsetFromNavaid(Offset value) {
        this.offsetFromNavaid = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setLocation(Location value) {
        this.location = value;
    }

}
