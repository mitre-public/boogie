package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import java.util.Arrays;
import java.util.List;

public enum IntersectionStates implements HmmState<Integer> {

  A(Arrays.asList(.2, .8, .6, .3, .3, .8)),
  B(Arrays.asList(.5, .6, .5, .9, .5, .2)),
  C(Arrays.asList(.7, .7, .2, .6, .7, .6)),
  D(Arrays.asList(.4, .3, .6, .3, .9, .5)),
  E(Arrays.asList(.5, .9, .3, .3, .1, .5)),
  F(Arrays.asList(.6, .4, .9, .2, .3, .3));

  private final List<Double> delays;

  IntersectionStates(List<Double> delays) {
    this.delays = delays;
  }

  @Override
  public double getValue(Integer stage) {
    return delays.get(stage);
  }

  @Override
  public List<HmmTransition> getPossibleTransitions(Integer stage) {
    return IntersectionTransitions.getTransition(this, stage);
  }
}