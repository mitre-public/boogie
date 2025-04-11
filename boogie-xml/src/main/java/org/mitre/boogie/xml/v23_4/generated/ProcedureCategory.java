
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProcedureCategory.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ProcedureCategory"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="LAAS"/&gt;
 *     &lt;enumeration value="WAAS"/&gt;
 *     &lt;enumeration value="FMS"/&gt;
 *     &lt;enumeration value="GPS"/&gt;
 *     &lt;enumeration value="VDME"/&gt;
 *     &lt;enumeration value="CIRC"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ProcedureCategory", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ProcedureCategory {


    /**
     * Local Area Differential Augmentation System
     * 
     */
    LAAS,

    /**
     * Wide Area Differential Augmentation System
     * 
     */
    WAAS,

    /**
     * Flight Management System
     * 
     */
    FMS,

    /**
     * Global Positioning System, no Augmentation
     * 
     */
    GPS,

    /**
     * VORDME, VORTACAN
     * 
     */
    VDME,

    /**
     * Circle-to-Land
     * 
     */
    CIRC;

    public String value() {
        return name();
    }

    public static ProcedureCategory fromValue(String v) {
        return valueOf(v);
    }

}
