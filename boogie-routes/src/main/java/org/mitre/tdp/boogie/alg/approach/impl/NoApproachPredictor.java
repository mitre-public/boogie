package org.mitre.tdp.boogie.alg.approach.impl;

import org.mitre.tdp.boogie.alg.ExpandRoutes;
import org.mitre.tdp.boogie.alg.approach.ApproachPredictor;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

/**
 * A dummy approach predictor for cases where no good alternative predictor will work.
 */
public class NoApproachPredictor implements ApproachPredictor {
  @Override
  public void configure(ExpandRoutes expander) {
  }

  @Override
  public ResolvedSection predictCandidateApproaches(ResolvedSection prev, ResolvedSection last) {
    SectionSplit split = new SectionSplit.Builder().setValue("APCH").setIndex(0).build();
    return new ResolvedSection(split);
  }
}
