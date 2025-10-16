
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BlockIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="BlockIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Block"/&gt;
 *     &lt;enumeration value="Individual"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "BlockIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum BlockIndicator {


    /**
     * indicating an altitude block
     * 
     */
    @XmlEnumValue("Block")
    BLOCK("Block"),

    /**
     * indicating individual altitudes
     * 
     */
    @XmlEnumValue("Individual")
    INDIVIDUAL("Individual");
    private final String value;

    BlockIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BlockIndicator fromValue(String v) {
        for (BlockIndicator c: BlockIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
