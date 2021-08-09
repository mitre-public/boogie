package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import org.mitre.tdp.boogie.arinc.v18.field.AltitudeDescription;
import org.mitre.tdp.boogie.fn.TriFunction;

import com.google.common.collect.Range;

/**
 * This conversion is lossy - generally speaking for modeling it is sufficient to cover the most common cases:
 * <br>
 * AT
 * AT_OR_ABOVE
 * AT_OR_BELOW
 * AT_OR_ABOVE_TO_AT_OR_BELOW
 * <br>
 * For the full set of possibilities and interpretations see {@link AltitudeDescription}.
 * <br>
 * For unsupported description codes this class returns {@link Range#all()}.
 */
public final class AltitudeLimitToRange implements TriFunction<String, Double, Double, Range<Double>> {

  public static final AltitudeLimitToRange INSTANCE = new AltitudeLimitToRange();

  private AltitudeLimitToRange() {
  }

  @Override
  public Range<Double> apply(String altitudeDescription, @Nullable Double altitude1, @Nullable Double altitude2) {
    requireNonNull(altitudeDescription, "The description code must be provided to interpret the provided altitude values.");

    if ("+".equals(altitudeDescription) && altitude1 != null) {
      return Range.atLeast(altitude1);
    } else if ("-".equals(altitudeDescription) && altitude1 != null) {
      return Range.atMost(altitude1);
    } else if ("@".equals(altitudeDescription) && altitude1 != null) {
      return Range.closed(altitude1, altitude1);
    } else if ("B".equals(altitudeDescription) && altitude1 != null && altitude2 != null) {
      // as per the spec the higher value appears FIRST and the lower SECOND
      return Range.closed(altitude2, altitude1);
    } else if ("C".equals(altitudeDescription) && altitude2 != null) {
      return Range.atLeast(altitude2);
    } else {
      return Range.all();
    }
  }
}
