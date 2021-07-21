package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The Level field defines the airway structure of which the record is an element.
 */
public enum Level implements FieldSpec<Level> {
  SPEC,
  /**
   * All altitudes
   */
  B,
  /**
   * High level airways.
   */
  H,
  /**
   * Low level airways
   */
  L;

  static final Set<String> enumValues = Arrays.stream(Level.values()).filter(e -> !SPEC.equals(e)).map(Enum::name).collect(Collectors.toSet());

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.19";
  }

  @Override
  public Optional<Level> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(enumValues::contains).map(Level::valueOf);
  }
}
