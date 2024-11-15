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

public class TestAirportPrimaryExtensionValidator {
  private static final AirportPrimaryExtensionValidator validator = new AirportPrimaryExtensionValidator();

  @Test
  void testIsCorrectSectionSubSection() {
    ArincRecord record = newArincRecord("HI");
    assertTrue(validator.isCorrectSectionSubSection(record), "Baseline mocked ArincRecord should match appropriate section/subSection.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutAirportIdentifier() {
    ArincRecord record = newArincRecord("latitude", "longitude", "icaoRegion", "fileRecordNumber", "cycle");
    assertFalse(validator.test(record), "Validator should not pass record missing airportIdentifier information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutAirportIcaoRegion() {
    ArincRecord record = newArincRecord("latitude", "longitude", "airportIdentifier", "fileRecordNumber", "cycle");
    assertFalse(validator.test(record), "Validator should not pass record missing airportIcaoRegion information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutFileRecordNumber() {
    ArincRecord record = newArincRecord("latitude", "longitude", "airportIdentifier", "icaoRegion", "cycle");
    assertFalse(validator.test(record), "Validator should not pass record missing flightRecordNumber information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutLastUpdateCycle() {
    ArincRecord record = newArincRecord("latitude", "longitude", "airportIdentifier", "icaoRegion", "fileRecordNumber");
    assertFalse(validator.test(record), "Validator should not pass record missing lastUpdateCycle information.");
  }

  @Test
  void testValidatorAcceptsRecordWithAllRequiredFields() {
    ArincRecord record = newArincRecord("airportIdentifier", "icaoRegion", "fileRecordNumber", "cycle");
    assertTrue(validator.test(record), "Validator should pass record containing all required information.");
  }

  private ArincRecord newArincRecord(String... fields) {
    Optional presentOptional = mock(Optional.class);
    when(presentOptional.isPresent()).thenReturn(true);

    ArincRecord record = mock(ArincRecord.class);
    when(record.optionalField(eq("sectionCode"))).thenReturn(Optional.of(SectionCode.P));
    when(record.optionalField(eq("subSectionCode"))).thenReturn(Optional.of("A"));
    when(record.optionalField(matches(Joiner.on("|").join(fields)))).thenReturn(presentOptional);
    return record;
  }
}
