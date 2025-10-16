
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class is an abstraction used to represent the direction of flight to which the cruising levels apply.
 * 
 * <p>Java class for CruiseDirection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CruiseDirection"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="courseFrom" type="{http://www.arinc424.com/xml/datatypes}CourseValue"/&gt;
 *         &lt;element name="courseTo" type="{http://www.arinc424.com/xml/datatypes}CourseValue"/&gt;
 *         &lt;element name="magneticTrueIndicator" type="{http://www.arinc424.com/xml/enumerations}MagneticTrueIndicator"/&gt;
 *         &lt;element name="cruiseVerticalDetails" type="{}CruiseVerticalDetails" maxOccurs="unbounded"/&gt;
 *         &lt;element name="cycleDate" type="{http://www.arinc424.com/xml/datatypes}CycleDate"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CruiseDirection", propOrder = {
    "courseFrom",
    "courseTo",
    "magneticTrueIndicator",
    "cruiseVerticalDetails",
    "cycleDate"
})
public class CruiseDirection {

    @XmlElement(required = true)
    protected BigDecimal courseFrom;
    @XmlElement(required = true)
    protected BigDecimal courseTo;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected MagneticTrueIndicator magneticTrueIndicator;
    @XmlElement(required = true)
    protected List<CruiseVerticalDetails> cruiseVerticalDetails;
    @XmlElement(required = true)
    protected String cycleDate;

    /**
     * Gets the value of the courseFrom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCourseFrom() {
        return courseFrom;
    }

    /**
     * Sets the value of the courseFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCourseFrom(BigDecimal value) {
        this.courseFrom = value;
    }

    /**
     * Gets the value of the courseTo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCourseTo() {
        return courseTo;
    }

    /**
     * Sets the value of the courseTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCourseTo(BigDecimal value) {
        this.courseTo = value;
    }

    /**
     * Gets the value of the magneticTrueIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link MagneticTrueIndicator }
     *     
     */
    public MagneticTrueIndicator getMagneticTrueIndicator() {
        return magneticTrueIndicator;
    }

    /**
     * Sets the value of the magneticTrueIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link MagneticTrueIndicator }
     *     
     */
    public void setMagneticTrueIndicator(MagneticTrueIndicator value) {
        this.magneticTrueIndicator = value;
    }

    /**
     * Gets the value of the cruiseVerticalDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the cruiseVerticalDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCruiseVerticalDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CruiseVerticalDetails }
     * 
     * 
     */
    public List<CruiseVerticalDetails> getCruiseVerticalDetails() {
        if (cruiseVerticalDetails == null) {
            cruiseVerticalDetails = new ArrayList<CruiseVerticalDetails>();
        }
        return this.cruiseVerticalDetails;
    }

    /**
     * Gets the value of the cycleDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCycleDate() {
        return cycleDate;
    }

    /**
     * Sets the value of the cycleDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCycleDate(String value) {
        this.cycleDate = value;
    }

}
