
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.Duration;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Holding Pattern Records.
 * 
 * <p>Java class for HoldingPattern complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HoldingPattern"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isEnroute" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="arcRadius" type="{http://www.arinc424.com/xml/datatypes}ArcRadius" minOccurs="0"/&gt;
 *         &lt;element name="holdingPatternName" type="{http://www.arinc424.com/xml/datatypes}Name" minOccurs="0"/&gt;
 *         &lt;element name="holdingSpeed" type="{http://www.arinc424.com/xml/datatypes}HoldingSpeed" minOccurs="0"/&gt;
 *         &lt;element name="holdingTime" type="{http://www.w3.org/2001/XMLSchema}duration" minOccurs="0"/&gt;
 *         &lt;element name="holdingDistance" type="{http://www.arinc424.com/xml/datatypes}HoldingLegLength" minOccurs="0"/&gt;
 *         &lt;element name="inboundHoldingCourse" type="{http://www.arinc424.com/xml/datatypes}Course" minOccurs="0"/&gt;
 *         &lt;element name="legInboundOutboundIndicator" type="{http://www.arinc424.com/xml/enumerations}LegInboundOutboundIndicator" minOccurs="0"/&gt;
 *         &lt;element name="holdMinMaxAltitudes" type="{http://www.arinc424.com/xml/datatypes}HoldRvsmMinimumMaximumAltitudeConstraint" minOccurs="0"/&gt;
 *         &lt;element name="rnp" type="{http://www.arinc424.com/xml/datatypes}RequiredNavigationPerformance" minOccurs="0"/&gt;
 *         &lt;element name="rvsmMinMaxLevels" type="{http://www.arinc424.com/xml/datatypes}HoldRvsmMinimumMaximumAltitudeConstraint" minOccurs="0"/&gt;
 *         &lt;element name="turnDirection" type="{http://www.arinc424.com/xml/enumerations}Turn" minOccurs="0"/&gt;
 *         &lt;element name="verticalScaleFactor" type="{http://www.arinc424.com/xml/datatypes}VerticalScaleFactor" minOccurs="0"/&gt;
 *         &lt;element name="fixIdentifier" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="fixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="portRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="holdingUses" type="{http://www.arinc424.com/xml/datatypes}HoldingUses" minOccurs="0"/&gt;
 *         &lt;element name="multipleIndicator" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="inboundCourseNavaid" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="inboundCourseTheta" type="{http://www.arinc424.com/xml/datatypes}Theta" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HoldingPattern", propOrder = {
    "isEnroute",
    "arcRadius",
    "holdingPatternName",
    "holdingSpeed",
    "holdingTime",
    "holdingDistance",
    "inboundHoldingCourse",
    "legInboundOutboundIndicator",
    "holdMinMaxAltitudes",
    "rnp",
    "rvsmMinMaxLevels",
    "turnDirection",
    "verticalScaleFactor",
    "fixIdentifier",
    "fixRef",
    "portRef",
    "holdingUses",
    "multipleIndicator",
    "inboundCourseNavaid",
    "inboundCourseTheta"
})
public class HoldingPattern
    extends A424Record
{

    @XmlElement(defaultValue = "false")
    protected Boolean isEnroute;
    protected BigDecimal arcRadius;
    protected String holdingPatternName;
    @XmlSchemaType(name = "unsignedInt")
    protected Long holdingSpeed;
    protected Duration holdingTime;
    protected BigDecimal holdingDistance;
    protected Course inboundHoldingCourse;
    @XmlSchemaType(name = "string")
    protected LegInboundOutboundIndicator legInboundOutboundIndicator;
    protected HoldRvsmMinimumMaximumAltitudeConstraint holdMinMaxAltitudes;
    protected BigDecimal rnp;
    protected HoldRvsmMinimumMaximumAltitudeConstraint rvsmMinMaxLevels;
    @XmlSchemaType(name = "string")
    protected Turn turnDirection;
    @XmlSchemaType(name = "unsignedInt")
    protected Long verticalScaleFactor;
    protected String fixIdentifier;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object fixRef;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object portRef;
    protected HoldingUses holdingUses;
    protected BigInteger multipleIndicator;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object inboundCourseNavaid;
    protected BigDecimal inboundCourseTheta;

    /**
     * Gets the value of the isEnroute property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsEnroute() {
        return isEnroute;
    }

    /**
     * Sets the value of the isEnroute property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsEnroute(Boolean value) {
        this.isEnroute = value;
    }

    /**
     * Gets the value of the arcRadius property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getArcRadius() {
        return arcRadius;
    }

    /**
     * Sets the value of the arcRadius property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setArcRadius(BigDecimal value) {
        this.arcRadius = value;
    }

    /**
     * Gets the value of the holdingPatternName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHoldingPatternName() {
        return holdingPatternName;
    }

    /**
     * Sets the value of the holdingPatternName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHoldingPatternName(String value) {
        this.holdingPatternName = value;
    }

    /**
     * Gets the value of the holdingSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getHoldingSpeed() {
        return holdingSpeed;
    }

    /**
     * Sets the value of the holdingSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHoldingSpeed(Long value) {
        this.holdingSpeed = value;
    }

    /**
     * Gets the value of the holdingTime property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getHoldingTime() {
        return holdingTime;
    }

    /**
     * Sets the value of the holdingTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setHoldingTime(Duration value) {
        this.holdingTime = value;
    }

    /**
     * Gets the value of the holdingDistance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHoldingDistance() {
        return holdingDistance;
    }

    /**
     * Sets the value of the holdingDistance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHoldingDistance(BigDecimal value) {
        this.holdingDistance = value;
    }

    /**
     * Gets the value of the inboundHoldingCourse property.
     * 
     * @return
     *     possible object is
     *     {@link Course }
     *     
     */
    public Course getInboundHoldingCourse() {
        return inboundHoldingCourse;
    }

    /**
     * Sets the value of the inboundHoldingCourse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Course }
     *     
     */
    public void setInboundHoldingCourse(Course value) {
        this.inboundHoldingCourse = value;
    }

    /**
     * Gets the value of the legInboundOutboundIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link LegInboundOutboundIndicator }
     *     
     */
    public LegInboundOutboundIndicator getLegInboundOutboundIndicator() {
        return legInboundOutboundIndicator;
    }

    /**
     * Sets the value of the legInboundOutboundIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link LegInboundOutboundIndicator }
     *     
     */
    public void setLegInboundOutboundIndicator(LegInboundOutboundIndicator value) {
        this.legInboundOutboundIndicator = value;
    }

    /**
     * Gets the value of the holdMinMaxAltitudes property.
     * 
     * @return
     *     possible object is
     *     {@link HoldRvsmMinimumMaximumAltitudeConstraint }
     *     
     */
    public HoldRvsmMinimumMaximumAltitudeConstraint getHoldMinMaxAltitudes() {
        return holdMinMaxAltitudes;
    }

    /**
     * Sets the value of the holdMinMaxAltitudes property.
     * 
     * @param value
     *     allowed object is
     *     {@link HoldRvsmMinimumMaximumAltitudeConstraint }
     *     
     */
    public void setHoldMinMaxAltitudes(HoldRvsmMinimumMaximumAltitudeConstraint value) {
        this.holdMinMaxAltitudes = value;
    }

    /**
     * Gets the value of the rnp property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRnp() {
        return rnp;
    }

    /**
     * Sets the value of the rnp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRnp(BigDecimal value) {
        this.rnp = value;
    }

    /**
     * Gets the value of the rvsmMinMaxLevels property.
     * 
     * @return
     *     possible object is
     *     {@link HoldRvsmMinimumMaximumAltitudeConstraint }
     *     
     */
    public HoldRvsmMinimumMaximumAltitudeConstraint getRvsmMinMaxLevels() {
        return rvsmMinMaxLevels;
    }

    /**
     * Sets the value of the rvsmMinMaxLevels property.
     * 
     * @param value
     *     allowed object is
     *     {@link HoldRvsmMinimumMaximumAltitudeConstraint }
     *     
     */
    public void setRvsmMinMaxLevels(HoldRvsmMinimumMaximumAltitudeConstraint value) {
        this.rvsmMinMaxLevels = value;
    }

    /**
     * Gets the value of the turnDirection property.
     * 
     * @return
     *     possible object is
     *     {@link Turn }
     *     
     */
    public Turn getTurnDirection() {
        return turnDirection;
    }

    /**
     * Sets the value of the turnDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link Turn }
     *     
     */
    public void setTurnDirection(Turn value) {
        this.turnDirection = value;
    }

    /**
     * Gets the value of the verticalScaleFactor property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getVerticalScaleFactor() {
        return verticalScaleFactor;
    }

    /**
     * Sets the value of the verticalScaleFactor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setVerticalScaleFactor(Long value) {
        this.verticalScaleFactor = value;
    }

    /**
     * Gets the value of the fixIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFixIdentifier() {
        return fixIdentifier;
    }

    /**
     * Sets the value of the fixIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFixIdentifier(String value) {
        this.fixIdentifier = value;
    }

    /**
     * Gets the value of the fixRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFixRef() {
        return fixRef;
    }

    /**
     * Sets the value of the fixRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFixRef(Object value) {
        this.fixRef = value;
    }

    /**
     * Gets the value of the portRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getPortRef() {
        return portRef;
    }

    /**
     * Sets the value of the portRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setPortRef(Object value) {
        this.portRef = value;
    }

    /**
     * Gets the value of the holdingUses property.
     * 
     * @return
     *     possible object is
     *     {@link HoldingUses }
     *     
     */
    public HoldingUses getHoldingUses() {
        return holdingUses;
    }

    /**
     * Sets the value of the holdingUses property.
     * 
     * @param value
     *     allowed object is
     *     {@link HoldingUses }
     *     
     */
    public void setHoldingUses(HoldingUses value) {
        this.holdingUses = value;
    }

    /**
     * Gets the value of the multipleIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMultipleIndicator() {
        return multipleIndicator;
    }

    /**
     * Sets the value of the multipleIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMultipleIndicator(BigInteger value) {
        this.multipleIndicator = value;
    }

    /**
     * Gets the value of the inboundCourseNavaid property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getInboundCourseNavaid() {
        return inboundCourseNavaid;
    }

    /**
     * Sets the value of the inboundCourseNavaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setInboundCourseNavaid(Object value) {
        this.inboundCourseNavaid = value;
    }

    /**
     * Gets the value of the inboundCourseTheta property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getInboundCourseTheta() {
        return inboundCourseTheta;
    }

    /**
     * Sets the value of the inboundCourseTheta property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setInboundCourseTheta(BigDecimal value) {
        this.inboundCourseTheta = value;
    }

}
