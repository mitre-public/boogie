package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Pad Dimensions” field
 * defines the landing surface dimensions of the helicopter
 * landing pad. The pad may be described as a rectangle or as
 * a circle.
 * <p>
 * Pad dimensions will be derived from
 * official government sources and entered into the field in feet
 * with a resolution of one foot. When the pad is rectangular,
 * the first three digits define one side of the landing pad and
 * the last three digits the other side of the pad, e.g. “060120”
 * indicates the pad is 60 feet by 120 feet. When the pad is
 * circular, the first three digits define the diameter of the pad
 * and the last three digits will be zeros, e.g., “080000”
 * indicates a pad that is 80 feet in diameter.
 */
public final class PadDimensions implements FieldSpec<String> {
  @Override
  public int fieldLength() {
    return 6;
  }

  @Override
  public String fieldCode() {
    return "5.176";
  }

  @Override
  public Optional<String> apply(String s) {
    return Optional.ofNullable(s);
  }
}
