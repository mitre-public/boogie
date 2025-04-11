
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
 * The Minimum Sector Altitude (MSA) file contains details relating to available sector MSA  Extension Class.
 * 
 * <p>Java class for Msa complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Msa"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="centerFix" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="icaoCode" type="{http://www.arinc424.com/xml/datatypes}IcaoCode" minOccurs="0"/&gt;
 *         &lt;element name="magneticTrueIndicator" type="{http://www.arinc424.com/xml/enumerations}MagneticTrueIndicator" minOccurs="0"/&gt;
 *         &lt;element name="multipleCode" type="{http://www.arinc424.com/xml/datatypes}MultipleCode" minOccurs="0"/&gt;
 *         &lt;element name="sector" type="{http://www.arinc424.com/xml/datatypes}Sector" maxOccurs="unbounded"/&gt;
 *         &lt;element name="centerFixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
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
@XmlType(name = "Msa", propOrder = {
    "centerFix",
    "icaoCode",
    "magneticTrueIndicator",
    "multipleCode",
    "sector",
    "centerFixRef"
})
public class Msa
    extends A424Record
{

    protected String centerFix;
    protected String icaoCode;
    @XmlSchemaType(name = "string")
    protected MagneticTrueIndicator magneticTrueIndicator;
    protected String multipleCode;
    @XmlElement(required = true)
    protected List<Sector> sector;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object centerFixRef;
    @XmlAttribute(name = "referenceId", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String referenceId;

    /**
     * Gets the value of the centerFix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCenterFix() {
        return centerFix;
    }

    /**
     * Sets the value of the centerFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCenterFix(String value) {
        this.centerFix = value;
    }

    /**
     * Gets the value of the icaoCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIcaoCode() {
        return icaoCode;
    }

    /**
     * Sets the value of the icaoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIcaoCode(String value) {
        this.icaoCode = value;
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
     * Gets the value of the multipleCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMultipleCode() {
        return multipleCode;
    }

    /**
     * Sets the value of the multipleCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMultipleCode(String value) {
        this.multipleCode = value;
    }

    /**
     * Gets the value of the sector property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the sector property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSector().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Sector }
     * 
     * 
     */
    public List<Sector> getSector() {
        if (sector == null) {
            sector = new ArrayList<Sector>();
        }
        return this.sector;
    }

    /**
     * Gets the value of the centerFixRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getCenterFixRef() {
        return centerFixRef;
    }

    /**
     * Sets the value of the centerFixRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setCenterFixRef(Object value) {
        this.centerFixRef = value;
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
