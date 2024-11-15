package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestTurnDirection {

  @Test
  void testTurnDirectionValidValue() {
    assertEquals(Optional.of(TurnDirection.R), TurnDirection.SPEC.apply("R"));
  }

  @Test
  void testInvalidTurnDirectionReturnsEmpty() {
    assertEquals(Optional.empty(), TurnDirection.SPEC.apply("D"));
  }
}
