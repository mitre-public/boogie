package org.mitre.tdp.boogie.util;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.emptyList;
import static org.mitre.tdp.boogie.util.Preconditions.allMatch;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.model.QueryableProcedure;

/**
 * Class for sorting {@link Transition} objects by their natural occurrence order when an aircraft moves through their containing
 * procedure object. To clarify the explicit sortings by type:
 * <br>
 * 1) STAR = ENROUTE -> COMMON -> RUNWAY
 * 2) SID = RUNWAY -> COMMON -> ENROUTE
 * 3) Approach -> APPROACH -> COMMON -> RUNWAY -> MISSED
 * <br>
 * These are convenient to have when finding entry/exit fixes/legs - a la {@link QueryableProcedure}.
 */
public final class TransitionSorter implements Function<Collection<? extends Transition>, List<List<Transition>>> {

  public static final TransitionSorter INSTANCE = new TransitionSorter();

  private TransitionSorter() {
  }

  /**
   * Sorts a generic collection of input transitions according to their natural ordering as traversed within a procedure.
   */
  @Override
  public List<List<Transition>> apply(Collection<? extends Transition> transitions) {
    checkMatching(transitions);
    Optional<? extends Transition> rep = transitions.stream().findAny();
    if (rep.filter(t -> t.procedureType().equals(ProcedureType.STAR)).isPresent()) {
      return sortStarTransitions(transitions);
    }
    if (rep.filter(t -> t.procedureType().equals(ProcedureType.SID)).isPresent()) {
      return sortSidTransitions(transitions);
    }
    if (rep.filter(t -> t.procedureType().equals(ProcedureType.APPROACH)).isPresent()) {
      return sortApproachTransitions(transitions);
    }
    throw new RuntimeException("Unsupported procedure type for sorting.");
  }

  public List<List<Transition>> sortSidTransitions(Collection<? extends Transition> sids) {
    checkMatching(sids);
    checkArgument(allMatch(sids, Transition::procedureType, ProcedureType.SID));
    Map<TransitionType, List<Transition>> byType = groupByType(sids);
    return Arrays.asList(
        byType.getOrDefault(TransitionType.RUNWAY, emptyList()),
        byType.getOrDefault(TransitionType.COMMON, emptyList()),
        byType.getOrDefault(TransitionType.ENROUTE, emptyList())
    );
  }

  public List<List<Transition>> sortStarTransitions(Collection<? extends Transition> stars) {
    checkMatching(stars);
    checkArgument(allMatch(stars, Transition::procedureType, ProcedureType.STAR));
    Map<TransitionType, List<Transition>> byType = groupByType(stars);
    return Arrays.asList(
        byType.getOrDefault(TransitionType.ENROUTE, emptyList()),
        byType.getOrDefault(TransitionType.COMMON, emptyList()),
        byType.getOrDefault(TransitionType.RUNWAY, emptyList())
    );
  }

  public List<List<Transition>> sortApproachTransitions(Collection<? extends Transition> approaches) {
    checkMatching(approaches);
    checkArgument(allMatch(approaches, Transition::procedureType, ProcedureType.APPROACH));
    Map<TransitionType, List<Transition>> byType = groupByType(approaches);
    return Arrays.asList(
        byType.getOrDefault(TransitionType.APPROACH, emptyList()),
        byType.getOrDefault(TransitionType.COMMON, emptyList()),
        byType.getOrDefault(TransitionType.RUNWAY, emptyList()),
        byType.getOrDefault(TransitionType.MISSED, emptyList())
    );
  }

  private void checkMatching(Collection<? extends Transition> transitions) {
    checkArgument(allMatch(transitions, Transition::procedureIdentifier));
    checkArgument(allMatch(transitions, Transition::procedureType));
  }

  private Map<TransitionType, List<Transition>> groupByType(Collection<? extends Transition> transitions) {
    return transitions.stream().collect(Collectors.groupingBy(Transition::transitionType));
  }
}
