package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.v18.AirportSpec;
import org.mitre.tdp.boogie.arinc.v21.HelipadSpec;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestArincHelipadTest {

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