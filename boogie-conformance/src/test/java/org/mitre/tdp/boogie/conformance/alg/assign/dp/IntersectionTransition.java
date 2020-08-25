package org.mitre.tdp.boogie.conformance.alg.assign.dp;

public class IntersectionTransition implements HmmTransition {

  public final HmmState connected_state;

  public final Double probability;

  public IntersectionTransition(HmmState connected_state, Double probability) {
    this.connected_state = connected_state;
    this.probability = probability;
  }

  @Override
  public HmmState getTransition() {
    return connected_state;
  }

  @Override
  public Double getTransitionProbability() {
    return probability;
  }
}