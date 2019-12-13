package org.mitre.tdp.boogie.alg.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.util.Combinatorics;

import static org.mitre.tdp.boogie.utils.Collections.allMatch;
import static org.mitre.tdp.boogie.utils.Collections.noneMatch;
import static org.mitre.tdp.boogie.utils.Collections.transform;
import static org.mitre.tdp.boogie.utils.Iterators.checkMatchCount;
import static org.mitre.tdp.boogie.utils.Iterators.fastslow;

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

  /**
   * Takes two collections of transitions assumed to be of following types and zips them together
   * along their endpoints.
   *
   * e.g. List<ENROUTE> -> List<COMMON>
   */
  private void zipAndInsert(List<Transition> previous, List<Transition> next, ProcedureGraph graph) {
    // these should all have concrete fixes as path terminators
    List<Leg> terminals = transform(previous, t -> ((List<Leg>) t.legs()).get(t.legs().size() - 1));
    List<Leg> initials = transform(next, t -> ((List<Leg>) t.legs()).get(0));

    Iterator<Pair<Leg, Leg>> paired = Combinatorics.cartesianProduct(terminals, initials);
    paired.forEachRemaining(pair -> {

      if (pair.first().pathTerminator().identifier().equals(pair.second().pathTerminator().identifier())) {
        graph.addEdge(pair.first(), pair.second());
      }
    });
  }

  /**
   * Takes the provided procedure graph and its internal set of transitions divided by type and zips
   * the transitions together based on the type of procedure they are a part of.
   */
  void zipAndInsert(ProcedureGraph graph) {
    if (checkMatchCount(listOrdered(), c -> !c.isEmpty())) {
      fastslow(listOrdered(), c -> !c.isEmpty(), (l1, l2, skip) -> zipAndInsert(l1, l2, graph));
    }
  }

  /**
   * Takes a collection of transitions all associated with the same procedure and splits them by
   * {@link TransitionType} for later use.
   */
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
