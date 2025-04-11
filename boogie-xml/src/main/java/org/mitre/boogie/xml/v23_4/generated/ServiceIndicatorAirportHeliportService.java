
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceIndicatorAirportHeliportService.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ServiceIndicatorAirportHeliportService"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AAS"/&gt;
 *     &lt;enumeration value="CARC"/&gt;
 *     &lt;enumeration value="DepartureService"/&gt;
 *     &lt;enumeration value="FIS"/&gt;
 *     &lt;enumeration value="IC"/&gt;
 *     &lt;enumeration value="ArrivalService"/&gt;
 *     &lt;enumeration value="AFIS"/&gt;
 *     &lt;enumeration value="TerminalAreaControl"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ServiceIndicatorAirportHeliportService", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ServiceIndicatorAirportHeliportService {


    /**
     * Airport Advisory Service (AAS)
     * 
     */
    AAS("AAS"),

    /**
     * Community Aerodrome Radio Station (CARS)
     * 
     */
    CARC("CARC"),

    /**
     * Departure Service (Other than Departure Control Unit)
     * 
     */
    @XmlEnumValue("DepartureService")
    DEPARTURE_SERVICE("DepartureService"),

    /**
     * Flight Information Service (FIS)
     * 
     */
    FIS("FIS"),

    /**
     * Initial Contact (IC)
     * 
     */
    IC("IC"),

    /**
     * Arrival Service (Other than Arrival Control Unit)
     * 
     */
    @XmlEnumValue("ArrivalService")
    ARRIVAL_SERVICE("ArrivalService"),

    /**
     * Aerodrome Flight Information Service(AFIS)
     * 
     */
    AFIS("AFIS"),

    /**
     * Terminal Area Control (Other than dedicated Terminal Control Unit)
     * 
     */
    @XmlEnumValue("TerminalAreaControl")
    TERMINAL_AREA_CONTROL("TerminalAreaControl");
    private final String value;

    ServiceIndicatorAirportHeliportService(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ServiceIndicatorAirportHeliportService fromValue(String v) {
        for (ServiceIndicatorAirportHeliportService c: ServiceIndicatorAirportHeliportService.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
