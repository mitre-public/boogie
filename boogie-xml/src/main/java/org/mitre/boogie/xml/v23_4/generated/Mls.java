
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This record represents a Microwave Landing System, including the Azimuth station, the Elevation station and the Back Azimuth station if installed.
 * 
 * <p>Java class for Mls complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Mls"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}PrecisionApproachNavaid"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="azimuthCoverageLeft" type="{http://www.arinc424.com/xml/datatypes}AzimuthCoverageSectorRightLeft" minOccurs="0"/&gt;
 *         &lt;element name="azimuthCoverageRight" type="{http://www.arinc424.com/xml/datatypes}AzimuthCoverageSectorRightLeft" minOccurs="0"/&gt;
 *         &lt;element name="azimuthPosition" type="{http://www.arinc424.com/xml/datatypes}BeamPosition" minOccurs="0"/&gt;
 *         &lt;element name="azimuthPositionReference" type="{http://www.arinc424.com/xml/enumerations}LocalizerAzimuthPositionReference" minOccurs="0"/&gt;
 *         &lt;element name="azimuthProportionalAngleLeft" type="{http://www.arinc424.com/xml/datatypes}AzimuthProportionalAngleRightLeft" minOccurs="0"/&gt;
 *         &lt;element name="azimuthProportionalAngleRight" type="{http://www.arinc424.com/xml/datatypes}AzimuthProportionalAngleRightLeft" minOccurs="0"/&gt;
 *         &lt;element name="elevationAngleSpan" type="{http://www.arinc424.com/xml/datatypes}ElevationAngleSpan" minOccurs="0"/&gt;
 *         &lt;element name="elevationLocation" type="{http://www.arinc424.com/xml/datatypes}Location" minOccurs="0"/&gt;
 *         &lt;element name="elevationPosition" type="{http://www.arinc424.com/xml/datatypes}BeamPosition" minOccurs="0"/&gt;
 *         &lt;element name="mlsBackAzimuthDetails" type="{}MlsBackAzimuthDetails" minOccurs="0"/&gt;
 *         &lt;element name="nominalElevationAngle" type="{http://www.arinc424.com/xml/datatypes}NominalElevationAngle" minOccurs="0"/&gt;
 *         &lt;element name="supportingFacilityRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="mlsChannel" type="{http://www.arinc424.com/xml/datatypes}Channel"/&gt;
 *         &lt;element name="mlsDmeLocation" type="{http://www.arinc424.com/xml/enumerations}MlsDmeLocation" minOccurs="0"/&gt;
 *         &lt;element name="mlsApproachAzimuthScanRate" type="{http://www.arinc424.com/xml/enumerations}MlsApproachAzimuthScanRate" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Mls", propOrder = {
    "azimuthCoverageLeft",
    "azimuthCoverageRight",
    "azimuthPosition",
    "azimuthPositionReference",
    "azimuthProportionalAngleLeft",
    "azimuthProportionalAngleRight",
    "elevationAngleSpan",
    "elevationLocation",
    "elevationPosition",
    "mlsBackAzimuthDetails",
    "nominalElevationAngle",
    "supportingFacilityRef",
    "mlsChannel",
    "mlsDmeLocation",
    "mlsApproachAzimuthScanRate"
})
public class Mls
    extends PrecisionApproachNavaid
{

    @XmlSchemaType(name = "unsignedInt")
    protected Long azimuthCoverageLeft;
    @XmlSchemaType(name = "unsignedInt")
    protected Long azimuthCoverageRight;
    @XmlSchemaType(name = "unsignedInt")
    protected Long azimuthPosition;
    @XmlSchemaType(name = "string")
    protected LocalizerAzimuthPositionReference azimuthPositionReference;
    @XmlSchemaType(name = "unsignedInt")
    protected Long azimuthProportionalAngleLeft;
    @XmlSchemaType(name = "unsignedInt")
    protected Long azimuthProportionalAngleRight;
    @XmlSchemaType(name = "unsignedInt")
    protected Long elevationAngleSpan;
    protected Location elevationLocation;
    @XmlSchemaType(name = "unsignedInt")
    protected Long elevationPosition;
    protected MlsBackAzimuthDetails mlsBackAzimuthDetails;
    @XmlSchemaType(name = "unsignedInt")
    protected Long nominalElevationAngle;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object supportingFacilityRef;
    @XmlSchemaType(name = "unsignedInt")
    protected long mlsChannel;
    @XmlSchemaType(name = "string")
    protected MlsDmeLocation mlsDmeLocation;
    @XmlSchemaType(name = "string")
    protected MlsApproachAzimuthScanRate mlsApproachAzimuthScanRate;

    /**
     * Gets the value of the azimuthCoverageLeft property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAzimuthCoverageLeft() {
        return azimuthCoverageLeft;
    }

    /**
     * Sets the value of the azimuthCoverageLeft property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAzimuthCoverageLeft(Long value) {
        this.azimuthCoverageLeft = value;
    }

    /**
     * Gets the value of the azimuthCoverageRight property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAzimuthCoverageRight() {
        return azimuthCoverageRight;
    }

    /**
     * Sets the value of the azimuthCoverageRight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAzimuthCoverageRight(Long value) {
        this.azimuthCoverageRight = value;
    }

    /**
     * Gets the value of the azimuthPosition property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAzimuthPosition() {
        return azimuthPosition;
    }

    /**
     * Sets the value of the azimuthPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAzimuthPosition(Long value) {
        this.azimuthPosition = value;
    }

    /**
     * Gets the value of the azimuthPositionReference property.
     * 
     * @return
     *     possible object is
     *     {@link LocalizerAzimuthPositionReference }
     *     
     */
    public LocalizerAzimuthPositionReference getAzimuthPositionReference() {
        return azimuthPositionReference;
    }

    /**
     * Sets the value of the azimuthPositionReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalizerAzimuthPositionReference }
     *     
     */
    public void setAzimuthPositionReference(LocalizerAzimuthPositionReference value) {
        this.azimuthPositionReference = value;
    }

    /**
     * Gets the value of the azimuthProportionalAngleLeft property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAzimuthProportionalAngleLeft() {
        return azimuthProportionalAngleLeft;
    }

    /**
     * Sets the value of the azimuthProportionalAngleLeft property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAzimuthProportionalAngleLeft(Long value) {
        this.azimuthProportionalAngleLeft = value;
    }

    /**
     * Gets the value of the azimuthProportionalAngleRight property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAzimuthProportionalAngleRight() {
        return azimuthProportionalAngleRight;
    }

    /**
     * Sets the value of the azimuthProportionalAngleRight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAzimuthProportionalAngleRight(Long value) {
        this.azimuthProportionalAngleRight = value;
    }

    /**
     * Gets the value of the elevationAngleSpan property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getElevationAngleSpan() {
        return elevationAngleSpan;
    }

    /**
     * Sets the value of the elevationAngleSpan property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setElevationAngleSpan(Long value) {
        this.elevationAngleSpan = value;
    }

    /**
     * Gets the value of the elevationLocation property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getElevationLocation() {
        return elevationLocation;
    }

    /**
     * Sets the value of the elevationLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setElevationLocation(Location value) {
        this.elevationLocation = value;
    }

    /**
     * Gets the value of the elevationPosition property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getElevationPosition() {
        return elevationPosition;
    }

    /**
     * Sets the value of the elevationPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setElevationPosition(Long value) {
        this.elevationPosition = value;
    }

    /**
     * Gets the value of the mlsBackAzimuthDetails property.
     * 
     * @return
     *     possible object is
     *     {@link MlsBackAzimuthDetails }
     *     
     */
    public MlsBackAzimuthDetails getMlsBackAzimuthDetails() {
        return mlsBackAzimuthDetails;
    }

    /**
     * Sets the value of the mlsBackAzimuthDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link MlsBackAzimuthDetails }
     *     
     */
    public void setMlsBackAzimuthDetails(MlsBackAzimuthDetails value) {
        this.mlsBackAzimuthDetails = value;
    }

    /**
     * Gets the value of the nominalElevationAngle property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNominalElevationAngle() {
        return nominalElevationAngle;
    }

    /**
     * Sets the value of the nominalElevationAngle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNominalElevationAngle(Long value) {
        this.nominalElevationAngle = value;
    }

    /**
     * Gets the value of the supportingFacilityRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSupportingFacilityRef() {
        return supportingFacilityRef;
    }

    /**
     * Sets the value of the supportingFacilityRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSupportingFacilityRef(Object value) {
        this.supportingFacilityRef = value;
    }

    /**
     * Gets the value of the mlsChannel property.
     * 
     */
    public long getMlsChannel() {
        return mlsChannel;
    }

    /**
     * Sets the value of the mlsChannel property.
     * 
     */
    public void setMlsChannel(long value) {
        this.mlsChannel = value;
    }

    /**
     * Gets the value of the mlsDmeLocation property.
     * 
     * @return
     *     possible object is
     *     {@link MlsDmeLocation }
     *     
     */
    public MlsDmeLocation getMlsDmeLocation() {
        return mlsDmeLocation;
    }

    /**
     * Sets the value of the mlsDmeLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link MlsDmeLocation }
     *     
     */
    public void setMlsDmeLocation(MlsDmeLocation value) {
        this.mlsDmeLocation = value;
    }

    /**
     * Gets the value of the mlsApproachAzimuthScanRate property.
     * 
     * @return
     *     possible object is
     *     {@link MlsApproachAzimuthScanRate }
     *     
     */
    public MlsApproachAzimuthScanRate getMlsApproachAzimuthScanRate() {
        return mlsApproachAzimuthScanRate;
    }

    /**
     * Sets the value of the mlsApproachAzimuthScanRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link MlsApproachAzimuthScanRate }
     *     
     */
    public void setMlsApproachAzimuthScanRate(MlsApproachAzimuthScanRate value) {
        this.mlsApproachAzimuthScanRate = value;
    }

}
