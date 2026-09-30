package org.mitre.tdp.boogie.airspace;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * An explicit vertical constraint in feet, including whether the constraint has been resolved.
 *
 * <p>Callers must resolve limits and trajectory altitudes to the same altitude reference before using this class.
 * It does not convert ground-relative heights or flight levels to MSL. A single missing numeric bound leaves that side
 * unlimited. When both numeric bounds are missing, {@link #of(Double, Double)} returns {@link #unknown()};
 * callers can deliberately choose {@link #unbounded()} when no vertical restriction applies.
 *
 * <p>A limit whose altitude reference is explicitly unresolved, such as a terrain-relative height without terrain data,
 * requires resolution before comparison. Callers should represent that condition with {@link #unknown()}.
 *
 * <p>Bounds are inclusive. Unknown bands never establish definite vertical membership; callers can inspect {@link #status()}
 * to distinguish an unresolved constraint from a resolved constraint which excludes an altitude.
 */
public final class AltitudeBand {

  /** Whether the vertical constraint is unknown, explicitly unlimited, or has numeric limits. */
  public enum Status {
    /** The available information cannot establish definite vertical membership. */
    UNKNOWN,
    /** Every finite altitude is allowed by an explicit caller decision. */
    UNBOUNDED,
    /** At least one numeric limit is resolved; any missing side is unlimited. */
    RESOLVED
  }

  private static final AltitudeBand UNKNOWN = new AltitudeBand(Status.UNKNOWN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
  private static final AltitudeBand UNBOUNDED = new AltitudeBand(Status.UNBOUNDED, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
  private static final Interval FULL_INTERVAL = new Interval(0.0, 1.0);

  private final Status status;
  private final double lowerFeet;
  private final double upperFeet;

  private AltitudeBand(Status status, double lowerFeet, double upperFeet) {
    this.status = status;
    this.lowerFeet = lowerFeet;
    this.upperFeet = upperFeet;
  }

  /**
   * Returns an unresolved constraint, which cannot establish definite vertical membership.
   *
   * @return the unknown band
   */
  public static AltitudeBand unknown() {
    return UNKNOWN;
  }

  /**
   * Returns an explicitly unlimited constraint, which accepts every finite altitude.
   *
   * @return the intentionally unlimited band
   */
  public static AltitudeBand unbounded() {
    return UNBOUNDED;
  }

  /**
   * Creates a band from nullable inclusive numeric limits. One absent limit leaves that side unlimited; two absent limits
   * return {@link #unknown()}. Use {@link #unbounded()} to intentionally allow every altitude.
   *
   * <p>Present limits must be finite and use the same resolved altitude reference as trajectory altitudes.
   *
   * @param lowerFeet inclusive lower altitude in feet, or null for a missing lower limit
   * @param upperFeet inclusive upper altitude in feet, or null for a missing upper limit
   * @return a band constrained by the present limits, or unknown if neither limit is present
   * @throws IllegalArgumentException if a present limit is nonfinite or the lower limit exceeds the upper limit
   */
  public static AltitudeBand of(Double lowerFeet, Double upperFeet) {
    return Optional.ofNullable(lowerFeet)
        .map(lower -> Optional.ofNullable(upperFeet)
            .map(upper -> closed(lower, upper))
            .orElseGet(() -> atLeast(lower)))
        .orElseGet(() -> Optional.ofNullable(upperFeet).map(AltitudeBand::atMost).orElseGet(AltitudeBand::unknown));
  }

  /**
   * Returns a resolved band with inclusive, finite limits.
   *
   * @param lowerFeet inclusive lower altitude in feet
   * @param upperFeet inclusive upper altitude in feet
   * @return a band including every altitude between the limits
   * @throws IllegalArgumentException if a limit is nonfinite or the lower limit exceeds the upper limit
   */
  public static AltitudeBand closed(double lowerFeet, double upperFeet) {
    requireFinite(lowerFeet);
    requireFinite(upperFeet);
    if (lowerFeet > upperFeet) {
      throw new IllegalArgumentException("Lower altitude must not exceed upper altitude.");
    }
    return new AltitudeBand(Status.RESOLVED, lowerFeet, upperFeet);
  }

  /**
   * Returns a resolved inclusive lower limit with an explicitly unlimited upper side.
   *
   * @param lowerFeet inclusive lower altitude in feet
   * @return a band including the lower limit and all higher altitudes
   * @throws IllegalArgumentException if the limit is nonfinite
   */
  public static AltitudeBand atLeast(double lowerFeet) {
    requireFinite(lowerFeet);
    return new AltitudeBand(Status.RESOLVED, lowerFeet, Double.POSITIVE_INFINITY);
  }

  /**
   * Returns a resolved inclusive upper limit with an explicitly unlimited lower side.
   *
   * @param upperFeet inclusive upper altitude in feet
   * @return a band including the upper limit and all lower altitudes
   * @throws IllegalArgumentException if the limit is nonfinite
   */
  public static AltitudeBand atMost(double upperFeet) {
    requireFinite(upperFeet);
    return new AltitudeBand(Status.RESOLVED, Double.NEGATIVE_INFINITY, upperFeet);
  }

  /**
   * Describes how this band treats vertical membership.
   *
   * @return the band's resolution status
   */
  public Status status() {
    return status;
  }

  /**
   * Returns the resolved lower limit, or empty for an unknown or unlimited lower side.
   *
   * @return the finite lower altitude in feet, when present
   */
  public OptionalDouble lowerFeet() {
    return Double.isFinite(lowerFeet) ? OptionalDouble.of(lowerFeet) : OptionalDouble.empty();
  }

  /**
   * Returns the resolved upper limit, or empty for an unknown or unlimited upper side.
   *
   * @return the finite upper altitude in feet, when present
   */
  public OptionalDouble upperFeet() {
    return Double.isFinite(upperFeet) ? OptionalDouble.of(upperFeet) : OptionalDouble.empty();
  }

  /**
   * Returns whether the finite altitude is definitely included; unknown bands always return false.
   *
   * @param altitudeFeet altitude in feet using this band's reference
   * @return whether the resolved constraint includes the altitude
   * @throws IllegalArgumentException if the altitude is nonfinite
   */
  public boolean contains(double altitudeFeet) {
    requireFinite(altitudeFeet);
    return status != Status.UNKNOWN && altitudeFeet >= lowerFeet && altitudeFeet <= upperFeet;
  }

  /**
   * Clips a linear change in altitude to this band, returning fractions along the segment in {@code [0, 1]}.
   * Both endpoint altitudes must be finite and use the same reference as this band.
   *
   * <p>Empty means there is no definite vertical intersection, either because the band is unknown or because the segment
   * misses it. A touch at a single altitude is retained as an interval with equal endpoints; callers requiring positive
   * duration can filter those intervals. A constant altitude on an inclusive boundary returns the full interval.
   *
   * @param startFeet starting altitude in feet
   * @param endFeet ending altitude in feet
   * @return the inclusive interval of vertical occupancy, or empty for an unknown band or a miss
   * @throws IllegalArgumentException if either altitude is nonfinite
   */
  public Optional<Interval> clip(double startFeet, double endFeet) {
    requireFinite(startFeet);
    requireFinite(endFeet);
    if (status == Status.UNKNOWN) {
      return Optional.empty();
    }
    if (startFeet == endFeet) {
      return contains(startFeet) ? Optional.of(FULL_INTERVAL) : Optional.empty();
    }

    double lower = Math.max(Math.min(startFeet, endFeet), lowerFeet);
    double upper = Math.min(Math.max(startFeet, endFeet), upperFeet);
    if (lower > upper) {
      return Optional.empty();
    }
    double first = fraction(lower, startFeet, endFeet);
    double second = fraction(upper, startFeet, endFeet);
    return Optional.of(new Interval(Math.min(first, second), Math.max(first, second)));
  }

  private static double fraction(double altitudeFeet, double startFeet, double endFeet) {
    if (altitudeFeet == startFeet) {
      return 0.0;
    }
    if (altitudeFeet == endFeet) {
      return 1.0;
    }
    double difference = endFeet - startFeet;
    // Halving preserves the ratio if subtracting two finite extremes overflows.
    return Double.isFinite(difference)
        ? (altitudeFeet - startFeet) / difference
        : (altitudeFeet / 2.0 - startFeet / 2.0) / (endFeet / 2.0 - startFeet / 2.0);
  }

  private static void requireFinite(double altitudeFeet) {
    if (!Double.isFinite(altitudeFeet)) {
      throw new IllegalArgumentException("Altitude must be finite: " + altitudeFeet);
    }
  }

  /**
   * Inclusive fractions along a segment, including a zero-length touch when both values are equal.
   *
   * @param startFraction first included fraction within {@code [0, 1]}
   * @param endFraction last included fraction within {@code [startFraction, 1]}
   */
  public record Interval(double startFraction, double endFraction) {

    /**
     * Creates an inclusive interval along a segment.
     *
     * @param startFraction first included fraction within {@code [0, 1]}
     * @param endFraction last included fraction within {@code [startFraction, 1]}
     * @throws IllegalArgumentException if either fraction is nonfinite, outside {@code [0, 1]}, or in reverse order
     */
    public Interval {
      if (!Double.isFinite(startFraction) || !Double.isFinite(endFraction)
          || startFraction < 0.0 || endFraction > 1.0 || startFraction > endFraction) {
        throw new IllegalArgumentException("Fractions must be ordered within [0, 1].");
      }
    }
  }

  @Override
  public boolean equals(Object other) {
    return this == other || other instanceof AltitudeBand band
        && status == band.status
        && Double.compare(lowerFeet, band.lowerFeet) == 0
        && Double.compare(upperFeet, band.upperFeet) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, lowerFeet, upperFeet);
  }

  @Override
  public String toString() {
    return "AltitudeBand{status=" + status + ", lowerFeet=" + lowerFeet + ", upperFeet=" + upperFeet + '}';
  }
}
