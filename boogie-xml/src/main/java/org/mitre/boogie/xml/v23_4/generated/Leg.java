
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Leg complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Leg"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sequenceNumber" type="{http://www.arinc424.com/xml/datatypes}SequenceNumber"/&gt;
 *         &lt;element name="fixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="fixIdent" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="recNavaidIdent" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="recNavaidRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Leg", propOrder = {
    "sequenceNumber",
    "fixRef",
    "fixIdent",
    "recNavaidIdent",
    "recNavaidRef"
})
@XmlSeeAlso({
    AirwayLeg.class,
    PreferredRouteLeg.class,
    ProcedureLeg.class,
    ViaEnrouteLeg.class
})
public abstract class Leg
    extends A424Record
{

    @XmlSchemaType(name = "unsignedInt")
    protected long sequenceNumber;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object fixRef;
    protected String fixIdent;
    protected String recNavaidIdent;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object recNavaidRef;

    /**
     * Gets the value of the sequenceNumber property.
     * 
     */
    public long getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the value of the sequenceNumber property.
     * 
     */
    public void setSequenceNumber(long value) {
        this.sequenceNumber = value;
    }

    /**
     * Gets the value of the fixRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFixRef() {
        return fixRef;
    }

    /**
     * Sets the value of the fixRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFixRef(Object value) {
        this.fixRef = value;
    }

    /**
     * Gets the value of the fixIdent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFixIdent() {
        return fixIdent;
    }

    /**
     * Sets the value of the fixIdent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFixIdent(String value) {
        this.fixIdent = value;
    }

    /**
     * Gets the value of the recNavaidIdent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecNavaidIdent() {
        return recNavaidIdent;
    }

    /**
     * Sets the value of the recNavaidIdent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecNavaidIdent(String value) {
        this.recNavaidIdent = value;
    }

    /**
     * Gets the value of the recNavaidRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRecNavaidRef() {
        return recNavaidRef;
    }

    /**
     * Sets the value of the recNavaidRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRecNavaidRef(Object value) {
        this.recNavaidRef = value;
    }

}
