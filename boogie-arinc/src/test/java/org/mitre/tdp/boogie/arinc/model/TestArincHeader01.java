package org.mitre.tdp.boogie.arinc.model;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.Header01Spec;

import nl.jqno.equalsverifier.EqualsVerifier;

public class TestArincHeader01 {
  @Test
  void testHeader01() {
    EqualsVerifier.forClass(Header01Spec.class);
  }
}
