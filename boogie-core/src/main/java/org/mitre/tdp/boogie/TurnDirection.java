package org.mitre.tdp.boogie;

/**
 * Interface representing a turn direction as either left, right, or either (isLeft == isRight == true).
 */
public interface TurnDirection {
  /**
   * True when the direction of the turn is to the left of the traversal path.
   */
  boolean isLeft();

  /**
   * True when the direction of the turn is to the right of the traversal path.
   */
  boolean isRight();
}
