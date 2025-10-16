
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Used to represent levels and separations in cruising tables.  These vales are somewhat unique in that they may be specified in feet or meters.
 * 
 * <p>Java class for CruiseLevelOrSeparation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CruiseLevelOrSeparation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="level"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt"&gt;
 *               &lt;totalDigits value="5"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="feetOrMeters" type="{http://www.arinc424.com/xml/enumerations}HeightUnitsIndicator"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CruiseLevelOrSeparation", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "level",
    "feetOrMeters"
})
public class CruiseLevelOrSeparation {

    protected long level;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected HeightUnitsIndicator feetOrMeters;

    /**
     * Gets the value of the level property.
     * 
     */
    public long getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     */
    public void setLevel(long value) {
        this.level = value;
    }

    /**
     * Gets the value of the feetOrMeters property.
     * 
     * @return
     *     possible object is
     *     {@link HeightUnitsIndicator }
     *     
     */
    public HeightUnitsIndicator getFeetOrMeters() {
        return feetOrMeters;
    }

    /**
     * Sets the value of the feetOrMeters property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeightUnitsIndicator }
     *     
     */
    public void setFeetOrMeters(HeightUnitsIndicator value) {
        this.feetOrMeters = value;
    }

}
