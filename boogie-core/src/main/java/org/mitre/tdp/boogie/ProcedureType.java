package org.mitre.tdp.boogie;

/**
 * Broad classifier for the "type" of procedure being referenced.
 */
public enum ProcedureType {
  /**
   * Standard Instrument Departure (SID).
   * <br>
   * SIDs exist to get the aircraft from the end of the runway and into enroute airspace headed toward their destination airport.
   */
  SID,
  /**
   * Standard Terminal Arrival Route (STAR).
   * <br>
   * STARs exist to get the aircraft from enroute airspace and into the airport.
   */
  STAR,
  /**
   * Approach procedures exist to get the aircraft from the terminal airspace around the airport onto a runway. They typically
   * have different required equipage levels.
   */
  APPROACH
}
