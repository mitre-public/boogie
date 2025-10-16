
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 An ATC specifies the frequency for contacting it
 *                 and whether the frequency applies in the east
 *                 direction, west direction, or both.
 *             
 * 
 * <p>Java class for FrequencyPlusDirection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FrequencyPlusDirection"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;&gt;ATCFrequency"&gt;
 *       &lt;attribute name="direction" type="{}FrequencyEastWest" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FrequencyPlusDirection")
public class FrequencyPlusDirection
    extends ATCFrequency
{

    @XmlAttribute(name = "direction")
    protected FrequencyEastWest direction;

    /**
     * Gets the value of the direction property.
     * 
     * @return
     *     possible object is
     *     {@link FrequencyEastWest }
     *     
     */
    public FrequencyEastWest getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     * 
     * @param value
     *     allowed object is
     *     {@link FrequencyEastWest }
     *     
     */
    public void setDirection(FrequencyEastWest value) {
        this.direction = value;
    }

}
