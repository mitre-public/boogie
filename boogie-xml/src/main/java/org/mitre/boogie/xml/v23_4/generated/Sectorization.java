
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The “Sectorization” field is used to define the airspace sector a communication frequency is applicable for when an airport defines sectors by bearing from the same point.
 * 
 * <p>Java class for Sectorization complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Sectorization"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="multiSectorIndicator" type="{http://www.arinc424.com/xml/enumerations}MultiSectorIndicator" minOccurs="0"/&gt;
 *         &lt;element name="sectorFacilityRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="communicationSector" type="{http://www.arinc424.com/xml/datatypes}CommunicationSector" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="sectorizationNarrative" type="{http://www.arinc424.com/xml/datatypes}SectorizationNarrative" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sectorization", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "multiSectorIndicator",
    "sectorFacilityRef",
    "communicationSector",
    "sectorizationNarrative"
})
public class Sectorization {

    @XmlSchemaType(name = "string")
    protected MultiSectorIndicator multiSectorIndicator;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object sectorFacilityRef;
    protected List<CommunicationSector> communicationSector;
    protected String sectorizationNarrative;

    /**
     * Gets the value of the multiSectorIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link MultiSectorIndicator }
     *     
     */
    public MultiSectorIndicator getMultiSectorIndicator() {
        return multiSectorIndicator;
    }

    /**
     * Sets the value of the multiSectorIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiSectorIndicator }
     *     
     */
    public void setMultiSectorIndicator(MultiSectorIndicator value) {
        this.multiSectorIndicator = value;
    }

    /**
     * Gets the value of the sectorFacilityRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSectorFacilityRef() {
        return sectorFacilityRef;
    }

    /**
     * Sets the value of the sectorFacilityRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSectorFacilityRef(Object value) {
        this.sectorFacilityRef = value;
    }

    /**
     * Gets the value of the communicationSector property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the communicationSector property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCommunicationSector().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CommunicationSector }
     * 
     * 
     */
    public List<CommunicationSector> getCommunicationSector() {
        if (communicationSector == null) {
            communicationSector = new ArrayList<CommunicationSector>();
        }
        return this.communicationSector;
    }

    /**
     * Gets the value of the sectorizationNarrative property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectorizationNarrative() {
        return sectorizationNarrative;
    }

    /**
     * Sets the value of the sectorizationNarrative property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectorizationNarrative(String value) {
        this.sectorizationNarrative = value;
    }

}
