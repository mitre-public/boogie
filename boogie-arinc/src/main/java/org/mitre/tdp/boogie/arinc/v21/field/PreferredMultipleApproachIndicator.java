package org.mitre.tdp.boogie.arinc.v21.field;

import java.util.Optional;
import java.util.Set;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Preferred Multiple Approach Indicator is used to identify the multiple approach that is generally considered to be the most likely one to be utilized/needed
 * when there are only multiple approaches available for a given approach type at a runway.
 * This will be defined on the Approach FAF record in the Final Approach.
 * For a given approach type at a runway, there shall be one and only one Primary Multiple Approach Indicator provided.
 * <br>
 * A P in this field on the approach final FAF record indicates the approach is the preferred multiple approach and can be given priority during data packing, if desired.
 * A blank on the approach final FAF record will be interpreted that the approach is not the preferred multiple approach.
 */
public final class PreferredMultipleApproachIndicator implements FieldSpec<String> {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.306";
  }

  private static final Set<String> VALID = Set.of("P");

  @Override
  public Optional<String> apply(String s) {
    return Optional.ofNullable(s).filter(VALID::contains);
  }
}
