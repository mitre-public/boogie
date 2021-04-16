package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.arinc.v18.spec.TestNdbNavaidSpec.navaid1;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestArincNdbNavaid {

  @Test
  void testFieldAccess() {
    ArincNdbNavaid navaid = ArincNdbNavaid.wrap(ArincVersion.V18.parser().apply(navaid1).orElseThrow(AssertionError::new));

    assertAll(
        () -> assertEquals(RecordType.S, navaid.recordType().orElseThrow(AssertionError::new)),
        () -> assertEquals(CustomerAreaCode.EEU, navaid.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.P, navaid.sectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("N", navaid.subSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("UUOL", navaid.airportIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("UU", navaid.airportIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals("D", navaid.ndbIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("UU", navaid.icaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals("1", navaid.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals(400.0d, navaid.ndbFrequency().orElseThrow(AssertionError::new)),
        () -> assertEquals("HMLW ", navaid.navaidClass().orElseThrow(AssertionError::new)),
        () -> assertEquals(52.72094444444445d, navaid.latitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(39.52708333333333d, navaid.longitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(10.0d, navaid.magneticVariation().orElseThrow(AssertionError::new)),
        () -> assertEquals("RPE", navaid.datumCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("LIPETSK LMM RW15", navaid.ndbNavaidName().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(35303), navaid.fileRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("2003", navaid.cycle().orElseThrow(AssertionError::new))
    );
  }
}
