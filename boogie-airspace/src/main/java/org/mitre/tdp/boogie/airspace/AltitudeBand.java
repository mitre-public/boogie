package org.mitre.tdp.boogie.airspace;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.AltitudeLimits;
import org.mitre.tdp.boogie.AltitudeReference;

/**
 * An airspace's vertical constraint, built from its {@link AltitudeLimits}, and how a trajectory is compared with it.
 *
 * <p>Each height limit is measured from its own reference: mean sea level, or the terrain beneath the trajectory. A
 * trajectory is compared with each limit in that limit's reference (see {@link VerticalPosition}), so a band may mix
 * references, such as a floor 700 feet above ground under a ceiling 12,000 feet above mean sea level. A side with no
 * limit is open; a side not specified, or set by NOTAM, cannot be evaluated. The {@code double} methods take altitudes
 * above mean sea level with no terrain, and so can evaluate only limits measured from mean sea level.
 *
 * <p>Bounds are inclusive. Callers can inspect {@link #status()} to distinguish a band that cannot establish vertical
 * membership at all from a resolved band which excludes an altitude, and {@link #checkable(VerticalPosition)} to tell
 * whether a position reports what the band's limits need.
 */
public final class AltitudeBand {

  /**
   * Whether the band can establish vertical membership, allows every altitude, or has limits.
   */
  public enum Status {
    /**
     * No side has a height and at least one is not known, so the band cannot establish vertical membership.
     */
    UNKNOWN,
    /**
     * Neither side has a limit: every altitude is allowed.
     */
    UNBOUNDED,
    /**
     * At least one side has a height.
     */
    RESOLVED
  }

  /**
   * The lowest terrain on Earth in feet above mean sea level: the shore of the Dead Sea, about 430 meters below sea
   * level, rounded down. Bounds a floor above ground level where the terrain is missing.
   */
  public static final double LOWEST_TERRAIN_FEET = -1_420.0;

  /**
   * The highest terrain on Earth in feet above mean sea level: the summit of Everest. Bounds a ceiling above ground level
   * where the terrain is missing.
   */
  public static final double HIGHEST_TERRAIN_FEET = 29_032.0;

  /**
   * The band for limits not specified on either side.
   */
  private static final AltitudeBand UNKNOWN = new AltitudeBand(AltitudeLimits.unknown());
  /**
   * The band without limits on either side.
   */
  private static final AltitudeBand UNBOUNDED = new AltitudeBand(AltitudeLimits.of(AltitudeLimit.none(), AltitudeLimit.none()));
  /**
   * The whole segment.
   */
  private static final Interval FULL_INTERVAL = new Interval(0.0, 1.0);

  /**
   * The limits this band was built from.
   */
  private final AltitudeLimits limits;
  /**
   * How the band treats vertical membership, derived from its limits.
   */
  private final Status status;

  /**
   * Creates a band and derives its status: resolved if either side has a height, unbounded if both sides have no limit,
   * and unknown otherwise.
   *
   * @param limits the limits
   */
  private AltitudeBand(AltitudeLimits limits) {
    this.limits = limits;
    boolean anyHeight = limits.lower().height().isPresent() || limits.upper().height().isPresent();
    this.status = anyHeight ? Status.RESOLVED : limits.known() ? Status.UNBOUNDED : Status.UNKNOWN;
  }

  /**
   * Creates the band for an airspace's limits.
   *
   * @param limits the limits
   * @return the band
   */
  public static AltitudeBand of(AltitudeLimits limits) {
    return new AltitudeBand(requireNonNull(limits, "limits"));
  }

  /**
   * Returns a band that cannot establish vertical membership: both sides not specified.
   *
   * @return the unknown band
   */
  public static AltitudeBand unknown() {
    return UNKNOWN;
  }

  /**
   * Returns a band without limits, which allows every altitude.
   *
   * @return the unbounded band
   */
  public static AltitudeBand unbounded() {
    return UNBOUNDED;
  }

  /**
   * Creates a band from nullable inclusive limits above mean sea level. A null limit leaves that side open, except that
   * two null limits return {@link #unknown()}, since they say nothing about the band.
   *
   * @param lowerFeet inclusive lower altitude in feet above mean sea level, or null
   * @param upperFeet inclusive upper altitude in feet above mean sea level, or null
   * @return the band
   * @throws IllegalArgumentException if a present limit is nonfinite or the lower limit exceeds the upper limit
   */
  public static AltitudeBand of(Double lowerFeet, Double upperFeet) {
    if (lowerFeet == null && upperFeet == null) {
      return UNKNOWN;
    }
    return of(AltitudeLimits.of(
        lowerFeet == null ? AltitudeLimit.none() : AltitudeLimit.msl(lowerFeet),
        upperFeet == null ? AltitudeLimit.none() : AltitudeLimit.msl(upperFeet)));
  }

