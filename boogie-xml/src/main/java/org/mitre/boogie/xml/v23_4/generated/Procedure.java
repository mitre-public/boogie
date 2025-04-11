
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This abstract element is used to represent elements common to different types of terminal procedures.
 * 
 * <p>Java class for Procedure complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Procedure"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424ObjectWithId"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="recordType" type="{http://www.arinc424.com/xml/enumerations}RecordType" minOccurs="0"/&gt;
 *         &lt;element name="isRnav" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isHelicopterOnlyProcedure" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="procedureDesignAircraftCategories" type="{http://www.arinc424.com/xml/datatypes}ProcedureDesignAircraftCategories" minOccurs="0"/&gt;
 *         &lt;element name="procedureDesignAircraftTypes" type="{http://www.arinc424.com/xml/datatypes}ProcedureDesignAircraftTypes" minOccurs="0"/&gt;
 *         &lt;element name="isMilitary" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isSpecial" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="procedureDesignMagVar" type="{http://www.arinc424.com/xml/datatypes}MagneticVariation" minOccurs="0"/&gt;
 *         &lt;element name="procedureReferencedFixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="longIdent" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="procedureName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "Procedure", propOrder = {
    "recordType",
    "isRnav",
    "isHelicopterOnlyProcedure",
    "procedureDesignAircraftCategories",
    "procedureDesignAircraftTypes",
    "isMilitary",
    "isSpecial",
    "procedureDesignMagVar",
    "procedureReferencedFixRef",
    "longIdent",
    "procedureName"
})
@XmlSeeAlso({
    SidStar.class,
    Approach.class
})
public abstract class Procedure
    extends A424ObjectWithId
{

    @XmlSchemaType(name = "string")
    protected RecordType recordType;
    @XmlElement(defaultValue = "false")
    protected Boolean isRnav;
    @XmlElement(defaultValue = "false")
    protected Boolean isHelicopterOnlyProcedure;
    protected ProcedureDesignAircraftCategories procedureDesignAircraftCategories;
    protected ProcedureDesignAircraftTypes procedureDesignAircraftTypes;
    @XmlElement(defaultValue = "false")
    protected Boolean isMilitary;
    @XmlElement(defaultValue = "false")
    protected Boolean isSpecial;
    protected MagneticVariation procedureDesignMagVar;
    @XmlElementRef(name = "procedureReferencedFixRef", type = JAXBElement.class, required = false)
    protected List<JAXBElement<Object>> procedureReferencedFixRef;
    protected String longIdent;
    protected String procedureName;
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
     * Gets the value of the isRnav property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRnav() {
        return isRnav;
    }

    /**
     * Sets the value of the isRnav property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRnav(Boolean value) {
        this.isRnav = value;
    }

    /**
     * Gets the value of the isHelicopterOnlyProcedure property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsHelicopterOnlyProcedure() {
        return isHelicopterOnlyProcedure;
    }

    /**
     * Sets the value of the isHelicopterOnlyProcedure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsHelicopterOnlyProcedure(Boolean value) {
        this.isHelicopterOnlyProcedure = value;
    }

    /**
     * Gets the value of the procedureDesignAircraftCategories property.
     * 
     * @return
     *     possible object is
     *     {@link ProcedureDesignAircraftCategories }
     *     
     */
    public ProcedureDesignAircraftCategories getProcedureDesignAircraftCategories() {
        return procedureDesignAircraftCategories;
    }

    /**
     * Sets the value of the procedureDesignAircraftCategories property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedureDesignAircraftCategories }
     *     
     */
    public void setProcedureDesignAircraftCategories(ProcedureDesignAircraftCategories value) {
        this.procedureDesignAircraftCategories = value;
    }

    /**
     * Gets the value of the procedureDesignAircraftTypes property.
     * 
     * @return
     *     possible object is
     *     {@link ProcedureDesignAircraftTypes }
     *     
     */
    public ProcedureDesignAircraftTypes getProcedureDesignAircraftTypes() {
        return procedureDesignAircraftTypes;
    }

    /**
     * Sets the value of the procedureDesignAircraftTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedureDesignAircraftTypes }
     *     
     */
    public void setProcedureDesignAircraftTypes(ProcedureDesignAircraftTypes value) {
        this.procedureDesignAircraftTypes = value;
    }

    /**
     * Gets the value of the isMilitary property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMilitary() {
        return isMilitary;
    }

    /**
     * Sets the value of the isMilitary property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMilitary(Boolean value) {
        this.isMilitary = value;
    }

    /**
     * Gets the value of the isSpecial property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsSpecial() {
        return isSpecial;
    }

    /**
     * Sets the value of the isSpecial property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsSpecial(Boolean value) {
        this.isSpecial = value;
    }

    /**
     * Gets the value of the procedureDesignMagVar property.
     * 
     * @return
     *     possible object is
     *     {@link MagneticVariation }
     *     
     */
    public MagneticVariation getProcedureDesignMagVar() {
        return procedureDesignMagVar;
    }

    /**
     * Sets the value of the procedureDesignMagVar property.
     * 
     * @param value
     *     allowed object is
     *     {@link MagneticVariation }
     *     
     */
    public void setProcedureDesignMagVar(MagneticVariation value) {
        this.procedureDesignMagVar = value;
    }

    /**
     * Gets the value of the procedureReferencedFixRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the procedureReferencedFixRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcedureReferencedFixRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * 
     * 
     */
    public List<JAXBElement<Object>> getProcedureReferencedFixRef() {
        if (procedureReferencedFixRef == null) {
            procedureReferencedFixRef = new ArrayList<JAXBElement<Object>>();
        }
        return this.procedureReferencedFixRef;
    }

    /**
     * Gets the value of the longIdent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongIdent() {
        return longIdent;
    }

    /**
     * Sets the value of the longIdent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongIdent(String value) {
        this.longIdent = value;
    }

    /**
     * Gets the value of the procedureName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * Sets the value of the procedureName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcedureName(String value) {
        this.procedureName = value;
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
