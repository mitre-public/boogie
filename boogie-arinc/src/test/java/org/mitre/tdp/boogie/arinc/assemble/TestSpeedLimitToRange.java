package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Range;

class TestSpeedLimitToRange {

  @Test
  void testValueForAtOrAbove() {
    assertEquals(Range.atLeast(250.), SpeedLimitToRange.INSTANCE.apply("+", 250.));
  }

  @Test
  void testValueForAtOrBelow() {
    assertEquals(Range.atMost(250.), SpeedLimitToRange.INSTANCE.apply("-", 250.));
  }

  @Test
  void testValueForAt() {
    assertAll(
        () -> assertEquals(Range.closed(250., 250.), SpeedLimitToRange.INSTANCE.apply("@", 250.), "@ - AT"),
        () -> assertEquals(Range.closed(250., 250.), SpeedLimitToRange.INSTANCE.apply(" ", 250.), "blank - AT")
    );
  }

  @Test
  void testValueForNullLimitValue() {
    assertEquals(Range.all(), SpeedLimitToRange.INSTANCE.apply("+", null));
  }
}
