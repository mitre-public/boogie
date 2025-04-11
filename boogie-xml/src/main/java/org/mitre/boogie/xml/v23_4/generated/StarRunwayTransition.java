
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This element represents a STAR Runway Transition.
 * 
 * <p>Java class for StarRunwayTransition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StarRunwayTransition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}RunwayTransition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isToRunway" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StarRunwayTransition", propOrder = {
    "isToRunway"
})
public class StarRunwayTransition
    extends RunwayTransition
{

    @XmlElement(defaultValue = "false")
    protected Boolean isToRunway;

    /**
     * Gets the value of the isToRunway property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsToRunway() {
        return isToRunway;
    }

    /**
     * Sets the value of the isToRunway property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsToRunway(Boolean value) {
        this.isToRunway = value;
    }

}
