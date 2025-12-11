package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import nl.jqno.equalsverifier.EqualsVerifier;

public class TestArincHeliport {
  @Test
  void testEquals() {
    EqualsVerifier.forClass(ArincHeliport.class);
  }

  @Test
  void builder() {
    ArincHeliport port = ArincHeliport.builder()
        .recordType(RecordType.T)
        .customerAreaCode(CustomerAreaCode.USA)
        .sectionCode(SectionCode.H)
        .heliportIdentifier("DMB")
        .heliportIcaoRegion("H7")
        .build();
    assertNotNull(port);
    ArincHeliport.Builder builder = port.toBuilder();
    assertNotNull(builder);
    assertDoesNotThrow(builder::build);
  }
}
