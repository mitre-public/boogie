
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CommunicationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="CommunicationType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ACC"/&gt;
 *     &lt;enumeration value="ACP"/&gt;
 *     &lt;enumeration value="AIR"/&gt;
 *     &lt;enumeration value="APP"/&gt;
 *     &lt;enumeration value="ARR"/&gt;
 *     &lt;enumeration value="ASO"/&gt;
 *     &lt;enumeration value="ATI"/&gt;
 *     &lt;enumeration value="AWI"/&gt;
 *     &lt;enumeration value="AWO"/&gt;
 *     &lt;enumeration value="AWS"/&gt;
 *     &lt;enumeration value="CBA"/&gt;
 *     &lt;enumeration value="CCA"/&gt;
 *     &lt;enumeration value="CLD"/&gt;
 *     &lt;enumeration value="CPT"/&gt;
 *     &lt;enumeration value="CTA"/&gt;
 *     &lt;enumeration value="CTF"/&gt;
 *     &lt;enumeration value="CTL"/&gt;
 *     &lt;enumeration value="DEP"/&gt;
 *     &lt;enumeration value="DIR"/&gt;
 *     &lt;enumeration value="EFS"/&gt;
 *     &lt;enumeration value="EMR"/&gt;
 *     &lt;enumeration value="FSS"/&gt;
 *     &lt;enumeration value="GCO"/&gt;
 *     &lt;enumeration value="GND"/&gt;
 *     &lt;enumeration value="GTE"/&gt;
 *     &lt;enumeration value="HEL"/&gt;
 *     &lt;enumeration value="INF"/&gt;
 *     &lt;enumeration value="MBZ"/&gt;
 *     &lt;enumeration value="MIL"/&gt;
 *     &lt;enumeration value="MUL"/&gt;
 *     &lt;enumeration value="OPS"/&gt;
 *     &lt;enumeration value="PAL"/&gt;
 *     &lt;enumeration value="RDO"/&gt;
 *     &lt;enumeration value="RDR"/&gt;
 *     &lt;enumeration value="RFS"/&gt;
 *     &lt;enumeration value="RMP"/&gt;
 *     &lt;enumeration value="RSA"/&gt;
 *     &lt;enumeration value="TCA"/&gt;
 *     &lt;enumeration value="TMA"/&gt;
 *     &lt;enumeration value="TML"/&gt;
 *     &lt;enumeration value="TRS"/&gt;
 *     &lt;enumeration value="TWE"/&gt;
 *     &lt;enumeration value="TWR"/&gt;
 *     &lt;enumeration value="UAC"/&gt;
 *     &lt;enumeration value="UNI"/&gt;
 *     &lt;enumeration value="VOL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CommunicationType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum CommunicationType {


    /**
     * Area Control Center
     * 
     */
    ACC,

    /**
     * Airlift Command Post
     * 
     */
    ACP,

    /**
     * Air to Air
     * 
     */
    AIR,

    /**
     * Approach Control
     * 
     */
    APP,

    /**
     * Arrival Control
     * 
     */
    ARR,

    /**
     * Automatic Surface Observing System (ASOS)
     * 
     */
    ASO,

    /**
     * Automatic Terminal Info Service (ATIS)
     * 
     */
    ATI,

    /**
     * Airport Weather Information Broadcast (AWIB)
     * 
     */
    AWI,

    /**
     * Automatic Weather Observing Service (AWOS)
     * 
     */
    AWO,

    /**
     * Aerodrome Weather Information Services (AWIS)
     * 
     */
    AWS,

    /**
     * Class B Airspace
     * 
     */
    CBA,

    /**
     * Class C Airspace
     * 
     */
    CCA,

    /**
     * Clearance Delivery
     * 
     */
    CLD,

    /**
     * Clearance, Pre-Taxi
     * 
     */
    CPT,

    /**
     * Control Area (Terminal)
     * 
     */
    CTA,

    /**
     * Common Traffic Advisory Frequencies (Note 2)
     * 
     */
    CTF,

    /**
     * Control
     * 
     */
    CTL,

    /**
     * Departure Control
     * 
     */
    DEP,

    /**
     * Director (Approach Control Radar)
     * 
     */
    DIR,

    /**
     * Enroute Flight Advisory Service (EFAS)
     * 
     */
    EFS,

    /**
     * Emergency
     * 
     */
    EMR,

    /**
     * Flight Service Station
     * 
     */
    FSS,

    /**
     * Ground Comm Outlet
     * 
     */
    GCO,

    /**
     * Ground Control
     * 
     */
    GND,

    /**
     * Gate Control
     * 
     */
    GTE,

    /**
     * Helicopter Frequency
     * 
     */
    HEL,

    /**
     * Information
     * 
     */
    INF,

    /**
     * Mandatory Broadcast
     * 
     */
    MBZ,

    /**
     * Military Frequency Zone (note 2)
     * 
     */
    MIL,

    /**
     * Multicom
     * 
     */
    MUL,

    /**
     * Operations
     * 
     */
    OPS,

    /**
     * Pilot Activated Lighting (Note 1)
     * 
     */
    PAL,

    /**
     * Radio
     * 
     */
    RDO,

    /**
     * Radar
     * 
     */
    RDR,

    /**
     * Remote Flight Service Station (RFSS)
     * 
     */
    RFS,

    /**
     * Ramp/Taxi Control
     * 
     */
    RMP,

    /**
     * Airport Radar Service Area (ARSA)
     * 
     */
    RSA,

    /**
     * Terminal Control Area (TCA)
     * 
     */
    TCA,

    /**
     * Terminal Control Area (TMA)
     * 
     */
    TMA,

    /**
     * Terminal
     * 
     */
    TML,

    /**
     * Terminal Radar Service Area (TRSA)
     * 
     */
    TRS,

    /**
     * Transcriber Weather Broadcast (TWEB)
     * 
     */
    TWE,

    /**
     * Tower, Air Traffic Control
     * 
     */
    TWR,

    /**
     * Upper Area Control
     * 
     */
    UAC,

    /**
     * Unicom
     * 
     */
    UNI,

    /**
     * Volmet
     * 
     */
    VOL;

    public String value() {
        return name();
    }

    public static CommunicationType fromValue(String v) {
        return valueOf(v);
    }

}
