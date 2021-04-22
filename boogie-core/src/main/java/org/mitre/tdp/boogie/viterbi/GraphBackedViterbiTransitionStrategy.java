package org.mitre.tdp.boogie.viterbi;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a strategy for scoring/returning transitions between states in a {@link ViterbiTagger} which is based on a backing
 * graph - this class makes no assumptions about the <i>type</i> of graph it's provided - internally only leveraging connections
 * between its components to determine transitions (and doing some bookkeeping in case the provided graph is directed).
 *
 * With this formulation:
 *
 * 1) The valid transitions from a state are the set of downstream connected states of a given state (i.e. there exists a directed
 * edge from state A -> B)
 * 2) The transitions scores are based on the weight of the edge between the two states
 *
 * This simplifies the more generic scoring problem by allowing for us to hook into a pre-computed graphical representation of
 * the state set with pre-set weights.
 *
 * One key thing to note is that in the current formulation this transition strategy does not implicitly include the current
 * state in the set of transition targets unless the provided graph contains a self loop A->A. This is done to maintain simplicity
 * in the interpretation logic on top of the graph.
 */
public final class GraphBackedViterbiTransitionStrategy<Stage, State> implements ViterbiTransitionStrategy<Stage, State> {

  private static final Logger LOG = LoggerFactory.getLogger(GraphBackedViterbiTransitionStrategy.class);

  /**
   * The directed + weighted graph backing the set of transitions for the {@link ViterbiTagger}.
   */
  private final Graph<State, DefaultWeightedEdge> stateGraph;
  /**
   * Simple {@link ConnectivityInspector} on top of the {@link #stateGraph} allowing us to easily grab the connected set of
   * states from any given provided state.
   */
  private final ConnectivityInspector<State, DefaultWeightedEdge> connectivityInspector;

  public GraphBackedViterbiTransitionStrategy(Graph<State, DefaultWeightedEdge> stateGraph) {
    this.stateGraph = requireNonNull(stateGraph);
    this.connectivityInspector = new ConnectivityInspector<>(stateGraph);
  }

  @Override
  public Collection<State> validTransitionsFrom(State state) {
    Set<State> undirectedLinks = connectivityInspector.connectedSetOf(state);
    LOG.debug("Considering {} undirected links.", undirectedLinks.size());

    // connectivity inspector returns the undirected connections - so we need to check the directional edge exists between
    // the weekly connected target vertex and the provided state (to handle the cases where we've been provided a directed
    // graph)
    List<State> finalLinks = undirectedLinks.stream()
        .filter(target -> stateGraph.containsEdge(state, target))
        .collect(Collectors.toList());

    LOG.debug("Returning {} total candidate transitions for state {}.", finalLinks.size(), state);
    return finalLinks;
  }

  @Override
  public Double scoreTransitionFrom(State current, State next) {
    Optional<DefaultWeightedEdge> edge = Optional.ofNullable(stateGraph.getEdge(current, next));
    return edge.map(stateGraph::getEdgeWeight).orElseThrow(() -> new IllegalStateException("Cannot get weight for edge not in graph (or with unset weight)."));
  }
}
