package org.mitre.tdp.boogie.util;

import java.util.stream.Stream;

import org.mitre.tdp.boogie.TurnDirection;

/**
 * Utility class for calculating arc angles between two angles in degrees.
 */
public final class ArcAngleCalculator {

  private ArcAngleCalculator() {
  }

  /**
   * Returns the arc angle between two angles in degrees. If either direction it uses shortest path.
   * @param startAngle starting angle in degrees
   * @param endAngle ending angle in degrees
   * @param turnDirection direction of the arc
   * @return the angle in the arc in degrees
   */
  public static double from(double startAngle, double endAngle, TurnDirection turnDirection) {
    if (turnDirection.isRight() && !turnDirection.isLeft()) {
      return (360 + endAngle - startAngle) % 360.;
    }

    if (turnDirection.isLeft() && !turnDirection.isRight()) {
      return (360 + startAngle - endAngle) % 360.;
    }

    return Stream.of((360 + endAngle - startAngle) % 360., (360 + startAngle - endAngle) % 360.)
        .min(Double::compare)
        .orElseThrow(IllegalStateException::new);
  }
}
