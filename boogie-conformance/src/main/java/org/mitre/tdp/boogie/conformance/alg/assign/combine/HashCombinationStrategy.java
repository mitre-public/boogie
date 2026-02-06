package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * Represents a combination strategy based on a {@link HashMap} which merges {@link FlyableLeg}s sharing the same generated
 * code via the provided {@link #hasher}.
 * <p>
 * For an example of a common hash function see {@link PathTerminatorBasedLegHasher}.
 */
public final class HashCombinationStrategy implements CombinationStrategy {

  private final Function<FlyableLeg, Integer> hasher;

  public HashCombinationStrategy(Function<FlyableLeg, Integer> hasher) {
    this.hasher = checkNotNull(hasher);
  }

  @Override
  public Map<FlyableLeg, CompositeLeg> combineSimilar(Collection<FlyableLeg> flyableLegs) {
    // generates the mapping of hashCode to combined representative
    Map<Integer, CompositeLeg> combinedRep = flyableLegs.stream()
        .collect(Collectors.toMap(
            hasher,
            CompositeLeg::new,
            CompositeLeg::unionWith,
            HashMap::new
        ));

    // applies that mapping to produce a lookup between original leg and mapped value
    return flyableLegs.stream()
        .collect(Collectors.toMap(
            Function.identity(),
            flyableLeg -> checkNotNull(combinedRep.get(hasher.apply(flyableLeg)))
        ));
  }
}
