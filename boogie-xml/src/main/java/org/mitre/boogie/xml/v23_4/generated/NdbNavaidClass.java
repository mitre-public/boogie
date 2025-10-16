
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NdbNavaidClass complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NdbNavaidClass"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isBfoRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="ndbNavaidCoverage" type="{http://www.arinc424.com/xml/enumerations}NdbNavaidCoverage" minOccurs="0"/&gt;
 *         &lt;element name="ndbNavaidIfMarker" type="{http://www.arinc424.com/xml/enumerations}NdbNavaidIfMarkerInfo" minOccurs="0"/&gt;
 *         &lt;element name="ndbNavaidType" type="{http://www.arinc424.com/xml/enumerations}NdbNavaidType"/&gt;
 *         &lt;element name="ndbNavaidWeatherInfo" type="{http://www.arinc424.com/xml/enumerations}NavaidWeatherInfo" minOccurs="0"/&gt;
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
@XmlType(name = "NdbNavaidClass", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "isBfoRequired",
    "ndbNavaidCoverage",
    "ndbNavaidIfMarker",
    "ndbNavaidType",
    "ndbNavaidWeatherInfo",
    "isNoVoice"
})
public class NdbNavaidClass {

    @XmlElement(defaultValue = "false")
    protected Boolean isBfoRequired;
    @XmlSchemaType(name = "string")
    protected NdbNavaidCoverage ndbNavaidCoverage;
    @XmlSchemaType(name = "string")
    protected NdbNavaidIfMarkerInfo ndbNavaidIfMarker;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected NdbNavaidType ndbNavaidType;
    @XmlSchemaType(name = "string")
    protected NavaidWeatherInfo ndbNavaidWeatherInfo;
    @XmlElement(defaultValue = "false")
    protected Boolean isNoVoice;

    /**
     * Gets the value of the isBfoRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsBfoRequired() {
        return isBfoRequired;
    }

    /**
     * Sets the value of the isBfoRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsBfoRequired(Boolean value) {
        this.isBfoRequired = value;
    }

    /**
     * Gets the value of the ndbNavaidCoverage property.
     * 
     * @return
     *     possible object is
     *     {@link NdbNavaidCoverage }
     *     
     */
    public NdbNavaidCoverage getNdbNavaidCoverage() {
        return ndbNavaidCoverage;
    }

    /**
     * Sets the value of the ndbNavaidCoverage property.
     * 
     * @param value
     *     allowed object is
     *     {@link NdbNavaidCoverage }
     *     
     */
    public void setNdbNavaidCoverage(NdbNavaidCoverage value) {
        this.ndbNavaidCoverage = value;
    }

    /**
     * Gets the value of the ndbNavaidIfMarker property.
     * 
     * @return
     *     possible object is
     *     {@link NdbNavaidIfMarkerInfo }
     *     
     */
    public NdbNavaidIfMarkerInfo getNdbNavaidIfMarker() {
        return ndbNavaidIfMarker;
    }

    /**
     * Sets the value of the ndbNavaidIfMarker property.
     * 
     * @param value
     *     allowed object is
     *     {@link NdbNavaidIfMarkerInfo }
     *     
     */
    public void setNdbNavaidIfMarker(NdbNavaidIfMarkerInfo value) {
        this.ndbNavaidIfMarker = value;
    }

    /**
     * Gets the value of the ndbNavaidType property.
     * 
     * @return
     *     possible object is
     *     {@link NdbNavaidType }
     *     
     */
    public NdbNavaidType getNdbNavaidType() {
        return ndbNavaidType;
    }

    /**
     * Sets the value of the ndbNavaidType property.
     * 
     * @param value
     *     allowed object is
     *     {@link NdbNavaidType }
     *     
     */
    public void setNdbNavaidType(NdbNavaidType value) {
        this.ndbNavaidType = value;
    }

    /**
     * Gets the value of the ndbNavaidWeatherInfo property.
     * 
     * @return
     *     possible object is
     *     {@link NavaidWeatherInfo }
     *     
     */
    public NavaidWeatherInfo getNdbNavaidWeatherInfo() {
        return ndbNavaidWeatherInfo;
    }

    /**
     * Sets the value of the ndbNavaidWeatherInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link NavaidWeatherInfo }
     *     
     */
    public void setNdbNavaidWeatherInfo(NavaidWeatherInfo value) {
        this.ndbNavaidWeatherInfo = value;
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
