
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in VOR Records.
 * 
 * <p>Java class for Vor complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Vor"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Navaid"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="vorFrequency" type="{http://www.arinc424.com/xml/datatypes}Frequency"/&gt;
 *         &lt;element name="navaidClass" type="{http://www.arinc424.com/xml/datatypes}VhfNavaidClass"/&gt;
 *         &lt;element name="dmeTacanRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="stationDeclination" type="{http://www.arinc424.com/xml/datatypes}StationDeclination" minOccurs="0"/&gt;
 *         &lt;element name="figureOfMerit" type="{http://www.arinc424.com/xml/enumerations}FigureOfMerit" minOccurs="0"/&gt;
 *         &lt;element name="frequencyProtection" type="{http://www.arinc424.com/xml/datatypes}FrequencyProtectionDistance" minOccurs="0"/&gt;
 *         &lt;element name="navaidSynchronization" type="{http://www.arinc424.com/xml/enumerations}NavaidSynchronization" minOccurs="0"/&gt;
 *         &lt;element name="navaidLimitations" type="{http://www.arinc424.com/xml/datatypes}NavaidLimitation" minOccurs="0"/&gt;
 *         &lt;element name="vorVoice" type="{http://www.arinc424.com/xml/enumerations}NavaidVoice" minOccurs="0"/&gt;
 *         &lt;element name="fraInfo" type="{http://www.arinc424.com/xml/datatypes}FraInfo" minOccurs="0"/&gt;
 *         &lt;element name="vorRangePower" type="{http://www.arinc424.com/xml/enumerations}VorRangePower" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Vor", propOrder = {
    "vorFrequency",
    "navaidClass",
    "dmeTacanRef",
    "stationDeclination",
    "figureOfMerit",
    "frequencyProtection",
    "navaidSynchronization",
    "navaidLimitations",
    "vorVoice",
    "fraInfo",
    "vorRangePower"
})
public class Vor
    extends Navaid
{

    @XmlElement(required = true)
    protected Frequency vorFrequency;
    @XmlElement(required = true)
    protected VhfNavaidClass navaidClass;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object dmeTacanRef;
    protected StationDeclination stationDeclination;
    @XmlSchemaType(name = "string")
    protected FigureOfMerit figureOfMerit;
    @XmlSchemaType(name = "unsignedInt")
    protected Long frequencyProtection;
    @XmlSchemaType(name = "string")
    protected NavaidSynchronization navaidSynchronization;
    protected NavaidLimitation navaidLimitations;
    @XmlSchemaType(name = "string")
    protected NavaidVoice vorVoice;
    protected FraInfo fraInfo;
    @XmlSchemaType(name = "string")
    protected VorRangePower vorRangePower;

    /**
     * Gets the value of the vorFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Frequency }
     *     
     */
    public Frequency getVorFrequency() {
        return vorFrequency;
    }

    /**
     * Sets the value of the vorFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Frequency }
     *     
     */
    public void setVorFrequency(Frequency value) {
        this.vorFrequency = value;
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
     * Gets the value of the dmeTacanRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getDmeTacanRef() {
        return dmeTacanRef;
    }

    /**
     * Sets the value of the dmeTacanRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setDmeTacanRef(Object value) {
        this.dmeTacanRef = value;
    }

    /**
     * Gets the value of the stationDeclination property.
     * 
     * @return
     *     possible object is
     *     {@link StationDeclination }
     *     
     */
    public StationDeclination getStationDeclination() {
        return stationDeclination;
    }

    /**
     * Sets the value of the stationDeclination property.
     * 
     * @param value
     *     allowed object is
     *     {@link StationDeclination }
     *     
     */
    public void setStationDeclination(StationDeclination value) {
        this.stationDeclination = value;
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
     * Gets the value of the navaidSynchronization property.
     * 
     * @return
     *     possible object is
     *     {@link NavaidSynchronization }
     *     
     */
    public NavaidSynchronization getNavaidSynchronization() {
        return navaidSynchronization;
    }

    /**
     * Sets the value of the navaidSynchronization property.
     * 
     * @param value
     *     allowed object is
     *     {@link NavaidSynchronization }
     *     
     */
    public void setNavaidSynchronization(NavaidSynchronization value) {
        this.navaidSynchronization = value;
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
     * Gets the value of the vorVoice property.
     * 
     * @return
     *     possible object is
     *     {@link NavaidVoice }
     *     
     */
    public NavaidVoice getVorVoice() {
        return vorVoice;
    }

    /**
     * Sets the value of the vorVoice property.
     * 
     * @param value
     *     allowed object is
     *     {@link NavaidVoice }
     *     
     */
    public void setVorVoice(NavaidVoice value) {
        this.vorVoice = value;
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

    /**
     * Gets the value of the vorRangePower property.
     * 
     * @return
     *     possible object is
     *     {@link VorRangePower }
     *     
     */
    public VorRangePower getVorRangePower() {
        return vorRangePower;
    }

    /**
     * Sets the value of the vorRangePower property.
     * 
     * @param value
     *     allowed object is
     *     {@link VorRangePower }
     *     
     */
    public void setVorRangePower(VorRangePower value) {
        this.vorRangePower = value;
    }

}
