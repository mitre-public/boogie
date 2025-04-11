
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProcedureType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ProcedureType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ArrivalInDb"/&gt;
 *     &lt;enumeration value="ArrivalNotInDb"/&gt;
 *     &lt;enumeration value="DepartureInDb"/&gt;
 *     &lt;enumeration value="DepartureNotInDb"/&gt;
 *     &lt;enumeration value="StarInDb"/&gt;
 *     &lt;enumeration value="StarNotInDb"/&gt;
 *     &lt;enumeration value="SidInDb"/&gt;
 *     &lt;enumeration value="SidNotInDb"/&gt;
 *     &lt;enumeration value="VectorSidInDb"/&gt;
 *     &lt;enumeration value="VectorSidNotInDb"/&gt;
 *     &lt;enumeration value="ApproachInDb"/&gt;
 *     &lt;enumeration value="ApproachNotInDb"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ProcedureType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ProcedureType {


    /**
     * Arrival Procedure, Available in Database
     * 
     */
    @XmlEnumValue("ArrivalInDb")
    ARRIVAL_IN_DB("ArrivalInDb"),

    /**
     * Arrival Procedure, Not Available in Database
     * 
     */
    @XmlEnumValue("ArrivalNotInDb")
    ARRIVAL_NOT_IN_DB("ArrivalNotInDb"),

    /**
     * Departure Procedure, Available in Database
     * 
     */
    @XmlEnumValue("DepartureInDb")
    DEPARTURE_IN_DB("DepartureInDb"),

    /**
     * Departure Procedure, Not Available in Database
     * 
     */
    @XmlEnumValue("DepartureNotInDb")
    DEPARTURE_NOT_IN_DB("DepartureNotInDb"),

    /**
     * Standard Terminal Arrival Route (STAR), Available in Database
     * 
     */
    @XmlEnumValue("StarInDb")
    STAR_IN_DB("StarInDb"),

    /**
     * Standard Terminal Arrival Route (STAR), Not Available in Database
     * 
     */
    @XmlEnumValue("StarNotInDb")
    STAR_NOT_IN_DB("StarNotInDb"),

    /**
     * Standard Instrument Departure (SID), Available in Database
     * 
     */
    @XmlEnumValue("SidInDb")
    SID_IN_DB("SidInDb"),

    /**
     * Standard Instrument Departure (SID), Not Available in Database
     * 
     */
    @XmlEnumValue("SidNotInDb")
    SID_NOT_IN_DB("SidNotInDb"),

    /**
     * Vector SID, Available in Database
     * 
     */
    @XmlEnumValue("VectorSidInDb")
    VECTOR_SID_IN_DB("VectorSidInDb"),

    /**
     * Vector SID, Not Available in Database
     * 
     */
    @XmlEnumValue("VectorSidNotInDb")
    VECTOR_SID_NOT_IN_DB("VectorSidNotInDb"),

    /**
     * Approach Procedure, Available in Database
     * 
     */
    @XmlEnumValue("ApproachInDb")
    APPROACH_IN_DB("ApproachInDb"),

    /**
     * Approach Procedure, Not Available in Database
     * 
     */
    @XmlEnumValue("ApproachNotInDb")
    APPROACH_NOT_IN_DB("ApproachNotInDb");
    private final String value;

    ProcedureType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ProcedureType fromValue(String v) {
        for (ProcedureType c: ProcedureType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
