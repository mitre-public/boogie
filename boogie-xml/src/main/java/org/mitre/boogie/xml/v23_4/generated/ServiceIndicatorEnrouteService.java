
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceIndicatorEnrouteService.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ServiceIndicatorEnrouteService"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AEI"/&gt;
 *     &lt;enumeration value="FIS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ServiceIndicatorEnrouteService", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ServiceIndicatorEnrouteService {


    /**
     * Aeronautical Enroute Information
     * 
     */
    AEI,

    /**
     * Flight Information Service (FIS)
     * 
     */
    FIS;

    public String value() {
        return name();
    }

    public static ServiceIndicatorEnrouteService fromValue(String v) {
        return valueOf(v);
    }

}
