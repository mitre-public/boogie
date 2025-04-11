
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * For VHF NAVAIDS, the “Station Declination” field contains the angular difference between true north and the zero degree radial of the NAVAID at the time the NAVAID was last site checked. For ILS localizers, the field contains the angular difference between true north and magnetic north at the localizer antenna site at the time the magnetic bearing of the localizer course was established.
 * 
 * <p>Java class for StationDeclination complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StationDeclination"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="stationDeclinationEWT" type="{http://www.arinc424.com/xml/enumerations}StationDeclinationEWT"/&gt;
 *         &lt;element name="stationDeclinationValue" type="{http://www.arinc424.com/xml/datatypes}MagneticVariationValue"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StationDeclination", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "stationDeclinationEWT",
    "stationDeclinationValue"
})
public class StationDeclination {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StationDeclinationEWT stationDeclinationEWT;
    @XmlElement(required = true)
    protected BigDecimal stationDeclinationValue;

    /**
     * Gets the value of the stationDeclinationEWT property.
     * 
     * @return
     *     possible object is
     *     {@link StationDeclinationEWT }
     *     
     */
    public StationDeclinationEWT getStationDeclinationEWT() {
        return stationDeclinationEWT;
    }

    /**
     * Sets the value of the stationDeclinationEWT property.
     * 
     * @param value
     *     allowed object is
     *     {@link StationDeclinationEWT }
     *     
     */
    public void setStationDeclinationEWT(StationDeclinationEWT value) {
        this.stationDeclinationEWT = value;
    }

    /**
     * Gets the value of the stationDeclinationValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getStationDeclinationValue() {
        return stationDeclinationValue;
    }

    /**
     * Sets the value of the stationDeclinationValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setStationDeclinationValue(BigDecimal value) {
        this.stationDeclinationValue = value;
    }

}
