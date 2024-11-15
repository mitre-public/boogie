package org.mitre.tdp.boogie;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;
import java.util.Objects;

/**
 * Interface representing a turn direction as either left, right, or either (isLeft == isRight == true).
 */
public final class TurnDirection implements Serializable {

  private final boolean isLeft;
  private final boolean isRight;

  private TurnDirection(boolean isLeft, boolean isRight) {
    checkArgument(isLeft || isRight, "isRight and isLeft cannot both be false.");
    this.isLeft = isLeft;
    this.isRight = isRight;
  }

  /**
   * True when the direction of the turn is to the left of the traversal path.
   */
  public boolean isLeft() {
    return isLeft;
  }

  /**
   * True when the direction of the turn is to the right of the traversal path.
   */
  public boolean isRight() {
    return isRight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TurnDirection that = (TurnDirection) o;
    return isLeft == that.isLeft &&
        isRight == that.isRight;
  }

  @Override
  public int hashCode() {
    return Objects.hash(isLeft, isRight);
  }

  @Override
  public String toString() {
    return "TurnDirection{" +
        "isLeft=" + isLeft +
        ", isRight=" + isRight +
        '}';
  }

  public static TurnDirection right() {
    return new TurnDirection(false, true);
  }

  public static TurnDirection left() {
    return new TurnDirection(true, false);
  }

  public static TurnDirection either() {
    return new TurnDirection(true, true);
  }
}
