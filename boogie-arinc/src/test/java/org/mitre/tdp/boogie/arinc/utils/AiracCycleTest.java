package org.mitre.tdp.boogie.arinc.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class AiracCycleTest {

  @Test
  void testAiracCycleStartDate() {
    assertEquals(Instant.parse("2019-01-03T00:00:00.00Z"), AiracCycle.startDate("1901"));
  }

  @Test
  void testCycleFromDate() {
    assertEquals("1901", AiracCycle.cycleFor(Instant.parse("2019-01-03T00:00:00.00Z")));
  }
}
