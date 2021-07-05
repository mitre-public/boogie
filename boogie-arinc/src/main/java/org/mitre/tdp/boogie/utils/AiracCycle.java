package org.mitre.tdp.boogie.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.mitre.caasd.commons.TimeWindow;

import com.ko_sys.av.airac.Airac;

/**
 * Decorator class for internal only dependency on the {@link Airac} project.
 */
public final class AiracCycle {

  private final Airac airac;

  public AiracCycle(String cycle) {
    this.airac = Airac.fromIdentifier(cycle);
  }

  public AiracCycle(Instant date) {
    this.airac = Airac.fromInstant(date);
  }

  public AiracCycle(LocalDate date) {
    this(date.atStartOfDay().toInstant(ZoneOffset.UTC));
  }

  /**
   * Returns the start date (inclusive) of the cycle.
   */
  public Instant startDate() {
    return airac.getEffective();
  }

  /**
   * Returns the end date (non-inclusive) of the cycle.
   */
  public Instant endDate() {
    return airac.getNext().getEffective();
  }

  /**
   * Returns the cycle string associated with the cycle.
   *
   * e.g. 1704, 1913, etc.
   */
  public String cycle() {
    return airac.toString();
  }

  /**
   * Convenience method to return the cycle as a {@link TimeWindow}. As TimeWindows are [start, end] closed intervals we
   * decrement a ms from the end of the end date such that the previous cycle does not {@link TimeWindow#contains(Instant)}
   * the {@link #startDate()} of the next.
   */
  public TimeWindow asTimeWindow() {
    return new TimeWindow(startDate(), endDate().minus(Duration.ofMillis(1L)));
  }

  /**
   * Returns true when the given date is aligned with the {@link #startDate()} of the cycle.
   */
  public boolean isAlignedWithStartOfCycle(Instant dt) {
    return startDate().equals(dt);
  }

  /**
   * Returns true when the given date falls within the cycle.
   */
  public boolean fallsWithinCycle(Instant dt) {
    return asTimeWindow().contains(dt);
  }
}
