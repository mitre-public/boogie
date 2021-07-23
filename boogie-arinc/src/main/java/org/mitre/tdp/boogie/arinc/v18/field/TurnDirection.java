package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum TurnDirection implements FieldSpec<TurnDirection> {
  SPEC,
  /**
   * Left
   */
  L,
  /**
   * Right
   */
  R,
  /**
   * Either
   */
  E;

  private static final Set<String> validNames = Arrays.stream(TurnDirection.values())
      .filter(d -> !SPEC.equals(d))
      .map(TurnDirection::name)
      .collect(Collectors.toSet());

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.20";
  }

  @Override
  public Optional<TurnDirection> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(validNames::contains).map(TurnDirection::valueOf);
  }
}
