package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import nl.jqno.equalsverifier.EqualsVerifier;

public class TestArincHeliport {
  @Test
  void model() {
    EqualsVerifier.forClass(ArincHeliport.class);
  }

  @Test
  void builder() {
    ArincHeliport heliport = ArincHeliport.builder()
        .recordType(RecordType.S)
        .customerAreaCode(CustomerAreaCode.AFR)
        .sectionCode(SectionCode.H)
        .heliportIdentifier("KDDD")
        .heliportIcaoRegion("K1")
        .build();
    ArincHeliport.Builder builder = heliport.toBuilder();
    ArincHeliport again = builder.build();
    assertAll(
        () -> assertNotNull(heliport),
        () -> assertNotNull(builder),
        () -> assertEquals(heliport, again)
    );
  }
}
