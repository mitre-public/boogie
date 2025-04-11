
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This type provides the aircraft category(s) for which the procedure or portion of a procedure (transition) was designed.
 * 			
 * 
 * <p>Java class for ProcedureDesignAircraftCategories complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProcedureDesignAircraftCategories"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isCategoryA" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isCategoryB" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isCategoryC" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isCategoryD" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isCategoryE" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isCategoryHelicopter" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcedureDesignAircraftCategories", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "isCategoryA",
    "isCategoryB",
    "isCategoryC",
    "isCategoryD",
    "isCategoryE",
    "isCategoryHelicopter"
})
public class ProcedureDesignAircraftCategories {

    @XmlElement(defaultValue = "false")
    protected Boolean isCategoryA;
    @XmlElement(defaultValue = "false")
    protected Boolean isCategoryB;
    @XmlElement(defaultValue = "false")
    protected Boolean isCategoryC;
    @XmlElement(defaultValue = "false")
    protected Boolean isCategoryD;
    @XmlElement(defaultValue = "false")
    protected Boolean isCategoryE;
    @XmlElement(defaultValue = "false")
    protected Boolean isCategoryHelicopter;

    /**
     * Gets the value of the isCategoryA property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCategoryA() {
        return isCategoryA;
    }

    /**
     * Sets the value of the isCategoryA property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCategoryA(Boolean value) {
        this.isCategoryA = value;
    }

    /**
     * Gets the value of the isCategoryB property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCategoryB() {
        return isCategoryB;
    }

    /**
     * Sets the value of the isCategoryB property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCategoryB(Boolean value) {
        this.isCategoryB = value;
    }

    /**
     * Gets the value of the isCategoryC property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCategoryC() {
        return isCategoryC;
    }

    /**
     * Sets the value of the isCategoryC property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCategoryC(Boolean value) {
        this.isCategoryC = value;
    }

    /**
     * Gets the value of the isCategoryD property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCategoryD() {
        return isCategoryD;
    }

    /**
     * Sets the value of the isCategoryD property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCategoryD(Boolean value) {
        this.isCategoryD = value;
    }

    /**
     * Gets the value of the isCategoryE property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCategoryE() {
        return isCategoryE;
    }

    /**
     * Sets the value of the isCategoryE property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCategoryE(Boolean value) {
        this.isCategoryE = value;
    }

    /**
     * Gets the value of the isCategoryHelicopter property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCategoryHelicopter() {
        return isCategoryHelicopter;
    }

    /**
     * Sets the value of the isCategoryHelicopter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCategoryHelicopter(Boolean value) {
        this.isCategoryHelicopter = value;
    }

}
