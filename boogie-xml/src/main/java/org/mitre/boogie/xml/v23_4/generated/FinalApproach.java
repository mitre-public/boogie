
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class is an abstraction of Final Transition.
 * 
 * <p>Java class for FinalApproach complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FinalApproach"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}ApproachRoute"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="fasBlockProvided" type="{http://www.arinc424.com/xml/enumerations}LevelOfServiceAuthorized" minOccurs="0"/&gt;
 *         &lt;element name="fasBlockProvidedLevelOfServiceName" type="{http://www.arinc424.com/xml/enumerations}LevelOfServiceName" minOccurs="0"/&gt;
 *         &lt;element name="isRemoteAltimeterRestricted" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="lnavAuthorizedForSbas" type="{http://www.arinc424.com/xml/enumerations}LevelOfServiceAuthorized" minOccurs="0"/&gt;
 *         &lt;element name="lnavLevelOfServiceName" type="{http://www.arinc424.com/xml/enumerations}LevelOfServiceName" minOccurs="0"/&gt;
 *         &lt;element name="lnavVnavAuthorizedForSbas" type="{http://www.arinc424.com/xml/enumerations}LevelOfServiceAuthorized" minOccurs="0"/&gt;
 *         &lt;element name="lnavVnavLevelOfServiceName" type="{http://www.arinc424.com/xml/enumerations}LevelOfServiceName" minOccurs="0"/&gt;
 *         &lt;element name="procedureTch" type="{http://www.arinc424.com/xml/datatypes}ThresholdCrossingHeight" minOccurs="0"/&gt;
 *         &lt;element name="rnpLos" type="{http://www.arinc424.com/xml/datatypes}RnpLevelOfService" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="glideSlopeInterceptAltitude" type="{http://www.arinc424.com/xml/datatypes}AltitudeValue" minOccurs="0"/&gt;
 *         &lt;element name="baroVnavNotAuthorized" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="taaRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FinalApproach", propOrder = {
    "fasBlockProvided",
    "fasBlockProvidedLevelOfServiceName",
    "isRemoteAltimeterRestricted",
    "lnavAuthorizedForSbas",
    "lnavLevelOfServiceName",
    "lnavVnavAuthorizedForSbas",
    "lnavVnavLevelOfServiceName",
    "procedureTch",
    "rnpLos",
    "glideSlopeInterceptAltitude",
    "baroVnavNotAuthorized",
    "taaRef"
})
public class FinalApproach
    extends ApproachRoute
{

    @XmlSchemaType(name = "string")
    protected LevelOfServiceAuthorized fasBlockProvided;
    @XmlSchemaType(name = "string")
    protected LevelOfServiceName fasBlockProvidedLevelOfServiceName;
    @XmlElement(defaultValue = "false")
    protected Boolean isRemoteAltimeterRestricted;
    @XmlSchemaType(name = "string")
    protected LevelOfServiceAuthorized lnavAuthorizedForSbas;
    @XmlSchemaType(name = "string")
    protected LevelOfServiceName lnavLevelOfServiceName;
    @XmlSchemaType(name = "string")
    protected LevelOfServiceAuthorized lnavVnavAuthorizedForSbas;
    @XmlSchemaType(name = "string")
    protected LevelOfServiceName lnavVnavLevelOfServiceName;
    @XmlSchemaType(name = "unsignedInt")
    protected Long procedureTch;
    protected List<RnpLevelOfService> rnpLos;
    @XmlSchemaType(name = "integer")
    protected Integer glideSlopeInterceptAltitude;
    @XmlElement(defaultValue = "false")
    protected Boolean baroVnavNotAuthorized;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object taaRef;

    /**
     * Gets the value of the fasBlockProvided property.
     * 
     * @return
     *     possible object is
     *     {@link LevelOfServiceAuthorized }
     *     
     */
    public LevelOfServiceAuthorized getFasBlockProvided() {
        return fasBlockProvided;
    }

    /**
     * Sets the value of the fasBlockProvided property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevelOfServiceAuthorized }
     *     
     */
    public void setFasBlockProvided(LevelOfServiceAuthorized value) {
        this.fasBlockProvided = value;
    }

    /**
     * Gets the value of the fasBlockProvidedLevelOfServiceName property.
     * 
     * @return
     *     possible object is
     *     {@link LevelOfServiceName }
     *     
     */
    public LevelOfServiceName getFasBlockProvidedLevelOfServiceName() {
        return fasBlockProvidedLevelOfServiceName;
    }

    /**
     * Sets the value of the fasBlockProvidedLevelOfServiceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevelOfServiceName }
     *     
     */
    public void setFasBlockProvidedLevelOfServiceName(LevelOfServiceName value) {
        this.fasBlockProvidedLevelOfServiceName = value;
    }

    /**
     * Gets the value of the isRemoteAltimeterRestricted property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRemoteAltimeterRestricted() {
        return isRemoteAltimeterRestricted;
    }

    /**
     * Sets the value of the isRemoteAltimeterRestricted property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRemoteAltimeterRestricted(Boolean value) {
        this.isRemoteAltimeterRestricted = value;
    }

    /**
     * Gets the value of the lnavAuthorizedForSbas property.
     * 
     * @return
     *     possible object is
     *     {@link LevelOfServiceAuthorized }
     *     
     */
    public LevelOfServiceAuthorized getLnavAuthorizedForSbas() {
        return lnavAuthorizedForSbas;
    }

    /**
     * Sets the value of the lnavAuthorizedForSbas property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevelOfServiceAuthorized }
     *     
     */
    public void setLnavAuthorizedForSbas(LevelOfServiceAuthorized value) {
        this.lnavAuthorizedForSbas = value;
    }

    /**
     * Gets the value of the lnavLevelOfServiceName property.
     * 
     * @return
     *     possible object is
     *     {@link LevelOfServiceName }
     *     
     */
    public LevelOfServiceName getLnavLevelOfServiceName() {
        return lnavLevelOfServiceName;
    }

    /**
     * Sets the value of the lnavLevelOfServiceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevelOfServiceName }
     *     
     */
    public void setLnavLevelOfServiceName(LevelOfServiceName value) {
        this.lnavLevelOfServiceName = value;
    }

    /**
     * Gets the value of the lnavVnavAuthorizedForSbas property.
     * 
     * @return
     *     possible object is
     *     {@link LevelOfServiceAuthorized }
     *     
     */
    public LevelOfServiceAuthorized getLnavVnavAuthorizedForSbas() {
        return lnavVnavAuthorizedForSbas;
    }

    /**
     * Sets the value of the lnavVnavAuthorizedForSbas property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevelOfServiceAuthorized }
     *     
     */
    public void setLnavVnavAuthorizedForSbas(LevelOfServiceAuthorized value) {
        this.lnavVnavAuthorizedForSbas = value;
    }

    /**
     * Gets the value of the lnavVnavLevelOfServiceName property.
     * 
     * @return
     *     possible object is
     *     {@link LevelOfServiceName }
     *     
     */
    public LevelOfServiceName getLnavVnavLevelOfServiceName() {
        return lnavVnavLevelOfServiceName;
    }

    /**
     * Sets the value of the lnavVnavLevelOfServiceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevelOfServiceName }
     *     
     */
    public void setLnavVnavLevelOfServiceName(LevelOfServiceName value) {
        this.lnavVnavLevelOfServiceName = value;
    }

    /**
     * Gets the value of the procedureTch property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getProcedureTch() {
        return procedureTch;
    }

    /**
     * Sets the value of the procedureTch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setProcedureTch(Long value) {
        this.procedureTch = value;
    }

    /**
     * Gets the value of the rnpLos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the rnpLos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRnpLos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RnpLevelOfService }
     * 
     * 
     */
    public List<RnpLevelOfService> getRnpLos() {
        if (rnpLos == null) {
            rnpLos = new ArrayList<RnpLevelOfService>();
        }
        return this.rnpLos;
    }

    /**
     * Gets the value of the glideSlopeInterceptAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getGlideSlopeInterceptAltitude() {
        return glideSlopeInterceptAltitude;
    }

    /**
     * Sets the value of the glideSlopeInterceptAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setGlideSlopeInterceptAltitude(Integer value) {
        this.glideSlopeInterceptAltitude = value;
    }

    /**
     * Gets the value of the baroVnavNotAuthorized property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBaroVnavNotAuthorized() {
        return baroVnavNotAuthorized;
    }

    /**
     * Sets the value of the baroVnavNotAuthorized property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBaroVnavNotAuthorized(Boolean value) {
        this.baroVnavNotAuthorized = value;
    }

    /**
     * Gets the value of the taaRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getTaaRef() {
        return taaRef;
    }

    /**
     * Sets the value of the taaRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setTaaRef(Object value) {
        this.taaRef = value;
    }

}
