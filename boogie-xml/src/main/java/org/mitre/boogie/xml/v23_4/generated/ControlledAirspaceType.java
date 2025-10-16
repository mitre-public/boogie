
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ControlledAirspaceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ControlledAirspaceType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ClassC"/&gt;
 *     &lt;enumeration value="Control"/&gt;
 *     &lt;enumeration value="TerminalControl"/&gt;
 *     &lt;enumeration value="Radar"/&gt;
 *     &lt;enumeration value="ClassB"/&gt;
 *     &lt;enumeration value="RadioMandatoryZone"/&gt;
 *     &lt;enumeration value="TransponderMandatoryZone"/&gt;
 *     &lt;enumeration value="ClassD"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ControlledAirspaceType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ControlledAirspaceType {


    /**
     * Class C Airspace (Was ARSA within the USA).
     * 
     */
    @XmlEnumValue("ClassC")
    CLASS_C("ClassC"),

    /**
     * Control Area, ICAO Designation (CTA).
     * 
     */
    @XmlEnumValue("Control")
    CONTROL("Control"),

    /**
     * Terminal Control Area, ICAO Designation (TMA or TCA).
     * 
     */
    @XmlEnumValue("TerminalControl")
    TERMINAL_CONTROL("TerminalControl"),

    /**
     * Radar Zone or Radar Area (Was TRSA within the USA).
     * 
     */
    @XmlEnumValue("Radar")
    RADAR("Radar"),

    /**
     * Class B Airspace (Was TCA with the USA).
     * 
     */
    @XmlEnumValue("ClassB")
    CLASS_B("ClassB"),

    /**
     * Radio Mandatory Zone (RMZ)
     * 
     */
    @XmlEnumValue("RadioMandatoryZone")
    RADIO_MANDATORY_ZONE("RadioMandatoryZone"),

    /**
     * Transponder Mandatory Zone (TMZ)
     * 
     */
    @XmlEnumValue("TransponderMandatoryZone")
    TRANSPONDER_MANDATORY_ZONE("TransponderMandatoryZone"),

    /**
     * Class D Airspace within the USA, Control Zone, ICAO Designation (CTR).
     * 
     */
    @XmlEnumValue("ClassD")
    CLASS_D("ClassD");
    private final String value;

    ControlledAirspaceType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ControlledAirspaceType fromValue(String v) {
        for (ControlledAirspaceType c: ControlledAirspaceType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
