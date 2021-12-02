package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Optional;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.fn.TriFunction;

import com.google.common.collect.Range;

/**
 * Class for mapping a collection of Airway leg-level altitudes to a {@link Range} for simplified use.
 */
public final class AirwayAltitudeRange implements TriFunction<Double, Double, Double, Range<Double>> {

  public static final AirwayAltitudeRange INSTANCE = new AirwayAltitudeRange();

  @Override
  public Range<Double> apply(@Nullable Double minAltitude1, @Nullable Double minAltitude2, @Nullable Double maxAltitude) {

    Optional<Double> lowerAltitude = lowerAltitude(minAltitude1, minAltitude2);
    Optional<Double> upperAltitude = Optional.ofNullable(maxAltitude);

    if (lowerAltitude.isPresent() && upperAltitude.isPresent()) {
      return Range.closed(lowerAltitude.get(), upperAltitude.get());
    } else {
      return lowerAltitude.map(Range::atLeast).orElseGet(() -> upperAltitude.map(Range::atMost).orElseGet(Range::all));
    }
  }

  private Optional<Double> lowerAltitude(@Nullable Double minAltitude1, @Nullable Double minAltitude2) {
    if (minAltitude1 != null && minAltitude2 != null) {
      return Optional.of(Math.min(minAltitude1, minAltitude2));
    }
    return Optional.ofNullable(minAltitude1 != null ? minAltitude1 : minAltitude2);
  }
}
