package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

/**
 * Represents a combination strategy based on a {@link HashMap} which merges {@link FlyableLeg}s sharing the same generated
 * code via the provided {@link #hasher}.
 *
 * Note that this strategy and the associated {@link #combine(FlyableLeg, FlyableLeg)} method maintain information at the leg
 * level based on order of precedence in feeding into the combiner.
 *
 * For an example of a common hash function see {@link PathTerminatorBasedLegHasher}.
 */
public class HashCombinationStrategy implements CombinationStrategy {

  private final Function<FlyableLeg, Integer> hasher;

  public HashCombinationStrategy(Function<FlyableLeg, Integer> hasher) {
    this.hasher = checkNotNull(hasher);
  }

  @Override
  public Map<FlyableLeg, FlyableLeg> combineSimilar(Collection<FlyableLeg> flyableLegs) {
    // generates the mapping of hashCode to combined representative
    Map<Integer, FlyableLeg> combinedRep = flyableLegs.stream()
        .collect(Collectors.toMap(
            hasher,
            Function.identity(),
            this::combine,
            HashMap::new
        ));

    // applies that mapping to produce a lookup between original leg and mapped value
    return flyableLegs.stream()
        .collect(Collectors.toMap(
            Function.identity(),
            flyableLeg -> checkNotNull(combinedRep.get(hasher.apply(flyableLeg)))
        ));
  }

  /**
   * Generates a combined representation of the two {@link FlyableLeg}s. This method relies on the assumption that for any two
   * legs we've flagged as acceptable to combine (via sharing a {@link #hasher} mapping) it doesn't matter which legs internals
   * are copied into the final flyable leg.
   *
   * That being said we generally prefer consistent fields - i.e. we completely mirror the original leg.
   */
  FlyableLeg combine(FlyableLeg f1, FlyableLeg f2) {
    LinkedHashSet<Route> routes = new LinkedHashSet<>();
    routes.addAll(f1.routes());
    routes.addAll(f2.routes());

    return new FlyableLeg(
        f1.previous().orElse(null),
        f1.current(),
        f1.next().orElse(null),
        routes
    );
  }
}
