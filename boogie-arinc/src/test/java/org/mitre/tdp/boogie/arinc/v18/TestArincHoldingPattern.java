package org.mitre.tdp.boogie.arinc.v18;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.model.ArincHoldingPattern;

import nl.jqno.equalsverifier.EqualsVerifier;

public class TestArincHoldingPattern {
  @Test
  void equals() {
    EqualsVerifier.forClass(ArincHoldingPattern.class).verify();
  }
}
