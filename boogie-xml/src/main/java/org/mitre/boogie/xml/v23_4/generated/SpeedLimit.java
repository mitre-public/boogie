
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The Speed Limit field defines a speed, expressed in Knots, Indicated (K.I.A.S.), for a fix in a terminal procedure or for an airport or heliport terminal environment.
 * 
 * <p>Java class for SpeedLimit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SpeedLimit"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="at" type="{http://www.arinc424.com/xml/datatypes}SpeedValue" minOccurs="0"/&gt;
 *         &lt;element name="atOrAbove" type="{http://www.arinc424.com/xml/datatypes}SpeedValue" minOccurs="0"/&gt;
 *         &lt;element name="atOrBelow" type="{http://www.arinc424.com/xml/datatypes}SpeedValue" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpeedLimit", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "at",
    "atOrAbove",
    "atOrBelow"
})
public class SpeedLimit {

    @XmlSchemaType(name = "unsignedInt")
    protected Long at;
    @XmlSchemaType(name = "unsignedInt")
    protected Long atOrAbove;
    @XmlSchemaType(name = "unsignedInt")
    protected Long atOrBelow;

    /**
     * Gets the value of the at property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAt() {
        return at;
    }

    /**
     * Sets the value of the at property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAt(Long value) {
        this.at = value;
    }

    /**
     * Gets the value of the atOrAbove property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAtOrAbove() {
        return atOrAbove;
    }

    /**
     * Sets the value of the atOrAbove property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAtOrAbove(Long value) {
        this.atOrAbove = value;
    }

    /**
     * Gets the value of the atOrBelow property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAtOrBelow() {
        return atOrBelow;
    }

    /**
     * Sets the value of the atOrBelow property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAtOrBelow(Long value) {
        this.atOrBelow = value;
    }

}
