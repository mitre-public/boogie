package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.base.Joiner;

public class TestGnssLandingSystemValidator {

  private static final GnssLandingSystemValidator VALIDATOR = new GnssLandingSystemValidator();

  @Test
  void testIsCorrectSectionSubSection() {
    ArincRecord record = newArincRecord("HI");
    assertTrue(VALIDATOR.isCorrectSectionSubSection(record), "If we can't get this right, we are doomed");
  }

  @Test
  void testLatLong() {
    ArincRecord record = newArincRecord("glsRefPathIdentifier", "latitude", "longitude", "magneticVariation", "airportIdentifier", "airportIcaoRegion", "fileRecordNumber", "lastUpdatedCycle", "glsChannel", "runwayIdentifier");
    assertTrue(VALIDATOR.test(record));
  }

  @Test
  void testMissingLocation() {
    ArincRecord record = newArincRecord("glsRefPathIdentifier", "magneticVariation", "airportIdentifier", "airportIcaoRegion", "fileRecordNumber", "lastUpdatedCycle", "glsChannel", "runwayIdentifier");
    assertFalse(VALIDATOR.test(record));
  }

  @Test
  void testMissingIdentStuff() {
    ArincRecord record = newArincRecord( "magneticVariation", "fileRecordNumber", "lastUpdatedCycle");
    assertFalse(VALIDATOR.test(record));
  }

  @Test
  void testMissingMisc() {
    ArincRecord record = newArincRecord("glsRefPathIdentifier", "magneticVariation", "airportIdentifier", "airportIcaoRegion", "glsChannel", "runwayIdentifier");
    assertFalse(VALIDATOR.test(record));
  }

  private ArincRecord newArincRecord(String... fields) {
    Optional presentOptional = mock(Optional.class);
    when(presentOptional.isPresent()).thenReturn(true);

    ArincRecord record = mock(ArincRecord.class);
    when(record.optionalField(eq("sectionCode"))).thenReturn(Optional.of(SectionCode.P));
    when(record.optionalField(eq("subSectionCode"))).thenReturn(Optional.of("T"));
    when(record.optionalField(matches(Joiner.on("|").join(fields)))).thenReturn(presentOptional);
    return record;
  }

}