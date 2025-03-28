package org.mitre.tdp.boogie.arinc.v20.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * This field provides the aircraft category(s) or types for which the procedure or portion of a procedure (transition) was designed.
 * This can be aircraft category information or aircraft type information.
 */
public enum ProcedureDesignAircraftCategoryOrType implements FieldSpec<ProcedureDesignAircraftCategoryOrType> {
  SPEC,
  /**
   * Aircraft Category A only
   */
  A,
  /**
   * Aircraft Category N only
   */
  B,
  /**
   * Aircraft Category C only
   */
  C,
  /**
   * Aircraft Category D only
   */
  D,
  /**
   * Aircraft Category E only
   */
  E,
  /**
   * Aircraft Categories A and B only
   */
  F,
  /**
   * Aircraft Categories C and D only
   */
  G,
  /**
   * Aircraft Categories A, B, and C
   */
  I,
  /**
   * Aircraft Categories A, B, C and D only
   */
  J,
  /**
   * Aircraft Categories A, B, C, D, and E only
   */
  K,
  /**
   * Aircraft Categories D adn E only
   */
  L,
  /**
   * Aircraft Category H - (Helicopter) only
   */
  H,
  /**
   * Aircraft Type not limited
   */
  P,
  /**
   * Aircraft Type Turbojet only
   */
  R,
  /**
   * Aircraft type Turboprop only
   */
  S,
  /**
   * Aircraft Type Prop only
   */
  T,
  /**
   * Aircraft Type Turboprop and Prop
   */
  U;

  public static final Set<String> VALID = Arrays.stream(ProcedureDesignAircraftCategoryOrType.values())
      .filter(d -> !SPEC.equals(d))
      .map(ProcedureDesignAircraftCategoryOrType::name)
      .collect(Collectors.toSet());

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.301";
  }

  @Override
  public Optional<ProcedureDesignAircraftCategoryOrType> apply(String string) {
    return Optional.of(string).filter(VALID::contains).map(ProcedureDesignAircraftCategoryOrType::valueOf);
  }
}
