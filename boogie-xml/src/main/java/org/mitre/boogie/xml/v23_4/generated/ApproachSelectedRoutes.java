
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApproachSelectedRoutes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApproachSelectedRoutes"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="selectedFinal" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="selectedApproachTransition" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApproachSelectedRoutes", propOrder = {
    "selectedFinal",
    "selectedApproachTransition"
})
public class ApproachSelectedRoutes {

    protected String selectedFinal;
    protected String selectedApproachTransition;

    /**
     * Gets the value of the selectedFinal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectedFinal() {
        return selectedFinal;
    }

    /**
     * Sets the value of the selectedFinal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectedFinal(String value) {
        this.selectedFinal = value;
    }

    /**
     * Gets the value of the selectedApproachTransition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectedApproachTransition() {
        return selectedApproachTransition;
    }

    /**
     * Sets the value of the selectedApproachTransition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectedApproachTransition(String value) {
        this.selectedApproachTransition = value;
    }

}
