
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AirRefuelingUsage.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AirRefuelingUsage"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AN"/&gt;
 *     &lt;enumeration value="AP"/&gt;
 *     &lt;enumeration value="CP"/&gt;
 *     &lt;enumeration value="ET"/&gt;
 *     &lt;enumeration value="EX"/&gt;
 *     &lt;enumeration value="IP"/&gt;
 *     &lt;enumeration value="NC"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AirRefuelingUsage")
@XmlEnum
public enum AirRefuelingUsage {

    AN,
    AP,
    CP,
    ET,
    EX,
    IP,
    NC;

    public String value() {
        return name();
    }

    public static AirRefuelingUsage fromValue(String v) {
        return valueOf(v);
    }

}
