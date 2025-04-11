
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The “Navaid Class” field provides information in coded format on the type of navaid, the coverage of the navaid, information carried on the navaid signal and collocation of navaids in both an electronic and aeronautical sense. The field is made up of five columns of codes that define this information.
 * 
 * <p>Java class for VhfNavaidClass complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VhfNavaidClass"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="vhfNavaidCoverage" type="{http://www.arinc424.com/xml/enumerations}VhfNavaidCoverage" minOccurs="0"/&gt;
 *         &lt;element name="vhfNavaidWeatherInfo" type="{http://www.arinc424.com/xml/enumerations}NavaidWeatherInfo" minOccurs="0"/&gt;
 *         &lt;element name="isNotCollocated" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isBiased" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isNoVoice" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VhfNavaidClass", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "vhfNavaidCoverage",
    "vhfNavaidWeatherInfo",
    "isNotCollocated",
    "isBiased",
    "isNoVoice"
})
public class VhfNavaidClass {

    @XmlSchemaType(name = "string")
    protected VhfNavaidCoverage vhfNavaidCoverage;
    @XmlSchemaType(name = "string")
    protected NavaidWeatherInfo vhfNavaidWeatherInfo;
    @XmlElement(defaultValue = "false")
    protected Boolean isNotCollocated;
    @XmlElement(defaultValue = "false")
    protected Boolean isBiased;
    @XmlElement(defaultValue = "false")
    protected Boolean isNoVoice;

    /**
     * Gets the value of the vhfNavaidCoverage property.
     * 
     * @return
     *     possible object is
     *     {@link VhfNavaidCoverage }
     *     
     */
    public VhfNavaidCoverage getVhfNavaidCoverage() {
        return vhfNavaidCoverage;
    }

    /**
     * Sets the value of the vhfNavaidCoverage property.
     * 
     * @param value
     *     allowed object is
     *     {@link VhfNavaidCoverage }
     *     
     */
    public void setVhfNavaidCoverage(VhfNavaidCoverage value) {
        this.vhfNavaidCoverage = value;
    }

    /**
     * Gets the value of the vhfNavaidWeatherInfo property.
     * 
     * @return
     *     possible object is
     *     {@link NavaidWeatherInfo }
     *     
     */
    public NavaidWeatherInfo getVhfNavaidWeatherInfo() {
        return vhfNavaidWeatherInfo;
    }

    /**
     * Sets the value of the vhfNavaidWeatherInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link NavaidWeatherInfo }
     *     
     */
    public void setVhfNavaidWeatherInfo(NavaidWeatherInfo value) {
        this.vhfNavaidWeatherInfo = value;
    }

    /**
     * Gets the value of the isNotCollocated property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNotCollocated() {
        return isNotCollocated;
    }

    /**
     * Sets the value of the isNotCollocated property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNotCollocated(Boolean value) {
        this.isNotCollocated = value;
    }

    /**
     * Gets the value of the isBiased property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsBiased() {
        return isBiased;
    }

    /**
     * Sets the value of the isBiased property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsBiased(Boolean value) {
        this.isBiased = value;
    }

    /**
     * Gets the value of the isNoVoice property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNoVoice() {
        return isNoVoice;
    }

    /**
     * Sets the value of the isNoVoice property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNoVoice(Boolean value) {
        this.isNoVoice = value;
    }

}
