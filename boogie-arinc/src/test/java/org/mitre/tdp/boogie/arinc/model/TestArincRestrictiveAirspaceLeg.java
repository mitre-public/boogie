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
import org.mitre.tdp.boogie.arinc.v18.RestrictiveAirspaceValidator;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryVia;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v19.RestrictiveAirspaceLegConverter;
import org.mitre.tdp.boogie.arinc.v19.RestrictiveAirspaceLegSpec;

public class TestArincRestrictiveAirspaceLeg {

  private static final ArincRecordParser PARSER = ArincRecordParser.standard(new RestrictiveAirspaceLegSpec());

  private static final Predicate<ArincRecord> VALIDATOR = new RestrictiveAirspaceValidator();

  private static final Function<ArincRecord, Optional<ArincRestrictiveAirspaceLeg>> CONVERTER = new RestrictiveAirspaceLegConverter();

  private static final String RESTRICTIVE_AIRSPACE = "SUSAURK1NBOARDMAN  A00101L    G N45525900W119310400                              04000M17999MBOARDMAN MOA                  714171703";

  private static final String RESTRICTIVE_AIRSPACE_ARC = "SAFRURDAD100A      A00020     R N35242020E004275131N35195300E00412160001350706                                             265382605";

  @Test
  void testParseRestrictiveAirspace() {
    ArincRecord record = PARSER.parse(RESTRICTIVE_AIRSPACE).orElseThrow(AssertionError::new);
    ArincRestrictiveAirspaceLeg restrictiveAirspace = CONVERTER.apply(record).orElseThrow(AssertionError::new);
    ArincRestrictiveAirspaceLeg rebuilt = restrictiveAirspace.toBuilder().build();

    assertAll(
        () -> assertEquals(RecordType.S, restrictiveAirspace.recordType()),
        () -> assertEquals(CustomerAreaCode.USA, restrictiveAirspace.customerAreaCode()),
        () -> assertEquals(SectionCode.U, restrictiveAirspace.sectionCode()),
        () -> assertEquals("R", restrictiveAirspace.subSectionCode().get()),
        () -> assertEquals("K1", restrictiveAirspace.icaoRegion()),
        () -> assertEquals("N", restrictiveAirspace.restrictiveType()),
        () -> assertEquals("BOARDMAN", restrictiveAirspace.restrictiveAirspaceDesignation()),
        () -> assertEquals("A", restrictiveAirspace.multipleCode().get()),
        () -> assertEquals(Integer.valueOf(10), restrictiveAirspace.sequenceNumber()),
        () -> assertEquals("1", restrictiveAirspace.continuationRecordNumber().get()),
        () -> assertEquals(Level.L, restrictiveAirspace.level().get()),
        () -> assertTrue(restrictiveAirspace.timeCode().isEmpty()),
        () -> assertTrue(restrictiveAirspace.notam().isEmpty()),
        () -> assertEquals(BoundaryVia.G, restrictiveAirspace.boundaryVia().get()),
        () -> assertEquals(45.88305555555556, restrictiveAirspace.latitude().get()),
        () -> assertEquals(-119.51777777777778, restrictiveAirspace.longitude().get()),
        () -> assertTrue(restrictiveAirspace.arcOriginLatitude().isEmpty()),
        () -> assertTrue(restrictiveAirspace.arcOriginLongitude().isEmpty()),
        () -> assertTrue(restrictiveAirspace.arcDistance().isEmpty()),
        () -> assertTrue(restrictiveAirspace.arcBearing().isEmpty()),
        () -> assertEquals(4000.0, restrictiveAirspace.lowerLimit().get()),
        () -> assertEquals("M", restrictiveAirspace.lowerUnitIndicator().get()),
        () -> assertEquals(17999.0, restrictiveAirspace.upperLimit().get()),
        () -> assertEquals("M", restrictiveAirspace.upperUnitIndicator().get()),
        () -> assertEquals("BOARDMAN MOA", restrictiveAirspace.restrictiveAirspaceName().get()),
        () -> assertEquals(71417, restrictiveAirspace.fileRecordNumber()),
        () -> assertEquals("1703", restrictiveAirspace.cycleDate()),
        () -> assertEquals(restrictiveAirspace, rebuilt)
    );
  }

  @Test
  void testParseArc() {
    ArincRecord record = PARSER.parse(RESTRICTIVE_AIRSPACE_ARC).orElseThrow(AssertionError::new);
    assertTrue(VALIDATOR.test(record));
    ArincRestrictiveAirspaceLeg restrictiveAirspace = CONVERTER.apply(record).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(BoundaryVia.R, record.requiredField("boundaryVia")),
        () -> assertEquals(35.40561111111111, record.requiredField("latitude")),
        () -> assertEquals(4.464252777777778, record.requiredField("longitude")),
        () -> assertEquals(35.331388888888895, record.requiredField("arcOriginLatitude")),
        () -> assertEquals(4.204444444444444, record.requiredField("arcOriginLongitude")),
        () -> assertEquals(13.5, record.requiredField("arcDistance")),
        () -> assertEquals(70.6, record.requiredField("arcBearing")),
        () -> assertEquals(Optional.of(BoundaryVia.R), restrictiveAirspace.boundaryVia()),
        () -> assertEquals(Optional.of(13.5), restrictiveAirspace.arcDistance()),
        () -> assertEquals(Optional.of(70.6), restrictiveAirspace.arcBearing())
    );
  }
}
