package org.mitre.tdp.boogie.arinc.v21.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

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
   * Categories B and C only
   */
  M,
  /**
   * Aircraft Categories C, D, and E only
   */
  N,
  /**
   * Aircraft Categories B, C, D, and E only
   */
  O,
  /**
   * Aircraft Type Jets only
   */
  W,
  /**
   * Aircraft Type Non-Jets only
   */
  X,
  /**
   * Aircraft Type Pistons only
   */
  Y,
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
  U,
  /**
   * Aircraft Type Non-Turbojets only
   */
  V;

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