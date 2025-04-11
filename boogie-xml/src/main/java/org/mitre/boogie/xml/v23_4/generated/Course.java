
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Course complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Course"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="courseValue" type="{http://www.arinc424.com/xml/datatypes}CourseValue"/&gt;
 *         &lt;element name="isTrue" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Course", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "courseValue",
    "isTrue"
})
public class Course {

    @XmlElement(required = true)
    protected BigDecimal courseValue;
    @XmlElement(defaultValue = "false")
    protected Boolean isTrue;

    /**
     * Gets the value of the courseValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCourseValue() {
        return courseValue;
    }

    /**
     * Sets the value of the courseValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCourseValue(BigDecimal value) {
        this.courseValue = value;
    }

    /**
     * Gets the value of the isTrue property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTrue() {
        return isTrue;
    }

    /**
     * Sets the value of the isTrue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTrue(Boolean value) {
        this.isTrue = value;
    }

}
