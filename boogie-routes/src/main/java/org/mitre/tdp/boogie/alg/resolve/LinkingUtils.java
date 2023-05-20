package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.util.TransitionSorter;

/**
 * Utility methods for building links between the various {@link ResolvedToken} implementations.
 */
final class LinkingUtils {

  private LinkingUtils() {
    throw new IllegalStateException("Cannot instantiate static utility class");
  }

  static Optional<Leg> firstLegWithLocation(Transition transition) {
    return transition.legs().stream().filter(leg -> leg.associatedFix().isPresent()).map(Leg.class::cast).findFirst();
  }

  static Optional<Leg> lastLegWithLocation(Transition transition) {
    return transition.legs().stream().filter(leg -> leg.associatedFix().isPresent()).map(Leg.class::cast).reduce((l1, l2) -> l2);
  }

  static List<Transition> finalStarTransitions(Procedure procedure) {
    return TransitionSorter.INSTANCE
        .sortStarTransitions(procedure.transitions())
        .stream().filter(col -> !col.isEmpty())
        .reduce((l1, l2) -> l2).orElseThrow(IllegalStateException::new);
  }

  static List<Transition> finalSidTransitions(Procedure procedure) {
    return TransitionSorter.INSTANCE
        .sortSidTransitions(procedure.transitions())
        .stream().filter(col -> !col.isEmpty())
        .reduce((l1, l2) -> l2).orElseThrow(IllegalStateException::new);
  }

  static List<Transition> initialApproachTransitions(ApproachToken approachElement) {
    return TransitionSorter.INSTANCE
        .sortApproachTransitions(approachElement.procedure().transitions())
        .stream().filter(col -> !col.isEmpty())
        .findFirst().orElseThrow(IllegalStateException::new);
  }

  /**
   * Returns the composition of the two linkers between {@link ResolvedToken}s - if the first returns no links then the second
   * will be called and its set of links returned.
   */
  static BiFunction<ResolvedToken, ResolvedToken, List<LinkedLegs>> orElse(
      BiFunction<ResolvedToken, ResolvedToken, List<LinkedLegs>> linker1,
      BiFunction<ResolvedToken, ResolvedToken, List<LinkedLegs>> linker2
  ) {
    requireNonNull(linker1);
    requireNonNull(linker2);

    return (resolvedElement1, resolvedElement2) -> {
      List<LinkedLegs> firstLinked = linker1.apply(resolvedElement1, resolvedElement2);
      return firstLinked.isEmpty()
          ? linker2.apply(resolvedElement1, resolvedElement2)
          : firstLinked;
    };
  }

  public static final Function<ApproachToken, List<Leg>> approachTransitions = approachElement -> approachElement.procedure().transitions().stream()
      .filter(t -> t.transitionType().equals(TransitionType.APPROACH))
      .map(LinkingUtils::firstLegWithLocation)
      .flatMap(Optional::stream)
      .collect(Collectors.toList());

  public static final Function<ApproachToken, List<Leg>> finalApproach = approachElement -> approachElement.procedure().transitions().stream()
      .filter(t -> t.transitionType().equals(TransitionType.COMMON))
      .map(LinkingUtils::firstLegWithLocation)
      .flatMap(Optional::stream)
      .collect(Collectors.toList());

  static Function<ApproachToken, List<Leg>> orElse(Function<ApproachToken, List<Leg>> one, Function<ApproachToken, List<Leg>> two) {
    requireNonNull(one);
    requireNonNull(two);

    return (resolvedOne) -> {
      List<Leg> oneList = one.apply(resolvedOne);
      return oneList.isEmpty()
          ? two.apply(resolvedOne)
          : oneList;
    };
  }

  static double distanceBetween(Pair<Leg, Leg> pair) {
    return pair.first().associatedFix().orElseThrow(IllegalStateException::new)
        .distanceInNmTo(pair.second().associatedFix().orElseThrow(IllegalStateException::new));
  }
}
