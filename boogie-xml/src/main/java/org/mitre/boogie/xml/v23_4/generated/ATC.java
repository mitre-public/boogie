
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 Certain points on an air refueling route
 *                 require that air traffic control (ATC) be
 *                 contacted. Here's the information you need
 *                 to contact ATC.
 *             
 * 
 * <p>Java class for ATC complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ATC"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}contactAtThisPoint"/&gt;
 *         &lt;element ref="{}center" minOccurs="0"/&gt;
 *         &lt;element ref="{}frequency" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{}airTrafficControlRemark" minOccurs="0"/&gt;
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
@XmlType(name = "ATC", propOrder = {
    "contactAtThisPoint",
    "center",
    "frequency",
    "airTrafficControlRemark",
    "footnotes"
})
public class ATC {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected AirRefuelingUsage contactAtThisPoint;
    protected String center;
    protected List<FrequencyPlusDirection> frequency;
    protected String airTrafficControlRemark;
    protected FootnotesType footnotes;

    /**
     * Gets the value of the contactAtThisPoint property.
     * 
     * @return
     *     possible object is
     *     {@link AirRefuelingUsage }
     *     
     */
    public AirRefuelingUsage getContactAtThisPoint() {
        return contactAtThisPoint;
    }

    /**
     * Sets the value of the contactAtThisPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirRefuelingUsage }
     *     
     */
    public void setContactAtThisPoint(AirRefuelingUsage value) {
        this.contactAtThisPoint = value;
    }

    /**
     * Gets the value of the center property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCenter() {
        return center;
    }

    /**
     * Sets the value of the center property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCenter(String value) {
        this.center = value;
    }

    /**
     * Gets the value of the frequency property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the frequency property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFrequency().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FrequencyPlusDirection }
     * 
     * 
     */
    public List<FrequencyPlusDirection> getFrequency() {
        if (frequency == null) {
            frequency = new ArrayList<FrequencyPlusDirection>();
        }
        return this.frequency;
    }

    /**
     * Gets the value of the airTrafficControlRemark property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAirTrafficControlRemark() {
        return airTrafficControlRemark;
    }

    /**
     * Sets the value of the airTrafficControlRemark property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAirTrafficControlRemark(String value) {
        this.airTrafficControlRemark = value;
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