  /**
   * Returns a band with inclusive, finite limits above mean sea level.
   *
   * @param lowerFeet inclusive lower altitude in feet above mean sea level
   * @param upperFeet inclusive upper altitude in feet above mean sea level
   * @return the band
   * @throws IllegalArgumentException if a limit is nonfinite or the lower limit exceeds the upper limit
   */
  public static AltitudeBand closed(double lowerFeet, double upperFeet) {
    return of(AltitudeLimits.of(AltitudeLimit.msl(lowerFeet), AltitudeLimit.msl(upperFeet)));
  }

  /**
   * Returns a band with an inclusive lower limit above mean sea level and no upper limit.
   *
   * @param lowerFeet inclusive lower altitude in feet above mean sea level
   * @return the band
   * @throws IllegalArgumentException if the limit is nonfinite
   */
  public static AltitudeBand atLeast(double lowerFeet) {
    return of(AltitudeLimits.of(AltitudeLimit.msl(lowerFeet), AltitudeLimit.none()));
  }

  /**
   * Returns a band with an inclusive upper limit above mean sea level and no lower limit.
   *
   * @param upperFeet inclusive upper altitude in feet above mean sea level
   * @return the band
   * @throws IllegalArgumentException if the limit is nonfinite
   */
  public static AltitudeBand atMost(double upperFeet) {
    return of(AltitudeLimits.of(AltitudeLimit.none(), AltitudeLimit.msl(upperFeet)));
  }

  /**
   * Describes how this band treats vertical membership.
   *
   * @return the band's status
   */
  public Status status() {
    return status;
  }

  /**
   * Gets the limits this band was built from.
   *
   * @return the limits
   */
  public AltitudeLimits limits() {
    return limits;
  }

  /**
   * Tells whether checking a position against this band needs the terrain beneath it.
   *
   * @return true if either limit is a height measured from the terrain
   */
  public boolean requiresTerrain() {
    return isAgl(limits.lower()) || isAgl(limits.upper());
  }

  /**
   * Tells whether a position reports what this band's limits need: an altitude for heights above mean sea level, and
   * also the terrain for heights above ground level. A band with a side that is not known is never checkable.
   *
   * @param position the position
   * @return true if both sides are known and every height can be evaluated at the position
   */
  public boolean checkable(VerticalPosition position) {
    requireNonNull(position, "position");
    return limits.known() && evaluable(limits.lower(), position) && evaluable(limits.upper(), position);
  }

  /**
   * Returns whether the finite altitude above mean sea level is definitely included; false for any band with a limit
   * measured from the terrain or not known.
   *
   * @param altitudeFeet altitude in feet above mean sea level
   * @return whether the band includes the altitude
   * @throws IllegalArgumentException if the altitude is nonfinite
   */
  public boolean contains(double altitudeFeet) {
    requireFinite(altitudeFeet);
    return contains(VerticalPosition.msl(altitudeFeet));
  }

  /**
   * Returns whether a position is definitely included: false when the band is not {@linkplain #checkable checkable}
   * at the position.
   *
   * @param position the position
   * @return whether the band includes the position
   */
  public boolean contains(VerticalPosition position) {
    if (!checkable(position)) {
      return false;
    }
    return limits.lower().height().map(h -> position.heightFeet(h.reference()).get() >= h.feet()).orElse(true)
        && limits.upper().height().map(h -> position.heightFeet(h.reference()).get() <= h.feet()).orElse(true);
  }

  /**
   * Clips a linear change in altitude above mean sea level to this band, returning fractions along the segment in
   * {@code [0, 1]}. Equivalent to {@link #clip(VerticalPosition, VerticalPosition)} with no terrain.
   *
   * @param startFeet starting altitude in feet above mean sea level
   * @param endFeet ending altitude in feet above mean sea level
   * @return the inclusive interval of vertical occupancy, or empty for an uncheckable band or a miss
   * @throws IllegalArgumentException if either altitude is nonfinite
   */
  public Optional<Interval> clip(double startFeet, double endFeet) {
    requireFinite(startFeet);
    requireFinite(endFeet);
    return clip(VerticalPosition.msl(startFeet), VerticalPosition.msl(endFeet));
  }

