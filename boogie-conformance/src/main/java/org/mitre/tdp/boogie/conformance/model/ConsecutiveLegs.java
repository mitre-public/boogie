package org.mitre.tdp.boogie.conformance.model;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.Scorable;
import org.mitre.tdp.boogie.conformance.Scorer;
import org.mitre.tdp.boogie.conformance.scorers.impl.LegScorerFactory;

/**
 * Top level interface implementing class for leg scoring, providing access to both the previous and the subsequent
 * legs as declared in the procedure/airway definition.
 */
public interface ConsecutiveLegs extends Scorable<ConformablePoint, ConsecutiveLegs> {

  /**
   * The leg which which the aircraft is flying from the end of.
   */
  Optional<Leg> previous();

  default <T> Optional<T> previous(Function<Leg, Optional<T>> ext) {
    return previous().map(ext).filter(Optional::isPresent).map(Optional::get);
  }

  /**
   * The leg which the aircraft is flying to the end of given it has finished flying the from leg.
   */
  Leg current();

  /**
   * The next leg following on after the current one.
   */
  Optional<Leg> next();

  default <T> Optional<T> next(Function<Leg, Optional<T>> ext) {
    return next().map(ext).filter(Optional::isPresent).map(Optional::get);
  }

  @Override
  default Scorer<ConformablePoint, ConsecutiveLegs> scorer() {
    return LegScorerFactory.forLegs(this);
  }
}
