package org.mitre.tdp.boogie;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class LegTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Leg.Standard.class).verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Leg.Record.class).verify();
  }
}
