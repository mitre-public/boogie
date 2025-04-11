
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class is an abstraction of Approach. Approach records are represented by this class.
 * 
 * <p>Java class for Approach complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Approach"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Procedure"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="approachRouteType" type="{http://www.arinc424.com/xml/enumerations}ApproachRouteType"/&gt;
 *         &lt;element name="gnssFmsIndicator" type="{http://www.arinc424.com/xml/enumerations}GnssFmsIndicator" minOccurs="0"/&gt;
 *         &lt;element name="gbasPathPoint" type="{}GbasPathPoint" minOccurs="0"/&gt;
 *         &lt;element name="sbasPathPoint" type="{}SbasPathPoint" minOccurs="0"/&gt;
 *         &lt;element name="approachTransition" type="{}ApproachTransition" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="finalApproach" type="{}FinalApproach"/&gt;
 *         &lt;element name="missedApproach" type="{}MissedApproach" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="approachPointRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="categoryARadius" type="{http://www.arinc424.com/xml/datatypes}CategoryRadius" minOccurs="0"/&gt;
 *         &lt;element name="categoryBRadius" type="{http://www.arinc424.com/xml/datatypes}CategoryRadius" minOccurs="0"/&gt;
 *         &lt;element name="categoryCRadius" type="{http://www.arinc424.com/xml/datatypes}CategoryRadius" minOccurs="0"/&gt;
 *         &lt;element name="categoryDRadius" type="{http://www.arinc424.com/xml/datatypes}CategoryRadius" minOccurs="0"/&gt;
 *         &lt;element name="minima" type="{}Minima" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="isRnavVisual" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isPInSProceedVisually" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isPInSProceedVfr" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isLocalizerBackcourse" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isPreferredProcedure" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Approach", propOrder = {
    "approachRouteType",
    "gnssFmsIndicator",
    "gbasPathPoint",
    "sbasPathPoint",
    "approachTransition",
    "finalApproach",
    "missedApproach",
    "approachPointRef",
    "categoryARadius",
    "categoryBRadius",
    "categoryCRadius",
    "categoryDRadius",
    "minima",
    "isRnavVisual",
    "isPInSProceedVisually",
    "isPInSProceedVfr",
    "isLocalizerBackcourse",
    "isPreferredProcedure"
})
public class Approach
    extends Procedure
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected ApproachRouteType approachRouteType;
    @XmlSchemaType(name = "string")
    protected GnssFmsIndicator gnssFmsIndicator;
    protected GbasPathPoint gbasPathPoint;
    protected SbasPathPoint sbasPathPoint;
    protected List<ApproachTransition> approachTransition;
    @XmlElement(required = true)
    protected FinalApproach finalApproach;
    protected List<MissedApproach> missedApproach;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object approachPointRef;
    protected BigDecimal categoryARadius;
    protected BigDecimal categoryBRadius;
    protected BigDecimal categoryCRadius;
    protected BigDecimal categoryDRadius;
    protected List<Minima> minima;
    @XmlElement(defaultValue = "false")
    protected Boolean isRnavVisual;
    @XmlElement(defaultValue = "false")
    protected Boolean isPInSProceedVisually;
    @XmlElement(defaultValue = "false")
    protected Boolean isPInSProceedVfr;
    @XmlElement(defaultValue = "false")
    protected Boolean isLocalizerBackcourse;
    @XmlElement(defaultValue = "false")
    protected Boolean isPreferredProcedure;

    /**
     * Gets the value of the approachRouteType property.
     * 
     * @return
     *     possible object is
     *     {@link ApproachRouteType }
     *     
     */
    public ApproachRouteType getApproachRouteType() {
        return approachRouteType;
    }

    /**
     * Sets the value of the approachRouteType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApproachRouteType }
     *     
     */
    public void setApproachRouteType(ApproachRouteType value) {
        this.approachRouteType = value;
    }

    /**
     * Gets the value of the gnssFmsIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link GnssFmsIndicator }
     *     
     */
    public GnssFmsIndicator getGnssFmsIndicator() {
        return gnssFmsIndicator;
    }

    /**
     * Sets the value of the gnssFmsIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link GnssFmsIndicator }
     *     
     */
    public void setGnssFmsIndicator(GnssFmsIndicator value) {
        this.gnssFmsIndicator = value;
    }

    /**
     * Gets the value of the gbasPathPoint property.
     * 
     * @return
     *     possible object is
     *     {@link GbasPathPoint }
     *     
     */
    public GbasPathPoint getGbasPathPoint() {
        return gbasPathPoint;
    }

    /**
     * Sets the value of the gbasPathPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link GbasPathPoint }
     *     
     */
    public void setGbasPathPoint(GbasPathPoint value) {
        this.gbasPathPoint = value;
    }

    /**
     * Gets the value of the sbasPathPoint property.
     * 
     * @return
     *     possible object is
     *     {@link SbasPathPoint }
     *     
     */
    public SbasPathPoint getSbasPathPoint() {
        return sbasPathPoint;
    }

    /**
     * Sets the value of the sbasPathPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link SbasPathPoint }
     *     
     */
    public void setSbasPathPoint(SbasPathPoint value) {
        this.sbasPathPoint = value;
    }

    /**
     * Gets the value of the approachTransition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the approachTransition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApproachTransition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ApproachTransition }
     * 
     * 
     */
    public List<ApproachTransition> getApproachTransition() {
        if (approachTransition == null) {
            approachTransition = new ArrayList<ApproachTransition>();
        }
        return this.approachTransition;
    }

    /**
     * Gets the value of the finalApproach property.
     * 
     * @return
     *     possible object is
     *     {@link FinalApproach }
     *     
     */
    public FinalApproach getFinalApproach() {
        return finalApproach;
    }

    /**
     * Sets the value of the finalApproach property.
     * 
     * @param value
     *     allowed object is
     *     {@link FinalApproach }
     *     
     */
    public void setFinalApproach(FinalApproach value) {
        this.finalApproach = value;
    }

    /**
     * Gets the value of the missedApproach property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the missedApproach property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMissedApproach().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MissedApproach }
     * 
     * 
     */
    public List<MissedApproach> getMissedApproach() {
        if (missedApproach == null) {
            missedApproach = new ArrayList<MissedApproach>();
        }
        return this.missedApproach;
    }

    /**
     * Gets the value of the approachPointRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getApproachPointRef() {
        return approachPointRef;
    }

    /**
     * Sets the value of the approachPointRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setApproachPointRef(Object value) {
        this.approachPointRef = value;
    }

    /**
     * Gets the value of the categoryARadius property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCategoryARadius() {
        return categoryARadius;
    }

    /**
     * Sets the value of the categoryARadius property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCategoryARadius(BigDecimal value) {
        this.categoryARadius = value;
    }

    /**
     * Gets the value of the categoryBRadius property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCategoryBRadius() {
        return categoryBRadius;
    }

    /**
     * Sets the value of the categoryBRadius property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCategoryBRadius(BigDecimal value) {
        this.categoryBRadius = value;
    }

    /**
     * Gets the value of the categoryCRadius property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCategoryCRadius() {
        return categoryCRadius;
    }

    /**
     * Sets the value of the categoryCRadius property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCategoryCRadius(BigDecimal value) {
        this.categoryCRadius = value;
    }

    /**
     * Gets the value of the categoryDRadius property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCategoryDRadius() {
        return categoryDRadius;
    }

    /**
     * Sets the value of the categoryDRadius property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCategoryDRadius(BigDecimal value) {
        this.categoryDRadius = value;
    }

    /**
     * Gets the value of the minima property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the minima property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMinima().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Minima }
     * 
     * 
     */
    public List<Minima> getMinima() {
        if (minima == null) {
            minima = new ArrayList<Minima>();
        }
        return this.minima;
    }

    /**
     * Gets the value of the isRnavVisual property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRnavVisual() {
        return isRnavVisual;
    }

    /**
     * Sets the value of the isRnavVisual property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRnavVisual(Boolean value) {
        this.isRnavVisual = value;
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

    /**
     * Gets the value of the isLocalizerBackcourse property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsLocalizerBackcourse() {
        return isLocalizerBackcourse;
    }

    /**
     * Sets the value of the isLocalizerBackcourse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsLocalizerBackcourse(Boolean value) {
        this.isLocalizerBackcourse = value;
    }

    /**
     * Gets the value of the isPreferredProcedure property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPreferredProcedure() {
        return isPreferredProcedure;
    }

    /**
     * Sets the value of the isPreferredProcedure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPreferredProcedure(Boolean value) {
        this.isPreferredProcedure = value;
    }

}