  /**
   * Clips a segment between two positions to this band, returning fractions along the segment in {@code [0, 1]}. The
   * altitude and the terrain each change linearly along the segment, so the height above each limit's reference does
   * too; each limit is clipped in its own reference and the results intersected.
   *
   * <p>Empty means there is no definite vertical intersection: the band is not {@linkplain #checkable checkable} at
   * either position, or the segment misses it. A touch at a single height is retained as an interval with equal
   * endpoints; callers requiring positive duration can filter those intervals. A constant height on an inclusive limit
   * satisfies it along the whole segment.
   *
   * @param start the segment's starting position
   * @param end the segment's ending position
   * @return the inclusive interval of vertical occupancy, or empty for an uncheckable band or a miss
   */
  public Optional<Interval> clip(VerticalPosition start, VerticalPosition end) {
    if (!checkable(start) || !checkable(end)) {
      return Optional.empty();
    }
    return clipEvaluable(start, end);
  }

  /**
   * Clips a segment between two positions by only those limits that can be evaluated at both ends, ignoring the rest.
   * A height above mean sea level needs an altitude at both ends; one above ground level also needs the terrain; a side
   * that is not known is never evaluated. This lets a limit that can be evaluated rule a segment out even when another
   * cannot: a segment above a ceiling measured from mean sea level misses the band whatever the terrain beneath it.
   *
   * <p>A height above ground level without the terrain at both ends is still bounded by the altitude above mean sea
   * level: the terrain lies between {@link #LOWEST_TERRAIN_FEET} and {@link #HIGHEST_TERRAIN_FEET}, so a segment more
   * than that far above an above-ground ceiling, or below an above-ground floor, misses the band whatever the terrain
   * beneath it.
   *
   * <p>When {@link #clip(VerticalPosition, VerticalPosition)} can check the whole band, the two methods agree. Otherwise
   * a present result is not a definite intersection, only the part of the segment no evaluable limit or terrain bound
   * excludes.
   *
   * @param start the segment's starting position
   * @param end the segment's ending position
   * @return the inclusive interval no evaluable limit or terrain bound excludes (the whole segment if none applies), or
   *     empty for an {@link Status#UNKNOWN} band or when a limit or terrain bound excludes the whole segment
   */
  public Optional<Interval> clipEvaluable(VerticalPosition start, VerticalPosition end) {
    requireNonNull(start, "start");
    requireNonNull(end, "end");
    if (status == Status.UNKNOWN) {
      return Optional.empty();
    }
    return limits.lower().height()
        .map(floor -> aboveFloor(floor, start, end))
        .orElse(Optional.of(FULL_INTERVAL))
        .flatMap(aboveFloor -> limits.upper().height()
            .map(ceiling -> belowCeiling(ceiling, start, end))
            .orElse(Optional.of(FULL_INTERVAL))
            .flatMap(belowCeiling -> intersection(aboveFloor, belowCeiling)));
  }

  /**
   * Finds where a segment is at or above a floor: exactly when the floor can be evaluated at both ends, against the
   * lowest terrain when an above-ground floor lacks the terrain, and the whole segment otherwise.
   *
   * @param floor the floor
   * @param start the segment's starting position
   * @param end the segment's ending position
   * @return the fractions not below the floor, or empty if the whole segment is
   */
  private static Optional<Interval> aboveFloor(AltitudeLimit.Height floor, VerticalPosition start, VerticalPosition end) {
    if (evaluable(floor, start) && evaluable(floor, end)) {
      return atOrAbove(floor.feet(), heightFeet(start, floor), heightFeet(end, floor));
    }
    return terrainBounded(floor, start, end)
        ? atOrAbove(floor.feet() + LOWEST_TERRAIN_FEET, start.altitudeFeet(), end.altitudeFeet())
        : Optional.of(FULL_INTERVAL);
  }

  /**
   * Finds where a segment is at or below a ceiling: exactly when the ceiling can be evaluated at both ends, against the
   * highest terrain when an above-ground ceiling lacks the terrain, and the whole segment otherwise.
   *
   * @param ceiling the ceiling
   * @param start the segment's starting position
   * @param end the segment's ending position
   * @return the fractions not above the ceiling, or empty if the whole segment is
   */
  private static Optional<Interval> belowCeiling(AltitudeLimit.Height ceiling, VerticalPosition start, VerticalPosition end) {
    if (evaluable(ceiling, start) && evaluable(ceiling, end)) {
      return atOrBelow(ceiling.feet(), heightFeet(start, ceiling), heightFeet(end, ceiling));
    }
    return terrainBounded(ceiling, start, end)
        ? atOrBelow(ceiling.feet() + HIGHEST_TERRAIN_FEET, start.altitudeFeet(), end.altitudeFeet())
        : Optional.of(FULL_INTERVAL);
  }

