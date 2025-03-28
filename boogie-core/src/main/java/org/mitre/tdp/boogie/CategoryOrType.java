package org.mitre.tdp.boogie;


import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This field provides an aircraft category or type with the categories defined by ARINC 424 Section 5.301
 * Nobody actually knows how they map to other definitions of these terms.
 */
public enum CategoryOrType {
  CAT_A,
  CAT_B,
  CAT_C,
  CAT_D,
  CAT_E,
  CAT_H,
  JET,
  PISTON,
  TURBOJET,
  TURBOPROP,
  PROP,
  NOT_SPECIFIED;
  public static final Set<String> VALID = Arrays.stream(CategoryOrType.values()).map(CategoryOrType::name).collect(Collectors.toSet());
}
