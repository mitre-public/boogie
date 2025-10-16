
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Defines the dimensions of the helicopter landing pad. There are three dimension fields, for the TLOF area, for the FATO area and for the safety area.
 * 			
 * 
 * <p>Java class for HelipadDimensions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HelipadDimensions"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="padLengthLongSide" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="padLengthShortSide" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="padDiameter" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HelipadDimensions", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "padLengthLongSide",
    "padLengthShortSide",
    "padDiameter"
})
public class HelipadDimensions {

    protected Integer padLengthLongSide;
    protected Integer padLengthShortSide;
    protected Integer padDiameter;

    /**
     * Gets the value of the padLengthLongSide property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPadLengthLongSide() {
        return padLengthLongSide;
    }

    /**
     * Sets the value of the padLengthLongSide property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPadLengthLongSide(Integer value) {
        this.padLengthLongSide = value;
    }

    /**
     * Gets the value of the padLengthShortSide property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPadLengthShortSide() {
        return padLengthShortSide;
    }

    /**
     * Sets the value of the padLengthShortSide property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPadLengthShortSide(Integer value) {
        this.padLengthShortSide = value;
    }

    /**
     * Gets the value of the padDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPadDiameter() {
        return padDiameter;
    }

    /**
     * Sets the value of the padDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPadDiameter(Integer value) {
        this.padDiameter = value;
    }

}
