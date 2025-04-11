
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FlightPlanningRecords complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FlightPlanningRecords"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="flightPlanningRecord" type="{}FlightPlanningRecord" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlightPlanningRecords", propOrder = {
    "flightPlanningRecord"
})
public class FlightPlanningRecords {

    @XmlElement(required = true)
    protected List<FlightPlanningRecord> flightPlanningRecord;

    /**
     * Gets the value of the flightPlanningRecord property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the flightPlanningRecord property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFlightPlanningRecord().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FlightPlanningRecord }
     * 
     * 
     */
    public List<FlightPlanningRecord> getFlightPlanningRecord() {
        if (flightPlanningRecord == null) {
            flightPlanningRecord = new ArrayList<FlightPlanningRecord>();
        }
        return this.flightPlanningRecord;
    }

}
