package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestEuIndicator {

  private static final EuIndicator parser = new EuIndicator();

  @Test
  void testYReturnsTrue() {
    assertEquals(Optional.of(true), parser.apply("Y"));
  }

  @Test
  void testBlankReturnsFalse() {
    assertEquals(Optional.of(false), parser.apply(" "));
  }
}
