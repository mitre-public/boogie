package org.mitre.tdp.boogie.conformance.scorers;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.Scorable;
import org.mitre.tdp.boogie.conformance.Scorer;
import org.mitre.tdp.boogie.conformance.scorers.impl.LegScorerFactory;

/**
 * Top level interface implementing class for leg scoring, providing access to both the previous
 * and the subsequent legs as declared in the procedure/airway definition.
 */
public interface ConsecutiveLegs extends Scorable<ConformablePoint> {

  /**
   * The leg which which the aircraft is flying from the end of.
   */
  Leg from();

  /**
   * The leg which the aircraft is flying to the end of given it has finished flying the from leg.
   */
  Leg to();

  @Override
  default Scorer<ConformablePoint> scorer() {
    return LegScorerFactory.forLegs(this);
  }
}
