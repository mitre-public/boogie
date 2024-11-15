package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Magnetic/True Indicator” field is used to indicate if the “Course From” and “Course To” fields of the Cruise Table record and the “Sector Bearing”
 * fields of the MSA and TAA record are in magnetic or true degrees. It is also used in the Airport Record to indicate that all detail and procedures for
 * that airport are included in the data base with a reference to either Magnetic North or True North. The field is blank in Airport Record when the data
 * base contains a mix of magnetic and true information for the airport.
 */
public enum MagneticTrueIndicator implements FieldSpec<MagneticTrueIndicator> {
  SPEC,
  M,
  T;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.165";
  }

  static final Set<String> enumValues = Arrays.stream(MagneticTrueIndicator.values()).filter(e -> !SPEC.equals(e)).map(Enum::name).collect(Collectors.toSet());

  @Override
  public Optional<MagneticTrueIndicator> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(enumValues::contains).map(MagneticTrueIndicator::valueOf);
  }
}
