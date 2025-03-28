package org.mitre.tdp.boogie.arinc.v21.field.qualifiers;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Starting in -21 the qualifiers became a major tool to organize instrument flight procedures.
 * They were organized by 3 categories.
 */
public enum SidQualifier2 implements FieldSpec<SidQualifier2> {
  SPEC,
  /**
   * RNAV PBN Nav Spec
   * <br>
   * RNAV Departures designed and published based upon an ICAO PBN RNAV Navigation Specification
   * will be coded with a qualifier 3 Z, Y, X, B, P, or U.
   * RNAV Departures not based upon PBN will be coded with a qualifier 3 U or V.
   */
  D,
  /**
   * RNP PHN Nav Spec
   * <br>
   * Departure Procedures designed and published based upon ICAO PBN RNP Navigation Specification, Qualifier 3
   * must be coded with D,E,F,A,G, or U;
   */
  E,
  /**
   * FMS Required
   */
  F,
  /**
   * Conventional Departures
   */
  G,
  /**
   * PinS Departure - Proceed Visually
   * <br>
   * Implied that Data Base Supported RNAV is required. Qualifier W and X can be used in conjunction with
   * Qualifier 1 set to P and SID route type 1, 2, or 3. Qualifier 2 to be set to D when procedure chart is not
   * annotated with Proceed Visually or Proceed VFR.
   */
  W,
  /**
   * PinS Departure - Procedure VFR
   * <br>
   * Implied that Data Base Supported RNAV is required. Qualifier W and X can be used in conjunction with
   * Qualifier 1 set to P and SID route type 1, 2, or 3. Qualifier 2 to be set to D when procedure chart is not
   * annotated with Proceed Visually or Proceed VFR.
   */
  X;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a-SID";
  }

  public static final Set<String> VALID = Arrays.stream(SidQualifier2.values())
      .filter(d -> !SPEC.equals(d))
      .map(SidQualifier2::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<SidQualifier2> apply(String s) {
    return Optional.of(s).filter(VALID::contains).map(SidQualifier2::valueOf);
  }
}
