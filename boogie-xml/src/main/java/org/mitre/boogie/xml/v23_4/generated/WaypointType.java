
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The “Waypoint Type” field identifies a number of data conditions (see 424 5.42).
 * 
 *  1. The first is whether or not the waypoint has been published in an official government source or created during database coding of routes or
 * procedures. 
 *  2. The second is whether or not the waypoint is an intersection and/or DME fix formed with reference to ground based navaids or is an RNAV Waypoint formed by the
 * latitude and longitude. 
 *  3. The third is an indication of one or more functions assigned to that waypoint in terminal procedure coding.
 *  4. The fourth is an indication of location of the waypoint with reference to airspace boundaries and/or grid lines.
 *  5. The fifth is an indication of how ATC might be using the waypoint in operational clearances.
 *  6. The sixth is an indication that the waypoint has been published for VFR use only.
 *  7. Lastly, there is an indication of whether the waypoint is published for use in terminal procedure coding of a specific type, multiple types or not published at all.
 * 
 * <p>Java class for WaypointType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WaypointType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isAirwayIntersection" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isArcCenter" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isBackMarker" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isControlledAirspaceIntersection" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isEnroute" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFacf" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFaf" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFirOrFraEntryPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFirOrFraExitPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFirUirFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isForApproach" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isForSid" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isForStar" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isForMultipleProcedures" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFullDegreeLatFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isHalfDegreeLatFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isHelicopterOnlyAirwayFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isInitialApproachFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isInitialDepartureFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isInnerMarker" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isIntermediateApproachFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isIntersectionDmeFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isMiddleMarker" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isMissedApproachPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isNdb" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isOceanicGateway" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isOffRoute" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isOuterMarker" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isRequiredOffRoute" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isRfLegNotProcedureFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isRnav" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isSourceProvidedEnroute" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isStepDownFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isUncharted" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isUnnamed" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isVfr" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WaypointType", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "isAirwayIntersection",
    "isArcCenter",
    "isBackMarker",
    "isControlledAirspaceIntersection",
    "isEnroute",
    "isFacf",
    "isFaf",
    "isFirOrFraEntryPoint",
    "isFirOrFraExitPoint",
    "isFirUirFix",
    "isForApproach",
    "isForSid",
    "isForStar",
    "isForMultipleProcedures",
    "isFullDegreeLatFix",
    "isHalfDegreeLatFix",
    "isHelicopterOnlyAirwayFix",
    "isInitialApproachFix",
    "isInitialDepartureFix",
    "isInnerMarker",
    "isIntermediateApproachFix",
    "isIntersectionDmeFix",
    "isMiddleMarker",
    "isMissedApproachPoint",
    "isNdb",
    "isOceanicGateway",
    "isOffRoute",
    "isOuterMarker",
    "isRequiredOffRoute",
    "isRfLegNotProcedureFix",
    "isRnav",
    "isSourceProvidedEnroute",
    "isStepDownFix",
    "isUncharted",
    "isUnnamed",
    "isVfr"
})
public class WaypointType {

    @XmlElement(defaultValue = "false")
    protected Boolean isAirwayIntersection;
    @XmlElement(defaultValue = "false")
    protected Boolean isArcCenter;
    @XmlElement(defaultValue = "false")
    protected Boolean isBackMarker;
    @XmlElement(defaultValue = "false")
    protected Boolean isControlledAirspaceIntersection;
    @XmlElement(defaultValue = "false")
    protected Boolean isEnroute;
    @XmlElement(defaultValue = "false")
    protected Boolean isFacf;
    @XmlElement(defaultValue = "false")
    protected Boolean isFaf;
    @XmlElement(defaultValue = "false")
    protected Boolean isFirOrFraEntryPoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isFirOrFraExitPoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isFirUirFix;
    @XmlElement(defaultValue = "false")
    protected Boolean isForApproach;
    @XmlElement(defaultValue = "false")
    protected Boolean isForSid;
    @XmlElement(defaultValue = "false")
    protected Boolean isForStar;
    @XmlElement(defaultValue = "false")
    protected Boolean isForMultipleProcedures;
    @XmlElement(defaultValue = "false")
    protected Boolean isFullDegreeLatFix;
    @XmlElement(defaultValue = "false")
    protected Boolean isHalfDegreeLatFix;
    @XmlElement(defaultValue = "false")
    protected Boolean isHelicopterOnlyAirwayFix;
    @XmlElement(defaultValue = "false")
    protected Boolean isInitialApproachFix;
    @XmlElement(defaultValue = "false")
    protected Boolean isInitialDepartureFix;
    @XmlElement(defaultValue = "false")
    protected Boolean isInnerMarker;
    @XmlElement(defaultValue = "false")
    protected Boolean isIntermediateApproachFix;
    @XmlElement(defaultValue = "false")
    protected Boolean isIntersectionDmeFix;
    @XmlElement(defaultValue = "false")
    protected Boolean isMiddleMarker;
    @XmlElement(defaultValue = "false")
    protected Boolean isMissedApproachPoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isNdb;
    @XmlElement(defaultValue = "false")
    protected Boolean isOceanicGateway;
    @XmlElement(defaultValue = "false")
    protected Boolean isOffRoute;
    @XmlElement(defaultValue = "false")
    protected Boolean isOuterMarker;
    @XmlElement(defaultValue = "false")
    protected Boolean isRequiredOffRoute;
    @XmlElement(defaultValue = "false")
    protected Boolean isRfLegNotProcedureFix;
    @XmlElement(defaultValue = "false")
    protected Boolean isRnav;
    @XmlElement(defaultValue = "false")
    protected Boolean isSourceProvidedEnroute;
    @XmlElement(defaultValue = "false")
    protected Boolean isStepDownFix;
    @XmlElement(defaultValue = "false")
    protected Boolean isUncharted;
    @XmlElement(defaultValue = "false")
    protected Boolean isUnnamed;
    @XmlElement(defaultValue = "false")
    protected Boolean isVfr;

