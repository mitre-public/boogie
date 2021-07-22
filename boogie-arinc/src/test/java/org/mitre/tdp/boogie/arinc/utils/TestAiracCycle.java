package org.mitre.tdp.boogie.arinc.utils;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class TestAiracCycle {

  private static final AiracCycle cycle = new AiracCycle("1901");

  @Test
  void testAiracCycleFromCycle() {
    assertEquals("1901", cycle.cycle());
  }

  @Test
  void testAiracCycleStartDate() {
    assertEquals(Instant.parse("2019-01-03T00:00:00.00Z"), cycle.startDate());
  }

  private static final AiracCycle instant = new AiracCycle(Instant.parse("2019-01-03T00:00:00.00Z"));

  @Test
  void testAiracCycleFromCycle2() {
    assertEquals("1901", instant.cycle());
  }

  @Test
  void testAiracCycleStartDate2() {
    assertEquals(Instant.parse("2019-01-03T00:00:00.00Z"), instant.startDate());
  }

  private static final AiracCycle local = new AiracCycle(LocalDate.of(2019, 1, 3));

  @Test
  void testAiracCycleFromCycle3() {
    assertEquals("1901", local.cycle());
  }

  @Test
  void testAiracCycleStartDate3() {
    assertEquals(Instant.parse("2019-01-03T00:00:00.00Z"), local.startDate());
  }

  @Test
  void testEndDate() {
    assertEquals(Instant.parse("2019-01-31T00:00:00.00Z"), local.endDate());
  }

  @Test
  void testIsStartOfCycleIsAtStartOfCycle() {
    assertTrue(local.isAlignedWithStartOfCycle(Instant.parse("2019-01-03T00:00:00.00Z")));
  }

  @Test
  void testIsOffsetIsNotAtStartOfCycle() {
    assertFalse(local.isAlignedWithStartOfCycle(Instant.parse("2019-01-05T00:00:00.00Z")));
  }

  @Test
  void testCycleContainsDateInCycle() {
    assertTrue(local.fallsWithinCycle(Instant.parse("2019-01-05T00:00:00.00Z")));
  }

  @Test
  void testCycleStartFallsWithinCycle() {
    assertTrue(local.fallsWithinCycle(Instant.parse("2019-01-03T00:00:00.00Z")));
  }

  @Test
  void testCycleEndDoesNotFallWithinCycle() {
    assertFalse(local.fallsWithinCycle(Instant.parse("2019-01-31T00:00:00.00Z")));
  }

  @Test
  void testCyclesBetween() {
    Instant start = Instant.parse("2015-01-01T00:00:00Z");
    Instant end1 = Instant.parse("2015-01-02T00:00:00Z");
    Instant end2 = Instant.parse("2015-06-01T00:00:00Z");

    assertAll(
        () -> assertEquals(singletonList("1413"), AiracCycle.cyclesBetween(start, end1)),
        () -> assertEquals(Arrays.asList("1413", "1501", "1502", "1503", "1504", "1505", "1506"), AiracCycle.cyclesBetween(start, end2))
    );
  }
}
