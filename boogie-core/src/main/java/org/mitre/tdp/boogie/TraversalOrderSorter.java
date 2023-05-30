package org.mitre.tdp.boogie;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

public final class TraversalOrderSorter {

  private final LinkedHashSet<TransitionType> ordering;

  private TraversalOrderSorter(LinkedHashSet<TransitionType> ordering) {
    this.ordering = ordering;
  }

  public static TraversalOrderSorter forProcedureType(ProcedureType procedureType) {
    switch (procedureType) {
      case SID:
        return sid();
      case STAR:
        return star();
      case APPROACH:
        return approach();
      default:
        throw new IllegalArgumentException("Unsupported procedure type for sorting: " + procedureType);
    }
  }

  public static TraversalOrderSorter approach() {

    Stream<TransitionType> sort = Stream.of(
        TransitionType.APPROACH,
        TransitionType.COMMON,
        TransitionType.RUNWAY,
        TransitionType.MISSED
    );

    return new TraversalOrderSorter(sort.collect(toCollection(LinkedHashSet::new)));
  }

  public static TraversalOrderSorter sid() {

    Stream<TransitionType> sort = Stream.of(
        TransitionType.RUNWAY,
        TransitionType.COMMON,
        TransitionType.ENROUTE
    );

    return new TraversalOrderSorter(sort.collect(toCollection(LinkedHashSet::new)));
  }

  public static TraversalOrderSorter star() {

    Stream<TransitionType> sort = Stream.of(
        TransitionType.ENROUTE,
        TransitionType.COMMON,
        TransitionType.RUNWAY
    );

    return new TraversalOrderSorter(sort.collect(toCollection(LinkedHashSet::new)));
  }

  public List<List<Transition>> sort(Collection<? extends Transition> transitions) {

    Map<TransitionType, List<Transition>> grouped = transitions.stream().collect(groupingBy(Transition::transitionType));
    ensureTypeCoverage(grouped.keySet());

    return ordering.stream().map(type -> grouped.getOrDefault(type, List.of())).collect(toList());
  }

  private void ensureTypeCoverage(Set<TransitionType> detectedTypes) {
    Sets.SetView<TransitionType> difference = Sets.difference(ordering, detectedTypes);
    checkArgument(difference.isEmpty(), "Found unhandled transition type when sorting transitions: {}", difference);
  }
}
