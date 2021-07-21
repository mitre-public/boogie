package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestSpeedLimitDescription {

  @Test
  void testBlankDescriptionIsAt() {
    assertEquals(Optional.of(SpeedLimitDescription.AT), SpeedLimitDescription.SPEC.apply(" "));
  }

  @Test
  void testApersandDescriptionIsAt() {
    assertEquals(Optional.of(SpeedLimitDescription.AT), SpeedLimitDescription.SPEC.apply("@"));
  }

  @Test
  void testPlusDescriptionIsAtOrAbove() {
    assertEquals(Optional.of(SpeedLimitDescription.AT_OR_ABOVE), SpeedLimitDescription.SPEC.apply("+"));
  }

  @Test
  void testMinusDescriptionIsAtOrBelow() {
    assertEquals(Optional.of(SpeedLimitDescription.AT_OR_BELOW), SpeedLimitDescription.SPEC.apply("-"));
  }

  @Test
  void testFiltersUnsupportedInput() {
    assertEquals(Optional.empty(), SpeedLimitDescription.SPEC.apply("HI"));
  }
}
