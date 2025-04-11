package org.mitre.boogie.xml.model.fields;

import java.io.Serializable;
import java.util.Objects;

public final class ArincFraInfo implements Serializable {
  private final boolean isFraArrivalTransitionPoint;
  private final boolean isFraDepartureTransitionPoint;
  private final boolean isFraIntermediatePoint;
  private final boolean isFraTerminalHoldingPoint;
  private final boolean isFraEntryPoint;
  private final boolean isFraExitPoint;

  private ArincFraInfo(Builder builder) {
    this.isFraArrivalTransitionPoint = builder.isFraArrivalTransitionPoint;
    this.isFraDepartureTransitionPoint = builder.isFraDepartureTransitionPoint;
    this.isFraIntermediatePoint = builder.isFraIntermediatePoint;
    this.isFraTerminalHoldingPoint = builder.isFraTerminalHoldingPoint;
    this.isFraEntryPoint = builder.isFraEntryPoint;
    this.isFraExitPoint = builder.isFraExitPoint;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .fraArrivalTransitionPoint(isFraArrivalTransitionPoint)
        .fraDepartureTransitionPoint(isFraDepartureTransitionPoint)
        .fraIntermediatePoint(isFraIntermediatePoint)
        .fraTerminalHoldingPoint(isFraTerminalHoldingPoint)
        .fraEntryPoint(isFraEntryPoint)
        .fraExitPoint(isFraExitPoint);
  }

  public boolean isFraArrivalTransitionPoint() {
    return isFraArrivalTransitionPoint;
  }

  public boolean isFraDepartureTransitionPoint() {
    return isFraDepartureTransitionPoint;
  }

  public boolean isFraIntermediatePoint() {
    return isFraIntermediatePoint;
  }

  public boolean isFraTerminalHoldingPoint() {
    return isFraTerminalHoldingPoint;
  }

  public boolean isFraEntryPoint() {
    return isFraEntryPoint;
  }

  public boolean isFraExitPoint() {
    return isFraExitPoint;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArincFraInfo that = (ArincFraInfo) o;
    return isFraArrivalTransitionPoint == that.isFraArrivalTransitionPoint && isFraDepartureTransitionPoint == that.isFraDepartureTransitionPoint && isFraIntermediatePoint == that.isFraIntermediatePoint && isFraTerminalHoldingPoint == that.isFraTerminalHoldingPoint && isFraEntryPoint == that.isFraEntryPoint && isFraExitPoint == that.isFraExitPoint;
  }

  @Override
  public int hashCode() {
    return Objects.hash(isFraArrivalTransitionPoint, isFraDepartureTransitionPoint, isFraIntermediatePoint, isFraTerminalHoldingPoint, isFraEntryPoint, isFraExitPoint);
  }

  @Override
  public String toString() {
    return "ArincFraInfo{" +
        "isFraArrivalTransitionPoint=" + isFraArrivalTransitionPoint +
        ", isFraDepartureTransitionPoint=" + isFraDepartureTransitionPoint +
        ", isFraIntermediatePoint=" + isFraIntermediatePoint +
        ", isFraTerminalHoldingPoint=" + isFraTerminalHoldingPoint +
        ", isFraEntryPoint=" + isFraEntryPoint +
        ", isFraExitPoint=" + isFraExitPoint +
        '}';
  }

  public static class Builder {
    private boolean isFraArrivalTransitionPoint = false;
    private boolean isFraDepartureTransitionPoint = false;
    private boolean isFraIntermediatePoint = false;
    private boolean isFraTerminalHoldingPoint = false;
    private boolean isFraEntryPoint = false;
    private boolean isFraExitPoint = false;
    private Builder() {}

    public Builder fraArrivalTransitionPoint(boolean fraArrivalTransitionPoint) {
      isFraArrivalTransitionPoint = fraArrivalTransitionPoint;
      return this;
    }

    public Builder fraDepartureTransitionPoint(boolean fraDepartureTransitionPoint) {
      isFraDepartureTransitionPoint = fraDepartureTransitionPoint;
      return this;
    }

    public Builder fraIntermediatePoint(boolean fraIntermediatePoint) {
      isFraIntermediatePoint = fraIntermediatePoint;
      return this;
    }

    public Builder fraTerminalHoldingPoint(boolean fraTerminalHoldingPoint) {
      isFraTerminalHoldingPoint = fraTerminalHoldingPoint;
      return this;
    }

    public Builder fraEntryPoint(boolean fraEntryPoint) {
      isFraEntryPoint = fraEntryPoint;
      return this;
    }

    public Builder fraExitPoint(boolean fraExitPoint) {
      isFraExitPoint = fraExitPoint;
      return this;
    }

    public ArincFraInfo build() {
      return new ArincFraInfo(this);
    }
  }

}
