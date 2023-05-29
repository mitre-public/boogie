package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.chooser.GraphBasedRouteChooser;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;

/**
 * A linking strategy represents a methodology of linking pairs of subsequently-filed {@link ResolvedToken}s from an underlying
 * route such that they can be traversed through by the {@link GraphBasedRouteChooser}.
 *
 * <p>These strategies allow clients to emphasize and de-emphasize certain combinations of elements to tailor expansion to better
 * fit their needs.
 */
public interface LinkingStrategy {

  /**
   * The standard linking strategy for use with the {@link GraphBasedRouteChooser}.
   *
   * <p>This strategy handles generating links between all commonly filed portions of a route as well as setting sane weights for
   * their relationships.
   *
   * <p>It uses information about the specific type of resolved infrastructure and its generated graphical representation to make
   * links between one piece of infrastructure and another (hence needing a {@link TokenGrapher}).
   *
   * <p>This strategy is meant to be robust across all common use cases, if a client wants more control over the linking of specific
   * pairs of {@link ResolvedToken}s they should use the {@link #overridable(LinkingStrategy)} decorator on top of this instance.
   */
  static LinkingStrategy standard(TokenGrapher grapher) {
    return new Standard(grapher);
  }

  /**
   *
   */
  Collection<LinkedLegs> links(ResolvedToken left, ResolvedToken right);

  /**
   * Leverages the double-dispatch pattern to decorate the provided {@link ResolvedToken}s as {@link LinkableToken}s and then
   * delegates to those to convert pairings of tokens to collections of {@link LinkedLegs} linking them.
   */
  final class Standard implements LinkingStrategy {

    private final TokenGrapher grapher;

    private Standard(TokenGrapher grapher) {
      this.grapher = requireNonNull(grapher);
    }

    @Override
    public Collection<LinkedLegs> links(ResolvedToken left, ResolvedToken right) {
      return wrap(left).accept(wrap(right)).links();
    }

    private LinkableToken wrap(ResolvedToken element) {

      ResolvedTokenWrapper wrapper = new ResolvedTokenWrapper();
      element.accept(wrapper);

      return wrapper.token()
          .orElseThrow(() -> new IllegalStateException("Couldn't handle element: " + element + " as graphable."));
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
        Procedure masked = Procedure.transitionMasked(sid.infrastructure(), common().or(enroute()));
        this.token = LinkableToken.anySid(masked, grapher.graphRepresentationOf(sid));
      }

      @Override
      public void visit(ResolvedToken.SidRunway sid) {
        Procedure masked = Procedure.transitionMasked(sid.infrastructure(), runway());
        this.token = LinkableToken.anySid(masked, grapher.graphRepresentationOf(sid));
      }

      @Override
      public void visit(ResolvedToken.StarEnrouteCommon star) {
        Procedure masked = Procedure.transitionMasked(star.infrastructure(), common().or(enroute()));
        this.token = LinkableToken.anyStar(masked, grapher.graphRepresentationOf(star));
      }

      @Override
      public void visit(ResolvedToken.StarRunway star) {
        Procedure masked = Procedure.transitionMasked(star.infrastructure(), runway());
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

//  /**
//   *
//   */
//  static Overridable overridable(LinkingStrategy strategy) {
//    return new Overridable();
//  }

  //  final class Overridable {
//
//    private Overridable() {
//    }
//
//    private static final class ElementTypes {
//
//      private final Class<? extends ResolvedToken> left;
//      private final Class<? extends ResolvedToken> right;
//
//      private ElementTypes(Class<? extends ResolvedToken> left, Class<? extends ResolvedToken> right) {
//        this.left = requireNonNull(left);
//        this.right = requireNonNull(right);
//      }
//
//      private boolean matchesLeft(ResolvedToken element) {
//        return left.isAssignableFrom(element.getClass());
//      }
//
//      private boolean matchesRight(ResolvedToken element) {
//        return right.isAssignableFrom(element.getClass());
//      }
//    }
//
//    private static final class Strategy implements LinkingStrategy {
//
//
//      @Override
//      public List<LinkedLegs> links(ResolvedToken left, ResolvedToken right) {
//        return null;
//      }
//    }
//  }
}
