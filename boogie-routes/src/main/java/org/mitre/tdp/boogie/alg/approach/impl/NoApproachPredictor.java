package org.mitre.tdp.boogie.alg.approach.impl;

import java.util.Optional;

import org.mitre.tdp.boogie.alg.approach.ApproachPredictor;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;

/**
 * A dummy approach predictor for cases where no good alternative predictor will work.
 */
public final class NoApproachPredictor implements ApproachPredictor {

  @Override
  public Optional<ResolvedSection> predictCandidateApproaches(ResolvedSection prev, ResolvedSection last) {
    return Optional.empty();
  }
}
