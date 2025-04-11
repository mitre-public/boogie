
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 A point on an air refueling route may be a navaid.
 *                 Point to the portion of the XML document containing
 *                 the complete set of information about the navaid.
 *             
 * 
 * <p>Java class for PointIsaNavaid complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PointIsaNavaid"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}navaid"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PointIsaNavaid", propOrder = {
    "navaid"
})
public class PointIsaNavaid {

    @XmlElement(required = true)
    protected NavaidRefPlusIdAndType navaid;

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

}
