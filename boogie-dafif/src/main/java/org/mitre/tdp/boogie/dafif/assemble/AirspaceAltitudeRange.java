package org.mitre.tdp.boogie.dafif.assemble;

import java.util.Optional;
import java.util.function.BiFunction;

import com.google.common.collect.Range;

/**
 * Converts DAFIF airspace limits into feet MSL. Flight levels are expressed as hundreds of feet.
 * Limits relative to terrain (AGL, GND and SURFACE), unknown limits and limits set by NOTAM leave
 * that side of the range unbounded because no terrain elevation is available.
 */
public final class AirspaceAltitudeRange implements BiFunction<String, String, Range<Double>> {

  public static final AirspaceAltitudeRange INSTANCE = new AirspaceAltitudeRange();

  private AirspaceAltitudeRange() {
  }

  @Override
  public Range<Double> apply(String lowerAltitude, String upperAltitude) {
    Optional<Double> lower = altitude(lowerAltitude);
    Optional<Double> upper = altitude(upperAltitude);
    if (lower.isPresent() && upper.isPresent()) {
      return Range.closed(lower.orElseThrow(), upper.orElseThrow());
    }
    return lower.map(Range::atLeast)
        .orElseGet(() -> upper.map(Range::atMost).orElseGet(Range::all));
  }

  private Optional<Double> altitude(String value) {
    if (value == null || value.isBlank()) {
      return Optional.empty();
    }
    if (value.matches("FL[0-9]+")) {
      return Optional.of(Double.parseDouble(value.substring(2)) * 100.0);
    }
    if (value.matches("[0-9]+AMSL")) {
      return Optional.of(Double.parseDouble(value.substring(0, value.length() - 4)));
    }
    if (value.matches("[0-9]+AGL") || switch (value) {
      case "GND", "SURFACE", "U", "UNLTD", "BY NOTAM" -> true;
      default -> false;
    }) {
      return Optional.empty();
    }
    throw new IllegalArgumentException("Unsupported DAFIF airspace altitude: " + value);
  }
}
