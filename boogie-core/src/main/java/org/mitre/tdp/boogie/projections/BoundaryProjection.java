package org.mitre.tdp.boogie.projections;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.List;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.AirspaceSequence;

/**
 * Samples the outgoing boundary using the existing projection helpers with configurable steps.
 *
 * <p>Lines and arcs include their start and exclude their end, which is supplied by the next
 * sequence. Circles produce an open ring. The caller closes the boundary after its final sequence.
 */
public final class BoundaryProjection {
  private BoundaryProjection() {}

  /**
   * Samples geometry between its fix and next's fix. For a circle,
   * only the current sequence's center and radius are used and {@code next} may be null.
   * Arc bearings come from the fixes and the radius comes from the center to the starting fix.
   *
   * @param current sequence defining the outgoing edge
   * @param next sequence defining its endpoint; unused for circles
   * @param lineStepNm sample spacing for great-circle and rhumb lines, in nautical miles
   * @param arcStepDegrees bearing increment for arcs and circles, in degrees
   * @return boundary samples including the start and excluding the end
   * @throws IllegalArgumentException for missing geometry or an invalid step
   */
  public static List<LatLong> project(AirspaceSequence current, AirspaceSequence next, double lineStepNm, double arcStepDegrees) {
    requireNonNull(current, "Current sequence is required.");
    requireNonNull(current.geometry(), "Sequence geometry is required.");
    return switch (current.geometry()) {
      case GREAT_CIRCLE -> GreatCircle.project(fix(current), fix(next), lineStepNm);
      case RHUMB_LINE -> RhumbLine.project(fix(current), fix(next), lineStepNm);
      case CLOCKWISE_ARC -> ClockwiseArc.project(fix(current), current.centerFix().orElseThrow(() -> missing("Arc center")), fix(next), arcStepDegrees);
      case COUNTER_CLOCKWISE_ARC -> CounterClockwiseArc.project(fix(current), current.centerFix().orElseThrow(() -> missing("Arc center")), fix(next), arcStepDegrees);
      case CIRCLE -> Circle.project(current.arcRadius().orElseThrow(() -> missing("Circle radius")),
          current.centerFix().orElseThrow(() -> missing("Circle center")), arcStepDegrees);
    };
  }

  private static LatLong fix(AirspaceSequence sequence) {
    checkArgument(sequence != null, "Sequence is required for a line or arc.");
    return sequence.associatedFix().orElseThrow(() -> missing("Associated fix"));
  }

  private static IllegalArgumentException missing(String field) {
    return new IllegalArgumentException(field + " is required for boundary projection.");
  }
}
