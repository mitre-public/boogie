
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 The location of an air refueling point is specified
 *                 by specifying a navaid (the point is a 
 *                 the navaid), a waypoint (the point is a
 *                 the waypoint), an offset from a navaid, an offset from
 *                 a waypoint, or a lat/long (the point is independent
 *                 of a navaid or waypoint). Use one or more of them 
 *                 to specify the location of a point. Obviously, if 
 *                 the point is a navaid,  then it can't be offset 
 *                 from the navaid (although it could be offset from 
 *                 another navaid).
 *             
 * 
 * <p>Java class for Point complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Point"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}pointDescription" minOccurs="0"/&gt;
 *         &lt;element ref="{}sequenceNumber"/&gt;
 *         &lt;element ref="{}typeOfPoint"/&gt;
 *         &lt;element ref="{}pointIsaNavaid" minOccurs="0"/&gt;
 *         &lt;element ref="{}pointIsaWaypoint" minOccurs="0"/&gt;
 *         &lt;element ref="{}pointIsOffsetFromNavaid" minOccurs="0"/&gt;
 *         &lt;element ref="{}pointIsOffsetFromWaypoint" minOccurs="0"/&gt;
 *         &lt;element ref="{}pointIsIndependentOfNavaidOrWaypoint" minOccurs="0"/&gt;
 *         &lt;element ref="{}footnotes" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Point", propOrder = {
    "pointDescription",
    "sequenceNumber",
    "typeOfPoint",
    "pointIsaNavaid",
    "pointIsaWaypoint",
    "pointIsOffsetFromNavaid",
    "pointIsOffsetFromWaypoint",
    "pointIsIndependentOfNavaidOrWaypoint",
    "footnotes"
})
public class Point {

    protected String pointDescription;
    @XmlSchemaType(name = "unsignedInt")
    protected long sequenceNumber;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected AirRefuelingUsage typeOfPoint;
    protected PointIsaNavaid pointIsaNavaid;
    protected PointIsaWaypoint pointIsaWaypoint;
    protected PointIsOffsetFromNavaid pointIsOffsetFromNavaid;
    protected PointIsOffsetFromWaypoint pointIsOffsetFromWaypoint;
    protected PointIsIndependentOfNavaidOrWaypoint pointIsIndependentOfNavaidOrWaypoint;
    protected FootnotesType footnotes;

    /**
     * Gets the value of the pointDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPointDescription() {
        return pointDescription;
    }

    /**
     * Sets the value of the pointDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPointDescription(String value) {
        this.pointDescription = value;
    }

    /**
     * Gets the value of the sequenceNumber property.
     * 
     */
    public long getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the value of the sequenceNumber property.
     * 
     */
    public void setSequenceNumber(long value) {
        this.sequenceNumber = value;
    }

    /**
     * Gets the value of the typeOfPoint property.
     * 
     * @return
     *     possible object is
     *     {@link AirRefuelingUsage }
     *     
     */
    public AirRefuelingUsage getTypeOfPoint() {
        return typeOfPoint;
    }

    /**
     * Sets the value of the typeOfPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirRefuelingUsage }
     *     
     */
    public void setTypeOfPoint(AirRefuelingUsage value) {
        this.typeOfPoint = value;
    }

    /**
     * Gets the value of the pointIsaNavaid property.
     * 
     * @return
     *     possible object is
     *     {@link PointIsaNavaid }
     *     
     */
    public PointIsaNavaid getPointIsaNavaid() {
        return pointIsaNavaid;
    }

    /**
     * Sets the value of the pointIsaNavaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link PointIsaNavaid }
     *     
     */
    public void setPointIsaNavaid(PointIsaNavaid value) {
        this.pointIsaNavaid = value;
    }

    /**
     * Gets the value of the pointIsaWaypoint property.
     * 
     * @return
     *     possible object is
     *     {@link PointIsaWaypoint }
     *     
     */
    public PointIsaWaypoint getPointIsaWaypoint() {
        return pointIsaWaypoint;
    }

    /**
     * Sets the value of the pointIsaWaypoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link PointIsaWaypoint }
     *     
     */
    public void setPointIsaWaypoint(PointIsaWaypoint value) {
        this.pointIsaWaypoint = value;
    }

    /**
     * Gets the value of the pointIsOffsetFromNavaid property.
     * 
     * @return
     *     possible object is
     *     {@link PointIsOffsetFromNavaid }
     *     
     */
    public PointIsOffsetFromNavaid getPointIsOffsetFromNavaid() {
        return pointIsOffsetFromNavaid;
    }

    /**
     * Sets the value of the pointIsOffsetFromNavaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link PointIsOffsetFromNavaid }
     *     
     */
    public void setPointIsOffsetFromNavaid(PointIsOffsetFromNavaid value) {
        this.pointIsOffsetFromNavaid = value;
    }

    /**
     * Gets the value of the pointIsOffsetFromWaypoint property.
     * 
     * @return
     *     possible object is
     *     {@link PointIsOffsetFromWaypoint }
     *     
     */
    public PointIsOffsetFromWaypoint getPointIsOffsetFromWaypoint() {
        return pointIsOffsetFromWaypoint;
    }

    /**
     * Sets the value of the pointIsOffsetFromWaypoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link PointIsOffsetFromWaypoint }
     *     
     */
    public void setPointIsOffsetFromWaypoint(PointIsOffsetFromWaypoint value) {
        this.pointIsOffsetFromWaypoint = value;
    }

    /**
     * Gets the value of the pointIsIndependentOfNavaidOrWaypoint property.
     * 
     * @return
     *     possible object is
     *     {@link PointIsIndependentOfNavaidOrWaypoint }
     *     
     */
    public PointIsIndependentOfNavaidOrWaypoint getPointIsIndependentOfNavaidOrWaypoint() {
        return pointIsIndependentOfNavaidOrWaypoint;
    }

    /**
     * Sets the value of the pointIsIndependentOfNavaidOrWaypoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link PointIsIndependentOfNavaidOrWaypoint }
     *     
     */
    public void setPointIsIndependentOfNavaidOrWaypoint(PointIsIndependentOfNavaidOrWaypoint value) {
        this.pointIsIndependentOfNavaidOrWaypoint = value;
    }

    /**
     * Gets the value of the footnotes property.
     * 
     * @return
     *     possible object is
     *     {@link FootnotesType }
     *     
     */
    public FootnotesType getFootnotes() {
        return footnotes;
    }

    /**
     * Sets the value of the footnotes property.
     * 
     * @param value
     *     allowed object is
     *     {@link FootnotesType }
     *     
     */
    public void setFootnotes(FootnotesType value) {
        this.footnotes = value;
    }

}
