package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeValidator;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.base.Joiner;

class TestLocalizerGlideSlopeValidator {

  private static final LocalizerGlideSlopeValidator validator = new LocalizerGlideSlopeValidator();

  @Test
  void testIsCorrectSectionSubSection() {
    ArincRecord record = newArincRecord("HI");
    assertTrue(validator.isCorrectSectionSubSection(record), "Baseline mocked ArincRecord should match appropriate section/subSection.");
  }

  @Test
  void testContainsCompleteSupportingFacilityInformation() {
    ArincRecord recordMissingFacilityIdentifier = newArincRecord("supportingFacilityIcaoRegion", "supportingFacilitySectionCode");
    ArincRecord recordMissingFacilityIcaoRegion = newArincRecord("supportingFacilityIdentifier", "supportingFacilitySectionCode");
    ArincRecord recordMissingFacilitySectionCode = newArincRecord("supportingFacilityIdentifier", "supportingFacilityIcaoRegion");
    ArincRecord recordWithFacilityId = newArincRecord("supportingFacilityIdentifier", "supportingFacilityIcaoRegion", "supportingFacilitySectionCode");

    assertAll(
        () -> assertFalse(validator.containsCompleteSupportingFacilityInformation(recordMissingFacilityIdentifier)),
        () -> assertFalse(validator.containsCompleteSupportingFacilityInformation(recordMissingFacilityIcaoRegion)),
        () -> assertFalse(validator.containsCompleteSupportingFacilityInformation(recordMissingFacilitySectionCode)),
        () -> assertTrue(validator.containsCompleteSupportingFacilityInformation(recordWithFacilityId))
    );
  }

  @Test
  void testValidatorRejectsRecordsWithoutRecordType() {
    ArincRecord record = newArincRecord("airportIdentifier", "airportIcaoRegion", "localizerIdentifier", "runwayIdentifier", "fileRecordNumber", "lastUpdateCycle", "supportingFacilityIdentifier", "supportingFacilityIcaoRegion", "supportingFacilitySectionCode");
    assertFalse(validator.test(record), "Validator should fail records missing RecordType.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutAirportIdentifier() {
    ArincRecord record = newArincRecord("recordType", "airportIcaoRegion", "localizerIdentifier", "runwayIdentifier", "fileRecordNumber", "lastUpdateCycle", "supportingFacilityIdentifier", "supportingFacilityIcaoRegion", "supportingFacilitySectionCode");
    assertFalse(validator.test(record), "Validator should fail records missing AirportIdentifier.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutAirportIcaoRegion() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "localizerIdentifier", "runwayIdentifier", "fileRecordNumber", "lastUpdateCycle", "supportingFacilityIdentifier", "supportingFacilityIcaoRegion", "supportingFacilitySectionCode");
    assertFalse(validator.test(record), "Validator should fail records missing AirportIcaoRegion.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutLocalizerIdentifier() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "runwayIdentifier", "fileRecordNumber", "lastUpdateCycle", "supportingFacilityIdentifier", "supportingFacilityIcaoRegion", "supportingFacilitySectionCode");
    assertFalse(validator.test(record), "Validator should fail records missing LocalizerIdentifier.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutRunwayIdentifier() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "localizerIdentifier", "fileRecordNumber", "lastUpdateCycle", "supportingFacilityIdentifier", "supportingFacilityIcaoRegion", "supportingFacilitySectionCode");
    assertFalse(validator.test(record), "Validator should fail records missing RunwayIdentifier.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutFileRecordNumber() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "localizerIdentifier", "runwayIdentifier", "lastUpdateCycle", "supportingFacilityIdentifier", "supportingFacilityIcaoRegion", "supportingFacilitySectionCode");
    assertFalse(validator.test(record), "Validator should fail records missing FileRecordNumber.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutLastUpdateCycle() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "localizerIdentifier", "runwayIdentifier", "fileRecordNumber", "supportingFacilityIdentifier", "supportingFacilityIcaoRegion", "supportingFacilitySectionCode");
    assertFalse(validator.test(record), "Validator should fail records missing LastUpdateCycle.");
  }

  @Test
  void testValidatorAcceptsRecordWithAllRequiredFields() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "localizerIdentifier", "runwayIdentifier", "fileRecordNumber", "lastUpdateCycle", "supportingFacilityIdentifier", "supportingFacilityIcaoRegion", "supportingFacilitySectionCode");
    assertTrue(validator.test(record), "Validator should pass record with all required fields.");
  }

  private ArincRecord newArincRecord(String... fields) {
    ArincRecord record = mock(ArincRecord.class);
    when(record.optionalField(eq("sectionCode"))).thenReturn(Optional.of(SectionCode.P));
    when(record.optionalField(eq("subSectionCode"))).thenReturn(Optional.of("I"));
    when(record.containsParsedField(matches(Joiner.on("|").join(fields)))).thenReturn(true);
    return record;
  }
}
