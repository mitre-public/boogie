package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.v18.ControlledAirspaceLegConverter;
import org.mitre.tdp.boogie.arinc.v18.ControlledAirspaceLegSpec;
import org.mitre.tdp.boogie.arinc.v18.ControlledAirspaceValidator;
import org.mitre.tdp.boogie.arinc.v18.field.AirspaceType;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryVia;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public class TestArincControlledAirspaceLeg {

  private static final ArincRecordParser PARSER = ArincRecordParser.standard(new ControlledAirspaceLegSpec());

  private static final Predicate<ArincRecord> VALIDATOR = new ControlledAirspaceValidator();

  private static final Function<ArincRecord, Optional<ArincControlledAirspaceLeg>> CONVERTER = new ControlledAirspaceLegConverter();

  private static final String CONTROLLED_AIRSPACE = "SSPAUCAGCAGGG UFA  A00010H    H S04500000E159000000                              FL245MFL660MHONIARA CTA                   794722210";

  @Test
  void testParseControlledAirspace() {
    ArincRecord record = PARSER.parse(CONTROLLED_AIRSPACE).orElseThrow(AssertionError::new);
    
    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.SPA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.U, record.requiredField("sectionCode")),
        () -> assertEquals("C", record.requiredField("subSectionCode")),
        () -> assertEquals("AG", record.requiredField("icaoRegion")),
        () -> assertEquals(AirspaceType.C, record.requiredField("airspaceType")),
        () -> assertEquals("AGGG", record.requiredField("airspaceCenter")),
        () -> assertEquals(SectionCode.U, record.requiredField("sectionCode2")),
        () -> assertEquals("F", record.requiredField("subSectionCode2")),
        () -> assertEquals("A", record.requiredField("airspaceClassification")),
        () -> assertEquals("A", record.requiredField("multipleCode")),
        () -> assertEquals(Integer.valueOf(1), record.requiredField("sequenceNumber")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(Level.H, record.requiredField("level")),
        () -> assertFalse(record.optionalField("timeCode").isPresent()),
        () -> assertFalse(record.optionalField("notam").isPresent()),
        () -> assertEquals(BoundaryVia.H, record.requiredField("boundaryVia")),
        () -> assertEquals(-4.833333333333333, record.requiredField("latitude")),
        () -> assertEquals(159.0, record.requiredField("longitude")),
        () -> assertFalse(record.optionalField("arcOriginLatitude").isPresent()),
        () -> assertFalse(record.optionalField("arcOriginLongitude").isPresent()),
        () -> assertFalse(record.optionalField("arcDistance").isPresent()),
        () -> assertFalse(record.optionalField("arcBearing").isPresent()),
        () -> assertFalse(record.optionalField("rnp").isPresent()),
        () -> assertEquals(24500.0, record.requiredField("lowerLimit")),
        () -> assertEquals("M", record.requiredField("lowerIndicator")),
        () -> assertEquals(66000.0, record.requiredField("upperLimit")),
        () -> assertEquals("M", record.requiredField("upperIndicator")),
        () -> assertEquals("HONIARA CTA", record.requiredField("controlledAirspaceName")),
        () -> assertEquals(Integer.valueOf(79472), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2210", record.requiredField("cycle"))
    );

    assertTrue(VALIDATOR.test(record));
    
    ArincControlledAirspaceLeg controlledAirspace = CONVERTER.apply(record).orElseThrow(AssertionError::new);
    
    assertAll(
        () -> assertEquals(RecordType.S, controlledAirspace.recordType()),
        () -> assertEquals(CustomerAreaCode.SPA, controlledAirspace.customerAreaCode()),
        () -> assertEquals(SectionCode.U, controlledAirspace.sectionCode()),
        () -> assertEquals("C", controlledAirspace.subSectionCode().get()),
        () -> assertEquals("AG", controlledAirspace.icaoRegion()),
        () -> assertEquals(AirspaceType.C, controlledAirspace.airspaceType()),
        () -> assertEquals("AGGG", controlledAirspace.airspaceCenter()),
        () -> assertEquals(SectionCode.U, controlledAirspace.supplierSectionCode().get()),
        () -> assertEquals("F", controlledAirspace.supplierSubSectionCode().get()),
        () -> assertEquals("A", controlledAirspace.airspaceClassification().get()),
        () -> assertEquals("A", controlledAirspace.multipleCode().get()),
        () -> assertEquals(1, controlledAirspace.sequenceNumber()),
        () -> assertEquals("0", controlledAirspace.continuationRecordNumber().get()),
        () -> assertEquals(Level.H, controlledAirspace.level().get()),
        () -> assertTrue(controlledAirspace.timeCode().isEmpty()),
        () -> assertTrue(controlledAirspace.notam().isEmpty()),
        () -> assertEquals(BoundaryVia.H, controlledAirspace.boundaryVia()),
        () -> assertEquals(-4.833333333333333, controlledAirspace.latitude().get()),
        () -> assertEquals(159.0, controlledAirspace.longitude().get()),
        () -> assertTrue(controlledAirspace.arcOriginLatitude().isEmpty()),
        () -> assertTrue(controlledAirspace.arcOriginLongitude().isEmpty()),
        () -> assertTrue(controlledAirspace.arcDistance().isEmpty()),
        () -> assertTrue(controlledAirspace.arcBearing().isEmpty()),
        () -> assertTrue(controlledAirspace.rnp().isEmpty()),
        () -> assertEquals(24500.0, controlledAirspace.lowerLimit().get()),
        () -> assertEquals("M", controlledAirspace.lowerUnitIndicator().get()),
        () -> assertEquals(66000.0, controlledAirspace.upperLimit().get()),
        () -> assertEquals("M", controlledAirspace.upperUnitIndicator().get()),
        () -> assertEquals("HONIARA CTA", controlledAirspace.controlledAirspaceName().get()),
        () -> assertEquals(79472, controlledAirspace.fileRecordNumber()),
        () -> assertEquals("2210", controlledAirspace.cycleDate())
    );
  }
}
