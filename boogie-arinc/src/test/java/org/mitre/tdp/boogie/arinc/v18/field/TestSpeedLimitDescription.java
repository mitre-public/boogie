package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestSpeedLimitDescription {

  private static final SpeedLimitDescription parser = new SpeedLimitDescription();

  @Test
  void testBlankDescriptionIsAllowed() {
    assertEquals(Optional.of("@"), parser.apply(" "), "Space should be allowed and mapped to '@'");
  }

  @Test
  void testApersandDescriptionIsAllowed() {
    assertEquals(Optional.of("@"), parser.apply("@"));
  }

  @Test
  void testPlusDescriptionIsAllowed() {
    assertEquals(Optional.of("+"), parser.apply("+"));
  }

  @Test
  void testMinusDescriptionIsAllowed() {
    assertEquals(Optional.of("-"), parser.apply("-"));
  }

  @Test
  void testFiltersUnsupportedInput() {
    assertEquals(Optional.empty(), parser.apply("HI"));
  }
}
