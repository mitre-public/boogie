
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This data type represents the company route.  It contains children starting with the letters via.  The company route is made up of references to other data. e.g., the company route is to proceed via a SID then via an enroute ...etc.
 * 
 * <p>Java class for CompanyRoute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CompanyRoute"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="alternateDistance" type="{http://www.arinc424.com/xml/datatypes}AlternateDistance" minOccurs="0"/&gt;
 *         &lt;element name="costIndex" type="{http://www.arinc424.com/xml/datatypes}CostIndex" minOccurs="0"/&gt;
 *         &lt;element name="cruiseAltitude" type="{http://www.arinc424.com/xml/datatypes}CruiseAltitude" minOccurs="0"/&gt;
 *         &lt;element name="enrouteEmergencyPortRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="fromPortFixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="identifier" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier"/&gt;
 *         &lt;element name="terminalAlternateAirport" type="{http://www.arinc424.com/xml/datatypes}TerminalAlternateAirport" minOccurs="0"/&gt;
 *         &lt;element name="toPortFix" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="viaSid" type="{}ViaSid" minOccurs="0"/&gt;
 *         &lt;element name="viaEnrouteLegs" type="{}ViaEnrouteLeg" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="viaStar" type="{}ViaStar" minOccurs="0"/&gt;
 *         &lt;element name="viaApproachConnectorLegs" type="{}ViaEnrouteLeg" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="viaApproach" type="{}ViaApproach" minOccurs="0"/&gt;
 *         &lt;element name="alternateRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="referenceId" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompanyRoute", propOrder = {
    "alternateDistance",
    "costIndex",
    "cruiseAltitude",
    "enrouteEmergencyPortRef",
    "fromPortFixRef",
    "identifier",
    "terminalAlternateAirport",
    "toPortFix",
    "viaSid",
    "viaEnrouteLegs",
    "viaStar",
    "viaApproachConnectorLegs",
    "viaApproach",
    "alternateRef"
})
public class CompanyRoute
    extends A424Record
{

    @XmlSchemaType(name = "unsignedInt")
    protected Long alternateDistance;
    @XmlSchemaType(name = "unsignedInt")
    protected Long costIndex;
    protected String cruiseAltitude;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object enrouteEmergencyPortRef;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object fromPortFixRef;
    @XmlElement(required = true)
    protected String identifier;
    protected String terminalAlternateAirport;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object toPortFix;
    protected ViaSid viaSid;
    protected List<ViaEnrouteLeg> viaEnrouteLegs;
    protected ViaStar viaStar;
    protected List<ViaEnrouteLeg> viaApproachConnectorLegs;
    protected ViaApproach viaApproach;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object alternateRef;
    @XmlAttribute(name = "referenceId", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String referenceId;

    /**
     * Gets the value of the alternateDistance property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAlternateDistance() {
        return alternateDistance;
    }

    /**
     * Sets the value of the alternateDistance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAlternateDistance(Long value) {
        this.alternateDistance = value;
    }

    /**
     * Gets the value of the costIndex property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCostIndex() {
        return costIndex;
    }

    /**
     * Sets the value of the costIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCostIndex(Long value) {
        this.costIndex = value;
    }

    /**
     * Gets the value of the cruiseAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCruiseAltitude() {
        return cruiseAltitude;
    }

    /**
     * Sets the value of the cruiseAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCruiseAltitude(String value) {
        this.cruiseAltitude = value;
    }

    /**
     * Gets the value of the enrouteEmergencyPortRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getEnrouteEmergencyPortRef() {
        return enrouteEmergencyPortRef;
    }

    /**
     * Sets the value of the enrouteEmergencyPortRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setEnrouteEmergencyPortRef(Object value) {
        this.enrouteEmergencyPortRef = value;
    }

    /**
     * Gets the value of the fromPortFixRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFromPortFixRef() {
        return fromPortFixRef;
    }

    /**
     * Sets the value of the fromPortFixRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFromPortFixRef(Object value) {
        this.fromPortFixRef = value;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the terminalAlternateAirport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminalAlternateAirport() {
        return terminalAlternateAirport;
    }

    /**
     * Sets the value of the terminalAlternateAirport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminalAlternateAirport(String value) {
        this.terminalAlternateAirport = value;
    }

    /**
     * Gets the value of the toPortFix property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getToPortFix() {
        return toPortFix;
    }

    /**
     * Sets the value of the toPortFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setToPortFix(Object value) {
        this.toPortFix = value;
    }

    /**
     * Gets the value of the viaSid property.
     * 
     * @return
     *     possible object is
     *     {@link ViaSid }
     *     
     */
    public ViaSid getViaSid() {
        return viaSid;
    }

    /**
     * Sets the value of the viaSid property.
     * 
     * @param value
     *     allowed object is
     *     {@link ViaSid }
     *     
     */
    public void setViaSid(ViaSid value) {
        this.viaSid = value;
    }

    /**
     * Gets the value of the viaEnrouteLegs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the viaEnrouteLegs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getViaEnrouteLegs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ViaEnrouteLeg }
     * 
     * 
     */
    public List<ViaEnrouteLeg> getViaEnrouteLegs() {
        if (viaEnrouteLegs == null) {
            viaEnrouteLegs = new ArrayList<ViaEnrouteLeg>();
        }
        return this.viaEnrouteLegs;
    }

    /**
     * Gets the value of the viaStar property.
     * 
     * @return
     *     possible object is
     *     {@link ViaStar }
     *     
     */
    public ViaStar getViaStar() {
        return viaStar;
    }

    /**
     * Sets the value of the viaStar property.
     * 
     * @param value
     *     allowed object is
     *     {@link ViaStar }
     *     
     */
    public void setViaStar(ViaStar value) {
        this.viaStar = value;
    }

    /**
     * Gets the value of the viaApproachConnectorLegs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the viaApproachConnectorLegs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getViaApproachConnectorLegs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ViaEnrouteLeg }
     * 
     * 
     */
    public List<ViaEnrouteLeg> getViaApproachConnectorLegs() {
        if (viaApproachConnectorLegs == null) {
            viaApproachConnectorLegs = new ArrayList<ViaEnrouteLeg>();
        }
        return this.viaApproachConnectorLegs;
    }

    /**
     * Gets the value of the viaApproach property.
     * 
     * @return
     *     possible object is
     *     {@link ViaApproach }
     *     
     */
    public ViaApproach getViaApproach() {
        return viaApproach;
    }

    /**
     * Sets the value of the viaApproach property.
     * 
     * @param value
     *     allowed object is
     *     {@link ViaApproach }
     *     
     */
    public void setViaApproach(ViaApproach value) {
        this.viaApproach = value;
    }

    /**
     * Gets the value of the alternateRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getAlternateRef() {
        return alternateRef;
    }

    /**
     * Sets the value of the alternateRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setAlternateRef(Object value) {
        this.alternateRef = value;
    }

    /**
     * Gets the value of the referenceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceId() {
        return referenceId;
    }

    /**
     * Sets the value of the referenceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceId(String value) {
        this.referenceId = value;
    }

}
