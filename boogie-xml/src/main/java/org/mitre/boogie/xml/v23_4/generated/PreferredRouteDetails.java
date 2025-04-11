
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class is an abstraction used to represent the details Preferred Route Identifier and Preferred Route Use Indicator.
 * 
 * <p>Java class for PreferredRouteDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PreferredRouteDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="preferredRouteIdentifier" type="{http://www.arinc424.com/xml/datatypes}RouteIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="preferredRouteUseIndicator" type="{http://www.arinc424.com/xml/datatypes}PreferredRouteUseIndicator" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PreferredRouteDetails", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "preferredRouteIdentifier",
    "preferredRouteUseIndicator"
})
public class PreferredRouteDetails {

    protected String preferredRouteIdentifier;
    protected PreferredRouteUseIndicator preferredRouteUseIndicator;

    /**
     * Gets the value of the preferredRouteIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreferredRouteIdentifier() {
        return preferredRouteIdentifier;
    }

    /**
     * Sets the value of the preferredRouteIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreferredRouteIdentifier(String value) {
        this.preferredRouteIdentifier = value;
    }

    /**
     * Gets the value of the preferredRouteUseIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link PreferredRouteUseIndicator }
     *     
     */
    public PreferredRouteUseIndicator getPreferredRouteUseIndicator() {
        return preferredRouteUseIndicator;
    }

    /**
     * Sets the value of the preferredRouteUseIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link PreferredRouteUseIndicator }
     *     
     */
    public void setPreferredRouteUseIndicator(PreferredRouteUseIndicator value) {
        this.preferredRouteUseIndicator = value;
    }

}
