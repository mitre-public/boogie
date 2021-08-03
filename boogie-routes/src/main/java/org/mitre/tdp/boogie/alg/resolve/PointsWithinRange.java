package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.distanceBetween;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.withLocation;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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
    List<Leg> element1Legs = withLocation(resolvedElement1.toLinkedLegs(), LinkedLegs::target);
    List<Leg> element2Legs = withLocation(resolvedElement2.toLinkedLegs(), LinkedLegs::source);

    return cartesianProduct(element1Legs, element2Legs).stream()
        .sorted(Comparator.comparing(LinkingUtils::distanceBetween))
        .map(pair -> new LinkedLegs(pair.first(), pair.second(), distanceBetween(pair)))
        .filter(pair -> pair.linkWeight() < distanceThreshold)
        .collect(Collectors.toList());
  }
}
