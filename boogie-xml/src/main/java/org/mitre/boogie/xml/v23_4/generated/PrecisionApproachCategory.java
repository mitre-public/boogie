
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PrecisionApproachCategory.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="PrecisionApproachCategory"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IlsLocOnly"/&gt;
 *     &lt;enumeration value="IlsMlsGlsCat1"/&gt;
 *     &lt;enumeration value="IlsMlsGlsCat2"/&gt;
 *     &lt;enumeration value="IlsMlsGlsCat3"/&gt;
 *     &lt;enumeration value="Igs"/&gt;
 *     &lt;enumeration value="LdaGlideslope"/&gt;
 *     &lt;enumeration value="LdaNoGlideslope"/&gt;
 *     &lt;enumeration value="SdfGlideslope"/&gt;
 *     &lt;enumeration value="SdfNoGlideSlope"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PrecisionApproachCategory", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum PrecisionApproachCategory {


    /**
     * ILS Localizer Only, No Glideslope
     * 
     */
    @XmlEnumValue("IlsLocOnly")
    ILS_LOC_ONLY("IlsLocOnly"),

    /**
     * ILS Localizer/MLS/GLS Category I
     * 
     */
    @XmlEnumValue("IlsMlsGlsCat1")
    ILS_MLS_GLS_CAT_1("IlsMlsGlsCat1"),

    /**
     * ILS Localizer/MLS/GLS Category II
     * 
     */
    @XmlEnumValue("IlsMlsGlsCat2")
    ILS_MLS_GLS_CAT_2("IlsMlsGlsCat2"),

    /**
     * ILS Localizer/MLS/GLS Category III
     * 
     */
    @XmlEnumValue("IlsMlsGlsCat3")
    ILS_MLS_GLS_CAT_3("IlsMlsGlsCat3"),

    /**
     * IGS Facility
     * 
     */
    @XmlEnumValue("Igs")
    IGS("Igs"),

    /**
     * LDA Facility with Glideslope
     * 
     */
    @XmlEnumValue("LdaGlideslope")
    LDA_GLIDESLOPE("LdaGlideslope"),

    /**
     * LDA Facility, no Glideslope
     * 
     */
    @XmlEnumValue("LdaNoGlideslope")
    LDA_NO_GLIDESLOPE("LdaNoGlideslope"),

    /**
     * SDF Facility with Glideslope
     * 
     */
    @XmlEnumValue("SdfGlideslope")
    SDF_GLIDESLOPE("SdfGlideslope"),

    /**
     * SDF Facility, no Glideslope
     * 
     */
    @XmlEnumValue("SdfNoGlideSlope")
    SDF_NO_GLIDE_SLOPE("SdfNoGlideSlope");
    private final String value;

    PrecisionApproachCategory(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PrecisionApproachCategory fromValue(String v) {
        for (PrecisionApproachCategory c: PrecisionApproachCategory.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
