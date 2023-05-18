package org.mitre.tdp.boogie.alg.chooser;

import static java.util.stream.Collectors.toMap;
import static org.mitre.tdp.boogie.util.Streams.pairwise;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.ExpandedRouteLeg;
import org.mitre.tdp.boogie.alg.resolve.RouteTokenResolver;
import org.mitre.tdp.boogie.alg.split.Wildcard;
import org.mitre.tdp.boogie.model.BoogieLeg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Due to the nature of the {@link RouteTokenResolver} step in the expansion process we get DF->DF leg pairings often when expanding
 * routes which look similar to the following:
 * <br>
 * Route String: KDCA..JMACK..HUNTN..TABLO..KORD
 * <br>
 * Expanded Leg Sequence: KDCA (IF) -> JMACK (DF) -> HUNTN (DF) -> TABLO (DF) -> KORD (IF)
 * <br>
 * This behavior is an artifact of the resolution process - where these individual "direct-to" waypoint filings on their own
 * don't provide enough context natively to know whether they would be flown as TF or DF leg types by the FMS.
 * <br>
 * e.g. (JMACK->HUNTN, HUNTN->TABLO) would be flown as TF's by the FMS because the previous leg terminates in a fix.
 * <br>
 * So tagging them as such (within the expansion) would require knowing the sequence of legs produced by the expander which isn't
 * possible until the expansion has been performed...
 * <br>
 * As such this class exists to modify the leg types of these expanded legs if they appear in a resultant expanded sequence as the
 * overall goal of the expansion process is to create a route from the plan which looks as similar as possible to what the FMS
 * would generate if it was internally doing the expansion.
 * <br>
 * Note the return of this class is the mapping from the original Leg implementation to the updated implementation as a potentially
 * transformed DF->TF leg.
 */
final class SubsequentDfToTfConverter implements UnaryOperator<List<ExpandedRouteLeg>> {

  private static final Logger LOG = LoggerFactory.getLogger(SubsequentDfToTfConverter.class);

  @Override
  public List<ExpandedRouteLeg> apply(List<ExpandedRouteLeg> legs) {

    Map<ExpandedRouteLeg, ExpandedRouteLeg> embedding = pairwise(legs)
        .map(pair -> updateSequentialDFLegsToTF(pair.first(), pair.second()))
        .collect(toMap(Pair::first, Pair::second));

    LOG.debug("Generated embedding for {} legs of {} total.", embedding.size(), legs.size());

    return legs.stream().map(leg -> embedding.getOrDefault(leg, leg)).collect(Collectors.toList());
  }

  /**
   * Updates the latter of the two provided legs to be a TF type if:
   * <br>
   * 1. The previous leg is of an xF leg type (it terminates in a fix)
   * 2. The next leg is a DF leg type - and the section name (from the route string) matches the name of the terminal fix
   */
  Pair<ExpandedRouteLeg, ExpandedRouteLeg> updateSequentialDFLegsToTF(ExpandedRouteLeg previous, ExpandedRouteLeg next) {
    if (previous.leg().pathTerminator().isFixTerminating() &&
        !Wildcard.TAILORED.test(previous.wildcards()) &&
        sectionMatchesFix(next) &&
        PathTerminator.DF.equals(next.leg().pathTerminator())) {

      LOG.debug("Found leg matching criteria {} - updating type to TF from DF. Previous leg was {}", next, previous);
      return Pair.of(next, new ExpandedRouteLeg(next.section(), next.elementType(), next.wildcards(), makeIntoTfLeg(next.leg())));
    }
    return Pair.of(next, next);
  }

  boolean sectionMatchesFix(ExpandedRouteLeg resolvedLeg) {
    return resolvedLeg.leg()
        .associatedFix()
        .map(Fix::fixIdentifier)
        .filter(resolvedLeg.section()::equalsIgnoreCase)
        .isPresent();
  }

  private Leg makeIntoTfLeg(Leg leg) {
    return new BoogieLeg.Builder()
        .associatedFix(leg.associatedFix().orElse(null))
        .recommendedNavaid(leg.recommendedNavaid().orElse(null))
        .pathTerminator(PathTerminator.TF)
        .sequenceNumber(leg.sequenceNumber())
        .speedConstraint(leg.speedConstraint())
        .altitudeConstraint(leg.altitudeConstraint())
        .outboundMagneticCourse(leg.outboundMagneticCourse().orElse(null))
        .theta(leg.theta().orElse(null))
        .rho(leg.rho().orElse(null))
        .rnp(leg.rnp().orElse(null))
        .routeDistance(leg.routeDistance().orElse(null))
        .holdTime(leg.holdTime().orElse(null))
        .isPublishedHoldingFix(leg.isPublishedHoldingFix())
        .isFlyOverFix(leg.isFlyOverFix())
        .build();
  }
}
