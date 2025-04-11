
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IlsDmeLocation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="IlsDmeLocation"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NotCollocated"/&gt;
 *     &lt;enumeration value="CollocatedLocalizer"/&gt;
 *     &lt;enumeration value="CollocatedGlideSlope"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "IlsDmeLocation", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum IlsDmeLocation {


    /**
     * Not collocated with Localizer or Glide Slope
     * 
     */
    @XmlEnumValue("NotCollocated")
    NOT_COLLOCATED("NotCollocated"),

    /**
     * Collocated with localizer
     * 
     */
    @XmlEnumValue("CollocatedLocalizer")
    COLLOCATED_LOCALIZER("CollocatedLocalizer"),

    /**
     * Collocated with Glide Slope
     * 
     */
    @XmlEnumValue("CollocatedGlideSlope")
    COLLOCATED_GLIDE_SLOPE("CollocatedGlideSlope");
    private final String value;

    IlsDmeLocation(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IlsDmeLocation fromValue(String v) {
        for (IlsDmeLocation c: IlsDmeLocation.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
