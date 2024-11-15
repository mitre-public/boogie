package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Localizer/Azimuth Position Reference” field indicates whether the antenna is situated beyond the stop end of the runway,
 * ahead of or beyond the approach end of the runway. The “Back Azimuth Position Reference” field indicates whether the antenna
 * is situated ahead of the approach end of the runway, ahead of or beyond the stop end of the runway.
 * <br>
 * or Localizer and Azimuth positions the field is blank (@) when the antenna is situated beyond the stop end of the runway, it
 * contains a plus (+) sign when the antenna is situated ahead of the approach end of the runway or a minus (-) sign when it is
 * located off to one side of the runway. For Back Azimuth positions the field is blank (@) when the antenna is situated ahead
 * of the approach end of the runway, it contains a plus (+) sign when the antenna is situated beyond the stop end of the runway
 * or a minus (-) sign when it is located off to one side of the runway.
 * <br>
 * The long and the short of it is blank space has a meaning in this field - so we don't trim the inputs.
 */
public final class LocalizerAzimuthPositionReference implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.49";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(s -> s.length() == 1);
  }
}
