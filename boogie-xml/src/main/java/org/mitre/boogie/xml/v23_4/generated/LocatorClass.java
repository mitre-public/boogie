
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LocatorClass complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LocatorClass"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ahLocalizerMarkerLocatorFacility1" type="{http://www.arinc424.com/xml/enumerations}AhLocalizerMarkerLocatorFacility1" minOccurs="0"/&gt;
 *         &lt;element name="ahLocalizerMarkerLocatorFacility2" type="{http://www.arinc424.com/xml/enumerations}AhLocalizerMarkerLocatorFacility2" minOccurs="0"/&gt;
 *         &lt;element name="ahLocalizerMarkerLocatorCoverage" type="{http://www.arinc424.com/xml/enumerations}AhLocalizerMarkerLocatorCoverage" minOccurs="0"/&gt;
 *         &lt;element name="ahLocalizerMarkerLocatorAddInfo" type="{http://www.arinc424.com/xml/enumerations}AhLocalizerMarkerLocatorAddInfo" minOccurs="0"/&gt;
 *         &lt;element name="ahLocalizerMarkerWeatherInfo" type="{http://www.arinc424.com/xml/enumerations}NavaidWeatherInfo" minOccurs="0"/&gt;
 *         &lt;element name="ahLocalizerMarkerLocatorCollocation" type="{http://www.arinc424.com/xml/enumerations}AhLocalizerMarkerLocatorCollocation" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LocatorClass", propOrder = {
    "ahLocalizerMarkerLocatorFacility1",
    "ahLocalizerMarkerLocatorFacility2",
    "ahLocalizerMarkerLocatorCoverage",
    "ahLocalizerMarkerLocatorAddInfo",
    "ahLocalizerMarkerWeatherInfo",
    "ahLocalizerMarkerLocatorCollocation"
})
public class LocatorClass {

    @XmlSchemaType(name = "string")
    protected AhLocalizerMarkerLocatorFacility1 ahLocalizerMarkerLocatorFacility1;
    @XmlSchemaType(name = "string")
    protected AhLocalizerMarkerLocatorFacility2 ahLocalizerMarkerLocatorFacility2;
    @XmlSchemaType(name = "string")
    protected AhLocalizerMarkerLocatorCoverage ahLocalizerMarkerLocatorCoverage;
    @XmlSchemaType(name = "string")
    protected AhLocalizerMarkerLocatorAddInfo ahLocalizerMarkerLocatorAddInfo;
    @XmlSchemaType(name = "string")
    protected NavaidWeatherInfo ahLocalizerMarkerWeatherInfo;
    @XmlSchemaType(name = "string")
    protected AhLocalizerMarkerLocatorCollocation ahLocalizerMarkerLocatorCollocation;

    /**
     * Gets the value of the ahLocalizerMarkerLocatorFacility1 property.
     * 
     * @return
     *     possible object is
     *     {@link AhLocalizerMarkerLocatorFacility1 }
     *     
     */
    public AhLocalizerMarkerLocatorFacility1 getAhLocalizerMarkerLocatorFacility1() {
        return ahLocalizerMarkerLocatorFacility1;
    }

    /**
     * Sets the value of the ahLocalizerMarkerLocatorFacility1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link AhLocalizerMarkerLocatorFacility1 }
     *     
     */
    public void setAhLocalizerMarkerLocatorFacility1(AhLocalizerMarkerLocatorFacility1 value) {
        this.ahLocalizerMarkerLocatorFacility1 = value;
    }

    /**
     * Gets the value of the ahLocalizerMarkerLocatorFacility2 property.
     * 
     * @return
     *     possible object is
     *     {@link AhLocalizerMarkerLocatorFacility2 }
     *     
     */
    public AhLocalizerMarkerLocatorFacility2 getAhLocalizerMarkerLocatorFacility2() {
        return ahLocalizerMarkerLocatorFacility2;
    }

    /**
     * Sets the value of the ahLocalizerMarkerLocatorFacility2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link AhLocalizerMarkerLocatorFacility2 }
     *     
     */
    public void setAhLocalizerMarkerLocatorFacility2(AhLocalizerMarkerLocatorFacility2 value) {
        this.ahLocalizerMarkerLocatorFacility2 = value;
    }

    /**
     * Gets the value of the ahLocalizerMarkerLocatorCoverage property.
     * 
     * @return
     *     possible object is
     *     {@link AhLocalizerMarkerLocatorCoverage }
     *     
     */
    public AhLocalizerMarkerLocatorCoverage getAhLocalizerMarkerLocatorCoverage() {
        return ahLocalizerMarkerLocatorCoverage;
    }

    /**
     * Sets the value of the ahLocalizerMarkerLocatorCoverage property.
     * 
     * @param value
     *     allowed object is
     *     {@link AhLocalizerMarkerLocatorCoverage }
     *     
     */
    public void setAhLocalizerMarkerLocatorCoverage(AhLocalizerMarkerLocatorCoverage value) {
        this.ahLocalizerMarkerLocatorCoverage = value;
    }

    /**
     * Gets the value of the ahLocalizerMarkerLocatorAddInfo property.
     * 
     * @return
     *     possible object is
     *     {@link AhLocalizerMarkerLocatorAddInfo }
     *     
     */
    public AhLocalizerMarkerLocatorAddInfo getAhLocalizerMarkerLocatorAddInfo() {
        return ahLocalizerMarkerLocatorAddInfo;
    }

    /**
     * Sets the value of the ahLocalizerMarkerLocatorAddInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link AhLocalizerMarkerLocatorAddInfo }
     *     
     */
    public void setAhLocalizerMarkerLocatorAddInfo(AhLocalizerMarkerLocatorAddInfo value) {
        this.ahLocalizerMarkerLocatorAddInfo = value;
    }

    /**
     * Gets the value of the ahLocalizerMarkerWeatherInfo property.
     * 
     * @return
     *     possible object is
     *     {@link NavaidWeatherInfo }
     *     
     */
    public NavaidWeatherInfo getAhLocalizerMarkerWeatherInfo() {
        return ahLocalizerMarkerWeatherInfo;
    }

    /**
     * Sets the value of the ahLocalizerMarkerWeatherInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link NavaidWeatherInfo }
     *     
     */
    public void setAhLocalizerMarkerWeatherInfo(NavaidWeatherInfo value) {
        this.ahLocalizerMarkerWeatherInfo = value;
    }

    /**
     * Gets the value of the ahLocalizerMarkerLocatorCollocation property.
     * 
     * @return
     *     possible object is
     *     {@link AhLocalizerMarkerLocatorCollocation }
     *     
     */
    public AhLocalizerMarkerLocatorCollocation getAhLocalizerMarkerLocatorCollocation() {
        return ahLocalizerMarkerLocatorCollocation;
    }

    /**
     * Sets the value of the ahLocalizerMarkerLocatorCollocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link AhLocalizerMarkerLocatorCollocation }
     *     
     */
    public void setAhLocalizerMarkerLocatorCollocation(AhLocalizerMarkerLocatorCollocation value) {
        this.ahLocalizerMarkerLocatorCollocation = value;
    }

}
