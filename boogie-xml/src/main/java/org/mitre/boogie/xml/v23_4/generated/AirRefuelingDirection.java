
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AirRefuelingDirection.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AirRefuelingDirection"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="N"/&gt;
 *     &lt;enumeration value="S"/&gt;
 *     &lt;enumeration value="E"/&gt;
 *     &lt;enumeration value="W"/&gt;
 *     &lt;enumeration value="NE"/&gt;
 *     &lt;enumeration value="NW"/&gt;
 *     &lt;enumeration value="SE"/&gt;
 *     &lt;enumeration value="SW"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AirRefuelingDirection")
@XmlEnum
public enum AirRefuelingDirection {

    N,
    S,
    E,
    W,
    NE,
    NW,
    SE,
    SW;

    public String value() {
        return name();
    }

    public static AirRefuelingDirection fromValue(String v) {
        return valueOf(v);
    }

}
