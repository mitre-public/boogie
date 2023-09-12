package org.mitre.tdp.boogie.arinc.v18;

import com.google.common.base.Joiner;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAirportGateValidator {
  private static final AirportGateValidator VALIDATOR = new AirportGateValidator();

  @Test
  void testIsCorrectSectionSubSection() {
    ArincRecord record = newArincRecord("TEST");
    assertTrue(VALIDATOR.isCorrectSectionSubSection(record), "If we can't get this right, we are doomed");
  }

  @Test
  void testLatLong() {
    ArincRecord record = newArincRecord("gateIdentifier", "latitude", "longitude", "airportIdentifier", "airportIcaoRegion", "fileRecordNumber", "lastUpdatedCycle");
    assertTrue(VALIDATOR.test(record));
  }

  @Test
  void testMissingLatLong() {
    ArincRecord record = newArincRecord("gateIdentifier", "airportIdentifier", "airportIcaoRegion", "fileRecordNumber", "lastUpdatedCycle");
    assertFalse(VALIDATOR.test(record));
  }

  @Test
  void testMissingGateIdentifier() {
    ArincRecord record = newArincRecord("latitude", "longitude", "airportIdentifier", "airportIcaoRegion", "fileRecordNumber", "lastUpdatedCycle");
    assertFalse(VALIDATOR.test(record));
  }

  private ArincRecord newArincRecord(String... fields) {
    Optional presentOptional = mock(Optional.class);
    when(presentOptional.isPresent()).thenReturn(true);

    ArincRecord record = mock(ArincRecord.class);
    when(record.optionalField(eq("sectionCode"))).thenReturn(Optional.of(SectionCode.P));
    when(record.optionalField(eq("subSectionCode"))).thenReturn(Optional.of("B"));
    when(record.optionalField(matches(Joiner.on("|").join(fields)))).thenReturn(presentOptional);
    return record;
  }
}