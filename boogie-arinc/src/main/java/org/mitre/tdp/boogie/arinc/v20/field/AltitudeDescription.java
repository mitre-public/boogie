package org.mitre.tdp.boogie.arinc.v20.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import com.google.common.collect.ImmutableSet;

/**
 * The “Altitude Description” field will designate whether a waypoint should be crossed “at,” “at or above,” “at or below” or
 * “at or above to at or below” specified altitudes. The field is also used to designate recommended altitudes and cases where
 * two distinct altitudes are provided at a single fix.
 * <br>
 * <b>Standard procedure altitude limits (e.g. SID/STARS)</b>
 * (+) - “At or above” altitude specified in first “Altitude” field. Also used with Localizer Only Altitude field.
 * (-) - “At or below” altitude specified in first “Altitude” field. Also used with Localizer Only Altitude field.
 * (@) - “At” altitude specified in first “Altitude” field. Also used with Localizer Only Altitude field.
 * ( ) - "At" ^ same as above - blank implies "At".
 * (B) - “At or above to at or below” altitudes specified in the first and second “Altitude” fields.
 * (C) - “At or above” altitude specified in second “Altitude” field.
 * (D) - “At or above” altitude specified in second “Altitude” field. Condition is “which ever is later”, which is operationally equivalent to the condition of “not before.”
 * <br>
 * The “B” entry may only appear in Airport and Heliport SID/STAR/Approach Route, Airport/Enroute/Heliport Communications, VHF
 * Navaid Limitation and Preferred Route Records. The higher value will always appear first in records with two altitude fields,
 * or as the first three digits of the Altitude Limitation field.
 * <br>
 * <b>Approach procedure-specific altitude limits</b>
 * (G) - Glide Slope altitude (MSL) specified in the second “Altitude” field and “at” altitude specified in the first “Altitude”
 * field on the FAF Waypoint in Precision Approach Coding with electronic Glide Slope.
 * (V) - “At” altitude on the coded vertical angle in the second “Altitude” field and “at or above” altitude specified in first
 * “Altitude” field on step-down fix waypoints.
 * (X) - “At” altitude on the coded vertical angle in the second “Altitude” field and “at” altitude specified in the first “Altitude”
 * field on step- down fix waypoints.
 * (Y) - “At” altitude on the coded vertical angle in the second “Altitude” field and “at or below” altitude specified in the first
 * “Altitude” field on step-down fix waypoints.
 * <br>
 * NOTE: This class will remap all blanks to '@'s for ease of use downstream.
 */
public final class AltitudeDescription implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.29";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(allowedValues::contains).map(s -> s.replace(' ', '@'));
  }

  /**
   * The list of allowed values for the field - refer to the ARINC spec for interpretation.
   */
  static final ImmutableSet<String> allowedValues = ImmutableSet.of(
      "+",
      "-",
      "@", // at
      " ", // at
      "B",
      "C",
      "D",
      "G",
      "V",
      "X",
      "Y");
}
