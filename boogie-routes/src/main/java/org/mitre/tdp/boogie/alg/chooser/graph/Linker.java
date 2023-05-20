package org.mitre.tdp.boogie.alg.chooser.graph;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;

@FunctionalInterface
public interface Linker {

  /**
   * A linker which returns {@link LinkedLegs} between all leg pairs of the left and right {@link GraphableToken}s where the fixes
   * associated with the legs are within the given range.
   *
   * @param range the max {@link Distance} between legs to link from the two tokens
   * @param left  the token to link from
   * @param right the token to link to
   */
  static Linker pointsWithinRange(Distance range, GraphableToken left, GraphableToken right) {
    checkArgument(range.isPositive(), "Range should be positive.");
    return new PointsWithinRange(range, left, right);
  }

  /**
   * A linker which returns a single {@link LinkedLegs} taking the closes point between the legs of the {@link GraphableToken}s.
   *
   * @param left  the token to link from
   * @param right the token to link to
   */
  static Linker closestPointBetween(GraphableToken left, GraphableToken right) {
    return () -> pointsWithinRange(Distance.ofNauticalMiles(Double.MAX_VALUE), left, right)
        .links().stream().findFirst().map(List::of).orElseGet(Collections::emptyList);
  }

  /**
   * Returns a collection of links between a pair of configured {@link GraphableToken}s.
   */
  Collection<LinkedLegs> links();

  final class PointsWithinRange implements Linker {

    private final Distance range;

    private final GraphableToken left;

    private final GraphableToken right;

    private PointsWithinRange(Distance range, GraphableToken left, GraphableToken right) {
      this.range = requireNonNull(range);
      this.left = requireNonNull(left);
      this.right = requireNonNull(right);
    }

    @Override
    public List<LinkedLegs> links() {

      List<Leg> element1Legs = withLocation(left.toLinkedLegs());
      List<Leg> element2Legs = withLocation(right.toLinkedLegs());

      return cartesianProduct(element1Legs, element2Legs).stream()
          .sorted(comparing(this::distanceBetween))
          .map(this::createPair)
          .filter(pair -> pair.linkWeight() < range.inNauticalMiles())
          .collect(toList());
    }

    private double distanceBetween(Pair<Leg, Leg> pair) {

      Fix left = pair.first().associatedFix().orElseThrow(IllegalStateException::new);
      Fix right = pair.second().associatedFix().orElseThrow(IllegalStateException::new);

      return left.distanceInNmTo(right);
    }

    private LinkedLegs createPair(Pair<Leg, Leg> pair) {
      return new LinkedLegs(
          pair.first(),
          pair.second(),
          Math.max(LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT, distanceBetween(pair))
      );
    }

    private List<Leg> withLocation(Collection<LinkedLegs> linkedLegs) {
      return linkedLegs.stream()
          .flatMap(linked -> Stream.of(linked.source(), linked.target()))
          .distinct()
          .filter(leg -> leg.associatedFix().isPresent())
          .collect(toList());
    }
  }
}
