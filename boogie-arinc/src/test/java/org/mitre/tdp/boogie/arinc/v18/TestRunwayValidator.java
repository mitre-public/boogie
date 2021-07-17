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
import org.mitre.tdp.boogie.arinc.v18.RunwayValidator;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.base.Joiner;

class TestRunwayValidator {

  private static final RunwayValidator validator = new RunwayValidator();

  @Test
  void testIsCorrectSectionSubSection() {
    ArincRecord db = newArincRecord(SectionCode.P, "G", "HI");
    assertTrue(validator.isCorrectSectionSubSection(db), "Baseline mocked ArincRecord should match appropriate section/subSection.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutRecordType() {
    ArincRecord record = newArincRecord("airportIdentifier", "airportIcaoRegion", "runwayIdentifier", "latitude", "longitude", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should fail records missing RecordType.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutAirportIdentifier() {
    ArincRecord record = newArincRecord("recordType", "airportIcaoRegion", "runwayIdentifier", "latitude", "longitude", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should fail records missing AirportIdentifier.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutAirportIcaoRegion() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "runwayIdentifier", "latitude", "longitude", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should fail records missing AirportIcaoRegion.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutRunwayIdentifier() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "latitude", "longitude", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should fail records missing RunwayIdentifier.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutLatitude() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "runwayIdentifier", "longitude", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should fail records missing Latitude.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutLongitude() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "runwayIdentifier", "latitude", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should fail records missing Longitude.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutFileRecordNumber() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "runwayIdentifier", "latitude", "longitude", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should fail records missing FileRecordNumber.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutLastUpdateCycle() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "runwayIdentifier", "latitude", "longitude", "fileRecordNumber");
    assertFalse(validator.test(record), "Validator should fail records missing LastUpdateCycle.");
  }

  @Test
  void testValidatorAcceptsRecordWithAllRequiredFields() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "runwayIdentifier", "latitude", "longitude", "fileRecordNumber", "lastUpdateCycle");
    assertTrue(validator.test(record), "Validator should pass record with all required fields.");
  }

  private ArincRecord newArincRecord(String... fields) {
    return newArincRecord(SectionCode.P, "G", fields);
  }

  private ArincRecord newArincRecord(SectionCode sectionCode, String subSectionCode, String... fields) {
    ArincRecord record = mock(ArincRecord.class);
    when(record.optionalField(eq("sectionCode"))).thenReturn(Optional.of(sectionCode));
    when(record.optionalField(eq("subSectionCode"))).thenReturn(Optional.of(subSectionCode));
    when(record.containsParsedField(matches(Joiner.on("|").join(fields)))).thenReturn(true);
    return record;
  }
}
