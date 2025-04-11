
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The “Preferred Route Use Indicator” provides information on whether the route in question is point-to-point and therefore usable for navigation, or area-to-area and usable only as advisory information which requires further processing. The field will also provide information on whether or not RNAV equipment is required to use the route.
 * 
 * <p>Java class for PreferredRouteUseIndicator complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PreferredRouteUseIndicator"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.arinc424.com/xml/datatypes}Alpha"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="preferredRouteUseIndicatorPointArea" type="{http://www.arinc424.com/xml/enumerations}PreferredRouteUseIndicatorPointArea"/&gt;
 *         &lt;element name="preferredRouteUseIndicatorRnav" type="{http://www.arinc424.com/xml/enumerations}PreferredRouteUseIndicatorRnav"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PreferredRouteUseIndicator", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "preferredRouteUseIndicatorPointArea",
    "preferredRouteUseIndicatorRnav"
})
public class PreferredRouteUseIndicator
    extends Alpha
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected PreferredRouteUseIndicatorPointArea preferredRouteUseIndicatorPointArea;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected PreferredRouteUseIndicatorRnav preferredRouteUseIndicatorRnav;

    /**
     * Gets the value of the preferredRouteUseIndicatorPointArea property.
     * 
     * @return
     *     possible object is
     *     {@link PreferredRouteUseIndicatorPointArea }
     *     
     */
    public PreferredRouteUseIndicatorPointArea getPreferredRouteUseIndicatorPointArea() {
        return preferredRouteUseIndicatorPointArea;
    }

    /**
     * Sets the value of the preferredRouteUseIndicatorPointArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link PreferredRouteUseIndicatorPointArea }
     *     
     */
    public void setPreferredRouteUseIndicatorPointArea(PreferredRouteUseIndicatorPointArea value) {
        this.preferredRouteUseIndicatorPointArea = value;
    }

    /**
     * Gets the value of the preferredRouteUseIndicatorRnav property.
     * 
     * @return
     *     possible object is
     *     {@link PreferredRouteUseIndicatorRnav }
     *     
     */
    public PreferredRouteUseIndicatorRnav getPreferredRouteUseIndicatorRnav() {
        return preferredRouteUseIndicatorRnav;
    }

    /**
     * Sets the value of the preferredRouteUseIndicatorRnav property.
     * 
     * @param value
     *     allowed object is
     *     {@link PreferredRouteUseIndicatorRnav }
     *     
     */
    public void setPreferredRouteUseIndicatorRnav(PreferredRouteUseIndicatorRnav value) {
        this.preferredRouteUseIndicatorRnav = value;
    }

}
