package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class TestArincHeliport {
  @Test
  void testEquals() {
    EqualsVerifier.forClass(ArincHeliport.class);
  }

  @Test
  void builder() {
    ArincHeliport port = ArincHeliport.builder().build();
    assertNotNull(port);
    ArincHeliport.Builder builder = port.toBuilder();
    assertNotNull(builder);
  }
}
