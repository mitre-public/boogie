
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Runway Records
 * 
 * <p>Java class for Runway complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Runway"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Point"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isWithoutLocation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isDerivedLocation" type="{http://www.arinc424.com/xml/datatypes}IsDerived" minOccurs="0"/&gt;
 *         &lt;element name="displacedThresholdDistance" type="{http://www.arinc424.com/xml/datatypes}DisplacedThresholdDistance" minOccurs="0"/&gt;
 *         &lt;element name="landingThresholdElevation" type="{http://www.arinc424.com/xml/datatypes}Elevation" minOccurs="0"/&gt;
 *         &lt;element name="landingThresholdElevationType" type="{http://www.arinc424.com/xml/enumerations}ElevationType" minOccurs="0"/&gt;
 *         &lt;element name="runwayEndLocation" type="{http://www.arinc424.com/xml/datatypes}Location" minOccurs="0"/&gt;
 *         &lt;element name="runwayEndElevation" type="{http://www.arinc424.com/xml/datatypes}Elevation" minOccurs="0"/&gt;
 *         &lt;element name="ltpEllipsoidHeight" type="{http://www.arinc424.com/xml/datatypes}EllipsoidHeight" minOccurs="0"/&gt;
 *         &lt;element name="runwayBearing" type="{http://www.arinc424.com/xml/datatypes}Bearing" minOccurs="0"/&gt;
 *         &lt;element name="runwayTrueBearing" type="{http://www.arinc424.com/xml/datatypes}TrueBearing" minOccurs="0"/&gt;
 *         &lt;element name="runwayTrueBearingSource" type="{http://www.arinc424.com/xml/enumerations}TrueBearingSource" minOccurs="0"/&gt;
 *         &lt;element name="runwayDescription" type="{http://www.arinc424.com/xml/datatypes}RunwayDescription" minOccurs="0"/&gt;
 *         &lt;element name="runwayGradient" type="{http://www.arinc424.com/xml/datatypes}RunwayGradient" minOccurs="0"/&gt;
 *         &lt;element name="runwayIdentifier" type="{http://www.arinc424.com/xml/datatypes}RunwayIdentifier"/&gt;
 *         &lt;element name="runwayLength" type="{http://www.arinc424.com/xml/datatypes}RunwayLength" minOccurs="0"/&gt;
 *         &lt;element name="runwayWidth" type="{http://www.arinc424.com/xml/datatypes}RunwayWidth" minOccurs="0"/&gt;
 *         &lt;element name="stopway" type="{http://www.arinc424.com/xml/datatypes}Stopway" minOccurs="0"/&gt;
 *         &lt;element name="tchValueIndicator" type="{http://www.arinc424.com/xml/enumerations}TchValueIndicator" minOccurs="0"/&gt;
 *         &lt;element name="thresholdCrossingHeight" type="{http://www.arinc424.com/xml/datatypes}ThresholdCrossingHeight" minOccurs="0"/&gt;
 *         &lt;element name="touchDownZoneElevation" type="{http://www.arinc424.com/xml/datatypes}Elevation" minOccurs="0"/&gt;
 *         &lt;element name="starterExtension" type="{http://www.arinc424.com/xml/datatypes}StarterExtension" minOccurs="0"/&gt;
 *         &lt;element name="surfaceCode" type="{http://www.arinc424.com/xml/enumerations}RunwaySurfaceCode" minOccurs="0"/&gt;
 *         &lt;element name="surfaceType" type="{http://www.arinc424.com/xml/enumerations}SurfaceType" minOccurs="0"/&gt;
 *         &lt;element name="helicopterPerformanceReq" type="{http://www.arinc424.com/xml/enumerations}HelicopterPerformanceReq" minOccurs="0"/&gt;
 *         &lt;element name="takeOffRunwayAvailable" type="{http://www.arinc424.com/xml/datatypes}DistanceFeetFiveDigits" minOccurs="0"/&gt;
 *         &lt;element name="takeOffDistanceAvailable" type="{http://www.arinc424.com/xml/datatypes}DistanceFeetFiveDigits" minOccurs="0"/&gt;
 *         &lt;element name="accelerateStopDistanceAvailable" type="{http://www.arinc424.com/xml/datatypes}DistanceFeetFiveDigits" minOccurs="0"/&gt;
 *         &lt;element name="landingDistanceAvailable" type="{http://www.arinc424.com/xml/datatypes}DistanceFeetFiveDigits" minOccurs="0"/&gt;
 *         &lt;element name="runwayUsageIndicator" type="{http://www.arinc424.com/xml/enumerations}RunwayUsageIndicator" minOccurs="0"/&gt;
 *         &lt;element name="runwayAccuracy" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="runwayAccuracyCompliance" type="{http://www.arinc424.com/xml/enumerations}RunwayAccuracyCompliance" minOccurs="0"/&gt;
 *                   &lt;element name="landingThresholdElevationAccuracyCompliance" type="{http://www.arinc424.com/xml/enumerations}LandingThresholdElevationAccuracyCompliance" minOccurs="0"/&gt;
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
@XmlType(name = "Runway", propOrder = {
    "isWithoutLocation",
    "isDerivedLocation",
    "displacedThresholdDistance",
    "landingThresholdElevation",
    "landingThresholdElevationType",
    "runwayEndLocation",
    "runwayEndElevation",
    "ltpEllipsoidHeight",
    "runwayBearing",
    "runwayTrueBearing",
    "runwayTrueBearingSource",
    "runwayDescription",
    "runwayGradient",
    "runwayIdentifier",
    "runwayLength",
    "runwayWidth",
    "stopway",
    "tchValueIndicator",
    "thresholdCrossingHeight",
    "touchDownZoneElevation",
    "starterExtension",
    "surfaceCode",
    "surfaceType",
    "helicopterPerformanceReq",
    "takeOffRunwayAvailable",
    "takeOffDistanceAvailable",
    "accelerateStopDistanceAvailable",
    "landingDistanceAvailable",
    "runwayUsageIndicator",
    "runwayAccuracy"
})
public class Runway
    extends A424Point
{

    @XmlElement(defaultValue = "false")
    protected Boolean isWithoutLocation;
    @XmlElement(defaultValue = "false")
    protected Boolean isDerivedLocation;
    @XmlSchemaType(name = "unsignedInt")
    protected Long displacedThresholdDistance;
    protected Integer landingThresholdElevation;
    @XmlSchemaType(name = "string")
    protected ElevationType landingThresholdElevationType;
    protected Location runwayEndLocation;
    protected Integer runwayEndElevation;
    protected BigDecimal ltpEllipsoidHeight;
    protected Bearing runwayBearing;
    protected BigDecimal runwayTrueBearing;
    @XmlSchemaType(name = "string")
    protected TrueBearingSource runwayTrueBearingSource;
    protected String runwayDescription;
    protected BigDecimal runwayGradient;
    @XmlElement(required = true)
    protected RunwayIdentifier runwayIdentifier;
    @XmlSchemaType(name = "unsignedInt")
    protected Long runwayLength;
    @XmlSchemaType(name = "unsignedInt")
    protected Long runwayWidth;
    @XmlSchemaType(name = "unsignedInt")
    protected Long stopway;
    @XmlSchemaType(name = "string")
    protected TchValueIndicator tchValueIndicator;
    @XmlSchemaType(name = "unsignedInt")
    protected Long thresholdCrossingHeight;
    protected Integer touchDownZoneElevation;
    protected Integer starterExtension;
    @XmlSchemaType(name = "string")
    protected RunwaySurfaceCode surfaceCode;
    @XmlSchemaType(name = "string")
    protected SurfaceType surfaceType;
    @XmlSchemaType(name = "string")
    protected HelicopterPerformanceReq helicopterPerformanceReq;
    @XmlSchemaType(name = "unsignedInt")
    protected Long takeOffRunwayAvailable;
    @XmlSchemaType(name = "unsignedInt")
    protected Long takeOffDistanceAvailable;
    @XmlSchemaType(name = "unsignedInt")
    protected Long accelerateStopDistanceAvailable;
    @XmlSchemaType(name = "unsignedInt")
    protected Long landingDistanceAvailable;
    @XmlSchemaType(name = "string")
    protected RunwayUsageIndicator runwayUsageIndicator;
    protected Runway.RunwayAccuracy runwayAccuracy;

    /**
     * Gets the value of the isWithoutLocation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsWithoutLocation() {
        return isWithoutLocation;
    }

    /**
     * Sets the value of the isWithoutLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsWithoutLocation(Boolean value) {
        this.isWithoutLocation = value;
    }

    /**
     * Gets the value of the isDerivedLocation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDerivedLocation() {
        return isDerivedLocation;
    }

    /**
     * Sets the value of the isDerivedLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDerivedLocation(Boolean value) {
        this.isDerivedLocation = value;
    }

    /**
     * Gets the value of the displacedThresholdDistance property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDisplacedThresholdDistance() {
        return displacedThresholdDistance;
    }

    /**
     * Sets the value of the displacedThresholdDistance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDisplacedThresholdDistance(Long value) {
        this.displacedThresholdDistance = value;
    }

    /**
     * Gets the value of the landingThresholdElevation property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLandingThresholdElevation() {
        return landingThresholdElevation;
    }

    /**
     * Sets the value of the landingThresholdElevation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLandingThresholdElevation(Integer value) {
        this.landingThresholdElevation = value;
    }

    /**
     * Gets the value of the landingThresholdElevationType property.
     * 
     * @return
     *     possible object is
     *     {@link ElevationType }
     *     
     */
    public ElevationType getLandingThresholdElevationType() {
        return landingThresholdElevationType;
    }

    /**
     * Sets the value of the landingThresholdElevationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElevationType }
     *     
     */
    public void setLandingThresholdElevationType(ElevationType value) {
        this.landingThresholdElevationType = value;
    }

    /**
     * Gets the value of the runwayEndLocation property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getRunwayEndLocation() {
        return runwayEndLocation;
    }

    /**
     * Sets the value of the runwayEndLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setRunwayEndLocation(Location value) {
        this.runwayEndLocation = value;
    }

    /**
     * Gets the value of the runwayEndElevation property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRunwayEndElevation() {
        return runwayEndElevation;
    }

    /**
     * Sets the value of the runwayEndElevation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRunwayEndElevation(Integer value) {
        this.runwayEndElevation = value;
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
     * Gets the value of the runwayBearing property.
     * 
     * @return
     *     possible object is
     *     {@link Bearing }
     *     
     */
    public Bearing getRunwayBearing() {
        return runwayBearing;
    }

    /**
     * Sets the value of the runwayBearing property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bearing }
     *     
     */
    public void setRunwayBearing(Bearing value) {
        this.runwayBearing = value;
    }

    /**
     * Gets the value of the runwayTrueBearing property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRunwayTrueBearing() {
        return runwayTrueBearing;
    }

    /**
     * Sets the value of the runwayTrueBearing property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRunwayTrueBearing(BigDecimal value) {
        this.runwayTrueBearing = value;
    }

    /**
     * Gets the value of the runwayTrueBearingSource property.
     * 
     * @return
     *     possible object is
     *     {@link TrueBearingSource }
     *     
     */
    public TrueBearingSource getRunwayTrueBearingSource() {
        return runwayTrueBearingSource;
    }

    /**
     * Sets the value of the runwayTrueBearingSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueBearingSource }
     *     
     */
    public void setRunwayTrueBearingSource(TrueBearingSource value) {
        this.runwayTrueBearingSource = value;
    }

    /**
     * Gets the value of the runwayDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRunwayDescription() {
        return runwayDescription;
    }

    /**
     * Sets the value of the runwayDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRunwayDescription(String value) {
        this.runwayDescription = value;
    }

    /**
     * Gets the value of the runwayGradient property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRunwayGradient() {
        return runwayGradient;
    }

    /**
     * Sets the value of the runwayGradient property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRunwayGradient(BigDecimal value) {
        this.runwayGradient = value;
    }

    /**
     * Gets the value of the runwayIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link RunwayIdentifier }
     *     
     */
    public RunwayIdentifier getRunwayIdentifier() {
        return runwayIdentifier;
    }

    /**
     * Sets the value of the runwayIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link RunwayIdentifier }
     *     
     */
    public void setRunwayIdentifier(RunwayIdentifier value) {
        this.runwayIdentifier = value;
    }

    /**
     * Gets the value of the runwayLength property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRunwayLength() {
        return runwayLength;
    }

    /**
     * Sets the value of the runwayLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRunwayLength(Long value) {
        this.runwayLength = value;
    }

    /**
     * Gets the value of the runwayWidth property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRunwayWidth() {
        return runwayWidth;
    }

    /**
     * Sets the value of the runwayWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRunwayWidth(Long value) {
        this.runwayWidth = value;
    }

    /**
     * Gets the value of the stopway property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getStopway() {
        return stopway;
    }

    /**
     * Sets the value of the stopway property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setStopway(Long value) {
        this.stopway = value;
    }

    /**
     * Gets the value of the tchValueIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link TchValueIndicator }
     *     
     */
    public TchValueIndicator getTchValueIndicator() {
        return tchValueIndicator;
    }

    /**
     * Sets the value of the tchValueIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link TchValueIndicator }
     *     
     */
    public void setTchValueIndicator(TchValueIndicator value) {
        this.tchValueIndicator = value;
    }

    /**
     * Gets the value of the thresholdCrossingHeight property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getThresholdCrossingHeight() {
        return thresholdCrossingHeight;
    }

    /**
     * Sets the value of the thresholdCrossingHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setThresholdCrossingHeight(Long value) {
        this.thresholdCrossingHeight = value;
    }

    /**
     * Gets the value of the touchDownZoneElevation property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTouchDownZoneElevation() {
        return touchDownZoneElevation;
    }

    /**
     * Sets the value of the touchDownZoneElevation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTouchDownZoneElevation(Integer value) {
        this.touchDownZoneElevation = value;
    }

    /**
     * Gets the value of the starterExtension property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getStarterExtension() {
        return starterExtension;
    }

    /**
     * Sets the value of the starterExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStarterExtension(Integer value) {
        this.starterExtension = value;
    }

    /**
     * Gets the value of the surfaceCode property.
     * 
     * @return
     *     possible object is
     *     {@link RunwaySurfaceCode }
     *     
     */
    public RunwaySurfaceCode getSurfaceCode() {
        return surfaceCode;
    }

    /**
     * Sets the value of the surfaceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link RunwaySurfaceCode }
     *     
     */
    public void setSurfaceCode(RunwaySurfaceCode value) {
        this.surfaceCode = value;
    }

    /**
     * Gets the value of the surfaceType property.
     * 
     * @return
     *     possible object is
     *     {@link SurfaceType }
     *     
     */
    public SurfaceType getSurfaceType() {
        return surfaceType;
    }

    /**
     * Sets the value of the surfaceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SurfaceType }
     *     
     */
    public void setSurfaceType(SurfaceType value) {
        this.surfaceType = value;
    }

    /**
     * Gets the value of the helicopterPerformanceReq property.
     * 
     * @return
     *     possible object is
     *     {@link HelicopterPerformanceReq }
     *     
     */
    public HelicopterPerformanceReq getHelicopterPerformanceReq() {
        return helicopterPerformanceReq;
    }

    /**
     * Sets the value of the helicopterPerformanceReq property.
     * 
     * @param value
     *     allowed object is
     *     {@link HelicopterPerformanceReq }
     *     
     */
    public void setHelicopterPerformanceReq(HelicopterPerformanceReq value) {
        this.helicopterPerformanceReq = value;
    }

    /**
     * Gets the value of the takeOffRunwayAvailable property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTakeOffRunwayAvailable() {
        return takeOffRunwayAvailable;
    }

    /**
     * Sets the value of the takeOffRunwayAvailable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTakeOffRunwayAvailable(Long value) {
        this.takeOffRunwayAvailable = value;
    }

    /**
     * Gets the value of the takeOffDistanceAvailable property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTakeOffDistanceAvailable() {
        return takeOffDistanceAvailable;
    }

    /**
     * Sets the value of the takeOffDistanceAvailable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTakeOffDistanceAvailable(Long value) {
        this.takeOffDistanceAvailable = value;
    }

    /**
     * Gets the value of the accelerateStopDistanceAvailable property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAccelerateStopDistanceAvailable() {
        return accelerateStopDistanceAvailable;
    }

    /**
     * Sets the value of the accelerateStopDistanceAvailable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAccelerateStopDistanceAvailable(Long value) {
        this.accelerateStopDistanceAvailable = value;
    }

    /**
     * Gets the value of the landingDistanceAvailable property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLandingDistanceAvailable() {
        return landingDistanceAvailable;
    }

    /**
     * Sets the value of the landingDistanceAvailable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLandingDistanceAvailable(Long value) {
        this.landingDistanceAvailable = value;
    }

    /**
     * Gets the value of the runwayUsageIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link RunwayUsageIndicator }
     *     
     */
    public RunwayUsageIndicator getRunwayUsageIndicator() {
        return runwayUsageIndicator;
    }

    /**
     * Sets the value of the runwayUsageIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link RunwayUsageIndicator }
     *     
     */
    public void setRunwayUsageIndicator(RunwayUsageIndicator value) {
        this.runwayUsageIndicator = value;
    }

    /**
     * Gets the value of the runwayAccuracy property.
     * 
     * @return
     *     possible object is
     *     {@link Runway.RunwayAccuracy }
     *     
     */
    public Runway.RunwayAccuracy getRunwayAccuracy() {
        return runwayAccuracy;
    }

    /**
     * Sets the value of the runwayAccuracy property.
     * 
     * @param value
     *     allowed object is
     *     {@link Runway.RunwayAccuracy }
     *     
     */
    public void setRunwayAccuracy(Runway.RunwayAccuracy value) {
        this.runwayAccuracy = value;
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
     *         &lt;element name="runwayAccuracyCompliance" type="{http://www.arinc424.com/xml/enumerations}RunwayAccuracyCompliance" minOccurs="0"/&gt;
     *         &lt;element name="landingThresholdElevationAccuracyCompliance" type="{http://www.arinc424.com/xml/enumerations}LandingThresholdElevationAccuracyCompliance" minOccurs="0"/&gt;
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
        "runwayAccuracyCompliance",
        "landingThresholdElevationAccuracyCompliance"
    })
    public static class RunwayAccuracy {

        @XmlSchemaType(name = "string")
        protected RunwayAccuracyCompliance runwayAccuracyCompliance;
        @XmlSchemaType(name = "string")
        protected LandingThresholdElevationAccuracyCompliance landingThresholdElevationAccuracyCompliance;

        /**
         * Gets the value of the runwayAccuracyCompliance property.
         * 
         * @return
         *     possible object is
         *     {@link RunwayAccuracyCompliance }
         *     
         */
        public RunwayAccuracyCompliance getRunwayAccuracyCompliance() {
            return runwayAccuracyCompliance;
        }

        /**
         * Sets the value of the runwayAccuracyCompliance property.
         * 
         * @param value
         *     allowed object is
         *     {@link RunwayAccuracyCompliance }
         *     
         */
        public void setRunwayAccuracyCompliance(RunwayAccuracyCompliance value) {
            this.runwayAccuracyCompliance = value;
        }

        /**
         * Gets the value of the landingThresholdElevationAccuracyCompliance property.
         * 
         * @return
         *     possible object is
         *     {@link LandingThresholdElevationAccuracyCompliance }
         *     
         */
        public LandingThresholdElevationAccuracyCompliance getLandingThresholdElevationAccuracyCompliance() {
            return landingThresholdElevationAccuracyCompliance;
        }

        /**
         * Sets the value of the landingThresholdElevationAccuracyCompliance property.
         * 
         * @param value
         *     allowed object is
         *     {@link LandingThresholdElevationAccuracyCompliance }
         *     
         */
        public void setLandingThresholdElevationAccuracyCompliance(LandingThresholdElevationAccuracyCompliance value) {
            this.landingThresholdElevationAccuracyCompliance = value;
        }

    }

}
