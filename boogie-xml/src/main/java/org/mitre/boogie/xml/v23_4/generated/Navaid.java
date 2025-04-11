
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Navaid complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Navaid"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Point"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="portRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="elevation" type="{http://www.arinc424.com/xml/datatypes}Elevation" minOccurs="0"/&gt;
 *         &lt;element name="isVFRCheckpoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Navaid", propOrder = {
    "portRef",
    "elevation",
    "isVFRCheckpoint"
})
@XmlSeeAlso({
    Vor.class,
    DmeTacan.class,
    PrecisionApproachNavaid.class,
    Ndb.class
})
public abstract class Navaid
    extends A424Point
{

    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object portRef;
    protected Integer elevation;
    @XmlElement(defaultValue = "false")
    protected Boolean isVFRCheckpoint;

    /**
     * Gets the value of the portRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getPortRef() {
        return portRef;
    }

    /**
     * Sets the value of the portRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setPortRef(Object value) {
        this.portRef = value;
    }

    /**
     * Gets the value of the elevation property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getElevation() {
        return elevation;
    }

    /**
     * Sets the value of the elevation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setElevation(Integer value) {
        this.elevation = value;
    }

    /**
     * Gets the value of the isVFRCheckpoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsVFRCheckpoint() {
        return isVFRCheckpoint;
    }

    /**
     * Sets the value of the isVFRCheckpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsVFRCheckpoint(Boolean value) {
        this.isVFRCheckpoint = value;
    }

}
