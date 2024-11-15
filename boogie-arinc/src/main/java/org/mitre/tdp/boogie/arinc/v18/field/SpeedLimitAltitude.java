package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.AltitudeFlightLevelParser;

/**
 * “Speed Limit Altitude” is the altitude below which speed limits may be imposed.
 * <br>
 * The “Speed Limit Altitude” will be derived from official government sources in feet MSL or FL’s.
 */
public final class SpeedLimitAltitude implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.73";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).flatMap(AltitudeFlightLevelParser.INSTANCE);
  }
}
