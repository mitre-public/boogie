package org.mitre.tdp.boogie.alg.graph;

import static java.util.Collections.emptyList;
import static org.mitre.tdp.boogie.util.Collections.allMatch;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

import com.google.common.base.Preconditions;

/**
 * Class for sorting {@link Transition} objects by their natural occurrence order when an aircraft moves through their containing
 * procedure object. To clarify the explicit sortings by type:
 *
 * 1) STAR = ENROUTE -> COMMON -> RUNWAY
 * 2) SID = RUNWAY -> COMMON -> ENROUTE
 * 3) Approach -> APPROACH -> COMMON -> RUNWAY -> MISSED
 *
 * These are convenient to have when handing collections of transitions off to utilities like the {@link ProcedureGraph#from(List)}
 * which then calls {@link ProcedureGraph#generateEdges(List, List)}.
 */
public final class TransitionSorter {

  /**
   * Sorts a generic collection of input transitions according to their natural ordering as traversed within a procedure.
   */
  public List<List<Transition>> sort(Collection<? extends Transition> transitions) {
    checkMatching(transitions);
    Optional<? extends Transition> rep = transitions.stream().findAny();
    if (rep.filter(t -> t.procedureType().equals(ProcedureType.STAR)).isPresent()) {
      return sortStars(transitions);
    }
    if (rep.filter(t -> t.procedureType().equals(ProcedureType.SID)).isPresent()) {
      return sortSids(transitions);
    }
    if (rep.filter(t -> t.procedureType().equals(ProcedureType.APPROACH)).isPresent()) {
      return sortApproaches(transitions);
    }
    throw new RuntimeException("Unsupported procedure type for sorting.");
  }

  public List<List<Transition>> sortSids(Collection<? extends Transition> sids) {
    checkMatching(sids);
    Preconditions.checkArgument(allMatch(sids, Transition::procedureType, ProcedureType.SID));
    Map<TransitionType, List<Transition>> byType = groupByType(sids);
    return Arrays.asList(
        byType.getOrDefault(TransitionType.RUNWAY, emptyList()),
        byType.getOrDefault(TransitionType.COMMON, emptyList()),
        byType.getOrDefault(TransitionType.ENROUTE, emptyList())
    );
  }

  public List<List<Transition>> sortStars(Collection<? extends Transition> stars) {
    checkMatching(stars);
    Preconditions.checkArgument(allMatch(stars, Transition::procedureType, ProcedureType.STAR));
    Map<TransitionType, List<Transition>> byType = groupByType(stars);
    return Arrays.asList(
        byType.getOrDefault(TransitionType.ENROUTE, emptyList()),
        byType.getOrDefault(TransitionType.COMMON, emptyList()),
        byType.getOrDefault(TransitionType.RUNWAY, emptyList())
    );
  }

  public List<List<Transition>> sortApproaches(Collection<? extends Transition> approaches) {
    checkMatching(approaches);
    Preconditions.checkArgument(allMatch(approaches, Transition::procedureType, ProcedureType.APPROACH));
    Map<TransitionType, List<Transition>> byType = groupByType(approaches);
    return Arrays.asList(
        byType.getOrDefault(TransitionType.APPROACH, emptyList()),
        byType.getOrDefault(TransitionType.COMMON, emptyList()),
        byType.getOrDefault(TransitionType.RUNWAY, emptyList()),
        byType.getOrDefault(TransitionType.MISSED, emptyList())
    );
  }

  private void checkMatching(Collection<? extends Transition> transitions) {
    Preconditions.checkArgument(allMatch(transitions, Transition::procedure));
    Preconditions.checkArgument(allMatch(transitions, Transition::procedureType));
  }

  private Map<TransitionType, List<Transition>> groupByType(Collection<? extends Transition> transitions) {
    return transitions.stream().collect(Collectors.groupingBy(Transition::transitionType));
  }
}
