package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;

/**
 * An airspace's published vertical extent: its lower and upper {@link AltitudeLimit}, each measured from its own
 * reference or a {@link AltitudeLimit.Special} value.
 *
 * @param lower the lower limit
 * @param upper the upper limit
 */
public record AltitudeLimits(AltitudeLimit lower, AltitudeLimit upper) {

  /**
   * Limits not specified on either side.
   */
  private static final AltitudeLimits UNKNOWN = new AltitudeLimits(AltitudeLimit.notSpecified(), AltitudeLimit.notSpecified());

  /**
   * Validates the limits.
   *
   * @param lower the lower limit
   * @param upper the upper limit
   * @throws NullPointerException if either limit is null
   * @throws IllegalArgumentException if both are heights in the same reference and the lower exceeds the upper
   */
  public AltitudeLimits {
    requireNonNull(lower, "lower");
    requireNonNull(upper, "upper");
    if (contradictory(lower, upper)) {
      throw new IllegalArgumentException("Lower altitude must not exceed upper altitude: " + lower + " > " + upper);
    }
  }

  /**
   * Creates limits.
   *
   * @param lower the lower limit
   * @param upper the upper limit
   * @return the limits
   * @throws IllegalArgumentException if both are heights in the same reference and the lower exceeds the upper
   */
  public static AltitudeLimits of(AltitudeLimit lower, AltitudeLimit upper) {
    return new AltitudeLimits(lower, upper);
  }

  /**
   * Creates limits from a source's published values, which may contradict each other. Heights in the same reference
   * whose lower is above their upper cannot both be right, so neither side is known.
   *
   * @param lower the published lower limit
   * @param upper the published upper limit
   * @return the limits, or {@link #unknown()} if they contradict each other
   */
  public static AltitudeLimits published(AltitudeLimit lower, AltitudeLimit upper) {
    return contradictory(requireNonNull(lower, "lower"), requireNonNull(upper, "upper")) ? UNKNOWN : new AltitudeLimits(lower, upper);
  }

  /**
   * Returns limits the source does not specify on either side.
   *
   * @return the unknown limits
   */
  public static AltitudeLimits unknown() {
    return UNKNOWN;
  }

  /**
   * Tells whether both sides are known.
   *
   * @return true if the source says where both limits are, or that a side has none
   */
  public boolean known() {
    return lower.known() && upper.known();
  }

  /**
   * Tells whether two limits cannot both be right: heights in the same reference with the lower above the upper.
   * Heights in different references can only be ordered at a given terrain elevation or atmosphere, so they never
   * contradict.
   *
   * @param lower the lower limit
   * @param upper the upper limit
   * @return true if they contradict each other
   */
  private static boolean contradictory(AltitudeLimit lower, AltitudeLimit upper) {
    return lower instanceof AltitudeLimit.Height low && upper instanceof AltitudeLimit.Height high
        && low.reference() == high.reference() && low.feet() > high.feet();
  }
}
