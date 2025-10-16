
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MinimaHeightType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="MinimaHeightType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DH"/&gt;
 *     &lt;enumeration value="MDH"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MinimaHeightType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum MinimaHeightType {


    /**
     * Height value is a Decision Height (DH)
     * 
     */
    DH,

    /**
     * Height value is a Minimum Descent Height (MDH)
     * 
     */
    MDH;

    public String value() {
        return name();
    }

    public static MinimaHeightType fromValue(String v) {
        return valueOf(v);
    }

}
