
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AltitudeTerminationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AltitudeTerminationType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="LegTermination"/&gt;
 *     &lt;enumeration value="WhicheverIsLater"/&gt;
 *     &lt;enumeration value="WhicheverIsEarlier"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AltitudeTerminationType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AltitudeTerminationType {

    @XmlEnumValue("LegTermination")
    LEG_TERMINATION("LegTermination"),
    @XmlEnumValue("WhicheverIsLater")
    WHICHEVER_IS_LATER("WhicheverIsLater"),
    @XmlEnumValue("WhicheverIsEarlier")
    WHICHEVER_IS_EARLIER("WhicheverIsEarlier");
    private final String value;

    AltitudeTerminationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AltitudeTerminationType fromValue(String v) {
        for (AltitudeTerminationType c: AltitudeTerminationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
