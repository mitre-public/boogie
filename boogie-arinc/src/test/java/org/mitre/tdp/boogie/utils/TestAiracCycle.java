package org.mitre.tdp.boogie.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class TestAiracCycle {

  private static AiracCycle cycle = new AiracCycle("1901");

  @Test
  public void testAiracCycleFromCycle() {
    assertEquals("1901", cycle.cycle());
  }

  @Test
  public void testAiracCycleStartDate() {
    assertEquals(Instant.parse("2019-01-03T00:00:00.00Z"), cycle.startDate());
  }

  private static AiracCycle instant = new AiracCycle(Instant.parse("2019-01-03T00:00:00.00Z"));

  @Test
  public void testAiracCycleFromCycle2() {
    assertEquals("1901", instant.cycle());
  }

  @Test
  public void testAiracCycleStartDate2() {
    assertEquals(Instant.parse("2019-01-03T00:00:00.00Z"), instant.startDate());
  }

  private static AiracCycle local = new AiracCycle(LocalDate.of(2019, 1, 3));

  @Test
  public void testAiracCycleFromCycle3() {
    assertEquals("1901", local.cycle());
  }

  @Test
  public void testAiracCycleStartDate3() {
    assertEquals(Instant.parse("2019-01-03T00:00:00.00Z"), local.startDate());
  }

  @Test
  public void testEndDate() {
    assertEquals(Instant.parse("2019-01-31T00:00:00.00Z"), local.endDate());
  }

  @Test
  public void testIsStartOfCycleIsAtStartOfCycle() {
    assertTrue(local.isAlignedWithStartOfCycle(Instant.parse("2019-01-03T00:00:00.00Z")));
  }

  @Test
  public void testIsOffsetIsNotAtStartOfCycle() {
    assertFalse(local.isAlignedWithStartOfCycle(Instant.parse("2019-01-05T00:00:00.00Z")));
  }

  @Test
  public void testCycleContainsDateInCycle() {
    assertTrue(local.fallsWithinCycle(Instant.parse("2019-01-05T00:00:00.00Z")));
  }

  @Test
  public void testCycleStartFallsWithinCycle() {
    assertTrue(local.fallsWithinCycle(Instant.parse("2019-01-03T00:00:00.00Z")));
  }

  @Test
  public void testCycleEndDoesNotFallWithinCycle() {
    assertFalse(local.fallsWithinCycle(Instant.parse("2019-01-31T00:00:00.00Z")));
  }
}
