
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AlternateRecordType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AlternateRecordType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AA"/&gt;
 *     &lt;enumeration value="DA"/&gt;
 *     &lt;enumeration value="EA"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AlternateRecordType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AlternateRecordType {


    /**
     * The Airport identifier in Columns 7 through 11 of the Primary Record are the identifier of the Arrival Airport.
     * 
     */
    AA,

    /**
     * The Airport identifier in Columns 7 through 11 of the Primary Record are the identifier of the Departure Airport.
     * 
     */
    DA,

    /**
     * The end fix of a Company Route is identified in Columns 7 through 15 of the Primary Record.
     * 
     */
    EA;

    public String value() {
        return name();
    }

    public static AlternateRecordType fromValue(String v) {
        return valueOf(v);
    }

}
