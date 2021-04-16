package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import java.util.Collections;
import java.util.List;

/**
 * The “Maximum Altitude” field is used to indicate the maximum altitude allowed.
 */
public class MaxAltitude implements AltitudeFlightLevel {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.127";
  }

  @Override
  public Double parseValue(String fieldString) {
    checkSpec(this, fieldString, !specialAltitudeCodes().contains(fieldString));
    return AltitudeFlightLevel.super.parseValue(fieldString);
  }

  @Override
  public boolean filterInput(String fieldString) {
    return AltitudeFlightLevel.super.filterInput(fieldString) || specialAltitudeCodes().contains(fieldString);
  }

  /**
   * There are two special codes for the {@link MinimumAltitude} which don't directly parse to altitude values - these are
   * explicitly laid out in the ARINC spec and generally show up uncommonly.
   */
  public List<String> specialAltitudeCodes() {
    return Collections.singletonList("UNLTD");

  }
}
