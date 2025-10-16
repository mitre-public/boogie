
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Communication records.
 * 
 * <p>Java class for PortCommunication complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PortCommunication"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Communication"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="communicationDistance" type="{http://www.arinc424.com/xml/datatypes}CommunicationDistance" minOccurs="0"/&gt;
 *         &lt;element name="distanceDescription" type="{http://www.arinc424.com/xml/enumerations}DistanceDescription" minOccurs="0"/&gt;
 *         &lt;element name="sectorization" type="{http://www.arinc424.com/xml/datatypes}Sectorization" minOccurs="0"/&gt;
 *         &lt;element name="serviceIndicator" type="{http://www.arinc424.com/xml/datatypes}AirportHeliportCommunicationServiceIndicator" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortCommunication", propOrder = {
    "communicationDistance",
    "distanceDescription",
    "sectorization",
    "serviceIndicator"
})
public class PortCommunication
    extends Communication
{

    @XmlSchemaType(name = "unsignedInt")
    protected Long communicationDistance;
    @XmlSchemaType(name = "string")
    protected DistanceDescription distanceDescription;
    protected Sectorization sectorization;
    protected AirportHeliportCommunicationServiceIndicator serviceIndicator;

    /**
     * Gets the value of the communicationDistance property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCommunicationDistance() {
        return communicationDistance;
    }

    /**
     * Sets the value of the communicationDistance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCommunicationDistance(Long value) {
        this.communicationDistance = value;
    }

    /**
     * Gets the value of the distanceDescription property.
     * 
     * @return
     *     possible object is
     *     {@link DistanceDescription }
     *     
     */
    public DistanceDescription getDistanceDescription() {
        return distanceDescription;
    }

    /**
     * Sets the value of the distanceDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link DistanceDescription }
     *     
     */
    public void setDistanceDescription(DistanceDescription value) {
        this.distanceDescription = value;
    }

    /**
     * Gets the value of the sectorization property.
     * 
     * @return
     *     possible object is
     *     {@link Sectorization }
     *     
     */
    public Sectorization getSectorization() {
        return sectorization;
    }

    /**
     * Sets the value of the sectorization property.
     * 
     * @param value
     *     allowed object is
     *     {@link Sectorization }
     *     
     */
    public void setSectorization(Sectorization value) {
        this.sectorization = value;
    }

    /**
     * Gets the value of the serviceIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link AirportHeliportCommunicationServiceIndicator }
     *     
     */
    public AirportHeliportCommunicationServiceIndicator getServiceIndicator() {
        return serviceIndicator;
    }

    /**
     * Sets the value of the serviceIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirportHeliportCommunicationServiceIndicator }
     *     
     */
    public void setServiceIndicator(AirportHeliportCommunicationServiceIndicator value) {
        this.serviceIndicator = value;
    }

}
