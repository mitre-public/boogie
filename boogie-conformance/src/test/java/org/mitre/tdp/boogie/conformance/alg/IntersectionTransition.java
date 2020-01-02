package org.mitre.tdp.boogie.conformance.alg;

import org.mitre.tdp.boogie.conformance.alg.dp.DynamicProgrammerState;
import org.mitre.tdp.boogie.conformance.alg.dp.DynamicProgrammerTransition;

public class IntersectionTransition implements DynamicProgrammerTransition {

  public final DynamicProgrammerState connected_state;

  public final Double probability;

  public IntersectionTransition(DynamicProgrammerState connected_state, Double probability) {
    this.connected_state = connected_state;
    this.probability = probability;
  }

  @Override
  public DynamicProgrammerState getTransition() {
    return connected_state;
  }

  @Override
  public Double getTransitionProbability() {
    return probability;
  }
}