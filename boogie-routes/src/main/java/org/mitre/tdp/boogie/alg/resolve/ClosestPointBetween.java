package org.mitre.tdp.boogie.alg.resolve;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Returns a list of {@link LinkedLegs} with one entry corresponding to the closest pair of legs between the two resolved elements.
 * <br>
 * Leverages {@link PointsWithinRange} to build the set of candidates to take the min of.
 */
final class ClosestPointBetween implements BiFunction<ResolvedElement, ResolvedElement, List<LinkedLegs>> {

  static final ClosestPointBetween INSTANCE = new ClosestPointBetween();

  private ClosestPointBetween() {
  }

  @Override
  public List<LinkedLegs> apply(ResolvedElement resolvedElement1, ResolvedElement resolvedElement2) {
    Optional<LinkedLegs> bestLink = pointsWithinRange.apply(resolvedElement1, resolvedElement2).stream().findFirst();
    return bestLink.map(Collections::singletonList).orElseGet(Collections::emptyList);
  }

  private static final PointsWithinRange pointsWithinRange = new PointsWithinRange(Double.MAX_VALUE);
}
