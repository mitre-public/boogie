
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
 * The Airport Terminal Arrival Altitude (TAA) file contains details relating to TAA sectorization and sector altitudes.
 * 
 * <p>Java class for Taa complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Taa"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="approachTypeIdentifier" type="{http://www.arinc424.com/xml/datatypes}ApproachTypeIdentifier" maxOccurs="unbounded"/&gt;
 *         &lt;element name="magneticTrueIndicator" type="{http://www.arinc424.com/xml/enumerations}MagneticTrueIndicator" minOccurs="0"/&gt;
 *         &lt;element name="taaFixPositionIndicator" type="{http://www.arinc424.com/xml/enumerations}TaaSectorIdentifier"/&gt;
 *         &lt;element name="sectorTaaDetails" type="{http://www.arinc424.com/xml/datatypes}TaaSectorDetails" maxOccurs="unbounded"/&gt;
 *         &lt;element name="taaIafWaypointRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="sectorBearingWaypointRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/&gt;
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
@XmlType(name = "Taa", propOrder = {
    "approachTypeIdentifier",
    "magneticTrueIndicator",
    "taaFixPositionIndicator",
    "sectorTaaDetails",
    "taaIafWaypointRef",
    "sectorBearingWaypointRef"
})
public class Taa
    extends A424Record
{

    @XmlElement(required = true)
    protected List<String> approachTypeIdentifier;
    @XmlSchemaType(name = "string")
    protected MagneticTrueIndicator magneticTrueIndicator;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TaaSectorIdentifier taaFixPositionIndicator;
    @XmlElement(required = true)
    protected List<TaaSectorDetails> sectorTaaDetails;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object taaIafWaypointRef;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object sectorBearingWaypointRef;
    @XmlAttribute(name = "referenceId", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String referenceId;

    /**
     * Gets the value of the approachTypeIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the approachTypeIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApproachTypeIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getApproachTypeIdentifier() {
        if (approachTypeIdentifier == null) {
            approachTypeIdentifier = new ArrayList<String>();
        }
        return this.approachTypeIdentifier;
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
     * Gets the value of the taaFixPositionIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link TaaSectorIdentifier }
     *     
     */
    public TaaSectorIdentifier getTaaFixPositionIndicator() {
        return taaFixPositionIndicator;
    }

    /**
     * Sets the value of the taaFixPositionIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaaSectorIdentifier }
     *     
     */
    public void setTaaFixPositionIndicator(TaaSectorIdentifier value) {
        this.taaFixPositionIndicator = value;
    }

    /**
     * Gets the value of the sectorTaaDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the sectorTaaDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSectorTaaDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaaSectorDetails }
     * 
     * 
     */
    public List<TaaSectorDetails> getSectorTaaDetails() {
        if (sectorTaaDetails == null) {
            sectorTaaDetails = new ArrayList<TaaSectorDetails>();
        }
        return this.sectorTaaDetails;
    }

    /**
     * Gets the value of the taaIafWaypointRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getTaaIafWaypointRef() {
        return taaIafWaypointRef;
    }

    /**
     * Sets the value of the taaIafWaypointRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setTaaIafWaypointRef(Object value) {
        this.taaIafWaypointRef = value;
    }

    /**
     * Gets the value of the sectorBearingWaypointRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSectorBearingWaypointRef() {
        return sectorBearingWaypointRef;
    }

    /**
     * Sets the value of the sectorBearingWaypointRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSectorBearingWaypointRef(Object value) {
        this.sectorBearingWaypointRef = value;
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
