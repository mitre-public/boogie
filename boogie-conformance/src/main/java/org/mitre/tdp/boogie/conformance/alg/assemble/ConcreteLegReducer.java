package org.mitre.tdp.boogie.conformance.alg.assemble;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.fn.Partitioner;

import ch.hsr.geohash.GeoHash;

/**
 * Reducer class for list of {@link ConsecutiveLegs} which start and end with concrete leg types.
 */
public interface ConcreteLegReducer extends ConsecutiveLegReducer {

  /**
   * Simplifies the input collection of {@link LegPair}s to just those representing unique legs.
   */
  @Override
  default List<ConsecutiveLegs> reduce(List<? extends ConsecutiveLegs> consecutiveLegs) {
    Map<Boolean, List<ConsecutiveLegs>> split = consecutiveLegs.stream().collect(Collectors.groupingBy(this::isConcretePair));

    Map<String, List<ConsecutiveLegs>> byId = split.getOrDefault(true, emptyList()).stream()
        .collect(Collectors.groupingBy(pair -> pair.current().pathTerminator().identifier()
            + pair.previous().orElseThrow(RuntimeException::new).pathTerminator().identifier()));

    return concat(split.getOrDefault(false, emptyList()).stream(), byId.values().stream().flatMap(list -> reduceCollocated(list).stream())).collect(toList());
  }

  /**
   * Comparator for legs based on the general location of the legs.
   */
  default Comparator<ConsecutiveLegs> legLocationComparator() {
    Function<ConsecutiveLegs, GeoHash> hasher = consecutiveLegs -> GeoHash.withBitPrecision(
        consecutiveLegs.current().pathTerminator().latitude(),
        consecutiveLegs.current().pathTerminator().longitude(),
        30);
    return Comparator.comparing(hasher);
  }

  /**
   * Reduces the input {@link ConsecutiveLegs} based on whether they're collocated.
   */
  default List<ConsecutiveLegs> reduceCollocated(List<ConsecutiveLegs> consecutiveLegs) {
    BiPredicate<ConsecutiveLegs, ConsecutiveLegs> similarLocation = (l1, l2) -> l1.current().pathTerminator().distanceInNmTo(l2.current().pathTerminator()) < 1.0;
    return consecutiveLegs.stream().sorted(legLocationComparator()).collect(Partitioner.listByPredicate(similarLocation)).stream().map(list -> list.get(0)).collect(toList());
  }

  default boolean isConcretePair(ConsecutiveLegs consecutiveLegs) {
    return consecutiveLegs.current().type().isConcrete()
        && consecutiveLegs.previous().map(l -> l.type().isConcrete()).orElse(false);
  }

  static ConcreteLegReducer newInstance() {
    return new ConcreteLegReducer() {};
  }
}
