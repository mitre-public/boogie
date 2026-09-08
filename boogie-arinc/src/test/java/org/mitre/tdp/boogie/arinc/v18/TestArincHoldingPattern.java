package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.model.ArincHoldingPattern;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import nl.jqno.equalsverifier.EqualsVerifier;

public class TestArincHoldingPattern {
  @Test
  void equals() {
    EqualsVerifier.forClass(ArincHoldingPattern.class).verify();
  }

  @Test
  void comparesFixSubsectionCodesWithNullsLast() {
    ArincHoldingPattern enrouteWaypoint = holdingPattern("A");
    ArincHoldingPattern navaid = holdingPattern(null);

    assertTrue(enrouteWaypoint.compareTo(navaid) < 0);
    assertTrue(navaid.compareTo(enrouteWaypoint) > 0);
  }

  private static ArincHoldingPattern holdingPattern(String fixSubsectionCode) {
    return new ArincHoldingPattern.Builder()
        .recordType(RecordType.S)
        .customerAreaCode(CustomerAreaCode.USA)
        .regionCode("US")
        .fixIdentifier("FIX")
        .fixIcaoRegion("K1")
        .duplicateIdentifier("0")
        .fixSectionCode(SectionCode.E)
        .fixSubsectionCode(fixSubsectionCode)
        .build();
  }
}
