package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.alg.chooser.GraphBasedRouteChooser;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;
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
   * The standard linking strategy for use with the {@link GraphBasedRouteChooser}. This strategy handles generating links between
   * all the commonly filed portions of a route as well as setting sane weights for their relationships.
   *
   * <p>This strategy is meant to be robust across all common use cases, if a client wants more control over the linking of specific
   * pairs of {@link ResolvedToken}s they should use the {@link #overridable(LinkingStrategy)} decorator on top of this instance.
   */
  static LinkingStrategy standard() {
    return new Standard();
  }

  /**
   *
   */
  static Overridable overridable(LinkingStrategy strategy) {
    return new Overridable();
  }

  /**
   *
   */
  Collection<LinkedLegs> links(ResolvedToken left, ResolvedToken right);

  /**
   * Convert a {@link ResolvedToken} to a graphical representation (a collection of {@link LinkedLegs}) which can be traversed as
   * part of the route choosing process.
   *
   * <p>For simple elements this may be linked legs from an element to itself (e.g. FIX->FIX) while for more complex ones it may
   * represent a full sub-graph itself (e.g. a SID or STAR).
   *
   * @param token the {@link ResolvedToken} to generate a representation for
   */
  Collection<LinkedLegs> graphRepresentation(ResolvedToken token);

  final class Overridable {

    private Overridable() {
    }

    private static final class ElementTypes {

      private final Class<? extends ResolvedToken> left;
      private final Class<? extends ResolvedToken> right;

      private ElementTypes(Class<? extends ResolvedToken> left, Class<? extends ResolvedToken> right) {
        this.left = requireNonNull(left);
        this.right = requireNonNull(right);
      }

      private boolean matchesLeft(ResolvedToken element) {
        return left.isAssignableFrom(element.getClass());
      }

      private boolean matchesRight(ResolvedToken element) {
        return right.isAssignableFrom(element.getClass());
      }
    }

    private static final class Strategy implements LinkingStrategy {


      @Override
      public List<LinkedLegs> links(ResolvedToken left, ResolvedToken right) {
        return null;
      }

      @Override
      public Collection<LinkedLegs> graphRepresentation(ResolvedToken token) {
        return null;
      }
    }
  }

  /**
   * Leverages the double-dispatch pattern to decorate the provided {@link ResolvedToken}s as {@link GraphableToken}s and then
   * delegates to those to convert pairings of tokens to collections of {@link LinkedLegs} linking them.
   */
  final class Standard implements LinkingStrategy {

    private Standard() {
    }

    @Override
    public Collection<LinkedLegs> links(ResolvedToken left, ResolvedToken right) {
      return wrap(left).accept(wrap(right)).links();
    }

    @Override
    public Collection<LinkedLegs> graphRepresentation(ResolvedToken token) {
      return wrap(token).linkedLegs();
    }

    private GraphableToken wrap(ResolvedToken element) {

      ResolvedTokenWrapper wrapper = new ResolvedTokenWrapper();
      element.accept(wrapper);

      return wrapper.token()
          .orElseThrow(() -> new IllegalStateException("Couldn't handle element: " + element + " as graphable."));
    }

    private static final class ResolvedTokenWrapper implements ResolvedTokenVisitor {

      private GraphableToken token;

      private ResolvedTokenWrapper() {
      }

      public Optional<GraphableToken> token() {
        return ofNullable(token);
      }

      @Override
      public void visit(ResolvedToken.StandardAirport airport) {
        this.token = GraphableToken.standardAirport(airport.infrastructure());
      }

      @Override
      public void visit(ResolvedToken.DirectToAirport airport) {
        this.token = GraphableToken.directToAirport(airport.infrastructure());
      }

      @Override
      public void visit(ResolvedToken.StandardAirway airway) {
        this.token = GraphableToken.standardAirway(airway.infrastructure());
      }

      @Override
      public void visit(ResolvedToken.StandardApproach approach) {
        this.token = GraphableToken.standardApproach(approach.infrastructure());
      }

      @Override
      public void visit(ResolvedToken.StandardFix fix) {
        this.token = GraphableToken.standardFix(fix.infrastructure());
      }

      @Override
      public void visit(ResolvedToken.DirectToFix fix) {
        this.token = GraphableToken.directToFix(fix.infrastructure());
      }

      @Override
      public void visit(ResolvedToken.StandardLatLong latLong) {
        this.token = GraphableToken.standardLatLong(latLong.infrastructure());
      }

      @Override
      public void visit(ResolvedToken.DirectToLatLong latLong) {
        this.token = GraphableToken.directToLatLong(latLong.infrastructure());
      }

      @Override
      public void visit(ResolvedToken.SidEnrouteCommon sid) {
        this.token = GraphableToken.sidEnrouteCommon(sid.infrastructure());
      }

      @Override
      public void visit(ResolvedToken.SidRunway sid) {
        this.token = GraphableToken.sidRunway(sid.infrastructure());
      }

      @Override
      public void visit(ResolvedToken.StarEnrouteCommon star) {
        this.token = GraphableToken.starEnrouteCommon(star.infrastructure());
      }

      @Override
      public void visit(ResolvedToken.StarRunway star) {
        this.token = GraphableToken.starRunway(star.infrastructure());
      }

      @Override
      public void visit(ResolvedToken.StandardFrd frd) {
        this.token = GraphableToken.standardFrd(frd.infrastructure());
      }

      @Override
      public void visit(ResolvedToken.DirectToFrd frd) {
        this.token = GraphableToken.directToFrd(frd.infrastructure());
      }
    }
  }
}
