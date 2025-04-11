
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FinalGuidanceSystem.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="FinalGuidanceSystem"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CatI"/&gt;
 *     &lt;enumeration value="CatII"/&gt;
 *     &lt;enumeration value="CatIII"/&gt;
 *     &lt;enumeration value="CatIIIA"/&gt;
 *     &lt;enumeration value="CatIIIB"/&gt;
 *     &lt;enumeration value="CatIIIC"/&gt;
 *     &lt;enumeration value="GlideSlope"/&gt;
 *     &lt;enumeration value="GlideSlopeOut"/&gt;
 *     &lt;enumeration value="Lnav"/&gt;
 *     &lt;enumeration value="LnavVnav"/&gt;
 *     &lt;enumeration value="Lp"/&gt;
 *     &lt;enumeration value="Lpv"/&gt;
 *     &lt;enumeration value="SaCatI"/&gt;
 *     &lt;enumeration value="SaCatII"/&gt;
 *     &lt;enumeration value="Uncategorized"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FinalGuidanceSystem", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum FinalGuidanceSystem {


    /**
     * FinalGuidanceSytem of ILS/MLS/GLS Category I
     * 
     */
    @XmlEnumValue("CatI")
    CAT_I("CatI"),

    /**
     * FinalGuidanceSytem of ILS/MLS/GLS Category II
     * 
     */
    @XmlEnumValue("CatII")
    CAT_II("CatII"),

    /**
     * FinalGuidanceSytem of ILS/MLS/GLS Category III
     * 
     */
    @XmlEnumValue("CatIII")
    CAT_III("CatIII"),

    /**
     * FinalGuidanceSytem of ILS/MLS/GLS Category IIIA
     * 
     */
    @XmlEnumValue("CatIIIA")
    CAT_IIIA("CatIIIA"),

    /**
     * FinalGuidanceSytem of ILS/MLS/GLS Category IIIB
     * 
     */
    @XmlEnumValue("CatIIIB")
    CAT_IIIB("CatIIIB"),

    /**
     * FinalGuidanceSytem of ILS/MLS/GLS Category IIIC
     * 
     */
    @XmlEnumValue("CatIIIC")
    CAT_IIIC("CatIIIC"),

    /**
     * FinalGuidanceSytem for an approach with LDA/GS or SDF/GS and this landing minima is applicable when the glide slope component is in-service
     * 
     */
    @XmlEnumValue("GlideSlope")
    GLIDE_SLOPE("GlideSlope"),

    /**
     * FinalGuidanceSytem for an approach with ILS, LDA/GS or SDF/GS and this landing minima is applicable when the glide slope component is out-of-service
     * 
     */
    @XmlEnumValue("GlideSlopeOut")
    GLIDE_SLOPE_OUT("GlideSlopeOut"),

    /**
     * RNAV FinalGuidanceSytem of LNAV
     * 
     */
    @XmlEnumValue("Lnav")
    LNAV("Lnav"),

    /**
     * RNAV FinalGuidanceSytem of LNAV/VNAV
     * 
     */
    @XmlEnumValue("LnavVnav")
    LNAV_VNAV("LnavVnav"),

    /**
     * RNAV FinalGuidanceSytem of LP
     * 
     */
    @XmlEnumValue("Lp")
    LP("Lp"),

    /**
     * RNAV FinalGuidanceSytem of LPV
     * 
     */
    @XmlEnumValue("Lpv")
    LPV("Lpv"),

    /**
     * FinalGuidanceSytem of ILS/GLS Category I which requires Special Authorization (SA)
     * 
     */
    @XmlEnumValue("SaCatI")
    SA_CAT_I("SaCatI"),

    /**
     * FinalGuidanceSytem of ILS/GLS SA Category II which requires Special Authorization (SA)
     * 
     */
    @XmlEnumValue("SaCatII")
    SA_CAT_II("SaCatII"),

    /**
     * ILS/MLS/GLS procedure where the final guidance facility is sourced as uncatorized
     * 
     */
    @XmlEnumValue("Uncategorized")
    UNCATEGORIZED("Uncategorized");
    private final String value;

    FinalGuidanceSystem(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FinalGuidanceSystem fromValue(String v) {
        for (FinalGuidanceSystem c: FinalGuidanceSystem.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
