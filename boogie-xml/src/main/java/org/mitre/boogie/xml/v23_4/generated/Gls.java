
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This record contains a sequential listing of all GNSS Landing Systems (GLS) approaches, including the slope, course and reference path idents of the GLS approach. A GLS approach is identified by its ident and channel. Note that several GLS approaches can be supported by a single differential GLS ground station.
 * 
 * <p>Java class for Gls complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Gls"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}PrecisionApproachNavaid"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="serviceVolumeRadius" type="{http://www.arinc424.com/xml/datatypes}ServiceVolumeRadius" minOccurs="0"/&gt;
 *         &lt;element name="stationElevationWgs84" type="{http://www.arinc424.com/xml/datatypes}StationElevationWgs84" minOccurs="0"/&gt;
 *         &lt;element name="stationType" type="{http://www.arinc424.com/xml/datatypes}StationType" minOccurs="0"/&gt;
 *         &lt;element name="tdmaSlots" type="{http://www.arinc424.com/xml/datatypes}TdmaSlots" minOccurs="0"/&gt;
 *         &lt;element name="glsChannel" type="{http://www.arinc424.com/xml/datatypes}Channel"/&gt;
 *         &lt;element name="thresholdCrossingHeight" type="{http://www.arinc424.com/xml/datatypes}ThresholdCrossingHeight" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Gls", propOrder = {
    "serviceVolumeRadius",
    "stationElevationWgs84",
    "stationType",
    "tdmaSlots",
    "glsChannel",
    "thresholdCrossingHeight"
})
public class Gls
    extends PrecisionApproachNavaid
{

    @XmlSchemaType(name = "unsignedInt")
    protected Long serviceVolumeRadius;
    protected Integer stationElevationWgs84;
    protected String stationType;
    protected String tdmaSlots;
    @XmlSchemaType(name = "unsignedInt")
    protected long glsChannel;
    @XmlSchemaType(name = "unsignedInt")
    protected Long thresholdCrossingHeight;

    /**
     * Gets the value of the serviceVolumeRadius property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getServiceVolumeRadius() {
        return serviceVolumeRadius;
    }

    /**
     * Sets the value of the serviceVolumeRadius property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setServiceVolumeRadius(Long value) {
        this.serviceVolumeRadius = value;
    }

    /**
     * Gets the value of the stationElevationWgs84 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getStationElevationWgs84() {
        return stationElevationWgs84;
    }

    /**
     * Sets the value of the stationElevationWgs84 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStationElevationWgs84(Integer value) {
        this.stationElevationWgs84 = value;
    }

    /**
     * Gets the value of the stationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStationType() {
        return stationType;
    }

    /**
     * Sets the value of the stationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStationType(String value) {
        this.stationType = value;
    }

    /**
     * Gets the value of the tdmaSlots property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTdmaSlots() {
        return tdmaSlots;
    }

    /**
     * Sets the value of the tdmaSlots property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTdmaSlots(String value) {
        this.tdmaSlots = value;
    }

    /**
     * Gets the value of the glsChannel property.
     * 
     */
    public long getGlsChannel() {
        return glsChannel;
    }

    /**
     * Sets the value of the glsChannel property.
     * 
     */
    public void setGlsChannel(long value) {
        this.glsChannel = value;
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

}
