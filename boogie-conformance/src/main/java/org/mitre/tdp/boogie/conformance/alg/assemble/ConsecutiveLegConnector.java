package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.util.Combinatorics;

/**
 * Interface for objects which provide the capability to connect multiple groupings of legs together.
 */
@FunctionalInterface
public interface ConsecutiveLegConnector {

  /**
   * Returns the set of connections between the supplied groupings of legs.
   */
  default Set<ConsecutiveLegs> connections(List<ConsecutiveLegs>... legGroups) {
    return connections(Arrays.asList(legGroups));
  }

  /**
   * Returns the set of connections between the collection of leg groups based in their internal identifiers.
   */
  default Set<ConsecutiveLegs> connections(List<List<ConsecutiveLegs>> legGroups) {
    Set<ConsecutiveLegs> connections = new HashSet<>();

    Iterator<Pair<List<ConsecutiveLegs>, List<ConsecutiveLegs>>> pairwise = Combinatorics.pairwiseCombos(legGroups);

    // iterate over the pairwise combinations of leg groups
    while (pairwise.hasNext()) {
      Pair<List<ConsecutiveLegs>, List<ConsecutiveLegs>> groups = pairwise.next();
      Iterator<Pair<ConsecutiveLegs, ConsecutiveLegs>> cartesian = Combinatorics.cartesianProduct(groups.first()::iterator, groups.second()::iterator);

      // for each group pair consider all candidate connections between contained leg pairs
      while (cartesian.hasNext()) {
        Pair<ConsecutiveLegs, ConsecutiveLegs> legs = cartesian.next();
        if (isConnected(legs.first(), legs.second())) {
          connections.add(new LegTriple(legs.first().current(), legs.second().current(), legs.second().next().orElse(null)));
          connections.add(new LegTriple(legs.second().current(), legs.first().current(), legs.first().next().orElse(null)));
        }
      }
    }

    return connections;
  }

  /**
   * Returns whether the given pair of {@link ConsecutiveLegs} should be connected.
   */
  boolean isConnected(ConsecutiveLegs left, ConsecutiveLegs right);

  static ConsecutiveLegConnector newConnector(BiPredicate<ConsecutiveLegs, ConsecutiveLegs>... predicates) {
    return newConnector(Arrays.asList(predicates));
  }

  static ConsecutiveLegConnector newConnector(List<BiPredicate<ConsecutiveLegs, ConsecutiveLegs>> predicates) {
    BiPredicate<ConsecutiveLegs, ConsecutiveLegs> composite = predicates.stream().reduce((l1, l2) -> false, BiPredicate::or);
    return composite::test;
  }
}
