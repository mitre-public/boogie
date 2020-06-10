package org.mitre.tdp.boogie.conformance.alg.assemble;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.fn.Partitioner;

/**
 * Reducer class for list of {@link ConsecutiveLegs} which start and end with concrete leg types.
 */
public interface ConcreteLegReducer extends ConsecutiveLegReducer {

  /**
   * Simplifies the input collection of {@link LegPair}s to just those representing unique legs.
   */
  @Override
  default List<? extends ConsecutiveLegs> reduce(List<? extends ConsecutiveLegs> consecutiveLegs) {
    checkArgument(allConcreteLegs(consecutiveLegs), "Provided legs must contain concrete leg types to be reduced.");

    Map<String, List<ConsecutiveLegs>> byId = consecutiveLegs.stream()
        .collect(Collectors.groupingBy(pair -> pair.current().pathTerminator().identifier()
            + pair.previous().orElseThrow(RuntimeException::new).pathTerminator().identifier()));

    return byId.values().stream()
        .flatMap(list -> reduceCollocated(list).stream())
        .collect(Collectors.toList());
  }

  /**
   * Comparator for legs based on the general location of the legs.
   */
  default Comparator<ConsecutiveLegs> legLocationComparator() {
    Comparator<ConsecutiveLegs> latcomp = Comparator.comparing(consecutiveLegs -> consecutiveLegs.current().pathTerminator().latLong().latitude());
    Comparator<ConsecutiveLegs> loncomp = Comparator.comparing(consecutiveLegs -> consecutiveLegs.current().pathTerminator().latLong().longitude());
    return latcomp.thenComparing(loncomp);
  }

  /**
   * Reduces the input {@link ConsecutiveLegs} based on whether they're collocated.
   */
  default List<ConsecutiveLegs> reduceCollocated(List<ConsecutiveLegs> consecutiveLegs) {
    BiPredicate<ConsecutiveLegs, ConsecutiveLegs> similarLocation = (l1, l2) -> l1.current().pathTerminator().distanceInNmTo(l2.current().pathTerminator()) < 1.0;
    return consecutiveLegs.stream().sorted(legLocationComparator()).collect(Partitioner.listByPredicate(similarLocation))
        .stream().map(list -> list.get(0)).collect(Collectors.toList());
  }

  /**
   * Returns true when the passed {@link ConsecutiveLegs} contain only concrete leg types.
   */
  default boolean allConcreteLegs(List<? extends ConsecutiveLegs> consecutiveLegs) {
    return consecutiveLegs.stream().allMatch(cl -> cl.current().type().isConcrete() && cl.previous().isPresent() && cl.previous().get().type().isConcrete());
  }

  static ConcreteLegReducer newInstance() {
    return new ConcreteLegReducer() {};
  }
}
