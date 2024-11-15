package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestDirectionRestriction {

  private static final DirectionRestriction parser = new DirectionRestriction();

  @Test
  void testEmptyOnInvalidValues() {
    assertAll(
        () -> assertEquals(Optional.empty(), parser.apply("A")),
        () -> assertEquals(Optional.empty(), parser.apply("1")),
        () -> assertEquals(Optional.empty(), parser.apply("@"))
    );
  }

  @Test
  void testValidValues() {
    assertAll(
        () -> assertEquals(Optional.of("F"), parser.apply("F")),
        () -> assertEquals(Optional.of("B"), parser.apply("B")),
        () -> assertEquals(Optional.of(" "), parser.apply(" "))
    );
  }
}
