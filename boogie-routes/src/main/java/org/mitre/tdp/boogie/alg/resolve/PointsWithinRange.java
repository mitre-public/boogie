package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.distanceBetween;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;

/**
 * Returns links between all distinct legs of the two ResolvedElements where the {@link Leg#associatedFix()}es are present and
 * are within the supplied range.
 * <br>
 * A static implementation is provided with a range that is generally sufficient.
 */
final class PointsWithinRange implements BiFunction<ResolvedElement, ResolvedElement, List<LinkedLegs>> {

  static final PointsWithinRange INSTANCE = new PointsWithinRange(.25);

  /**
   * The max distance two associated fixes can be apart (in NM) for them to be linked.
   */
  private final double distanceThreshold;

  PointsWithinRange(double distanceThreshold) {
    checkArgument(distanceThreshold > 0, "Distance threshold cannot be below zero.");
    this.distanceThreshold = distanceThreshold;
  }

  @Override
  public List<LinkedLegs> apply(ResolvedElement resolvedElement1, ResolvedElement resolvedElement2) {
    List<Leg> element1Legs = withLocation(resolvedElement1.toLinkedLegs());
    List<Leg> element2Legs = withLocation(resolvedElement2.toLinkedLegs());

    return cartesianProduct(element1Legs, element2Legs).stream().sorted(comparing(LinkingUtils::distanceBetween))
        .map(this::createPair).filter(pair -> pair.linkWeight() < distanceThreshold).collect(toList());
  }

  private LinkedLegs createPair(Pair<Leg, Leg> pair) {
    return new LinkedLegs(pair.first(), pair.second(), Math.max(LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT, distanceBetween(pair)));
  }

  static List<Leg> withLocation(List<LinkedLegs> linkedLegs) {
    return linkedLegs.stream()
        .flatMap(linked -> Stream.of(linked.source(), linked.target()))
        .distinct()
        .filter(leg -> leg.associatedFix().isPresent())
        .collect(toList());
  }
}
