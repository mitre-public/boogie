
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import javax.xml.datatype.Duration;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class is an abstraction of Procedure Leg. all the details pertaining to a Procedure leg are captured here.
 * 
 * <p>Java class for ProcedureLeg complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProcedureLeg"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Leg"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="arcRadius" type="{http://www.arinc424.com/xml/datatypes}ArcRadius" minOccurs="0"/&gt;
 *         &lt;element name="atcIndicator" type="{http://www.arinc424.com/xml/enumerations}AtcIndicator" minOccurs="0"/&gt;
 *         &lt;element name="distance" type="{http://www.arinc424.com/xml/datatypes}LegDistance" minOccurs="0"/&gt;
 *         &lt;element name="holdTime" type="{http://www.w3.org/2001/XMLSchema}duration" minOccurs="0"/&gt;
 *         &lt;element name="waypointDescriptor" type="{http://www.arinc424.com/xml/datatypes}ProcedureWaypointDescription" minOccurs="0"/&gt;
 *         &lt;element name="legDistance" type="{http://www.arinc424.com/xml/datatypes}LegDistance" minOccurs="0"/&gt;
 *         &lt;element name="legInboundIndicator" type="{http://www.arinc424.com/xml/datatypes}LegInboundIndicator" minOccurs="0"/&gt;
 *         &lt;element name="legInboundOutboundIndicator" type="{http://www.arinc424.com/xml/enumerations}LegInboundOutboundIndicator" minOccurs="0"/&gt;
 *         &lt;element name="pathAndTermination" type="{http://www.arinc424.com/xml/enumerations}PathAndTermination"/&gt;
 *         &lt;element name="procedureDesignMagVar" type="{http://www.arinc424.com/xml/datatypes}MagneticVariation" minOccurs="0"/&gt;
 *         &lt;element name="rho" type="{http://www.arinc424.com/xml/datatypes}Rho" minOccurs="0"/&gt;
 *         &lt;element name="rnp" type="{http://www.arinc424.com/xml/datatypes}RequiredNavigationPerformance" minOccurs="0"/&gt;
 *         &lt;element name="speedLimit" type="{http://www.arinc424.com/xml/datatypes}SpeedLimit" minOccurs="0"/&gt;
 *         &lt;element name="theta" type="{http://www.arinc424.com/xml/datatypes}Theta" minOccurs="0"/&gt;
 *         &lt;element name="transitionsAltitudeLevel" type="{http://www.arinc424.com/xml/datatypes}TransitionAltitude" minOccurs="0"/&gt;
 *         &lt;element name="turnDirection" type="{http://www.arinc424.com/xml/enumerations}TurnDirection" minOccurs="0"/&gt;
 *         &lt;element name="isTurnDirectionValid" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="verticalScaleFactor" type="{http://www.arinc424.com/xml/datatypes}VerticalScaleFactor" minOccurs="0"/&gt;
 *         &lt;element name="course" type="{http://www.arinc424.com/xml/datatypes}Course" minOccurs="0"/&gt;
 *         &lt;element name="centerFix" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="centerFixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="altitudeConstraint" type="{http://www.arinc424.com/xml/datatypes}AltitudeConstraint" minOccurs="0"/&gt;
 *         &lt;element name="altitudeTermination" type="{http://www.arinc424.com/xml/datatypes}AltitudeTermination" minOccurs="0"/&gt;
 *         &lt;element name="raceTrackAltitude" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="atOrAbove" type="{http://www.arinc424.com/xml/datatypes}Constraint"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcedureLeg", propOrder = {
    "arcRadius",
    "atcIndicator",
    "distance",
    "holdTime",
    "waypointDescriptor",
    "legDistance",
    "legInboundIndicator",
    "legInboundOutboundIndicator",
    "pathAndTermination",
    "procedureDesignMagVar",
    "rho",
    "rnp",
    "speedLimit",
    "theta",
    "transitionsAltitudeLevel",
    "turnDirection",
    "isTurnDirectionValid",
    "verticalScaleFactor",
    "course",
    "centerFix",
    "centerFixRef",
    "altitudeConstraint",
    "altitudeTermination",
    "raceTrackAltitude"
})
@XmlSeeAlso({
    ApproachLeg.class,
    SidStarLeg.class
})
public abstract class ProcedureLeg
    extends Leg
{

    protected BigDecimal arcRadius;
    @XmlSchemaType(name = "string")
    protected AtcIndicator atcIndicator;
    protected BigDecimal distance;
    protected Duration holdTime;
    protected ProcedureWaypointDescription waypointDescriptor;
    protected BigDecimal legDistance;
    protected String legInboundIndicator;
    @XmlSchemaType(name = "string")
    protected LegInboundOutboundIndicator legInboundOutboundIndicator;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected PathAndTermination pathAndTermination;
    protected MagneticVariation procedureDesignMagVar;
    protected BigDecimal rho;
    protected BigDecimal rnp;
    protected SpeedLimit speedLimit;
    protected BigDecimal theta;
    @XmlSchemaType(name = "integer")
    protected Integer transitionsAltitudeLevel;
    @XmlSchemaType(name = "string")
    protected TurnDirection turnDirection;
    @XmlElement(defaultValue = "false")
    protected Boolean isTurnDirectionValid;
    @XmlSchemaType(name = "unsignedInt")
    protected Long verticalScaleFactor;
    protected Course course;
    protected String centerFix;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object centerFixRef;
    protected AltitudeConstraint altitudeConstraint;
    protected AltitudeTermination altitudeTermination;
    protected ProcedureLeg.RaceTrackAltitude raceTrackAltitude;

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
     * Gets the value of the atcIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link AtcIndicator }
     *     
     */
    public AtcIndicator getAtcIndicator() {
        return atcIndicator;
    }

    /**
     * Sets the value of the atcIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link AtcIndicator }
     *     
     */
    public void setAtcIndicator(AtcIndicator value) {
        this.atcIndicator = value;
    }

    /**
     * Gets the value of the distance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDistance() {
        return distance;
    }

    /**
     * Sets the value of the distance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDistance(BigDecimal value) {
        this.distance = value;
    }

    /**
     * Gets the value of the holdTime property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getHoldTime() {
        return holdTime;
    }

    /**
     * Sets the value of the holdTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setHoldTime(Duration value) {
        this.holdTime = value;
    }

    /**
     * Gets the value of the waypointDescriptor property.
     * 
     * @return
     *     possible object is
     *     {@link ProcedureWaypointDescription }
     *     
     */
    public ProcedureWaypointDescription getWaypointDescriptor() {
        return waypointDescriptor;
    }

    /**
     * Sets the value of the waypointDescriptor property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedureWaypointDescription }
     *     
     */
    public void setWaypointDescriptor(ProcedureWaypointDescription value) {
        this.waypointDescriptor = value;
    }

    /**
     * Gets the value of the legDistance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLegDistance() {
        return legDistance;
    }

    /**
     * Sets the value of the legDistance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLegDistance(BigDecimal value) {
        this.legDistance = value;
    }

    /**
     * Gets the value of the legInboundIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegInboundIndicator() {
        return legInboundIndicator;
    }

    /**
     * Sets the value of the legInboundIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegInboundIndicator(String value) {
        this.legInboundIndicator = value;
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
     * Gets the value of the pathAndTermination property.
     * 
     * @return
     *     possible object is
     *     {@link PathAndTermination }
     *     
     */
    public PathAndTermination getPathAndTermination() {
        return pathAndTermination;
    }

    /**
     * Sets the value of the pathAndTermination property.
     * 
     * @param value
     *     allowed object is
     *     {@link PathAndTermination }
     *     
     */
    public void setPathAndTermination(PathAndTermination value) {
        this.pathAndTermination = value;
    }

    /**
     * Gets the value of the procedureDesignMagVar property.
     * 
     * @return
     *     possible object is
     *     {@link MagneticVariation }
     *     
     */
    public MagneticVariation getProcedureDesignMagVar() {
        return procedureDesignMagVar;
    }

    /**
     * Sets the value of the procedureDesignMagVar property.
     * 
     * @param value
     *     allowed object is
     *     {@link MagneticVariation }
     *     
     */
    public void setProcedureDesignMagVar(MagneticVariation value) {
        this.procedureDesignMagVar = value;
    }

    /**
     * Gets the value of the rho property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRho() {
        return rho;
    }

    /**
     * Sets the value of the rho property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRho(BigDecimal value) {
        this.rho = value;
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
     * Gets the value of the speedLimit property.
     * 
     * @return
     *     possible object is
     *     {@link SpeedLimit }
     *     
     */
    public SpeedLimit getSpeedLimit() {
        return speedLimit;
    }

    /**
     * Sets the value of the speedLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link SpeedLimit }
     *     
     */
    public void setSpeedLimit(SpeedLimit value) {
        this.speedLimit = value;
    }

    /**
     * Gets the value of the theta property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTheta() {
        return theta;
    }

    /**
     * Sets the value of the theta property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTheta(BigDecimal value) {
        this.theta = value;
    }

    /**
     * Gets the value of the transitionsAltitudeLevel property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTransitionsAltitudeLevel() {
        return transitionsAltitudeLevel;
    }

    /**
     * Sets the value of the transitionsAltitudeLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTransitionsAltitudeLevel(Integer value) {
        this.transitionsAltitudeLevel = value;
    }

    /**
     * Gets the value of the turnDirection property.
     * 
     * @return
     *     possible object is
     *     {@link TurnDirection }
     *     
     */
    public TurnDirection getTurnDirection() {
        return turnDirection;
    }

    /**
     * Sets the value of the turnDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link TurnDirection }
     *     
     */
    public void setTurnDirection(TurnDirection value) {
        this.turnDirection = value;
    }

    /**
     * Gets the value of the isTurnDirectionValid property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTurnDirectionValid() {
        return isTurnDirectionValid;
    }

    /**
     * Sets the value of the isTurnDirectionValid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTurnDirectionValid(Boolean value) {
        this.isTurnDirectionValid = value;
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
     * Gets the value of the course property.
     * 
     * @return
     *     possible object is
     *     {@link Course }
     *     
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the value of the course property.
     * 
     * @param value
     *     allowed object is
     *     {@link Course }
     *     
     */
    public void setCourse(Course value) {
        this.course = value;
    }

    /**
     * Gets the value of the centerFix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCenterFix() {
        return centerFix;
    }

    /**
     * Sets the value of the centerFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCenterFix(String value) {
        this.centerFix = value;
    }

    /**
     * Gets the value of the centerFixRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getCenterFixRef() {
        return centerFixRef;
    }

    /**
     * Sets the value of the centerFixRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setCenterFixRef(Object value) {
        this.centerFixRef = value;
    }

    /**
     * Gets the value of the altitudeConstraint property.
     * 
     * @return
     *     possible object is
     *     {@link AltitudeConstraint }
     *     
     */
    public AltitudeConstraint getAltitudeConstraint() {
        return altitudeConstraint;
    }

    /**
     * Sets the value of the altitudeConstraint property.
     * 
     * @param value
     *     allowed object is
     *     {@link AltitudeConstraint }
     *     
     */
    public void setAltitudeConstraint(AltitudeConstraint value) {
        this.altitudeConstraint = value;
    }

    /**
     * Gets the value of the altitudeTermination property.
     * 
     * @return
     *     possible object is
     *     {@link AltitudeTermination }
     *     
     */
    public AltitudeTermination getAltitudeTermination() {
        return altitudeTermination;
    }

    /**
     * Sets the value of the altitudeTermination property.
     * 
     * @param value
     *     allowed object is
     *     {@link AltitudeTermination }
     *     
     */
    public void setAltitudeTermination(AltitudeTermination value) {
        this.altitudeTermination = value;
    }

    /**
     * Gets the value of the raceTrackAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link ProcedureLeg.RaceTrackAltitude }
     *     
     */
    public ProcedureLeg.RaceTrackAltitude getRaceTrackAltitude() {
        return raceTrackAltitude;
    }

    /**
     * Sets the value of the raceTrackAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedureLeg.RaceTrackAltitude }
     *     
     */
    public void setRaceTrackAltitude(ProcedureLeg.RaceTrackAltitude value) {
        this.raceTrackAltitude = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="atOrAbove" type="{http://www.arinc424.com/xml/datatypes}Constraint"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "atOrAbove"
    })
    public static class RaceTrackAltitude {

        @XmlElement(required = true)
        protected Constraint atOrAbove;

        /**
         * Gets the value of the atOrAbove property.
         * 
         * @return
         *     possible object is
         *     {@link Constraint }
         *     
         */
        public Constraint getAtOrAbove() {
            return atOrAbove;
        }

        /**
         * Sets the value of the atOrAbove property.
         * 
         * @param value
         *     allowed object is
         *     {@link Constraint }
         *     
         */
        public void setAtOrAbove(Constraint value) {
            this.atOrAbove = value;
        }

    }

}
