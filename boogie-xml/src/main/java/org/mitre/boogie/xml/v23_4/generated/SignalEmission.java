
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SignalEmission.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="SignalEmission"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="A3"/&gt;
 *     &lt;enumeration value="A3a"/&gt;
 *     &lt;enumeration value="A3b"/&gt;
 *     &lt;enumeration value="A3h"/&gt;
 *     &lt;enumeration value="A3j"/&gt;
 *     &lt;enumeration value="LsbCarrierUnk"/&gt;
 *     &lt;enumeration value="UsbCarrierUnk"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SignalEmission", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum SignalEmission {


    /**
     * Double Sideband (A3)
     * 
     */
    @XmlEnumValue("A3")
    A_3("A3"),

    /**
     * Single sideband, reduced carrier (A3A)
     * 
     */
    @XmlEnumValue("A3a")
    A_3_A("A3a"),

    /**
     * Two Independent sidebands (A3B)
     * 
     */
    @XmlEnumValue("A3b")
    A_3_B("A3b"),

    /**
     * Single sideband, full carrier (A3H)
     * 
     */
    @XmlEnumValue("A3h")
    A_3_H("A3h"),

    /**
     * Single sideband, suppressed carrier (A3J)
     * 
     */
    @XmlEnumValue("A3j")
    A_3_J("A3j"),

    /**
     * Lower (single) sideband, carrier unknown
     * 
     */
    @XmlEnumValue("LsbCarrierUnk")
    LSB_CARRIER_UNK("LsbCarrierUnk"),

    /**
     * Upper (single) sideband, carrier unknown
     * 
     */
    @XmlEnumValue("UsbCarrierUnk")
    USB_CARRIER_UNK("UsbCarrierUnk");
    private final String value;

    SignalEmission(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SignalEmission fromValue(String v) {
        for (SignalEmission c: SignalEmission.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
