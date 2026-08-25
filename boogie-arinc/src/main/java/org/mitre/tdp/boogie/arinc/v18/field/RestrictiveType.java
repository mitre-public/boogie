package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The Restrictive Airspace Type field is used to indicate the
 * type of Airspace in which the flight of aircraft is prohibited or restricted. The
 * restriction may be continuous or specified for certain times.
 */
public enum RestrictiveType implements FieldSpec<RestrictiveType> {
  SPEC,
  /**
   * Alert
   */
  A,
  /**
   * Caution
   */
  C,
  /**
   * Danger
   */
  D,
  /**
   * Military Operations Area
   */
  M,
  /**
   * Prohibited
   */
  P,
  /**
   * Restricted
   */
  R,
  /**
   * Training
   */
  T,
  /**
   * Warning
   */
  W,
  /**
   * Unspecified or Unknown
   */
  U
  ;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.128";
  }

  public static final Set<String> VALID = Arrays.stream(RestrictiveType.values())
      .filter(d -> !SPEC.equals(d))
      .map(RestrictiveType::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<RestrictiveType> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .filter(VALID::contains)
        .map(RestrictiveType::valueOf);
  }
}
