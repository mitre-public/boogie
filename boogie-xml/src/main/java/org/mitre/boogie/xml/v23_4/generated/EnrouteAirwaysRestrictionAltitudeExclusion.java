
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This record type contains altitudes, normally available, that are excluded from use for the Enroute Airway Segment. May be further restricted by Time of Operation information.
 * 
 * <p>Java class for EnrouteAirwaysRestrictionAltitudeExclusion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EnrouteAirwaysRestrictionAltitudeExclusion"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}EnrouteAirwaysRestriction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="exclusionIndicator" type="{http://www.arinc424.com/xml/enumerations}ExclusionIndicator" minOccurs="0"/&gt;
 *         &lt;element name="restrictionAltitudeBlockIndicator" type="{http://www.arinc424.com/xml/datatypes}RestrictionAltitudeBlockIndicator" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="unitsOfAltitude" type="{http://www.arinc424.com/xml/enumerations}UnitsOfAltitude" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnrouteAirwaysRestrictionAltitudeExclusion", propOrder = {
    "exclusionIndicator",
    "restrictionAltitudeBlockIndicator",
    "unitsOfAltitude"
})
public class EnrouteAirwaysRestrictionAltitudeExclusion
    extends EnrouteAirwaysRestriction
{

    @XmlSchemaType(name = "string")
    protected ExclusionIndicator exclusionIndicator;
    protected List<RestrictionAltitudeBlockIndicator> restrictionAltitudeBlockIndicator;
    @XmlSchemaType(name = "string")
    protected UnitsOfAltitude unitsOfAltitude;

    /**
     * Gets the value of the exclusionIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link ExclusionIndicator }
     *     
     */
    public ExclusionIndicator getExclusionIndicator() {
        return exclusionIndicator;
    }

    /**
     * Sets the value of the exclusionIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExclusionIndicator }
     *     
     */
    public void setExclusionIndicator(ExclusionIndicator value) {
        this.exclusionIndicator = value;
    }

    /**
     * Gets the value of the restrictionAltitudeBlockIndicator property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the restrictionAltitudeBlockIndicator property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRestrictionAltitudeBlockIndicator().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RestrictionAltitudeBlockIndicator }
     * 
     * 
     */
    public List<RestrictionAltitudeBlockIndicator> getRestrictionAltitudeBlockIndicator() {
        if (restrictionAltitudeBlockIndicator == null) {
            restrictionAltitudeBlockIndicator = new ArrayList<RestrictionAltitudeBlockIndicator>();
        }
        return this.restrictionAltitudeBlockIndicator;
    }

    /**
     * Gets the value of the unitsOfAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link UnitsOfAltitude }
     *     
     */
    public UnitsOfAltitude getUnitsOfAltitude() {
        return unitsOfAltitude;
    }

    /**
     * Sets the value of the unitsOfAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnitsOfAltitude }
     *     
     */
    public void setUnitsOfAltitude(UnitsOfAltitude value) {
        this.unitsOfAltitude = value;
    }

}
