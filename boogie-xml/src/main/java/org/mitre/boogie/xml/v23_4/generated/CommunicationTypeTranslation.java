
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Communication Type Translation Record.
 * 
 * <p>Java class for CommunicationTypeTranslation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CommunicationTypeTranslation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="communicationType" type="{http://www.arinc424.com/xml/enumerations}CommunicationType" minOccurs="0"/&gt;
 *         &lt;element name="communicationClass" type="{http://www.arinc424.com/xml/enumerations}CommunicationClass" minOccurs="0"/&gt;
 *         &lt;element name="translation" type="{http://www.arinc424.com/xml/datatypes}Translation" minOccurs="0"/&gt;
 *         &lt;element name="typeRecognizedBy" type="{http://www.arinc424.com/xml/enumerations}TypeRecognizedBy" minOccurs="0"/&gt;
 *         &lt;element name="usedOn" type="{http://www.arinc424.com/xml/enumerations}UsedOn" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommunicationTypeTranslation", propOrder = {
    "communicationType",
    "communicationClass",
    "translation",
    "typeRecognizedBy",
    "usedOn"
})
public class CommunicationTypeTranslation {

    @XmlSchemaType(name = "string")
    protected CommunicationType communicationType;
    @XmlSchemaType(name = "string")
    protected CommunicationClass communicationClass;
    protected String translation;
    @XmlSchemaType(name = "string")
    protected TypeRecognizedBy typeRecognizedBy;
    @XmlSchemaType(name = "string")
    protected UsedOn usedOn;

    /**
     * Gets the value of the communicationType property.
     * 
     * @return
     *     possible object is
     *     {@link CommunicationType }
     *     
     */
    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    /**
     * Sets the value of the communicationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommunicationType }
     *     
     */
    public void setCommunicationType(CommunicationType value) {
        this.communicationType = value;
    }

    /**
     * Gets the value of the communicationClass property.
     * 
     * @return
     *     possible object is
     *     {@link CommunicationClass }
     *     
     */
    public CommunicationClass getCommunicationClass() {
        return communicationClass;
    }

    /**
     * Sets the value of the communicationClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommunicationClass }
     *     
     */
    public void setCommunicationClass(CommunicationClass value) {
        this.communicationClass = value;
    }

    /**
     * Gets the value of the translation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranslation() {
        return translation;
    }

    /**
     * Sets the value of the translation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranslation(String value) {
        this.translation = value;
    }

    /**
     * Gets the value of the typeRecognizedBy property.
     * 
     * @return
     *     possible object is
     *     {@link TypeRecognizedBy }
     *     
     */
    public TypeRecognizedBy getTypeRecognizedBy() {
        return typeRecognizedBy;
    }

    /**
     * Sets the value of the typeRecognizedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeRecognizedBy }
     *     
     */
    public void setTypeRecognizedBy(TypeRecognizedBy value) {
        this.typeRecognizedBy = value;
    }

    /**
     * Gets the value of the usedOn property.
     * 
     * @return
     *     possible object is
     *     {@link UsedOn }
     *     
     */
    public UsedOn getUsedOn() {
        return usedOn;
    }

    /**
     * Sets the value of the usedOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link UsedOn }
     *     
     */
    public void setUsedOn(UsedOn value) {
        this.usedOn = value;
    }

}
