
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AreaCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AreaCode"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AFR"/&gt;
 *     &lt;enumeration value="CAN"/&gt;
 *     &lt;enumeration value="EEU"/&gt;
 *     &lt;enumeration value="EUR"/&gt;
 *     &lt;enumeration value="LAM"/&gt;
 *     &lt;enumeration value="MES"/&gt;
 *     &lt;enumeration value="PAC"/&gt;
 *     &lt;enumeration value="SAM"/&gt;
 *     &lt;enumeration value="SPA"/&gt;
 *     &lt;enumeration value="USA"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AreaCode", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AreaCode {


    /**
     * Africa
     * 
     */
    AFR,

    /**
     * Canada
     * 
     */
    CAN,

    /**
     * Eastern Europe and Asia
     * 
     */
    EEU,

    /**
     * Europe
     * 
     */
    EUR,

    /**
     * Latin America
     * 
     */
    LAM,

    /**
     * Middle East
     * 
     */
    MES,

    /**
     * Pacific
     * 
     */
    PAC,

    /**
     * South America
     * 
     */
    SAM,

    /**
     * South Pacific
     * 
     */
    SPA,

    /**
     * United States
     * 
     */
    USA;

    public String value() {
        return name();
    }

    public static AreaCode fromValue(String v) {
        return valueOf(v);
    }

}
