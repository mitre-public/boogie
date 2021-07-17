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
import org.mitre.tdp.boogie.arinc.v18.AirwayLegValidator;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.base.Joiner;

class TestAirwayLegValidator {

  private static final AirwayLegValidator validator = new AirwayLegValidator();

  @Test
  void testIsCorrectSectionSubSection() {
    ArincRecord record = newArincRecord("HI");
    assertTrue(validator.isCorrectSectionSubSection(record), "Baseline mocked ArincRecord should match appropriate section/subSection.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutRecordType() {
    ArincRecord record = newArincRecord("customerAreaCode", "routeIdentifier", "sequenceNumber", "fixIdentifier", "fixIcaoRegion", "fixSectionCode", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should not pass record missing recordType information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutCustomerAreaCode() {
    ArincRecord record = newArincRecord("recordType", "routeIdentifier", "sequenceNumber", "fixIdentifier", "fixIcaoRegion", "fixSectionCode", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should not pass record missing customerAreaCode information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutRouteIdentifier() {
    ArincRecord record = newArincRecord("recordType", "customerAreaCode", "sequenceNumber", "fixIdentifier", "fixIcaoRegion", "fixSectionCode", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should not pass record missing routeIdentifier information.");
  }


  @Test
  void testValidatorRejectsRecordsWithoutSequenceNumber() {
    ArincRecord record = newArincRecord("recordType", "customerAreaCode", "routeIdentifier", "fixIdentifier", "fixIcaoRegion", "fixSectionCode", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should not pass record missing sequenceNumber information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutFixIdentifier() {
    ArincRecord record = newArincRecord("recordType", "customerAreaCode", "routeIdentifier", "sequenceNumber", "fixIcaoRegion", "fixSectionCode", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should not pass record missing fixIdentifier information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutFixIcaoRegion() {
    ArincRecord record = newArincRecord("recordType", "customerAreaCode", "routeIdentifier", "sequenceNumber", "fixIdentifier", "fixSectionCode", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should not pass record missing fixIcaoRegion information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutFixSectionCode() {
    ArincRecord record = newArincRecord("recordType", "customerAreaCode", "routeIdentifier", "sequenceNumber", "fixIdentifier", "fixIcaoRegion", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should not pass record missing fixSectionCode information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutFileRecordNumber() {
    ArincRecord record = newArincRecord("recordType", "customerAreaCode", "routeIdentifier", "sequenceNumber", "fixIdentifier", "fixIcaoRegion", "fixSectionCode", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should not pass record missing fileRecordNumber information.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutLastUpdateCycle() {
    ArincRecord record = newArincRecord("recordType", "customerAreaCode", "routeIdentifier", "sequenceNumber", "fixIdentifier", "fixIcaoRegion", "fixSectionCode", "fileRecordNumber");
    assertFalse(validator.test(record), "Validator should not pass record missing lastUpdateCycle information.");
  }

  @Test
  void testValidatorAcceptsRecordWithAllRequiredFields() {
    ArincRecord record = newArincRecord("recordType", "customerAreaCode", "routeIdentifier", "sequenceNumber", "fixIdentifier", "fixIcaoRegion", "fixSectionCode", "fileRecordNumber", "lastUpdateCycle");
    assertTrue(validator.test(record), "Validator should pass record with all required fields.");
  }

  private ArincRecord newArincRecord(String... fields) {
    ArincRecord record = mock(ArincRecord.class);
    when(record.optionalField(eq("sectionCode"))).thenReturn(Optional.of(SectionCode.E));
    when(record.optionalField(eq("subSectionCode"))).thenReturn(Optional.of("R"));
    when(record.containsParsedField(matches(Joiner.on("|").join(fields)))).thenReturn(true);
    return record;
  }
}
