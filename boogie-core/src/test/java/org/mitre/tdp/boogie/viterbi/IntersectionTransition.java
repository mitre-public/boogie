package org.mitre.tdp.boogie.viterbi;

import org.mitre.tdp.boogie.viterbi.HmmState;
import org.mitre.tdp.boogie.viterbi.HmmTransition;

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