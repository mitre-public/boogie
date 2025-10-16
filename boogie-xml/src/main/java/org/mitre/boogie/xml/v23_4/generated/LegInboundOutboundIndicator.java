
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LegInboundOutboundIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="LegInboundOutboundIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Inbound"/&gt;
 *     &lt;enumeration value="Outbound"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LegInboundOutboundIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum LegInboundOutboundIndicator {


    /**
     * Inbound
     * 
     */
    @XmlEnumValue("Inbound")
    INBOUND("Inbound"),

    /**
     * Inbound
     * 
     */
    @XmlEnumValue("Outbound")
    OUTBOUND("Outbound");
    private final String value;

    LegInboundOutboundIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LegInboundOutboundIndicator fromValue(String v) {
        for (LegInboundOutboundIndicator c: LegInboundOutboundIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
