package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Range;

class TestAltitudeLimitToRange {

  @Test
  void testValueForAtOrAbove() {
    assertEquals(Range.atLeast(10000.), AltitudeLimitToRange.INSTANCE.apply("+", 10000., null));
  }

  @Test
  void testValueForAtOrBelow() {
    assertEquals(Range.atMost(10000.), AltitudeLimitToRange.INSTANCE.apply("-", 10000., null));
  }

  @Test
  void testValueForAt() {
    assertEquals(Range.closed(10000., 10000.), AltitudeLimitToRange.INSTANCE.apply("@", 10000., null));
  }

  @Test
  void testValueForAt_Blank() {
    assertEquals(Range.closed(10000., 10000.), AltitudeLimitToRange.INSTANCE.apply(" ", 10000., null));
  }

  @Test
  void testValueForBetween() {
    assertEquals(Range.closed(6000., 10000.), AltitudeLimitToRange.INSTANCE.apply("B", 10000., 6000.));
  }

  @Test
  void testValueForUnsupportedType() {
    assertEquals(Range.all(), AltitudeLimitToRange.INSTANCE.apply("HI", null, null));
  }

  @Test
  void testD() {
    assertEquals(Range.atMost(10.), AltitudeLimitToRange.INSTANCE.apply("D", null, 10.));
  }

  @Test
  void testG() {
    assertEquals(Range.closed(10., 10.), AltitudeLimitToRange.INSTANCE.apply("G", 10., null));
  }

  @Test
  void testO() {
    assertEquals(Range.atLeast(10.), AltitudeLimitToRange.INSTANCE.apply("O", null, 10.));
  }
}
