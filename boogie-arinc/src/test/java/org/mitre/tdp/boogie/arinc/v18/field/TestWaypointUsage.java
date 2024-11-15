package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestWaypointUsage {

  private static final WaypointUsage parser = new WaypointUsage();

  @Test
  void testPassOnValidColumn12() {
    assertEquals(Optional.of("RB"), parser.apply("RB"));
  }

  @Test
  void testPassOnBlankColumn1() {
    assertEquals(Optional.of(" B"), parser.apply(" B"));
  }

  @Test
  void testPassOnBlankColumn2() {
    assertEquals(Optional.of("R "), parser.apply("R "));
  }

  @Test
  void testParseEmptyOnNonRColumn1() {
    assertEquals(Optional.empty(), parser.apply("QB"));
  }

  @Test
  void testParseEmptyOnInvalidColumn2() {
    assertEquals(Optional.empty(), parser.apply("RA"));
  }
}
