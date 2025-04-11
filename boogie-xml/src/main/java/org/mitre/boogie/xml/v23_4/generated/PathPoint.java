
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Path Point Primary Record Description
 * 
 * <p>Java class for PathPoint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PathPoint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="runwayNumber"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="3"/&gt;
 *               &lt;minLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="approachPerformanceDesignator" type="{http://www.arinc424.com/xml/enumerations}ApproachPerformanceDesignator"/&gt;
 *         &lt;element name="approachRouteIdentifier" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="approachTypeIdentifier" type="{http://www.arinc424.com/xml/datatypes}ApproachTypeIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="courseWidthAtThreshold" type="{http://www.arinc424.com/xml/datatypes}CourseWidthAtThreshold"/&gt;
 *         &lt;element name="fasDataCrcRemainder" type="{http://www.arinc424.com/xml/datatypes}CrcRemainder"/&gt;
 *         &lt;element name="flightPathAlignmentPoint" type="{http://www.arinc424.com/xml/datatypes}HighPrecisionLocation"/&gt;
 *         &lt;element name="fpapEllipsoidHeight" type="{http://www.arinc424.com/xml/datatypes}EllipsoidHeight" minOccurs="0"/&gt;
 *         &lt;element name="fpapOrthometricHeight" type="{http://www.arinc424.com/xml/datatypes}OrthometricHeight" minOccurs="0"/&gt;
 *         &lt;element name="glidePathAngle" type="{http://www.arinc424.com/xml/datatypes}GlidePathAngle"/&gt;
 *         &lt;element name="gnssChannelNumber" type="{http://www.arinc424.com/xml/datatypes}Channel" minOccurs="0"/&gt;
 *         &lt;element name="helicopterProcedureCourse" type="{http://www.arinc424.com/xml/datatypes}CourseValue" minOccurs="0"/&gt;
 *         &lt;element name="landingThresholdPoint" type="{http://www.arinc424.com/xml/datatypes}HighPrecisionLocation"/&gt;
 *         &lt;element name="lengthOffset" type="{http://www.arinc424.com/xml/datatypes}LengthOffset" minOccurs="0"/&gt;
 *         &lt;element name="ltpEllipsoidHeight" type="{http://www.arinc424.com/xml/datatypes}EllipsoidHeight"/&gt;
 *         &lt;element name="ltpOrthometricHeight" type="{http://www.arinc424.com/xml/datatypes}OrthometricHeight" minOccurs="0"/&gt;
 *         &lt;element name="pathPointTch" type="{http://www.arinc424.com/xml/datatypes}PathPointTch"/&gt;
 *         &lt;element name="referencePathDataSelector" type="{http://www.arinc424.com/xml/datatypes}ReferencePathDataSelector"/&gt;
 *         &lt;element name="referencePathIdentifier" type="{http://www.arinc424.com/xml/datatypes}ReferencePathIdentifier"/&gt;
 *         &lt;element name="routeIndicator" type="{http://www.arinc424.com/xml/datatypes}RouteIndicator" minOccurs="0"/&gt;
 *         &lt;element name="tchUnitsIndicator" type="{http://www.arinc424.com/xml/enumerations}HeightUnitsIndicator"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PathPoint", propOrder = {
    "runwayNumber",
    "approachPerformanceDesignator",
    "approachRouteIdentifier",
    "approachTypeIdentifier",
    "courseWidthAtThreshold",
    "fasDataCrcRemainder",
    "flightPathAlignmentPoint",
    "fpapEllipsoidHeight",
    "fpapOrthometricHeight",
    "glidePathAngle",
    "gnssChannelNumber",
    "helicopterProcedureCourse",
    "landingThresholdPoint",
    "lengthOffset",
    "ltpEllipsoidHeight",
    "ltpOrthometricHeight",
    "pathPointTch",
    "referencePathDataSelector",
    "referencePathIdentifier",
    "routeIndicator",
    "tchUnitsIndicator"
})
@XmlSeeAlso({
    GbasPathPoint.class,
    SbasPathPoint.class
})
public class PathPoint
    extends A424Record
{

    @XmlElement(required = true)
    protected String runwayNumber;
    @XmlElement(required = true)
    protected String approachPerformanceDesignator;
    protected String approachRouteIdentifier;
    protected String approachTypeIdentifier;
    @XmlElement(required = true)
    protected BigDecimal courseWidthAtThreshold;
    @XmlElement(required = true)
    protected String fasDataCrcRemainder;
    @XmlElement(required = true)
    protected HighPrecisionLocation flightPathAlignmentPoint;
    protected BigDecimal fpapEllipsoidHeight;
    protected BigDecimal fpapOrthometricHeight;
    @XmlElement(required = true)
    protected BigDecimal glidePathAngle;
    @XmlSchemaType(name = "unsignedInt")
    protected Long gnssChannelNumber;
    protected BigDecimal helicopterProcedureCourse;
    @XmlElement(required = true)
    protected HighPrecisionLocation landingThresholdPoint;
    @XmlSchemaType(name = "unsignedInt")
    protected Long lengthOffset;
    @XmlElement(required = true)
    protected BigDecimal ltpEllipsoidHeight;
    protected BigDecimal ltpOrthometricHeight;
    @XmlElement(required = true)
    protected BigDecimal pathPointTch;
    @XmlSchemaType(name = "unsignedInt")
    protected long referencePathDataSelector;
    @XmlElement(required = true)
    protected String referencePathIdentifier;
    protected String routeIndicator;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected HeightUnitsIndicator tchUnitsIndicator;

    /**
     * Gets the value of the runwayNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRunwayNumber() {
        return runwayNumber;
    }

    /**
     * Sets the value of the runwayNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRunwayNumber(String value) {
        this.runwayNumber = value;
    }

    /**
     * Gets the value of the approachPerformanceDesignator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApproachPerformanceDesignator() {
        return approachPerformanceDesignator;
    }

    /**
     * Sets the value of the approachPerformanceDesignator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApproachPerformanceDesignator(String value) {
        this.approachPerformanceDesignator = value;
    }

    /**
     * Gets the value of the approachRouteIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApproachRouteIdentifier() {
        return approachRouteIdentifier;
    }

    /**
     * Sets the value of the approachRouteIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApproachRouteIdentifier(String value) {
        this.approachRouteIdentifier = value;
    }

    /**
     * Gets the value of the approachTypeIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApproachTypeIdentifier() {
        return approachTypeIdentifier;
    }

    /**
     * Sets the value of the approachTypeIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApproachTypeIdentifier(String value) {
        this.approachTypeIdentifier = value;
    }

    /**
     * Gets the value of the courseWidthAtThreshold property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCourseWidthAtThreshold() {
        return courseWidthAtThreshold;
    }

    /**
     * Sets the value of the courseWidthAtThreshold property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCourseWidthAtThreshold(BigDecimal value) {
        this.courseWidthAtThreshold = value;
    }

    /**
     * Gets the value of the fasDataCrcRemainder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFasDataCrcRemainder() {
        return fasDataCrcRemainder;
    }

    /**
     * Sets the value of the fasDataCrcRemainder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFasDataCrcRemainder(String value) {
        this.fasDataCrcRemainder = value;
    }

    /**
     * Gets the value of the flightPathAlignmentPoint property.
     * 
     * @return
     *     possible object is
     *     {@link HighPrecisionLocation }
     *     
     */
    public HighPrecisionLocation getFlightPathAlignmentPoint() {
        return flightPathAlignmentPoint;
    }

    /**
     * Sets the value of the flightPathAlignmentPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link HighPrecisionLocation }
     *     
     */
    public void setFlightPathAlignmentPoint(HighPrecisionLocation value) {
        this.flightPathAlignmentPoint = value;
    }

    /**
     * Gets the value of the fpapEllipsoidHeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFpapEllipsoidHeight() {
        return fpapEllipsoidHeight;
    }

    /**
     * Sets the value of the fpapEllipsoidHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFpapEllipsoidHeight(BigDecimal value) {
        this.fpapEllipsoidHeight = value;
    }

    /**
     * Gets the value of the fpapOrthometricHeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFpapOrthometricHeight() {
        return fpapOrthometricHeight;
    }

    /**
     * Sets the value of the fpapOrthometricHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFpapOrthometricHeight(BigDecimal value) {
        this.fpapOrthometricHeight = value;
    }

    /**
     * Gets the value of the glidePathAngle property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGlidePathAngle() {
        return glidePathAngle;
    }

    /**
     * Sets the value of the glidePathAngle property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGlidePathAngle(BigDecimal value) {
        this.glidePathAngle = value;
    }

    /**
     * Gets the value of the gnssChannelNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getGnssChannelNumber() {
        return gnssChannelNumber;
    }

    /**
     * Sets the value of the gnssChannelNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setGnssChannelNumber(Long value) {
        this.gnssChannelNumber = value;
    }

    /**
     * Gets the value of the helicopterProcedureCourse property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHelicopterProcedureCourse() {
        return helicopterProcedureCourse;
    }

    /**
     * Sets the value of the helicopterProcedureCourse property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHelicopterProcedureCourse(BigDecimal value) {
        this.helicopterProcedureCourse = value;
    }

    /**
     * Gets the value of the landingThresholdPoint property.
     * 
     * @return
     *     possible object is
     *     {@link HighPrecisionLocation }
     *     
     */
    public HighPrecisionLocation getLandingThresholdPoint() {
        return landingThresholdPoint;
    }

    /**
     * Sets the value of the landingThresholdPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link HighPrecisionLocation }
     *     
     */
    public void setLandingThresholdPoint(HighPrecisionLocation value) {
        this.landingThresholdPoint = value;
    }

    /**
     * Gets the value of the lengthOffset property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLengthOffset() {
        return lengthOffset;
    }

    /**
     * Sets the value of the lengthOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLengthOffset(Long value) {
        this.lengthOffset = value;
    }

    /**
     * Gets the value of the ltpEllipsoidHeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLtpEllipsoidHeight() {
        return ltpEllipsoidHeight;
    }

    /**
     * Sets the value of the ltpEllipsoidHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLtpEllipsoidHeight(BigDecimal value) {
        this.ltpEllipsoidHeight = value;
    }

    /**
     * Gets the value of the ltpOrthometricHeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLtpOrthometricHeight() {
        return ltpOrthometricHeight;
    }

    /**
     * Sets the value of the ltpOrthometricHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLtpOrthometricHeight(BigDecimal value) {
        this.ltpOrthometricHeight = value;
    }

    /**
     * Gets the value of the pathPointTch property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPathPointTch() {
        return pathPointTch;
    }

    /**
     * Sets the value of the pathPointTch property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPathPointTch(BigDecimal value) {
        this.pathPointTch = value;
    }

    /**
     * Gets the value of the referencePathDataSelector property.
     * 
     */
    public long getReferencePathDataSelector() {
        return referencePathDataSelector;
    }

    /**
     * Sets the value of the referencePathDataSelector property.
     * 
     */
    public void setReferencePathDataSelector(long value) {
        this.referencePathDataSelector = value;
    }

    /**
     * Gets the value of the referencePathIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferencePathIdentifier() {
        return referencePathIdentifier;
    }

    /**
     * Sets the value of the referencePathIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferencePathIdentifier(String value) {
        this.referencePathIdentifier = value;
    }

    /**
     * Gets the value of the routeIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRouteIndicator() {
        return routeIndicator;
    }

    /**
     * Sets the value of the routeIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRouteIndicator(String value) {
        this.routeIndicator = value;
    }

    /**
     * Gets the value of the tchUnitsIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link HeightUnitsIndicator }
     *     
     */
    public HeightUnitsIndicator getTchUnitsIndicator() {
        return tchUnitsIndicator;
    }

    /**
     * Sets the value of the tchUnitsIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeightUnitsIndicator }
     *     
     */
    public void setTchUnitsIndicator(HeightUnitsIndicator value) {
        this.tchUnitsIndicator = value;
    }

}
