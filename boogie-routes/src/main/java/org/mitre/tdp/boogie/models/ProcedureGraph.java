package org.mitre.tdp.boogie.models;

import java.util.Collection;

import com.google.common.base.Preconditions;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.LowestCommonAncestorAlgorithm;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.utils.Iterators;

import static org.mitre.tdp.boogie.utils.Collections.allMatch;

/**
 * Representation of the procedure built from its collection of transitions as a graph object.
 *
 * Using jgrapht as the engine we leverage a whole host of common graph algorithms on the procedure
 * treating it as a directed graph. For the route expansion use case the most important are:
 *
 * {@link ConnectivityInspector}
 * {@link AllDirectedPaths}
 * {@link LowestCommonAncestorAlgorithm}
 */
public class ProcedureGraph<F extends Fix, L extends Leg<F>, T extends Transition<F, L>> extends SimpleDirectedGraph<L, DefaultEdge> implements Procedure<F, L, T> {

  private final Collection<T> transitions;

  private ProcedureGraph(Collection<T> transitions) {
    super(DefaultEdge.class);
    this.transitions = transitions;
  }

  @Override
  public Collection<T> transitions() {
    return transitions;
  }

  /**
   * Constructs a procedure graph object from the collection of transitions associated with a particular procedure.
   */
  public static <F extends Fix, L extends Leg<F>, T extends Transition<F, L>> ProcedureGraph<F, L, T> from(Collection<T> transitions) {
    Preconditions.checkArgument(allMatch(transitions, Transition::procedure));
    Preconditions.checkArgument(allMatch(transitions, Transition::airport));
    Preconditions.checkArgument(allMatch(transitions, Transition::source));

    ProcedureGraph<F, L, T> procedure = new ProcedureGraph<>(transitions);
    transitions.forEach(transition -> Iterators.pairwise(
        transition.legs(),
        (prev, curr) -> {
          procedure.addVertex(prev);
          procedure.addVertex(curr);
          procedure.addEdge(prev, curr);
        }
    ));

    return procedure;
  }
}
