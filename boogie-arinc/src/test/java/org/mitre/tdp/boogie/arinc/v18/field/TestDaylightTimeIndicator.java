package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestDaylightTimeIndicator {

  private static final DaylightTimeIndicator parser = new DaylightTimeIndicator();

  @Test
  void testEmptyInputIsFalse() {
    assertEquals(Optional.of(false), parser.apply(""));
  }

  @Test
  void testWhitespaceInputIsFalse() {
    assertEquals(Optional.of(false), parser.apply("    "));
  }

  @Test
  void test_N_IsFalse() {
    assertEquals(Optional.of(false), parser.apply("N"));
  }

  @Test
  void test_Y_IsTrue() {
    assertEquals(Optional.of(true), parser.apply("Y"));
  }
}
