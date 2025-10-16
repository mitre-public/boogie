
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NavaidType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="NavaidType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="vor"/&gt;
 *     &lt;enumeration value="vortac"/&gt;
 *     &lt;enumeration value="tacan"/&gt;
 *     &lt;enumeration value="vor-dme"/&gt;
 *     &lt;enumeration value="ndb"/&gt;
 *     &lt;enumeration value="ndb-dme"/&gt;
 *     &lt;enumeration value="locator"/&gt;
 *     &lt;enumeration value="dme excluding isl-dme"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NavaidType")
@XmlEnum
public enum NavaidType {

    @XmlEnumValue("vor")
    VOR("vor"),
    @XmlEnumValue("vortac")
    VORTAC("vortac"),
    @XmlEnumValue("tacan")
    TACAN("tacan"),
    @XmlEnumValue("vor-dme")
    VOR_DME("vor-dme"),
    @XmlEnumValue("ndb")
    NDB("ndb"),
    @XmlEnumValue("ndb-dme")
    NDB_DME("ndb-dme"),
    @XmlEnumValue("locator")
    LOCATOR("locator"),
    @XmlEnumValue("dme excluding isl-dme")
    DME_EXCLUDING_ISL_DME("dme excluding isl-dme");
    private final String value;

    NavaidType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NavaidType fromValue(String v) {
        for (NavaidType c: NavaidType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
