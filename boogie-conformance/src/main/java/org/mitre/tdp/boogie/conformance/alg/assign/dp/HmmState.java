package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import java.util.List;

/**
 * Interface used by the {@link ViterbiTagger} to determine scores and transitions between states.
 * <p>
 * The two interface methods are the required elements of a Hidden Markov Model: a method for estimating
 * the likelihood that an observation (the "stage") was produced by a specific underlying state, and a
 * method for determining transitions from the state and their corresponding likelihoods.
 * <p>
 * @param <T> The stage type.
 */
public interface HmmState<T> {

  double getValue(T stage);

  List<? extends HmmTransition> getPossibleTransitions(T stage);
}