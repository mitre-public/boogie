package org.mitre.tdp.boogie.conformance.alg.assign.score;

import org.mitre.tdp.boogie.TurnDirection;

/**
 * Wrapper class for common angle utilities.
 */
public interface Angles {

  double initialAngle();

  double terminalAngle();

  TurnDirection turnDirection();

  boolean contains(double angle);
}
