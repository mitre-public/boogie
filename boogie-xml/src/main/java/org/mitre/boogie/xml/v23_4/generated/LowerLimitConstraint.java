
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Lower Limit (5.121) for FIR/UIR, controlled airspace, restrictive airspace and special activity area.
 * 
 * <p>Java class for LowerLimitConstraint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LowerLimitConstraint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.arinc424.com/xml/datatypes}AirspaceRouteHoldAltitude"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isGround" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LowerLimitConstraint", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "isGround"
})
public class LowerLimitConstraint
    extends AirspaceRouteHoldAltitude
{

    @XmlElement(defaultValue = "false")
    protected Boolean isGround;

    /**
     * Gets the value of the isGround property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsGround() {
        return isGround;
    }

    /**
     * Sets the value of the isGround property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsGround(Boolean value) {
        this.isGround = value;
    }

}
