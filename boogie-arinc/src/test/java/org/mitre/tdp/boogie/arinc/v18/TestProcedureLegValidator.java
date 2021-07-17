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
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.base.Joiner;

class TestProcedureLegValidator {

  private static final ProcedureLegValidator validator = new ProcedureLegValidator();

  @Test
  void testIsCorrectSectionSubSection() {
    ArincRecord pd = newArincRecord(SectionCode.P, "D", "HI");
    ArincRecord pe = newArincRecord(SectionCode.P, "E", "HI");
    ArincRecord pf = newArincRecord(SectionCode.P, "F", "HI");

    assertAll(
        () -> assertTrue(validator.isCorrectSectionSubSection(pd), "Baseline mocked ArincRecord should match appropriate section/subSection."),
        () -> assertTrue(validator.isCorrectSectionSubSection(pe), "Baseline mocked ArincRecord should match appropriate section/subSection."),
        () -> assertTrue(validator.isCorrectSectionSubSection(pf), "Baseline mocked ArincRecord should match appropriate section/subSection.")
    );
  }

  @Test
  void testValidatorRejectsRecordsWithoutRecordType() {
    ArincRecord record = newArincRecord("airportIdentifier", "airportIcaoRegion", "sidStarIdentifier", "routeType", "sequenceNumber", "fileRecordNumber", "lastUpdateCycle", "pathTerm");
    assertFalse(validator.test(record), "Validator should fail records missing RecordType.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutAirportIdentifier() {
    ArincRecord record = newArincRecord("recordType", "airportIcaoRegion", "sidStarIdentifier", "routeType", "sequenceNumber", "fileRecordNumber", "lastUpdateCycle", "pathTerm");
    assertFalse(validator.test(record), "Validator should fail records missing AirportIdentifier.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutAirportIcaoRegion() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "sidStarIdentifier", "routeType", "sequenceNumber", "fileRecordNumber", "lastUpdateCycle", "pathTerm");
    assertFalse(validator.test(record), "Validator should fail records missing AirportIcaoRegion.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutSidStarIdentifier() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "routeType", "sequenceNumber", "fileRecordNumber", "lastUpdateCycle", "pathTerm");
    assertFalse(validator.test(record), "Validator should fail records missing SidStarIdentifier.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutRouteType() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "sidStarIdentifier", "sequenceNumber", "fileRecordNumber", "lastUpdateCycle", "pathTerm");
    assertFalse(validator.test(record), "Validator should fail records missing RouteType.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutSequenceNumber() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "sidStarIdentifier", "routeType", "fileRecordNumber", "lastUpdateCycle", "pathTerm");
    assertFalse(validator.test(record), "Validator should fail records missing SequenceNumber.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutFileRecordNumber() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "sidStarIdentifier", "routeType", "sequenceNumber", "lastUpdateCycle", "pathTerm");
    assertFalse(validator.test(record), "Validator should fail records missing FileRecordNumber.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutLastUpdateCycle() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "sidStarIdentifier", "routeType", "sequenceNumber", "fileRecordNumber", "pathTerm");
    assertFalse(validator.test(record), "Validator should fail records missing LastUpdateCycle.");
  }

  @Test
  void testValidatorRejectsRecordsWithoutPathTerm() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "sidStarIdentifier", "routeType", "sequenceNumber", "fileRecordNumber", "lastUpdateCycle");
    assertFalse(validator.test(record), "Validator should fail records missing PathTerm.");
  }

  @Test
  void testValidatorAcceptsRecordWithAllRequiredFields() {
    ArincRecord record = newArincRecord("recordType", "airportIdentifier", "airportIcaoRegion", "sidStarIdentifier", "routeType", "sequenceNumber", "fileRecordNumber", "lastUpdateCycle", "pathTerm");
    assertTrue(validator.test(record), "Validator should pass record with all required fields.");
  }

  private ArincRecord newArincRecord(String... fields) {
    return newArincRecord(SectionCode.P, "F", fields);
  }

  private ArincRecord newArincRecord(SectionCode sectionCode, String subSectionCode, String... fields) {
    ArincRecord record = mock(ArincRecord.class);
    when(record.optionalField(eq("sectionCode"))).thenReturn(Optional.of(sectionCode));
    when(record.optionalField(eq("subSectionCode"))).thenReturn(Optional.of(subSectionCode));
    when(record.containsParsedField(matches(Joiner.on("|").join(fields)))).thenReturn(true);

    // stuff to get us past the leg validators
    when(record.requiredField(eq("pathTerm"))).thenReturn(PathTerm.VI);
    when(record.containsParsedField(eq("outboundMagneticCourse"))).thenReturn(true);
    return record;
  }
}
