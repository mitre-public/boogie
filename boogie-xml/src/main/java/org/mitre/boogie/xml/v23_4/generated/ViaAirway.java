
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ViaAirway complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ViaAirway"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="viaAirwayRef" type="{http://www.w3.org/2001/XMLSchema}IDREF"/&gt;
 *         &lt;element name="exitFixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference"/&gt;
 *         &lt;element name="entryFixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ViaAirway", propOrder = {
    "viaAirwayRef",
    "exitFixRef",
    "entryFixRef"
})
public class ViaAirway {

    @XmlElement(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object viaAirwayRef;
    @XmlElement(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object exitFixRef;
    @XmlElement(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object entryFixRef;

    /**
     * Gets the value of the viaAirwayRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getViaAirwayRef() {
        return viaAirwayRef;
    }

    /**
     * Sets the value of the viaAirwayRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setViaAirwayRef(Object value) {
        this.viaAirwayRef = value;
    }

    /**
     * Gets the value of the exitFixRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getExitFixRef() {
        return exitFixRef;
    }

    /**
     * Sets the value of the exitFixRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setExitFixRef(Object value) {
        this.exitFixRef = value;
    }

    /**
     * Gets the value of the entryFixRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getEntryFixRef() {
        return entryFixRef;
    }

    /**
     * Sets the value of the entryFixRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setEntryFixRef(Object value) {
        this.entryFixRef = value;
    }

}
