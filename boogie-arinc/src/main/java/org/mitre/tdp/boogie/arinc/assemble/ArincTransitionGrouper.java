package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

/**
 * Groups the legs of one ARINC procedure into transitions, then orders the legs within each transition.
 *
 * <p>Route type {@code Z} identifies a separately coded missed-approach route and must not be combined with final-approach coding,
 * whose embedded primary missed approach is split later at an {@code M} waypoint marker. Qualifier 2 keeps separately coded
 * missed-approach variants distinct. Other route types deliberately ignore qualifier 2 because it can vary between legs in one
 * valid transition.
 */
final class ArincTransitionGrouper implements Function<Collection<ArincProcedureLeg>, Collection<List<ArincProcedureLeg>>> {
  public static final String DEFAULT_TRANSITION = "ALL";
  public static final String DEFAULT_CAT_TYPE = "ANY";
  static final ArincTransitionGrouper INSTANCE = new ArincTransitionGrouper();
  private static final Comparator<ArincProcedureLeg> LEG_COMPARATOR = comparing(ArincProcedureLeg::sequenceNumber)
      .thenComparing(i -> i.categoryOrType().orElse("UNK"));

  private ArincTransitionGrouper() {
  }

  private static TransitionGroupKey groupKey(ArincProcedureLeg procedureLeg) {
    String routeType = procedureLeg.routeType();
    String missedApproachVariant = Optional.of(procedureLeg)
        .filter(leg -> "Z".equals(leg.routeType()))
        .flatMap(ArincProcedureLeg::routeTypeQualifier2)
        .orElse(null);

    return new TransitionGroupKey(
        procedureLeg.transitionIdentifier().orElse(DEFAULT_TRANSITION),
        procedureLeg.categoryOrType().orElse(DEFAULT_CAT_TYPE),
        routeType,
        missedApproachVariant
    );
  }

  private static List<ArincProcedureLeg> sortTransition(List<ArincProcedureLeg> transition) {
    transition.sort(LEG_COMPARATOR);
    return transition;
  }

  @Override
  public Collection<List<ArincProcedureLeg>> apply(Collection<ArincProcedureLeg> procedureLegs) {
    return procedureLegs.stream()
        .collect(groupingBy(
            ArincTransitionGrouper::groupKey,
            Collectors.collectingAndThen(Collectors.toList(), ArincTransitionGrouper::sortTransition)
        ))
        .values();
  }

  private record TransitionGroupKey(String transitionIdentifier, String categoryOrType, String routeType, String missedApproachVariant) {
  }
}
