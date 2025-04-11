
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class represents the additional waypoint description information used by approach procedure legs.
 * 
 * <p>Java class for ApproachWaypointDescription complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApproachWaypointDescription"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isEngineOutDisarmPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFacf" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isMissedApproachPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFaf" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFinalEndPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFixTurningFinalApproach" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isInitialApproachFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isIntermediateApproachFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="stepDownFix" type="{http://www.arinc424.com/xml/enumerations}StepDownFix" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApproachWaypointDescription", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "isEngineOutDisarmPoint",
    "isFacf",
    "isMissedApproachPoint",
    "isFaf",
    "isFinalEndPoint",
    "isFixTurningFinalApproach",
    "isInitialApproachFix",
    "isIntermediateApproachFix",
    "stepDownFix"
})
public class ApproachWaypointDescription {

    @XmlElement(defaultValue = "false")
    protected Boolean isEngineOutDisarmPoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isFacf;
    protected Boolean isMissedApproachPoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isFaf;
    @XmlElement(defaultValue = "false")
    protected Boolean isFinalEndPoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isFixTurningFinalApproach;
    @XmlElement(defaultValue = "false")
    protected Boolean isInitialApproachFix;
    @XmlElement(defaultValue = "false")
    protected Boolean isIntermediateApproachFix;
    @XmlSchemaType(name = "string")
    protected StepDownFix stepDownFix;

    /**
     * Gets the value of the isEngineOutDisarmPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsEngineOutDisarmPoint() {
        return isEngineOutDisarmPoint;
    }

    /**
     * Sets the value of the isEngineOutDisarmPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsEngineOutDisarmPoint(Boolean value) {
        this.isEngineOutDisarmPoint = value;
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
     * Gets the value of the isFinalEndPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFinalEndPoint() {
        return isFinalEndPoint;
    }

    /**
     * Sets the value of the isFinalEndPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFinalEndPoint(Boolean value) {
        this.isFinalEndPoint = value;
    }

    /**
     * Gets the value of the isFixTurningFinalApproach property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFixTurningFinalApproach() {
        return isFixTurningFinalApproach;
    }

    /**
     * Sets the value of the isFixTurningFinalApproach property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFixTurningFinalApproach(Boolean value) {
        this.isFixTurningFinalApproach = value;
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
     * Gets the value of the stepDownFix property.
     * 
     * @return
     *     possible object is
     *     {@link StepDownFix }
     *     
     */
    public StepDownFix getStepDownFix() {
        return stepDownFix;
    }

    /**
     * Sets the value of the stepDownFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link StepDownFix }
     *     
     */
    public void setStepDownFix(StepDownFix value) {
        this.stepDownFix = value;
    }

}
