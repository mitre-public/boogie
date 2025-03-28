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
public enum SidQualifier1 implements FieldSpec<SidQualifier1> {
  SPEC,
  /**
   * DME Required
   */
  D,
  /**
   * GNSS Required
   */
  G,
  /**
   * Radar Required
   */
  R,
  /**
   * Helicopter SID from Runway
   */
  H,
  /**
   * Point-in-Space (PinS) SID
   */
  P;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a-SID";
  }

  public static final Set<String> VALID = Arrays.stream(SidQualifier1.values())
      .filter(d -> !SPEC.equals(d))
      .map(SidQualifier1::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<SidQualifier1> apply(String s) {
    return Optional.of(s).filter(VALID::contains).map(SidQualifier1::valueOf);
  }
}
