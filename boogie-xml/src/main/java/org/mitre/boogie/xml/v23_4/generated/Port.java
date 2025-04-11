
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Port complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Port"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Point"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="elevation" type="{http://www.arinc424.com/xml/datatypes}Elevation"/&gt;
 *         &lt;element name="ataIataDesignator" type="{http://www.arinc424.com/xml/datatypes}AtaIataDesignator" minOccurs="0"/&gt;
 *         &lt;element name="daylightIndicator" type="{http://www.arinc424.com/xml/datatypes}DaylightIndicator" minOccurs="0"/&gt;
 *         &lt;element name="isIfrCapable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="magneticTrueIndicator" type="{http://www.arinc424.com/xml/enumerations}MagneticTrueIndicator" minOccurs="0"/&gt;
 *         &lt;element name="publicMilitaryIndicator" type="{http://www.arinc424.com/xml/enumerations}PublicMilitaryIndicator" minOccurs="0"/&gt;
 *         &lt;element name="recommendedNavaidRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="speedLimit" type="{http://www.arinc424.com/xml/datatypes}SpeedLimit" minOccurs="0"/&gt;
 *         &lt;element name="speedLimitAltitude" type="{http://www.arinc424.com/xml/datatypes}Constraint" minOccurs="0"/&gt;
 *         &lt;element name="utcOffset" type="{http://www.arinc424.com/xml/datatypes}UtcOffset" minOccurs="0"/&gt;
 *         &lt;element name="transitionAltitude" type="{http://www.arinc424.com/xml/datatypes}TransitionAltitude" minOccurs="0"/&gt;
 *         &lt;element name="transitionLevel" type="{http://www.arinc424.com/xml/datatypes}TransitionAltitude" minOccurs="0"/&gt;
 *         &lt;element name="terminalNdb" type="{}TerminalNdb" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="terminalProcedures" type="{}TerminalProcedures" minOccurs="0"/&gt;
 *         &lt;element name="terminalWaypoint" type="{}TerminalWaypoint" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taa" type="{}Taa" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="portCommunication" type="{}PortCommunication" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="helipad" type="{}Helipad" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="localizerMarker" type="{}AirportHeliportLocalizerMarker" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="localizerGlideslope" type="{}LocalizerGlideslope" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="gls" type="{}Gls" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="mls" type="{}Mls" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="msa" type="{}Msa" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="flightPlanningArrivalDepartureRecords" type="{}FlightPlanningRecords" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="isVFRCheckpoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="controlledAsArptIndicator" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="controlledAsIndicator" type="{http://www.arinc424.com/xml/enumerations}ControlledAsIndicator" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Port", propOrder = {
    "elevation",
    "ataIataDesignator",
    "daylightIndicator",
    "isIfrCapable",
    "magneticTrueIndicator",
    "publicMilitaryIndicator",
    "recommendedNavaidRef",
    "speedLimit",
    "speedLimitAltitude",
    "utcOffset",
    "transitionAltitude",
    "transitionLevel",
    "terminalNdb",
    "terminalProcedures",
    "terminalWaypoint",
    "taa",
    "portCommunication",
    "helipad",
    "localizerMarker",
    "localizerGlideslope",
    "gls",
    "mls",
    "msa",
    "flightPlanningArrivalDepartureRecords",
    "isVFRCheckpoint",
    "controlledAsArptIndicator",
    "controlledAsIndicator"
})
@XmlSeeAlso({
    Airport.class,
    Heliport.class
})
public abstract class Port
    extends A424Point
{

    protected int elevation;
    protected String ataIataDesignator;
    protected Boolean daylightIndicator;
    @XmlElement(defaultValue = "false")
    protected Boolean isIfrCapable;
    @XmlSchemaType(name = "string")
    protected MagneticTrueIndicator magneticTrueIndicator;
    @XmlSchemaType(name = "string")
    protected PublicMilitaryIndicator publicMilitaryIndicator;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object recommendedNavaidRef;
    protected SpeedLimit speedLimit;
    protected Constraint speedLimitAltitude;
    protected UtcOffset utcOffset;
    @XmlSchemaType(name = "integer")
    protected Integer transitionAltitude;
    @XmlSchemaType(name = "integer")
    protected Integer transitionLevel;
    protected List<TerminalNdb> terminalNdb;
    protected TerminalProcedures terminalProcedures;
    protected List<TerminalWaypoint> terminalWaypoint;
    protected List<Taa> taa;
    protected List<PortCommunication> portCommunication;
    protected List<Helipad> helipad;
    protected List<AirportHeliportLocalizerMarker> localizerMarker;
    protected List<LocalizerGlideslope> localizerGlideslope;
    protected List<Gls> gls;
    protected List<Mls> mls;
    protected List<Msa> msa;
    protected List<FlightPlanningRecords> flightPlanningArrivalDepartureRecords;
    @XmlElement(defaultValue = "false")
    protected Boolean isVFRCheckpoint;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object controlledAsArptIndicator;
    @XmlSchemaType(name = "string")
    protected ControlledAsIndicator controlledAsIndicator;

    /**
     * Gets the value of the elevation property.
     * 
     */
    public int getElevation() {
        return elevation;
    }

    /**
     * Sets the value of the elevation property.
     * 
     */
    public void setElevation(int value) {
        this.elevation = value;
    }

    /**
     * Gets the value of the ataIataDesignator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAtaIataDesignator() {
        return ataIataDesignator;
    }

    /**
     * Sets the value of the ataIataDesignator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAtaIataDesignator(String value) {
        this.ataIataDesignator = value;
    }

    /**
     * Gets the value of the daylightIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDaylightIndicator() {
        return daylightIndicator;
    }

    /**
     * Sets the value of the daylightIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDaylightIndicator(Boolean value) {
        this.daylightIndicator = value;
    }

    /**
     * Gets the value of the isIfrCapable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsIfrCapable() {
        return isIfrCapable;
    }

    /**
     * Sets the value of the isIfrCapable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsIfrCapable(Boolean value) {
        this.isIfrCapable = value;
    }

    /**
     * Gets the value of the magneticTrueIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link MagneticTrueIndicator }
     *     
     */
    public MagneticTrueIndicator getMagneticTrueIndicator() {
        return magneticTrueIndicator;
    }

    /**
     * Sets the value of the magneticTrueIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link MagneticTrueIndicator }
     *     
     */
    public void setMagneticTrueIndicator(MagneticTrueIndicator value) {
        this.magneticTrueIndicator = value;
    }

    /**
     * Gets the value of the publicMilitaryIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link PublicMilitaryIndicator }
     *     
     */
    public PublicMilitaryIndicator getPublicMilitaryIndicator() {
        return publicMilitaryIndicator;
    }

    /**
     * Sets the value of the publicMilitaryIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link PublicMilitaryIndicator }
     *     
     */
    public void setPublicMilitaryIndicator(PublicMilitaryIndicator value) {
        this.publicMilitaryIndicator = value;
    }

    /**
     * Gets the value of the recommendedNavaidRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRecommendedNavaidRef() {
        return recommendedNavaidRef;
    }

    /**
     * Sets the value of the recommendedNavaidRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRecommendedNavaidRef(Object value) {
        this.recommendedNavaidRef = value;
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
     * Gets the value of the speedLimitAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link Constraint }
     *     
     */
    public Constraint getSpeedLimitAltitude() {
        return speedLimitAltitude;
    }

    /**
     * Sets the value of the speedLimitAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Constraint }
     *     
     */
    public void setSpeedLimitAltitude(Constraint value) {
        this.speedLimitAltitude = value;
    }

    /**
     * Gets the value of the utcOffset property.
     * 
     * @return
     *     possible object is
     *     {@link UtcOffset }
     *     
     */
    public UtcOffset getUtcOffset() {
        return utcOffset;
    }

    /**
     * Sets the value of the utcOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link UtcOffset }
     *     
     */
    public void setUtcOffset(UtcOffset value) {
        this.utcOffset = value;
    }

    /**
     * Gets the value of the transitionAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTransitionAltitude() {
        return transitionAltitude;
    }

    /**
     * Sets the value of the transitionAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTransitionAltitude(Integer value) {
        this.transitionAltitude = value;
    }

    /**
     * Gets the value of the transitionLevel property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTransitionLevel() {
        return transitionLevel;
    }

    /**
     * Sets the value of the transitionLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTransitionLevel(Integer value) {
        this.transitionLevel = value;
    }

    /**
     * Gets the value of the terminalNdb property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the terminalNdb property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTerminalNdb().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TerminalNdb }
     * 
     * 
     */
    public List<TerminalNdb> getTerminalNdb() {
        if (terminalNdb == null) {
            terminalNdb = new ArrayList<TerminalNdb>();
        }
        return this.terminalNdb;
    }

    /**
     * Gets the value of the terminalProcedures property.
     * 
     * @return
     *     possible object is
     *     {@link TerminalProcedures }
     *     
     */
    public TerminalProcedures getTerminalProcedures() {
        return terminalProcedures;
    }

    /**
     * Sets the value of the terminalProcedures property.
     * 
     * @param value
     *     allowed object is
     *     {@link TerminalProcedures }
     *     
     */
    public void setTerminalProcedures(TerminalProcedures value) {
        this.terminalProcedures = value;
    }

    /**
     * Gets the value of the terminalWaypoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the terminalWaypoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTerminalWaypoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TerminalWaypoint }
     * 
     * 
     */
    public List<TerminalWaypoint> getTerminalWaypoint() {
        if (terminalWaypoint == null) {
            terminalWaypoint = new ArrayList<TerminalWaypoint>();
        }
        return this.terminalWaypoint;
    }

    /**
     * Gets the value of the taa property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the taa property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaa().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Taa }
     * 
     * 
     */
    public List<Taa> getTaa() {
        if (taa == null) {
            taa = new ArrayList<Taa>();
        }
        return this.taa;
    }

    /**
     * Gets the value of the portCommunication property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the portCommunication property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPortCommunication().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PortCommunication }
     * 
     * 
     */
    public List<PortCommunication> getPortCommunication() {
        if (portCommunication == null) {
            portCommunication = new ArrayList<PortCommunication>();
        }
        return this.portCommunication;
    }

    /**
     * Gets the value of the helipad property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the helipad property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHelipad().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Helipad }
     * 
     * 
     */
    public List<Helipad> getHelipad() {
        if (helipad == null) {
            helipad = new ArrayList<Helipad>();
        }
        return this.helipad;
    }

    /**
     * Gets the value of the localizerMarker property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the localizerMarker property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocalizerMarker().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AirportHeliportLocalizerMarker }
     * 
     * 
     */
    public List<AirportHeliportLocalizerMarker> getLocalizerMarker() {
        if (localizerMarker == null) {
            localizerMarker = new ArrayList<AirportHeliportLocalizerMarker>();
        }
        return this.localizerMarker;
    }

    /**
     * Gets the value of the localizerGlideslope property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the localizerGlideslope property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocalizerGlideslope().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LocalizerGlideslope }
     * 
     * 
     */
    public List<LocalizerGlideslope> getLocalizerGlideslope() {
        if (localizerGlideslope == null) {
            localizerGlideslope = new ArrayList<LocalizerGlideslope>();
        }
        return this.localizerGlideslope;
    }

    /**
     * Gets the value of the gls property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the gls property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGls().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Gls }
     * 
     * 
     */
    public List<Gls> getGls() {
        if (gls == null) {
            gls = new ArrayList<Gls>();
        }
        return this.gls;
    }

    /**
     * Gets the value of the mls property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the mls property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMls().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Mls }
     * 
     * 
     */
    public List<Mls> getMls() {
        if (mls == null) {
            mls = new ArrayList<Mls>();
        }
        return this.mls;
    }

    /**
     * Gets the value of the msa property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the msa property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMsa().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Msa }
     * 
     * 
     */
    public List<Msa> getMsa() {
        if (msa == null) {
            msa = new ArrayList<Msa>();
        }
        return this.msa;
    }

    /**
     * Gets the value of the flightPlanningArrivalDepartureRecords property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the flightPlanningArrivalDepartureRecords property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFlightPlanningArrivalDepartureRecords().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FlightPlanningRecords }
     * 
     * 
     */
    public List<FlightPlanningRecords> getFlightPlanningArrivalDepartureRecords() {
        if (flightPlanningArrivalDepartureRecords == null) {
            flightPlanningArrivalDepartureRecords = new ArrayList<FlightPlanningRecords>();
        }
        return this.flightPlanningArrivalDepartureRecords;
    }

    /**
     * Gets the value of the isVFRCheckpoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsVFRCheckpoint() {
        return isVFRCheckpoint;
    }

    /**
     * Sets the value of the isVFRCheckpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsVFRCheckpoint(Boolean value) {
        this.isVFRCheckpoint = value;
    }

    /**
     * Gets the value of the controlledAsArptIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getControlledAsArptIndicator() {
        return controlledAsArptIndicator;
    }

    /**
     * Sets the value of the controlledAsArptIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setControlledAsArptIndicator(Object value) {
        this.controlledAsArptIndicator = value;
    }

    /**
     * Gets the value of the controlledAsIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link ControlledAsIndicator }
     *     
     */
    public ControlledAsIndicator getControlledAsIndicator() {
        return controlledAsIndicator;
    }

    /**
     * Sets the value of the controlledAsIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlledAsIndicator }
     *     
     */
    public void setControlledAsIndicator(ControlledAsIndicator value) {
        this.controlledAsIndicator = value;
    }

}
