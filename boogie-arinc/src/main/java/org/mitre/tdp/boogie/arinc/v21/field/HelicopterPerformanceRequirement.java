package org.mitre.tdp.boogie.arinc.v21.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum HelicopterPerformanceRequirement implements FieldSpec<HelicopterPerformanceRequirement> {
  SPEC,
  /**
   * Multi-engine required
   */
  M,
  /**
   * Single-engine only
   */
  S,
  /**
   * Unknown
   */
  U;


  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.310";
  }

  public static Set<String> VALID = Arrays.stream(HelicopterPerformanceRequirement.values())
      .filter(i -> !SPEC.equals(i))
      .map(HelicopterPerformanceRequirement::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<HelicopterPerformanceRequirement> apply(String s) {
    return Optional.ofNullable(s).filter(VALID::contains).map(HelicopterPerformanceRequirement::valueOf);
  }
}
