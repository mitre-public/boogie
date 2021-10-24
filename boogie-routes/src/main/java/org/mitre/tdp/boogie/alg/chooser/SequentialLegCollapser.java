package org.mitre.tdp.boogie.alg.chooser;

import static org.mitre.tdp.boogie.alg.resolve.ElementType.AIRWAY;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.AIRWAY_TO_APPROACH;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.AIRWAY_TO_STAR;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.APPROACH;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID_TO_APPROACH;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID_TO_STAR;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.STAR;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.STAR_TO_APPROACH;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.ExpandedRouteLeg;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.fn.LeftMerger;
import org.mitre.tdp.boogie.model.BoogieLeg;

/**
 * A sequential leg combiner attempts to <i>safely</i> combine the definitions of two legs when certain conditions are met.
 * <br>
 * The key thing that makes this operation safe is that it preserves the (more constraining) of the altitude and speed constraints
 * available at a fix along the expansion.
 */
final class SequentialLegCollapser implements UnaryOperator<List<ExpandedRouteLeg>> {

  @Override
  public List<ExpandedRouteLeg> apply(List<ExpandedRouteLeg> legs) {
    LeftMerger<ExpandedRouteLeg> merger = new LeftMerger<>(
        this::isSimilarFixTerminatingLeg,
        this::safelyMergeLegs
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
   * Namely this method seeks to preserve the RNP values, Altitude/Speed constraints from the limiting procedure.
   */
  ExpandedRouteLeg safelyMergeLegs(ExpandedRouteLeg previous, ExpandedRouteLeg next) {
    ExpandedRouteLeg preferred = preferredLeg(previous, next);

    Leg combined = new BoogieLeg.Builder()
        .associatedFix(preferred.associatedFix().orElse(null))
        .recommendedNavaid(preferred.recommendedNavaid().orElse(null))
        // intentional - keep the previous path type (as the termination is identical)
        // this should mostly be TF's anyway...
        .pathTerminator(previous.pathTerminator())
        .sequenceNumber(preferred.sequenceNumber())
        .speedConstraint(preferred.speedConstraint())
        .altitudeConstraint(preferred.altitudeConstraint())
        .outboundMagneticCourse(preferred.outboundMagneticCourse().orElse(null))
        .theta(preferred.theta().orElse(null))
        .rho(preferred.rho().orElse(null))
        .rnp(preferred.rnp().orElse(null))
        .routeDistance(preferred.routeDistance().orElse(null))
        .holdTime(preferred.holdTime().orElse(null))
        .isPublishedHoldingFix(preferred.isPublishedHoldingFix())
        .isFlyOverFix(preferred.isFlyOverFix())
        .build();

    ElementType inferredElementType = specialElementTypeBetween(previous.elementType(), next.elementType()).orElse(preferred.elementType());

    return new ExpandedRouteLeg(preferred.section(), inferredElementType, previous.wildcards().concat(next.wildcards()), combined);
  }

  /**
   * Returns one of the special {@link ElementType} classes if the previous and next match the appropriate criteria.
   * <br>
   * These special classes are to indicate that certain legs (post combination) may contain a hybrid of information from distinct
   * pieces of infrastructure.
   * <br>
   * e.g. for a TF leg where an airway joins a STAR at some fix - the actual leg geometry (the path that the aircraft will fly)
   * is a property of the airway (as it defines the previous fix to the STAR join). However the restriction information and the
   * required navigational performance values for the leg come from the STAR (as thats what's being joined).
   * <br>
   * These need to be special-cased so that metrics downstream like "airway segment usage" can still identify these legs as having
   * been defined by the airway geometrically (and so we should track usage for them) even if the constraints come from the STAR
   * (and from the perspective of the FMS the leg is really then "sourced from" the STAR).
   */
  Optional<ElementType> specialElementTypeBetween(ElementType previous, ElementType next) {
    if (AIRWAY.equals(previous) && STAR.equals(next)) {
      return Optional.of(AIRWAY_TO_STAR);
    } else if (SID.equals(previous) && STAR.equals(next)) {
      return Optional.of(SID_TO_STAR);
    } else if (AIRWAY.equals(previous) && APPROACH.equals(next)) {
      return Optional.of(AIRWAY_TO_APPROACH);
    } else if (STAR.equals(previous) && APPROACH.equals(next)) {
      return Optional.of(STAR_TO_APPROACH);
    } else if (SID.equals(previous) && APPROACH.equals(next)) {
      return Optional.of(SID_TO_APPROACH);
    }
    return Optional.empty();
  }

  ExpandedRouteLeg preferredLeg(ExpandedRouteLeg previous, ExpandedRouteLeg next) {
    if (APPROACH.equals(next.elementType()) || STAR.equals(next.elementType())) {
      return next;
    } else if (SID.equals(previous.elementType())) {
      return previous;
    } else if (AIRWAY.equals(next.elementType())) {
      return next;
    } else if (AIRWAY.equals(previous.elementType())) {
      return previous;
    } else {
      return previous;
    }
  }

  boolean isSimilarFixTerminatingLeg(Leg previous, Leg next) {
    return previous.pathTerminator().isFixTerminating()
        && next.pathTerminator().isFixTerminating()
        && matchingAssociatedFixIdentifiers(previous, next);
  }

  boolean matchingAssociatedFixIdentifiers(Leg left, Leg right) {
    return left.associatedFix().isPresent()
        && right.associatedFix().isPresent()
        && left.associatedFix().orElseThrow(IllegalStateException::new).fixIdentifier()
        .equalsIgnoreCase(right.associatedFix().orElseThrow(IllegalStateException::new).fixIdentifier());
  }
}
