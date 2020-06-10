package org.mitre.tdp.boogie.conformance.alg.assign.dp;

public interface DynamicProgrammerTransition<T, S extends DynamicProgrammerState<T>> {

  S getTransition();

  Double getTransitionProbability();
}