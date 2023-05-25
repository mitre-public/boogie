package org.mitre.tdp.boogie.alg.chooser.graph;

import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedureGraph;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.FixRadialDistance;
import org.mitre.tdp.boogie.alg.resolve.FixTerminationLeg;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;
import org.mitre.tdp.boogie.model.BoogieFix;
import org.mitre.tdp.boogie.model.ProcedureGraph;
import org.mitre.tdp.boogie.util.Declinations;
import org.mitre.tdp.boogie.util.Streams;

import com.google.common.collect.ImmutableList;

@FunctionalInterface
public interface TokenGrapher {

  static TokenGrapher standard() {
    return new Standard();
  }

  /**
   * Convert a {@link ResolvedToken} to a graphical representation (a collection of {@link LinkedLegs}) which can be traversed as
   * part of the route choosing process.
   *
   * <p>For simple elements this may be linked legs from an element to itself (e.g. FIX->FIX) while for more complex ones it may
   * represent a full sub-graph itself (e.g. a SID or STAR).
   *
   * @param token the {@link ResolvedToken} to generate a representation for
   */
  Collection<LinkedLegs> graphRepresentationOf(ResolvedToken token);

  final class Standard implements TokenGrapher {

    private Standard() {
    }

    @Override
    public Collection<LinkedLegs> graphRepresentationOf(ResolvedToken token) {

      VisitingGrapher wrapper = new VisitingGrapher();
      token.accept(wrapper);

      return wrapper.linkedLegs();
    }

    /**
     * Visitor implementation of the various {@link ResolvedToken} implementations
     */
    private static final class VisitingGrapher implements ResolvedTokenVisitor {

      private static final ProcedureGrapher PROCEDURE_GRAPHER = new ProcedureGrapher();

      private final ImmutableList.Builder<LinkedLegs> linkedLegs;

      private VisitingGrapher() {
        this.linkedLegs = ImmutableList.builder();
      }

      public Collection<LinkedLegs> linkedLegs() {
        return linkedLegs.build();
      }

      @Override
      public void visit(ResolvedToken.StandardAirport airport) {
        Leg leg = FixTerminationLeg.IF(airport.infrastructure());
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      @Override
      public void visit(ResolvedToken.DirectToAirport airport) {
        Leg leg = FixTerminationLeg.DF(airport.infrastructure());
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      @Override
      public void visit(ResolvedToken.StandardAirway airway) {
        Streams.pairwise(airway.infrastructure().legs())
            .flatMap(pair -> Stream.of(
                // bidirectional insertion for airways
                new LinkedLegs(pair.first(), pair.second(), LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT),
                new LinkedLegs(pair.second(), pair.first(), LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT)
            ))
            .forEach(linkedLegs::add);
      }

      @Override
      public void visit(ResolvedToken.StandardApproach approach) {
        linkedLegs.addAll(PROCEDURE_GRAPHER.apply(approach.infrastructure()));
      }

      @Override
      public void visit(ResolvedToken.StandardFix fix) {
        Leg leg = FixTerminationLeg.IF(fix.infrastructure());
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      @Override
      public void visit(ResolvedToken.DirectToFix fix) {
        Leg leg = FixTerminationLeg.DF(fix.infrastructure());
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      @Override
      public void visit(ResolvedToken.StandardLatLong latLong) {
        Leg leg = FixTerminationLeg.IF(createFix(latLong.infrastructure()));
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      @Override
      public void visit(ResolvedToken.DirectToLatLong latLong) {
        Leg leg = FixTerminationLeg.DF(createFix(latLong.infrastructure()));
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      @Override
      public void visit(ResolvedToken.SidEnrouteCommon sid) {
        linkedLegs.addAll(PROCEDURE_GRAPHER.apply(Procedure.transitionMasked(sid.infrastructure(), ProcedureGrapher.enrouteOrCommon())));
      }

      @Override
      public void visit(ResolvedToken.SidRunway sid) {
        linkedLegs.addAll(PROCEDURE_GRAPHER.apply(Procedure.transitionMasked(sid.infrastructure(), ProcedureGrapher.runway())));
      }

      @Override
      public void visit(ResolvedToken.StarEnrouteCommon star) {
        linkedLegs.addAll(PROCEDURE_GRAPHER.apply(Procedure.transitionMasked(star.infrastructure(), ProcedureGrapher.enrouteOrCommon())));
      }

      @Override
      public void visit(ResolvedToken.StarRunway star) {
        linkedLegs.addAll(PROCEDURE_GRAPHER.apply(Procedure.transitionMasked(star.infrastructure(), ProcedureGrapher.runway())));
      }

      @Override
      public void visit(ResolvedToken.StandardFrd frd) {
        Leg leg = FixTerminationLeg.IF(createFix(frd.infrastructure()));
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      @Override
      public void visit(ResolvedToken.DirectToFrd frd) {
        Leg leg = FixTerminationLeg.DF(createFix(frd.infrastructure()));
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      private Fix createFix(LatLong latLong) {

        double declinationApprox = Declinations.declination(
            latLong.latitude(),
            latLong.longitude(),
            null,
            Instant.now()
        );

        return new BoogieFix.Builder()
            .fixIdentifier(latLong.toBase64())
            .fixRegion("ANONYMOUS")
            .latitude(latLong.latitude())
            .longitude(latLong.longitude())
            .modeledVariation(declinationApprox)
            .build();
      }

      private Fix createFix(FixRadialDistance frd) {
        LatLong project = frd.projectedLocation();
        return new BoogieFix.Builder()
            .fixIdentifier(frd.fix().fixIdentifier())
            .fixRegion(frd.fix().fixRegion())
            .latitude(project.latitude())
            .longitude(project.longitude())
            .elevation(null)
            .modeledVariation(frd.fix().modeledVariation())
            .build();
      }
    }


    /**
     * Standard handler for converting incoming {@link Procedure} definitions to graphical representations.
     */
    private static final class ProcedureGrapher implements Function<Procedure, Collection<LinkedLegs>> {

      private ProcedureGrapher() {
      }

      static Predicate<Transition> enrouteOrCommon() {
        return t -> TransitionType.COMMON.equals(t.transitionType()) || TransitionType.ENROUTE.equals(t.transitionType());
      }

      static Predicate<Transition> runway() {
        return t -> TransitionType.RUNWAY.equals(t.transitionType());
      }

      @Override
      public Collection<LinkedLegs> apply(Procedure procedure) {
        return procedure.transitions().isEmpty() ? Collections.emptyList() : createLegs(procedure);
      }

      private Collection<LinkedLegs> createLegs(Procedure procedure) {

        ProcedureGraph graph = newProcedureGraph(procedure);

        return graph.edgeSet().stream()
            .map(edge -> {
              Leg source = graph.getEdgeSource(edge);
              Leg target = graph.getEdgeTarget(edge);
              return new LinkedLegs(source, target, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT);
            })
            .collect(Collectors.toList());
      }
    }
  }
}
