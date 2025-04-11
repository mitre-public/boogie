
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for FirUir complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FirUir"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cruiseTableRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/&gt;
 *         &lt;element name="isEntryReport" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="firUirAddress" type="{http://www.arinc424.com/xml/datatypes}FirUirAddress"/&gt;
 *         &lt;element name="firUirIdentifier" type="{http://www.arinc424.com/xml/datatypes}FirUirIdentifier"/&gt;
 *         &lt;element name="firUirIndicator" type="{http://www.arinc424.com/xml/enumerations}FirUirIndicator"/&gt;
 *         &lt;element name="firUirSegment" type="{}FirUirSegment" maxOccurs="unbounded"/&gt;
 *         &lt;element name="firUirName" type="{http://www.arinc424.com/xml/datatypes}FirUirName" minOccurs="0"/&gt;
 *         &lt;element name="firAltitudeLimits" type="{http://www.arinc424.com/xml/datatypes}AirspaceAltitudeConstraint" minOccurs="0"/&gt;
 *         &lt;element name="uirAltitudeLimits" type="{http://www.arinc424.com/xml/datatypes}AirspaceAltitudeConstraint" minOccurs="0"/&gt;
 *         &lt;element name="firUirAtcReportingUnitsAltitude" type="{http://www.arinc424.com/xml/enumerations}FirUirAtcReportingUnitsAltitude" minOccurs="0"/&gt;
 *         &lt;element name="firUirAtcReportingUnitsSpeed" type="{http://www.arinc424.com/xml/enumerations}FirUirAtcReportingUnitsSpeed" minOccurs="0"/&gt;
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
@XmlType(name = "FirUir", propOrder = {
    "cruiseTableRef",
    "isEntryReport",
    "firUirAddress",
    "firUirIdentifier",
    "firUirIndicator",
    "firUirSegment",
    "firUirName",
    "firAltitudeLimits",
    "uirAltitudeLimits",
    "firUirAtcReportingUnitsAltitude",
    "firUirAtcReportingUnitsSpeed"
})
public class FirUir
    extends A424Record
{

    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object cruiseTableRef;
    @XmlElement(defaultValue = "false")
    protected Boolean isEntryReport;
    @XmlElement(required = true)
    protected String firUirAddress;
    @XmlElement(required = true)
    protected String firUirIdentifier;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected FirUirIndicator firUirIndicator;
    @XmlElement(required = true)
    protected List<FirUirSegment> firUirSegment;
    protected String firUirName;
    protected AirspaceAltitudeConstraint firAltitudeLimits;
    protected AirspaceAltitudeConstraint uirAltitudeLimits;
    @XmlSchemaType(name = "string")
    protected FirUirAtcReportingUnitsAltitude firUirAtcReportingUnitsAltitude;
    @XmlSchemaType(name = "string")
    protected FirUirAtcReportingUnitsSpeed firUirAtcReportingUnitsSpeed;
    @XmlAttribute(name = "referenceId", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String referenceId;

    /**
     * Gets the value of the cruiseTableRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getCruiseTableRef() {
        return cruiseTableRef;
    }

    /**
     * Sets the value of the cruiseTableRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setCruiseTableRef(Object value) {
        this.cruiseTableRef = value;
    }

    /**
     * Gets the value of the isEntryReport property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsEntryReport() {
        return isEntryReport;
    }

    /**
     * Sets the value of the isEntryReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsEntryReport(Boolean value) {
        this.isEntryReport = value;
    }

    /**
     * Gets the value of the firUirAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirUirAddress() {
        return firUirAddress;
    }

    /**
     * Sets the value of the firUirAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirUirAddress(String value) {
        this.firUirAddress = value;
    }

    /**
     * Gets the value of the firUirIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirUirIdentifier() {
        return firUirIdentifier;
    }

    /**
     * Sets the value of the firUirIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirUirIdentifier(String value) {
        this.firUirIdentifier = value;
    }

    /**
     * Gets the value of the firUirIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link FirUirIndicator }
     *     
     */
    public FirUirIndicator getFirUirIndicator() {
        return firUirIndicator;
    }

    /**
     * Sets the value of the firUirIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link FirUirIndicator }
     *     
     */
    public void setFirUirIndicator(FirUirIndicator value) {
        this.firUirIndicator = value;
    }

    /**
     * Gets the value of the firUirSegment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the firUirSegment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFirUirSegment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FirUirSegment }
     * 
     * 
     */
    public List<FirUirSegment> getFirUirSegment() {
        if (firUirSegment == null) {
            firUirSegment = new ArrayList<FirUirSegment>();
        }
        return this.firUirSegment;
    }

    /**
     * Gets the value of the firUirName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirUirName() {
        return firUirName;
    }

    /**
     * Sets the value of the firUirName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirUirName(String value) {
        this.firUirName = value;
    }

    /**
     * Gets the value of the firAltitudeLimits property.
     * 
     * @return
     *     possible object is
     *     {@link AirspaceAltitudeConstraint }
     *     
     */
    public AirspaceAltitudeConstraint getFirAltitudeLimits() {
        return firAltitudeLimits;
    }

    /**
     * Sets the value of the firAltitudeLimits property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirspaceAltitudeConstraint }
     *     
     */
    public void setFirAltitudeLimits(AirspaceAltitudeConstraint value) {
        this.firAltitudeLimits = value;
    }

    /**
     * Gets the value of the uirAltitudeLimits property.
     * 
     * @return
     *     possible object is
     *     {@link AirspaceAltitudeConstraint }
     *     
     */
    public AirspaceAltitudeConstraint getUirAltitudeLimits() {
        return uirAltitudeLimits;
    }

    /**
     * Sets the value of the uirAltitudeLimits property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirspaceAltitudeConstraint }
     *     
     */
    public void setUirAltitudeLimits(AirspaceAltitudeConstraint value) {
        this.uirAltitudeLimits = value;
    }

    /**
     * Gets the value of the firUirAtcReportingUnitsAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link FirUirAtcReportingUnitsAltitude }
     *     
     */
    public FirUirAtcReportingUnitsAltitude getFirUirAtcReportingUnitsAltitude() {
        return firUirAtcReportingUnitsAltitude;
    }

    /**
     * Sets the value of the firUirAtcReportingUnitsAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link FirUirAtcReportingUnitsAltitude }
     *     
     */
    public void setFirUirAtcReportingUnitsAltitude(FirUirAtcReportingUnitsAltitude value) {
        this.firUirAtcReportingUnitsAltitude = value;
    }

    /**
     * Gets the value of the firUirAtcReportingUnitsSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link FirUirAtcReportingUnitsSpeed }
     *     
     */
    public FirUirAtcReportingUnitsSpeed getFirUirAtcReportingUnitsSpeed() {
        return firUirAtcReportingUnitsSpeed;
    }

    /**
     * Sets the value of the firUirAtcReportingUnitsSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link FirUirAtcReportingUnitsSpeed }
     *     
     */
    public void setFirUirAtcReportingUnitsSpeed(FirUirAtcReportingUnitsSpeed value) {
        this.firUirAtcReportingUnitsSpeed = value;
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
