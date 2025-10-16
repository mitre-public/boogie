
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This record type contains only a reference to a Cruising Table Identifier. That Cruise Table will be in force, replacing the Cruise Table Identifier in the Enroute Airway segment records defined in the Start Fix/End Fix fields.
 * 
 * <p>Java class for EnrouteAirwaysRestrictionCruisingTableReplacement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EnrouteAirwaysRestrictionCruisingTableReplacement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}EnrouteAirwaysRestriction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cruiseTableRef" type="{http://www.w3.org/2001/XMLSchema}IDREF"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnrouteAirwaysRestrictionCruisingTableReplacement", propOrder = {
    "cruiseTableRef"
})
public class EnrouteAirwaysRestrictionCruisingTableReplacement
    extends EnrouteAirwaysRestriction
{

    @XmlElement(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object cruiseTableRef;

    /**
     * Gets the value of the cruiseTableRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getCruiseTableRef() {
        return cruiseTableRef;
    }

    /**
     * Sets the value of the cruiseTableRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setCruiseTableRef(Object value) {
        this.cruiseTableRef = value;
    }

}
