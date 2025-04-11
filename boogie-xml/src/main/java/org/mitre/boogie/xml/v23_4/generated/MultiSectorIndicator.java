
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MultiSectorIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="MultiSectorIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="MultiSector"/&gt;
 *     &lt;enumeration value="NotDefined"/&gt;
 *     &lt;enumeration value="SingleSector"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MultiSectorIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum MultiSectorIndicator {


    /**
     * Multi-sector data is published in official government source for the service and frequency
     * 
     */
    @XmlEnumValue("MultiSector")
    MULTI_SECTOR("MultiSector"),

    /**
     * There is no defined sector data published for the service and frequency.
     * 
     */
    @XmlEnumValue("NotDefined")
    NOT_DEFINED("NotDefined"),

    /**
     * The official government source has provided only a single defined sector for the service and frequency
     * 
     */
    @XmlEnumValue("SingleSector")
    SINGLE_SECTOR("SingleSector");
    private final String value;

    MultiSectorIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MultiSectorIndicator fromValue(String v) {
        for (MultiSectorIndicator c: MultiSectorIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
