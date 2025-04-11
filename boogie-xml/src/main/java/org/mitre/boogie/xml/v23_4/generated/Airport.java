
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Airport Record.
 * 
 * <p>Java class for Airport complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Airport"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Port"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="longestRunway" type="{http://www.arinc424.com/xml/datatypes}LongestRunway" minOccurs="0"/&gt;
 *         &lt;element name="longestRunwaySurfaceCode" type="{http://www.arinc424.com/xml/enumerations}RunwaySurfaceCode" minOccurs="0"/&gt;
 *         &lt;element name="runway" type="{}Runway" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="airportGate" type="{}AirportGate" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Airport", propOrder = {
    "longestRunway",
    "longestRunwaySurfaceCode",
    "runway",
    "airportGate"
})
public class Airport
    extends Port
{

    @XmlSchemaType(name = "unsignedInt")
    protected Long longestRunway;
    @XmlSchemaType(name = "string")
    protected RunwaySurfaceCode longestRunwaySurfaceCode;
    protected List<Runway> runway;
    protected List<AirportGate> airportGate;

    /**
     * Gets the value of the longestRunway property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLongestRunway() {
        return longestRunway;
    }

    /**
     * Sets the value of the longestRunway property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLongestRunway(Long value) {
        this.longestRunway = value;
    }

    /**
     * Gets the value of the longestRunwaySurfaceCode property.
     * 
     * @return
     *     possible object is
     *     {@link RunwaySurfaceCode }
     *     
     */
    public RunwaySurfaceCode getLongestRunwaySurfaceCode() {
        return longestRunwaySurfaceCode;
    }

    /**
     * Sets the value of the longestRunwaySurfaceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link RunwaySurfaceCode }
     *     
     */
    public void setLongestRunwaySurfaceCode(RunwaySurfaceCode value) {
        this.longestRunwaySurfaceCode = value;
    }

    /**
     * Gets the value of the runway property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the runway property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRunway().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Runway }
     * 
     * 
     */
    public List<Runway> getRunway() {
        if (runway == null) {
            runway = new ArrayList<Runway>();
        }
        return this.runway;
    }

    /**
     * Gets the value of the airportGate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the airportGate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAirportGate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AirportGate }
     * 
     * 
     */
    public List<AirportGate> getAirportGate() {
        if (airportGate == null) {
            airportGate = new ArrayList<AirportGate>();
        }
        return this.airportGate;
    }

}
