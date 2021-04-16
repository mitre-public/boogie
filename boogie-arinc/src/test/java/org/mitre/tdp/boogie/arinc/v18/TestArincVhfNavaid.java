package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mitre.tdp.boogie.arinc.v18.spec.TestVhfNavaidSpec.navaid2;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestArincVhfNavaid {

  @Test
  void testFieldAccess() {
    ArincVhfNavaid navaid = ArincVhfNavaid.wrap(ArincVersion.V18.parser().apply(navaid2).orElseThrow(AssertionError::new));

    assertAll(
        () -> assertEquals(RecordType.S, navaid.recordType().orElseThrow(AssertionError::new)),
        () -> assertEquals(CustomerAreaCode.USA, navaid.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.D, navaid.sectionCode().orElseThrow(AssertionError::new)),
        () -> assertFalse(navaid.subSectionCode().isPresent()),
        () -> assertFalse(navaid.airportIdentifier().isPresent()),
        () -> assertFalse(navaid.airportIcaoRegion().isPresent()),
        () -> assertEquals("ABQ", navaid.vhfIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("K2", navaid.icaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals("1", navaid.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals(1132.0d, navaid.vhfFrequency().orElseThrow(AssertionError::new)),
        () -> assertEquals("VTHW ", navaid.navaidClass().orElseThrow(AssertionError::new)),
        () -> assertEquals(35.043794444444444d, navaid.latitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(-106.8163111111111d, navaid.longitude().orElseThrow(AssertionError::new)),
        () -> assertEquals("ABQ", navaid.dmeIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals(35.043794444444444d, navaid.dmeLatitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(-106.8163111111111d, navaid.dmeLongitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(13.0d, navaid.stationDeclination().orElseThrow(AssertionError::new)),
        () -> assertEquals(5749.0d, navaid.dmeElevation().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(2), navaid.figureOfMerit().orElseThrow(AssertionError::new)),
        () -> assertFalse(navaid.ilsDmeBias().isPresent()),
        () -> assertEquals(423.0d, navaid.frequencyProtectionDistance().orElseThrow(AssertionError::new)),
        () -> assertEquals("NAR", navaid.datumCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(5758), navaid.fileRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("2003", navaid.cycle().orElseThrow(AssertionError::new))
    );
  }
}
