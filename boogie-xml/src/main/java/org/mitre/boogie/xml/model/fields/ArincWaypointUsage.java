package org.mitre.boogie.xml.model.fields;

import java.io.Serializable;
import java.util.Objects;

public final class ArincWaypointUsage implements Serializable {
  private final boolean isHi;
  private final boolean isLo;
  private final boolean isTerminal;

  private ArincWaypointUsage(boolean isHi, boolean isLo, boolean isTerminal) {
    this.isHi = isHi;
    this.isLo = isLo;
    this.isTerminal = isTerminal;
  }

  public static ArincWaypointUsage of(boolean isHi, boolean isLo, boolean isTerminal) {
    return new ArincWaypointUsage(isHi, isLo, isTerminal);
  }

  public boolean isHi() {
    return isHi;
  }

  public boolean isLo() {
    return isLo;
  }

  public boolean isTerminal() {
    return isTerminal;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArincWaypointUsage that = (ArincWaypointUsage) o;
    return isHi == that.isHi && isLo == that.isLo && isTerminal == that.isTerminal;
  }

  @Override
  public int hashCode() {
    return Objects.hash(isHi, isLo, isTerminal);
  }

  @Override
  public String toString() {
    return "ArincWaypointUsage{" +
        "isHi=" + isHi +
        ", isLo=" + isLo +
        ", isTerminal=" + isTerminal +
        '}';
  }
}
