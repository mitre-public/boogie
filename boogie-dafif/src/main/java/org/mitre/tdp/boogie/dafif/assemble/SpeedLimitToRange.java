package org.mitre.tdp.boogie.dafif.assemble;

import java.util.function.Function;

import com.google.common.collect.Range;

/**
 * Converts a speed limit to a range of speeds. Dafif just has limits :(
 */
public final class SpeedLimitToRange implements Function<Double, Range<Double>> {
  public static final SpeedLimitToRange INSTANCE = new SpeedLimitToRange();
  private SpeedLimitToRange() {}
  public static SpeedLimitToRange instance() { return INSTANCE; }
  @Override
  public Range<Double> apply(Double aDouble) {
    return Range.atMost(aDouble);
  }
}
