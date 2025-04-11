
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The “Leg Type Code” field used on Flight Planning Arrival/Departure Data Records is a simplification of the Path Terminator concept. It will provide the information on the path between intermediate waypoints as straight or curved and provide an indication of the change in direction of flight, expressed as left or right, at an intermediate waypoint.
 * 
 * <p>Java class for LegTypeCode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LegTypeCode"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.arinc424.com/xml/datatypes}Alpha"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="legTypeCodeSC" type="{http://www.arinc424.com/xml/enumerations}LegTypeCodeSC"/&gt;
 *         &lt;element name="legTypeTurnIndication" type="{http://www.arinc424.com/xml/enumerations}LegTypeTurnIndication"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LegTypeCode", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "legTypeCodeSC",
    "legTypeTurnIndication"
})
public class LegTypeCode
    extends Alpha
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected LegTypeCodeSC legTypeCodeSC;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected LegTypeTurnIndication legTypeTurnIndication;

    /**
     * Gets the value of the legTypeCodeSC property.
     * 
     * @return
     *     possible object is
     *     {@link LegTypeCodeSC }
     *     
     */
    public LegTypeCodeSC getLegTypeCodeSC() {
        return legTypeCodeSC;
    }

    /**
     * Sets the value of the legTypeCodeSC property.
     * 
     * @param value
     *     allowed object is
     *     {@link LegTypeCodeSC }
     *     
     */
    public void setLegTypeCodeSC(LegTypeCodeSC value) {
        this.legTypeCodeSC = value;
    }

    /**
     * Gets the value of the legTypeTurnIndication property.
     * 
     * @return
     *     possible object is
     *     {@link LegTypeTurnIndication }
     *     
     */
    public LegTypeTurnIndication getLegTypeTurnIndication() {
        return legTypeTurnIndication;
    }

    /**
     * Sets the value of the legTypeTurnIndication property.
     * 
     * @param value
     *     allowed object is
     *     {@link LegTypeTurnIndication }
     *     
     */
    public void setLegTypeTurnIndication(LegTypeTurnIndication value) {
        this.legTypeTurnIndication = value;
    }

}
