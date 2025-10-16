
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Airspaces complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Airspaces"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="controlledAirspace" type="{}ControlledAirspace" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="restrictiveAirspace" type="{}RestrictiveAirspace" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="specialActivityArea" type="{}SpecialActivityArea" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="firUir" type="{}FirUir" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Airspaces", propOrder = {
    "controlledAirspace",
    "restrictiveAirspace",
    "specialActivityArea",
    "firUir"
})
public class Airspaces {

    protected List<ControlledAirspace> controlledAirspace;
    protected List<RestrictiveAirspace> restrictiveAirspace;
    protected List<SpecialActivityArea> specialActivityArea;
    protected List<FirUir> firUir;

    /**
     * Gets the value of the controlledAirspace property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the controlledAirspace property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getControlledAirspace().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ControlledAirspace }
     * 
     * 
     */
    public List<ControlledAirspace> getControlledAirspace() {
        if (controlledAirspace == null) {
            controlledAirspace = new ArrayList<ControlledAirspace>();
        }
        return this.controlledAirspace;
    }

    /**
     * Gets the value of the restrictiveAirspace property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the restrictiveAirspace property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRestrictiveAirspace().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RestrictiveAirspace }
     * 
     * 
     */
    public List<RestrictiveAirspace> getRestrictiveAirspace() {
        if (restrictiveAirspace == null) {
            restrictiveAirspace = new ArrayList<RestrictiveAirspace>();
        }
        return this.restrictiveAirspace;
    }

    /**
     * Gets the value of the specialActivityArea property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the specialActivityArea property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecialActivityArea().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SpecialActivityArea }
     * 
     * 
     */
    public List<SpecialActivityArea> getSpecialActivityArea() {
        if (specialActivityArea == null) {
            specialActivityArea = new ArrayList<SpecialActivityArea>();
        }
        return this.specialActivityArea;
    }

    /**
     * Gets the value of the firUir property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the firUir property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFirUir().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FirUir }
     * 
     * 
     */
    public List<FirUir> getFirUir() {
        if (firUir == null) {
            firUir = new ArrayList<FirUir>();
        }
        return this.firUir;
    }

}
