package org.mitre.tdp.boogie;

/**
 * High-level classification of the type of transition implementing the {@link Transition} interface.
 *
 * This breakdown is leveraged in the route expansion logic.
 */
public enum TransitionType {
  /**
   * Indicates this transition is mean't to take aircraft from/to the enroute portion of the flight plan
   * into/out of the common portion of the STAR or SID.
   *
   * Along transitions of this type is frequently when events like holding occur.
   */
  ENROUTE,
  /**
   * Indicates this is the shared portion of the SID or STAR through which all traffic flying the procedure
   * travels.
   */
  COMMON,
  /**
   * Indicates the transition is mean't to support service either to or from a runway at a particular
   * airport. For SIDs typically this is a sequence of CA and VI type legs, for STARs this is really
   * to get the aircraft to its preferred {@link ProcedureType#APPROACH} for its target runway.
   */
  RUNWAY,
  /**
   * Common type for all approach procedure transitions.
   */
  APPROACH,
  /**
   * If there are tags for missed approach transitions as part of the {@link ProcedureType#APPROACH} use
   * this tag can be used and they will be zipped on the ends of the primary approach transitions.
   *
   * If missed approaches aren't a concern then just tag these as APPROACH, there is generally little
   * affect on the algorithms results.
   */
  MISSED
}
