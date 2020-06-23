package org.mitre.tdp.boogie.v18;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.v18.record.TestNdbNavaidSpec.navaid1;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

public class TestArincNdbNavaid {

  @Test
  public void testFieldAccess() {
    ArincNdbNavaid navaid = ArincNdbNavaid.wrap(ArincVersion.V18.parse(navaid1));

    assertEquals(RecordType.S, navaid.recordType().get());
    assertEquals(CustomerAreaCode.EEU, navaid.customerAreaCode().get());
    assertEquals(SectionCode.P, navaid.sectionCode().get());
    assertEquals("N", navaid.subSectionCode().get());
    assertEquals("UUOL", navaid.airportIdentifier().get());
    assertEquals("UU", navaid.airportIcaoRegion().get());
    assertEquals("D", navaid.ndbIdentifier().get());
    assertEquals("UU", navaid.icaoRegion().get());
    assertEquals("1", navaid.continuationRecordNumber().get());
    assertEquals(400.0d, navaid.ndbFrequency().get());
    assertEquals("HMLW ", navaid.navaidClass().get());
    assertEquals(52.72094444444445d, navaid.latitude().get());
    assertEquals(39.52708333333333d, navaid.longitude().get());
    assertEquals(10.0d, navaid.magneticVariation().get());
    assertEquals("RPE", navaid.datumCode().get());
    assertEquals("LIPETSK LMM RW15", navaid.ndbNavaidName().get());
    assertEquals(Integer.valueOf(35303), navaid.fileRecordNumber().get());
    assertEquals("2003", navaid.cycle().get());
  }
}
