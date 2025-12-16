package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestArincHelipad {

  @Test
  void equals() {
    EqualsVerifier.forClass(ArincHelipad.class);
  }

  @Test
  void builder() {
    ArincHelipad pad = ArincHelipad.builder().build();
    assertNotNull(pad);
    ArincHelipad.Builder bldr = pad.toBuilder();
    assertNotNull(bldr);
  }
}