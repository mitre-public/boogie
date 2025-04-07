package org.mitre.tdp.boogie.arinc.v21.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The Pad Dimensions field defines the landing surface
 * dimensions of the helicopter landing pad. The pad may be described as a runway, a
 * rectangle or a circle.
 * ARINC SPECIFICATION 424 – Page 222
 * 5.0 NAVIGATION DATA – FIELD DEFINITIONS
 * Source/Content: Pad dimensions will be derived from official government sources
 * and entered into the field in feet with a resolution of one foot.
 * When the pad is rectangular, the first five digits define one side of the landing pad
 * and the last three digits the other side of the pad, e.g., 00060120 indicates the pad
 * is 60 feet by 120 feet.
 * When the pad is circular, the first five digits define the diameter of the pad and the
 * last three digits will be zeros, e.g., 00080000 indicates a pad that is 80 feet in
 * diameter.
 * When the pad is a runway, the first five digits define the length of the pad and the
 * last three digits the width of the pad, e.g., 12500120 indicates a pad that is 12500
 * feet long and 120 feet wide.
 */
public final class PadDimensions implements FieldSpec<String> {
  @Override
  public int fieldLength() {
    return 8;
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
