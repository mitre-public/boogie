package org.mitre.tdp.boogie.arinc.v18.field;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec2;

import com.google.common.collect.ImmutableSet;

/**
 * The “Altitude Description” field will designate whether a waypoint should be crossed “at,” “at or above,” “at or below” or “at or above to at
 * or below” specified altitudes. The field is also used to designate recommended altitudes and cases where two distinct altitudes are provided
 * at a single fix.
 */
public final class AltitudeDescription implements FieldSpec2<String> {

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
    return Optional.of(fieldValue).map(String::trim).filter(s -> !s.isEmpty()).filter(allowedValues::contains);
  }

  /**
   * The list of allowed values for the field - refer to the ARINC spec for interpretation.
   */
  static final ImmutableSet<String> allowedValues = ImmutableSet.copyOf(newHashSet(
      "+",
      "-",
      "@",
      "B",
      "C",
      "G",
      "H",
      "I",
      "J",
      "V",
      "X",
      "Y"));
}
