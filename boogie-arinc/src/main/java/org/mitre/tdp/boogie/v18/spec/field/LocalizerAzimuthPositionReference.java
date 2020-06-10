package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The “Localizer/Azimuth Position Reference” field indicates whether the antenna is situated beyond the stop end of the runway,
 * ahead of or beyond the approach end of the runway. The “Back Azimuth Position Reference” field indicates whether the antenna
 * is situated ahead of the approach end of the runway, ahead of or beyond the stop end of the runway.
 */
public class LocalizerAzimuthPositionReference implements FreeFormString {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.49";
  }
}
