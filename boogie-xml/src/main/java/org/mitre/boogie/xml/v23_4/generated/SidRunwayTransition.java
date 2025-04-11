
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This element represents a SID runway transition.
 * 
 * <p>Java class for SidRunwayTransition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SidRunwayTransition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}RunwayTransition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isFromRunway" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SidRunwayTransition", propOrder = {
    "isFromRunway"
})
public class SidRunwayTransition
    extends RunwayTransition
{

    @XmlElement(defaultValue = "false")
    protected Boolean isFromRunway;

    /**
     * Gets the value of the isFromRunway property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFromRunway() {
        return isFromRunway;
    }

    /**
     * Sets the value of the isFromRunway property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFromRunway(Boolean value) {
        this.isFromRunway = value;
    }

}
