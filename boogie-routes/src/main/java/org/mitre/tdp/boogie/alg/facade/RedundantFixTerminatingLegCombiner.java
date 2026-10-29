package org.mitre.tdp.boogie.alg.facade;

import com.google.common.collect.Range;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.fn.LeftMerger;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * A sequential leg combiner attempts to <i>safely</i> combine the definitions of two legs when certain conditions are met.
 * <br>
 * Altitude and speed constraints are combined according to the {@link ConstraintMerge} policy for the pair of legs, while
 * fly-over comes from the {@link PreferredLeg}.
 * <br>
 * RNP is a property of the path rather than the fix, so it comes from the previous leg (which supplies the path) where present.
 */
final class RedundantFixTerminatingLegCombiner implements UnaryOperator<List<ExpandedRouteLeg>> {
  private static final CollapsedElementType COLLAPSED_ELEMENT_TYPE = CollapsedElementType.INSTANCE;
  private static final PreferredLeg PREFERRED_LEG = PreferredLeg.INSTANCE;
  @Override
  public List<ExpandedRouteLeg> apply(List<ExpandedRouteLeg> legs) {
    LeftMerger<ExpandedRouteLeg> merger = new LeftMerger<>(
        SimilarFixTerminatingLeg.INSTANCE::test,
        RedundantFixTerminatingLegCombiner::safelyMergeLegs
    );
    return legs.stream().collect(merger.asCollector());
  }

  /**
   * Attempts to safely merge the subsequent leg definitions where the provided set of conditions are met.
   * <br>
   * The primary goal of this method is to merge legs identified within the expanded route which terminate in similar fixes (as
   * a side effect of the overall expansion process).
   * <br>
   * However the word "safely" is used here because simply choosing one implementation of the leg from the expansion is not by
   * itself sufficient to capture all of the correct information about how the aircraft will fly the leg.
   * <br>
   * The previous leg supplies the path and RNP, the {@link ConstraintMerge} policy the altitude and speed, and the preferred leg
   * the fly-over.
   */
  private static ExpandedRouteLeg safelyMergeLegs(ExpandedRouteLeg previous, ExpandedRouteLeg next) {
    ExpandedRouteLeg preferred = PREFERRED_LEG.apply(previous, next);

    Leg merged = flyPreviousToCombinedConstraints(previous, next, preferred);

    ElementType inferredElementType = COLLAPSED_ELEMENT_TYPE.apply(previous.elementType(), next.elementType()).orElse(preferred.elementType());

    return new ExpandedRouteLeg(preferred.section(), inferredElementType, previous.wildcards().concat(next.wildcards()), merged);
  }

  /**
   * Flies the previous leg using the merged constraints.
   * @param previous the previous leg that would typically end a transition.
   * @param next the next leg at the same fix
   * @param preferred the leg selected as the fly-over source
   * @return a collapsed leg that retains the previous path and RNP with the merged constraints and fly-over
   */
  private static Leg flyPreviousToCombinedConstraints(ExpandedRouteLeg previous, ExpandedRouteLeg next, ExpandedRouteLeg preferred) {
    ConstraintMerge constraintMerge = ConstraintMerge.between(previous, next);
    Range<Double> speedConstraint = constraintMerge.apply(previous.speedConstraint(), next.speedConstraint());
    Range<Double> altitudeConstraint = constraintMerge.apply(previous.altitudeConstraint(), next.altitudeConstraint());
    Double rnp = previous.rnp().or(next::rnp).orElse(null);
    boolean flyOverMe = preferred.isFlyOverFix();
    return Leg.builder(previous.pathTerminator(), previous.sequenceNumber())
        .associatedFix(previous.associatedFix().orElse(null))
        .recommendedNavaid(previous.recommendedNavaid().orElse(null))
        .sequenceNumber(previous.sequenceNumber())
        .speedConstraint(speedConstraint)
        .altitudeConstraint(altitudeConstraint)
        .outboundMagneticCourse(previous.outboundMagneticCourse().orElse(null))
        .theta(previous.theta().orElse(null))
        .rho(previous.rho().orElse(null))
        .rnp(rnp)
        .routeDistance(previous.routeDistance().orElse(null))
        .holdTime(previous.holdTime().orElse(null))
        .isPublishedHoldingFix(previous.isPublishedHoldingFix())
        .isFlyOverFix(flyOverMe)
        .build();
  }
}
