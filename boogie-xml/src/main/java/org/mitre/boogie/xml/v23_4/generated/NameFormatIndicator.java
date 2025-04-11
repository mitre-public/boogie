
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NameFormatIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="NameFormatIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Abeam"/&gt;
 *     &lt;enumeration value="BearingDistance"/&gt;
 *     &lt;enumeration value="AirportName"/&gt;
 *     &lt;enumeration value="Fir"/&gt;
 *     &lt;enumeration value="PhoneticLetterName"/&gt;
 *     &lt;enumeration value="AirportIdent"/&gt;
 *     &lt;enumeration value="LatLong"/&gt;
 *     &lt;enumeration value="MultipleWord"/&gt;
 *     &lt;enumeration value="Navaid"/&gt;
 *     &lt;enumeration value="PublishedFiveLetterName"/&gt;
 *     &lt;enumeration value="PublishedLessThanFiveLetterName"/&gt;
 *     &lt;enumeration value="PublishedMoreThanFiveName"/&gt;
 *     &lt;enumeration value="AptRwyRelated"/&gt;
 *     &lt;enumeration value="Uir"/&gt;
 *     &lt;enumeration value="OfficialFiveLetter"/&gt;
 *     &lt;enumeration value="NoPublishedFiveLetter"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NameFormatIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum NameFormatIndicator {


    /**
     * Abeam a Fix
     * 
     */
    @XmlEnumValue("Abeam")
    ABEAM("Abeam"),

    /**
     * Bearing and Distance Fix
     * 
     */
    @XmlEnumValue("BearingDistance")
    BEARING_DISTANCE("BearingDistance"),

    /**
     * Airport Name as Fix
     * 
     */
    @XmlEnumValue("AirportName")
    AIRPORT_NAME("AirportName"),

    /**
     * FIR Fix
     * 
     */
    @XmlEnumValue("Fir")
    FIR("Fir"),

    /**
     * Phonetic Letter Name Fix
     * 
     */
    @XmlEnumValue("PhoneticLetterName")
    PHONETIC_LETTER_NAME("PhoneticLetterName"),

    /**
     * Airport Ident as Fix
     * 
     */
    @XmlEnumValue("AirportIdent")
    AIRPORT_IDENT("AirportIdent"),

    /**
     * Latitude/Longitude Fix
     * 
     */
    @XmlEnumValue("LatLong")
    LAT_LONG("LatLong"),

    /**
     * Multiple Word as Fix
     * 
     */
    @XmlEnumValue("MultipleWord")
    MULTIPLE_WORD("MultipleWord"),

    /**
     * Navaid Ident as Fix
     * 
     */
    @XmlEnumValue("Navaid")
    NAVAID("Navaid"),

    /**
     * Published Five - Letter - Name Fix
     * 
     */
    @XmlEnumValue("PublishedFiveLetterName")
    PUBLISHED_FIVE_LETTER_NAME("PublishedFiveLetterName"),

    /**
     * Published Name Fix, less than Five Letters
     * 
     */
    @XmlEnumValue("PublishedLessThanFiveLetterName")
    PUBLISHED_LESS_THAN_FIVE_LETTER_NAME("PublishedLessThanFiveLetterName"),

    /**
     * Published Name Fix, more than Five Letters
     * 
     */
    @XmlEnumValue("PublishedMoreThanFiveName")
    PUBLISHED_MORE_THAN_FIVE_NAME("PublishedMoreThanFiveName"),

    /**
     * Apt/Rwy Related Fix (Note 2)
     * 
     */
    @XmlEnumValue("AptRwyRelated")
    APT_RWY_RELATED("AptRwyRelated"),

    /**
     * UIR Fix
     * 
     */
    @XmlEnumValue("Uir")
    UIR("Uir"),

    /**
     * Localizer Marker with official published five-letter identifier
     * 
     */
    @XmlEnumValue("OfficialFiveLetter")
    OFFICIAL_FIVE_LETTER("OfficialFiveLetter"),

    /**
     * Localizer Marker without official published five-letter identifier
     * 
     */
    @XmlEnumValue("NoPublishedFiveLetter")
    NO_PUBLISHED_FIVE_LETTER("NoPublishedFiveLetter");
    private final String value;

    NameFormatIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NameFormatIndicator fromValue(String v) {
        for (NameFormatIndicator c: NameFormatIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
