package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.graph.ProcedureGraph;
import org.mitre.tdp.boogie.alg.resolve.CommonOrEnrouteTransitionFilter;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.alg.resolve.StarRunwayTransitionFilter;
import org.mitre.tdp.boogie.models.LinkedLegs;
import org.mitre.tdp.boogie.models.Procedure;

public class ProcedureElement extends ResolvedElement<Procedure> {

  private static Predicate<Transition> DEFAULT_TRANSITION_FILTER = new CommonOrEnrouteTransitionFilter();

  /**
   * Filter for transitions to be applied prior to construction of the {@link ProcedureGraph}.
   *
   * Typically this will be something like the {@link StarRunwayTransitionFilter}.
   */
  private Predicate<Transition> transitionFilter;

  public ProcedureElement(Procedure ref) {
    super(elementTypeFor(ref.type()), ref);
    // by default drop all runway/approach transitions
    this.transitionFilter = DEFAULT_TRANSITION_FILTER;
  }

  public Predicate<Transition> transitionFilter() {
    return transitionFilter;
  }

  /**
   * Sets the internal transition filter to be the provided transition predicate.
   */
  public ProcedureElement setTransitionFilter(Predicate<Transition> transitionFilter) {
    this.transitionFilter = transitionFilter;
    return this;
  }

  @Override
  public List<LinkedLegs> buildLegs() {
    List<LinkedLegs> legs = new ArrayList<>();

    ProcedureGraph graph = ProcedureGraph.from(reference.transitions().stream().filter(transitionFilter).collect(Collectors.toSet()));

    graph.edgeSet().forEach(edge -> {
      Leg source = graph.getEdgeSource(edge);
      Leg target = graph.getEdgeTarget(edge);
      GraphableLeg ssl1 = new GraphableLeg(source);
      GraphableLeg ssl2 = new GraphableLeg(target);
      legs.add(new LinkedLegs(ssl1, ssl2));
    });
    return legs;
  }

  /**
   * The {@link ProcedureType} -> {@link ElementType} mapping for resolved procedures.
   */
  public static ElementType elementTypeFor(ProcedureType ref) {
    switch (ref) {
      case SID:
        return ElementType.SID;
      case STAR:
        return ElementType.STAR;
      case APPROACH:
        return ElementType.APPROACH;
      default:
        throw new UnsupportedOperationException("Unknown procedure type: " + ref);
    }
  }
}
