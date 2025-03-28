package org.mitre.tdp.boogie.alg.resolve;

import java.util.Arrays;

/**
 * The categorical elements that the route string can directly reference for matching.
 */
public enum ElementType {
  AIRPORT,
  AIRWAY,
  FIX,
  LATLON,
  TAILORED,
  SID,
  STAR,
  APPROACH,
  /**
   * A special element type representing a leg who's lateral path was defined as part of a SID but who's altitude and speed
   * limits come from a joined-to STAR.
   */
  SID_TO_STAR,
  /**
   * A special element type representing a leg who's lateral path was defined as part of an AIRWAY but who's altitude and speed
   * limits come from a joined-to STAR.
   */
  AIRWAY_TO_STAR,
  /**
   * A special element type representing a leg who's lateral path was defined as part of a STAR but who's altitude and speed
   * limits come from a joined-to APPROACH.
   */
  STAR_TO_APPROACH,
  /**
   * A special element type representing a leg who's lateral path was defined as part of an AIRWAY but who's altitude and speed
   * limits come from a joined-to APPROACH.
   */
  AIRWAY_TO_APPROACH,
  /**
   * A special element type representing a leg who's lateral path was defined as part of a SID but who's altitude and speed
   * limits come from a joined-to APPROACH.
   */
  SID_TO_APPROACH;

  public boolean isProcedure() {
    return Arrays.asList(SID, STAR, APPROACH).contains(this);
  }
}
