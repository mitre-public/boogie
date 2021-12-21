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
        .map(this::glueLegBetweenStarAndApproach)
        .map(leg -> checkForOverlapInManualTerminatingStarCase(leg, finalStarTransitions))
        .flatMap(List::stream)
        .sorted(Comparator.comparing(x -> x.source().sequenceNumber()))
        .collect(toList());

    return linkedLegs.size() > 1 ? verifySequenceNumbers(linkedLegs) : linkedLegs;
  }

  private List<Transition> initialApproachTransitions(ApproachElement approachElement) {
    return TransitionSorter.INSTANCE
        .sortApproachTransitions(approachElement.procedure().transitions())
        .stream().filter(col -> !col.isEmpty())
        .findFirst().orElseThrow(IllegalStateException::new);
  }

  /**
   * Edge case where the manual terminating star leg starts at the fix originating approach leg. In this instance, drop the manual terminating
   * star leg and take the closest previous leg from the transition and form the link using that leg instead.
   */
  private List<LinkedLegs> checkForOverlapInManualTerminatingStarCase(List<LinkedLegs> legs, List<Transition> finalStarTransitions) {
    Optional<LinkedLegs> overlappingLeg = legs.stream().filter(leg -> leg.linkWeight() == 999999.0).findFirst();
    if(!overlappingLeg.isPresent()) {
      return legs;
    }
    List<Leg> terminalStarLegs = finalStarTransitions.stream()
        .filter(transition -> transition.legs().contains(overlappingLeg.get().source()))
        .map(Transition::legs)
        .flatMap(List::stream)
        .filter(leg -> leg.associatedFix().isPresent())
        .collect(toList());

    Leg closestPreviousStarLeg = terminalStarLegs.get(terminalStarLegs.size() - 2);
    Leg replacedOverlappingLeg = convertToDF(overlappingLeg.get().target());

    return asList(
        new LinkedLegs(closestPreviousStarLeg, replacedOverlappingLeg, distanceBetween(Pair.of(closestPreviousStarLeg, replacedOverlappingLeg))),
        new LinkedLegs(replacedOverlappingLeg, overlappingLeg.get().target(), distanceBetween(Pair.of(replacedOverlappingLeg, overlappingLeg.get().target()))));
  }

  /**
   * Checks to see if a leg needs to be inserted inbetween Star and Approach. Firstly, ensures that the first leg of the
   * approach is a fix originating leg. Then checks to see if the star ends with a fix terminating leg or manual terminating leg.
   * If the Star has a fix termanating leg and the distance between the approach leg is non zero, add a DF leg to the approach in between.
   * If the Star has a manual terminating leg, check the distance between. If this distance between is non zero, convert
   * the manual terminating leg of the star to a DF leg. If the distance is zero, this is an edge case where the manual terminating leg
   * needs to be dropped because it is overlapping with the fic originating leg and the closest leg in the star is used in the linking.
   */
  private List<LinkedLegs> glueLegBetweenStarAndApproach(LinkedLegs leg) {
    Preconditions.checkArgument(fixOriginatingLegs.test(leg.target().pathTerminator().toString()), "Approaches can't start with non-fix-originating legs");

    List<LinkedLegs> newLegs = new ArrayList<>();
    if (fixTerminatingLegs.test(leg.source().pathTerminator().toString()) && leg.linkWeight() != 0) {
      newLegs.addAll(fixTerminatingStarWithNonZeroDistanceAdjustment(leg));
    } else if (manualTerminatingLegs.test(leg.source().pathTerminator().toString())) {
      if (leg.linkWeight() != 0) { //should i make this less than a certain threshold? like only add/convert leg if the distance between hte manual terminating leg and first approach is less than 0.5 miles
        newLegs.addAll(manualTerminatingStarWithNonZeroDistanceAdjustment(leg));
      } else {
        newLegs.add(new LinkedLegs(leg.source(), leg.target(),999999.0));
      }
    }

    return newLegs.isEmpty() ? asList(leg) : newLegs;
  }

  /**
   * Clones first leg of approach, makes it a DF, and inserts it in between the fix terminating leg of star and fix originating leg
   * of approach
   */
  private List<LinkedLegs> fixTerminatingStarWithNonZeroDistanceAdjustment(LinkedLegs leg) {
    List<LinkedLegs> adjustedLegs = new ArrayList<>();
    BoogieLeg newDFApproachLeg = convertToDF(leg.target());
    Pair<Leg, Leg> starToNewLeg = Pair.of(leg.source(), newDFApproachLeg);
    Pair<Leg, Leg> newLegToApproach = Pair.of(newDFApproachLeg, leg.target());
    adjustedLegs.add(new LinkedLegs(starToNewLeg.first(), starToNewLeg.second(), distanceBetween(starToNewLeg)));
    adjustedLegs.add(new LinkedLegs(newLegToApproach.first(), newLegToApproach.second(), distanceBetween(newLegToApproach)));
    return adjustedLegs;
  }

  /**
   * Replaces the manual terminating leg of star with a DF to the first leg of approach.
   */
  private List<LinkedLegs> manualTerminatingStarWithNonZeroDistanceAdjustment(LinkedLegs leg) {
    List<LinkedLegs> adjustedLegs = new ArrayList<>();
    Pair<Leg, Leg> pair = Pair.of(setSequenceNumberOfLeg(convertToDF(leg.target()), leg.target().sequenceNumber() - 1), leg.target());
    adjustedLegs.add(new LinkedLegs(pair.first(), pair.second(), distanceBetween(pair)));
    return adjustedLegs;
  }

  /**
   * Converts the path terminator of the inputted leg to DF.
   */
  private BoogieLeg convertToDF(Leg leg) {
    return ((BoogieLeg) leg).toBuilder().pathTerminator(PathTerminator.DF).build();
  }

  /**
   * Parses through linked legs to verify sequence numbers are all incrementing by 1 starting with the first LinkedLeg.
   */
  private List<LinkedLegs> verifySequenceNumbers(List<LinkedLegs> linkedLegs) {
    List<LinkedLegs> resultingLegs = new ArrayList<>();
    int currentSequenceNumber = linkedLegs.get(0).source().sequenceNumber();
    for(Iterator<LinkedLegs> it = linkedLegs.iterator(); it.hasNext(); ) {
      LinkedLegs legs = it.next();
      Leg source = legs.source();
      Leg target = legs.target();
      if(source.sequenceNumber() != currentSequenceNumber) {
        source = setSequenceNumberOfLeg(source, currentSequenceNumber);
      }
      if(target.sequenceNumber() != source.sequenceNumber() - 1) {
        target = setSequenceNumberOfLeg(target, currentSequenceNumber + 1);
      }
      LinkedLegs newLegs = new LinkedLegs(source, target, distanceBetween(Pair.of(source, target)));
      resultingLegs.add(newLegs);
      currentSequenceNumber = target.sequenceNumber();
    }
    return resultingLegs;
  }

  private BoogieLeg setSequenceNumberOfLeg(Leg leg, int newSequenceNumber) {
    return ((BoogieLeg) leg).toBuilder().sequenceNumber(newSequenceNumber).build();
  }
}