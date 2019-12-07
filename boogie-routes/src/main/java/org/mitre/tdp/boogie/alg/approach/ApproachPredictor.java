package org.mitre.tdp.boogie.alg.approach;

import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.models.Procedure;

/**
 * The job of an approach predictor is to, given the context of the last and second to last sections
 * of the route string, determine the final approach procedure.
 */
public interface ApproachPredictor {

  /**  */
  Procedure predictApproach(ResolvedSection prev, ResolvedSection last);
}
