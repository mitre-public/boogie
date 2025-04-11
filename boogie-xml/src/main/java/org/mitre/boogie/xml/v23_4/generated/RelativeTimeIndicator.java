
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RelativeTimeIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RelativeTimeIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="BeforeSunrise"/&gt;
 *     &lt;enumeration value="AfterSunrise"/&gt;
 *     &lt;enumeration value="BeforeSunset"/&gt;
 *     &lt;enumeration value="AfterSunset"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RelativeTimeIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum RelativeTimeIndicator {

    @XmlEnumValue("BeforeSunrise")
    BEFORE_SUNRISE("BeforeSunrise"),
    @XmlEnumValue("AfterSunrise")
    AFTER_SUNRISE("AfterSunrise"),
    @XmlEnumValue("BeforeSunset")
    BEFORE_SUNSET("BeforeSunset"),
    @XmlEnumValue("AfterSunset")
    AFTER_SUNSET("AfterSunset");
    private final String value;

    RelativeTimeIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RelativeTimeIndicator fromValue(String v) {
        for (RelativeTimeIndicator c: RelativeTimeIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
