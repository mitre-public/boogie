
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This data type defines the enroute portion of the SID/STAR selected for this company route.
 * 
 * <p>Java class for ViaEnroute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ViaEnroute"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="enrouteIdent" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier"/&gt;
 *         &lt;element name="toFixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ViaEnroute", propOrder = {
    "enrouteIdent",
    "toFixRef"
})
public class ViaEnroute {

    @XmlElement(required = true)
    protected String enrouteIdent;
    @XmlElement(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object toFixRef;

    /**
     * Gets the value of the enrouteIdent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnrouteIdent() {
        return enrouteIdent;
    }

    /**
     * Sets the value of the enrouteIdent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnrouteIdent(String value) {
        this.enrouteIdent = value;
    }

    /**
     * Gets the value of the toFixRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getToFixRef() {
        return toFixRef;
    }

    /**
     * Sets the value of the toFixRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setToFixRef(Object value) {
        this.toFixRef = value;
    }

}
