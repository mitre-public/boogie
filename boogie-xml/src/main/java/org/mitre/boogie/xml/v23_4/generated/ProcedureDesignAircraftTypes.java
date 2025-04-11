
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This type provides the aircraft type(s) for which the procedure or portion of a procedure (transition) was designed.
 * 			
 * 
 * <p>Java class for ProcedureDesignAircraftTypes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProcedureDesignAircraftTypes"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isTypeJet" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isTypeTurbojet" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isTypeTurboprop" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isTypeProp" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isTypePiston" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isTypeNonJets" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isTypeNotLimited" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isTypeNonTurbojets" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcedureDesignAircraftTypes", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "isTypeJet",
    "isTypeTurbojet",
    "isTypeTurboprop",
    "isTypeProp",
    "isTypePiston",
    "isTypeNonJets",
    "isTypeNotLimited",
    "isTypeNonTurbojets"
})
public class ProcedureDesignAircraftTypes {

    @XmlElement(defaultValue = "false")
    protected Boolean isTypeJet;
    @XmlElement(defaultValue = "false")
    protected Boolean isTypeTurbojet;
    @XmlElement(defaultValue = "false")
    protected Boolean isTypeTurboprop;
    @XmlElement(defaultValue = "false")
    protected Boolean isTypeProp;
    @XmlElement(defaultValue = "false")
    protected Boolean isTypePiston;
    @XmlElement(defaultValue = "false")
    protected Boolean isTypeNonJets;
    @XmlElement(defaultValue = "false")
    protected Boolean isTypeNotLimited;
    @XmlElement(defaultValue = "false")
    protected Boolean isTypeNonTurbojets;

    /**
     * Gets the value of the isTypeJet property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTypeJet() {
        return isTypeJet;
    }

    /**
     * Sets the value of the isTypeJet property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTypeJet(Boolean value) {
        this.isTypeJet = value;
    }

    /**
     * Gets the value of the isTypeTurbojet property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTypeTurbojet() {
        return isTypeTurbojet;
    }

    /**
     * Sets the value of the isTypeTurbojet property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTypeTurbojet(Boolean value) {
        this.isTypeTurbojet = value;
    }

    /**
     * Gets the value of the isTypeTurboprop property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTypeTurboprop() {
        return isTypeTurboprop;
    }

    /**
     * Sets the value of the isTypeTurboprop property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTypeTurboprop(Boolean value) {
        this.isTypeTurboprop = value;
    }

    /**
     * Gets the value of the isTypeProp property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTypeProp() {
        return isTypeProp;
    }

    /**
     * Sets the value of the isTypeProp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTypeProp(Boolean value) {
        this.isTypeProp = value;
    }

    /**
     * Gets the value of the isTypePiston property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTypePiston() {
        return isTypePiston;
    }

    /**
     * Sets the value of the isTypePiston property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTypePiston(Boolean value) {
        this.isTypePiston = value;
    }

    /**
     * Gets the value of the isTypeNonJets property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTypeNonJets() {
        return isTypeNonJets;
    }

    /**
     * Sets the value of the isTypeNonJets property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTypeNonJets(Boolean value) {
        this.isTypeNonJets = value;
    }

    /**
     * Gets the value of the isTypeNotLimited property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTypeNotLimited() {
        return isTypeNotLimited;
    }

    /**
     * Sets the value of the isTypeNotLimited property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTypeNotLimited(Boolean value) {
        this.isTypeNotLimited = value;
    }

    /**
     * Gets the value of the isTypeNonTurbojets property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTypeNonTurbojets() {
        return isTypeNonTurbojets;
    }

    /**
     * Sets the value of the isTypeNonTurbojets property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTypeNonTurbojets(Boolean value) {
        this.isTypeNonTurbojets = value;
    }

}
