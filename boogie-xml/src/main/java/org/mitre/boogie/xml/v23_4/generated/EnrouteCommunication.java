
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Enroute Communication Records.
 * 
 * <p>Java class for EnrouteCommunication complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EnrouteCommunication"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Communication"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="firRdoIdent" type="{http://www.arinc424.com/xml/datatypes}FirRdoIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="firUirRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/&gt;
 *         &lt;element name="positionNarrative" type="{http://www.arinc424.com/xml/datatypes}PositionNarrative" minOccurs="0"/&gt;
 *         &lt;element name="serviceIndicator" type="{http://www.arinc424.com/xml/datatypes}EnrouteCommunicationServiceIndicator" minOccurs="0"/&gt;
 *         &lt;element name="assignedSectorName" type="{http://www.arinc424.com/xml/datatypes}AssignedSectorName" minOccurs="0"/&gt;
 *         &lt;element name="level" type="{http://www.arinc424.com/xml/enumerations}Level" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnrouteCommunication", propOrder = {
    "firRdoIdent",
    "firUirRef",
    "positionNarrative",
    "serviceIndicator",
    "assignedSectorName",
    "level"
})
public class EnrouteCommunication
    extends Communication
{

    protected String firRdoIdent;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object firUirRef;
    protected String positionNarrative;
    protected EnrouteCommunicationServiceIndicator serviceIndicator;
    protected String assignedSectorName;
    @XmlSchemaType(name = "string")
    protected Level level;

    /**
     * Gets the value of the firRdoIdent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirRdoIdent() {
        return firRdoIdent;
    }

    /**
     * Sets the value of the firRdoIdent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirRdoIdent(String value) {
        this.firRdoIdent = value;
    }

    /**
     * Gets the value of the firUirRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFirUirRef() {
        return firUirRef;
    }

    /**
     * Sets the value of the firUirRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFirUirRef(Object value) {
        this.firUirRef = value;
    }

    /**
     * Gets the value of the positionNarrative property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPositionNarrative() {
        return positionNarrative;
    }

    /**
     * Sets the value of the positionNarrative property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPositionNarrative(String value) {
        this.positionNarrative = value;
    }

    /**
     * Gets the value of the serviceIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link EnrouteCommunicationServiceIndicator }
     *     
     */
    public EnrouteCommunicationServiceIndicator getServiceIndicator() {
        return serviceIndicator;
    }

    /**
     * Sets the value of the serviceIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnrouteCommunicationServiceIndicator }
     *     
     */
    public void setServiceIndicator(EnrouteCommunicationServiceIndicator value) {
        this.serviceIndicator = value;
    }

    /**
     * Gets the value of the assignedSectorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignedSectorName() {
        return assignedSectorName;
    }

    /**
     * Sets the value of the assignedSectorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignedSectorName(String value) {
        this.assignedSectorName = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link Level }
     *     
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link Level }
     *     
     */
    public void setLevel(Level value) {
        this.level = value;
    }

}
