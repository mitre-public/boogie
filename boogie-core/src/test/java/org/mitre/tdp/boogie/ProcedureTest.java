package org.mitre.tdp.boogie;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class ProcedureTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Procedure.Standard.class).verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Procedure.Record.class).verify();
  }
}
