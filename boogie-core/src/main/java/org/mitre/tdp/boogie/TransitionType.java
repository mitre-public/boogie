package org.mitre.tdp.boogie;

/**
 * High-level classification of the type of transition implementing the {@link Transition} interface.
 * <br>
 * This breakdown is leveraged in the route expansion logic.
 */
public enum TransitionType {
  /**
   * Indicates this transition is meant to take aircraft from/to the enroute portion of the flight plan into/out of the common
   * portion of the STAR or SID.
   * <br>
   * Along transitions of this type is frequently when events like holding occur.
   */
  ENROUTE,
  /**
   * Indicates this is the shared portion of the SID or STAR through which all traffic flying the procedure travels.
   * <br>
   * Or indicates this is the
   */
  COMMON,
  /**
   * Indicates the transition is meant to support service either to or from a runway at a particular airport. For SIDs typically
   * this is a sequence of CA and VI type legs, for STARs this is really to get the aircraft in position to join their preferred
   * {@link ProcedureType#APPROACH} for its assigned runway.
   * <br>
   * This can also be tagged in approach procedures when the approach serves multiple runways.
   */
  RUNWAY,
  /**
   * Indicates an approach transition - typically these represent a sequence of legs (coded as part of the overall approach
   * procedure) which get the aircraft from initial/intermediate approach fix to the final approach fix.
   */
  APPROACH,
  /**
   * Indicates a transition represents the dedicated missed approach portion of an approach procedure.
   */
  MISSED
}
