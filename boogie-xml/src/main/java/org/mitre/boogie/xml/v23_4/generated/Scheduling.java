
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 To arrange for an air refueling you must
 *                 first contact a scheduling unit. This complexType 
 *                 contains theinformation you need to contact the unit.
 *             
 * 
 * <p>Java class for Scheduling complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Scheduling"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}schedulingUnit" minOccurs="0"/&gt;
 *         &lt;element ref="{}dsnTelephoneNumber" minOccurs="0"/&gt;
 *         &lt;element ref="{}commercialTelephoneNumber" minOccurs="0"/&gt;
 *         &lt;element ref="{}footnotes" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Scheduling", propOrder = {
    "schedulingUnit",
    "dsnTelephoneNumber",
    "commercialTelephoneNumber",
    "footnotes"
})
public class Scheduling {

    protected String schedulingUnit;
    protected String dsnTelephoneNumber;
    protected String commercialTelephoneNumber;
    protected FootnotesType footnotes;

    /**
     * Gets the value of the schedulingUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchedulingUnit() {
        return schedulingUnit;
    }

    /**
     * Sets the value of the schedulingUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchedulingUnit(String value) {
        this.schedulingUnit = value;
    }

    /**
     * Gets the value of the dsnTelephoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDsnTelephoneNumber() {
        return dsnTelephoneNumber;
    }

    /**
     * Sets the value of the dsnTelephoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDsnTelephoneNumber(String value) {
        this.dsnTelephoneNumber = value;
    }

    /**
     * Gets the value of the commercialTelephoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommercialTelephoneNumber() {
        return commercialTelephoneNumber;
    }

    /**
     * Sets the value of the commercialTelephoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommercialTelephoneNumber(String value) {
        this.commercialTelephoneNumber = value;
    }

    /**
     * Gets the value of the footnotes property.
     * 
     * @return
     *     possible object is
     *     {@link FootnotesType }
     *     
     */
    public FootnotesType getFootnotes() {
        return footnotes;
    }

    /**
     * Sets the value of the footnotes property.
     * 
     * @param value
     *     allowed object is
     *     {@link FootnotesType }
     *     
     */
    public void setFootnotes(FootnotesType value) {
        this.footnotes = value;
    }

}
