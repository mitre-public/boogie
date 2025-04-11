
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SurfaceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="SurfaceType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Asphalt"/&gt;
 *     &lt;enumeration value="AsphaltAndGrass"/&gt;
 *     &lt;enumeration value="BituminousTarOrAsphalt"/&gt;
 *     &lt;enumeration value="BrickIsLaidOrMortared"/&gt;
 *     &lt;enumeration value="Clay"/&gt;
 *     &lt;enumeration value="Concrete"/&gt;
 *     &lt;enumeration value="ConcreteAndAsphalt"/&gt;
 *     &lt;enumeration value="ConcreteAndGrass"/&gt;
 *     &lt;enumeration value="Coral"/&gt;
 *     &lt;enumeration value="Dirt"/&gt;
 *     &lt;enumeration value="Grass"/&gt;
 *     &lt;enumeration value="Gravel"/&gt;
 *     &lt;enumeration value="Ice"/&gt;
 *     &lt;enumeration value="Laterite"/&gt;
 *     &lt;enumeration value="Macadam"/&gt;
 *     &lt;enumeration value="LandingMat"/&gt;
 *     &lt;enumeration value="ProtectiveLaminate"/&gt;
 *     &lt;enumeration value="Metal"/&gt;
 *     &lt;enumeration value="Mix"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *     &lt;enumeration value="Paved"/&gt;
 *     &lt;enumeration value="PiercedSteelPlanking"/&gt;
 *     &lt;enumeration value="Sand"/&gt;
 *     &lt;enumeration value="Sealed"/&gt;
 *     &lt;enumeration value="Silt"/&gt;
 *     &lt;enumeration value="Snow"/&gt;
 *     &lt;enumeration value="Soil"/&gt;
 *     &lt;enumeration value="Stone"/&gt;
 *     &lt;enumeration value="Tarmac"/&gt;
 *     &lt;enumeration value="Treated"/&gt;
 *     &lt;enumeration value="Turf"/&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *     &lt;enumeration value="Unpaved"/&gt;
 *     &lt;enumeration value="Water"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SurfaceType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum SurfaceType {

    @XmlEnumValue("Asphalt")
    ASPHALT("Asphalt"),
    @XmlEnumValue("AsphaltAndGrass")
    ASPHALT_AND_GRASS("AsphaltAndGrass"),

    /**
     * Bituminous tar or asphalt and/or oil or bitumen bound, mix-in-place surfaces (often referred to as “earth cement”)
     * 
     */
    @XmlEnumValue("BituminousTarOrAsphalt")
    BITUMINOUS_TAR_OR_ASPHALT("BituminousTarOrAsphalt"),
    @XmlEnumValue("BrickIsLaidOrMortared")
    BRICK_IS_LAID_OR_MORTARED("BrickIsLaidOrMortared"),
    @XmlEnumValue("Clay")
    CLAY("Clay"),
    @XmlEnumValue("Concrete")
    CONCRETE("Concrete"),
    @XmlEnumValue("ConcreteAndAsphalt")
    CONCRETE_AND_ASPHALT("ConcreteAndAsphalt"),
    @XmlEnumValue("ConcreteAndGrass")
    CONCRETE_AND_GRASS("ConcreteAndGrass"),
    @XmlEnumValue("Coral")
    CORAL("Coral"),
    @XmlEnumValue("Dirt")
    DIRT("Dirt"),
    @XmlEnumValue("Grass")
    GRASS("Grass"),
    @XmlEnumValue("Gravel")
    GRAVEL("Gravel"),
    @XmlEnumValue("Ice")
    ICE("Ice"),
    @XmlEnumValue("Laterite")
    LATERITE("Laterite"),
    @XmlEnumValue("Macadam")
    MACADAM("Macadam"),

    /**
     * Usually made of aluminium
     * 
     */
    @XmlEnumValue("LandingMat")
    LANDING_MAT("LandingMat"),

    /**
     * Usually made of rubber
     * 
     */
    @XmlEnumValue("ProtectiveLaminate")
    PROTECTIVE_LAMINATE("ProtectiveLaminate"),

    /**
     * Steel or Aluminium
     * 
     */
    @XmlEnumValue("Metal")
    METAL("Metal"),

    /**
     * Non bituminous mix
     * 
     */
    @XmlEnumValue("Mix")
    MIX("Mix"),
    @XmlEnumValue("Other")
    OTHER("Other"),

    /**
     * generic hard surface
     * 
     */
    @XmlEnumValue("Paved")
    PAVED("Paved"),
    @XmlEnumValue("PiercedSteelPlanking")
    PIERCED_STEEL_PLANKING("PiercedSteelPlanking"),
    @XmlEnumValue("Sand")
    SAND("Sand"),
    @XmlEnumValue("Sealed")
    SEALED("Sealed"),
    @XmlEnumValue("Silt")
    SILT("Silt"),
    @XmlEnumValue("Snow")
    SNOW("Snow"),
    @XmlEnumValue("Soil")
    SOIL("Soil"),
    @XmlEnumValue("Stone")
    STONE("Stone"),
    @XmlEnumValue("Tarmac")
    TARMAC("Tarmac"),
    @XmlEnumValue("Treated")
    TREATED("Treated"),
    @XmlEnumValue("Turf")
    TURF("Turf"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown"),
    @XmlEnumValue("Unpaved")
    UNPAVED("Unpaved"),
    @XmlEnumValue("Water")
    WATER("Water");
    private final String value;

    SurfaceType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SurfaceType fromValue(String v) {
        for (SurfaceType c: SurfaceType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
