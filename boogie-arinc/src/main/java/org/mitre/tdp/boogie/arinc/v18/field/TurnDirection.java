package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.SingleCharacterLookup;

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

  private static final SingleCharacterLookup<TurnDirection> PARSED_VALUES =
      SingleCharacterLookup.of(validNames, TurnDirection::valueOf);

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.20";
  }

  @Override
  public Optional<TurnDirection> parse(String source, int startOffset, int endOffset) {
    return PARSED_VALUES.parse(source, startOffset, endOffset);
  }
}
