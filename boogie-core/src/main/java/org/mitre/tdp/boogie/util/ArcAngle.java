package org.mitre.tdp.boogie.util;

import org.mitre.tdp.boogie.TurnDirection;

public final class ArcAngle {
  private ArcAngle() {}
  /**
   * While we don't use the E in this method its covered as better than throwing
   *
   * @param startAngle    starting angle in degrees
   * @param endAngle      ending angle in degrees
   * @param turnDirection LEFT/RIGHt/EITHER because this is an arc leg that requires a value to make sense of the leg
   * @return the angle
   */
  public static double from(double startAngle, double endAngle, TurnDirection turnDirection) {
    if (turnDirection.isRight() && !turnDirection.isLeft()) {
      return (360 + endAngle - startAngle) % 360.;
    }

    if (turnDirection.isLeft() && !turnDirection.isRight()) {
      return (360 + startAngle - endAngle) % 360.;
    }

    throw new IllegalArgumentException("Invalid turn direction: " + turnDirection);
  }
}
