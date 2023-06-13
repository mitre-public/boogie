package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum ApplicationType implements FieldSpec<ApplicationType> {
  SPEC,
  /**
   * Standard 424 continuation containing notes or other formatted text
   */
  A,
  /**
   * Combined Controlling Agency/Call Sign and formatted Time of Operation
   */
  B,
  /**
   * Call Sign/Controlling Agency Continuation
   */
  C,
  /**
   * Primary Record Extension
   */
  E,
  /**
   * VHF Navaid/TACAN only Navaid Limitation Continuation
   */
  L,
  /**
   * A Sector Narrative Continuation
   */
  N,
  /**
   * A Time of Operations Continuation, formatted time data
   */
  T,
  /**
   * A time of Operations Continuation, Narrative time data
   */
  U,
  /**
   * A Time of Operations Continuation, Start/End Date
   */
  V,
  /**
   * A Flight Planning Application Continuation
   */
  P,
  /**
   * Simulation Application Continuation
   */
  S,
  /**
   * An Airport or Heliport Procedure Data Continuation
   */
  W;

  private static final Set<String> validNames = Arrays.stream(ApplicationType.values())
      .filter(d -> !SPEC.equals(d))
      .map(ApplicationType::name)
      .collect(Collectors.toSet());

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.91";
  }

  @Override
  public Optional<ApplicationType> apply(String s) {
    return Optional.of(s).filter(validNames::contains).map(ApplicationType::valueOf);
  }
}
