
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The Flight Planning Arrival/Departure Data Record (424 PR record) is used to provide the sub-set of data defining SIDs (PD), STARs (PE) and Approach Procedures (PF) from Section 4.1.9 required for the computer generation of Flight Plans which include Terminal Procedures. The file contains a sequential listing of published Arrival Procedures, Approach Procedures and Departure Procedures, the available Enroute and Runway Transitions for those procedures, the Transition waypoints, the appropriate along track distance fields and the intermediate fixes along those routes.
 * 
 * <p>Java class for FlightPlanningRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FlightPlanningRecord"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="altitude" type="{http://www.arinc424.com/xml/datatypes}AltitudeConstraint" minOccurs="0"/&gt;
 *         &lt;element name="atcIdentifier" type="{http://www.arinc424.com/xml/datatypes}AtcIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="atcWeightCategory" type="{http://www.arinc424.com/xml/enumerations}AtcWeightCategory" minOccurs="0"/&gt;
 *         &lt;element name="commonSegmentDetails" type="{http://www.arinc424.com/xml/datatypes}CommonSegmentDetails" minOccurs="0"/&gt;
 *         &lt;element name="enrouteDetails" type="{http://www.arinc424.com/xml/datatypes}SegmentDetails" minOccurs="0"/&gt;
 *         &lt;element name="intermediateFixDetails" type="{http://www.arinc424.com/xml/datatypes}IntermediateFixDetails" maxOccurs="4" minOccurs="0"/&gt;
 *         &lt;element name="timesOfOperation" type="{http://www.arinc424.com/xml/datatypes}TimesOfOperation" minOccurs="0"/&gt;
 *         &lt;element name="initialCruiseTableRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/&gt;
 *         &lt;element name="initialDepartureMagneticCourse" type="{http://www.arinc424.com/xml/datatypes}Course" minOccurs="0"/&gt;
 *         &lt;element name="legTypeCode" type="{http://www.arinc424.com/xml/datatypes}LegTypeCode" minOccurs="0"/&gt;
 *         &lt;element name="numberOfEngines" type="{http://www.arinc424.com/xml/datatypes}NumberOfEnginesRestriction" minOccurs="0"/&gt;
 *         &lt;element name="portRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="procedureDescription" type="{http://www.arinc424.com/xml/datatypes}ProcedureDescription" minOccurs="0"/&gt;
 *         &lt;element name="procedureType" type="{http://www.arinc424.com/xml/enumerations}ProcedureType" minOccurs="0"/&gt;
 *         &lt;element name="reportingCode" type="{http://www.arinc424.com/xml/enumerations}ReportingCode" minOccurs="0"/&gt;
 *         &lt;element name="rnavFlags" type="{http://www.arinc424.com/xml/enumerations}RnavFlag" minOccurs="0"/&gt;
 *         &lt;element name="runwaySegmentDetails" type="{http://www.arinc424.com/xml/datatypes}SegmentDetails" minOccurs="0"/&gt;
 *         &lt;element name="sequenceNumber" type="{http://www.arinc424.com/xml/datatypes}SequenceNumber"/&gt;
 *         &lt;element name="sidStarApproachIdentifier" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="speedLimit" type="{http://www.arinc424.com/xml/datatypes}SpeedLimit" minOccurs="0"/&gt;
 *         &lt;element name="turbopropJetIndicator" type="{http://www.arinc424.com/xml/enumerations}TurbopropJetIndicator" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlightPlanningRecord", propOrder = {
    "altitude",
    "atcIdentifier",
    "atcWeightCategory",
    "commonSegmentDetails",
    "enrouteDetails",
    "intermediateFixDetails",
    "timesOfOperation",
    "initialCruiseTableRef",
    "initialDepartureMagneticCourse",
    "legTypeCode",
    "numberOfEngines",
    "portRef",
    "procedureDescription",
    "procedureType",
    "reportingCode",
    "rnavFlags",
    "runwaySegmentDetails",
    "sequenceNumber",
    "sidStarApproachIdentifier",
    "speedLimit",
    "turbopropJetIndicator"
})
public class FlightPlanningRecord
    extends A424Record
{

    protected AltitudeConstraint altitude;
    protected String atcIdentifier;
    @XmlSchemaType(name = "string")
    protected AtcWeightCategory atcWeightCategory;
    protected CommonSegmentDetails commonSegmentDetails;
    protected SegmentDetails enrouteDetails;
    protected List<IntermediateFixDetails> intermediateFixDetails;
    protected TimesOfOperation timesOfOperation;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object initialCruiseTableRef;
    protected Course initialDepartureMagneticCourse;
    protected LegTypeCode legTypeCode;
    protected String numberOfEngines;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object portRef;
    protected String procedureDescription;
    @XmlSchemaType(name = "string")
    protected ProcedureType procedureType;
    @XmlSchemaType(name = "string")
    protected ReportingCode reportingCode;
    @XmlSchemaType(name = "string")
    protected RnavFlag rnavFlags;
    protected SegmentDetails runwaySegmentDetails;
    @XmlSchemaType(name = "unsignedInt")
    protected long sequenceNumber;
    protected String sidStarApproachIdentifier;
    protected SpeedLimit speedLimit;
    @XmlSchemaType(name = "string")
    protected TurbopropJetIndicator turbopropJetIndicator;

    /**
     * Gets the value of the altitude property.
     * 
     * @return
     *     possible object is
     *     {@link AltitudeConstraint }
     *     
     */
    public AltitudeConstraint getAltitude() {
        return altitude;
    }

    /**
     * Sets the value of the altitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link AltitudeConstraint }
     *     
     */
    public void setAltitude(AltitudeConstraint value) {
        this.altitude = value;
    }

    /**
     * Gets the value of the atcIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAtcIdentifier() {
        return atcIdentifier;
    }

    /**
     * Sets the value of the atcIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAtcIdentifier(String value) {
        this.atcIdentifier = value;
    }

    /**
     * Gets the value of the atcWeightCategory property.
     * 
     * @return
     *     possible object is
     *     {@link AtcWeightCategory }
     *     
     */
    public AtcWeightCategory getAtcWeightCategory() {
        return atcWeightCategory;
    }

    /**
     * Sets the value of the atcWeightCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link AtcWeightCategory }
     *     
     */
    public void setAtcWeightCategory(AtcWeightCategory value) {
        this.atcWeightCategory = value;
    }

    /**
     * Gets the value of the commonSegmentDetails property.
     * 
     * @return
     *     possible object is
     *     {@link CommonSegmentDetails }
     *     
     */
    public CommonSegmentDetails getCommonSegmentDetails() {
        return commonSegmentDetails;
    }

    /**
     * Sets the value of the commonSegmentDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommonSegmentDetails }
     *     
     */
    public void setCommonSegmentDetails(CommonSegmentDetails value) {
        this.commonSegmentDetails = value;
    }

    /**
     * Gets the value of the enrouteDetails property.
     * 
     * @return
     *     possible object is
     *     {@link SegmentDetails }
     *     
     */
    public SegmentDetails getEnrouteDetails() {
        return enrouteDetails;
    }

    /**
     * Sets the value of the enrouteDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link SegmentDetails }
     *     
     */
    public void setEnrouteDetails(SegmentDetails value) {
        this.enrouteDetails = value;
    }

    /**
     * Gets the value of the intermediateFixDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the intermediateFixDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIntermediateFixDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IntermediateFixDetails }
     * 
     * 
     */
    public List<IntermediateFixDetails> getIntermediateFixDetails() {
        if (intermediateFixDetails == null) {
            intermediateFixDetails = new ArrayList<IntermediateFixDetails>();
        }
        return this.intermediateFixDetails;
    }

    /**
     * Gets the value of the timesOfOperation property.
     * 
     * @return
     *     possible object is
     *     {@link TimesOfOperation }
     *     
     */
    public TimesOfOperation getTimesOfOperation() {
        return timesOfOperation;
    }

    /**
     * Sets the value of the timesOfOperation property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimesOfOperation }
     *     
     */
    public void setTimesOfOperation(TimesOfOperation value) {
        this.timesOfOperation = value;
    }

    /**
     * Gets the value of the initialCruiseTableRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getInitialCruiseTableRef() {
        return initialCruiseTableRef;
    }

    /**
     * Sets the value of the initialCruiseTableRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setInitialCruiseTableRef(Object value) {
        this.initialCruiseTableRef = value;
    }

    /**
     * Gets the value of the initialDepartureMagneticCourse property.
     * 
     * @return
     *     possible object is
     *     {@link Course }
     *     
     */
    public Course getInitialDepartureMagneticCourse() {
        return initialDepartureMagneticCourse;
    }

    /**
     * Sets the value of the initialDepartureMagneticCourse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Course }
     *     
     */
    public void setInitialDepartureMagneticCourse(Course value) {
        this.initialDepartureMagneticCourse = value;
    }

    /**
     * Gets the value of the legTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link LegTypeCode }
     *     
     */
    public LegTypeCode getLegTypeCode() {
        return legTypeCode;
    }

    /**
     * Sets the value of the legTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link LegTypeCode }
     *     
     */
    public void setLegTypeCode(LegTypeCode value) {
        this.legTypeCode = value;
    }

    /**
     * Gets the value of the numberOfEngines property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfEngines() {
        return numberOfEngines;
    }

    /**
     * Sets the value of the numberOfEngines property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfEngines(String value) {
        this.numberOfEngines = value;
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
     * Gets the value of the procedureDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcedureDescription() {
        return procedureDescription;
    }

    /**
     * Sets the value of the procedureDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcedureDescription(String value) {
        this.procedureDescription = value;
    }

    /**
     * Gets the value of the procedureType property.
     * 
     * @return
     *     possible object is
     *     {@link ProcedureType }
     *     
     */
    public ProcedureType getProcedureType() {
        return procedureType;
    }

    /**
     * Sets the value of the procedureType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedureType }
     *     
     */
    public void setProcedureType(ProcedureType value) {
        this.procedureType = value;
    }

    /**
     * Gets the value of the reportingCode property.
     * 
     * @return
     *     possible object is
     *     {@link ReportingCode }
     *     
     */
    public ReportingCode getReportingCode() {
        return reportingCode;
    }

    /**
     * Sets the value of the reportingCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportingCode }
     *     
     */
    public void setReportingCode(ReportingCode value) {
        this.reportingCode = value;
    }

    /**
     * Gets the value of the rnavFlags property.
     * 
     * @return
     *     possible object is
     *     {@link RnavFlag }
     *     
     */
    public RnavFlag getRnavFlags() {
        return rnavFlags;
    }

    /**
     * Sets the value of the rnavFlags property.
     * 
     * @param value
     *     allowed object is
     *     {@link RnavFlag }
     *     
     */
    public void setRnavFlags(RnavFlag value) {
        this.rnavFlags = value;
    }

    /**
     * Gets the value of the runwaySegmentDetails property.
     * 
     * @return
     *     possible object is
     *     {@link SegmentDetails }
     *     
     */
    public SegmentDetails getRunwaySegmentDetails() {
        return runwaySegmentDetails;
    }

    /**
     * Sets the value of the runwaySegmentDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link SegmentDetails }
     *     
     */
    public void setRunwaySegmentDetails(SegmentDetails value) {
        this.runwaySegmentDetails = value;
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
     * Gets the value of the sidStarApproachIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSidStarApproachIdentifier() {
        return sidStarApproachIdentifier;
    }

    /**
     * Sets the value of the sidStarApproachIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSidStarApproachIdentifier(String value) {
        this.sidStarApproachIdentifier = value;
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
     * Gets the value of the turbopropJetIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link TurbopropJetIndicator }
     *     
     */
    public TurbopropJetIndicator getTurbopropJetIndicator() {
        return turbopropJetIndicator;
    }

    /**
     * Sets the value of the turbopropJetIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link TurbopropJetIndicator }
     *     
     */
    public void setTurbopropJetIndicator(TurbopropJetIndicator value) {
        this.turbopropJetIndicator = value;
    }

}
