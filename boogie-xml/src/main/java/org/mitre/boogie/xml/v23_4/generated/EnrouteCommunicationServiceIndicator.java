
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The Service Indicator field is used to further define the use of the frequency for the specified Communication Type (5.101).
 * 
 * <p>Java class for EnrouteCommunicationServiceIndicator complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EnrouteCommunicationServiceIndicator"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="serviceIndicatorEnrouteFrequency" type="{http://www.arinc424.com/xml/enumerations}ServiceIndicatorEnrouteFrequency" minOccurs="0"/&gt;
 *         &lt;element name="serviceIndicatorEnrouteInformation" type="{http://www.arinc424.com/xml/enumerations}ServiceIndicatorEnrouteInformation" minOccurs="0"/&gt;
 *         &lt;element name="serviceIndicatorEnrouteService" type="{http://www.arinc424.com/xml/enumerations}ServiceIndicatorEnrouteService" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnrouteCommunicationServiceIndicator", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "serviceIndicatorEnrouteFrequency",
    "serviceIndicatorEnrouteInformation",
    "serviceIndicatorEnrouteService"
})
public class EnrouteCommunicationServiceIndicator {

    @XmlSchemaType(name = "string")
    protected ServiceIndicatorEnrouteFrequency serviceIndicatorEnrouteFrequency;
    @XmlSchemaType(name = "string")
    protected ServiceIndicatorEnrouteInformation serviceIndicatorEnrouteInformation;
    @XmlSchemaType(name = "string")
    protected ServiceIndicatorEnrouteService serviceIndicatorEnrouteService;

    /**
     * Gets the value of the serviceIndicatorEnrouteFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceIndicatorEnrouteFrequency }
     *     
     */
    public ServiceIndicatorEnrouteFrequency getServiceIndicatorEnrouteFrequency() {
        return serviceIndicatorEnrouteFrequency;
    }

    /**
     * Sets the value of the serviceIndicatorEnrouteFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceIndicatorEnrouteFrequency }
     *     
     */
    public void setServiceIndicatorEnrouteFrequency(ServiceIndicatorEnrouteFrequency value) {
        this.serviceIndicatorEnrouteFrequency = value;
    }

    /**
     * Gets the value of the serviceIndicatorEnrouteInformation property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceIndicatorEnrouteInformation }
     *     
     */
    public ServiceIndicatorEnrouteInformation getServiceIndicatorEnrouteInformation() {
        return serviceIndicatorEnrouteInformation;
    }

    /**
     * Sets the value of the serviceIndicatorEnrouteInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceIndicatorEnrouteInformation }
     *     
     */
    public void setServiceIndicatorEnrouteInformation(ServiceIndicatorEnrouteInformation value) {
        this.serviceIndicatorEnrouteInformation = value;
    }

    /**
     * Gets the value of the serviceIndicatorEnrouteService property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceIndicatorEnrouteService }
     *     
     */
    public ServiceIndicatorEnrouteService getServiceIndicatorEnrouteService() {
        return serviceIndicatorEnrouteService;
    }

    /**
     * Sets the value of the serviceIndicatorEnrouteService property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceIndicatorEnrouteService }
     *     
     */
    public void setServiceIndicatorEnrouteService(ServiceIndicatorEnrouteService value) {
        this.serviceIndicatorEnrouteService = value;
    }

}
