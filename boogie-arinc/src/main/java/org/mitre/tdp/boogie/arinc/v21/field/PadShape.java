package org.mitre.tdp.boogie.arinc.v21.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The Helipad Shape field defines the geometric shape of a
 * helipad as being either circle, runway, or rectangular.
 */
public enum PadShape implements FieldSpec<PadShape> {
  SPEC,
  /**
   * Circle
   */
  C,
  /**
   * Square/Rectangle
   */
  S,
  /**
   * Runway
   */
  R,
  /**
   * Undefined, helipad shape not provided in source
   */
  U;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.303";
  }

  public static final Set<String> VALID = Arrays.stream(PadShape.values())
      .filter(i -> !SPEC.equals(i))
      .map(PadShape::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<PadShape> apply(String s) {
    return Optional.ofNullable(s).filter(VALID::contains).map(PadShape::valueOf);
  }
}
