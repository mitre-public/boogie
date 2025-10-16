
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The “Service Indicator” field is used to further define the use of the frequency for the specified Communication Type.
 * 
 * <p>Java class for AirportHeliportCommunicationServiceIndicator complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AirportHeliportCommunicationServiceIndicator"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="serviceIndicatorAirportHeliportFrequency" type="{http://www.arinc424.com/xml/enumerations}ServiceIndicatorAirportHeliportFrequency" minOccurs="0"/&gt;
 *         &lt;element name="serviceIndicatorAirportHeliportInformation" type="{http://www.arinc424.com/xml/enumerations}ServiceIndicatorAirportHeliportInformation" minOccurs="0"/&gt;
 *         &lt;element name="serviceIndicatorAirportHeliportService" type="{http://www.arinc424.com/xml/enumerations}ServiceIndicatorAirportHeliportService" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirportHeliportCommunicationServiceIndicator", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "serviceIndicatorAirportHeliportFrequency",
    "serviceIndicatorAirportHeliportInformation",
    "serviceIndicatorAirportHeliportService"
})
public class AirportHeliportCommunicationServiceIndicator {

    @XmlSchemaType(name = "string")
    protected ServiceIndicatorAirportHeliportFrequency serviceIndicatorAirportHeliportFrequency;
    @XmlSchemaType(name = "string")
    protected ServiceIndicatorAirportHeliportInformation serviceIndicatorAirportHeliportInformation;
    @XmlSchemaType(name = "string")
    protected ServiceIndicatorAirportHeliportService serviceIndicatorAirportHeliportService;

    /**
     * Gets the value of the serviceIndicatorAirportHeliportFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceIndicatorAirportHeliportFrequency }
     *     
     */
    public ServiceIndicatorAirportHeliportFrequency getServiceIndicatorAirportHeliportFrequency() {
        return serviceIndicatorAirportHeliportFrequency;
    }

    /**
     * Sets the value of the serviceIndicatorAirportHeliportFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceIndicatorAirportHeliportFrequency }
     *     
     */
    public void setServiceIndicatorAirportHeliportFrequency(ServiceIndicatorAirportHeliportFrequency value) {
        this.serviceIndicatorAirportHeliportFrequency = value;
    }

    /**
     * Gets the value of the serviceIndicatorAirportHeliportInformation property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceIndicatorAirportHeliportInformation }
     *     
     */
    public ServiceIndicatorAirportHeliportInformation getServiceIndicatorAirportHeliportInformation() {
        return serviceIndicatorAirportHeliportInformation;
    }

    /**
     * Sets the value of the serviceIndicatorAirportHeliportInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceIndicatorAirportHeliportInformation }
     *     
     */
    public void setServiceIndicatorAirportHeliportInformation(ServiceIndicatorAirportHeliportInformation value) {
        this.serviceIndicatorAirportHeliportInformation = value;
    }

    /**
     * Gets the value of the serviceIndicatorAirportHeliportService property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceIndicatorAirportHeliportService }
     *     
     */
    public ServiceIndicatorAirportHeliportService getServiceIndicatorAirportHeliportService() {
        return serviceIndicatorAirportHeliportService;
    }

    /**
     * Sets the value of the serviceIndicatorAirportHeliportService property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceIndicatorAirportHeliportService }
     *     
     */
    public void setServiceIndicatorAirportHeliportService(ServiceIndicatorAirportHeliportService value) {
        this.serviceIndicatorAirportHeliportService = value;
    }

}
