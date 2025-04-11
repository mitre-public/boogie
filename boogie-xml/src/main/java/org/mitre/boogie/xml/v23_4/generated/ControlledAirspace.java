
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
 * This following record contains the fields used in Controlled Airspace Records.
 * 
 * <p>Java class for ControlledAirspace complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ControlledAirspace"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Airspace"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="airspaceClassification" type="{http://www.arinc424.com/xml/datatypes}AirspaceClassification" minOccurs="0"/&gt;
 *         &lt;element name="controlledAirspaceCenterRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="controlledAirspaceType" type="{http://www.arinc424.com/xml/enumerations}ControlledAirspaceType"/&gt;
 *         &lt;element name="rnp" type="{http://www.arinc424.com/xml/datatypes}RequiredNavigationPerformance" minOccurs="0"/&gt;
 *         &lt;element name="airspaceSpeedRestriction" type="{http://www.arinc424.com/xml/datatypes}AirspaceSpeedRestriction" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ControlledAirspace", propOrder = {
    "airspaceClassification",
    "controlledAirspaceCenterRef",
    "controlledAirspaceType",
    "rnp",
    "airspaceSpeedRestriction"
})
public class ControlledAirspace
    extends Airspace
{

    protected String airspaceClassification;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object controlledAirspaceCenterRef;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected ControlledAirspaceType controlledAirspaceType;
    protected BigDecimal rnp;
    protected List<AirspaceSpeedRestriction> airspaceSpeedRestriction;

    /**
     * Gets the value of the airspaceClassification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAirspaceClassification() {
        return airspaceClassification;
    }

    /**
     * Sets the value of the airspaceClassification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAirspaceClassification(String value) {
        this.airspaceClassification = value;
    }

    /**
     * Gets the value of the controlledAirspaceCenterRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getControlledAirspaceCenterRef() {
        return controlledAirspaceCenterRef;
    }

    /**
     * Sets the value of the controlledAirspaceCenterRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setControlledAirspaceCenterRef(Object value) {
        this.controlledAirspaceCenterRef = value;
    }

    /**
     * Gets the value of the controlledAirspaceType property.
     * 
     * @return
     *     possible object is
     *     {@link ControlledAirspaceType }
     *     
     */
    public ControlledAirspaceType getControlledAirspaceType() {
        return controlledAirspaceType;
    }

    /**
     * Sets the value of the controlledAirspaceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlledAirspaceType }
     *     
     */
    public void setControlledAirspaceType(ControlledAirspaceType value) {
        this.controlledAirspaceType = value;
    }

    /**
     * Gets the value of the rnp property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRnp() {
        return rnp;
    }

    /**
     * Sets the value of the rnp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRnp(BigDecimal value) {
        this.rnp = value;
    }

    /**
     * Gets the value of the airspaceSpeedRestriction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the airspaceSpeedRestriction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAirspaceSpeedRestriction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AirspaceSpeedRestriction }
     * 
     * 
     */
    public List<AirspaceSpeedRestriction> getAirspaceSpeedRestriction() {
        if (airspaceSpeedRestriction == null) {
            airspaceSpeedRestriction = new ArrayList<AirspaceSpeedRestriction>();
        }
        return this.airspaceSpeedRestriction;
    }

}
