package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.alg.resolve.AirportToken;
import org.mitre.tdp.boogie.alg.resolve.AirwayToken;
import org.mitre.tdp.boogie.alg.resolve.ApproachToken;
import org.mitre.tdp.boogie.alg.resolve.DirectToFixToken;
import org.mitre.tdp.boogie.alg.resolve.FixToken;
import org.mitre.tdp.boogie.alg.resolve.InitialFixElement;
import org.mitre.tdp.boogie.alg.resolve.LatLonToken;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;
import org.mitre.tdp.boogie.alg.resolve.SidToken;
import org.mitre.tdp.boogie.alg.resolve.StarToken;
import org.mitre.tdp.boogie.alg.resolve.TailoredToken;

/**  */
@FunctionalInterface
public interface LinkingStrategy {

  /**
   * The standard linking strategy for
   */
  static LinkingStrategy standard() {
    return new Standard();
  }

  static Overridable overridable() {
    return new Overridable();
  }

  Collection<LinkedLegs> links(ResolvedToken left, ResolvedToken right);

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
    }
  }

  /**
   * Leverages the double-dispatch pattern to
   */
  final class Standard implements LinkingStrategy {

    private Standard() {
    }

    @Override
    public Collection<LinkedLegs> links(ResolvedToken left, ResolvedToken right) {
      return wrap(left).accept(wrap(right)).links();
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
      public void visit(AirportToken airportElement) {
        this.token = new DirectToAirport(airportElement);
      }

      @Override
      public void visit(AirwayToken airwayElement) {
        this.token = new Airway(airwayElement);
      }

      @Override
      public void visit(FixToken fixElement) {

      }

      @Override
      public void visit(SidToken sidElement) {

      }

      @Override
      public void visit(StarToken starElement) {

      }

      @Override
      public void visit(ApproachToken approachElement) {

      }

      @Override
      public void visit(TailoredToken tailoredElement) {

      }

      @Override
      public void visit(LatLonToken latLonElement) {

      }

      @Override
      public void visit(DirectToFixToken fixElement) {

      }

      @Override
      public void visit(InitialFixElement fixElement) {

      }
    }
  }
}
