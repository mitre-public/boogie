
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DmeTacan complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DmeTacan"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Navaid"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="frequency" type="{http://www.arinc424.com/xml/datatypes}Frequency"/&gt;
 *         &lt;element name="navaidClass" type="{http://www.arinc424.com/xml/datatypes}VhfNavaidClass" minOccurs="0"/&gt;
 *         &lt;element name="dmeOperationalServiceVolume" type="{http://www.arinc424.com/xml/enumerations}DmeOperationalServiceVolume" minOccurs="0"/&gt;
 *         &lt;element name="dmeExpandedServiceVolume" type="{http://www.arinc424.com/xml/enumerations}DmeExpandedServiceVolume" minOccurs="0"/&gt;
 *         &lt;element name="isIlsComponent" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="ilsDmeLocation" type="{http://www.arinc424.com/xml/enumerations}IlsDmeLocation" minOccurs="0"/&gt;
 *         &lt;element name="ilsDmeBias" type="{http://www.arinc424.com/xml/datatypes}IlsDmeBias" minOccurs="0"/&gt;
 *         &lt;element name="isRouteInappropriateDme" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="figureOfMerit" type="{http://www.arinc424.com/xml/enumerations}FigureOfMerit" minOccurs="0"/&gt;
 *         &lt;element name="isPaired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isMlsP" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="frequencyProtection" type="{http://www.arinc424.com/xml/datatypes}FrequencyProtectionDistance" minOccurs="0"/&gt;
 *         &lt;element name="navaidLimitations" type="{http://www.arinc424.com/xml/datatypes}NavaidLimitation" minOccurs="0"/&gt;
 *         &lt;element name="fraInfo" type="{http://www.arinc424.com/xml/datatypes}FraInfo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DmeTacan", propOrder = {
    "frequency",
    "navaidClass",
    "dmeOperationalServiceVolume",
    "dmeExpandedServiceVolume",
    "isIlsComponent",
    "ilsDmeLocation",
    "ilsDmeBias",
    "isRouteInappropriateDme",
    "figureOfMerit",
    "isPaired",
    "isMlsP",
    "frequencyProtection",
    "navaidLimitations",
    "fraInfo"
})
@XmlSeeAlso({
    Dme.class,
    Tacan.class,
    MilitaryTacan.class
})
public abstract class DmeTacan
    extends Navaid
{

    @XmlElement(required = true)
    protected Frequency frequency;
    protected VhfNavaidClass navaidClass;
    protected String dmeOperationalServiceVolume;
    @XmlSchemaType(name = "string")
    protected DmeExpandedServiceVolume dmeExpandedServiceVolume;
    @XmlElement(defaultValue = "false")
    protected Boolean isIlsComponent;
    @XmlSchemaType(name = "string")
    protected IlsDmeLocation ilsDmeLocation;
    protected BigDecimal ilsDmeBias;
    @XmlElement(defaultValue = "false")
    protected Boolean isRouteInappropriateDme;
    @XmlSchemaType(name = "string")
    protected FigureOfMerit figureOfMerit;
    @XmlElement(defaultValue = "false")
    protected Boolean isPaired;
    @XmlElement(defaultValue = "false")
    protected Boolean isMlsP;
    @XmlSchemaType(name = "unsignedInt")
    protected Long frequencyProtection;
    protected NavaidLimitation navaidLimitations;
    protected FraInfo fraInfo;

    /**
     * Gets the value of the frequency property.
     * 
     * @return
     *     possible object is
     *     {@link Frequency }
     *     
     */
    public Frequency getFrequency() {
        return frequency;
    }

    /**
     * Sets the value of the frequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Frequency }
     *     
     */
    public void setFrequency(Frequency value) {
        this.frequency = value;
    }

    /**
     * Gets the value of the navaidClass property.
     * 
     * @return
     *     possible object is
     *     {@link VhfNavaidClass }
     *     
     */
    public VhfNavaidClass getNavaidClass() {
        return navaidClass;
    }

    /**
     * Sets the value of the navaidClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link VhfNavaidClass }
     *     
     */
    public void setNavaidClass(VhfNavaidClass value) {
        this.navaidClass = value;
    }

    /**
     * Gets the value of the dmeOperationalServiceVolume property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDmeOperationalServiceVolume() {
        return dmeOperationalServiceVolume;
    }

    /**
     * Sets the value of the dmeOperationalServiceVolume property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDmeOperationalServiceVolume(String value) {
        this.dmeOperationalServiceVolume = value;
    }

    /**
     * Gets the value of the dmeExpandedServiceVolume property.
     * 
     * @return
     *     possible object is
     *     {@link DmeExpandedServiceVolume }
     *     
     */
    public DmeExpandedServiceVolume getDmeExpandedServiceVolume() {
        return dmeExpandedServiceVolume;
    }

    /**
     * Sets the value of the dmeExpandedServiceVolume property.
     * 
     * @param value
     *     allowed object is
     *     {@link DmeExpandedServiceVolume }
     *     
     */
    public void setDmeExpandedServiceVolume(DmeExpandedServiceVolume value) {
        this.dmeExpandedServiceVolume = value;
    }

    /**
     * Gets the value of the isIlsComponent property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsIlsComponent() {
        return isIlsComponent;
    }

    /**
     * Sets the value of the isIlsComponent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsIlsComponent(Boolean value) {
        this.isIlsComponent = value;
    }

    /**
     * Gets the value of the ilsDmeLocation property.
     * 
     * @return
     *     possible object is
     *     {@link IlsDmeLocation }
     *     
     */
    public IlsDmeLocation getIlsDmeLocation() {
        return ilsDmeLocation;
    }

    /**
     * Sets the value of the ilsDmeLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link IlsDmeLocation }
     *     
     */
    public void setIlsDmeLocation(IlsDmeLocation value) {
        this.ilsDmeLocation = value;
    }

    /**
     * Gets the value of the ilsDmeBias property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIlsDmeBias() {
        return ilsDmeBias;
    }

    /**
     * Sets the value of the ilsDmeBias property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIlsDmeBias(BigDecimal value) {
        this.ilsDmeBias = value;
    }

    /**
     * Gets the value of the isRouteInappropriateDme property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRouteInappropriateDme() {
        return isRouteInappropriateDme;
    }

    /**
     * Sets the value of the isRouteInappropriateDme property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRouteInappropriateDme(Boolean value) {
        this.isRouteInappropriateDme = value;
    }

    /**
     * Gets the value of the figureOfMerit property.
     * 
     * @return
     *     possible object is
     *     {@link FigureOfMerit }
     *     
     */
    public FigureOfMerit getFigureOfMerit() {
        return figureOfMerit;
    }

    /**
     * Sets the value of the figureOfMerit property.
     * 
     * @param value
     *     allowed object is
     *     {@link FigureOfMerit }
     *     
     */
    public void setFigureOfMerit(FigureOfMerit value) {
        this.figureOfMerit = value;
    }

    /**
     * Gets the value of the isPaired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPaired() {
        return isPaired;
    }

    /**
     * Sets the value of the isPaired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPaired(Boolean value) {
        this.isPaired = value;
    }

    /**
     * Gets the value of the isMlsP property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMlsP() {
        return isMlsP;
    }

    /**
     * Sets the value of the isMlsP property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMlsP(Boolean value) {
        this.isMlsP = value;
    }

    /**
     * Gets the value of the frequencyProtection property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFrequencyProtection() {
        return frequencyProtection;
    }

    /**
     * Sets the value of the frequencyProtection property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFrequencyProtection(Long value) {
        this.frequencyProtection = value;
    }

    /**
     * Gets the value of the navaidLimitations property.
     * 
     * @return
     *     possible object is
     *     {@link NavaidLimitation }
     *     
     */
    public NavaidLimitation getNavaidLimitations() {
        return navaidLimitations;
    }

    /**
     * Sets the value of the navaidLimitations property.
     * 
     * @param value
     *     allowed object is
     *     {@link NavaidLimitation }
     *     
     */
    public void setNavaidLimitations(NavaidLimitation value) {
        this.navaidLimitations = value;
    }

    /**
     * Gets the value of the fraInfo property.
     * 
     * @return
     *     possible object is
     *     {@link FraInfo }
     *     
     */
    public FraInfo getFraInfo() {
        return fraInfo;
    }

    /**
     * Sets the value of the fraInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link FraInfo }
     *     
     */
    public void setFraInfo(FraInfo value) {
        this.fraInfo = value;
    }

}
