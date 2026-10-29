package org.mitre.tdp.boogie.alg.facade;

import com.google.common.collect.Range;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BinaryOperator;

import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.STAR;

/**
 * How two legs terminating at the same fix merge into one, based on which restrictions the pilot is cleared to fly when
 * crossing it. The strategy is selected once for the pair via {@link #between} and then builds the whole merged leg.
 * <br>
 * Every strategy flies the previous leg's path (and so keeps its RNP), takes the fix properties (section, fly-over) from the
 * governing leg and differs only in how the altitude and speed constraints combine. When either constraint is
 * {@link Range#all()} every strategy returns the other, they differ only when both legs are constrained:
 * <pre>
 * previous   next        PREVIOUS_GOVERNS  NEXT_GOVERNS  BOTH_APPLY
 * [3, 8]     [5, 10]     [3, 8]            [5, 10]       [5, 8]
 * (-inf, 8]  [5, +inf)   (-inf, 8]         [5, +inf)     [5, 8]
 * [6, +inf)  (-inf, 5]   [6, +inf)         (-inf, 5]     [6, +inf)
 * </pre>
 */
enum LegMerge {
  /**
   * The previous procedure governs the fix, e.g. a SID joining an airway.
   */
  PREVIOUS_GOVERNS((previous, next) -> previous, (previous, next) -> isBounded(previous) ? previous : next),
  /**
   * The procedure being joined governs the fix, e.g. an approach clearance supersedes the STAR's restrictions.
   */
  NEXT_GOVERNS((previous, next) -> next, (previous, next) -> isBounded(next) ? next : previous),
  /**
   * Both procedures are in force at the fix, e.g. nothing supersedes the SID when joining a STAR, so the most restrictive
   * combination is kept. The joined procedure still supplies the fix properties.
   */
  BOTH_APPLY((previous, next) -> next, LegMerge::mostRestrictive);

  private static final Logger LOG = LoggerFactory.getLogger(LegMerge.class);

  /**
   * Selects the leg whose fix properties (section, fly-over, fallback element type) the merged leg keeps.
   */
  private final BinaryOperator<ExpandedRouteLeg> governing;

  /**
   * Combines the previous and next legs' altitude or speed constraint.
   */
  private final BinaryOperator<Range<Double>> constraint;

  LegMerge(BinaryOperator<ExpandedRouteLeg> governing, BinaryOperator<Range<Double>> constraint) {
    this.governing = governing;
    this.constraint = constraint;
  }

  /**
   * Selects the strategy for the pair of legs - {@link #BOTH_APPLY} for SID to STAR, otherwise whichever leg is the
   * {@link PreferredLeg} governs.
   */
  static LegMerge between(ExpandedRouteLeg previous, ExpandedRouteLeg next) {
    if (SID.equals(previous.elementType()) && STAR.equals(next.elementType())) {
      return BOTH_APPLY;
    }
    return PreferredLeg.INSTANCE.apply(previous, next) == next ? NEXT_GOVERNS : PREVIOUS_GOVERNS;
  }

  /**
   * Flies the previous leg's path to the merged constraints, keeping the governing leg's fix properties.
   */
  ExpandedRouteLeg merge(ExpandedRouteLeg previous, ExpandedRouteLeg next) {
    ExpandedRouteLeg governing = this.governing.apply(previous, next);

    Leg merged = Leg.builder(previous.pathTerminator(), previous.sequenceNumber())
        .associatedFix(previous.associatedFix().orElse(null))
        .recommendedNavaid(previous.recommendedNavaid().orElse(null))
        .centerFix(previous.centerFix().orElse(null))
        .outboundMagneticCourse(previous.outboundMagneticCourse().orElse(null))
        .theta(previous.theta().orElse(null))
        .rho(previous.rho().orElse(null))
        .rnp(previous.rnp().or(next::rnp).orElse(null))
        .routeDistance(previous.routeDistance().orElse(null))
        .holdTime(previous.holdTime().orElse(null))
        .verticalAngle(previous.verticalAngle().or(next::verticalAngle).orElse(null))
        .turnDirection(previous.turnDirection().orElse(null))
        .arcRadius(previous.arcRadius().orElse(null))
        .speedConstraint(constraint(previous.speedConstraint(), next.speedConstraint()))
        .altitudeConstraint(constraint(previous.altitudeConstraint(), next.altitudeConstraint()))
        .isPublishedHoldingFix(previous.isPublishedHoldingFix())
        .isFlyOverFix(governing.isFlyOverFix())
        .isIntermediateOrInitialApproachFix(previous.isIntermediateOrInitialApproachFix() || next.isIntermediateOrInitialApproachFix())
        .build();

    ElementType elementType = CollapsedElementType.INSTANCE.apply(previous.elementType(), next.elementType()).orElse(governing.elementType());

    return new ExpandedRouteLeg(governing.section(), elementType, previous.wildcards().concat(next.wildcards()), merged);
  }

  Range<Double> constraint(Range<Double> previous, Range<Double> next) {
    return constraint.apply(previous, next);
  }

  private static boolean isBounded(Range<Double> range) {
    return range.hasLowerBound() || range.hasUpperBound();
  }

  /**
   * Keeps the higher lower bound and lower upper bound of the two constraints. When they share no common value the procedures
   * weren't coordinated, so the previous constraint (the clearance in force while crossing the fix) is kept.
   */
  private static Range<Double> mostRestrictive(Range<Double> previous, Range<Double> next) {
    if (previous.isConnected(next)) {
      Range<Double> intersection = previous.intersection(next);
      if (!intersection.isEmpty()) {
        return intersection;
      }
    }
    LOG.debug("Conflicting constraints {} and {} at a shared fix, keeping {}.", previous, next, previous);
    return previous;
  }
}
