
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The “Time of Operation” field is used to indicate the times of operation of a Facility or Restriction (See ARINC 424 5.195).
 * 
 * <p>Java class for TimeOfOperationInterval complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TimeOfOperationInterval"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dayOfWeek" type="{http://www.arinc424.com/xml/enumerations}DayOfWeek" maxOccurs="7" minOccurs="0"/&gt;
 *         &lt;element name="start" type="{http://www.arinc424.com/xml/datatypes}StartOrEndTime"/&gt;
 *         &lt;element name="end" type="{http://www.arinc424.com/xml/datatypes}StartOrEndTime"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimeOfOperationInterval", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "dayOfWeek",
    "start",
    "end"
})
public class TimeOfOperationInterval {

    @XmlSchemaType(name = "string")
    protected List<DayOfWeek> dayOfWeek;
    @XmlElement(required = true)
    protected StartOrEndTime start;
    @XmlElement(required = true)
    protected StartOrEndTime end;

    /**
     * Gets the value of the dayOfWeek property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the dayOfWeek property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDayOfWeek().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DayOfWeek }
     * 
     * 
     */
    public List<DayOfWeek> getDayOfWeek() {
        if (dayOfWeek == null) {
            dayOfWeek = new ArrayList<DayOfWeek>();
        }
        return this.dayOfWeek;
    }

    /**
     * Gets the value of the start property.
     * 
     * @return
     *     possible object is
     *     {@link StartOrEndTime }
     *     
     */
    public StartOrEndTime getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     *     allowed object is
     *     {@link StartOrEndTime }
     *     
     */
    public void setStart(StartOrEndTime value) {
        this.start = value;
    }

    /**
     * Gets the value of the end property.
     * 
     * @return
     *     possible object is
     *     {@link StartOrEndTime }
     *     
     */
    public StartOrEndTime getEnd() {
        return end;
    }

    /**
     * Sets the value of the end property.
     * 
     * @param value
     *     allowed object is
     *     {@link StartOrEndTime }
     *     
     */
    public void setEnd(StartOrEndTime value) {
        this.end = value;
    }

}
