package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.TestVhfNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidConverter;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestArincVhfNavaid {

  private static final VhfNavaidConverter converter = new VhfNavaidConverter();

  @Test
  void testEqualsHashCode() {
    EqualsVerifier.forClass(ArincVhfNavaid.class).verify();
  }

  @Test
  void testFieldAccess() {
    ArincVhfNavaid navaid = ArincVersion.V18.parser().apply(TestVhfNavaidSpec.navaid2).flatMap(converter).orElseThrow(AssertionError::new);

    assertAll(
        () -> Assertions.assertEquals(RecordType.S, navaid.recordType()),
        () -> Assertions.assertEquals(CustomerAreaCode.USA, navaid.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> Assertions.assertEquals(SectionCode.D, navaid.sectionCode()),
        () -> assertFalse(navaid.subSectionCode().isPresent()),
        () -> assertFalse(navaid.airportIdentifier().isPresent()),
        () -> assertFalse(navaid.airportIcaoRegion().isPresent()),
        () -> assertEquals("ABQ", navaid.vhfIdentifier()),
        () -> assertEquals("K2", navaid.vhfIcaoRegion()),
        () -> assertEquals("1", navaid.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals(1132.0d, navaid.vhfFrequency().orElseThrow(AssertionError::new)),
        () -> assertEquals("VTHW ", navaid.navaidClass().orElseThrow(AssertionError::new)),
        () -> assertEquals(35.043794444444444d, navaid.latitude()),
        () -> assertEquals(-106.8163111111111d, navaid.longitude()),
        () -> assertEquals("ABQ", navaid.dmeIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals(35.043794444444444d, navaid.dmeLatitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(-106.8163111111111d, navaid.dmeLongitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(13.0d, navaid.stationDeclination().orElseThrow(AssertionError::new)),
        () -> assertEquals(5749.0d, navaid.dmeElevation().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(2), navaid.figureOfMerit().orElseThrow(AssertionError::new)),
        () -> assertFalse(navaid.ilsDmeBias().isPresent()),
        () -> assertEquals(423.0d, navaid.frequencyProtectionDistance().orElseThrow(AssertionError::new)),
        () -> assertEquals("NAR", navaid.datumCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(5758), navaid.fileRecordNumber()),
        () -> assertEquals("2003", navaid.lastUpdateCycle())
    );
  }
}
