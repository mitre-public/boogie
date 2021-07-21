package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestTurnDirectionValid {

  private static final TurnDirectionValid parser = new TurnDirectionValid();

  @Test
  void testParseValidTurnDirection() {
    assertEquals(Optional.of(true), parser.apply("Y"));
  }

  @Test
  void testParseFalseIfBlank() {
    assertEquals(Optional.of(false), parser.apply(" "));
  }

  @Test
  void testParserFalseForRandomCrap() {
    assertEquals(Optional.of(false), parser.apply("G"));
  }
}
