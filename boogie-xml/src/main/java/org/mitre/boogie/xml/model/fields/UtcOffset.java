package org.mitre.boogie.xml.model.fields;

import java.io.Serializable;
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
}
