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
    if (direction.isRight() && !direction.isLeft() && b1 > b2) {
      return (a1 > b1 && a1 < 360.0) || (a1 > 0.0 && a1 < b2);
    }
    if (direction.isLeft() && !direction.isRight() && b2 > b1) {
      return (a1 > b2 && a1 < 360.0) || (a1 > 0.0 && a1 < b1);
    }
    if (direction.isRight() && !direction.isLeft()) {
      return a1 > b1 && a1 < b2;
    }
    if (direction.isLeft() && !direction.isRight()) {
      return a1 < b1 && a1 > b2;
    }
    return true;
  }
}
