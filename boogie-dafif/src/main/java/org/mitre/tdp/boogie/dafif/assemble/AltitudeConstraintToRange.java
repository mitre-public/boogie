package org.mitre.tdp.boogie.dafif.assemble;

import java.util.Optional;

import org.mitre.tdp.boogie.fn.TriFunction;

import com.google.common.collect.Range;

public final class AltitudeConstraintToRange implements TriFunction<String, Double, Double, Range<Double>> {
  public static final AltitudeConstraintToRange INSTANCE = new AltitudeConstraintToRange();
  private AltitudeConstraintToRange() {}
  @Override
  public Range<Double> apply(String altDesc, Double alt1, Double alt2) {
    return switch (altDesc) {
      case "+" -> Range.atLeast(alt1);
      case "-" -> Range.atMost(alt1);
      case "B" -> Range.closed(alt2, alt1);
      case "" -> Optional.ofNullable(alt1).map(Range::singleton).orElse(Range.all());
      default -> Range.all();
    };
  }
}
