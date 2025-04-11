
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Abstract superclass for any class that has a latitude and longitude
 * 
 * <p>Java class for A424Point complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="A424Point"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="datumCode" type="{http://www.arinc424.com/xml/enumerations}DatumCode" minOccurs="0"/&gt;
 *         &lt;element name="icaoCode" type="{http://www.arinc424.com/xml/datatypes}IcaoCode"/&gt;
 *         &lt;element name="identifier" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier"/&gt;
 *         &lt;element name="location" type="{http://www.arinc424.com/xml/datatypes}Location"/&gt;
 *         &lt;element name="magneticVariation" type="{http://www.arinc424.com/xml/datatypes}MagneticVariation" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.arinc424.com/xml/datatypes}Name" minOccurs="0"/&gt;
 *         &lt;element name="uirRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/&gt;
 *         &lt;element name="firRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/&gt;
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
@XmlType(name = "A424Point", propOrder = {
    "datumCode",
    "icaoCode",
    "identifier",
    "location",
    "magneticVariation",
    "name",
    "uirRef",
    "firRef"
})
@XmlSeeAlso({
    Waypoint.class,
    AirwaysMarker.class,
    MlsBackAzimuthDetails.class,
    Navaid.class,
    AirportHeliportLocalizerMarker.class,
    AirportGate.class,
    Runway.class,
    Port.class,
    Helipad.class
})
public abstract class A424Point
    extends A424Record
{

    @XmlSchemaType(name = "string")
    protected DatumCode datumCode;
    @XmlElement(required = true)
    protected String icaoCode;
    @XmlElement(required = true)
    protected String identifier;
    @XmlElement(required = true)
    protected Location location;
    protected MagneticVariation magneticVariation;
    protected String name;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object uirRef;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object firRef;
    @XmlAttribute(name = "referenceId", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String referenceId;

    /**
     * Gets the value of the datumCode property.
     * 
     * @return
     *     possible object is
     *     {@link DatumCode }
     *     
     */
    public DatumCode getDatumCode() {
        return datumCode;
    }

    /**
     * Sets the value of the datumCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatumCode }
     *     
     */
    public void setDatumCode(DatumCode value) {
        this.datumCode = value;
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
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setLocation(Location value) {
        this.location = value;
    }

    /**
     * Gets the value of the magneticVariation property.
     * 
     * @return
     *     possible object is
     *     {@link MagneticVariation }
     *     
     */
    public MagneticVariation getMagneticVariation() {
        return magneticVariation;
    }

    /**
     * Sets the value of the magneticVariation property.
     * 
     * @param value
     *     allowed object is
     *     {@link MagneticVariation }
     *     
     */
    public void setMagneticVariation(MagneticVariation value) {
        this.magneticVariation = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the uirRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getUirRef() {
        return uirRef;
    }

    /**
     * Sets the value of the uirRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setUirRef(Object value) {
        this.uirRef = value;
    }

    /**
     * Gets the value of the firRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFirRef() {
        return firRef;
    }

    /**
     * Sets the value of the firRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFirRef(Object value) {
        this.firRef = value;
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
