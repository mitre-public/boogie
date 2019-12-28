package org.mitre.tdp.boogie.conformance.alg.dp;

public interface DynamicProgrammerTransition<T, S extends DynamicProgrammerState<T>> {

  S getTransition();

  Double getTransitionProbability();
}