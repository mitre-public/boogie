
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CommunicationClass.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="CommunicationClass"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="LIRC"/&gt;
 *     &lt;enumeration value="LIRI"/&gt;
 *     &lt;enumeration value="USVC"/&gt;
 *     &lt;enumeration value="ASVC"/&gt;
 *     &lt;enumeration value="ATCF"/&gt;
 *     &lt;enumeration value="GNDF"/&gt;
 *     &lt;enumeration value="AOTF"/&gt;
 *     &lt;enumeration value="AFAC"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CommunicationClass", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum CommunicationClass {


    /**
     * The Communication Type is that of one linked to an Information Region (FIR/UIR) for the purposes of providing control services to aircraft.
     * 
     */
    LIRC,

    /**
     * The Communication Type is that of one linked to an Information Region (FIR/UIR) for the purposes of providing information services to aircraft.
     * 
     */
    LIRI,

    /**
     * The Communication Type is that of one used within an Information Region (FIR/UIR) for purposes other than control or information services and is not linked to that Region
     * 
     */
    USVC,

    /**
     * The Communication Type is that of one providing automated or broadcast services within an Information Region (FIR/UIR).
     * 
     */
    ASVC,

    /**
     * The Communication Type is that of one providing ATC services to aircraft within the terminal area of an airport.
     * 
     */
    ATCF,

    /**
     * The Communication Type is that of one providing ATC services to aircraft on the ground at an airport.
     * 
     */
    GNDF,

    /**
     * The Communication Type is that of one providing services other than ATC functions on the ground or within the terminal area of an airport.
     * 
     */
    AOTF,

    /**
     * The Communication Type is that of one provided automated or broadcast services to aircraft on the ground or with the terminal area of an airport.
     * 
     */
    AFAC;

    public String value() {
        return name();
    }

    public static CommunicationClass fromValue(String v) {
        return valueOf(v);
    }

}
