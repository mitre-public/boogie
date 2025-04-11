
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This data type is a child of Leg and allows for a choice between having the leg reference a fix or airway.
 * 
 * <p>Java class for ViaEnrouteLeg complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ViaEnrouteLeg"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Leg"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="selectedEnroute" type="{}SelectedEnroute"/&gt;
 *         &lt;element name="cruisingAltitude" type="{http://www.arinc424.com/xml/datatypes}AltitudeValue" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ViaEnrouteLeg", propOrder = {
    "selectedEnroute",
    "cruisingAltitude"
})
public class ViaEnrouteLeg
    extends Leg
{

    @XmlElement(required = true)
    protected SelectedEnroute selectedEnroute;
    @XmlSchemaType(name = "integer")
    protected Integer cruisingAltitude;

    /**
     * Gets the value of the selectedEnroute property.
     * 
     * @return
     *     possible object is
     *     {@link SelectedEnroute }
     *     
     */
    public SelectedEnroute getSelectedEnroute() {
        return selectedEnroute;
    }

    /**
     * Sets the value of the selectedEnroute property.
     * 
     * @param value
     *     allowed object is
     *     {@link SelectedEnroute }
     *     
     */
    public void setSelectedEnroute(SelectedEnroute value) {
        this.selectedEnroute = value;
    }

    /**
     * Gets the value of the cruisingAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCruisingAltitude() {
        return cruisingAltitude;
    }

    /**
     * Sets the value of the cruisingAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCruisingAltitude(Integer value) {
        this.cruisingAltitude = value;
    }

}
