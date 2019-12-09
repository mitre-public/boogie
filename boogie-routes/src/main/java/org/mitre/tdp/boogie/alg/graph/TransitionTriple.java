package org.mitre.tdp.boogie.alg.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

import static org.mitre.tdp.boogie.utils.Collections.allMatch;
import static org.mitre.tdp.boogie.utils.Collections.noneMatch;

/**
 * Convenience class for handling transitions grouped by their {@link TransitionType}.
 */
class TransitionTriple {

  private final ProcedureType procedureType;
  private final List<Transition> enroute;
  private final List<Transition> common;
  private final List<Transition> approach;

  private TransitionTriple(ProcedureType t, List<Transition> e, List<Transition> c, List<Transition> a) {
    this.procedureType = t;
    this.enroute = e;
    this.common = c;
    this.approach = a;
  }

  public ProcedureType procedureType() {
    return procedureType;
  }

  public List<Transition> enroute() {
    return enroute;
  }

  public List<Transition> common() {
    return common;
  }

  public List<Transition> approach() {
    return approach;
  }

  public List<List<Transition>> listOrdered() {
    return procedureType.equals(ProcedureType.STAR)
        ? Arrays.asList(enroute, common, approach)
        : Arrays.asList(approach, common, enroute);
  }

  public static TransitionTriple from(Collection<? extends Transition> transitions) {
    Preconditions.checkArgument(noneMatch(transitions, Transition::transitionType, TransitionType.RUNWAY));
    Preconditions.checkArgument(noneMatch(transitions, Transition::procedureType, ProcedureType.APPROACH));
    Preconditions.checkArgument(allMatch(transitions, Transition::procedure));
    Preconditions.checkArgument(allMatch(transitions, Transition::procedureType));

    Map<TransitionType, List<Transition>> byType = transitions.stream()
        .collect(Collectors.groupingBy(Transition::transitionType));

    return new TransitionTriple(
        transitions.iterator().next().procedureType(),
        byType.getOrDefault(TransitionType.ENROUTE, Collections.emptyList()),
        byType.getOrDefault(TransitionType.COMMON, Collections.emptyList()),
        byType.getOrDefault(TransitionType.APPROACH, Collections.emptyList()));
  }
}
