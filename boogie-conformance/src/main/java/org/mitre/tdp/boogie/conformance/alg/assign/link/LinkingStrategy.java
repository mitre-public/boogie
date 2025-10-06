package org.mitre.tdp.boogie.conformance.alg.assign.link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * A linking strategy defines a way to connect potentially disconnected sequences of {@link FlyableLeg}s into a connected
 * overall graph representing the connected infrastructure we want to consider assignment against.
 * <br>
 * Note that all legs originating from the same route are assumed to be linked (in their declared consecutive order) and do not
 * need to be manually linked in the linking strategy. The strategy should primarily address linking between otherwise unlinked
 * routes.
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

  /**
   * This strategy provides no links. Most usefull when only assigning routes from a single procedure.
   * @return a strategy that has empty link lists
   */
  static LinkingStrategy noop() {
    return new NoOpLinkingStrategy();
  }

  /**
   * This strategy lets others else provide a list of links.
   * @param links the precomputed links
   * @return the links that were already computed.
   */
  static LinkingStrategy supplied(Pair<FlyableLeg, FlyableLeg>... links) {
    return new SuppliedLinkStrategy(links);
  }
}

  final class NoOpLinkingStrategy implements LinkingStrategy {
    NoOpLinkingStrategy() {}
    @Override
    public Collection<Pair<FlyableLeg, FlyableLeg>> links(Collection<FlyableLeg> flyableLegs) {
      return List.of();
    }
  }

  final class SuppliedLinkStrategy implements LinkingStrategy, Supplier<Collection<Pair<FlyableLeg, FlyableLeg>>> {

    private final Collection<Pair<FlyableLeg, FlyableLeg>> links;

    @SafeVarargs
    SuppliedLinkStrategy(Pair<FlyableLeg, FlyableLeg>... links) {
      this(Arrays.asList(links));
    }

    SuppliedLinkStrategy(Collection<Pair<FlyableLeg, FlyableLeg>> links) {
      this.links = links;
    }

    @Override
    public Collection<Pair<FlyableLeg, FlyableLeg>> get() {
      return links;
    }

    @Override
    public Collection<Pair<FlyableLeg, FlyableLeg>> links(Collection<FlyableLeg> flyableLegs) {
      return get();
    }
}
