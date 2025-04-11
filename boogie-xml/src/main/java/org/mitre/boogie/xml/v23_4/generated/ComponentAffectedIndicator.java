
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ComponentAffectedIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ComponentAffectedIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TacanOrVortacAzimuth"/&gt;
 *     &lt;enumeration value="VorDmeOrVortacAzimuthAndDistance"/&gt;
 *     &lt;enumeration value="VordmeOrDmeDistance"/&gt;
 *     &lt;enumeration value="VortacOrTacanAzimth"/&gt;
 *     &lt;enumeration value="TacanVortacDistance"/&gt;
 *     &lt;enumeration value="VorOrVordmeOrVorAzimuth"/&gt;
 *     &lt;enumeration value="VorOrVordmeOrTacanOrVortacanAzimuth"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ComponentAffectedIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ComponentAffectedIndicator {


    /**
     * TACAN or VORTAC, TACAN azimuth component only affected.
     * 
     */
    @XmlEnumValue("TacanOrVortacAzimuth")
    TACAN_OR_VORTAC_AZIMUTH("TacanOrVortacAzimuth"),

    /**
     * VORDME or VORTAC, both azimuth and distance component affected.
     * 
     */
    @XmlEnumValue("VorDmeOrVortacAzimuthAndDistance")
    VOR_DME_OR_VORTAC_AZIMUTH_AND_DISTANCE("VorDmeOrVortacAzimuthAndDistance"),

    /**
     * VORDME or DME, distance component only affected.
     * 
     */
    @XmlEnumValue("VordmeOrDmeDistance")
    VORDME_OR_DME_DISTANCE("VordmeOrDmeDistance"),

    /**
     * VORTAC or TACAN, TACAN azimuth and distance component affected.
     * 
     */
    @XmlEnumValue("VortacOrTacanAzimth")
    VORTAC_OR_TACAN_AZIMTH("VortacOrTacanAzimth"),

    /**
     * TACAN or VORTAC, distance component affected.
     * 
     */
    @XmlEnumValue("TacanVortacDistance")
    TACAN_VORTAC_DISTANCE("TacanVortacDistance"),

    /**
     * VOR, VORDME or VORDME, VOR azimuth component affected
     * 
     */
    @XmlEnumValue("VorOrVordmeOrVorAzimuth")
    VOR_OR_VORDME_OR_VOR_AZIMUTH("VorOrVordmeOrVorAzimuth"),

    /**
     * VOR, VORDME or TACAN, VOR and TACAN azimuth and distance component affected.
     * 
     */
    @XmlEnumValue("VorOrVordmeOrTacanOrVortacanAzimuth")
    VOR_OR_VORDME_OR_TACAN_OR_VORTACAN_AZIMUTH("VorOrVordmeOrTacanOrVortacanAzimuth");
    private final String value;

    ComponentAffectedIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ComponentAffectedIndicator fromValue(String v) {
        for (ComponentAffectedIndicator c: ComponentAffectedIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
