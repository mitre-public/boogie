package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.TurnDirection;

/**
 * Container class representing a couple of key operations on radial angles with associated turn directions.
 */
public final class RadialAngle {

  private final double initialAngle;
  private final double terminalAngle;
  private final TurnDirection turnDirection;

  public RadialAngle(double initialAngle, double terminalAngle, TurnDirection turnDirection) {
    this.initialAngle = initialAngle;
    this.terminalAngle = terminalAngle;
    this.turnDirection = requireNonNull(turnDirection);
  }

  /**
   * Returns whether the given angle falls between the configured {@link #initialAngle} and {@link #terminalAngle} taking into
   * account the provided {@link TurnDirection}.
   */
  public boolean contains(double angle) {
    if (turnDirection.isRight() && !turnDirection.isLeft() && initialAngle > terminalAngle) {
      return (angle > initialAngle && angle < 360.0) || (angle > 0.0 && angle < terminalAngle);
    }
    if (turnDirection.isLeft() && !turnDirection.isRight() && terminalAngle > initialAngle) {
      return (angle > terminalAngle && angle < 360.0) || (angle > 0.0 && angle < initialAngle);
    }
    if (turnDirection.isRight() && !turnDirection.isLeft()) {
      return angle > initialAngle && angle < terminalAngle;
    }
    if (turnDirection.isLeft() && !turnDirection.isRight()) {
      return angle < initialAngle && angle > terminalAngle;
    }
    return true;
  }
}
