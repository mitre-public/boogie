package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Longest Runway Surface Code” field is used to define whether or not there is a hard surface runway at the airport,
 * the length of which is indicated in the Longest Runway field.
 */
public enum LongestRunwaySurfaceCode implements FieldSpec<LongestRunwaySurfaceCode> {
  SPEC,
  /**
   * Hard Surface, for example, asphalt or concrete.
   */
  H,
  /**
   * Soft Surface, for example, gravel, grass or soil.
   */
  S,
  /**
   * Water.
   */
  W,
  /**
   * Undefined, surface material not provided in source.
   */
  U;

  static final Set<String> enumValues = Arrays.stream(LongestRunwaySurfaceCode.values()).filter(e -> !SPEC.equals(e)).map(Enum::name).collect(Collectors.toSet());

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.249";
  }

  @Override
  public Optional<LongestRunwaySurfaceCode> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(enumValues::contains).map(LongestRunwaySurfaceCode::valueOf);
  }
}
