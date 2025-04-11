
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The landing minima data that applies to an individual aircraft category or a group of aircraft categories. See the categoryNA element to determine when minima cateogry information was sourced as NA for these aircraft categories.
 * 
 * <p>Java class for LandingMinimaCategory complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LandingMinimaCategory"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isCatA" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isCatB" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isCatC" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isCatD" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isCatE" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isCatHeli" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isCatLarge" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="categoryNa" type="{http://www.arinc424.com/xml/enumerations}NaType" minOccurs="0"/&gt;
 *         &lt;element name="altitude" type="{http://www.arinc424.com/xml/datatypes}MinimaAltitude" minOccurs="0"/&gt;
 *         &lt;element name="height" type="{http://www.arinc424.com/xml/datatypes}MinimaHeight" minOccurs="0"/&gt;
 *         &lt;element name="radioAltimeterHeight" type="{http://www.arinc424.com/xml/datatypes}RadioAltimeterHeight" minOccurs="0"/&gt;
 *         &lt;element name="isDerived" type="{http://www.arinc424.com/xml/datatypes}IsDerived" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LandingMinimaCategory", propOrder = {
    "isCatA",
    "isCatB",
    "isCatC",
    "isCatD",
    "isCatE",
    "isCatHeli",
    "isCatLarge",
    "categoryNa",
    "altitude",
    "height",
    "radioAltimeterHeight",
    "isDerived"
})
public class LandingMinimaCategory {

    @XmlElement(defaultValue = "false")
    protected Boolean isCatA;
    @XmlElement(defaultValue = "false")
    protected Boolean isCatB;
    @XmlElement(defaultValue = "false")
    protected Boolean isCatC;
    @XmlElement(defaultValue = "false")
    protected Boolean isCatD;
    @XmlElement(defaultValue = "false")
    protected Boolean isCatE;
    @XmlElement(defaultValue = "false")
    protected Boolean isCatHeli;
    @XmlElement(defaultValue = "false")
    protected Boolean isCatLarge;
    @XmlSchemaType(name = "string")
    protected NaType categoryNa;
    protected MinimaAltitude altitude;
    protected MinimaHeight height;
    protected RadioAltimeterHeight radioAltimeterHeight;
    @XmlElement(defaultValue = "false")
    protected Boolean isDerived;

    /**
     * Gets the value of the isCatA property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCatA() {
        return isCatA;
    }

    /**
     * Sets the value of the isCatA property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCatA(Boolean value) {
        this.isCatA = value;
    }

    /**
     * Gets the value of the isCatB property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCatB() {
        return isCatB;
    }

    /**
     * Sets the value of the isCatB property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCatB(Boolean value) {
        this.isCatB = value;
    }

    /**
     * Gets the value of the isCatC property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCatC() {
        return isCatC;
    }

    /**
     * Sets the value of the isCatC property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCatC(Boolean value) {
        this.isCatC = value;
    }

    /**
     * Gets the value of the isCatD property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCatD() {
        return isCatD;
    }

    /**
     * Sets the value of the isCatD property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCatD(Boolean value) {
        this.isCatD = value;
    }

    /**
     * Gets the value of the isCatE property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCatE() {
        return isCatE;
    }

    /**
     * Sets the value of the isCatE property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCatE(Boolean value) {
        this.isCatE = value;
    }

    /**
     * Gets the value of the isCatHeli property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCatHeli() {
        return isCatHeli;
    }

    /**
     * Sets the value of the isCatHeli property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCatHeli(Boolean value) {
        this.isCatHeli = value;
    }

    /**
     * Gets the value of the isCatLarge property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCatLarge() {
        return isCatLarge;
    }

    /**
     * Sets the value of the isCatLarge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCatLarge(Boolean value) {
        this.isCatLarge = value;
    }

    /**
     * Gets the value of the categoryNa property.
     * 
     * @return
     *     possible object is
     *     {@link NaType }
     *     
     */
    public NaType getCategoryNa() {
        return categoryNa;
    }

    /**
     * Sets the value of the categoryNa property.
     * 
     * @param value
     *     allowed object is
     *     {@link NaType }
     *     
     */
    public void setCategoryNa(NaType value) {
        this.categoryNa = value;
    }

    /**
     * Gets the value of the altitude property.
     * 
     * @return
     *     possible object is
     *     {@link MinimaAltitude }
     *     
     */
    public MinimaAltitude getAltitude() {
        return altitude;
    }

    /**
     * Sets the value of the altitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link MinimaAltitude }
     *     
     */
    public void setAltitude(MinimaAltitude value) {
        this.altitude = value;
    }

    /**
     * Gets the value of the height property.
     * 
     * @return
     *     possible object is
     *     {@link MinimaHeight }
     *     
     */
    public MinimaHeight getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     * @param value
     *     allowed object is
     *     {@link MinimaHeight }
     *     
     */
    public void setHeight(MinimaHeight value) {
        this.height = value;
    }

    /**
     * Gets the value of the radioAltimeterHeight property.
     * 
     * @return
     *     possible object is
     *     {@link RadioAltimeterHeight }
     *     
     */
    public RadioAltimeterHeight getRadioAltimeterHeight() {
        return radioAltimeterHeight;
    }

    /**
     * Sets the value of the radioAltimeterHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link RadioAltimeterHeight }
     *     
     */
    public void setRadioAltimeterHeight(RadioAltimeterHeight value) {
        this.radioAltimeterHeight = value;
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

}
