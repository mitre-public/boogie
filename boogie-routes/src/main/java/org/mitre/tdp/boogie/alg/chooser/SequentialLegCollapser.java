package org.mitre.tdp.boogie.alg.chooser;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.mitre.tdp.boogie.util.Streams.pairwise;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.ExpandedRouteLeg;import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.model.BoogieLeg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A sequential leg combiner attempts to <i>safely</i> combine the definitions of two legs when certain conditions are met.
 * <br>
 * The key thing that makes this operation safe is that it preserves the (more constraining) of the altitude and speed constraints
 * available at a fix along the expansion.
 */
final class SequentialLegCollapser implements UnaryOperator<List<ExpandedRouteLeg>> {

  private static final Logger LOG = LoggerFactory.getLogger(SequentialLegCollapser.class);

  @Override
  public List<ExpandedRouteLeg> apply(List<ExpandedRouteLeg> legs) {

    Map<ExpandedRouteLeg, ExpandedRouteLeg> embedding = pairwise(legs)
        .filter(pair -> isSimilarFixTerminatingLeg(pair.first().leg(), pair.second().leg()))
        .collect(toMap(Pair::second, pair -> safelyMergeLegs(pair.first(), pair.second())));

    LOG.debug("Generated embedding for {} legs of {} total.", embedding.size(), legs.size());

    return legs.stream().map(leg -> embedding.getOrDefault(leg, leg)).collect(toList());
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
  private ExpandedRouteLeg safelyMergeLegs(ExpandedRouteLeg previous, ExpandedRouteLeg next) {
    ExpandedRouteLeg preferred = preferredLeg(previous, next);

    Leg combined = new BoogieLeg.Builder()
        .associatedFix(preferred.associatedFix().orElse(null))
        .recommendedNavaid(preferred.recommendedNavaid().orElse(null))
        // intentional - keep the previous path type (as the termination is identical)
        // this should mostly be TF's anyway...
        .pathTerminator(preferred.pathTerminator())
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

    return new ExpandedRouteLeg(preferred.section(), preferred.elementType(), previous.wildcards().concat(next.wildcards()), combined);
  }

  ExpandedRouteLeg preferredLeg(ExpandedRouteLeg previous, ExpandedRouteLeg next) {
    if (ElementType.APPROACH.equals(next.elementType()) || ElementType.STAR.equals(next.elementType())) {
      return next;
    } else if (ElementType.SID.equals(previous.elementType())) {
      return previous;
    } else if (ElementType.AIRWAY.equals(next.elementType())) {
      return next;
    } else if (ElementType.AIRWAY.equals(previous.elementType())) {
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
