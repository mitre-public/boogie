package org.mitre.tdp.boogie.v18;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mitre.tdp.boogie.v18.record.TestVhfNavaidSpec.navaid2;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

public class TestArincVhfNavaid {

  @Test
  public void testFieldAccess() {
    ArincVhfNavaid navaid = ArincVhfNavaid.wrap(ArincVersion.V18.parse(navaid2));

    assertEquals(RecordType.S, navaid.recordType());
    assertEquals(CustomerAreaCode.USA, navaid.customerAreaCode());
    assertEquals(SectionCode.D, navaid.sectionCode());
    assertFalse(navaid.subSectionCode().isPresent());
    assertFalse(navaid.airportIdentifier().isPresent());
    assertFalse(navaid.airportIcaoRegion().isPresent());
    assertEquals("ABQ", navaid.vhfIdentifier());
    assertEquals("K2", navaid.icaoRegion());
    assertEquals("1", navaid.continuationRecordNumber());
    assertEquals(1132.0d, navaid.vhfFrequency().get());
    assertEquals("VTHW ", navaid.navaidClass().get());
    assertEquals(35.043794444444444d, navaid.latitude());
    assertEquals(-106.8163111111111d, navaid.longitude());
    assertEquals("ABQ", navaid.dmeIdentifier().get());
    assertEquals(35.043794444444444d, navaid.dmeLatitude().get());
    assertEquals(-106.8163111111111d, navaid.dmeLongitude().get());
    assertEquals("E0130", navaid.stationDeclination().get());
    assertEquals(5749.0d, navaid.dmeElevation().get());
    assertEquals(Integer.valueOf(2), navaid.figureOfMerit().get());
    assertFalse(navaid.ilsDmeBias().isPresent());
    assertEquals(423.0d, navaid.frequencyProtectionDistance().get());
    assertEquals("NAR", navaid.datumCode().get());
    assertEquals(Integer.valueOf(5758), navaid.fileRecordNumber());
    assertEquals("2003", navaid.cycle());
  }
}
