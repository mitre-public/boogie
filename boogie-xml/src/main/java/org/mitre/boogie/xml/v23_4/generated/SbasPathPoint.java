
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Path Point Record.
 * 
 * <p>Java class for SbasPathPoint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SbasPathPoint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}PathPoint"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="hal" type="{http://www.arinc424.com/xml/datatypes}HorizontalAlertLimit"/&gt;
 *         &lt;element name="operationType" type="{http://www.arinc424.com/xml/enumerations}SbasOperationType"/&gt;
 *         &lt;element name="sbasServiceProviderIdentifier" type="{http://www.arinc424.com/xml/enumerations}SbasServiceProviderIdentifier"/&gt;
 *         &lt;element name="val" type="{http://www.arinc424.com/xml/datatypes}VerticalAlertLimit"/&gt;
 *         &lt;element name="finalApproachCourse" type="{http://www.arinc424.com/xml/datatypes}Course" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SbasPathPoint", propOrder = {
    "hal",
    "operationType",
    "sbasServiceProviderIdentifier",
    "val",
    "finalApproachCourse"
})
public class SbasPathPoint
    extends PathPoint
{

    @XmlElement(required = true)
    protected BigDecimal hal;
    @XmlElement(required = true)
    protected String operationType;
    @XmlElement(required = true)
    protected String sbasServiceProviderIdentifier;
    @XmlElement(required = true)
    protected BigDecimal val;
    protected Course finalApproachCourse;

    /**
     * Gets the value of the hal property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHal() {
        return hal;
    }

    /**
     * Sets the value of the hal property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHal(BigDecimal value) {
        this.hal = value;
    }

    /**
     * Gets the value of the operationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperationType() {
        return operationType;
    }

    /**
     * Sets the value of the operationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperationType(String value) {
        this.operationType = value;
    }

    /**
     * Gets the value of the sbasServiceProviderIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSbasServiceProviderIdentifier() {
        return sbasServiceProviderIdentifier;
    }

    /**
     * Sets the value of the sbasServiceProviderIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSbasServiceProviderIdentifier(String value) {
        this.sbasServiceProviderIdentifier = value;
    }

    /**
     * Gets the value of the val property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVal() {
        return val;
    }

    /**
     * Sets the value of the val property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVal(BigDecimal value) {
        this.val = value;
    }

    /**
     * Gets the value of the finalApproachCourse property.
     * 
     * @return
     *     possible object is
     *     {@link Course }
     *     
     */
    public Course getFinalApproachCourse() {
        return finalApproachCourse;
    }

    /**
     * Sets the value of the finalApproachCourse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Course }
     *     
     */
    public void setFinalApproachCourse(Course value) {
        this.finalApproachCourse = value;
    }

}
