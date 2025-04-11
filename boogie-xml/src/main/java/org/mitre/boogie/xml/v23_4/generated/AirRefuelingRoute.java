
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                 This complexType extends A424Record.
 *                 A424Record contains supplementalData, areaCode,
 *                 customerCode, cycleDate, notes, and recordType (standard/tailored).
 *                 Use the notes element to store air refueling remarks.
 *                 Sometimes an air refueling route needs to be customized
 *                 for a customer; use customerCode and recordType for that.
 *             
 * 
 * <p>Java class for AirRefuelingRoute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AirRefuelingRoute"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}airRefuelingRouteIdentifier"/&gt;
 *         &lt;element ref="{}direction" minOccurs="0"/&gt;
 *         &lt;element ref="{}typeOfAirRefuelingRoute"/&gt;
 *         &lt;element ref="{}icaoCode"/&gt;
 *         &lt;element ref="{}datumCode" minOccurs="0"/&gt;
 *         &lt;element ref="{}primaryFrequency" minOccurs="0"/&gt;
 *         &lt;element ref="{}backupFrequency" minOccurs="0"/&gt;
 *         &lt;element ref="{}apnRadarBeaconCode" minOccurs="0"/&gt;
 *         &lt;element ref="{}apxRadarBeaconCode" minOccurs="0"/&gt;
 *         &lt;element ref="{}air-to-airY-BandTacanChannelToBeUsedByTheReceiver" minOccurs="0"/&gt;
 *         &lt;element ref="{}air-to-airY-BandTacanChannelToBeUsedByTheTanker" minOccurs="0"/&gt;
 *         &lt;element ref="{}refuelAltitude" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{}pointsDefiningTheAirRefuelingRoute"/&gt;
 *         &lt;element ref="{}airspaceSurroundingTheAirRefuelingRoute" minOccurs="0"/&gt;
 *         &lt;element ref="{}assignedAirRefuelingTrafficControlCenters" minOccurs="0"/&gt;
 *         &lt;element ref="{}scheduling" minOccurs="0"/&gt;
 *         &lt;element ref="{}footnotes" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="referenceId" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirRefuelingRoute", propOrder = {
    "airRefuelingRouteIdentifier",
    "direction",
    "typeOfAirRefuelingRoute",
    "icaoCode",
    "datumCode",
    "primaryFrequency",
    "backupFrequency",
    "apnRadarBeaconCode",
    "apxRadarBeaconCode",
    "airToAirYBandTacanChannelToBeUsedByTheReceiver",
    "airToAirYBandTacanChannelToBeUsedByTheTanker",
    "refuelAltitude",
    "pointsDefiningTheAirRefuelingRoute",
    "airspaceSurroundingTheAirRefuelingRoute",
    "assignedAirRefuelingTrafficControlCenters",
    "scheduling",
    "footnotes"
})
public class AirRefuelingRoute
    extends A424Record
{

    @XmlElement(required = true)
    protected String airRefuelingRouteIdentifier;
    @XmlSchemaType(name = "string")
    protected AirRefuelingDirection direction;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected AirRefuelingType typeOfAirRefuelingRoute;
    @XmlElement(required = true)
    protected String icaoCode;
    @XmlSchemaType(name = "string")
    protected DatumCode datumCode;
    protected Frequency primaryFrequency;
    protected Frequency backupFrequency;
    @XmlSchemaType(name = "integer")
    protected Integer apnRadarBeaconCode;
    protected BigInteger apxRadarBeaconCode;
    @XmlElement(name = "air-to-airY-BandTacanChannelToBeUsedByTheReceiver")
    protected AirRefuelingTacanReceiver airToAirYBandTacanChannelToBeUsedByTheReceiver;
    @XmlElement(name = "air-to-airY-BandTacanChannelToBeUsedByTheTanker")
    protected AirRefuelingTacanTanker airToAirYBandTacanChannelToBeUsedByTheTanker;
    protected List<AltitudeConstraint> refuelAltitude;
    @XmlElement(required = true)
    protected Points pointsDefiningTheAirRefuelingRoute;
    protected Segments airspaceSurroundingTheAirRefuelingRoute;
    protected ATCs assignedAirRefuelingTrafficControlCenters;
    protected Scheduling scheduling;
    protected FootnotesType footnotes;
    @XmlAttribute(name = "referenceId", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String referenceId;

    /**
     * Gets the value of the airRefuelingRouteIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAirRefuelingRouteIdentifier() {
        return airRefuelingRouteIdentifier;
    }

    /**
     * Sets the value of the airRefuelingRouteIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAirRefuelingRouteIdentifier(String value) {
        this.airRefuelingRouteIdentifier = value;
    }

    /**
     * Gets the value of the direction property.
     * 
     * @return
     *     possible object is
     *     {@link AirRefuelingDirection }
     *     
     */
    public AirRefuelingDirection getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirRefuelingDirection }
     *     
     */
    public void setDirection(AirRefuelingDirection value) {
        this.direction = value;
    }

    /**
     * Gets the value of the typeOfAirRefuelingRoute property.
     * 
     * @return
     *     possible object is
     *     {@link AirRefuelingType }
     *     
     */
    public AirRefuelingType getTypeOfAirRefuelingRoute() {
        return typeOfAirRefuelingRoute;
    }

    /**
     * Sets the value of the typeOfAirRefuelingRoute property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirRefuelingType }
     *     
     */
    public void setTypeOfAirRefuelingRoute(AirRefuelingType value) {
        this.typeOfAirRefuelingRoute = value;
    }

    /**
     * Gets the value of the icaoCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIcaoCode() {
        return icaoCode;
    }

    /**
     * Sets the value of the icaoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIcaoCode(String value) {
        this.icaoCode = value;
    }

    /**
     * 
     *                                 The datumCode applies to all locations specified within airRefuelingRoute.
     *                             
     * 
     * @return
     *     possible object is
     *     {@link DatumCode }
     *     
     */
    public DatumCode getDatumCode() {
        return datumCode;
    }

    /**
     * Sets the value of the datumCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatumCode }
     *     
     */
    public void setDatumCode(DatumCode value) {
        this.datumCode = value;
    }

    /**
     * Gets the value of the primaryFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Frequency }
     *     
     */
    public Frequency getPrimaryFrequency() {
        return primaryFrequency;
    }

    /**
     * Sets the value of the primaryFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Frequency }
     *     
     */
    public void setPrimaryFrequency(Frequency value) {
        this.primaryFrequency = value;
    }

    /**
     * Gets the value of the backupFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Frequency }
     *     
     */
    public Frequency getBackupFrequency() {
        return backupFrequency;
    }

    /**
     * Sets the value of the backupFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Frequency }
     *     
     */
    public void setBackupFrequency(Frequency value) {
        this.backupFrequency = value;
    }

    /**
     * Gets the value of the apnRadarBeaconCode property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getApnRadarBeaconCode() {
        return apnRadarBeaconCode;
    }

    /**
     * Sets the value of the apnRadarBeaconCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setApnRadarBeaconCode(Integer value) {
        this.apnRadarBeaconCode = value;
    }

    /**
     * Gets the value of the apxRadarBeaconCode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getApxRadarBeaconCode() {
        return apxRadarBeaconCode;
    }

    /**
     * Sets the value of the apxRadarBeaconCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setApxRadarBeaconCode(BigInteger value) {
        this.apxRadarBeaconCode = value;
    }

    /**
     * Gets the value of the airToAirYBandTacanChannelToBeUsedByTheReceiver property.
     * 
     * @return
     *     possible object is
     *     {@link AirRefuelingTacanReceiver }
     *     
     */
    public AirRefuelingTacanReceiver getAirToAirYBandTacanChannelToBeUsedByTheReceiver() {
        return airToAirYBandTacanChannelToBeUsedByTheReceiver;
    }

    /**
     * Sets the value of the airToAirYBandTacanChannelToBeUsedByTheReceiver property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirRefuelingTacanReceiver }
     *     
     */
    public void setAirToAirYBandTacanChannelToBeUsedByTheReceiver(AirRefuelingTacanReceiver value) {
        this.airToAirYBandTacanChannelToBeUsedByTheReceiver = value;
    }

    /**
     * Gets the value of the airToAirYBandTacanChannelToBeUsedByTheTanker property.
     * 
     * @return
     *     possible object is
     *     {@link AirRefuelingTacanTanker }
     *     
     */
    public AirRefuelingTacanTanker getAirToAirYBandTacanChannelToBeUsedByTheTanker() {
        return airToAirYBandTacanChannelToBeUsedByTheTanker;
    }

    /**
     * Sets the value of the airToAirYBandTacanChannelToBeUsedByTheTanker property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirRefuelingTacanTanker }
     *     
     */
    public void setAirToAirYBandTacanChannelToBeUsedByTheTanker(AirRefuelingTacanTanker value) {
        this.airToAirYBandTacanChannelToBeUsedByTheTanker = value;
    }

    /**
     * Gets the value of the refuelAltitude property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the refuelAltitude property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRefuelAltitude().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AltitudeConstraint }
     * 
     * 
     */
    public List<AltitudeConstraint> getRefuelAltitude() {
        if (refuelAltitude == null) {
            refuelAltitude = new ArrayList<AltitudeConstraint>();
        }
        return this.refuelAltitude;
    }

    /**
     * Gets the value of the pointsDefiningTheAirRefuelingRoute property.
     * 
     * @return
     *     possible object is
     *     {@link Points }
     *     
     */
    public Points getPointsDefiningTheAirRefuelingRoute() {
        return pointsDefiningTheAirRefuelingRoute;
    }

    /**
     * Sets the value of the pointsDefiningTheAirRefuelingRoute property.
     * 
     * @param value
     *     allowed object is
     *     {@link Points }
     *     
     */
    public void setPointsDefiningTheAirRefuelingRoute(Points value) {
        this.pointsDefiningTheAirRefuelingRoute = value;
    }

    /**
     * Gets the value of the airspaceSurroundingTheAirRefuelingRoute property.
     * 
     * @return
     *     possible object is
     *     {@link Segments }
     *     
     */
    public Segments getAirspaceSurroundingTheAirRefuelingRoute() {
        return airspaceSurroundingTheAirRefuelingRoute;
    }

    /**
     * Sets the value of the airspaceSurroundingTheAirRefuelingRoute property.
     * 
     * @param value
     *     allowed object is
     *     {@link Segments }
     *     
     */
    public void setAirspaceSurroundingTheAirRefuelingRoute(Segments value) {
        this.airspaceSurroundingTheAirRefuelingRoute = value;
    }

    /**
     * Gets the value of the assignedAirRefuelingTrafficControlCenters property.
     * 
     * @return
     *     possible object is
     *     {@link ATCs }
     *     
     */
    public ATCs getAssignedAirRefuelingTrafficControlCenters() {
        return assignedAirRefuelingTrafficControlCenters;
    }

    /**
     * Sets the value of the assignedAirRefuelingTrafficControlCenters property.
     * 
     * @param value
     *     allowed object is
     *     {@link ATCs }
     *     
     */
    public void setAssignedAirRefuelingTrafficControlCenters(ATCs value) {
        this.assignedAirRefuelingTrafficControlCenters = value;
    }

    /**
     * Gets the value of the scheduling property.
     * 
     * @return
     *     possible object is
     *     {@link Scheduling }
     *     
     */
    public Scheduling getScheduling() {
        return scheduling;
    }

    /**
     * Sets the value of the scheduling property.
     * 
     * @param value
     *     allowed object is
     *     {@link Scheduling }
     *     
     */
    public void setScheduling(Scheduling value) {
        this.scheduling = value;
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

    /**
     * Gets the value of the referenceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceId() {
        return referenceId;
    }

    /**
     * Sets the value of the referenceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceId(String value) {
        this.referenceId = value;
    }

}
