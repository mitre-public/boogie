package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.isNull;

import java.util.function.BiFunction;

import com.google.common.collect.Range;

public final class LimitsToRange implements BiFunction<Double, Double, Range<Double>> {
  public static final LimitsToRange INSTANCE = new LimitsToRange();
  private LimitsToRange(){}
  @Override
  public Range<Double> apply(Double aDouble, Double aDouble2) {
    if (isNull(aDouble) && isNull(aDouble2)) {
      return Range.all();
    }

    if (isNull(aDouble)) {
      return Range.atMost(aDouble2);
    }

    if (isNull(aDouble2)) {
      return Range.atLeast(aDouble);
    }

    return Range.closed(aDouble, aDouble2);
  }
}
