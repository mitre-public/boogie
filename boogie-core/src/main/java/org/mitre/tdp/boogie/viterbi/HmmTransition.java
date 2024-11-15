package org.mitre.tdp.boogie.viterbi;

/**
 * Interface used by the {@link ViterbiTagger} to determine valid transitions between
 * Hidden Markov Model states (see {@link HmmState}) and their corresponding likelihoods.
 * <p>
 * @param <T> The HMM Stage type
 * @param <S> The HmmState implementation type
 */
public interface HmmTransition<T, S extends HmmState<T>> {

  S getTransition();

  Double getTransitionProbability();
}