package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimitDescription;

import com.google.common.collect.Range;

class TestSpeedLimitToRange {

  private static final SpeedLimitToRange converter = new SpeedLimitToRange();

  @Test
  void testValueForAtOrAbove() {
    assertEquals(Range.atLeast(250.), converter.apply(SpeedLimitDescription.AT_OR_ABOVE, 250.));
  }

  @Test
  void testValueForAtOrBelow() {
    assertEquals(Range.atMost(250.), converter.apply(SpeedLimitDescription.AT_OR_BELOW, 250.));
  }

  @Test
  void testValueForAt() {
    assertEquals(Range.closed(250., 250.), converter.apply(SpeedLimitDescription.AT, 250.));
  }

  @Test
  void testValueForNullLimitValue() {
    assertEquals(Range.all(), converter.apply(SpeedLimitDescription.AT_OR_ABOVE, null));
  }
}
