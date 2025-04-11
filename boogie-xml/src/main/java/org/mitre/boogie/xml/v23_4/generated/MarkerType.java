
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MarkerType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="MarkerType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IM"/&gt;
 *     &lt;enumeration value="MM"/&gt;
 *     &lt;enumeration value="OM"/&gt;
 *     &lt;enumeration value="BM"/&gt;
 *     &lt;enumeration value="L"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MarkerType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum MarkerType {


    /**
     * Inner Marker
     * 
     */
    IM,

    /**
     * Middle Marker
     * 
     */
    MM,

    /**
     * Outer Marker
     * 
     */
    OM,

    /**
     * Back Marker
     * 
     */
    BM,

    /**
     * Locator at Marker
     * 
     */
    L;

    public String value() {
        return name();
    }

    public static MarkerType fromValue(String v) {
        return valueOf(v);
    }

}
