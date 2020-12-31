package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

import com.google.common.base.Preconditions;

public class ScaledValueScorer implements OnLegScorer {

  private final OnLegScorer onLegScorer;
  private final Double scaleFactor;

  public ScaledValueScorer(OnLegScorer onLegScorer, double scaleFactor) {
    Preconditions.checkArgument(scaleFactor <= 1.0);
    Preconditions.checkArgument(scaleFactor > 0.0);
    this.onLegScorer = onLegScorer;
    this.scaleFactor = scaleFactor;
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple) {
    return onLegScorer.scoreAgainstLeg(point, legTriple) * scaleFactor;
  }

  public static ScaledValueScorer wrap(OnLegScorer onLegScorer, Double maxValue) {
    return new ScaledValueScorer(onLegScorer, maxValue);
  }
}
