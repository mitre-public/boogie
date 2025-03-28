package org.mitre.tdp.boogie.alg.chooser.graph;

import java.util.Collection;
import java.util.Optional;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;

final class LinkingSupport {

  private LinkingSupport() {
  }

  static Optional<Leg> firstLegWithLocation(Transition transition) {
    return transition.legs().stream().filter(leg -> leg.associatedFix().isPresent()).map(Leg.class::cast).findFirst();
  }

  static Optional<Leg> lastLegWithLocation(Transition transition) {
    return transition.legs().stream().filter(leg -> leg.associatedFix().isPresent()).map(Leg.class::cast).reduce((l1, l2) -> l2);
  }

  static double distanceBetween(Pair<Leg, Leg> pair) {
    return pair.first().associatedFix().orElseThrow(IllegalStateException::new)
        .distanceInNmTo(pair.second().associatedFix().orElseThrow(IllegalStateException::new));
  }

  /**
   * <a href="https://www.youtube.com/watch?v=_J3VeogFUOs">"There can only be one"</a>
   */
  static <T> Optional<T> highlander(Collection<T> col) {
    return col.size() == 1 ? Optional.of(col.iterator().next()) : Optional.empty();
  }
}