  /**
   * Tells whether a height above ground level that cannot be evaluated can still be bounded by the range of terrain:
   * both ends report an altitude above mean sea level.
   *
   * @param height the limit
   * @param start the segment's starting position
   * @param end the segment's ending position
   * @return true for a height above ground level with an altitude above mean sea level at both ends
   */
  private static boolean terrainBounded(AltitudeLimit.Height height, VerticalPosition start, VerticalPosition end) {
    return height.reference() == AltitudeReference.AGL && start.altitudeFeet() != null && end.altitudeFeet() != null;
  }

  /**
   * Gets a position's height in a limit's reference, which the caller has checked is available.
   *
   * @param position the position
   * @param height the limit
   * @return the position's height in feet in the limit's reference
   */
  private static double heightFeet(VerticalPosition position, AltitudeLimit.Height height) {
    return position.heightFeet(height.reference()).orElseThrow();
  }

  /**
   * Finds where a linearly changing height is at or above a floor.
   *
   * @param floorFeet the floor
   * @param startFeet the height at the segment's start
   * @param endFeet the height at the segment's end
   * @return the fractions of the segment at or above the floor, or empty if none are
   */
  private static Optional<Interval> atOrAbove(double floorFeet, double startFeet, double endFeet) {
    if (Math.max(startFeet, endFeet) < floorFeet) {
      return Optional.empty();
    }
    if (Math.min(startFeet, endFeet) >= floorFeet) {
      return Optional.of(FULL_INTERVAL);
    }
    double crossing = fraction(floorFeet, startFeet, endFeet);
    return Optional.of(endFeet > startFeet ? new Interval(crossing, 1.0) : new Interval(0.0, crossing));
  }

  /**
   * Finds where a linearly changing height is at or below a ceiling.
   *
   * @param ceilingFeet the ceiling
   * @param startFeet the height at the segment's start
   * @param endFeet the height at the segment's end
   * @return the fractions of the segment at or below the ceiling, or empty if none are
   */
  private static Optional<Interval> atOrBelow(double ceilingFeet, double startFeet, double endFeet) {
    if (Math.min(startFeet, endFeet) > ceilingFeet) {
      return Optional.empty();
    }
    if (Math.max(startFeet, endFeet) <= ceilingFeet) {
      return Optional.of(FULL_INTERVAL);
    }
    double crossing = fraction(ceilingFeet, startFeet, endFeet);
    return Optional.of(endFeet > startFeet ? new Interval(0.0, crossing) : new Interval(crossing, 1.0));
  }

  /**
   * Intersects two intervals along the same segment.
   *
   * @param first one interval
   * @param second the other interval
   * @return the fractions in both, or empty if they do not overlap
   */
  private static Optional<Interval> intersection(Interval first, Interval second) {
    double from = Math.max(first.startFraction(), second.startFraction());
    double to = Math.min(first.endFraction(), second.endFraction());
    if (from > to) {
      return Optional.empty();
    }
    return Optional.of(from == 0.0 && to == 1.0 ? FULL_INTERVAL : new Interval(from, to));
  }

  /**
   * Tells whether a limit can be evaluated at a position: always for a side without a height, and otherwise when the
   * position reports the value the limit's reference needs.
   *
   * @param limit the limit
   * @param position the position
   * @return true if the limit can be evaluated there
   */
  private static boolean evaluable(AltitudeLimit limit, VerticalPosition position) {
    return limit.height().map(height -> evaluable(height, position)).orElse(true);
  }

  /**
   * Tells whether a position reports the value a height's reference needs.
   *
   * @param height the limit
   * @param position the position
   * @return true if the position's height in the limit's reference is available
   */
  private static boolean evaluable(AltitudeLimit.Height height, VerticalPosition position) {
    return position.heightFeet(height.reference()).isPresent();
  }

  /**
   * Tells whether a limit is a height measured from the terrain.
   *
   * @param limit the limit
   * @return true for a height above ground level
   */
  private static boolean isAgl(AltitudeLimit limit) {
    return limit.height().filter(height -> height.reference() == AltitudeReference.AGL).isPresent();
  }

  /**
   * Locates where a linearly changing height reaches a value.
   *
   * @param altitudeFeet the value
   * @param startFeet the height at the segment's start
   * @param endFeet the height at the segment's end
   * @return the fraction of the segment, exactly 0 or 1 at the ends
   */
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

  /**
   * Rejects a nonfinite altitude.
   *
   * @param altitudeFeet the altitude
   * @throws IllegalArgumentException if the altitude is not finite
   */
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
    return this == other || other instanceof AltitudeBand band && limits.equals(band.limits);
  }

  @Override
  public int hashCode() {
    return limits.hashCode();
  }

  @Override
  public String toString() {
    return "AltitudeBand{status=" + status + ", lower=" + limits.lower() + ", upper=" + limits.upper() + '}';
  }
}