    /**
     * Gets the value of the isAirwayIntersection property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAirwayIntersection() {
        return isAirwayIntersection;
    }

    /**
     * Sets the value of the isAirwayIntersection property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAirwayIntersection(Boolean value) {
        this.isAirwayIntersection = value;
    }

    /**
     * Gets the value of the isArcCenter property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsArcCenter() {
        return isArcCenter;
    }

    /**
     * Sets the value of the isArcCenter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsArcCenter(Boolean value) {
        this.isArcCenter = value;
    }

    /**
     * Gets the value of the isBackMarker property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsBackMarker() {
        return isBackMarker;
    }

    /**
     * Sets the value of the isBackMarker property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsBackMarker(Boolean value) {
        this.isBackMarker = value;
    }

    /**
     * Gets the value of the isControlledAirspaceIntersection property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsControlledAirspaceIntersection() {
        return isControlledAirspaceIntersection;
    }

    /**
     * Sets the value of the isControlledAirspaceIntersection property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsControlledAirspaceIntersection(Boolean value) {
        this.isControlledAirspaceIntersection = value;
    }

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
     * Gets the value of the isFacf property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFacf() {
        return isFacf;
    }

    /**
     * Sets the value of the isFacf property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFacf(Boolean value) {
        this.isFacf = value;
    }

    /**
     * Gets the value of the isFaf property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFaf() {
        return isFaf;
    }

    /**
     * Sets the value of the isFaf property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFaf(Boolean value) {
        this.isFaf = value;
    }

    /**
     * Gets the value of the isFirOrFraEntryPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFirOrFraEntryPoint() {
        return isFirOrFraEntryPoint;
    }

    /**
     * Sets the value of the isFirOrFraEntryPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFirOrFraEntryPoint(Boolean value) {
        this.isFirOrFraEntryPoint = value;
    }

    /**
     * Gets the value of the isFirOrFraExitPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFirOrFraExitPoint() {
        return isFirOrFraExitPoint;
    }

    /**
     * Sets the value of the isFirOrFraExitPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFirOrFraExitPoint(Boolean value) {
        this.isFirOrFraExitPoint = value;
    }

    /**
     * Gets the value of the isFirUirFix property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFirUirFix() {
        return isFirUirFix;
    }

    /**
     * Sets the value of the isFirUirFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFirUirFix(Boolean value) {
        this.isFirUirFix = value;
    }

    /**
     * Gets the value of the isForApproach property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsForApproach() {
        return isForApproach;
    }

    /**
     * Sets the value of the isForApproach property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsForApproach(Boolean value) {
        this.isForApproach = value;
    }

    /**
     * Gets the value of the isForSid property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsForSid() {
        return isForSid;
    }

    /**
     * Sets the value of the isForSid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsForSid(Boolean value) {
        this.isForSid = value;
    }

    /**
     * Gets the value of the isForStar property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsForStar() {
        return isForStar;
    }

    /**
     * Sets the value of the isForStar property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsForStar(Boolean value) {
        this.isForStar = value;
    }

    /**
     * Gets the value of the isForMultipleProcedures property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsForMultipleProcedures() {
        return isForMultipleProcedures;
    }

    /**
     * Sets the value of the isForMultipleProcedures property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsForMultipleProcedures(Boolean value) {
        this.isForMultipleProcedures = value;
    }

    /**
     * Gets the value of the isFullDegreeLatFix property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFullDegreeLatFix() {
        return isFullDegreeLatFix;
    }

    /**
     * Sets the value of the isFullDegreeLatFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFullDegreeLatFix(Boolean value) {
        this.isFullDegreeLatFix = value;
    }

    /**
     * Gets the value of the isHalfDegreeLatFix property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsHalfDegreeLatFix() {
        return isHalfDegreeLatFix;
    }

    /**
     * Sets the value of the isHalfDegreeLatFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsHalfDegreeLatFix(Boolean value) {
        this.isHalfDegreeLatFix = value;
    }

    /**
     * Gets the value of the isHelicopterOnlyAirwayFix property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsHelicopterOnlyAirwayFix() {
        return isHelicopterOnlyAirwayFix;
    }

    /**
     * Sets the value of the isHelicopterOnlyAirwayFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsHelicopterOnlyAirwayFix(Boolean value) {
        this.isHelicopterOnlyAirwayFix = value;
    }

    /**
     * Gets the value of the isInitialApproachFix property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsInitialApproachFix() {
        return isInitialApproachFix;
    }

    /**
     * Sets the value of the isInitialApproachFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsInitialApproachFix(Boolean value) {
        this.isInitialApproachFix = value;
    }

    /**
     * Gets the value of the isInitialDepartureFix property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsInitialDepartureFix() {
        return isInitialDepartureFix;
    }

    /**
     * Sets the value of the isInitialDepartureFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsInitialDepartureFix(Boolean value) {
        this.isInitialDepartureFix = value;
    }

    /**
     * Gets the value of the isInnerMarker property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsInnerMarker() {
        return isInnerMarker;
    }

    /**
     * Sets the value of the isInnerMarker property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsInnerMarker(Boolean value) {
        this.isInnerMarker = value;
    }

    /**
     * Gets the value of the isIntermediateApproachFix property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsIntermediateApproachFix() {
        return isIntermediateApproachFix;
    }

    /**
     * Sets the value of the isIntermediateApproachFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsIntermediateApproachFix(Boolean value) {
        this.isIntermediateApproachFix = value;
    }

    /**
     * Gets the value of the isIntersectionDmeFix property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsIntersectionDmeFix() {
        return isIntersectionDmeFix;
    }

    /**
     * Sets the value of the isIntersectionDmeFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsIntersectionDmeFix(Boolean value) {
        this.isIntersectionDmeFix = value;
    }

    /**
     * Gets the value of the isMiddleMarker property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMiddleMarker() {
        return isMiddleMarker;
    }

    /**
     * Sets the value of the isMiddleMarker property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMiddleMarker(Boolean value) {
        this.isMiddleMarker = value;
    }

    /**
     * Gets the value of the isMissedApproachPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMissedApproachPoint() {
        return isMissedApproachPoint;
    }

    /**
     * Sets the value of the isMissedApproachPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMissedApproachPoint(Boolean value) {
        this.isMissedApproachPoint = value;
    }

    /**
     * Gets the value of the isNdb property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNdb() {
        return isNdb;
    }

    /**
     * Sets the value of the isNdb property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNdb(Boolean value) {
        this.isNdb = value;
    }

    /**
     * Gets the value of the isOceanicGateway property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOceanicGateway() {
        return isOceanicGateway;
    }

    /**
     * Sets the value of the isOceanicGateway property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOceanicGateway(Boolean value) {
        this.isOceanicGateway = value;
    }

    /**
     * Gets the value of the isOffRoute property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOffRoute() {
        return isOffRoute;
    }

    /**
     * Sets the value of the isOffRoute property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOffRoute(Boolean value) {
        this.isOffRoute = value;
    }

    /**
     * Gets the value of the isOuterMarker property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOuterMarker() {
        return isOuterMarker;
    }

    /**
     * Sets the value of the isOuterMarker property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOuterMarker(Boolean value) {
        this.isOuterMarker = value;
    }

    /**
     * Gets the value of the isRequiredOffRoute property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRequiredOffRoute() {
        return isRequiredOffRoute;
    }

    /**
     * Sets the value of the isRequiredOffRoute property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRequiredOffRoute(Boolean value) {
        this.isRequiredOffRoute = value;
    }

    /**
     * Gets the value of the isRfLegNotProcedureFix property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRfLegNotProcedureFix() {
        return isRfLegNotProcedureFix;
    }

    /**
     * Sets the value of the isRfLegNotProcedureFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRfLegNotProcedureFix(Boolean value) {
        this.isRfLegNotProcedureFix = value;
    }

    /**
     * Gets the value of the isRnav property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRnav() {
        return isRnav;
    }

    /**
     * Sets the value of the isRnav property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRnav(Boolean value) {
        this.isRnav = value;
    }

    /**
     * Gets the value of the isSourceProvidedEnroute property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsSourceProvidedEnroute() {
        return isSourceProvidedEnroute;
    }

    /**
     * Sets the value of the isSourceProvidedEnroute property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsSourceProvidedEnroute(Boolean value) {
        this.isSourceProvidedEnroute = value;
    }

    /**
     * Gets the value of the isStepDownFix property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsStepDownFix() {
        return isStepDownFix;
    }

    /**
     * Sets the value of the isStepDownFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsStepDownFix(Boolean value) {
        this.isStepDownFix = value;
    }

    /**
     * Gets the value of the isUncharted property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsUncharted() {
        return isUncharted;
    }

    /**
     * Sets the value of the isUncharted property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsUncharted(Boolean value) {
        this.isUncharted = value;
    }

    /**
     * Gets the value of the isUnnamed property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsUnnamed() {
        return isUnnamed;
    }

    /**
     * Sets the value of the isUnnamed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsUnnamed(Boolean value) {
        this.isUnnamed = value;
    }

    /**
     * Gets the value of the isVfr property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsVfr() {
        return isVfr;
    }

    /**
     * Sets the value of the isVfr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsVfr(Boolean value) {
        this.isVfr = value;
    }

}
