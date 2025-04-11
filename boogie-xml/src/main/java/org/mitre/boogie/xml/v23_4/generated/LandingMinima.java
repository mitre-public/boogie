
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The landing minima data for an airport/heliport approach.
 * 
 * <p>Java class for LandingMinima complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LandingMinima"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="alignment" type="{http://www.arinc424.com/xml/enumerations}MinimaAlignment"/&gt;
 *         &lt;element name="finalGuidanceSystem" type="{http://www.arinc424.com/xml/enumerations}FinalGuidanceSystem" minOccurs="0"/&gt;
 *         &lt;element name="isDerived" type="{http://www.arinc424.com/xml/datatypes}IsDerived" minOccurs="0"/&gt;
 *         &lt;element name="remoteAltimeterSettingPort" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="rnp" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="sidestepRunway" type="{http://www.arinc424.com/xml/datatypes}PointReference" maxOccurs="2" minOccurs="0"/&gt;
 *         &lt;element name="stepdownFix" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="whenCondition" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="4" minOccurs="0"/&gt;
 *         &lt;element name="category" type="{}LandingMinimaCategory" maxOccurs="7" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LandingMinima", propOrder = {
    "alignment",
    "finalGuidanceSystem",
    "isDerived",
    "remoteAltimeterSettingPort",
    "rnp",
    "sidestepRunway",
    "stepdownFix",
    "whenCondition",
    "category"
})
public class LandingMinima {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected MinimaAlignment alignment;
    @XmlSchemaType(name = "string")
    protected FinalGuidanceSystem finalGuidanceSystem;
    @XmlElement(defaultValue = "false")
    protected Boolean isDerived;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object remoteAltimeterSettingPort;
    protected BigDecimal rnp;
    @XmlElementRef(name = "sidestepRunway", type = JAXBElement.class, required = false)
    protected List<JAXBElement<Object>> sidestepRunway;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object stepdownFix;
    protected List<String> whenCondition;
    protected List<LandingMinimaCategory> category;

    /**
     * Gets the value of the alignment property.
     * 
     * @return
     *     possible object is
     *     {@link MinimaAlignment }
     *     
     */
    public MinimaAlignment getAlignment() {
        return alignment;
    }

    /**
     * Sets the value of the alignment property.
     * 
     * @param value
     *     allowed object is
     *     {@link MinimaAlignment }
     *     
     */
    public void setAlignment(MinimaAlignment value) {
        this.alignment = value;
    }

    /**
     * Gets the value of the finalGuidanceSystem property.
     * 
     * @return
     *     possible object is
     *     {@link FinalGuidanceSystem }
     *     
     */
    public FinalGuidanceSystem getFinalGuidanceSystem() {
        return finalGuidanceSystem;
    }

    /**
     * Sets the value of the finalGuidanceSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link FinalGuidanceSystem }
     *     
     */
    public void setFinalGuidanceSystem(FinalGuidanceSystem value) {
        this.finalGuidanceSystem = value;
    }

    /**
     * Gets the value of the isDerived property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDerived() {
        return isDerived;
    }

    /**
     * Sets the value of the isDerived property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDerived(Boolean value) {
        this.isDerived = value;
    }

    /**
     * Gets the value of the remoteAltimeterSettingPort property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRemoteAltimeterSettingPort() {
        return remoteAltimeterSettingPort;
    }

    /**
     * Sets the value of the remoteAltimeterSettingPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRemoteAltimeterSettingPort(Object value) {
        this.remoteAltimeterSettingPort = value;
    }

    /**
     * Gets the value of the rnp property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRnp() {
        return rnp;
    }

    /**
     * Sets the value of the rnp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRnp(BigDecimal value) {
        this.rnp = value;
    }

    /**
     * Gets the value of the sidestepRunway property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the sidestepRunway property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSidestepRunway().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * 
     * 
     */
    public List<JAXBElement<Object>> getSidestepRunway() {
        if (sidestepRunway == null) {
            sidestepRunway = new ArrayList<JAXBElement<Object>>();
        }
        return this.sidestepRunway;
    }

    /**
     * Gets the value of the stepdownFix property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getStepdownFix() {
        return stepdownFix;
    }

    /**
     * Sets the value of the stepdownFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setStepdownFix(Object value) {
        this.stepdownFix = value;
    }

    /**
     * Gets the value of the whenCondition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the whenCondition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWhenCondition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getWhenCondition() {
        if (whenCondition == null) {
            whenCondition = new ArrayList<String>();
        }
        return this.whenCondition;
    }

    /**
     * Gets the value of the category property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the category property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LandingMinimaCategory }
     * 
     * 
     */
    public List<LandingMinimaCategory> getCategory() {
        if (category == null) {
            category = new ArrayList<LandingMinimaCategory>();
        }
        return this.category;
    }

}
