package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.AltitudeFlightLevelParser;

/**
 * The “Altitude/Minimum Altitude” field indicates the reference altitude associated with (1) Enroute Airways (MEA, MFA or
 * other minimum altitudes as defined by source), (2) holding pattern path of Holding Pattern record, (3) altitudes at fixes
 * in terminal procedures and terminal procedure path termination defined by the Path Terminator in the Airport or Heliport
 * SID/STAR/Approach Record and (4) lowest altitude of the “blocked altitudes” for a Preferred Route.
 * <br>
 * e.g. 05000, FL050, 18000, FL180 00600, -0012, 29000, FL290, UNKNN or NESTB (the last two on Enroute Airways only)
 */
public final class MinimumAltitude implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.30";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .map(String::trim)
        // min altitude unknown
        .filter(s -> !"UNKNN".equalsIgnoreCase(s))
        // min altitude weird
        .filter(s -> !"NESTB".equalsIgnoreCase(s))
        .flatMap(AltitudeFlightLevelParser.INSTANCE);
  }
}
