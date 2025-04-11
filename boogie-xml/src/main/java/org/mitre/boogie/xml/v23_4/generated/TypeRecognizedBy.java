
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TypeRecognizedBy.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="TypeRecognizedBy"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Icao"/&gt;
 *     &lt;enumeration value="Faa"/&gt;
 *     &lt;enumeration value="IcaoAndFaa"/&gt;
 *     &lt;enumeration value="LocalCountry"/&gt;
 *     &lt;enumeration value="OtherLocalCountry"/&gt;
 *     &lt;enumeration value="MadeUp"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TypeRecognizedBy", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum TypeRecognizedBy {


    /**
     * The Communication Type is found in government source provided in accordance with ICAO standards.
     * 
     */
    @XmlEnumValue("Icao")
    ICAO("Icao"),

    /**
     * The Communication Type is found in government source provided in accordance with US FAA standards.
     * 
     */
    @XmlEnumValue("Faa")
    FAA("Faa"),

    /**
     * The Communication Type is found in government source provided in accordance with both ICAO and US FAA standards.
     * 
     */
    @XmlEnumValue("IcaoAndFaa")
    ICAO_AND_FAA("IcaoAndFaa"),

    /**
     * The Communication Type is found in government source provided by the country in which the communication is used.
     * 
     */
    @XmlEnumValue("LocalCountry")
    LOCAL_COUNTRY("LocalCountry"),

    /**
     * The Communication Type is found in government source provided by the country in which the communication is used.
     * 
     */
    @XmlEnumValue("OtherLocalCountry")
    OTHER_LOCAL_COUNTRY("OtherLocalCountry"),

    /**
     * The Communication Type has been established by the data supplier.
     * 
     */
    @XmlEnumValue("MadeUp")
    MADE_UP("MadeUp");
    private final String value;

    TypeRecognizedBy(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TypeRecognizedBy fromValue(String v) {
        for (TypeRecognizedBy c: TypeRecognizedBy.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
