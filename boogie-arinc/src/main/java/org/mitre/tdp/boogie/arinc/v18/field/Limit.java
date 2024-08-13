package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;
import java.util.Set;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.AltitudeFlightLevelParser;

import com.google.common.collect.ImmutableSet;

/**
 * Special Use Airspace is described by both lateral and vertical boundaries. The Lower/Upper Limit fields
 * contain the lower and upper limits of the FIR/UIR or Restrictive Airspace being described.
 *
 * Limits for the special use airspace should be derived from official government sources. The field may contain
 * altitude (all numerics), flight levels (alpha/numerics) or an all alpha entry (see examples). The flight
 * level entry will contain the alpha characters FL followed by the altitude in hundreds of feet. These fields
 * will be entered on the first record only of each FIR/UIR or Restrictive Airspace being described.
 *
 * Examples:
 * All Numeric:
 * 05000, 25000
 *
 * Alpha/Numeric:
 * FL245, FL450
 *
 * All Alpha:
 * NOTSP (for Not Specified)
 * UNLTD (for unlimited)
 * GND (for Ground)
 * MSL (for Mean Sea Level)
 * NOTAM (for Restrictive Airspace only)
 */

public final class Limit implements FieldSpec<Double> {
  private static final Set<String> STRINGS = ImmutableSet.of("NOTSP", "UNLTD", "GND", "MSL", "NOTAM");
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.121";
  }

  @Override
  public Optional<Double> apply(String string) {
    return Optional.ofNullable(string)
        .filter(s -> !STRINGS.contains(s))
        .flatMap(AltitudeFlightLevelParser.INSTANCE);

  }
}
