package org.mitre.tdp.boogie;

/**
 * Interface for classes which can operate as a container for leg information relevant to conformance.
 */
public interface LegType {

  /**
   * Returns whether the leg is concrete - i.e. ends in a physical fix.
   */
  boolean isConcrete();

  /**
   * Returns whether the leg specifies/traces an arc for the aircraft to fly.
   */
  boolean isArc();

  /**
   * Returns whether the leg has the required fields to be used as a leg of the given type.
   *
   * Note - this is <i>not</i> necessarily a complete check as for certain leg types the information needed to fly them may
   * be contained in the previous/next legs of the sequence.
   */
  boolean hasRequiredFields(Leg leg);
}
