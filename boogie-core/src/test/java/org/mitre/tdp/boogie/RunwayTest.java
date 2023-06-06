package org.mitre.tdp.boogie;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class RunwayTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Runway.Standard.class).verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Runway.Record.class).verify();
  }
}
