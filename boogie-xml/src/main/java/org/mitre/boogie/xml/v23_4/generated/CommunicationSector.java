
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Represents the details for a Communication Sector.
 * 
 * <p>Java class for CommunicationSector complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CommunicationSector"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sectorAltitude" type="{http://www.arinc424.com/xml/datatypes}AltitudeConstraint" maxOccurs="2" minOccurs="0"/&gt;
 *         &lt;element name="sectorBearings" type="{http://www.arinc424.com/xml/datatypes}SectorBearings" minOccurs="0"/&gt;
 *         &lt;element name="sectorNarrative" type="{http://www.arinc424.com/xml/datatypes}SectorizationNarrative" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommunicationSector", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "sectorAltitude",
    "sectorBearings",
    "sectorNarrative"
})
public class CommunicationSector {

    protected List<AltitudeConstraint> sectorAltitude;
    protected SectorBearings sectorBearings;
    protected String sectorNarrative;

    /**
     * Gets the value of the sectorAltitude property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the sectorAltitude property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSectorAltitude().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AltitudeConstraint }
     * 
     * 
     */
    public List<AltitudeConstraint> getSectorAltitude() {
        if (sectorAltitude == null) {
            sectorAltitude = new ArrayList<AltitudeConstraint>();
        }
        return this.sectorAltitude;
    }

    /**
     * Gets the value of the sectorBearings property.
     * 
     * @return
     *     possible object is
     *     {@link SectorBearings }
     *     
     */
    public SectorBearings getSectorBearings() {
        return sectorBearings;
    }

    /**
     * Sets the value of the sectorBearings property.
     * 
     * @param value
     *     allowed object is
     *     {@link SectorBearings }
     *     
     */
    public void setSectorBearings(SectorBearings value) {
        this.sectorBearings = value;
    }

    /**
     * Gets the value of the sectorNarrative property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectorNarrative() {
        return sectorNarrative;
    }

    /**
     * Sets the value of the sectorNarrative property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectorNarrative(String value) {
        this.sectorNarrative = value;
    }

}
