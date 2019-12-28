package org.mitre.tdp.boogie.alg;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.conformance.alg.dp.DynamicProgrammerState;
import org.mitre.tdp.boogie.conformance.alg.dp.DynamicProgrammerTransition;

public enum IntersectionStates implements DynamicProgrammerState<Integer> {

  A(Arrays.asList(2, 8, 6, 3, 3, 8)), // 30
  B(Arrays.asList(5, 6, 5, 9, 5, 2)), // 32
  C(Arrays.asList(7, 7, 2, 6, 7, 6)), // 35
  D(Arrays.asList(4, 3, 6, 3, 9, 5)), // 30
  E(Arrays.asList(5, 9, 3, 3, 1, 5)), // 26
  F(Arrays.asList(6, 4, 9, 2, 3, 3)); // 27

  private final List<Integer> delays;

  IntersectionStates(List<Integer> delays) {
    this.delays = delays;
  }

  @Override
  public double getValue(Integer stage) {
    return delays.get(stage).doubleValue();
  }

  @Override
  public List<DynamicProgrammerTransition> getPossibleTransitions(Integer stage) {
    return IntersectionTransitions.getTransition(this, stage);
  }
}