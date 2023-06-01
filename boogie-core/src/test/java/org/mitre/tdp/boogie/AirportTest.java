package org.mitre.tdp.boogie;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class AirportTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Airport.Standard.class).verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Airport.Record.class).verify();
  }
}
