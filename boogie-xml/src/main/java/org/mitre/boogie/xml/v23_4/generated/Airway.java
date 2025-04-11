
package org.mitre.boogie.xml.v23_4.generated;

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
 * This following record contains the fields used in Enroute Airway Records.
 * 
 * <p>Java class for Airway complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Airway"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424ObjectWithId"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="recordType" type="{http://www.arinc424.com/xml/enumerations}RecordType" minOccurs="0"/&gt;
 *         &lt;element name="customerCode" type="{http://www.arinc424.com/xml/datatypes}CustomerCode" minOccurs="0"/&gt;
 *         &lt;element name="airwayLeg" type="{}AirwayLeg" maxOccurs="unbounded" minOccurs="2"/&gt;
 *         &lt;element name="airwayRouteType" type="{http://www.arinc424.com/xml/enumerations}EnrouteAirwayRouteType"/&gt;
 *         &lt;element name="qualifier1" type="{http://www.arinc424.com/xml/enumerations}AirwayQualifier1" minOccurs="0"/&gt;
 *         &lt;element name="qualifier2" type="{http://www.arinc424.com/xml/enumerations}AirwayQualifier2" minOccurs="0"/&gt;
 *         &lt;element name="rnavPbnNavSpec" type="{http://www.arinc424.com/xml/enumerations}AirwayRnavPbnNavSpec" minOccurs="0"/&gt;
 *         &lt;element name="rnpPbnNavSpec" type="{http://www.arinc424.com/xml/enumerations}AirwayRnpPbnNavSpec" minOccurs="0"/&gt;
 *         &lt;element name="airwaysMarker" type="{}AirwaysMarker" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="airwaysRestrictionAltitudeExclusion" type="{}EnrouteAirwaysRestrictionAltitudeExclusion" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="airwaysRestrictionSeasonalClosure" type="{}EnrouteAirwaysRestrictionSeasonalClosure" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="airwaysRestrictionCruisingTableReplacement" type="{}EnrouteAirwaysRestrictionCruisingTableReplacement" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlType(name = "Airway", propOrder = {
    "recordType",
    "customerCode",
    "airwayLeg",
    "airwayRouteType",
    "qualifier1",
    "qualifier2",
    "rnavPbnNavSpec",
    "rnpPbnNavSpec",
    "airwaysMarker",
    "airwaysRestrictionAltitudeExclusion",
    "airwaysRestrictionSeasonalClosure",
    "airwaysRestrictionCruisingTableReplacement"
})
public class Airway
    extends A424ObjectWithId
{

    @XmlSchemaType(name = "string")
    protected RecordType recordType;
    protected String customerCode;
    @XmlElement(required = true)
    protected List<AirwayLeg> airwayLeg;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected EnrouteAirwayRouteType airwayRouteType;
    @XmlSchemaType(name = "string")
    protected AirwayQualifier1 qualifier1;
    @XmlSchemaType(name = "string")
    protected AirwayQualifier2 qualifier2;
    @XmlSchemaType(name = "string")
    protected AirwayRnavPbnNavSpec rnavPbnNavSpec;
    @XmlSchemaType(name = "string")
    protected AirwayRnpPbnNavSpec rnpPbnNavSpec;
    protected List<AirwaysMarker> airwaysMarker;
    protected List<EnrouteAirwaysRestrictionAltitudeExclusion> airwaysRestrictionAltitudeExclusion;
    protected List<EnrouteAirwaysRestrictionSeasonalClosure> airwaysRestrictionSeasonalClosure;
    protected List<EnrouteAirwaysRestrictionCruisingTableReplacement> airwaysRestrictionCruisingTableReplacement;
    @XmlAttribute(name = "referenceId", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String referenceId;

    /**
     * Gets the value of the recordType property.
     * 
     * @return
     *     possible object is
     *     {@link RecordType }
     *     
     */
    public RecordType getRecordType() {
        return recordType;
    }

    /**
     * Sets the value of the recordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordType }
     *     
     */
    public void setRecordType(RecordType value) {
        this.recordType = value;
    }

    /**
     * Gets the value of the customerCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * Sets the value of the customerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerCode(String value) {
        this.customerCode = value;
    }

    /**
     * Gets the value of the airwayLeg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the airwayLeg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAirwayLeg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AirwayLeg }
     * 
     * 
     */
    public List<AirwayLeg> getAirwayLeg() {
        if (airwayLeg == null) {
            airwayLeg = new ArrayList<AirwayLeg>();
        }
        return this.airwayLeg;
    }

    /**
     * Gets the value of the airwayRouteType property.
     * 
     * @return
     *     possible object is
     *     {@link EnrouteAirwayRouteType }
     *     
     */
    public EnrouteAirwayRouteType getAirwayRouteType() {
        return airwayRouteType;
    }

    /**
     * Sets the value of the airwayRouteType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnrouteAirwayRouteType }
     *     
     */
    public void setAirwayRouteType(EnrouteAirwayRouteType value) {
        this.airwayRouteType = value;
    }

    /**
     * Gets the value of the qualifier1 property.
     * 
     * @return
     *     possible object is
     *     {@link AirwayQualifier1 }
     *     
     */
    public AirwayQualifier1 getQualifier1() {
        return qualifier1;
    }

    /**
     * Sets the value of the qualifier1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirwayQualifier1 }
     *     
     */
    public void setQualifier1(AirwayQualifier1 value) {
        this.qualifier1 = value;
    }

    /**
     * Gets the value of the qualifier2 property.
     * 
     * @return
     *     possible object is
     *     {@link AirwayQualifier2 }
     *     
     */
    public AirwayQualifier2 getQualifier2() {
        return qualifier2;
    }

    /**
     * Sets the value of the qualifier2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirwayQualifier2 }
     *     
     */
    public void setQualifier2(AirwayQualifier2 value) {
        this.qualifier2 = value;
    }

    /**
     * Gets the value of the rnavPbnNavSpec property.
     * 
     * @return
     *     possible object is
     *     {@link AirwayRnavPbnNavSpec }
     *     
     */
    public AirwayRnavPbnNavSpec getRnavPbnNavSpec() {
        return rnavPbnNavSpec;
    }

    /**
     * Sets the value of the rnavPbnNavSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirwayRnavPbnNavSpec }
     *     
     */
    public void setRnavPbnNavSpec(AirwayRnavPbnNavSpec value) {
        this.rnavPbnNavSpec = value;
    }

    /**
     * Gets the value of the rnpPbnNavSpec property.
     * 
     * @return
     *     possible object is
     *     {@link AirwayRnpPbnNavSpec }
     *     
     */
    public AirwayRnpPbnNavSpec getRnpPbnNavSpec() {
        return rnpPbnNavSpec;
    }

    /**
     * Sets the value of the rnpPbnNavSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirwayRnpPbnNavSpec }
     *     
     */
    public void setRnpPbnNavSpec(AirwayRnpPbnNavSpec value) {
        this.rnpPbnNavSpec = value;
    }

    /**
     * Gets the value of the airwaysMarker property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the airwaysMarker property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAirwaysMarker().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AirwaysMarker }
     * 
     * 
     */
    public List<AirwaysMarker> getAirwaysMarker() {
        if (airwaysMarker == null) {
            airwaysMarker = new ArrayList<AirwaysMarker>();
        }
        return this.airwaysMarker;
    }

    /**
     * Gets the value of the airwaysRestrictionAltitudeExclusion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the airwaysRestrictionAltitudeExclusion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAirwaysRestrictionAltitudeExclusion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnrouteAirwaysRestrictionAltitudeExclusion }
     * 
     * 
     */
    public List<EnrouteAirwaysRestrictionAltitudeExclusion> getAirwaysRestrictionAltitudeExclusion() {
        if (airwaysRestrictionAltitudeExclusion == null) {
            airwaysRestrictionAltitudeExclusion = new ArrayList<EnrouteAirwaysRestrictionAltitudeExclusion>();
        }
        return this.airwaysRestrictionAltitudeExclusion;
    }

    /**
     * Gets the value of the airwaysRestrictionSeasonalClosure property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the airwaysRestrictionSeasonalClosure property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAirwaysRestrictionSeasonalClosure().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnrouteAirwaysRestrictionSeasonalClosure }
     * 
     * 
     */
    public List<EnrouteAirwaysRestrictionSeasonalClosure> getAirwaysRestrictionSeasonalClosure() {
        if (airwaysRestrictionSeasonalClosure == null) {
            airwaysRestrictionSeasonalClosure = new ArrayList<EnrouteAirwaysRestrictionSeasonalClosure>();
        }
        return this.airwaysRestrictionSeasonalClosure;
    }

    /**
     * Gets the value of the airwaysRestrictionCruisingTableReplacement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the airwaysRestrictionCruisingTableReplacement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAirwaysRestrictionCruisingTableReplacement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnrouteAirwaysRestrictionCruisingTableReplacement }
     * 
     * 
     */
    public List<EnrouteAirwaysRestrictionCruisingTableReplacement> getAirwaysRestrictionCruisingTableReplacement() {
        if (airwaysRestrictionCruisingTableReplacement == null) {
            airwaysRestrictionCruisingTableReplacement = new ArrayList<EnrouteAirwaysRestrictionCruisingTableReplacement>();
        }
        return this.airwaysRestrictionCruisingTableReplacement;
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
