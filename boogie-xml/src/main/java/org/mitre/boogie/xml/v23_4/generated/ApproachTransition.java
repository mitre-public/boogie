
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class is an abstraction of Approach Transition.
 * 
 * <p>Java class for ApproachTransition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApproachTransition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}ApproachRoute"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="multipleIndicator" type="{http://www.arinc424.com/xml/datatypes}MultipleIndicator" minOccurs="0"/&gt;
 *         &lt;element name="taaRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/&gt;
 *         &lt;element name="isTfOverlay" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApproachTransition", propOrder = {
    "multipleIndicator",
    "taaRef",
    "isTfOverlay"
})
public class ApproachTransition
    extends ApproachRoute
{

    protected String multipleIndicator;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object taaRef;
    @XmlElement(defaultValue = "false")
    protected Boolean isTfOverlay;

    /**
     * Gets the value of the multipleIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMultipleIndicator() {
        return multipleIndicator;
    }

    /**
     * Sets the value of the multipleIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMultipleIndicator(String value) {
        this.multipleIndicator = value;
    }

    /**
     * Gets the value of the taaRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getTaaRef() {
        return taaRef;
    }

    /**
     * Sets the value of the taaRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setTaaRef(Object value) {
        this.taaRef = value;
    }

    /**
     * Gets the value of the isTfOverlay property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTfOverlay() {
        return isTfOverlay;
    }

    /**
     * Sets the value of the isTfOverlay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTfOverlay(Boolean value) {
        this.isTfOverlay = value;
    }

}
