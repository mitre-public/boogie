package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedureGraph;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Stream;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;
import org.mitre.tdp.boogie.model.ProcedureGraph;
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
   * <p>For simple elements this may be linked legs from an element to itself (e.g. {@code FIX->FIX}) while for more complex ones
   * it may represent a full sub-graph itself (e.g. a SID or STAR).
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
    static final class VisitingGrapher implements ResolvedTokenVisitor {

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
        Leg leg = Leg.ifBuilder(createFix(airport.infrastructure()), 0).build();
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      @Override
      public void visit(ResolvedToken.DirectToAirport airport) {
        Leg leg = Leg.dfBuilder(createFix(airport.infrastructure()), 0).build();
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
        Leg leg = Leg.ifBuilder(fix.infrastructure(), 0).build();
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      @Override
      public void visit(ResolvedToken.DirectToFix fix) {
        Leg leg = Leg.dfBuilder(fix.infrastructure(), 0).build();
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      @Override
      public void visit(ResolvedToken.StandardLatLong latLong) {
        Leg leg = Leg.ifBuilder(createFix(latLong.infrastructure()), 0).build();
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      @Override
      public void visit(ResolvedToken.DirectToLatLong latLong) {
        Leg leg = Leg.dfBuilder(createFix(latLong.infrastructure()), 0).build();
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      @Override
      public void visit(ResolvedToken.SidEnrouteCommon sid) {
        linkedLegs.addAll(PROCEDURE_GRAPHER.apply(sid.infrastructure()));
      }

      @Override
      public void visit(ResolvedToken.SidRunway sid) {
        linkedLegs.addAll(PROCEDURE_GRAPHER.apply(sid.infrastructure()));
      }

      @Override
      public void visit(ResolvedToken.StarEnrouteCommon star) {
        linkedLegs.addAll(PROCEDURE_GRAPHER.apply(star.infrastructure()));
      }

      @Override
      public void visit(ResolvedToken.StarRunway star) {
        linkedLegs.addAll(PROCEDURE_GRAPHER.apply(star.infrastructure()));
      }

      @Override
      public void visit(ResolvedToken.StandardFrd frd) {
        Leg leg = Leg.ifBuilder(frd.infrastructure(), 0).build();
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      @Override
      public void visit(ResolvedToken.DirectToFrd frd) {
        Leg leg = Leg.dfBuilder(frd.infrastructure(), 0).build();
        linkedLegs.add(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
      }

      private Fix createFix(LatLong latLong) {
        return Fix.builder()
            .fixIdentifier(latLong.toBase64())
            .latLong(latLong)
            .build();
      }

      private Fix createFix(Airport airport) {
        return Fix.builder()
            .fixIdentifier(airport.airportIdentifier())
            .latLong(airport.latLong())
            .magneticVariation(airport.magneticVariation().orElse(null))
            .build();
      }
    }

    /**
     * Standard handler for converting incoming {@link Procedure} definitions to graphical representations.
     */
    static final class ProcedureGrapher implements Function<Procedure, Collection<LinkedLegs>> {

      ProcedureGrapher() {
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
            .collect(toList());
      }
    }
  }
}
