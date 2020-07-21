package org.mitre.tdp.boogie.alg.resolve;

import java.util.Optional;

/**
 * Interface for functionals which can, based on internal state, return the predicted arrival runway of a flight.
 */
@FunctionalInterface
public interface RunwayPredictor {

  /**
   * Returns the predicated arrival runway for a flight if one could be deduced.
   */
  Optional<String> predictedRunway();

  /**
   * Returns a new no-op runway predictor which simply returns an empty optional.
   */
  static RunwayPredictor noop() {
    return Optional::empty;
  }
}
