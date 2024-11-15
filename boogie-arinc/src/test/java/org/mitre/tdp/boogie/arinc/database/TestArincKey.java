package org.mitre.tdp.boogie.arinc.database;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestArincKey {

  @Test
  void testEqualsAndHashCode() {
    EqualsVerifier.forClass(ArincKey.class).verify();
  }
}
