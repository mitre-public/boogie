package org.mitre.tdp.boogie;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class AirwayTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Airway.Standard.class).verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Airway.Record.class).verify();
  }
}
