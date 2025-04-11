
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class is the base class for SID and STAR records. It contains all the fields that are common to both SID and STAR.
 * 
 * <p>Java class for SidStar complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SidStar"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Procedure"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isVorDmeRnav" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="rnavPbnNavSpec" type="{http://www.arinc424.com/xml/enumerations}RnavPbnNavSpec" minOccurs="0"/&gt;
 *         &lt;element name="rnpPbnNavSpec" type="{http://www.arinc424.com/xml/enumerations}RnpPbnNavSpec" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SidStar", propOrder = {
    "isVorDmeRnav",
    "rnavPbnNavSpec",
    "rnpPbnNavSpec"
})
@XmlSeeAlso({
    Sid.class,
    Star.class
})
public abstract class SidStar
    extends Procedure
{

    @XmlElement(defaultValue = "false")
    protected Boolean isVorDmeRnav;
    @XmlSchemaType(name = "string")
    protected RnavPbnNavSpec rnavPbnNavSpec;
    @XmlSchemaType(name = "string")
    protected RnpPbnNavSpec rnpPbnNavSpec;

    /**
     * Gets the value of the isVorDmeRnav property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsVorDmeRnav() {
        return isVorDmeRnav;
    }

    /**
     * Sets the value of the isVorDmeRnav property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsVorDmeRnav(Boolean value) {
        this.isVorDmeRnav = value;
    }

    /**
     * Gets the value of the rnavPbnNavSpec property.
     * 
     * @return
     *     possible object is
     *     {@link RnavPbnNavSpec }
     *     
     */
    public RnavPbnNavSpec getRnavPbnNavSpec() {
        return rnavPbnNavSpec;
    }

    /**
     * Sets the value of the rnavPbnNavSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link RnavPbnNavSpec }
     *     
     */
    public void setRnavPbnNavSpec(RnavPbnNavSpec value) {
        this.rnavPbnNavSpec = value;
    }

    /**
     * Gets the value of the rnpPbnNavSpec property.
     * 
     * @return
     *     possible object is
     *     {@link RnpPbnNavSpec }
     *     
     */
    public RnpPbnNavSpec getRnpPbnNavSpec() {
        return rnpPbnNavSpec;
    }

    /**
     * Sets the value of the rnpPbnNavSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link RnpPbnNavSpec }
     *     
     */
    public void setRnpPbnNavSpec(RnpPbnNavSpec value) {
        this.rnpPbnNavSpec = value;
    }

}
