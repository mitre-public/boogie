package org.mitre.boogie.xml.model.fields;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public final class UtcOffset implements Serializable {
  private final Integer hoursOffset;
  private final Integer minutesOffset;
  private final Boolean daylightObserving;

  public UtcOffset(Integer hoursOffset, Integer minutesOffset, Boolean daylightObserving) {
    this.hoursOffset = hoursOffset;
    this.minutesOffset = minutesOffset;
    this.daylightObserving = daylightObserving;
  }

  public static UtcOffset from(Integer hoursOffset, Integer minutesOffset, Boolean daylightObserving) {
    return new UtcOffset(hoursOffset, minutesOffset, daylightObserving);
  }

  public Optional<Integer> hoursOffset() {
    return Optional.ofNullable(hoursOffset);
  }

  public Optional<Integer> minutesOffset() {
    return Optional.ofNullable(minutesOffset);
  }

  public Optional<Boolean> daylightObserving() {
    return Optional.ofNullable(daylightObserving);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    UtcOffset utcOffset = (UtcOffset) o;
    return Objects.equals(hoursOffset, utcOffset.hoursOffset) && Objects.equals(minutesOffset, utcOffset.minutesOffset) && Objects.equals(daylightObserving, utcOffset.daylightObserving);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hoursOffset, minutesOffset, daylightObserving);
  }

  @Override
  public String toString() {
    return "UtcOffset{" +
        "hoursOffset=" + hoursOffset +
        ", minutesOffset=" + minutesOffset +
        ", daylightObserving=" + daylightObserving +
        '}';
  }
}
