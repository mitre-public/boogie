
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 All the air refueling routes around the world.
 *                 Air refueling routes consists of one or more
 *                 air refueling route.
 *             
 * 
 * <p>Java class for AirRefuelingRoutes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AirRefuelingRoutes"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}airRefuelingRoute" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirRefuelingRoutes", propOrder = {
    "airRefuelingRoute"
})
public class AirRefuelingRoutes {

    @XmlElement(required = true)
    protected List<AirRefuelingRoute> airRefuelingRoute;

    /**
     * Gets the value of the airRefuelingRoute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the airRefuelingRoute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAirRefuelingRoute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AirRefuelingRoute }
     * 
     * 
     */
    public List<AirRefuelingRoute> getAirRefuelingRoute() {
        if (airRefuelingRoute == null) {
            airRefuelingRoute = new ArrayList<AirRefuelingRoute>();
        }
        return this.airRefuelingRoute;
    }

}
