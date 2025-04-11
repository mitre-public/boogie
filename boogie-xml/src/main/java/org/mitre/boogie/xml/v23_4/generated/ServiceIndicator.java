
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The “Service Indicator” field is used to further define the use of the frequency for the specified Communication Type.
 * 
 * <p>Java class for ServiceIndicator complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceIndicator"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.arinc424.com/xml/datatypes}Alpha"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="airportHeliportCommunicationServiceIndicator" type="{http://www.arinc424.com/xml/datatypes}AirportHeliportCommunicationServiceIndicator" minOccurs="0"/&gt;
 *         &lt;element name="enrouteCommunicationServiceIndicator" type="{http://www.arinc424.com/xml/datatypes}EnrouteCommunicationServiceIndicator" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceIndicator", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "airportHeliportCommunicationServiceIndicator",
    "enrouteCommunicationServiceIndicator"
})
public class ServiceIndicator
    extends Alpha
{

    protected AirportHeliportCommunicationServiceIndicator airportHeliportCommunicationServiceIndicator;
    protected EnrouteCommunicationServiceIndicator enrouteCommunicationServiceIndicator;

    /**
     * Gets the value of the airportHeliportCommunicationServiceIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link AirportHeliportCommunicationServiceIndicator }
     *     
     */
    public AirportHeliportCommunicationServiceIndicator getAirportHeliportCommunicationServiceIndicator() {
        return airportHeliportCommunicationServiceIndicator;
    }

    /**
     * Sets the value of the airportHeliportCommunicationServiceIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirportHeliportCommunicationServiceIndicator }
     *     
     */
    public void setAirportHeliportCommunicationServiceIndicator(AirportHeliportCommunicationServiceIndicator value) {
        this.airportHeliportCommunicationServiceIndicator = value;
    }

    /**
     * Gets the value of the enrouteCommunicationServiceIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link EnrouteCommunicationServiceIndicator }
     *     
     */
    public EnrouteCommunicationServiceIndicator getEnrouteCommunicationServiceIndicator() {
        return enrouteCommunicationServiceIndicator;
    }

    /**
     * Sets the value of the enrouteCommunicationServiceIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnrouteCommunicationServiceIndicator }
     *     
     */
    public void setEnrouteCommunicationServiceIndicator(EnrouteCommunicationServiceIndicator value) {
        this.enrouteCommunicationServiceIndicator = value;
    }

}
