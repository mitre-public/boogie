package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegConverter;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegSpec;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegValidator;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryVia;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public class TestArincFirUirLeg {

  private static final ArincRecordParser PARSER = ArincRecordParser.standard(new FirUirLegSpec());

  private static final Predicate<ArincRecord> VALIDATOR = new FirUirLegValidator();

  private static final Function<ArincRecord, Optional<ArincFirUirLeg>> CONVERTER = new FirUirLegConverter();

  private static final String ARINC_FIR_UIR = "SAFRUFDRRRZRZXB10950DNKK    12N G N13342821E006270449                           FL245FL245UNLTDRR NIAMEY FIR/UIR           923192313";

  private static final String PROBLEM = "SSPAUFWIIFZRZXF00940WBFC        G N01312508E109030528                                                                      563692403";
  @Test
  void testParseFirUirLeg() {
    ArincRecord record = PARSER.parse(ARINC_FIR_UIR).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.AFR, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.U, record.requiredField("sectionCode")),
        () -> assertEquals(Optional.of("F"), record.optionalField("subSectionCode")),
        () -> assertEquals("DRRR", record.requiredField("firUirIdentifier")),
        () -> assertEquals(Optional.of("ZRZX"), record.optionalField("firUirAddress")),
        () -> assertEquals(FirUirIndicator.B, record.requiredField("firUirIndicator")),
        () -> assertEquals(Integer.valueOf(1095), record.requiredField("sequenceNumber")),
        () -> assertEquals(Optional.of("0"), record.optionalField("continuationRecordNumber")),
        () -> assertEquals(Optional.of("DNKK"), record.optionalField("adjacentFirIdentifier")),
        () -> assertTrue(record.optionalField("adjacentUirIdentifier").isEmpty()),
        () -> assertEquals(Optional.of("1"), record.optionalField("reportingUnitsSpeed")),
        () -> assertEquals(Optional.of("2"), record.optionalField("reportingUnitsAltitude")),
        () -> assertEquals(Boolean.FALSE, record.requiredField("firUirEntryReport")),
        () -> assertEquals(BoundaryVia.G, record.requiredField("boundaryVia")),
        () -> assertEquals(Double.valueOf(13.574502777777777), record.optionalField("firUirLatitude").get()),
        () -> assertEquals(Double.valueOf(6.451247222222222), record.optionalField("firUirLongitude").get()),
        () -> assertTrue(record.optionalField("arcOriginLatitude").isEmpty()),
        () -> assertTrue(record.optionalField("arcOriginLongitude").isEmpty()),
        () -> assertTrue(record.optionalField("arcDistance").isEmpty()),
        () -> assertTrue(record.optionalField("arcBearing").isEmpty()),
        () -> assertEquals(Optional.of(24500.0), record.optionalField("firUpperLimit")),
        () -> assertEquals(Optional.of(24500.0), record.optionalField("uirLowerLimit")),
        () -> assertTrue(record.optionalField("uirUpperLimit").isEmpty()),
        () -> assertEquals(Optional.of("RR"), record.optionalField("cruiseTableIndicator")),
        () -> assertEquals(Optional.of("NIAMEY FIR/UIR"), record.optionalField("firUirName")),
        () -> assertEquals(Integer.valueOf(92319), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2313", record.requiredField("cycle")));

    assertTrue(VALIDATOR.test(record));
  }

  @Test
  void testProblem() {
    ArincRecord record = PARSER.parse(PROBLEM).orElseThrow(AssertionError::new);

    assertTrue(VALIDATOR.test(record));
    ArincFirUirLeg firUirLeg = CONVERTER.apply(record).orElseThrow(AssertionError::new);

    firUirLeg.toString();
  }

  @Test
  void testConvertFirUirLeg() {
    ArincRecord record = PARSER.parse(ARINC_FIR_UIR).orElseThrow(AssertionError::new);

    assertTrue(VALIDATOR.test(record));
    ArincFirUirLeg firUirLeg = CONVERTER.apply(record).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, firUirLeg.recordType()),
        () -> assertEquals(CustomerAreaCode.AFR, firUirLeg.customerAreaCode()),
        () -> assertEquals(SectionCode.U, firUirLeg.sectionCode()),
        () -> assertEquals(Optional.of("F"), firUirLeg.subSectionCode()),
        () -> assertEquals("DRRR", firUirLeg.firUirIdentifier()),
        () -> assertEquals(Optional.of("ZRZX"), firUirLeg.firUirAddress()),
        () -> assertEquals(FirUirIndicator.B, firUirLeg.firUirIndicator()),
        () -> assertEquals(1095, firUirLeg.sequenceNumber()),
        () -> assertEquals(Optional.of("0"), firUirLeg.continuationRecordNumber()),
        () -> assertEquals(Optional.of("DNKK"), firUirLeg.adjacentFirIdentifier()),
        () -> assertTrue(firUirLeg.adjacentUirIdentifier().isEmpty()),
        () -> assertEquals(Optional.of("1"), firUirLeg.reportingUnitsSpeed()),
        () -> assertEquals(Optional.of("2"), firUirLeg.reportingUnitsAltitude()),
        () -> assertEquals(Boolean.FALSE, firUirLeg.entryReport().get()),  // Assuming `firUirEntryReport` maps to a Boolean
        () -> assertEquals(BoundaryVia.G, firUirLeg.boundaryVia()),
        () -> assertEquals(13.574502777777777, firUirLeg.firUirLatitude().get()),
        () -> assertEquals(6.451247222222222, firUirLeg.firUirLongitude().get()),
        () -> assertTrue(firUirLeg.arcOriginLatitude().isEmpty()),
        () -> assertTrue(firUirLeg.arcOriginLongitude().isEmpty()),
        () -> assertTrue(firUirLeg.arcDistance().isEmpty()),
        () -> assertTrue(firUirLeg.arcBearing().isEmpty()),
        () -> assertEquals(Optional.of(24500.0), firUirLeg.firUpperLimit()),
        () -> assertEquals(Optional.of(24500.0), firUirLeg.uirLowerLimit()),
        () -> assertTrue(firUirLeg.uirUpperLimit().isEmpty()),  // Adjusted based on the absence of this field
        () -> assertEquals(Optional.of("RR"), firUirLeg.cruiseTableIndicator()),
        () -> assertEquals(Optional.of("NIAMEY FIR/UIR"), firUirLeg.firUirName()),
        () -> assertEquals(92319, firUirLeg.fileRecordNumber()),
        () -> assertEquals("2313", firUirLeg.cycleDate())
    );

  }

  static String ARC = "SPACUFPGZUZRZXF00010        13N CE                   N13320480E14454306025000672UNLTD          RR GUAM CERAP               405972003";

  @Test
  void testArc() {
    ArincRecord record = PARSER.parse(ARC).orElseThrow(AssertionError::new);
    assertTrue(VALIDATOR.test(record));
    ArincFirUirLeg firUirLeg = CONVERTER.apply(record).orElseThrow(AssertionError::new);
    ArincFirUirLeg rebuilt = firUirLeg.toBuilder().build();
    assertEquals("CE", firUirLeg.boundaryVia().name());
    assertEquals(firUirLeg, rebuilt);
  }
}
