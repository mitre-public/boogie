package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestAltitudeDescription {

  private static final AltitudeDescription parser = new AltitudeDescription();

  @Test
  void testFiltersUnallowedValues() {
    assertFalse(AltitudeDescription.allowedValues.contains("Q"));
    assertEquals(Optional.empty(), parser.apply("Q"));
  }

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
}