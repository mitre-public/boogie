package org.mitre.tdp.boogie;

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
   */
  boolean hasRequiredFields(Leg leg);
}
