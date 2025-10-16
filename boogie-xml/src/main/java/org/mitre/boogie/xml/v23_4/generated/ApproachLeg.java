
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApproachLeg complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApproachLeg"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}ProcedureLeg"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="approachWaypointDescription" type="{http://www.arinc424.com/xml/datatypes}ApproachWaypointDescription" minOccurs="0"/&gt;
 *         &lt;element name="verticalAngle" type="{http://www.arinc424.com/xml/datatypes}VerticalAngle" minOccurs="0"/&gt;
 *         &lt;element name="glideSlopeCrossingAltitude" type="{http://www.arinc424.com/xml/datatypes}AltitudeValue" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApproachLeg", propOrder = {
    "approachWaypointDescription",
    "verticalAngle",
    "glideSlopeCrossingAltitude"
})
public class ApproachLeg
    extends ProcedureLeg
{

    protected ApproachWaypointDescription approachWaypointDescription;
    protected BigDecimal verticalAngle;
    @XmlSchemaType(name = "integer")
    protected Integer glideSlopeCrossingAltitude;

    /**
     * Gets the value of the approachWaypointDescription property.
     * 
     * @return
     *     possible object is
     *     {@link ApproachWaypointDescription }
     *     
     */
    public ApproachWaypointDescription getApproachWaypointDescription() {
        return approachWaypointDescription;
    }

    /**
     * Sets the value of the approachWaypointDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApproachWaypointDescription }
     *     
     */
    public void setApproachWaypointDescription(ApproachWaypointDescription value) {
        this.approachWaypointDescription = value;
    }

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

    /**
     * Gets the value of the glideSlopeCrossingAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getGlideSlopeCrossingAltitude() {
        return glideSlopeCrossingAltitude;
    }

    /**
     * Sets the value of the glideSlopeCrossingAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setGlideSlopeCrossingAltitude(Integer value) {
        this.glideSlopeCrossingAltitude = value;
    }

}
