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

    assertEquals(RecordType.S, navaid.recordType());
    assertEquals(CustomerAreaCode.EEU, navaid.customerAreaCode());
    assertEquals(SectionCode.P, navaid.sectionCode());
    assertEquals("N", navaid.subSectionCode());
    assertEquals("UUOL", navaid.airportIdentifier().get());
    assertEquals("UU", navaid.airportIcaoRegion().get());
    assertEquals("D", navaid.ndbIdentifier());
    assertEquals("UU", navaid.icaoRegion());
    assertEquals("1", navaid.continuationRecordNumber());
    assertEquals(400.0d, navaid.ndbFrequency().get());
    assertEquals("HMLW ", navaid.navaidClass().get());
    assertEquals(52.72094444444445d, navaid.latitude());
    assertEquals(39.52708333333333d, navaid.longitude());
    assertEquals(10.0d, navaid.magneticVariation());
    assertEquals("RPE", navaid.datumCode().get());
    assertEquals("LIPETSK LMM RW15", navaid.ndbNavaidName().get());
    assertEquals(Integer.valueOf(35303), navaid.fileRecordNumber());
    assertEquals("2003", navaid.cycle());
  }
}
