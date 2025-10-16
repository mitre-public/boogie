
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This element represents a standard instrument departure.
 * 
 * <p>Java class for Sid complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Sid"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}SidStar"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sidRunwayTransition" type="{}SidRunwayTransition" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="sidCommonRoute" type="{}SidCommonRoute" minOccurs="0"/&gt;
 *         &lt;element name="sidEnrouteTransition" type="{}SidEnrouteTransition" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="isEngineOut" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isVector" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isPInS" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isPInSProceedVisually" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isPInSProceedVfr" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sid", propOrder = {
    "sidRunwayTransition",
    "sidCommonRoute",
    "sidEnrouteTransition",
    "isEngineOut",
    "isVector",
    "isPInS",
    "isPInSProceedVisually",
    "isPInSProceedVfr"
})
public class Sid
    extends SidStar
{

    protected List<SidRunwayTransition> sidRunwayTransition;
    protected SidCommonRoute sidCommonRoute;
    protected List<SidEnrouteTransition> sidEnrouteTransition;
    @XmlElement(defaultValue = "false")
    protected Boolean isEngineOut;
    @XmlElement(defaultValue = "false")
    protected Boolean isVector;
    @XmlElement(defaultValue = "false")
    protected Boolean isPInS;
    @XmlElement(defaultValue = "false")
    protected Boolean isPInSProceedVisually;
    @XmlElement(defaultValue = "false")
    protected Boolean isPInSProceedVfr;

    /**
     * Gets the value of the sidRunwayTransition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the sidRunwayTransition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSidRunwayTransition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SidRunwayTransition }
     * 
     * 
     */
    public List<SidRunwayTransition> getSidRunwayTransition() {
        if (sidRunwayTransition == null) {
            sidRunwayTransition = new ArrayList<SidRunwayTransition>();
        }
        return this.sidRunwayTransition;
    }

    /**
     * Gets the value of the sidCommonRoute property.
     * 
     * @return
     *     possible object is
     *     {@link SidCommonRoute }
     *     
     */
    public SidCommonRoute getSidCommonRoute() {
        return sidCommonRoute;
    }

    /**
     * Sets the value of the sidCommonRoute property.
     * 
     * @param value
     *     allowed object is
     *     {@link SidCommonRoute }
     *     
     */
    public void setSidCommonRoute(SidCommonRoute value) {
        this.sidCommonRoute = value;
    }

    /**
     * Gets the value of the sidEnrouteTransition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the sidEnrouteTransition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSidEnrouteTransition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SidEnrouteTransition }
     * 
     * 
     */
    public List<SidEnrouteTransition> getSidEnrouteTransition() {
        if (sidEnrouteTransition == null) {
            sidEnrouteTransition = new ArrayList<SidEnrouteTransition>();
        }
        return this.sidEnrouteTransition;
    }

    /**
     * Gets the value of the isEngineOut property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsEngineOut() {
        return isEngineOut;
    }

    /**
     * Sets the value of the isEngineOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsEngineOut(Boolean value) {
        this.isEngineOut = value;
    }

    /**
     * Gets the value of the isVector property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsVector() {
        return isVector;
    }

    /**
     * Sets the value of the isVector property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsVector(Boolean value) {
        this.isVector = value;
    }

    /**
     * Gets the value of the isPInS property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPInS() {
        return isPInS;
    }

    /**
     * Sets the value of the isPInS property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPInS(Boolean value) {
        this.isPInS = value;
    }

    /**
     * Gets the value of the isPInSProceedVisually property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPInSProceedVisually() {
        return isPInSProceedVisually;
    }

    /**
     * Sets the value of the isPInSProceedVisually property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPInSProceedVisually(Boolean value) {
        this.isPInSProceedVisually = value;
    }

    /**
     * Gets the value of the isPInSProceedVfr property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPInSProceedVfr() {
        return isPInSProceedVfr;
    }

    /**
     * Sets the value of the isPInSProceedVfr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPInSProceedVfr(Boolean value) {
        this.isPInSProceedVfr = value;
    }

}
