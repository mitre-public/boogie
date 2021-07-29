package org.mitre.tdp.boogie;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestTurnDirection {

  @Test
  void testEqualsAndHashCode() {
    EqualsVerifier.forClass(TurnDirection.class).verify();
  }
}
