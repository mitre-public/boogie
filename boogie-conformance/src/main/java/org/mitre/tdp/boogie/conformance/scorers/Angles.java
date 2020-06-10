package org.mitre.tdp.boogie.conformance.scorers;

import org.mitre.tdp.boogie.TurnDirection;

/**
 * Wrapper class for common angle utilities.
 */
public class Angles {

  /**
   * Between for radial angles.
   */
  public static boolean between(double a1, double b1, double b2, TurnDirection direction) {
    if (direction.equals(TurnDirection.RIGHT) && b1 > b2) {
      return (a1 > b1 && a1 < 360.0) || (a1 > 0.0 && a1 < b2);
    }
    if (direction.equals(TurnDirection.LEFT) && b2 > b1) {
      return (a1 > b2 && a1 < 360.0) || (a1 > 0.0 && a1 < b1);
    }
    if (direction.equals(TurnDirection.RIGHT)) {
      return a1 > b1 && a1 < b2;
    }
    if (direction.equals(TurnDirection.LEFT)) {
      return a1 < b1 && a1 > b2;
    }
    return direction.equals(TurnDirection.EITHER);
  }
}
