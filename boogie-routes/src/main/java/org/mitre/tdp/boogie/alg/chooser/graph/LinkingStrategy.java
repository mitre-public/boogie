package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.alg.resolve.AirportElement;
import org.mitre.tdp.boogie.alg.resolve.AirwayElement;
import org.mitre.tdp.boogie.alg.resolve.ApproachElement;
import org.mitre.tdp.boogie.alg.resolve.DirectToFixElement;
import org.mitre.tdp.boogie.alg.resolve.FixElement;
import org.mitre.tdp.boogie.alg.resolve.InitialFixElement;
import org.mitre.tdp.boogie.alg.resolve.LatLonElement;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.ResolvedElement;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;
import org.mitre.tdp.boogie.alg.resolve.SidElement;
import org.mitre.tdp.boogie.alg.resolve.StarElement;
import org.mitre.tdp.boogie.alg.resolve.TailoredElement;

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

  Collection<LinkedLegs> links(ResolvedElement left, ResolvedElement right);

  final class Overridable {

    private Overridable() {
    }

    private static final class ElementTypes {

      private final Class<? extends ResolvedElement> left;
      private final Class<? extends ResolvedElement> right;

      private ElementTypes(Class<? extends ResolvedElement> left, Class<? extends ResolvedElement> right) {
        this.left = requireNonNull(left);
        this.right = requireNonNull(right);
      }

      private boolean matchesLeft(ResolvedElement element) {
        return left.isAssignableFrom(element.getClass());
      }

      private boolean matchesRight(ResolvedElement element) {
        return right.isAssignableFrom(element.getClass());
      }
    }

    private static final class Strategy implements LinkingStrategy {


      @Override
      public List<LinkedLegs> links(ResolvedElement left, ResolvedElement right) {
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
    public Collection<LinkedLegs> links(ResolvedElement left, ResolvedElement right) {
      return wrap(left).accept(wrap(right)).links();
    }

    private GraphableToken wrap(ResolvedElement element) {

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
      public void visit(AirportElement airportElement) {
        this.token = new GraphableDirectToAirport(airportElement);
      }

      @Override
      public void visit(AirwayElement airwayElement) {
        this.token = new GraphableAirway(airwayElement);
      }

      @Override
      public void visit(FixElement fixElement) {

      }

      @Override
      public void visit(SidElement sidElement) {

      }

      @Override
      public void visit(StarElement starElement) {

      }

      @Override
      public void visit(ApproachElement approachElement) {

      }

      @Override
      public void visit(TailoredElement tailoredElement) {

      }

      @Override
      public void visit(LatLonElement latLonElement) {

      }

      @Override
      public void visit(DirectToFixElement fixElement) {

      }

      @Override
      public void visit(InitialFixElement fixElement) {

      }
    }
  }
}
