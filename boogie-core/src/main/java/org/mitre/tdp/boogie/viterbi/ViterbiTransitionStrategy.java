package org.mitre.tdp.boogie.viterbi;

import java.util.Collection;

/**
 * Represents a strategy defining two primary things:
 *
 * 1) The set of candidate states which can be transitioned to from the provided state
 * 2) The likelihood of the transition between a current state and provided target next state
 *
 * See {@link GraphBackedViterbiTransitionStrategy} for a simple implementation of a transitioning strategy.
 */
public interface ViterbiTransitionStrategy<Stage, State> {

  /**
   * Returns the set of states it's valid to transition to from the current state.
   */
  Collection<State> validTransitionsFrom(State state);

  /**
   * Returns the likelihood of the transition between the provided current state and the candidate next state.
   */
  Double scoreTransitionFrom(State current, State next);
}
