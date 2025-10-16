
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class represents a Transition. This is the base class for other Transitions for inheriting the attributes of this class.
 * 
 * <p>Java class for ProcedureRoute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProcedureRoute"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Route"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="transitionAltitudeOrLevel" type="{http://www.arinc424.com/xml/datatypes}Constraint" minOccurs="0"/&gt;
 *         &lt;element name="msaRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/&gt;
 *         &lt;element name="isAtcAssignedOnly" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="procedureDesignAircraftCategory" type="{http://www.arinc424.com/xml/datatypes}ProcedureDesignAircraftCategories" minOccurs="0"/&gt;
 *         &lt;element name="procedureDesignAircraftType" type="{http://www.arinc424.com/xml/datatypes}ProcedureDesignAircraftTypes" minOccurs="0"/&gt;
 *         &lt;element name="procedureLeg" type="{}ProcedureLeg" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcedureRoute", propOrder = {
    "transitionAltitudeOrLevel",
    "msaRef",
    "isAtcAssignedOnly",
    "procedureDesignAircraftCategory",
    "procedureDesignAircraftType",
    "procedureLeg"
})
@XmlSeeAlso({
    RunwayTransition.class,
    CommonRoute.class,
    EnrouteTransition.class,
    ApproachRoute.class
})
public abstract class ProcedureRoute
    extends Route
{

    protected Constraint transitionAltitudeOrLevel;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object msaRef;
    @XmlElement(defaultValue = "false")
    protected Boolean isAtcAssignedOnly;
    protected ProcedureDesignAircraftCategories procedureDesignAircraftCategory;
    protected ProcedureDesignAircraftTypes procedureDesignAircraftType;
    @XmlElement(required = true)
    protected List<ProcedureLeg> procedureLeg;

    /**
     * Gets the value of the transitionAltitudeOrLevel property.
     * 
     * @return
     *     possible object is
     *     {@link Constraint }
     *     
     */
    public Constraint getTransitionAltitudeOrLevel() {
        return transitionAltitudeOrLevel;
    }

    /**
     * Sets the value of the transitionAltitudeOrLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Constraint }
     *     
     */
    public void setTransitionAltitudeOrLevel(Constraint value) {
        this.transitionAltitudeOrLevel = value;
    }

    /**
     * Gets the value of the msaRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getMsaRef() {
        return msaRef;
    }

    /**
     * Sets the value of the msaRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setMsaRef(Object value) {
        this.msaRef = value;
    }

    /**
     * Gets the value of the isAtcAssignedOnly property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAtcAssignedOnly() {
        return isAtcAssignedOnly;
    }

    /**
     * Sets the value of the isAtcAssignedOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAtcAssignedOnly(Boolean value) {
        this.isAtcAssignedOnly = value;
    }

    /**
     * Gets the value of the procedureDesignAircraftCategory property.
     * 
     * @return
     *     possible object is
     *     {@link ProcedureDesignAircraftCategories }
     *     
     */
    public ProcedureDesignAircraftCategories getProcedureDesignAircraftCategory() {
        return procedureDesignAircraftCategory;
    }

    /**
     * Sets the value of the procedureDesignAircraftCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedureDesignAircraftCategories }
     *     
     */
    public void setProcedureDesignAircraftCategory(ProcedureDesignAircraftCategories value) {
        this.procedureDesignAircraftCategory = value;
    }

    /**
     * Gets the value of the procedureDesignAircraftType property.
     * 
     * @return
     *     possible object is
     *     {@link ProcedureDesignAircraftTypes }
     *     
     */
    public ProcedureDesignAircraftTypes getProcedureDesignAircraftType() {
        return procedureDesignAircraftType;
    }

    /**
     * Sets the value of the procedureDesignAircraftType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedureDesignAircraftTypes }
     *     
     */
    public void setProcedureDesignAircraftType(ProcedureDesignAircraftTypes value) {
        this.procedureDesignAircraftType = value;
    }

    /**
     * Gets the value of the procedureLeg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the procedureLeg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcedureLeg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProcedureLeg }
     * 
     * 
     */
    public List<ProcedureLeg> getProcedureLeg() {
        if (procedureLeg == null) {
            procedureLeg = new ArrayList<ProcedureLeg>();
        }
        return this.procedureLeg;
    }

}
