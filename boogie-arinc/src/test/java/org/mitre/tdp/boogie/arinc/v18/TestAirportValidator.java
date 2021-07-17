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
import org.mitre.tdp.boogie.arinc.v18.AirportValidator;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.base.Joiner;

class TestAirportValidator {

  private static final AirportValidator validator = new AirportValidator();

  @Test
  void testIsCorrectSectionSubSection() {
    ArincRecord record = newArincRecord("HI");
    assertTrue(validator.isCorrectSectionSubSection(record), "Baseline mocked ArincRecord should match appropriate section/subSection.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutLatitude() {
    ArincRecord record = newArincRecord("longitude", "airportIdentifier", "airportIcaoRegion", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should not pass record missing latitude information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutLongitude() {
    ArincRecord record = newArincRecord("latitude", "airportIdentifier", "airportIcaoRegion", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should not pass record missing longitude information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutAirportIdentifier() {
    ArincRecord record = newArincRecord("latitude", "longitude", "airportIcaoRegion", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should not pass record missing airportIdentifier information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutAirportIcaoRegion() {
    ArincRecord record = newArincRecord("latitude", "longitude", "airportIdentifier", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should not pass record missing airportIcaoRegion information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutFileRecordNumber() {
    ArincRecord record = newArincRecord("latitude", "longitude", "airportIdentifier", "airportIcaoRegion", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should not pass record missing flightRecordNumber information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutLastUpdateCycle() {
    ArincRecord record = newArincRecord("latitude", "longitude", "airportIdentifier", "airportIcaoRegion", "fileRecordNumber");
    assertFalse(validator.test(record), "Validator should not pass record missing lastUpdateCycle information.");
  }

  @Test
  void testValidatorAcceptsRecordWithAllRequiredFields() {
    ArincRecord record = newArincRecord("latitude", "longitude", "airportIdentifier", "airportIcaoRegion", "fileRecordNumber", "lastUpdateCycle");
    assertTrue(validator.test(record), "Validator should pass record containing all required information.");
  }

  private ArincRecord newArincRecord(String... fields) {
    ArincRecord record = mock(ArincRecord.class);
    when(record.optionalField(eq("sectionCode"))).thenReturn(Optional.of(SectionCode.P));
    when(record.optionalField(eq("subSectionCode"))).thenReturn(Optional.of("A"));
    when(record.containsParsedField(matches(Joiner.on("|").join(fields)))).thenReturn(true);
    return record;
  }
}
