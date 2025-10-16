
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This data type defines the common portion of the SID/STAR selected for this company route.
 * 
 * <p>Java class for ViaCommon complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ViaCommon"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="commonIdent" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier"/&gt;
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
@XmlType(name = "ViaCommon", propOrder = {
    "commonIdent",
    "toFixRef"
})
public class ViaCommon {

    @XmlElement(required = true)
    protected String commonIdent;
    @XmlElement(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object toFixRef;

    /**
     * Gets the value of the commonIdent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommonIdent() {
        return commonIdent;
    }

    /**
     * Sets the value of the commonIdent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommonIdent(String value) {
        this.commonIdent = value;
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
