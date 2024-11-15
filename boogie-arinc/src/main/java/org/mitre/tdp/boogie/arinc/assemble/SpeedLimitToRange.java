package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.function.BiFunction;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimitDescription;

import com.google.common.collect.Range;

/**
 * This conversion covers all currently known cases:
 * <br>
 * AT
 * AT_OR_ABOVE
 * AT_OR_BELOW
 * <br>
 * For contextual info see {@link SpeedLimitDescription}.
 * <br>
 * If no speedLimit value is provided this class will map the range to "unconstrained" regardless of the descriptor.
 */
public final class SpeedLimitToRange implements BiFunction<String, Double, Range<Double>> {

  public static final SpeedLimitToRange INSTANCE = new SpeedLimitToRange();

  private SpeedLimitToRange() {
  }

  @Override
  public Range<Double> apply(String speedLimitDescription, @Nullable Double speedLimit) {
    requireNonNull(speedLimitDescription, "Description code for speed limits ");

    if (speedLimit == null) {
      return Range.all();
    }

    if (speedLimitDescription.trim().isEmpty() || "@".equals(speedLimitDescription)) {
      return Range.closed(speedLimit, speedLimit);
    } else if ("+".equals(speedLimitDescription)) {
      return Range.atLeast(speedLimit);
    } else if ("-".equals(speedLimitDescription)) {
      return Range.atMost(speedLimit);
    } else {
      throw new IllegalArgumentException("Unknown SpeedLimitDescription: ".concat(speedLimitDescription));
    }
  }
}
