package org.mitre.tdp.boogie.conformance.alg.assign.score;

import org.mitre.tdp.boogie.TurnDirection;

/**
 * Interface for creating angles objects which act with respect to radial angles.
 */
public interface RadialAngles extends Angles {

  /**
   * Between for radial angles.
   */
  @Override
  default boolean contains(double angle) {
    if (turnDirection().isRight() && !turnDirection().isLeft() && initialAngle() > terminalAngle()) {
      return (angle > initialAngle() && angle < 360.0) || (angle > 0.0 && angle < terminalAngle());
    }
    if (turnDirection().isLeft() && !turnDirection().isRight() && terminalAngle() > initialAngle()) {
      return (angle > terminalAngle() && angle < 360.0) || (angle > 0.0 && angle < initialAngle());
    }
    if (turnDirection().isRight() && !turnDirection().isLeft()) {
      return angle > initialAngle() && angle < terminalAngle();
    }
    if (turnDirection().isLeft() && !turnDirection().isRight()) {
      return angle < initialAngle() && angle > terminalAngle();
    }
    return true;
  }

  static RadialAngles of(double initial, double terminal, TurnDirection turnDirection) {
    return new RadialAngles() {
      @Override
      public double initialAngle() {
        return initial;
      }

      @Override
      public double terminalAngle() {
        return terminal;
      }

      @Override
      public TurnDirection turnDirection() {
        return turnDirection;
      }
    };
  }
}
