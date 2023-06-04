package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Optional;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;

@FunctionalInterface
public interface TokenMapper {

  static Standard standard() {
    return new Standard(TokenGrapher.standard());
  }

  /**
   * Converts an incoming resolved token to a linkable version which adds its representation as a subgraph as well as linking
   * behavior between it and other token types.
   *
   * @param resolvedToken the resolved token from the source route string
   */
  LinkableToken map(ResolvedToken resolvedToken);

  /**
   * Leverages the double-dispatch pattern to decorate the provided {@link ResolvedToken}s as {@link LinkableToken}s and then
   * delegates to those to convert pairings of tokens to collections of {@link LinkedLegs} linking them.
   */
  final class Standard implements TokenMapper {

    private final TokenGrapher grapher;

    private Standard(TokenGrapher grapher) {
      this.grapher = requireNonNull(grapher);
    }

    @Override
    public LinkableToken map(ResolvedToken resolvedToken) {

      ResolvedTokenWrapper wrapper = new ResolvedTokenWrapper();
      resolvedToken.accept(wrapper);

      return wrapper.token()
          .orElseThrow(() -> new IllegalStateException("Couldn't handle element: " + resolvedToken + " as graphable."));
    }

    static Predicate<Transition> common() {
      return t -> TransitionType.COMMON.equals(t.transitionType());
    }

    static Predicate<Transition> enroute() {
      return t -> TransitionType.ENROUTE.equals(t.transitionType());
    }

    static Predicate<Transition> runway() {
      return t -> TransitionType.RUNWAY.equals(t.transitionType());
    }

    private final class ResolvedTokenWrapper implements ResolvedTokenVisitor {

      private LinkableToken token;

      private ResolvedTokenWrapper() {
      }

      public Optional<LinkableToken> token() {
        return ofNullable(token);
      }

      @Override
      public void visit(ResolvedToken.StandardAirport airport) {
        this.token = LinkableToken.anyAirport(airport.infrastructure(), grapher.graphRepresentationOf(airport));
      }

      @Override
      public void visit(ResolvedToken.DirectToAirport airport) {
        this.token = LinkableToken.anyAirport(airport.infrastructure(), grapher.graphRepresentationOf(airport));
      }

      @Override
      public void visit(ResolvedToken.StandardAirway airway) {
        this.token = LinkableToken.anyAirway(airway.infrastructure(), grapher.graphRepresentationOf(airway));
      }

      @Override
      public void visit(ResolvedToken.StandardApproach approach) {
        this.token = LinkableToken.anyApproach(approach.infrastructure(), grapher.graphRepresentationOf(approach));
      }

      @Override
      public void visit(ResolvedToken.StandardFix fix) {
        this.token = LinkableToken.anyFix(fix.infrastructure(), grapher.graphRepresentationOf(fix));
      }

      @Override
      public void visit(ResolvedToken.DirectToFix fix) {
        this.token = LinkableToken.anyFix(fix.infrastructure(), grapher.graphRepresentationOf(fix));
      }

      @Override
      public void visit(ResolvedToken.StandardLatLong latLong) {
        this.token = LinkableToken.anyLatLong(latLong.infrastructure(), grapher.graphRepresentationOf(latLong));
      }

      @Override
      public void visit(ResolvedToken.DirectToLatLong latLong) {
        this.token = LinkableToken.anyLatLong(latLong.infrastructure(), grapher.graphRepresentationOf(latLong));
      }

      @Override
      public void visit(ResolvedToken.SidEnrouteCommon sid) {
        Procedure masked = Procedure.maskTransitions(sid.infrastructure(), runway());
        this.token = LinkableToken.anySid(masked, grapher.graphRepresentationOf(sid));
      }

      @Override
      public void visit(ResolvedToken.SidRunway sid) {
        Procedure masked = Procedure.maskTransitions(sid.infrastructure(), common().or(enroute()));
        this.token = LinkableToken.anySid(masked, grapher.graphRepresentationOf(sid));
      }

      @Override
      public void visit(ResolvedToken.StarEnrouteCommon star) {
        Procedure masked = Procedure.maskTransitions(star.infrastructure(), runway());
        this.token = LinkableToken.anyStar(masked, grapher.graphRepresentationOf(star));
      }

      @Override
      public void visit(ResolvedToken.StarRunway star) {
        Procedure masked = Procedure.maskTransitions(star.infrastructure(), common().or(enroute()));
        this.token = LinkableToken.anyStar(masked, grapher.graphRepresentationOf(star));
      }

      @Override
      public void visit(ResolvedToken.StandardFrd frd) {
        this.token = LinkableToken.anyFrd(frd.infrastructure(), grapher.graphRepresentationOf(frd));
      }

      @Override
      public void visit(ResolvedToken.DirectToFrd frd) {
        this.token = LinkableToken.anyFrd(frd.infrastructure(), grapher.graphRepresentationOf(frd));
      }
    }
  }
}
