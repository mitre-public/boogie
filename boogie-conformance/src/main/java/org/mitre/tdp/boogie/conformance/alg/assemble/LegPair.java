package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Optional;

import org.mitre.tdp.boogie.Leg;

/**
 * Interface representing a pair of subsequently traversed legs. This class is generally used in two ways:
 *
 * 1) To represent legs which can be reduced based on their similar features.
 * 2) To represent pairs of legs for the assessment of conformance - which as typically defined is a question of lateral
 * deviation from the flown path.
 */
public interface LegPair {

  /**
   * The leg which which the aircraft is flying from the end of.
   */
  Leg previous();

  /**
   * The leg which the aircraft is flying to the end of given it has finished flying the from leg.
   */
  Leg current();

  /**
   * Returns the source object for the ConsecutiveLegs if set via {@link #setSourceObject(Object)};
   */
  default Optional<Object> getSourceObject() {
    return Optional.empty();
  }

  /**
   * Sets the source object for the consecutive legs if configured.
   */
  default LegPair setSourceObject(Object source) {
    return this;
  }
}
