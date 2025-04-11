
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in MLS Back Azimuth Details.
 * 
 * <p>Java class for MlsBackAzimuthDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MlsBackAzimuthDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Point"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="azimuthTrueBearing" type="{http://www.arinc424.com/xml/datatypes}TrueBearing" minOccurs="0"/&gt;
 *         &lt;element name="azimuthTrueBearingSource" type="{http://www.arinc424.com/xml/enumerations}TrueBearingSource" minOccurs="0"/&gt;
 *         &lt;element name="backAzimuthBearing" type="{http://www.arinc424.com/xml/datatypes}Bearing" minOccurs="0"/&gt;
 *         &lt;element name="backAzimuthCoverageLeft" type="{http://www.arinc424.com/xml/datatypes}AzimuthCoverageSectorRightLeft" minOccurs="0"/&gt;
 *         &lt;element name="backAzimuthCoverageRight" type="{http://www.arinc424.com/xml/datatypes}AzimuthCoverageSectorRightLeft" minOccurs="0"/&gt;
 *         &lt;element name="backAzimuthPosition" type="{http://www.arinc424.com/xml/datatypes}BeamPosition" minOccurs="0"/&gt;
 *         &lt;element name="backAzimuthPositionReference" type="{http://www.arinc424.com/xml/enumerations}LocalizerAzimuthPositionReference" minOccurs="0"/&gt;
 *         &lt;element name="backAzimuthProportionalSectorLeft" type="{http://www.arinc424.com/xml/datatypes}AzimuthProportionalAngleRightLeft" minOccurs="0"/&gt;
 *         &lt;element name="backAzimuthProportionalSectorRight" type="{http://www.arinc424.com/xml/datatypes}AzimuthProportionalAngleRightLeft" minOccurs="0"/&gt;
 *         &lt;element name="backAzimuthTrueBearing" type="{http://www.arinc424.com/xml/datatypes}TrueBearing" minOccurs="0"/&gt;
 *         &lt;element name="backAzimuthTrueBearingSource" type="{http://www.arinc424.com/xml/enumerations}TrueBearingSource" minOccurs="0"/&gt;
 *         &lt;element name="glidePathHeightAtLandingThreshold" type="{http://www.arinc424.com/xml/datatypes}ThresholdCrossingHeight" minOccurs="0"/&gt;
 *         &lt;element name="mlsDatumPointLocation" type="{http://www.arinc424.com/xml/datatypes}Location" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MlsBackAzimuthDetails", propOrder = {
    "azimuthTrueBearing",
    "azimuthTrueBearingSource",
    "backAzimuthBearing",
    "backAzimuthCoverageLeft",
    "backAzimuthCoverageRight",
    "backAzimuthPosition",
    "backAzimuthPositionReference",
    "backAzimuthProportionalSectorLeft",
    "backAzimuthProportionalSectorRight",
    "backAzimuthTrueBearing",
    "backAzimuthTrueBearingSource",
    "glidePathHeightAtLandingThreshold",
    "mlsDatumPointLocation"
})
public class MlsBackAzimuthDetails
    extends A424Point
{

    protected BigDecimal azimuthTrueBearing;
    @XmlSchemaType(name = "string")
    protected TrueBearingSource azimuthTrueBearingSource;
    protected Bearing backAzimuthBearing;
    @XmlSchemaType(name = "unsignedInt")
    protected Long backAzimuthCoverageLeft;
    @XmlSchemaType(name = "unsignedInt")
    protected Long backAzimuthCoverageRight;
    @XmlSchemaType(name = "unsignedInt")
    protected Long backAzimuthPosition;
    @XmlSchemaType(name = "string")
    protected LocalizerAzimuthPositionReference backAzimuthPositionReference;
    @XmlSchemaType(name = "unsignedInt")
    protected Long backAzimuthProportionalSectorLeft;
    @XmlSchemaType(name = "unsignedInt")
    protected Long backAzimuthProportionalSectorRight;
    protected BigDecimal backAzimuthTrueBearing;
    @XmlSchemaType(name = "string")
    protected TrueBearingSource backAzimuthTrueBearingSource;
    @XmlSchemaType(name = "unsignedInt")
    protected Long glidePathHeightAtLandingThreshold;
    protected Location mlsDatumPointLocation;

    /**
     * Gets the value of the azimuthTrueBearing property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAzimuthTrueBearing() {
        return azimuthTrueBearing;
    }

    /**
     * Sets the value of the azimuthTrueBearing property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAzimuthTrueBearing(BigDecimal value) {
        this.azimuthTrueBearing = value;
    }

    /**
     * Gets the value of the azimuthTrueBearingSource property.
     * 
     * @return
     *     possible object is
     *     {@link TrueBearingSource }
     *     
     */
    public TrueBearingSource getAzimuthTrueBearingSource() {
        return azimuthTrueBearingSource;
    }

    /**
     * Sets the value of the azimuthTrueBearingSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueBearingSource }
     *     
     */
    public void setAzimuthTrueBearingSource(TrueBearingSource value) {
        this.azimuthTrueBearingSource = value;
    }

    /**
     * Gets the value of the backAzimuthBearing property.
     * 
     * @return
     *     possible object is
     *     {@link Bearing }
     *     
     */
    public Bearing getBackAzimuthBearing() {
        return backAzimuthBearing;
    }

    /**
     * Sets the value of the backAzimuthBearing property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bearing }
     *     
     */
    public void setBackAzimuthBearing(Bearing value) {
        this.backAzimuthBearing = value;
    }

    /**
     * Gets the value of the backAzimuthCoverageLeft property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBackAzimuthCoverageLeft() {
        return backAzimuthCoverageLeft;
    }

    /**
     * Sets the value of the backAzimuthCoverageLeft property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBackAzimuthCoverageLeft(Long value) {
        this.backAzimuthCoverageLeft = value;
    }

    /**
     * Gets the value of the backAzimuthCoverageRight property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBackAzimuthCoverageRight() {
        return backAzimuthCoverageRight;
    }

    /**
     * Sets the value of the backAzimuthCoverageRight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBackAzimuthCoverageRight(Long value) {
        this.backAzimuthCoverageRight = value;
    }

    /**
     * Gets the value of the backAzimuthPosition property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBackAzimuthPosition() {
        return backAzimuthPosition;
    }

    /**
     * Sets the value of the backAzimuthPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBackAzimuthPosition(Long value) {
        this.backAzimuthPosition = value;
    }

    /**
     * Gets the value of the backAzimuthPositionReference property.
     * 
     * @return
     *     possible object is
     *     {@link LocalizerAzimuthPositionReference }
     *     
     */
    public LocalizerAzimuthPositionReference getBackAzimuthPositionReference() {
        return backAzimuthPositionReference;
    }

    /**
     * Sets the value of the backAzimuthPositionReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalizerAzimuthPositionReference }
     *     
     */
    public void setBackAzimuthPositionReference(LocalizerAzimuthPositionReference value) {
        this.backAzimuthPositionReference = value;
    }

    /**
     * Gets the value of the backAzimuthProportionalSectorLeft property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBackAzimuthProportionalSectorLeft() {
        return backAzimuthProportionalSectorLeft;
    }

    /**
     * Sets the value of the backAzimuthProportionalSectorLeft property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBackAzimuthProportionalSectorLeft(Long value) {
        this.backAzimuthProportionalSectorLeft = value;
    }

    /**
     * Gets the value of the backAzimuthProportionalSectorRight property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBackAzimuthProportionalSectorRight() {
        return backAzimuthProportionalSectorRight;
    }

    /**
     * Sets the value of the backAzimuthProportionalSectorRight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBackAzimuthProportionalSectorRight(Long value) {
        this.backAzimuthProportionalSectorRight = value;
    }

    /**
     * Gets the value of the backAzimuthTrueBearing property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBackAzimuthTrueBearing() {
        return backAzimuthTrueBearing;
    }

    /**
     * Sets the value of the backAzimuthTrueBearing property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBackAzimuthTrueBearing(BigDecimal value) {
        this.backAzimuthTrueBearing = value;
    }

    /**
     * Gets the value of the backAzimuthTrueBearingSource property.
     * 
     * @return
     *     possible object is
     *     {@link TrueBearingSource }
     *     
     */
    public TrueBearingSource getBackAzimuthTrueBearingSource() {
        return backAzimuthTrueBearingSource;
    }

    /**
     * Sets the value of the backAzimuthTrueBearingSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueBearingSource }
     *     
     */
    public void setBackAzimuthTrueBearingSource(TrueBearingSource value) {
        this.backAzimuthTrueBearingSource = value;
    }

    /**
     * Gets the value of the glidePathHeightAtLandingThreshold property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getGlidePathHeightAtLandingThreshold() {
        return glidePathHeightAtLandingThreshold;
    }

    /**
     * Sets the value of the glidePathHeightAtLandingThreshold property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setGlidePathHeightAtLandingThreshold(Long value) {
        this.glidePathHeightAtLandingThreshold = value;
    }

    /**
     * Gets the value of the mlsDatumPointLocation property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getMlsDatumPointLocation() {
        return mlsDatumPointLocation;
    }

    /**
     * Sets the value of the mlsDatumPointLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setMlsDatumPointLocation(Location value) {
        this.mlsDatumPointLocation = value;
    }

}
