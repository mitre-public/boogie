
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigInteger;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This element contains all ATN Ground Facility logon codes and supporting ATN Network Service Access Point (NSAP) Address data for CPDLC system logon to ATSU where CPDLC is provided via ATN.
 * 
 * <p>Java class for AeroTelecomNetwork complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AeroTelecomNetwork"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Base"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="groundFacilityIdentifier" type="{http://www.arinc424.com/xml/datatypes}GroundFacilityIdentifier"/&gt;
 *         &lt;element name="authorityAndFormatIdentifier" type="{http://www.arinc424.com/xml/datatypes}AuthorityFormatIdentifier"/&gt;
 *         &lt;element name="initialDomainIdentifier" type="{http://www.arinc424.com/xml/datatypes}InitialDomainIdentifier"/&gt;
 *         &lt;element name="versionIdentifier" type="{http://www.arinc424.com/xml/datatypes}Version"/&gt;
 *         &lt;element name="administrativeIdentifier" type="{http://www.arinc424.com/xml/datatypes}Administration"/&gt;
 *         &lt;element name="routingDomainFormat" type="{http://www.arinc424.com/xml/datatypes}RoutingDomainFormat"/&gt;
 *         &lt;element name="administrativeRegionSelector" type="{http://www.arinc424.com/xml/datatypes}AdministrativeRegionSelector"/&gt;
 *         &lt;element name="locationIdentifier" type="{http://www.arinc424.com/xml/datatypes}AtnLocation"/&gt;
 *         &lt;element name="systemIdentifier" type="{http://www.arinc424.com/xml/datatypes}SystemIdentifier"/&gt;
 *         &lt;element name="nsapSelector" type="{http://www.arinc424.com/xml/datatypes}NetworkServiceAccessPointSelector"/&gt;
 *         &lt;element name="cmTransportSelector" type="{http://www.arinc424.com/xml/datatypes}ContextManagementTransportSelector"/&gt;
 *         &lt;element name="useIndicator" type="{http://www.arinc424.com/xml/enumerations}UseIndicator"/&gt;
 *         &lt;element name="firUirRef" type="{http://www.w3.org/2001/XMLSchema}IDREF"/&gt;
 *         &lt;element name="cycleDate" type="{http://www.arinc424.com/xml/datatypes}CycleDate"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AeroTelecomNetwork", propOrder = {
    "groundFacilityIdentifier",
    "authorityAndFormatIdentifier",
    "initialDomainIdentifier",
    "versionIdentifier",
    "administrativeIdentifier",
    "routingDomainFormat",
    "administrativeRegionSelector",
    "locationIdentifier",
    "systemIdentifier",
    "nsapSelector",
    "cmTransportSelector",
    "useIndicator",
    "firUirRef",
    "cycleDate"
})
public class AeroTelecomNetwork
    extends A424Base
{

    @XmlElement(required = true)
    protected String groundFacilityIdentifier;
    @XmlSchemaType(name = "integer")
    protected int authorityAndFormatIdentifier;
    @XmlElement(required = true)
    protected BigInteger initialDomainIdentifier;
    @XmlElement(required = true)
    protected String versionIdentifier;
    @XmlElement(required = true)
    protected String administrativeIdentifier;
    @XmlElement(required = true)
    protected String routingDomainFormat;
    @XmlElement(required = true)
    protected String administrativeRegionSelector;
    @XmlElement(required = true)
    protected String locationIdentifier;
    @XmlElement(required = true)
    protected String systemIdentifier;
    @XmlElement(required = true)
    protected String nsapSelector;
    @XmlElement(required = true)
    protected String cmTransportSelector;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected UseIndicator useIndicator;
    @XmlElement(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object firUirRef;
    @XmlElement(required = true)
    protected String cycleDate;

    /**
     * Gets the value of the groundFacilityIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroundFacilityIdentifier() {
        return groundFacilityIdentifier;
    }

    /**
     * Sets the value of the groundFacilityIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroundFacilityIdentifier(String value) {
        this.groundFacilityIdentifier = value;
    }

    /**
     * Gets the value of the authorityAndFormatIdentifier property.
     * 
     */
    public int getAuthorityAndFormatIdentifier() {
        return authorityAndFormatIdentifier;
    }

    /**
     * Sets the value of the authorityAndFormatIdentifier property.
     * 
     */
    public void setAuthorityAndFormatIdentifier(int value) {
        this.authorityAndFormatIdentifier = value;
    }

    /**
     * Gets the value of the initialDomainIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getInitialDomainIdentifier() {
        return initialDomainIdentifier;
    }

    /**
     * Sets the value of the initialDomainIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setInitialDomainIdentifier(BigInteger value) {
        this.initialDomainIdentifier = value;
    }

    /**
     * Gets the value of the versionIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionIdentifier() {
        return versionIdentifier;
    }

    /**
     * Sets the value of the versionIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionIdentifier(String value) {
        this.versionIdentifier = value;
    }

    /**
     * Gets the value of the administrativeIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdministrativeIdentifier() {
        return administrativeIdentifier;
    }

    /**
     * Sets the value of the administrativeIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdministrativeIdentifier(String value) {
        this.administrativeIdentifier = value;
    }

    /**
     * Gets the value of the routingDomainFormat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoutingDomainFormat() {
        return routingDomainFormat;
    }

    /**
     * Sets the value of the routingDomainFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoutingDomainFormat(String value) {
        this.routingDomainFormat = value;
    }

    /**
     * Gets the value of the administrativeRegionSelector property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdministrativeRegionSelector() {
        return administrativeRegionSelector;
    }

    /**
     * Sets the value of the administrativeRegionSelector property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdministrativeRegionSelector(String value) {
        this.administrativeRegionSelector = value;
    }

    /**
     * Gets the value of the locationIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationIdentifier() {
        return locationIdentifier;
    }

    /**
     * Sets the value of the locationIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationIdentifier(String value) {
        this.locationIdentifier = value;
    }

    /**
     * Gets the value of the systemIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemIdentifier() {
        return systemIdentifier;
    }

    /**
     * Sets the value of the systemIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemIdentifier(String value) {
        this.systemIdentifier = value;
    }

    /**
     * Gets the value of the nsapSelector property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNsapSelector() {
        return nsapSelector;
    }

    /**
     * Sets the value of the nsapSelector property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNsapSelector(String value) {
        this.nsapSelector = value;
    }

    /**
     * Gets the value of the cmTransportSelector property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmTransportSelector() {
        return cmTransportSelector;
    }

    /**
     * Sets the value of the cmTransportSelector property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmTransportSelector(String value) {
        this.cmTransportSelector = value;
    }

    /**
     * Gets the value of the useIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link UseIndicator }
     *     
     */
    public UseIndicator getUseIndicator() {
        return useIndicator;
    }

    /**
     * Sets the value of the useIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link UseIndicator }
     *     
     */
    public void setUseIndicator(UseIndicator value) {
        this.useIndicator = value;
    }

    /**
     * Gets the value of the firUirRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFirUirRef() {
        return firUirRef;
    }

    /**
     * Sets the value of the firUirRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFirUirRef(Object value) {
        this.firUirRef = value;
    }

    /**
     * Gets the value of the cycleDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCycleDate() {
        return cycleDate;
    }

    /**
     * Sets the value of the cycleDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCycleDate(String value) {
        this.cycleDate = value;
    }

}
