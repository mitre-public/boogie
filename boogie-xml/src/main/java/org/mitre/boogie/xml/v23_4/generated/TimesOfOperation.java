
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Used on airspace, flight planning, communications, and preferred route records to specify the times of operations of those entities.
 * 
 * <p>Java class for TimesOfOperation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TimesOfOperation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="timeCode" type="{http://www.arinc424.com/xml/enumerations}TimeCode"/&gt;
 *         &lt;element name="times" type="{http://www.arinc424.com/xml/datatypes}TimeOfOperationInterval" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="timeNarrative" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="byNotam" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="adjustForDst" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimesOfOperation", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "timeCode",
    "times",
    "timeNarrative",
    "byNotam",
    "adjustForDst"
})
public class TimesOfOperation {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TimeCode timeCode;
    protected List<TimeOfOperationInterval> times;
    protected String timeNarrative;
    @XmlElement(defaultValue = "false")
    protected Boolean byNotam;
    @XmlElement(defaultValue = "false")
    protected Boolean adjustForDst;

    /**
     * Gets the value of the timeCode property.
     * 
     * @return
     *     possible object is
     *     {@link TimeCode }
     *     
     */
    public TimeCode getTimeCode() {
        return timeCode;
    }

    /**
     * Sets the value of the timeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeCode }
     *     
     */
    public void setTimeCode(TimeCode value) {
        this.timeCode = value;
    }

    /**
     * Gets the value of the times property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the times property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TimeOfOperationInterval }
     * 
     * 
     */
    public List<TimeOfOperationInterval> getTimes() {
        if (times == null) {
            times = new ArrayList<TimeOfOperationInterval>();
        }
        return this.times;
    }

    /**
     * Gets the value of the timeNarrative property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeNarrative() {
        return timeNarrative;
    }

    /**
     * Sets the value of the timeNarrative property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeNarrative(String value) {
        this.timeNarrative = value;
    }

    /**
     * Gets the value of the byNotam property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isByNotam() {
        return byNotam;
    }

    /**
     * Sets the value of the byNotam property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setByNotam(Boolean value) {
        this.byNotam = value;
    }

    /**
     * Gets the value of the adjustForDst property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAdjustForDst() {
        return adjustForDst;
    }

    /**
     * Sets the value of the adjustForDst property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAdjustForDst(Boolean value) {
        this.adjustForDst = value;
    }

}
