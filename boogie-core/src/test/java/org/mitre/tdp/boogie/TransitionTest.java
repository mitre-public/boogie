package org.mitre.tdp.boogie;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class TransitionTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Transition.Standard.class).verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Transition.Record.class).verify();
  }
}
