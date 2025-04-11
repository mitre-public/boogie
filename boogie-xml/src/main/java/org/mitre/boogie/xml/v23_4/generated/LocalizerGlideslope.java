
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Localizer and Glideslope Records.
 * 
 * <p>Java class for LocalizerGlideslope complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LocalizerGlideslope"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}PrecisionApproachNavaid"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="approachRouteIdent" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier" maxOccurs="4" minOccurs="0"/&gt;
 *         &lt;element name="glideslopeBeamWidth" type="{http://www.arinc424.com/xml/datatypes}GlideslopeBeamWidth" minOccurs="0"/&gt;
 *         &lt;element name="glideslopeHeightAtLandingThreshold" type="{http://www.arinc424.com/xml/datatypes}ThresholdCrossingHeight" minOccurs="0"/&gt;
 *         &lt;element name="glideslopeLocation" type="{http://www.arinc424.com/xml/datatypes}Location" minOccurs="0"/&gt;
 *         &lt;element name="glideslopePosition" type="{http://www.arinc424.com/xml/datatypes}BeamPosition" minOccurs="0"/&gt;
 *         &lt;element name="localizerPosition" type="{http://www.arinc424.com/xml/datatypes}BeamPosition" minOccurs="0"/&gt;
 *         &lt;element name="localizerPositionReference" type="{http://www.arinc424.com/xml/enumerations}LocalizerAzimuthPositionReference" minOccurs="0"/&gt;
 *         &lt;element name="localizerTrueBearing" type="{http://www.arinc424.com/xml/datatypes}TrueBearing" minOccurs="0"/&gt;
 *         &lt;element name="localizerTrueBearingSource" type="{http://www.arinc424.com/xml/enumerations}TrueBearingSource" minOccurs="0"/&gt;
 *         &lt;element name="localizerWidth" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="stationDeclination" type="{http://www.arinc424.com/xml/datatypes}StationDeclination" minOccurs="0"/&gt;
 *         &lt;element name="supportingFacilityReference" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="localizerGlideslopeFrequency" type="{http://www.arinc424.com/xml/datatypes}Frequency"/&gt;
 *         &lt;element name="ilsBackCourse" type="{http://www.arinc424.com/xml/enumerations}IlsBackCourse" minOccurs="0"/&gt;
 *         &lt;element name="ilsDmeLocation" type="{http://www.arinc424.com/xml/enumerations}IlsDmeLocation" minOccurs="0"/&gt;
 *         &lt;element name="navaidClass" type="{http://www.arinc424.com/xml/datatypes}VhfNavaidClass" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LocalizerGlideslope", propOrder = {
    "approachRouteIdent",
    "glideslopeBeamWidth",
    "glideslopeHeightAtLandingThreshold",
    "glideslopeLocation",
    "glideslopePosition",
    "localizerPosition",
    "localizerPositionReference",
    "localizerTrueBearing",
    "localizerTrueBearingSource",
    "localizerWidth",
    "stationDeclination",
    "supportingFacilityReference",
    "localizerGlideslopeFrequency",
    "ilsBackCourse",
    "ilsDmeLocation",
    "navaidClass"
})
public class LocalizerGlideslope
    extends PrecisionApproachNavaid
{

    protected List<String> approachRouteIdent;
    protected BigDecimal glideslopeBeamWidth;
    @XmlSchemaType(name = "unsignedInt")
    protected Long glideslopeHeightAtLandingThreshold;
    protected Location glideslopeLocation;
    @XmlSchemaType(name = "unsignedInt")
    protected Long glideslopePosition;
    @XmlSchemaType(name = "unsignedInt")
    protected Long localizerPosition;
    @XmlSchemaType(name = "string")
    protected LocalizerAzimuthPositionReference localizerPositionReference;
    protected BigDecimal localizerTrueBearing;
    @XmlSchemaType(name = "string")
    protected TrueBearingSource localizerTrueBearingSource;
    protected BigDecimal localizerWidth;
    protected StationDeclination stationDeclination;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object supportingFacilityReference;
    @XmlElement(required = true)
    protected Frequency localizerGlideslopeFrequency;
    @XmlSchemaType(name = "string")
    protected IlsBackCourse ilsBackCourse;
    @XmlSchemaType(name = "string")
    protected IlsDmeLocation ilsDmeLocation;
    protected VhfNavaidClass navaidClass;

    /**
     * Gets the value of the approachRouteIdent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the approachRouteIdent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApproachRouteIdent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getApproachRouteIdent() {
        if (approachRouteIdent == null) {
            approachRouteIdent = new ArrayList<String>();
        }
        return this.approachRouteIdent;
    }

    /**
     * Gets the value of the glideslopeBeamWidth property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGlideslopeBeamWidth() {
        return glideslopeBeamWidth;
    }

    /**
     * Sets the value of the glideslopeBeamWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGlideslopeBeamWidth(BigDecimal value) {
        this.glideslopeBeamWidth = value;
    }

    /**
     * Gets the value of the glideslopeHeightAtLandingThreshold property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getGlideslopeHeightAtLandingThreshold() {
        return glideslopeHeightAtLandingThreshold;
    }

    /**
     * Sets the value of the glideslopeHeightAtLandingThreshold property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setGlideslopeHeightAtLandingThreshold(Long value) {
        this.glideslopeHeightAtLandingThreshold = value;
    }

    /**
     * Gets the value of the glideslopeLocation property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getGlideslopeLocation() {
        return glideslopeLocation;
    }

    /**
     * Sets the value of the glideslopeLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setGlideslopeLocation(Location value) {
        this.glideslopeLocation = value;
    }

    /**
     * Gets the value of the glideslopePosition property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getGlideslopePosition() {
        return glideslopePosition;
    }

    /**
     * Sets the value of the glideslopePosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setGlideslopePosition(Long value) {
        this.glideslopePosition = value;
    }

    /**
     * Gets the value of the localizerPosition property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLocalizerPosition() {
        return localizerPosition;
    }

    /**
     * Sets the value of the localizerPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLocalizerPosition(Long value) {
        this.localizerPosition = value;
    }

    /**
     * Gets the value of the localizerPositionReference property.
     * 
     * @return
     *     possible object is
     *     {@link LocalizerAzimuthPositionReference }
     *     
     */
    public LocalizerAzimuthPositionReference getLocalizerPositionReference() {
        return localizerPositionReference;
    }

    /**
     * Sets the value of the localizerPositionReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalizerAzimuthPositionReference }
     *     
     */
    public void setLocalizerPositionReference(LocalizerAzimuthPositionReference value) {
        this.localizerPositionReference = value;
    }

    /**
     * Gets the value of the localizerTrueBearing property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLocalizerTrueBearing() {
        return localizerTrueBearing;
    }

    /**
     * Sets the value of the localizerTrueBearing property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLocalizerTrueBearing(BigDecimal value) {
        this.localizerTrueBearing = value;
    }

    /**
     * Gets the value of the localizerTrueBearingSource property.
     * 
     * @return
     *     possible object is
     *     {@link TrueBearingSource }
     *     
     */
    public TrueBearingSource getLocalizerTrueBearingSource() {
        return localizerTrueBearingSource;
    }

    /**
     * Sets the value of the localizerTrueBearingSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueBearingSource }
     *     
     */
    public void setLocalizerTrueBearingSource(TrueBearingSource value) {
        this.localizerTrueBearingSource = value;
    }

    /**
     * Gets the value of the localizerWidth property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLocalizerWidth() {
        return localizerWidth;
    }

    /**
     * Sets the value of the localizerWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLocalizerWidth(BigDecimal value) {
        this.localizerWidth = value;
    }

    /**
     * Gets the value of the stationDeclination property.
     * 
     * @return
     *     possible object is
     *     {@link StationDeclination }
     *     
     */
    public StationDeclination getStationDeclination() {
        return stationDeclination;
    }

    /**
     * Sets the value of the stationDeclination property.
     * 
     * @param value
     *     allowed object is
     *     {@link StationDeclination }
     *     
     */
    public void setStationDeclination(StationDeclination value) {
        this.stationDeclination = value;
    }

    /**
     * Gets the value of the supportingFacilityReference property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSupportingFacilityReference() {
        return supportingFacilityReference;
    }

    /**
     * Sets the value of the supportingFacilityReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSupportingFacilityReference(Object value) {
        this.supportingFacilityReference = value;
    }

    /**
     * Gets the value of the localizerGlideslopeFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Frequency }
     *     
     */
    public Frequency getLocalizerGlideslopeFrequency() {
        return localizerGlideslopeFrequency;
    }

    /**
     * Sets the value of the localizerGlideslopeFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Frequency }
     *     
     */
    public void setLocalizerGlideslopeFrequency(Frequency value) {
        this.localizerGlideslopeFrequency = value;
    }

    /**
     * Gets the value of the ilsBackCourse property.
     * 
     * @return
     *     possible object is
     *     {@link IlsBackCourse }
     *     
     */
    public IlsBackCourse getIlsBackCourse() {
        return ilsBackCourse;
    }

    /**
     * Sets the value of the ilsBackCourse property.
     * 
     * @param value
     *     allowed object is
     *     {@link IlsBackCourse }
     *     
     */
    public void setIlsBackCourse(IlsBackCourse value) {
        this.ilsBackCourse = value;
    }

    /**
     * Gets the value of the ilsDmeLocation property.
     * 
     * @return
     *     possible object is
     *     {@link IlsDmeLocation }
     *     
     */
    public IlsDmeLocation getIlsDmeLocation() {
        return ilsDmeLocation;
    }

    /**
     * Sets the value of the ilsDmeLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link IlsDmeLocation }
     *     
     */
    public void setIlsDmeLocation(IlsDmeLocation value) {
        this.ilsDmeLocation = value;
    }

    /**
     * Gets the value of the navaidClass property.
     * 
     * @return
     *     possible object is
     *     {@link VhfNavaidClass }
     *     
     */
    public VhfNavaidClass getNavaidClass() {
        return navaidClass;
    }

    /**
     * Sets the value of the navaidClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link VhfNavaidClass }
     *     
     */
    public void setNavaidClass(VhfNavaidClass value) {
        this.navaidClass = value;
    }

}
