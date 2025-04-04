package org.mitre.tdp.boogie.arinc.v21.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;
/**
 * This field provides information on the type of heliport.
 */
public enum HeliportType implements FieldSpec<HeliportType> {
  SPEC,
  /**
   * Hospital
   */
  H,
  /**
   * Oil Rig
   */
  O,
  /**
   * Other and/or Unknown
   */
  U;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.305";
  }

  public static Set<String> VALID = Arrays.stream(HeliportType.values())
      .filter(i -> !SPEC.equals(i))
      .map(Enum::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<HeliportType> apply(String s) {
    return Optional.ofNullable(s).filter(i -> VALID.contains(i)).map(HeliportType::valueOf);
  }
}
