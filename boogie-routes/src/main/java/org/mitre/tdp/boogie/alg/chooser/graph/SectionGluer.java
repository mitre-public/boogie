package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.alg.chooser.graph.LinkingSupport.distanceBetween;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;

import com.google.common.base.Preconditions;

final class SectionGluer implements BiFunction<Collection<LinkedLegs>, LinkableToken, List<LinkedLegs>> {

  private static final Predicate<String> fixTerminatingLegs = Pattern.compile("AF|CF|DF|RF|TF|IF|HF").asPredicate();

  private static final Predicate<String> manualTerminatingLegs = Pattern.compile("HM|FM|VM").asPredicate();

  private static final Predicate<String> fixOriginatingLegs = Pattern.compile("FC|FD|HF|IF|PI|FA").asPredicate();

  @Override
  public List<LinkedLegs> apply(Collection<LinkedLegs> linkedLegs, LinkableToken resolvedToken) {
    return linkedLegs.stream()
        .map(leg -> glueLegBetweenStarAndApproach(leg, resolvedToken))
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
  private List<LinkedLegs> glueLegBetweenStarAndApproach(LinkedLegs leg, LinkableToken resolvedToken) {
    Preconditions.checkArgument(fixOriginatingLegs.test(leg.target().pathTerminator().toString()), "Approaches can't start with non-fix-originating legs:" + leg.target().pathTerminator() + "/" + leg.target().associatedFix().map(Fix::fixIdentifier));

    List<LinkedLegs> newLegs = new ArrayList<>();

    Optional<Procedure> procedure = ProcedureVisitor.get(resolvedToken);

    if (fixTerminatingLegs.test(leg.source().pathTerminator().toString()) && leg.linkWeight() > 1.0E-5) {
      newLegs.addAll(fixTerminatingStarWithNonZeroDistanceAdjustment(leg));
    } else if (procedure.isPresent() && manualTerminatingLegs.test(leg.source().pathTerminator().toString())) {
      newLegs.addAll(manualTerminatingProcedureAdjustment(leg, procedure.get(), leg.linkWeight() > 1.0E-5));
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

    List<Transition> finalTransitions = procedure.finalTransitions();

    List<Leg> closestPreviousProcedureLeg = closestPreviousProcedureLeg(finalTransitions, manualTerminatingLeg);

    List<LinkedLegs> adjustedLegs = new ArrayList<>();
    if (!closestPreviousProcedureLeg.isEmpty()) {
      for (Leg closestPreviousLeg : closestPreviousProcedureLeg) {
        Pair<Leg, Leg> pair = Pair.of(closestPreviousLeg, distanceBetween ? clonedDFApproachLeg : initialApproachLeg);
        adjustedLegs.add(new LinkedLegs(pair.first(), pair.second(), distanceBetween(pair)));
      }
      if (distanceBetween) {
        adjustedLegs.add(new LinkedLegs(clonedDFApproachLeg, initialApproachLeg, 0));
      }
    } else {
      if (distanceBetween) {
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
    return Leg.dfBuilder(leg.associatedFix().orElseThrow(), leg.sequenceNumber())
        .recommendedNavaid(leg.recommendedNavaid().orElse(null))
        .rho(leg.rho().orElse(null))
        .theta(leg.theta().orElse(null))
        .rnp(leg.rnp().orElse(null))
        .altitudeConstraint(leg.altitudeConstraint())
        .speedConstraint(leg.speedConstraint())
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

  private static final class ProcedureVisitor implements LinkableTokenVisitor {

    private Procedure procedure;

    private ProcedureVisitor() {
    }

    static Optional<Procedure> get(LinkableToken token) {
      ProcedureVisitor visitor = new ProcedureVisitor();
      token.accept(visitor);
      return ofNullable(visitor.procedure);
    }

    @Override
    public void visit(AnyAirport airport) {
    }

    @Override
    public void visit(AnyAirway airway) {
    }

    @Override
    public void visit(AnyApproach approach) {
      this.procedure = approach.approach();
    }

    @Override
    public void visit(AnyFix fix) {
    }

    @Override
    public void visit(AnyLatLong latLong) {
    }

    @Override
    public void visit(AnySid sid) {
      this.procedure = sid.sid();
    }

    @Override
    public void visit(AnyStar star) {
      this.procedure = star.star();
    }

    @Override
    public void visit(AnyFrd frd) {
    }
  }
}
