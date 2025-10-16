
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FreqUnitOfMeasure.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="FreqUnitOfMeasure"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="megaHertz"/&gt;
 *     &lt;enumeration value="kiloHertz"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FreqUnitOfMeasure", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum FreqUnitOfMeasure {


    /**
     * megaHertz
     * 
     */
    @XmlEnumValue("megaHertz")
    MEGA_HERTZ("megaHertz"),

    /**
     * kiloHertz
     * 
     */
    @XmlEnumValue("kiloHertz")
    KILO_HERTZ("kiloHertz");
    private final String value;

    FreqUnitOfMeasure(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FreqUnitOfMeasure fromValue(String v) {
        for (FreqUnitOfMeasure c: FreqUnitOfMeasure.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
