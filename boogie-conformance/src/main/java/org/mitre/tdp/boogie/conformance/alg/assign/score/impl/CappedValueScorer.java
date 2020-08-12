package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

import com.google.common.base.Preconditions;

public class CappedValueScorer implements OnLegScorer {

  private final OnLegScorer onLegScorer;
  private final Double maxValue;

  public CappedValueScorer(OnLegScorer onLegScorer, double maxValue) {
    Preconditions.checkArgument(maxValue <= 1.0);
    Preconditions.checkArgument(maxValue > 0.0);
    this.onLegScorer = onLegScorer;
    this.maxValue = maxValue;
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple) {
    return onLegScorer.scoreAgainstLeg(point, legTriple) * maxValue;
  }

  public static CappedValueScorer wrap(OnLegScorer onLegScorer, Double maxValue) {
    return new CappedValueScorer(onLegScorer, maxValue);
  }
}
