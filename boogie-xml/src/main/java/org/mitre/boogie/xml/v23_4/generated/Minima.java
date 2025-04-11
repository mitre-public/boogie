
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * A minima for an airport/heliport approach procedure.
 * 
 * <p>Java class for Minima complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Minima"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="minimaNa" type="{http://www.arinc424.com/xml/enumerations}NaType" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="landingMinima" type="{}LandingMinima" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Minima", propOrder = {
    "minimaNa",
    "name",
    "landingMinima"
})
public class Minima
    extends A424Record
{

    @XmlSchemaType(name = "string")
    protected NaType minimaNa;
    @XmlElement(required = true)
    protected String name;
    protected LandingMinima landingMinima;

    /**
     * Gets the value of the minimaNa property.
     * 
     * @return
     *     possible object is
     *     {@link NaType }
     *     
     */
    public NaType getMinimaNa() {
        return minimaNa;
    }

    /**
     * Sets the value of the minimaNa property.
     * 
     * @param value
     *     allowed object is
     *     {@link NaType }
     *     
     */
    public void setMinimaNa(NaType value) {
        this.minimaNa = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the landingMinima property.
     * 
     * @return
     *     possible object is
     *     {@link LandingMinima }
     *     
     */
    public LandingMinima getLandingMinima() {
        return landingMinima;
    }

    /**
     * Sets the value of the landingMinima property.
     * 
     * @param value
     *     allowed object is
     *     {@link LandingMinima }
     *     
     */
    public void setLandingMinima(LandingMinima value) {
        this.landingMinima = value;
    }

}
