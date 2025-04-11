
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApproachRouteType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ApproachRouteType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="LocBackcourse"/&gt;
 *     &lt;enumeration value="VorDme"/&gt;
 *     &lt;enumeration value="Fms"/&gt;
 *     &lt;enumeration value="Igs"/&gt;
 *     &lt;enumeration value="Rnp"/&gt;
 *     &lt;enumeration value="Ils"/&gt;
 *     &lt;enumeration value="Gls"/&gt;
 *     &lt;enumeration value="LocOnly"/&gt;
 *     &lt;enumeration value="Mls"/&gt;
 *     &lt;enumeration value="Ndb"/&gt;
 *     &lt;enumeration value="Gps"/&gt;
 *     &lt;enumeration value="NdbDme"/&gt;
 *     &lt;enumeration value="Rnav"/&gt;
 *     &lt;enumeration value="VorUsingVordmeOrVortac"/&gt;
 *     &lt;enumeration value="Tacan"/&gt;
 *     &lt;enumeration value="Sdf"/&gt;
 *     &lt;enumeration value="Vor"/&gt;
 *     &lt;enumeration value="Lda"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ApproachRouteType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ApproachRouteType {


    /**
     * Localizer/Backcourse Approach
     * 
     */
    @XmlEnumValue("LocBackcourse")
    LOC_BACKCOURSE("LocBackcourse"),

    /**
     * VORDME Approach
     * 
     */
    @XmlEnumValue("VorDme")
    VOR_DME("VorDme"),

    /**
     * Flight Management System (FMS) Approach 
     * 
     */
    @XmlEnumValue("Fms")
    FMS("Fms"),

    /**
     * Instrument Guidance System (IGS) Approach
     * 
     */
    @XmlEnumValue("Igs")
    IGS("Igs"),

    /**
     * Route types of Rnp or Rnav indicate a procedure titled RNAV, e.g., RNAV (GPS) or RNAV (RNP). Route Type H indicates a procedure titled RNP. COMMENTARY: The Route Types H and R are coded to differentiate between the approach procedure titles published in state source. The words in brackets will not be considered for the coding of the Route Type. While according to the PBN manual there is no RNAV approach specification, many approaches are still published using an RNAV title. Additionally, there are still non PBN RNAV approaches published, e.g., VOR/DME RNAV. The following old titles will be coded with a Route Type R:  RNAV (GPS) RWY 09  RNAV (GNSS) RWY 09  RNAV (RNP) RWY 09 The following new titles will be coded with Route Type H:  RNP RWY 09  RNP RWY 09 (AR) The following new titles will be coded with Route Type R:  RNAV RWY 09  RNAV RWY 09 (AR
     * 
     */
    @XmlEnumValue("Rnp")
    RNP("Rnp"),

    /**
     * Instrument Landing System (ILS) Approach 
     * 
     */
    @XmlEnumValue("Ils")
    ILS("Ils"),

    /**
     * GNSS Landing System (GLS)Approach 
     * 
     */
    @XmlEnumValue("Gls")
    GLS("Gls"),

    /**
     * Localizer Only (LOC) Approach 
     * 
     */
    @XmlEnumValue("LocOnly")
    LOC_ONLY("LocOnly"),

    /**
     * Microwave Landing System (MLS) Approach 
     * 
     */
    @XmlEnumValue("Mls")
    MLS("Mls"),

    /**
     * Non-Directional Beacon (NDB) Approach 
     * 
     */
    @XmlEnumValue("Ndb")
    NDB("Ndb"),

    /**
     * Global Positioning System (GPS) Approach 
     * 
     */
    @XmlEnumValue("Gps")
    GPS("Gps"),

    /**
     * Non-Directional Beacon + DME(NDB+DME) Approach 
     * 
     */
    @XmlEnumValue("NdbDme")
    NDB_DME("NdbDme"),

    /**
     * Route Type R indicates a procedure titled RNAV, e.g., RNAV (GPS) or RNAV (RNP). Route Type H indicates a procedure titled RNP. COMMENTARY: The Route Types H and R are coded to differentiate between the approach procedure titles published in state source. The words in brackets will not be considered for the coding of the Route Type. While according to the PBN manual there is no RNAV approach specification, many approaches are still published using an RNAV title. Additionally, there are still non PBN RNAV approaches published, e.g., VOR/DME RNAV. The following old titles will be coded with a Route Type R:  RNAV (GPS) RWY 09  RNAV (GNSS) RWY 09  RNAV (RNP) RWY 09 The following new titles will be coded with Route Type H:  RNP RWY 09  RNP RWY 09 (AR) The following new titles will be coded with Route Type R:  RNAV RWY 09  RNAV RWY 09 (AR
     * 
     */
    @XmlEnumValue("Rnav")
    RNAV("Rnav"),

    /**
     * VOR Approach using VORDME/VORTAC 
     * 
     */
    @XmlEnumValue("VorUsingVordmeOrVortac")
    VOR_USING_VORDME_OR_VORTAC("VorUsingVordmeOrVortac"),

    /**
     * TACAN Approach 
     * 
     */
    @XmlEnumValue("Tacan")
    TACAN("Tacan"),

    /**
     * Simplified Directional Facility (SDF) Approach 
     * 
     */
    @XmlEnumValue("Sdf")
    SDF("Sdf"),

    /**
     * VOR Approach 
     * 
     */
    @XmlEnumValue("Vor")
    VOR("Vor"),

    /**
     * Localizer Directional Aid (LDA) Approach 
     * 
     */
    @XmlEnumValue("Lda")
    LDA("Lda");
    private final String value;

    ApproachRouteType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ApproachRouteType fromValue(String v) {
        for (ApproachRouteType c: ApproachRouteType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
