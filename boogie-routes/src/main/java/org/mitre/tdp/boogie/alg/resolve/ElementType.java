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
  APPROACH;

  public boolean isProcedure() {
    return Arrays.asList(SID, STAR, APPROACH).contains(this);
  }
}
