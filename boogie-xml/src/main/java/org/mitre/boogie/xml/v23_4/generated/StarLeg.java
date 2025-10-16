
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StarLeg complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StarLeg"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}SidStarLeg"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="verticalAngle" type="{http://www.arinc424.com/xml/datatypes}VerticalAngle" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StarLeg", propOrder = {
    "verticalAngle"
})
public class StarLeg
    extends SidStarLeg
{

    protected BigDecimal verticalAngle;

    /**
     * Gets the value of the verticalAngle property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVerticalAngle() {
        return verticalAngle;
    }

    /**
     * Sets the value of the verticalAngle property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVerticalAngle(BigDecimal value) {
        this.verticalAngle = value;
    }

}
