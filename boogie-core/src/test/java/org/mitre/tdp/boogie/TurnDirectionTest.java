package org.mitre.tdp.boogie;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class TurnDirectionTest {

  @Test
  void testEqualsAndHashCode() {
    EqualsVerifier.forClass(TurnDirection.class).verify();
  }
}
