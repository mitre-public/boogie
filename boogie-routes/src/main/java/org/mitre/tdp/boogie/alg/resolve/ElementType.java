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

  /**
   * This isn't a best practice implementation but this class as a whole is mainly provided for ease-of-use in downstream apps.
   */
  public static ElementType fromResolvedElement(ResolvedElement resolvedElement) {
    if (resolvedElement instanceof AirportElement) {
      return AIRPORT;
    } else if (resolvedElement instanceof AirwayElement) {
      return AIRWAY;
    } else if (resolvedElement instanceof FixElement) {
      return FIX;
    } else if (resolvedElement instanceof SidElement) {
      return SID;
    } else if (resolvedElement instanceof StarElement) {
      return STAR;
    } else if (resolvedElement instanceof ApproachElement) {
      return APPROACH;
    } else if (resolvedElement instanceof LatLonElement) {
      return LATLON;
    } else if (resolvedElement instanceof TailoredElement) {
      return TAILORED;
    } else {
      throw new IllegalArgumentException("Unknown how to map input ResolvedElement to ElementType. ResolvedElement class was: ".concat(resolvedElement.getClass().getSimpleName()));
    }
  }
}
