package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestRunwayGradient {

  private static final RunwayGradient parser = new RunwayGradient();

  @Test
  void testParseValidRunwayGradient() {
    assertEquals(Optional.of(.45), parser.apply("+0450"));
  }

  @Test
  void testParseNegativeRunwayGradient() {
    assertEquals(Optional.of(-.3), parser.apply("-0300"));
  }

  @Test
  void testReturnsEmptyOnInvalidInput() {
    assertEquals(Optional.empty(), parser.apply("110AB"));
  }
}