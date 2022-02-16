package org.mitre.tdp.boogie.alg.resolve;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.model.BoogieLeg;

import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.distanceBetween;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.finalSidTransitions;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.finalStarTransitions;

public class SectionGluer implements BiFunction<List<LinkedLegs>, ResolvedElement, List<LinkedLegs>> {

  private static final Predicate<String> fixTerminatingLegs = Pattern.compile("AF|CF|DF|RF|TF|IF|HF").asPredicate();

  private static final Predicate<String> manualTerminatingLegs = Pattern.compile("HM|FM|VM").asPredicate();

  private static final Predicate<String> fixOriginatingLegs = Pattern.compile("FC|FD|HF|IF|PI|FA").asPredicate();
  
  @Override
  public List<LinkedLegs> apply(List<LinkedLegs> linkedLegs, ResolvedElement resolvedElement) {
    return linkedLegs.stream()
        .map(leg -> glueLegBetweenStarAndApproach(leg, resolvedElement))
        .flatMap(List::stream)
        .sorted(Comparator.comparing(x -> x.source().sequenceNumber()))
        .collect(toList());
  }

  /**
   * Checks to see if a leg needs to be inserted inbetween ResolvedElement and Approach. Firstly, ensures that the first leg of the
   * approach is a fix originating leg. Then checks to see if the RE ends with a fix terminating leg or manual terminating leg.
   * If the RE has a fix termanating leg and the distance between the approach leg is non zero, add a DF leg to the approach in between.
   * If the Procedure has a manual terminating leg, check the distance between. If this distance between is non zero, convert
   * the manual terminating leg of the procedure to a DF leg. If the distance is zero, this is an edge case where the manual terminating leg
   * needs to be dropped because it is overlapping with the fic originating leg and the closest leg in the procedure is used in the linking.
   */
  private List<LinkedLegs> glueLegBetweenStarAndApproach(LinkedLegs leg, ResolvedElement resolvedElement) {
    Preconditions.checkArgument(fixOriginatingLegs.test(leg.target().pathTerminator().toString()), "Approaches can't start with non-fix-originating legs");

    List<LinkedLegs> newLegs = new ArrayList<>();
    if (fixTerminatingLegs.test(leg.source().pathTerminator().toString()) && leg.linkWeight() > 1.0E-5) {
      newLegs.addAll(fixTerminatingStarWithNonZeroDistanceAdjustment(leg));
    } else if (resolvedElement instanceof ProcedureElement && manualTerminatingLegs.test(leg.source().pathTerminator().toString())) {
      ProcedureElement procedureElement = (ProcedureElement) resolvedElement;
      newLegs.addAll(manualTerminatingProcedureAdjustment(leg, procedureElement.procedure(), leg.linkWeight() > 1.0E-5));
    }

    return newLegs.isEmpty() ? List.of(leg) : newLegs;
  }

  /**
   * Clones first leg of approach, makes it a DF, and inserts it in between the fix terminating leg of resolved element and fix originating leg
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
   * Removes the manual terminating leg of a SID/STAR and replaces it with the closest previous leg in the star with a fix. If the distance between
   * the approach and procedure was not zero, insert a cloned DF leg of the first approach leg. Otherwise, just link the closest previous with the
   * initial approach leg
   */
  private List<LinkedLegs> manualTerminatingProcedureAdjustment(LinkedLegs leg, Procedure procedure, boolean distanceBetween) {
    Leg manualTerminatingLeg = leg.source();
    Leg initialApproachLeg = leg.target();
    Leg clonedDFApproachLeg = dfLegOf(initialApproachLeg);

    List<Transition> finalTransitions = new ArrayList<>();
    if(procedure.procedureType().equals(ProcedureType.SID)) {
      finalTransitions.addAll(finalSidTransitions(procedure));
    } else if(procedure.procedureType().equals(ProcedureType.STAR)) {
      finalTransitions.addAll(finalStarTransitions(procedure));
    } else {
      throw new IllegalStateException("Manual terminating procedure adjustment can only be applied on SID/STAR");
    }

    List<Leg> closestPreviousProcedureLeg = closestPreviousProcedureLeg(finalTransitions, manualTerminatingLeg);

    List<LinkedLegs> adjustedLegs = new ArrayList<>();
    if(!closestPreviousProcedureLeg.isEmpty()) {
      for (Leg closestPreviousLeg : closestPreviousProcedureLeg) {
        Pair<Leg, Leg> pair = Pair.of(closestPreviousLeg, distanceBetween ? clonedDFApproachLeg : initialApproachLeg);
        adjustedLegs.add(new LinkedLegs(pair.first(), pair.second(), distanceBetween(pair)));
      }
      if (distanceBetween) {
        adjustedLegs.add(new LinkedLegs(clonedDFApproachLeg, initialApproachLeg, 0));
      }
    } else {
      if(distanceBetween) {
        adjustedLegs.add(new LinkedLegs(manualTerminatingLeg, clonedDFApproachLeg, distanceBetween(Pair.of(manualTerminatingLeg, clonedDFApproachLeg))));
      }
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

  private List<Leg> closestPreviousProcedureLeg(List<Transition> finalTransitions, Leg manualTerminatingLeg) {
    return finalTransitions.stream()
        .filter(t -> t.legs().contains(manualTerminatingLeg))
        .map(t -> t.legs().stream().filter(l -> l.associatedFix().isPresent()).collect(toList()))
        .filter(legs -> legs.size() > 1)
        .map(t -> t.get(t.size() - 2))
        .collect(toList());
  }
}
