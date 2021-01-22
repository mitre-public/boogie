package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import java.util.Optional;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.utils.Iterators;

public final class FlyableLegAssembler {

  private FlyableLegAssembler() {
    throw new IllegalStateException();
  }

  /**
   * Generates the sequence of flyable legs via traversing the given route in order.
   */
  public static List<FlyableLeg> assemble(Route route) {
    List<? extends Leg> legs = route.legs();

    if (legs.isEmpty()) {
      return Collections.emptyList();
    }

    List<FlyableLeg> assembled = new ArrayList<>();
    Iterators.triplesWithNulls(legs, (l1, l2, l3) -> Optional.ofNullable(l2).ifPresent(x -> assembled.add(new FlyableLeg(l1, l2, l3, route))));
    return assembled;
  }

  public static Pair<Collection<FlyableLeg>, Collection<Pair<FlyableLeg, FlyableLeg>>> assembleWithLinks(Collection<? extends Route> routes) {
    Collection<FlyableLeg> flyableLegs = new ArrayList<>();
    Collection<Pair<FlyableLeg, FlyableLeg>> links = new ArrayList<>();

    for (Route route : routes) {
      List<FlyableLeg> legs = FlyableLegAssembler.assemble(route);
      List<Pair<FlyableLeg, FlyableLeg>> linked = links(legs);

      flyableLegs.addAll(legs);
      links.addAll(linked);
    }

    return Pair.of(flyableLegs, links);
  }

  static List<Pair<FlyableLeg, FlyableLeg>> links(List<FlyableLeg> flyableLegs) {
    if (flyableLegs.size() < 2) {
      return Collections.emptyList();
    }
    List<Pair<FlyableLeg, FlyableLeg>> links = new ArrayList<>();
    Iterators.pairwise(flyableLegs, (l1, l2) -> links.add(Pair.of(l1, l2)));

    return links;
  }
}
