
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Heliport Record
 * 
 * <p>Java class for Heliport complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Heliport"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Port"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="heliportType" type="{http://www.arinc424.com/xml/enumerations}HeliportType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Heliport", propOrder = {
    "heliportType"
})
public class Heliport
    extends Port
{

    @XmlSchemaType(name = "string")
    protected HeliportType heliportType;

    /**
     * Gets the value of the heliportType property.
     * 
     * @return
     *     possible object is
     *     {@link HeliportType }
     *     
     */
    public HeliportType getHeliportType() {
        return heliportType;
    }

    /**
     * Sets the value of the heliportType property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeliportType }
     *     
     */
    public void setHeliportType(HeliportType value) {
        this.heliportType = value;
    }

}
