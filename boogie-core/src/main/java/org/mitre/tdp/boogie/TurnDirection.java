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

  /**
   * Creates an anonymous instance of a {@link TurnDirection} indicating a left turn.
   */
  static TurnDirection right() {
    return new TurnDirection() {
      @Override
      public boolean isLeft() {
        return false;
      }

      @Override
      public boolean isRight() {
        return true;
      }
    };
  }

  /**
   * Creates an anonymous instance of a {@link TurnDirection} indicating a right turn.
   */
  static TurnDirection left() {
    return new TurnDirection() {
      @Override
      public boolean isLeft() {
        return true;
      }

      @Override
      public boolean isRight() {
        return false;
      }
    };
  }

  /**
   * Creates an anonymous instance of a {@link TurnDirection} indicating a turn in either direction.
   */
  static TurnDirection either() {
    return new TurnDirection() {
      @Override
      public boolean isLeft() {
        return true;
      }

      @Override
      public boolean isRight() {
        return true;
      }
    };
  }
}
