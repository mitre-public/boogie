package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidConverter;
import org.mitre.tdp.boogie.arinc.v18.TestNdbNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestArincNdbNavaid {

  private static final NdbNavaidConverter converter = new NdbNavaidConverter();

  @Test
  void testEqualsHashCode() {
    EqualsVerifier.forClass(ArincNdbNavaid.class).verify();
  }

  @Test
  void testFieldAccess() {
    ArincNdbNavaid navaid = ArincVersion.V18.parser().apply(TestNdbNavaidSpec.navaid1).flatMap(converter).orElseThrow(AssertionError::new).toBuilder().build();

    assertAll(
        () -> Assertions.assertEquals(RecordType.S, navaid.recordType()),
        () -> Assertions.assertEquals(CustomerAreaCode.EEU, navaid.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> Assertions.assertEquals(SectionCode.P, navaid.sectionCode()),
        () -> assertEquals(Optional.of("N"), navaid.subSectionCode()),
        () -> assertEquals("UUOL", navaid.airportIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("UU", navaid.airportIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals("D", navaid.ndbIdentifier()),
        () -> assertEquals("UU", navaid.ndbIcaoRegion()),
        () -> assertEquals("1", navaid.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals(400.0d, navaid.ndbFrequency().orElseThrow(AssertionError::new)),
        () -> assertEquals("HMLW ", navaid.navaidClass().orElseThrow(AssertionError::new)),
        () -> assertEquals(52.72094444444445d, navaid.latitude()),
        () -> assertEquals(39.52708333333333d, navaid.longitude()),
        () -> assertEquals(10.0d, navaid.magneticVariation().orElseThrow(AssertionError::new)),
        () -> assertEquals("RPE", navaid.datumCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("LIPETSK LMM RW15", navaid.ndbNavaidName().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(35303), navaid.fileRecordNumber()),
        () -> assertEquals("2003", navaid.lastUpdateCycle())
    );
  }
}
