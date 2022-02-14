package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.distanceBetween;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.finalStarTransitions;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.model.BoogieLeg;
import org.mitre.tdp.boogie.util.TransitionSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class StarToApproachLinker implements BiFunction<StarElement, ApproachElement, List<LinkedLegs>> {

  private static final Logger LOG = LoggerFactory.getLogger(StarToApproachLinker.class);

  static final StarToApproachLinker INSTANCE = new StarToApproachLinker();

  private static final Predicate<String> fixTerminatingLegs = Pattern.compile("AF|CF|DF|RF|TF|IF|HF").asPredicate();

  private static final Predicate<String> manualTerminatingLegs = Pattern.compile("HM|FM|VM").asPredicate();

  private static final Predicate<String> fixOriginatingLegs = Pattern.compile("FC|FD|HF|IF|PI|FA").asPredicate();

  private StarToApproachLinker() {
  }

  @Override
  public List<LinkedLegs> apply(StarElement starElement, ApproachElement approachElement) {

    if (starElement.toLinkedLegs().isEmpty() || approachElement.toLinkedLegs().isEmpty()) {
      return Collections.emptyList();
    }

    List<Transition> finalStarTransitions = finalStarTransitions(starElement.procedure());
    List<Transition> initialApproachTransitions = initialApproachTransitions(approachElement);
    LOG.debug("Building connections between {} final STAR transition and {} initial approach transitions.", finalStarTransitions.size(), initialApproachTransitions.size());

    List<Leg> terminalStarLegs = finalStarTransitions.stream()
        .filter(transition -> !transition.legs().isEmpty())
        .map(LinkingUtils::lastLegWithLocation)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toList());

    List<Leg> initialApproachLegs = initialApproachTransitions.stream()
        .filter(transition -> !transition.legs().isEmpty())
        .map(LinkingUtils::firstLegWithLocation)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toList());

    LOG.debug("Connecting {} terminal STAR legs to {} initial approach legs.", terminalStarLegs.size(), initialApproachLegs.size());
    List<LinkedLegs> linkedLegs = cartesianProduct(terminalStarLegs, initialApproachLegs).stream()
        .map(pair -> new LinkedLegs(pair.first(), pair.second(), distanceBetween(pair)))
        .map(leg -> glueLegBetweenStarAndApproach(leg, finalStarTransitions))
        .flatMap(List::stream)
        .sorted(Comparator.comparing(x -> x.source().sequenceNumber()))
        .collect(toList());

    return linkedLegs;
  }

  private List<Transition> initialApproachTransitions(ApproachElement approachElement) {
    return TransitionSorter.INSTANCE
        .sortApproachTransitions(approachElement.procedure().transitions())
        .stream().filter(col -> !col.isEmpty())
        .findFirst().orElseThrow(IllegalStateException::new);
  }

  /**
   * Checks to see if a leg needs to be inserted inbetween Star and Approach. Firstly, ensures that the first leg of the
   * approach is a fix originating leg. Then checks to see if the star ends with a fix terminating leg or manual terminating leg.
   * If the Star has a fix termanating leg and the distance between the approach leg is non zero, add a DF leg to the approach in between.
   * If the Star has a manual terminating leg, check the distance between. If this distance between is non zero, convert
   * the manual terminating leg of the star to a DF leg. If the distance is zero, this is an edge case where the manual terminating leg
   * needs to be dropped because it is overlapping with the fic originating leg and the closest leg in the star is used in the linking.
   */
  private List<LinkedLegs> glueLegBetweenStarAndApproach(LinkedLegs leg, List<Transition> finalStarTransitions) {
    Preconditions.checkArgument(fixOriginatingLegs.test(leg.target().pathTerminator().toString()), "Approaches can't start with non-fix-originating legs");

    List<LinkedLegs> newLegs = new ArrayList<>();
    if (fixTerminatingLegs.test(leg.source().pathTerminator().toString()) && leg.linkWeight() > 1.0E-5) {
      newLegs.addAll(fixTerminatingStarWithNonZeroDistanceAdjustment(leg));
    } else if (manualTerminatingLegs.test(leg.source().pathTerminator().toString())) {
      newLegs.addAll(manualTerminatingStarAdjustment(leg, finalStarTransitions, leg.linkWeight() > 1.0E-5));
    }

    return newLegs.isEmpty() ? List.of(leg) : newLegs;
  }

  /**
   * Clones first leg of approach, makes it a DF, and inserts it in between the fix terminating leg of star and fix originating leg
   * of approach resulting in 2 LinkedLegs
   */
  private List<LinkedLegs> fixTerminatingStarWithNonZeroDistanceAdjustment(LinkedLegs leg) {
    List<LinkedLegs> adjustedLegs = new ArrayList<>();
    Leg newDFApproachLeg = dfLegOf(leg.target());

    Pair<Leg, Leg> starToNewLeg = Pair.of(leg.source(), newDFApproachLeg);
    Pair<Leg, Leg> newLegToApproach = Pair.of(newDFApproachLeg, leg.target());

    adjustedLegs.add(new LinkedLegs(starToNewLeg.first(), starToNewLeg.second(), distanceBetween(starToNewLeg)));
    adjustedLegs.add(new LinkedLegs(newLegToApproach.first(), newLegToApproach.second(), distanceBetween(newLegToApproach)));

    return adjustedLegs;
  }

  /**
   * Removes the manual terminating leg of star and replaces it with the closest previous leg in the star with a fix. If the distance between
   * the approach and star was not zero, insert a cloned DF leg of the first approach leg. Otherwise, just link the closest previous with the
   * initial approach leg
   */
  private List<LinkedLegs> manualTerminatingStarAdjustment(LinkedLegs leg, List<Transition> finalStarTransitions, boolean distanceBetween) {
    Leg manualTerminatingStarLeg = leg.source();
    Leg initialApproachLeg = leg.target();
    Leg clonedDFApproachLeg = dfLegOf(initialApproachLeg);

    List<Leg> closestPreviousStarLeg = finalStarTransitions.stream()
        .filter(t -> t.legs().contains(manualTerminatingStarLeg))
        .map(t -> t.legs().stream().filter(l -> l.associatedFix().isPresent()).collect(toList()))
        .map(t -> t.get(t.size() - 2))
        .collect(toList());

    List<LinkedLegs> adjustedLegs = new ArrayList<>();
    for(Leg closestPreviousLeg : closestPreviousStarLeg) {
      Pair<Leg, Leg> pair = Pair.of(closestPreviousLeg, distanceBetween ? clonedDFApproachLeg : initialApproachLeg);
      adjustedLegs.add(new LinkedLegs(pair.first(), pair.second(), distanceBetween(pair)));
    }
    if(distanceBetween) {
      adjustedLegs.add(new LinkedLegs(clonedDFApproachLeg, initialApproachLeg, 0));
    }

    return adjustedLegs;
  }

  /**
   * Converts the path terminator of the inputted leg to DF.
   */
  private Leg dfLegOf(Leg leg) {
    return new BoogieLeg.Builder()
        .associatedFix(leg.associatedFix().orElse(null))
        .recommendedNavaid(leg.recommendedNavaid().orElse(null))
        .centerFix(leg.centerFix().orElse(null))
        .pathTerminator(PathTerminator.DF)
        .sequenceNumber(leg.sequenceNumber())
        .outboundMagneticCourse(leg.outboundMagneticCourse().orElse(null))
        .rho(leg.rho().orElse(null))
        .rnp(leg.rnp().orElse(null))
        .routeDistance(leg.routeDistance().orElse(null))
        .holdTime(leg.holdTime().orElse(null))
        .verticalAngle(leg.verticalAngle().orElse(null))
        .speedConstraint(leg.speedConstraint())
        .altitudeConstraint(leg.altitudeConstraint())
        .turnDirection(leg.turnDirection().orElse(null))
        .isFlyOverFix(leg.isFlyOverFix())
        .isPublishedHoldingFix(leg.isPublishedHoldingFix())
        .build();
  }
}