package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import java.util.List;

public interface DynamicProgrammerState<T>  {

  double getValue(T stage);

  List<? extends DynamicProgrammerTransition> getPossibleTransitions(T stage);
}