package org.mitre.tdp.boogie.conformance.alg.assign.link;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * A linking strategy defines a way to connect potentially disconnected sequences of {@link FlyableLeg}s into a connected
 * overall graph representing the collection of route possibilities we want to consider assignment against.
 *
 * Note that all legs originating from the same route are assumed to be lined (in their declared consecutive order) and do not
 * need to be manually linked in the linking strategy. This strategy should primarily address linking between otherwise unlinked
 * from separate routes.
 */
@FunctionalInterface
public interface LinkingStrategy {

  /**
   * Generates the set of pairwise directional links between {@link FlyableLeg}s in the given input collection.
   */
  Collection<Pair<FlyableLeg, FlyableLeg>> links(Collection<FlyableLeg> flyableLegs);

  /**
   * Returns a new {@link LinkingStrategy} representing the hybridization of two linking strategies.
   */
  default LinkingStrategy hybrid(LinkingStrategy linkingStrategy) {
    return flyableLegs -> {
      List<Pair<FlyableLeg, FlyableLeg>> links = new ArrayList<>();

      links.addAll(links(flyableLegs));
      links.addAll(linkingStrategy.links(flyableLegs));

      return links;
    };
  }
}
